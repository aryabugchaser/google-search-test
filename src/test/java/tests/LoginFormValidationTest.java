package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
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

        // ✅ correct Google login page
        driver.get("https://accounts.google.com/");

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
        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("identifier"))
        );
        emailField.sendKeys("fakeuser@gmail.com");

        WebElement nextButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("identifierNext"))
        );
        nextButton.click();

        // Assert password field appears
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("Passwd"))
        );
        Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible after clicking Next");
    }

    @Test
    public void emptyFieldsLoginTest() {
        WebElement nextButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("identifierNext"))
        );
        nextButton.click();

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.o6cuMc"))
        );
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should appear for empty username");
    }
}
