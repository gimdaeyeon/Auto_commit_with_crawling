package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
public class GitService {
    private final File GIT_DIR;
    private final UsernamePasswordCredentialsProvider credentialsProvider;

//    todo 해당 프로젝트가 실행될 때 git 프로젝트로 등록하기

    public GitService(UsernamePasswordCredentialsProvider credentialsProvider,
                      @Value("${git.repoUri}") String gitRepoUri) throws GitAPIException, URISyntaxException {

        this.credentialsProvider = credentialsProvider;
        this.GIT_DIR = new File(System.getProperty("user.dir"));
        if (!isGitRepoDir()) {
            Git git = Git.init().setDirectory(GIT_DIR).call();
            RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName("origin");
            remoteAddCommand.setUri(new URIish(gitRepoUri));
            remoteAddCommand.call();
        }
    }


    public void gitCommitAndPush(CodingSolution solution) {
        try (Git git = Git.open(GIT_DIR)) {

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

    private boolean isGitRepoDir() {
        try {
            File currentDir = new File(".").getCanonicalFile(); // 현재 디렉토리의 정규 경로를 가져옵니다.
            File gitDir = new File(currentDir, ".git"); // 현재 디렉토리 내의 .git 폴더를 찾습니다.
            return gitDir.exists() && gitDir.isDirectory();
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
