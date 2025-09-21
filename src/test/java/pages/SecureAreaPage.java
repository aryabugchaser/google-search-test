package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SecureAreaPage extends BasePage {

    private final By successFlash = By.id("flash");
    private final By header       = By.tagName("h2"); // "Secure Area"

    public SecureAreaPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAtSecureArea() {
        return driver.getCurrentUrl().contains("/secure");
    }

    public String getSuccessMessage() {
        return text(successFlash);
    }

    public String getHeader() {
        return text(header);
    }
}
