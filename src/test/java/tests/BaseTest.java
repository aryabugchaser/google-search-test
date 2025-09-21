package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import java.time.Duration;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        org.openqa.selenium.chrome.ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
        if (System.getenv("CI") != null) { // on GitHub Actions
            options.addArguments("--headless=new",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--window-size=1920,1080");
        }
        driver = new org.openqa.selenium.chrome.ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(8));
        driver.manage().window().maximize();
    }

        @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && result.getStatus() == ITestResult.FAILURE) {
                saveScreenshot(result.getMethod().getMethodName());
            }
        } catch (Exception ignored) {}
        if (driver != null) driver.quit();
    }

    // Reusable full-page screenshot helper
    protected Path saveScreenshot(String name) throws Exception {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Path dir = Paths.get("screenshots");
        Files.createDirectories(dir);
        Path dest = dir.resolve(name + "_" + ts + ".png");
        Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

        String uri = dest.toUri().toString();
        Reporter.log("<a href='" + uri + "'>Open screenshot</a><br/><img src='" + uri + "' width='480'/>", true);
        return dest;
    }
}
