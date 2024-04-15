package com.app.autocommitwithcrawling.config;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LibraryConfig {
//    @Value("${chrome.driver}")
//    String driverPath;

    @Bean
    public WebDriver webDriver() {
//        System.setProperty("webdriver.chrome.driver",driverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");   //원격 연결 허용
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("--headless=new");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");         //gpu 비활성화
        options.addArguments("--no-sandbox"); //Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        return new ChromeDriver(options);
    }

    @Bean
    public WebDriverWait WebDriverWait() {
        return new WebDriverWait(webDriver(), Duration.ofMinutes(1L));
    }

    @Bean
    public FlexmarkHtmlConverter flexmarkHtmlConverter() {
        return FlexmarkHtmlConverter.builder().build();
    }

    @Bean
    public UsernamePasswordCredentialsProvider
    usernamePasswordCredentialsProvider(
            @Value("${git.userName}") String userName,
            @Value("${git.accessToken}") String accessToken) {
        return new UsernamePasswordCredentialsProvider(userName, accessToken);
    }


}
