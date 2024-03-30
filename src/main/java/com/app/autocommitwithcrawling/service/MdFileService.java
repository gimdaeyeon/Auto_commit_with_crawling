package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MdFileService {
    private final FlexmarkHtmlConverter flexmarkHtmlConverter;
    private final String FILE_EXTENSION = ".md";
    private final String TEMPLATE = """
            # %d. %s(%s)
            ### 문제 링크 
            %s
            ### 문제 내용
            %s
            ### 제출 답안
            ```java
            %s
            ```
            """;

    public void createAndWriteMdFile(CodingSolution codingSolution) throws IOException {
        File file = createFile(codingSolution);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            String fileContent = formatSolutionMarkdown(codingSolution);
            bw.write(fileContent);
        }

    }

    private File createFile(CodingSolution solution) throws IOException {
        File dir = new File(solution.getSite().name().toLowerCase()+File.separator+solution.getProblemLevel());
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName = solution.getProblemNumber()+"_"+solution.getProblemTitle().replaceAll(" ","_")+FILE_EXTENSION;
        File mdFile = new File(dir,fileName);
        mdFile.createNewFile();
        return mdFile;
    }

    private String formatSolutionMarkdown(CodingSolution solution){
        return String.format(TEMPLATE,solution.getProblemNumber()
                ,solution.getProblemTitle(),solution.getProblemLevel(),
                solution.getProblemLink(),
                flexmarkHtmlConverter.convert(solution.getProblemContent()),
                solution.getSolutionCode());
    }


}
