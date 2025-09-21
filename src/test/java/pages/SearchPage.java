package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;

public class SearchPage extends BasePage {

    private final By searchBox = By.name("q");
    // Google consent button (may or may not appear)
    private final By consentBtn = By.id("L2AGLb");

    public SearchPage(WebDriver driver) { super(driver); }

    public SearchPage open() {
        driver.get("https://www.google.com/ncr");
        // best-effort: dismiss consent if it shows up
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(searchBox),
                    ExpectedConditions.elementToBeClickable(consentBtn)
            ));
            if (!driver.findElements(consentBtn).isEmpty()) {
                driver.findElement(consentBtn).click();
            }
        } catch (Exception ignored) {}
        return this;
    }

    public SearchResultsPage search(String query) {
        type(searchBox, query);
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }

    public SearchPage submitEmpty() {
        driver.findElement(searchBox).click();
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
        return this; // stays on home
    }

    public boolean isHome() {
        String url = driver.getCurrentUrl();
        return url.contains("google.com") && !url.contains("/search");
    }
}
