package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class SearchAutomationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Google
    private static final String HOME = "https://www.google.com/ncr"; // ncr = no country redirect
    private static final By SEARCH_INPUT = By.name("q");
    private static final By RESULTS_CONTAINER = By.id("search");     // main results wrapper
    private static final By RESULT_STATS = By.id("result-stats");    // sometimes present

    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless=new"); // optional
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(HOME);
        acceptConsentIfPresent();
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (driver != null) driver.quit();
    }

    /** Scenario: Search with Special Characters
     *  Steps:
     *   1) Open Google homepage
     *   2) Enter "@%*!@#" in the search bar
     *   3) Click the search button (we press ENTER, which is how Google submits)
     *  Expected:
     *   - Search results are displayed and no error occurs
     */
    @Test
    public void searchWithSpecialCharacters() {
        String query = "@%*!@#";
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        box.clear();
        box.sendKeys(query);
        box.sendKeys(Keys.ENTER); // submit

        // Results page should load: wait for results container or result-stats
        boolean resultsVisible = wait.until(d -> {
            try {
                return d.findElement(RESULTS_CONTAINER).isDisplayed()
                        || d.findElement(RESULT_STATS).isDisplayed();
            } catch (NoSuchElementException ignored) { return false; }
        });
        Assert.assertTrue(resultsVisible, "Expected results to be visible for special characters query.");

        // Basic sanity: title contains query (Google behavior)
        Assert.assertTrue(driver.getTitle().toLowerCase().contains(query.toLowerCase()),
                "Page title should contain the query.");
    }

    /** Scenario: Empty Search
     *  Steps:
     *   1) Open Google homepage
     *   2) Leave the search bar empty
     *   3) Click the search button
     *  Expected:
     *   - No results page appears OR user is prompted to enter a term.
     *  Implementation detail:
     *   - On Google, pressing ENTER on an empty box generally keeps you on the homepage.
     */
    @Test
    public void emptySearchShouldStayOnHomepage() {
        // Ensure input is present and empty
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        box.clear();

        String urlBefore = driver.getCurrentUrl();
        box.sendKeys(Keys.ENTER); // try to submit empty

        // Give the page a moment; results should NOT appear
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(RESULTS_CONTAINER));
            Assert.fail("Results container should not appear for an empty search.");
        } catch (TimeoutException ignored) {
            // expected: no results container within 2s
        }

        String urlAfter = driver.getCurrentUrl();
        Assert.assertTrue(urlAfter.equals(urlBefore) || urlAfter.contains("google.com"),
                "Should remain on the Google home (no results page) after empty search.");
    }

    /** Accept Google consent dialog if it appears (varies by region/UX). */
    private void acceptConsentIfPresent() {
        try {
            // Newer consent: button text like "I agree", "Accept all"
            // Sometimes inside an iframe; try switch if needed.
            // 1) Try direct
            By agreeBtn = By.xpath("//button[.//div[text()='I agree'] or contains(.,'I agree') or contains(.,'Accept all')]");
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(agreeBtn),
                    ExpectedConditions.presenceOfElementLocated(By.tagName("iframe"))
            ));

            // If we’re in an iframe, switch into the first visible one and try again
            if (driver.findElements(agreeBtn).isEmpty()) {
                for (WebElement frame : driver.findElements(By.tagName("iframe"))) {
                    try {
                        driver.switchTo().frame(frame);
                        if (!driver.findElements(agreeBtn).isEmpty()) break;
                        driver.switchTo().defaultContent();
                    } catch (Exception ignored) {
                        driver.switchTo().defaultContent();
                    }
                }
            }

            if (!driver.findElements(agreeBtn).isEmpty()) {
                driver.findElement(agreeBtn).click();
                driver.switchTo().defaultContent();
            } else {
                driver.switchTo().defaultContent();
            }
        } catch (Exception ignored) {
            // No consent shown—carry on.
            driver.switchTo().defaultContent();
        }
    }
}

