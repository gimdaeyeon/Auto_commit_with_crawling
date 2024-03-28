package com.app.autocommitwithcrawling.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class MdFileServiceTest {


    @Test
    void stringTest(){
        String template = """
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

        System.out.printf(template,1234,"문제 제목","Lv2","naver.com","잘 풀어보쇼","System.out.println(\"hello\")");
    }

    @Test
    void fileTest() throws IOException {
        File f = new File("programmers"+File.separator+"lv1");
        if(!f.exists()){
            f.mkdirs();
        }
        File file = new File(f,"test.txt");
        file.createNewFile();
    }
}