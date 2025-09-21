package tests;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SecureAreaPage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginTests extends BaseTest {

    @DataProvider(name = "invalidCreds")
    public Object[][] invalidCreds() {
        return new Object[][]{
                {"wrongUser", "wrongPass"},
                {"tomsmith", "wrongPass"},
                {"", "SuperSecretPassword!"},
                {"tomsmith", ""},
                {"", ""}
        };
    }
    @Test
    public void force_screenshot_now() throws Exception {
        new pages.LoginPage(driver).open();
        // TAKE the screenshot now
        saveScreenshot("login_page_loaded");
    }

    @Test(dataProvider = "invalidCreds")
    public void invalidLogin_dataDriven(String user, String pass) {
        LoginPage login = new LoginPage(driver).open().loginInvalid(user, pass);
        Assert.assertTrue(login.isAtLoginPage(), "Should remain on /login after invalid login");
        Assert.assertTrue(login.getFlashMessage().toLowerCase().contains("invalid"),
                "Invalid credentials message should be shown");
    }

    @Test
    public void validLogin_shouldGoToSecureArea() {
        LoginPage login = new LoginPage(driver).open();
        SecureAreaPage secure = login.loginValid("tomsmith", "SuperSecretPassword!");
        Assert.assertTrue(secure.isAtSecureArea(), "Should be in /secure after valid login");
        Assert.assertTrue(secure.getSuccessMessage().contains("You logged into a secure area!"),
                "Success message should be shown");
    }

    @Test
    public void invalidLogin_shouldShowErrorAndStayOnLoginPage() {
        LoginPage login = new LoginPage(driver).open().loginInvalid("wrongUser", "wrongPass");
        Assert.assertTrue(login.isAtLoginPage(), "Should remain on /login after invalid login");
        Assert.assertTrue(login.getFlashMessage().contains("Your username is invalid!"),
                "Invalid username message should be shown");
    }

    // Optional: take a full-page screenshot during the test
    @Test
    public void demo_screenshot_mid_test() throws Exception {
        new LoginPage(driver).open();
        saveScreenshot("login_page_loaded");
    }

    // Full-page screenshot helper (kept here for you; later move to BaseTest to reuse everywhere)
    protected Path saveScreenshot(String name) throws Exception {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Path dir = Paths.get("screenshots");
        Files.createDirectories(dir);
        Path dest = dir.resolve(name + "_" + ts + ".png");
        Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

        // clickable link in emailable-report.html
        String link = dest.toAbsolutePath().toString().replace("\\", "/");
        Reporter.log("📸 Screenshot: <a href='file:///" + link + "'>open</a>", true);

        System.out.println("📸 Screenshot saved: " + dest.toAbsolutePath());
        return dest;
    }
}
