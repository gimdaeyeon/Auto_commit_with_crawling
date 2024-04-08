package com.app.autocommitwithcrawling.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RenameBranchCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class GitServiceTest {


    @Test
    @DisplayName("깃 리포지토리 확인 테스트")
    void gitRepoTest() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .readEnvironment()//환경변수를 사용하여 설정 읽기
                    .findGitDir()// 현재 디렉토리에서 시작하여 상위 디렉토리를 검색하여 .git 디렉토리 위치 찾기
                    .build();
            if (repository.getDirectory() != null) {
                System.out.println("Git 리포 맞음");
            } else {
                System.out.println("ㄴㄴ아님ㅋ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("깃 리포지토리 확인 테스트2")
    void gitRepoTest2() {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            File currentDir = new File(".").getCanonicalFile(); // 현재 디렉토리의 정규 경로를 가져옵니다.
            File gitDir = new File(currentDir, ".git"); // 현재 디렉토리 내의 .git 폴더를 찾습니다.

            if (gitDir.exists() && gitDir.isDirectory()) {
                // .git 폴더가 존재하는 경우, 저장소를 구성합니다.
                Repository repository = builder.setGitDir(gitDir)
                        .readEnvironment() // 시스템 환경을 읽어옵니다.
                        .findGitDir() // 가장 가까운 .git 폴더를 찾습니다.
                        .build();
                System.out.println("현재 디렉토리는 Git 저장소입니다. Git 디렉토리 경로: " + repository.getDirectory());
            } else {
                System.out.println("현재 디렉토리는 Git 저장소가 아닙니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("저장소를 찾거나 구성하는 중 에러가 발생했습니다.");
        }
    }

    @Test
    @DisplayName("git브랜치 이름 변경 테스트")
    void gitBranchNameTest() throws Exception {
        Git git = Git.open(new File(System.getProperty("user.dir")));

        RenameBranchCommand renameBranchCommand = git.branchRename();
        String oldBranchName = renameBranchCommand.getRepository().getBranch();
        renameBranchCommand.setOldName(oldBranchName);
        renameBranchCommand.setNewName("main");
        renameBranchCommand.call();
    }

}