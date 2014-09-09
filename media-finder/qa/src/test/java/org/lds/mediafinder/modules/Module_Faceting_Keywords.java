package org.lds.mediafinder.modules;

import java.util.List;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Allen Sudweeks
 */
public class Module_Faceting_Keywords extends Module_Faceting {

    public Module_Faceting_Keywords(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
    /**
     * Checks to ensure that keyword facets are displayed, along with the proper
     * heading and "more" link.
     * @throws TestException 
     */
    public void checkForKeywordFacets() throws TestException {
        System.out.print("Checking for generated keyword facets... ");
        waitForElementToBeVisible(XPath.facGenericKeywordFacet);
        //Pass only if all required elements are displayed
        if (driver.findElement(By.xpath(XPath.facGenericKeywordFacet)).isDisplayed() &&
                driver.findElement(By.xpath(XPath.facKeywordsHeader)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("No keyword facets were found or facet elements were missing.");
        }        
    }
    
    /**
     * Finds the first available facet in the keyword facet list and applies it 
     * by clicking it, waiting for the loading process to complete, and ensuring 
     * the applied facet element with the same name can be found.
     * @throws TestException 
     */
    public void applyFirstAvailableFacet() throws TestException {
        //Record facet's title and result count
        String facetTitle = driver.findElement(By.xpath(XPath.facGenericKeywordFacet)).getAttribute("title");
        resultCountBefore = getResultCount();
        //Apply the facet and wait for load
        System.out.print("Applying first available facet, '" + facetTitle + "'... ");
        clickElement(XPath.facGenericKeywordFacet);
        waitForLoadingIndicatorToDisappear();
        //Check for the corresponding applied facet element
        if (driver.findElement(By.xpath(XPath.facSpecificAppliedFacet(facetTitle))).isDisplayed()) {
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            throw new TestException("Facet not properly applied.");
        }
    }
    
    /**
     * Finds the specified facet in the keyword facet list and applies it by 
     * clicking it, waiting for the loading process to complete, and ensuring the
     * applied facet element with the same name can be found.
     * @param name
     * @throws TestException 
     */
    public void applySpecificFacet(String name) throws TestException {
        System.out.print("Applying facet, '" + name + "'... ");
        //Record result count
        resultCountBefore = getResultCount();
        //Search for facet with keywords filter
        sendKeysToElement(XPath.facFilter, name);
        //Apply the facet and wait for load
        clickElement(XPath.facSpecificFacet(name));
        waitForLoadingIndicatorToDisappear();
        //Check for the corresponding applied facet element
        if (driver.findElement(By.xpath(XPath.facSpecificAppliedFacet(name))).isDisplayed()) {
            //Record result count
            resultCountAfter = getResultCount();
            //Validate not zero
            if (resultCountAfter != 0) {
                System.out.println("Done.");
            } else {
                throw new TestException("Facet applied, but returned no results.");
            }
            //Clear facet filter
            driver.findElement(By.xpath(XPath.facFilter)).clear();
        } else {
            throw new TestException("Facet not properly applied.");
        }
    }
    
    /**
     * Removes the first available applied facet by clicking it, waiting for the 
     * loading process to complete, and ensuring the keyword facet can once 
     * again be found in the keyword facet list.
     * @throws TestException 
     */
    public void removeFirstAvailableAppliedFacet() throws TestException {
        //Record the facet's title and result count
        String facetTitle = driver.findElement(By.xpath(XPath.facGenericAppliedFacet)).getAttribute("data-name");
        resultCountBefore = getResultCount();
        System.out.print("Removing first available applied facet, '" + facetTitle + "'... ");
        //Remove the facet and wait for load
        clickElement(XPath.facSpecificAppliedFacet(facetTitle));
        waitForLoadingIndicatorToDisappear();
        //Check for the facet to be back in the facet list
        if (driver.findElement(By.xpath(XPath.facSpecificFacet(facetTitle))).isDisplayed()) {
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            throw new TestException("Facet not properly removed.");
        }
    }
    
    /**
     * Removes the specified applied facet by clicking it, waiting for the 
     * loading process to complete, and ensuring the keyword facet can once 
     * again be found in the keyword facet list.
     * @param name
     * @throws TestException 
     */
    public void removeSpecificAppliedFacet(String name) throws TestException {
        System.out.print("Removing applied facet, '" + name + "'... ");
        //Record result count
        resultCountBefore = getResultCount();
        //Remove the facet and wait for load
        clickElement(XPath.facSpecificAppliedFacet(name));
        waitForLoadingIndicatorToDisappear();
        //Check for the facet to be back in the facet list
        if (driver.findElement(By.xpath(XPath.facSpecificFacet(name))).isDisplayed()) {
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            throw new TestException("Facet not properly removed.");
        }
    }
    
    /**
     * Removes the last available applied facet. Result counts (before and after)
     * are updated.
     * @throws TestException 
     */
    public void removeLastAvailableFacet() throws TestException {
        //Record the facet's title and result count
        List<WebElement> appliedFacets = driver.findElements(By.xpath(XPath.facGenericAppliedFacet));
        WebElement lastFacet = appliedFacets.get(appliedFacets.size() - 1);
        String facetTitle = lastFacet.getAttribute("data-name");
        resultCountBefore = getResultCount();
        System.out.print("Removing last available applied facet, '" + facetTitle + "'... ");
        //Remove the facet and wait for load
        lastFacet.click();
        waitForLoadingIndicatorToDisappear();
        //Check for the facet to be back in the facet list
        if (driver.findElement(By.xpath(XPath.facSpecificFacet(facetTitle))).isDisplayed()) {
            //Record result count
            resultCountAfter = getResultCount();
            System.out.println("Done.");
        } else {
            throw new TestException("Facet not properly removed.");
        }
    }
    
    /**
     * Applies the first available facet that contains the specified text. Result 
     * counts (before and after) are updated.
     * @param text
     * @throws TestException 
     */
    public void applyFacetContaining(String text) throws TestException {
        System.out.print("Finding a facet containing '" + text + "'... ");
        //Retrieve all available facets
        List<WebElement> facets = driver.findElements(By.xpath(XPath.facGenericKeywordFacet));
        String title = "";
        Boolean found = false;
        //Iterate through facets and find one containing the text
        for (WebElement facet: facets) {
            title = facet.getAttribute("title");
            if (title.contains(text)) {
                System.out.println("Done.");
                found = true;
                break;
            }
        }
        //If a facet was found, apply it
        if (found) {
            applySpecificFacet(title);
        } else {
            throw new TestException("No facet containing '" + text + "' found.");
        }
    }
    
    /**
     * Removes the first available applied facet containing the specified text. 
     * Result counts (before and after) are updated.
     * @param text
     * @throws TestException 
     */
    public void removeFacetContaining(String text) throws TestException {
        System.out.print("Finding an applied facet containing '" + text + "'... ");
        //Retrieve all applied facets
        List<WebElement> facets = driver.findElements(By.xpath(XPath.facGenericAppliedFacet));
        String title = "";
        Boolean found = false;
        //Iterate through applied facets and find one containing the text
        for (WebElement facet: facets) {
            title = facet.getAttribute("data-name");
            if (title.contains(text)) {
                System.out.println("Done.");
                found = true;
                break;
            }
        }
        //If an applied facet was found, remove it
        if (found) {
            removeSpecificAppliedFacet(title);
        } else {
            throw new TestException("No applied facet containing '" + text + "' found.");
        }
    }
    
    /**
     * Applies the first available facet that contains the specified text.
     * Result counts (before and after) are updated.
     *
     * @param text
     * @throws TestException
     */
    public void applyFacetStartingWith(String text) throws TestException {
        System.out.print("Finding a facet starting with '" + text + "'... ");
        //Type text into facet search
        driver.findElement(By.xpath(XPath.facFilter)).sendKeys(text);
        //Retrieve all available facets
        List<WebElement> facets = driver.findElements(By.xpath(XPath.facGenericKeywordFacet));
        String title = "";
        Boolean found = false;
        //Iterate through facets and find one containing the text
        for (WebElement facet : facets) {
            title = facet.getAttribute("title");
            if (title.startsWith(text)) {
                System.out.println("Done.");
                found = true;
                break;
            }
        }
        //If a facet was found, apply it
        if (found) {
            applySpecificFacet(title);
        } else {
            throw new TestException("No facet starting with '" + text + "' found.");
        }
    }

    /**
     * Removes the first available applied facet containing the specified text.
     * Result counts (before and after) are updated.
     *
     * @param text
     * @throws TestException
     */
    public void removeFacetStartingWith(String text) throws TestException {
        System.out.print("Finding an applied facet starting with '" + text + "'... ");
        //Retrieve all applied facets
        List<WebElement> facets = driver.findElements(By.xpath(XPath.facGenericAppliedFacet));
        String title = "";
        Boolean found = false;
        //Iterate through applied facets and find one containing the text
        for (WebElement facet : facets) {
            title = facet.getAttribute("data-name");
            if (title.startsWith(text)) {
                System.out.println("Done.");
                found = true;
                break;
            }
        }
        //If an applied facet was found, remove it
        if (found) {
            removeSpecificAppliedFacet(title);
        } else {
            throw new TestException("No applied facet starting with '" + text + "' found.");
        }
    }

    /**
     * Validates that an applied facet with the specified text can be found.
     * @param text
     * @throws TestException 
     */
    public void checkSpecificKeywordFacetApplied(String text) throws TestException {
        System.out.print("Checking for applied facet '" + text + "'... ");
        try {
            driver.findElement(By.xpath(XPath.facSpecificAppliedFacet(text)));
        } catch(NoSuchElementException e) {
            throw new TestException("Applied facet not found.");
        }
        System.out.println("Done.");
    }
    
}
