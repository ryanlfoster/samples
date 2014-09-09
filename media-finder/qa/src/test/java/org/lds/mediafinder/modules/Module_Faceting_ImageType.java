package org.lds.mediafinder.modules;

import java.util.List;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

/**
 *
 * @author Allen Sudweeks
 */
public class Module_Faceting_ImageType extends Module_Faceting {

    public Module_Faceting_ImageType(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
    /**
     * Checks to ensure that image type facets are displayed, along with the proper
     * heading.
     * @throws TestException 
     */
    public void checkForImageTypeFacets() throws TestException {
        System.out.print("Checking for generated image type facets... ");
        //Wait for load to finish
        waitForElementToBeVisible(XPath.facGenericKeywordFacet);
        try {
            //Expand Image Type facets list
            clickElement(XPath.facImageTypeCarat);
            //Check for elements
            Assert.isTrue(driver.findElement(By.xpath(XPath.facImageTypeHeader)).isDisplayed());
            Assert.isTrue(driver.findElement(By.xpath(XPath.facGenericImageType)).isDisplayed());
            System.out.println("Done.");
        } catch(NoSuchElementException e) {
            throw new TestException("No image type facets were found or facet elements were missing.");
        }        
    }
    
    /**
     * Checks the applied image type facet counts against the search result count
     * @throws TestException 
     */
    public void checkImageTypeAppiedCounts() throws TestException {
        System.out.print("Checking facet counts... ");
        try {
            //Wait for load to finish
            waitForLoadingIndicatorToDisappear();
            //Get applied facet counts
            int count = 0;
            List<WebElement> imageTypeFacets = driver.findElements(By.xpath(XPath.facGenericImageType));
            for (WebElement element: imageTypeFacets) {
                if (element.isSelected()) {
                    count += getImageFacetResultCount(element);
                }
            }
            //Validate
            String resultCount = driver.findElement(By.xpath(XPath.srchResultsCount)).getText().trim();
            resultCount = resultCount.substring(0, resultCount.indexOf("Results")).trim();
            Assert.isTrue(count == Integer.parseInt(resultCount));
        } catch(NoSuchElementException e){
            throw new TestException("Error checking counts: " + e.getMessage());
        }             
        System.out.println("Done.");
    }
    
    /**
     * Checks the applied image type facet counts to ensure zero are applied
     * @throws TestException 
     */
    public void checkImageTypeRemovedCounts() throws TestException {
        System.out.print("Checking facet counts... ");
        try {
            //Wait for load to finish
            waitForLoadingIndicatorToDisappear();
            //Get applied facet counts
            int count = 0;
            List<WebElement> imageTypeFacets = driver.findElements(By.xpath(XPath.facGenericImageType));
            for (WebElement element: imageTypeFacets) {
                if (element.isSelected()) {
                    count += getImageFacetResultCount(element);
                }
            }
            //Validate
            Assert.isTrue(count == 0);
        } catch(NoSuchElementException e){
            throw new TestException("Error checking counts: " + e.getMessage());
        }             
        System.out.println("Done.");
    }
    
    /**
     * Applies the first available image type facet by clicking it.
     * @throws TestException 
     */
    public void applyFirstAvailableImageTypeFacet() throws TestException {
        System.out.print("Applying first available image type facet... ");
        try {
            clickElement(XPath.facGenericImageType);
        } catch(NoSuchElementException e) {
            throw new TestException("No image type facet to apply.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Applies the specified image type facet, if it is not already applied.
     * @param xpath
     * @throws TestException 
     */
    public void applyImageTypeFacet(String xpath) throws TestException {
        System.out.print("Applying image type facet... ");
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            if (!element.isSelected()) {
                clickElement(xpath);
                System.out.println("Done.");
            } else {
                System.out.println("'" + element.getAttribute("name") + "' image type facet already applied.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Image type facet not found.");
        }
        
    }
    
    /**
     * Removes the specified image type facet, if it is applied.
     * @param xpath
     * @throws TestException 
     */
    public void removeImageTypeFacet(String xpath) throws TestException {
        System.out.print("Removing image type facet... ");
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            if (element.isSelected()) {
                clickElement(xpath);
                System.out.println("Done.");
            } else {
                System.out.println("'" + element.getAttribute("name") + "' image type facet not applied.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Image type facet not found.");
        }
        
    }
}
