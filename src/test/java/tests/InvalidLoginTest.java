package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
public class InvalidLoginTest {

    WebDriver driver;


    @BeforeMethod
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
        driver.manage().window().maximize();
    }

    @Test
    public void testValidLogin() {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement successMsg = driver.findElement(By.id("flash"));
        Assert.assertTrue(successMsg.getText().contains("You logged into a secure area!"),
                "Success message not displayed");

        Assert.assertTrue(driver.getCurrentUrl().contains("/secure"),
                "User is not redirected to secure page after login");
    }


    @Test
    public void testInvalidLogin() {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("wrongUser");
        driver.findElement(By.id("password")).sendKeys("wrongPass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMsg = driver.findElement(By.id("flash"));
        Assert.assertTrue(errorMsg.getText().contains("Your username is invalid!"),
                "Error message not displayed correctly");

        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should remain on login page after invalid login");
    }



    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
