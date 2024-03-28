package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.type.Site;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CodingSolutionService {
    private final CodingSolutionRepository codingSolutionRepository;

    public boolean existsProblem(int problemNumber, Site site){
        return codingSolutionRepository.existsByProblemNumberAndSite(problemNumber,site);
    }

}
