package com.app.autocommitwithcrawling.service;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class GitServiceTest {


    @Test
    @DisplayName("깃 리포지토리 확인 테스트")
    void gitRepoTest(){
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .readEnvironment()//환경변수를 사용하여 설정 읽기
                    .findGitDir()// 현재 디렉토리에서 시작하여 상위 디렉토리를 검색하여 .git 디렉토리 위치 찾기
                    .build();
            if(repository.getDirectory()!=null) {
                System.out.println("Git 리포 맞음");
            }else {
                System.out.println("ㄴㄴ아님ㅋ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}