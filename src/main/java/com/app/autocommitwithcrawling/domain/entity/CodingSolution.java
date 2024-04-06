package com.app.autocommitwithcrawling.domain.entity;

import com.app.autocommitwithcrawling.domain.base.Period;
import com.app.autocommitwithcrawling.domain.type.Site;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_CODING_SOLUTION", indexes = @Index(name = "IDX_SOLUTION_SITE_AND_NUMBER", columnList = "problemNumber, site"))
@SequenceGenerator(name = "SEQ_CODING_SOLUTION_GENERATOR", sequenceName = "SEQ_CODING_SOLUTION", allocationSize = 1)
@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodingSolution extends Period {
    @Id
    @GeneratedValue(generator = "SEQ_CODING_SOLUTION_GENERATOR")
    @Column(name = "CODING_SOLUTION_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Site site;
    private Integer problemNumber;
    private String problemTitle;
    @Column(length = 3000)
    private String problemContent;
    @Column(length = 3000)
    private String solutionCode;
    private String problemLink;
    private String problemLevel;

    @Builder
    public CodingSolution(Long id, Site site, Integer problemNumber, String problemTitle, String problemContent, String solutionCode, String problemLink, String problemLevel) {
        this.id = id;
        this.site = site;
        this.problemNumber = problemNumber;
        this.problemTitle = problemTitle;
        this.problemContent = problemContent;
        this.solutionCode = solutionCode;
        this.problemLink = problemLink;
        this.problemLevel = problemLevel;
    }
}













