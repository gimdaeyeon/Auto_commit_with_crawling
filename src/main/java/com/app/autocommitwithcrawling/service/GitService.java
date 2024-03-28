package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
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

    private String createGitCommitMessage(CodingSolution solution){
//        todo 기능 정상동작 확인하면 커밋메세지에서 Test: 부분 지우기
        return"Test: "+ solution.getSite().name() +
               " - [" +
               solution.getProblemLevel() +
               "] " +
               solution.getProblemTitle() +
               '(' +
               solution.getProblemNumber() +
               "번) 등록";
    }


}
