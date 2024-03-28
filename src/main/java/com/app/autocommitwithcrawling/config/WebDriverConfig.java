package com.app.autocommitwithcrawling.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class WebDriverConfig {
    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");   //원격 연결 허용
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
//        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");         //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        return new ChromeDriver(options);
    }

    @Bean
    public WebDriverWait WebDriverWait() {
        return new WebDriverWait(webDriver(), Duration.ofSeconds(10));
    }

}
