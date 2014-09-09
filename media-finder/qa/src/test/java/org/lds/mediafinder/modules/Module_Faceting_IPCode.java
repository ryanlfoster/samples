package org.lds.mediafinder.modules;

import java.util.List;
import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Allen Sudweeks
 */
public class Module_Faceting_IPCode extends Module_Faceting {

    public Module_Faceting_IPCode(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
    /**
     * Checks to ensure that IP Code categories are displayed, along with the proper
     * heading.
     * @throws TestException 
     */
    public void checkForIPCodeCategories() throws TestException {
        System.out.print("Checking for generated IP Code categories... ");
        waitForElementToBeVisible(XPath.facIPCodeGroupsHeader);
        //Pass only if all required elements are displayed
        if (driver.findElement(By.xpath(XPath.facOpenUseCategory)).isDisplayed()
                && driver.findElement(By.xpath(XPath.facRequiresApprovalCategory)).isDisplayed()
                && driver.findElement(By.xpath(XPath.facRestrictedCategory)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("No IP Code categories were found or IP Code elements were missing.");
        }
    }
    
    /**
     * Applies the specified IP code category. Result counts (before and after)
     * are updated.
     * @param category
     * @throws TestException 
     */
    public void applyIPCodeCategory(Category category) throws TestException {
        System.out.print("Applying " + category.getName() + " IP code category... ");
        //Record result count
        resultCountBefore = getResultCount();
        if (!driver.findElement(By.xpath(category.getXPath())).isSelected()) {
            //Apply the category and wait for load
            clickElement(category.getXPath());
            waitForLoadingIndicatorToDisappear();
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            System.out.println(category.getName() + " category already applied.");
        }
    }
    
    /**
     * Removes the specified IP code category. Result counts (before and after)
     * are updated.
     * @param category
     * @throws TestException 
     */
    public void removeIPCodeCategory(Category category) throws TestException {
        System.out.print("Removing " + category.getName() + " IP code category... ");
        //Record result count
        resultCountBefore = getResultCount();
        if (driver.findElement(By.xpath(category.getXPath())).isSelected()) {
            //Remove the category and wait for load
            clickElement(category.getXPath());
            waitForLoadingIndicatorToDisappear();
            //Retry - for some reason, Firefox and Chrome seem to miss the first attempt periodically...
            if (driver.findElement(By.xpath(category.getXPath())).isSelected()) {
                clickElement(category.getXPath());
                waitForLoadingIndicatorToDisappear();
            }
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            System.out.println(category.getName() + " category already removed.");
        }
    }
    
    /**
     * Checks the specified number of search results (scrolls to load more results
     * until the number is met) and ensures they all belong to the specified IP
     * code category.
     * @param quantityOfResults
     * @param expectedCategory
     * @throws TestException 
     */
    public void checkResultsIPCodes(int quantityOfResults, Category expectedCategory) throws TestException {
        System.out.print("Checking that " + quantityOfResults + " results belong to the " + 
                expectedCategory.getName() + " category... ");
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
        //Iterate through results and check IP codes
        for (WebElement myResult: results) {
            //Fail if IP code is not found in category's vales
            if (!expectedCategory.getValues().contains(myResult.getAttribute("data-qa-ipcode"))) {
                throw new TestException("Result with external id '" + myResult.getAttribute("data-qa-externalid") +
                    "' does not belong to the " + expectedCategory.getName() + " category.");
            }
        }
        System.out.println("Done.");
    }
    
    /**
     * Checks that the specified IP code category is selected.
     * @param category
     * @throws TestException 
     */
    public void checkIPCodeCategorySelected(Category category) throws TestException {
        System.out.print("Checking that " + category.getName() + " category is selected... ");
        if (driver.findElement(By.xpath(category.getXPath())).isSelected()) {
            System.out.println("Done.");
        } else {
            throw new TestException(category.getName() + " category not selected as expected.");
        }
    }
    
    /**
     * Checks that the specified IP code category is not selected.
     * @param category
     * @throws TestException 
     */
    public void checkIPCodeCategoryNotSelected(Category category) throws TestException {
        System.out.print("Checking that " + category.getName() + " category is not selected... ");
        if (!driver.findElement(By.xpath(category.getXPath())).isSelected()) {
            System.out.println("Done.");
        } else {
            throw new TestException(category.getName() + " category selected when not expected.");
        }
    }
    
    
}
