package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.domain.type.Site;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final CodingSolutionRepository codingSolutionRepository;

    @Value("${programmers.id}")
    private String programmersId;
    @Value("${programmers.password}")
    private String programmersPassword;

    public CodingSolution fetchAndCrateSolutionFromProgrammers() {
        programmersLogin();
        int problemNumber = findTargetProblemNumber();

        System.out.println("problemNumber = " + problemNumber);

        String solutionUrl = "https://school.programmers.co.kr/learn/courses/30/lessons/" + problemNumber;
        String mySolutionUrl = solutionUrl + "/solution_groups?language=java&type=my";

        driver.get(solutionUrl);

        String title = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".challenge-title"))
        ).getText();

        String content = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".markdown.solarized-dark"))
        ).getText();


        driver.get(mySolutionUrl);

        String solutionCode = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".highlight .rouge-code"))
        ).getText();

        System.out.println("solutionCode = " + solutionCode);

        driver.close();
        driver.quit();  //브라우저를 닫는 메소드

        return CodingSolution.builder()
                .problemNumber(problemNumber)
                .problemTitle(title)
                .problemContent(content)
                .site(Site.PROGRAMMERS)
                .problemLink(solutionUrl)
                .solutionCode(solutionCode)
                .build();
    }

    private int findTargetProblemNumber() {
        String problemListPageUrl = "https://school.programmers.co.kr/learn/challenges?order=recent&languages=java&page=1&statuses=solved";
        int problemNumber = 0;
        boolean existsed = true;
        driver.get(problemListPageUrl);

        WebElement lastBtn = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.last"))
        );
        List<WebElement> earlyElements = webDriverWait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("td.title"))
        );

        lastBtn.click();

        webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(earlyElements));

        do {
//            문제 위에 버튼이 클릭되고 화면이 로드되기 전에 밑의 요소를 가져와서 첫 페이지의 요소를 가져오고 indexOut 오류가 발생한다.
//            클릭 된 후 대기시간을 줘야한다.
            List<WebElement> elements = webDriverWait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("td.title"))
            );
            for (int i = elements.size() - 1; i >= 0; i--) {
                WebElement problemTd = elements.get(i);
                WebElement aTag = problemTd.findElement(By.cssSelector(".bookmark>a"));
                String href = aTag.getAttribute("href");
                problemNumber = Integer.parseInt(href.substring(href.lastIndexOf("/") + 1));
                System.out.println(problemNumber);
                existsed = codingSolutionRepository.existsByProblemNumberAndSite(problemNumber, Site.PROGRAMMERS);
                System.out.println(href);

//                db에 등록하지 않은 번호를 만나면 중단
                if (!existsed) break;
            }
//            등록하지 않은 문제를 만나지 못했을 때만 다음 페이지로 이동
            if (existsed) {
                driver.findElement(By.cssSelector("button.drGwOp.prev")).click();
                webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(elements));
            }
        } while (existsed);

        return problemNumber;
    }

    private void programmersLogin() {
        String loginUrl = "https://programmers.co.kr/account/sign_in";
        driver.get(loginUrl);
        try {
            webDriverWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".G7QZ1shWGosDZ1csHsNt .FymRFM681OjzOdzor5nk"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebElement idInput = driver.findElement(By.cssSelector("input[type='email'].FymRFM681OjzOdzor5nk"));
        keyBoardInput(idInput, programmersId);

        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password'].FymRFM681OjzOdzor5nk"));
        keyBoardInput(passwordInput, programmersPassword);

        WebElement loginBtn = driver.findElement(By.cssSelector(".itAWTII94uCyf9uUgREi"));
        loginBtn.click();

        webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.UtilMenustyle__ProfilePopupButton-sc-2sjysx-3.uHasJ"))
        );
    }

    //    사람같은 입력을 위한 메서드
    private void keyBoardInput(WebElement inputElement, String inputValue) {
        String[] valueArr = inputValue.split("");
        for (int i = 0; i < inputValue.length(); i++) {
            inputElement.sendKeys(valueArr[i]);
        }
    }


}
