package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SearchPage;
import pages.SearchResultsPage;

public class SearchAutomationTest extends BaseTest {

    @Test
    public void searchWithSpecialCharacters_shouldNotCrash() {
        SearchPage search = new SearchPage(driver).open();
        SearchResultsPage results = search.search("@%*!@#");
        Assert.assertTrue(results.isResultsVisible(),
                "Search results page should load for special characters.");
    }

    @Test
    public void emptySearch_shouldStayOnHomeOrPrompt() {
        SearchPage search = new SearchPage(driver).open().submitEmpty();
        Assert.assertTrue(search.isHome(),
                "User should remain on Google home when submitting empty search.");
    }
}