package org.lds.mediafinder.modules;

import java.util.List;
import org.lds.mediafinder.constants.AssetType;
import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.constants.SortOption;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

/**
 * Houses all search methods.
 * @author Allen Sudweeks
 */
public class Module_Search extends ModuleMaster {
    
    private int endResultCount;
    
    public Module_Search(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Conducts a search for the specified text, and waits for the loading 
     * process to complete.
     * @param text
     * @throws TestException 
     */
    public void searchFor(String text) throws TestException {
        System.out.print("Searching for \"" + text + "\"... ");
        if (text != null && !text.equals("")) {
            waitForElementToBeVisible(XPath.srchSearchField);
            sendKeysToElement(XPath.srchSearchField, text);
            clickElement(XPath.srchSearchButton);
            waitForLoadingIndicatorToDisappear();
            endResultCount = Integer.parseInt(driver.findElement(By.xpath(XPath.srchResultsCount)).getText().trim().replace(" Results for " + text, ""));
            System.out.println("Done.");
        }
    }
    
    /**
     * Searches the page for any error message caused by the search text.
     * @throws TestException 
     */
    public void checkForError() throws TestException {
        System.out.println("Searching page for error message... ");
        if (!driver.getPageSource().contains("error") || driver.getPageSource().contains("Error")) {
            System.out.println("Done.");
        } else {
            throw new TestException("Error check failed");
        }
    }
    
    /**
     * Checks that the specified number of results (scrolls until the quantity is
     * met) are of the specified asset type.
     * @param quantityOfResults
     * @param type
     * @throws TestException 
     */
    public void checkResultsAssetType(int quantityOfResults, AssetType type) throws TestException {
        System.out.print("Checking that " + quantityOfResults + " results are of the "
                + type + " type... ");
        //Retrieve results
        List<WebElement> results = driver.findElements(By.xpath(XPath.resGenericResult));
        //Until the quantity is met, scroll down and wait for load 
        int lastSize;
        while (results.size() < quantityOfResults) {
            scrollDown();
            waitForLoadingIndicatorToDisappear();           
            //Record last result count before refreshing list of results
            lastSize = results.size();
            results = driver.findElements(By.xpath(XPath.resGenericResult));
            //If result count hasn't changed, break the loop
            if (lastSize == results.size()) {
                break;
            }
        }
        //Iterate through results and check asset type
        Boolean valid = false;
        WebElement failedResult = null;
        for (WebElement myResult : results) {
            if (type.toString().equalsIgnoreCase(myResult.getAttribute("data-qa-assettype"))) {
                valid = true;
            } else {
                failedResult = myResult;
                break;
            }
        }
        if (valid) {
            System.out.println("Done.");
        } else {
            throw new TestException("Result with external id '" + failedResult.getAttribute("data-qa-externalid")
                    + "' is not of the type " + type + ".");
        }
    }
    
    /**
     * BUG IN WEBDRIVER: WebElement.getCSSValue() returns inconsistent values. Tests using this 
     * method have been disabled until this bug is resolved.
     * @param quantityOfResults
     * @param expectedCategory
     * @throws TestException 
     */
    public void checkResultsIPCodeStyling(int quantityOfResults, Category expectedCategory) throws TestException {
        System.out.print("Checking that " + quantityOfResults + " results IP codes are correctly styled... ");
        //Load all results
        List<WebElement> results = driver.findElements(By.xpath(XPath.resGenericResult));
        int lastSize;
        //Until quantity is met, scroll down, wait for load, and check new quantity
        while (results.size() < quantityOfResults) {
            scrollDown();
            waitForLoadingIndicatorToDisappear();
            //Record last result count before refreshing list of results
            lastSize = results.size();
            results = driver.findElements(By.xpath(XPath.resGenericResult));
            //If result count hasn't changed, break the loop
            if (lastSize == results.size()) {
                break;
            }
        }
        //Iterate through results and check IP code styles
        for (WebElement myResult : results) {
            String id = myResult.getAttribute("data-qa-externalid");
            WebElement ipCode = driver.findElement(By.xpath(XPath.resSpecificResultIPIcon(id)));
            //Fail if background color does not match category's color
            if (!expectedCategory.getBackgroundColor().equals(ipCode.getCssValue("background-color"))) {
                throw new TestException("Result with external id '" + id +
                        "' failed. Expected background color: '" + expectedCategory.getBackgroundColor() + "', " +
                        "Actual background color: '" + ipCode.getCssValue("background-color") + "'");
            }
            //Fail if expected category is Restricted, but the bottom-border is not the correct color
            if (expectedCategory == Category.RESTRICTED && !ipCode.getCssValue("border-bottom").contains(expectedCategory.getBorderBottomColor())) {
                throw new TestException("Restricted result with external id '" + id +
                        "' failed. Expected bottom-border color: '" + expectedCategory.getBorderBottomColor() +
                        "', Actual bottom-border value: " + ipCode.getCssValue("bottom-border") + "'");
            }
        }
        System.out.println("Done.");
    }
    
    /**
     * Checks that the search result count is not only present, but is accurate
     * as well.
     * @throws TestException 
     */
    public void checkSearchResultCount() throws TestException {
        //Load all results
        List<WebElement> results = driver.findElements(By.xpath(XPath.resGenericResult));
        int lastSize;
        //Until quantity is met, scroll down, wait for load, and check new quantity
        while (results.size() < endResultCount) {
            scrollDown();
            waitForLoadingIndicatorToDisappear();
            //Record last result count before refreshing list of results
            lastSize = results.size();
            results = driver.findElements(By.xpath(XPath.resGenericResult));
            //If result count hasn't changed, break the loop
            if (lastSize == results.size()) {
                break;
            }
        }
    }
    
    /**
     * Validates that the current search term matches what was stored as the 
     * selectedText, by clicking a facet or keyword, usually.
     * @throws TestException 
     */
    public void checkSearchTermEqualsSelectedText() throws TestException {
        System.out.print("Validating search term matches expected text... ");
        String searchTerm = driver.getCurrentUrl().replaceAll("%20", " ");
        searchTerm = searchTerm.substring(searchTerm.indexOf("=") + 1);
        if (searchTerm.contains("&keywords")) {
            searchTerm = searchTerm.substring(0, searchTerm.indexOf("&"));
        }
        if (!searchTerm.equals(selectedText)) {
            throw new TestException("Search term '" + searchTerm + "' does not match "
                    + "expected text '" + selectedText + "'.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the current search term matches the provided text.
     * @param text
     * @throws TestException 
     */
    public void checkSearchTermEquals(String text) throws TestException {
        System.out.print("Validating search term matches expected text '" + text + "'... ");
        waitForElementToBeVisible(XPath.srchResultsTerm);
        String searchTerm = driver.getCurrentUrl().replaceAll("%20", " ");
        searchTerm = searchTerm.substring(searchTerm.indexOf("=") + 1);
        if (searchTerm.contains("&keywords")) {
            searchTerm = searchTerm.substring(0, searchTerm.indexOf("&"));
        }
        if (!searchTerm.equals(text)) {
            throw new TestException("Search term '" + searchTerm + "' does not match "
                    + "expected text '" + text + "'.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the provided SortOption is in fact the currently-applied sort option.
     * @param option
     * @throws TestException 
     */
    public void checkSelectedSortOption(SortOption option) throws TestException {
        System.out.print("Validating selected sort option is '" + option + "'... ");
        try {
            String identifier = null;
            switch (option) {
                case NEWEST:
                    identifier = XPath.sortOptionNewestSelected;
                    break;
                case OLDEST:
                    identifier = XPath.sortOptionOldestSelected;
                    break;
                case RELEVANCE:
                    identifier = XPath.sortOptionRelevanceSelected;
                    break;
            }
            driver.findElement(By.xpath(identifier));
        } catch(NoSuchElementException e) {
            throw new TestException("Sort option not applied as expected.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the specified sort option is actually applied to the search results.
     * @param option
     * @throws TestException 
     */
    public void checkSortOptionIsApplied(SortOption option) throws TestException {
        System.out.print("Validating '" + option + "' sort option is applied to search results... ");
        try {
            switch (option) {
                case NEWEST:
                    Assert.isTrue(newestApplied());
                    break;
                case OLDEST:
                    Assert.isTrue(oldestApplied());
                    break;
                case RELEVANCE:
                    Assert.isTrue(relevanceApplied());
                    break;
            }
        } catch(Exception e) {
            throw new TestException("Error checking if sort option applied: " + e.getMessage());
        }
        System.out.println("Done.");
    }
    
    /**
     * Checks that search results are ordered by highest externalId to lowest externalId.
     * @return 
     */
    private Boolean newestApplied() {
        List<WebElement> results = driver.findElements(By.xpath(XPath.resGenericResult));
        //Loop through results and check order
        int lastId = 0;
        for (WebElement result: results) {
            int resultId = Integer.parseInt(result.getAttribute("data-qa-externalid"));
            if (lastId == 0) {
                //If first result, set lastId
                lastId = resultId;
            } else {
                if (resultId > lastId) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks that search results are ordered by lowest externalId to highest externalId.
     * @return 
     */
    private Boolean oldestApplied() {
        List<WebElement> results = driver.findElements(By.xpath(XPath.resGenericResult));
        //Loop through results and check order
        int lastId = 0;
        for (WebElement result : results) {
            int resultId = Integer.parseInt(result.getAttribute("data-qa-externalid"));
            if (lastId == 0) {
                //If first result, set lastId
                lastId = resultId;
            } else {
                if (resultId < lastId) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks that neither the newest or oldest sort options are applied to verify that the relevance option is
     * actually applied (since there's no practical way to match the MarkLogic relevance algorithm)
     * @return 
     */
    private Boolean relevanceApplied() {
        if (!newestApplied() && !oldestApplied()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Selects the specified sort option by opening the drop down menu and clicking the option.
     * @param option
     * @throws TestException 
     */
    public void selectSortOption(SortOption option) throws TestException {
        System.out.print("Selecting the '" + option + "' sort option... ");
        try {
            clickElement(XPath.sortDropDown);
            switch (option) {
                case NEWEST:
                    clickElement(XPath.sortOptionNewest);
                    break;
                case OLDEST:
                    clickElement(XPath.sortOptionOldest);
                    break;
                case RELEVANCE:
                    clickElement(XPath.sortOptionRelevance);
                    break;
            }
            waitForLoadingIndicatorToDisappear();
        } catch(NoSuchElementException e) {
            throw new TestException("Unable to select option.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Loads more results by scrolling down ten times.
     * @throws TestException 
     */
    public void loadMoreResults() throws TestException {
        System.out.print("Loading more search results... ");
        for (int i = 0; i < 10; i++) {
            scrollDown();
        }
        System.out.println("Done.");
    }
    
    public WebElement getSearchResultWithIndex(int index) {
        List<WebElement> elements = driver.findElements(By.xpath(XPath.resGenericResult));
        return elements.get(index);
    }
}
