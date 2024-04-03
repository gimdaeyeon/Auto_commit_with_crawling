package com.app.autocommitwithcrawling.controller;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final CodingSolutionRepository codingSolutionRepository;

    @GetMapping("/list")
    public List<CodingSolution> codingSolutionList() {
        return codingSolutionRepository.findAll();
    }

}
