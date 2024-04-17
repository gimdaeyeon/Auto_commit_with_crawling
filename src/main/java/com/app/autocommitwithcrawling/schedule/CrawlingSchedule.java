package com.app.autocommitwithcrawling.schedule;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import com.app.autocommitwithcrawling.service.CrawlingService;
import com.app.autocommitwithcrawling.service.GitService;
import com.app.autocommitwithcrawling.service.MailService;
import com.app.autocommitwithcrawling.service.MdFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class CrawlingSchedule {
    private final CrawlingService crawlingService;
    private final MdFileService mdFileService;
    private final CodingSolutionRepository codingSolutionRepository;
    private final GitService gitService;
    private final MailService mailService;
    @Value("${programmers.id}")
    private String accountEmail;
    private final WebDriver driver;

//    @Scheduled(cron = "0 0 2,14 * * *",zone = "Asia/Seoul")
//    @Scheduled(fixedDelay = 100000)
    public void doScheduleProcess() {
//        각 해당 날짜에 이미 등록된 정보가 있으면 실행x
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        if (codingSolutionRepository.existsByCreatedDateAfter(today)) {
            return;
        }

        loggingDriverVersion();
        CodingSolution codingSolution = null;
        try {
            codingSolution = crawlingService.fetchAndCrateSolutionFromProgrammers();
            log.info("codingSolution : {}", codingSolution);
            mdFileService.createAndWriteMdFile(codingSolution);
            gitService.gitCommitAndPush(codingSolution);
            codingSolutionRepository.save(codingSolution);
        } catch (Exception e) {
            log.error(e.getMessage());
            mailService.sendEmail(accountEmail, "자동 커밋프로세스 오류", e.getMessage());
        }finally {
            driver.close();
            driver.quit();  //브라우저를 닫는 메소드
        }
    }

    private void loggingDriverVersion() {
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        String browserVersion = caps.getBrowserVersion();
        String browserName = caps.getBrowserName();
        log.info("browserVersion : {}", browserVersion);
        log.info("browserName : {}", browserName);
    }


}
