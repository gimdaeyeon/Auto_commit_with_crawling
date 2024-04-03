package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GitService {
    private final File gitDir = new File(System.getProperty("user.dir"));
    private final UsernamePasswordCredentialsProvider credentialsProvider;

    public void gitCommitAndPush(CodingSolution solution) {
        try (Git git = Git.open(gitDir)) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage(createGitCommitMessage(solution)).call();
            git.push()
                    .setCredentialsProvider(credentialsProvider)
                    .setRemote("origin")
                    .setRefSpecs(new RefSpec("main"))
                    .call();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private String createGitCommitMessage(CodingSolution solution) {
//        커밋 메세지 형식 : {사이트} - [{난이도}] {문제 제목}({문제 번호}번) 등록
//                    예 : PROGRAMMERS - [Lv1] 가운데 글자 가져오기(12903번) 등록
        return solution.getSite().name() +
               " - [" +
               solution.getProblemLevel() +
               "] " +
               solution.getProblemTitle() +
               '(' +
               solution.getProblemNumber() +
               "번) 등록";
    }


}
