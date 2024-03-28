package com.app.autocommitwithcrawling.schedule;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import com.app.autocommitwithcrawling.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CrawlingSchedule {
    private final CrawlingService crawlingService;
    private final CodingSolutionRepository codingSolutionRepository;

    @Scheduled(fixedDelay = 100000)
    public void scheduleTest(){
        CodingSolution codingSolution = crawlingService.fetchAndCrateSolutionFromProgrammers();
        codingSolutionRepository.save(codingSolution);
    }


}
