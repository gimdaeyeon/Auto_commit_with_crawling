package com.app.autocommitwithcrawling.schedule;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import com.app.autocommitwithcrawling.service.CrawlingService;
import com.app.autocommitwithcrawling.service.GitService;
import com.app.autocommitwithcrawling.service.MdFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CrawlingSchedule {
    private final CrawlingService crawlingService;
    private final MdFileService mdFileService;
    private final CodingSolutionRepository codingSolutionRepository;
    private final GitService gitService;

    @Scheduled(fixedDelay = 100000)
    public void doScheduleProcess(){
        CodingSolution codingSolution = crawlingService.fetchAndCrateSolutionFromProgrammers();
        try {
            mdFileService.createAndWriteMdFile(codingSolution);
        } catch (IOException e) {
            codingSolutionRepository.delete(codingSolution);
            doScheduleProcess();
        }
        gitService.gitCommitAndPush(codingSolution);
    }


}
