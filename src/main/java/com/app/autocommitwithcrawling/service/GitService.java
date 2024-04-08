package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.RenameBranchCommand;
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
    private final String MAIN = "main";
    private final String ORIGIN = "origin";

    public GitService(UsernamePasswordCredentialsProvider credentialsProvider,
                      @Value("${git.repoUri}") String gitRepoUri) {
        this.credentialsProvider = credentialsProvider;
        this.GIT_DIR = new File(System.getProperty("user.dir"));
        if (!isGitRepoDir()) {
            RemoteAddCommand remoteAddCommand;
            try (Git git = Git.init().setDirectory(GIT_DIR).call()) {
                remoteAddCommand = git.remoteAdd();
                remoteAddCommand.setName(ORIGIN);
                remoteAddCommand.setUri(new URIish(gitRepoUri));
                remoteAddCommand.call();

                git.add().addFilepattern(".").call();
                git.commit().setMessage("Init: 초기 컨테이너 가동").call();

                RenameBranchCommand renameBranchCommand = git.branchRename();
                String oldBranchName = renameBranchCommand.getRepository().getBranch();
                if (!MAIN.equals(oldBranchName)) {
                    renameBranchCommand
                            .setOldName(oldBranchName)
                            .setNewName(MAIN)
                            .call();
                }
            } catch (GitAPIException | URISyntaxException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }


    public void gitCommitAndPush(CodingSolution solution) {
        try (Git git = Git.open(GIT_DIR)) {

            git.add().addFilepattern(".").call();
            git.commit().setMessage(createGitCommitMessage(solution)).call();
            git.pull().setRemoteBranchName(MAIN)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
            git.push()
                    .setCredentialsProvider(credentialsProvider)
                    .setRemote(ORIGIN)
                    .setRefSpecs(new RefSpec(MAIN))
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
