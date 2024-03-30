package com.app.autocommitwithcrawling.repository;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.domain.type.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CodingSolutionRepository extends JpaRepository<CodingSolution,Long> {
    boolean existsByProblemNumberAndSite(Integer problemNumber, Site site);
    boolean existsByCreatedDateAfter(LocalDateTime today);

}
