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
public class Module_Faceting_ImageOrientation extends Module_Faceting {

    public Module_Faceting_ImageOrientation(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
    /**
     * Checks to ensure that image type facets are displayed, along with the proper
     * heading.
     * @throws TestException 
     */
    public void checkForImageOrientationFacets() throws TestException {
        System.out.print("Checking for generated image orientation facets... ");
        //Wait for load to finish
        waitForElementToBeVisible(XPath.facGenericKeywordFacet);
        try {
            //Expand Image Orientation facets list
            clickElement(XPath.facImageOrientationCarat);
            //Check for elements
            Assert.isTrue(driver.findElement(By.xpath(XPath.facImageOrientationHeader)).isDisplayed());
            Assert.isTrue(driver.findElement(By.xpath(XPath.facGenericImageOrientation)).isDisplayed());
            System.out.println("Done.");
        } catch(NoSuchElementException e) {
            throw new TestException("No image orientation facets were found or facet elements were missing.");
        }        
    }
    
    /**
     * Checks the applied image orientation facet counts against the search result count
     * @throws TestException 
     */
    public void checkImageOrientationAppiedCounts() throws TestException {
        System.out.print("Checking facet counts... ");
        try {
            //Wait for load to finish
            waitForLoadingIndicatorToDisappear();
            //Get applied facet counts
            int count = 0;
            List<WebElement> imageOrientationFacets = driver.findElements(By.xpath(XPath.facGenericImageOrientation));
            for (WebElement element: imageOrientationFacets) {
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
     * Checks the applied image orientation facet counts to ensure that zero are actually applied
     * @throws TestException 
     */
    public void checkImageOrientationRemovedCounts() throws TestException {
        System.out.print("Checking facet counts... ");
        try {
            //Wait for load to finish
            waitForLoadingIndicatorToDisappear();
            //Get applied facet counts
            int count = 0;
            List<WebElement> imageOrientationFacets = driver.findElements(By.xpath(XPath.facGenericImageOrientation));
            for (WebElement element: imageOrientationFacets) {
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
     * Applies the first available image orientation facet by clicking it.
     * @throws TestException 
     */
    public void applyFirstAvailableImageOrientationFacet() throws TestException {
        System.out.print("Applying first available image orientation facet... ");
        try {
            clickElement(XPath.facGenericImageOrientation);
        } catch(NoSuchElementException e) {
            throw new TestException("No image orientation facet to apply.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Applies the specified image orientation facet, if it is not already applied.
     * @param xpath
     * @throws TestException 
     */
    public void applyImageOrientationFacet(String xpath) throws TestException {
        System.out.print("Applying image orientation facet... ");
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            if (!element.isSelected()) {
                clickElement(xpath);
                System.out.println("Done.");
            } else {
                System.out.println("'" + element.getAttribute("name") + "' image orientation facet already applied.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Image orientation facet not found.");
        }
        
    }
    
    /**
     * Removes the specified image orientation facet, if it is applied.
     * @param xpath
     * @throws TestException 
     */
    public void removeImageOrientationFacet(String xpath) throws TestException {
        System.out.print("Removing image orientation facet... ");
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            if (element.isSelected()) {
                clickElement(xpath);
                System.out.println("Done.");
            } else {
                System.out.println("'" + element.getAttribute("name") + "' image orientation facet not applied.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Image orientation facet not found.");
        }
        
    }
}
