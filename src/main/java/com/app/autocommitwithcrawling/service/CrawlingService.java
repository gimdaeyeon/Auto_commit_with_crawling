package com.app.autocommitwithcrawling.service;

import com.app.autocommitwithcrawling.domain.entity.CodingSolution;
import com.app.autocommitwithcrawling.domain.type.Site;
import com.app.autocommitwithcrawling.repository.CodingSolutionRepository;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchContextException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final CodingSolutionRepository codingSolutionRepository;
    private final FlexmarkHtmlConverter flexmarkHtmlConverter;

    @Value("${programmers.id}")
    private String programmersId;
    @Value("${programmers.password}")
    private String programmersPassword;

    public CodingSolution fetchAndCrateSolutionFromProgrammers() throws NoSuchContextException {
        programmersLogin();
        int problemNumber = findTargetProblemNumber();

        log.info("problemNumber : {}", problemNumber);

        String solutionUrl = "https://school.programmers.co.kr/learn/courses/30/lessons/" + problemNumber;
        String mySolutionUrl = solutionUrl + "/solution_groups?language=java&type=my";

        driver.get(solutionUrl);

        String title = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".challenge-title"))
        ).getText();

        String content = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".markdown.solarized-dark"))
        ).getAttribute("outerHTML");

        String level = "Lv" + webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".lesson-content"))
        ).getAttribute("data-challenge-level");

        driver.get(mySolutionUrl);

        String solutionCode = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".highlight .rouge-table"))
        ).getText();

        return CodingSolution.builder()
                .problemNumber(problemNumber)
                .problemTitle(title)
                .problemContent(flexmarkHtmlConverter.convert(content))
                .site(Site.PROGRAMMERS)
                .problemLink(solutionUrl)
                .solutionCode(solutionCode)
                .problemLevel(level)
                .build();
    }

    private int findTargetProblemNumber() throws NoSuchContextException {
        String problemListPageUrl = "https://school.programmers.co.kr/learn/challenges?order=recent&languages=java&page=1&statuses=solved";
        //       https://school.programmers.co.kr/learn/challenges?order=recent&page=1&statuses=solved
        int problemNumber = 0;
        boolean existsed = true;
        driver.get(problemListPageUrl);

        WebElement lastBtn = webDriverWait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.last"))
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
                log.info("problemNumber : {}", problemNumber);
                existsed = codingSolutionRepository.existsByProblemNumberAndSite(problemNumber, Site.PROGRAMMERS);
                log.info("href : {}", href);


//                db에 등록하지 않은 번호를 만나면 중단
                if (!existsed) break;
            }
            if ("1".equals(driver.findElement(By.cssSelector("button.iGiYtR")).getText())) {
                throw new NoSuchContextException("제출한 모든 코딩테스트 문제가 등록되었습니다.");
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

        WebElement idInput = webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='email'].FymRFM681OjzOdzor5nk"))
        );

        keyBoardInput(idInput, programmersId);

        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password'].FymRFM681OjzOdzor5nk"));
        keyBoardInput(passwordInput, programmersPassword);

        WebElement loginBtn = driver.findElement(By.cssSelector("button.Gosd7zfsHAxk1MOYSkL1"));
        loginBtn.click();


        webDriverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.uHasJ"))
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
