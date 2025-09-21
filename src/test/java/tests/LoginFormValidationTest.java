package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginFormValidationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1280, 800));

        // ✅ navigate to your login page BEFORE the tests run
        driver.get("https://account.google.com/login"); // <-- change this to the real login page

        // ✅ create wait once
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void validLoginTest() {
        // ✅ wait for element instead of direct findElement
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        username.sendKeys("myUser");

        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("myPass");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        loginButton.click();

        // add your assertions here
    }

    @Test
    public void invalidLoginTest() {
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        username.sendKeys("wrongUser");

        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("wrongPass");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        loginButton.click();

        // add assertion for error message
    }
}
