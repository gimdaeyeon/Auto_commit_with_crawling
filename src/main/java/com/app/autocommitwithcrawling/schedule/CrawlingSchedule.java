package com.app.autocommitwithcrawling.schedule;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import com.app.autocommitwithcrawling.service.CrawlingService;
import com.app.autocommitwithcrawling.service.GitService;
import com.app.autocommitwithcrawling.service.MailService;
import com.app.autocommitwithcrawling.service.MdFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Slf4j
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

    @Scheduled(cron = "0 0 2,14 * * *",zone = "Asia/Seoul")
    public void doScheduleProcess() {
//        각 해당 날짜에 이미 등록된 정보가 있으면 실행x
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        if(codingSolutionRepository.existsByCreatedDateAfter(today)){
            return ;
        }

        CodingSolution codingSolution = null;
        try {
            codingSolution = crawlingService.fetchAndCrateSolutionFromProgrammers();
            mdFileService.createAndWriteMdFile(codingSolution);
//        커밋, 푸쉬하는 부분 잠시 주석
            gitService.gitCommitAndPush(codingSolution);
            codingSolutionRepository.save(codingSolution);
        } catch (Exception e) {
            mailService.sendEmail(accountEmail,"자동 커밋프로세스 오류",e.getMessage());
        }finally {
            driver.close();
            driver.quit();
        }
    }


}
