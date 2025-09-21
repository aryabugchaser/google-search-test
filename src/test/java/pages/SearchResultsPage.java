package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchResultsPage extends BasePage {

    private final By resultsContainer = By.id("search");
    private final By resultTitles     = By.cssSelector("h3");

    public SearchResultsPage(WebDriver driver) { super(driver); }

    public boolean isResultsVisible() {
        return !driver.findElements(resultsContainer).isEmpty()
                || driver.findElements(resultTitles).size() > 0
                || driver.getCurrentUrl().contains("/search");
    }

    public int resultCountRough() {
        return driver.findElements(resultTitles).size();
    }
}
