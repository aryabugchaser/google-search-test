package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginFormValidationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Demo login page (stable)
    private static final String BASE_URL = "https://the-internet.herokuapp.com/login";

    // Locators
    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BTN = By.cssSelector("button[type='submit']");
    private static final By FLASH = By.id("flash");
    private static final By SECURE_HEADER = By.cssSelector("div.example h2");

    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        // options.addArguments("--headless=new"); // optional
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test(description = "Valid creds should reach Secure Area and show success flash")
    public void validLoginTest() {
        driver.findElement(USERNAME).sendKeys("tomsmith");
        driver.findElement(PASSWORD).sendKeys("SuperSecretPassword!");
        driver.findElement(LOGIN_BTN).click();

        wait.until(ExpectedConditions.urlContains("/secure"));
        Assert.assertEquals(driver.findElement(SECURE_HEADER).getText().trim(), "Secure Area");

        String flash = driver.findElement(FLASH).getText();
        Assert.assertTrue(flash.contains("You logged into a secure area!"),
                "Expected success message, got: " + flash);
    }

    @Test(description = "Invalid creds should keep user on login and show invalid flash")
    public void invalidLoginTest() {
        driver.findElement(USERNAME).sendKeys("wrongUser");
        driver.findElement(PASSWORD).sendKeys("wrongPass");
        driver.findElement(LOGIN_BTN).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(FLASH));
        String flash = driver.findElement(FLASH).getText().toLowerCase();
        Assert.assertTrue(flash.contains("your username is invalid") || flash.contains("invalid"),
                "Expected invalid-credentials message, got: " + flash);
    }

    @Test(description = "Empty password shows an error (demo returns generic invalid)")
    public void emptyFieldsLoginTest() {
        driver.findElement(USERNAME).sendKeys("tomsmith");
        driver.findElement(PASSWORD).clear();
        driver.findElement(LOGIN_BTN).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(FLASH));
        String flash = driver.findElement(FLASH).getText().toLowerCase();
        Assert.assertTrue(flash.contains("invalid"),
                "Expected validation/invalid message, got: " + flash);
    }
}
