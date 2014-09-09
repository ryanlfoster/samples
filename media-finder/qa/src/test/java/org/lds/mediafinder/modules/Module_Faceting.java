package org.lds.mediafinder.modules;

import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.lds.stack.utils.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all shared faceting methods. See specific faceting modules for specific methods.
 * @author Allen Sudweeks
 */
public abstract class Module_Faceting extends ModuleMaster {
    
    //Stores result counts each time a facet is applied or removed
    //For use with result count validation methods
    protected static int resultCountBefore;
    protected static int resultCountAfter;
        
    public Module_Faceting(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Retrieves the result count as displayed on the page.
     * @return count
     * @throws TestException 
     */
    protected int getResultCount() throws TestException {
        int count;
        //Wait for count to be displayed
        waitForElementToBeVisible(XPath.srchResultsCount);
        //Retrieve the count
        String resultString = driver.findElement(By.xpath(XPath.srchResultsCount)).getText();
        //Parse extra text out of returned string, then parse the remaining integer
        char[] temp = resultString.toCharArray();
        String parseMe = "";
        for (char myChar: temp) {
            if (Character.isDigit(myChar)) {
                parseMe += Character.toString(myChar);
            } else {
                break;
            }
        }
        count = Integer.parseInt(parseMe);
        return count;
    }
    
    /**
     * Checks that the search result count was increased as expected after 
     * removing a facet or applying an IP Code category.
     * @throws TestException 
     */
    public void checkResultCountGreater() throws TestException {
        System.out.print("Checking result count properly increased... ");
        //Check that count increased
        if (resultCountBefore < resultCountAfter) {
            System.out.println("Done.");
        } else {
            throw new TestException("Result count not increased as expected.");
        }
    }
    
    /**
     * Checks that the search result count was decreased as expected after 
     * applying a facet or removing an IP Code category.
     * @throws TestException 
     */
    public void checkResultCountLesser() throws TestException {
        System.out.print("Checking result count properly decreased... ");
        //Check that count decreased
        if (resultCountBefore > resultCountAfter) {
            System.out.println("Done.");
        } else {
            throw new TestException("Result count not decreased as expected.");
        }
    }
    
    /**
     * Gets the text value for the provided xpath (assumed to be an image facet) and parses the count (i.e. "(123)") out.
     * @param xpath
     * @return
     * @throws TestException 
     */
    public int getImageFacetResultCount(WebElement element) throws TestException {
        String name = element.getAttribute("name");
        String count = driver.findElement(By.xpath(XPath.facImageFacetDataCount(name))).getText();
        if (StringUtils.isNotBlank(count)) {
            return Integer.parseInt(count.trim());
        } else {
            throw new TestException("Cannot get facet result count from given XPath.");
        }
    }
    
    
}
