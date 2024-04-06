package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitService {
    private final File gitDir = new File(System.getProperty("user.dir"));
    private final UsernamePasswordCredentialsProvider credentialsProvider;

//    todo 해당 프로젝트가 실행될 때 git 프로젝트로 등록하기

    //    public GitService(UsernamePasswordCredentialsProvider credentialsProvider) {
//        this.credentialsProvider = credentialsProvider;
//        this.gitDir  = new File(System.getProperty("user.dir"));
//    }


    public void gitCommitAndPush(CodingSolution solution) {
        try (Git git = Git.open(gitDir)) {

            git.add().addFilepattern(".").call();
            git.commit().setMessage(createGitCommitMessage(solution)).call();
            git.pull().setRemoteBranchName("main")
                            .setCredentialsProvider(credentialsProvider)
                                    .call();
            git.push()
                    .setCredentialsProvider(credentialsProvider)
                    .setRemote("origin")
                    .setRefSpecs(new RefSpec("main"))
                    .call();
        } catch (IOException | GitAPIException e) {
            log.info("e.getMessage() : {}", e.getMessage());
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
