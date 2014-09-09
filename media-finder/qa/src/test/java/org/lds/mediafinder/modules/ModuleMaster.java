package org.lds.mediafinder.modules;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.MalformedURLException;
import java.net.URL;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.constants.Tab;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses shared methods for all Modules.
 * @author Allen Sudweeks
 */
public class ModuleMaster {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static String selectedExternalId; //Stores external id of asset most recently interacted with
    protected static String selectedText; //Stores the text value of selected facets/keywords
    protected int fullWidth = 1920;
    protected int fullHeight = 1080;

    /**
     * Navigates forward one step in the browser history.
     */
    public void navigateForward() {
        System.out.print("Navigating forward... ");
        driver.navigate().forward();
        System.out.println("Done.");
    }

    /**
     * Navigates backward one step in the browser history.
     */
    public void navigateBackward() {
        System.out.print("Navigating backward... ");
        driver.navigate().back();
        System.out.println("Done.");
    }

    /**
     * Refreshes the current page in the browser.
     */
    public void navigateRefresh() {
        driver.navigate().refresh();
    }

    /**
     * Navigates to LDS.org by clicking the Church logo in Media Finder's header 
     * ribbon.
     * @throws TestException 
     */
    public void navigateToLDSOrg() throws TestException {
        System.out.println("Navigating to LDS.org... ");
        clickElement(XPath.ribImgChurchLogo);
        waitForPageTitleToBe("The Church of Jesus Christ of Latter-day Saints");
        System.out.println("Done.");
    }
    
    /**
     * Navigates to the specified tab by clicking the corresponding tab in Media 
     * Finder's header ribbon.
     * @param tab
     * @throws TestException 
     */
    public void navigateTo(Tab tab) throws TestException {
        System.out.print("Navigating to " + tab.getName() + " tab... ");
        clickElement(tab.getXPath());
        if (tab.getXPath().equals(XPath.ribMediaFinderLogo)) {
            waitForElementToBeVisible(XPath.pageLandingPageIdentifier);
            System.out.println("Done.");
        } else if(driver.findElement(By.xpath(tab.getXPath())).getAttribute("class").contains("selected")) {
            System.out.println("Done.");
        } else {
            throw new TestException("Error navigating to tab: " + tab.getXPath());
        }
    }
    
    /**
     * Navigates to the specified URL, if the URL is found to be valid.
     * @param url
     * @throws TestException 
     */
    public void navigateToURL(String url) throws TestException {
        //Check for valid URL
        URL newURL;
        try {
            newURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new TestException("Invalid URL provided: '" + url + "'.");
        }
        //Navigate to new URL
        System.out.print("Navigating to '" + url + "'... ");
        driver.navigate().to(newURL);
        System.out.println("Done.");
    }
    
    /**
     * Navigates to the environment's canary page.
     * @throws TestException 
     */
    public void navigateToCanaryPage() throws TestException {
        //Check for valid URL
        URL newURL = null;
        try {
            newURL = new URL(Constants.homeUrl + "canary");
        } catch (MalformedURLException e) {
            throw new TestException("Invalid URL provided: '" + newURL.toString() + "'.");
        }
        //Navigate to new URL
        System.out.print("Navigating to '" + newURL.toString() + "'... ");
        driver.navigate().to(newURL);
        System.out.println("Done.");
    }
    
    /**
     * Clicks a specified element.
     * @param xpath
     * @throws TestException 
     */
    public void clickElement(String xpath) throws TestException {
        try {
            driver.findElement(By.xpath(xpath)).click();
        } catch (NoSuchElementException e) {
            //Retry before failing
            try {
                driver.findElement(By.xpath(xpath)).click();
            } catch (NoSuchElementException e1) {
                throw new TestException("Error finding element to click: " + e1.getMessage());
            }            
        }
    }

    /**
     * Simulates typing into the specified element after clearing the element to 
     * ensure it's ready for new text.
     * @param xpath
     * @param text
     * @throws TestException 
     */
    public void sendKeysToElement(String xpath, String text) throws TestException {
        try {
            //Clear the text field before typing
            driver.findElement(By.xpath(xpath)).clear();
            driver.findElement(By.xpath(xpath)).sendKeys(text);
        } catch (NoSuchElementException e) {
            throw new TestException("Error finding element to send keys to: " + e.getMessage());
        }
    }
    
    /**
     * Waits up to the Constants.defaultWaitTime for an element to appear before 
     * timing out.
     * @param xpath
     * @throws TestException 
     */
    public void waitForElementToBeVisible(String xpath) throws TestException {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Throwable e) {
            throw new TestException("Element never located after wait: " + xpath);
        }
    }
    
    /**
     * Waits up to the Constants.defaultWaitTime for the page title to become 
     * the specified title before timing out.
     * @param title
     * @throws TestException 
     */
    public void waitForPageTitleToBe(String title) throws TestException {
        try {
            wait.until(ExpectedConditions.titleIs(title));
        } catch (Throwable e) {
            throw new TestException("Error, page title '" + title + "' never found: " + e.getMessage());
        }
    }
    
    /**
     * Waits up to the Constants.defaultWaitTime for the loading icon first to 
     * appear, then to disappear, signaling the completion of the loading process.
     * @throws TestException 
     */
    public void waitForLoadingIndicatorToDisappear() throws TestException {
        try {
            //Wait for loading indicator to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPath.srchLoading)));
            //Once it appears, wait for it to disappear
            Boolean stillVisible = true;
            while (stillVisible) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Thread sleeping error: " + e.getMessage());
                }
                if (!driver.findElement(By.xpath(XPath.srchLoading)).isDisplayed()) {
                    stillVisible = false;
                }
            }
        } catch(Throwable e) {
            //Perform no action, as this is thrown once the loading icon disappears, and is expected
        }
    }
    
    /**
     * The less-than-desired way to wait when no other wait suits the job...
     * @param millis 
     */
    public void hardWait(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) {
            //Fail silently
        }
    }
    
    /**
     * Simulates scrolling down by selecting a search result (to ensure there's 
     * a reason to scroll down), and hitting the Page Down key twice, which is 
     * sufficient to cause loading of additional results.
     * @throws TestException 
     */
    public void scrollDown() throws TestException {
        try {
            //Find an actual search result before sending keys
            driver.findElement(By.xpath(XPath.srchSearchField)).sendKeys(Keys.PAGE_DOWN);
            driver.findElement(By.xpath(XPath.srchSearchField)).sendKeys(Keys.PAGE_DOWN);
            try {
                //If Load More is visible, click it
                driver.findElement(By.xpath(XPath.srchLoadMore)).click();
            } catch(NoSuchElementException e) {
                //Fail silently
            }
        } catch (NoSuchElementException e) {
            throw new TestException("Error finding element to scroll on: " + e.getMessage());
        }
    }
     
    /**
     * Simulates scrolling up by selecting a search result (to ensure there's a
     * reason to scroll up), and hitting the Page Up key twice, which is the same
     * distance as the scrollDown() method.
     * @throws TestException 
     */
    public void scrollUp() throws TestException {
        try {
            //Find an actual search result before sending keys
            driver.findElement(By.xpath(XPath.srchSearchField)).sendKeys(Keys.PAGE_UP);
            driver.findElement(By.xpath(XPath.srchSearchField)).sendKeys(Keys.PAGE_UP);
        } catch (NoSuchElementException e) {
            throw new TestException("Error finding element to scroll on: " + e.getMessage());
        }
    }
    
    /**
     * Moves mouse approximately over the specified element.
     * @param xpath
     * @throws TestException 
     */
    public void mouseOverElement(WebElement element) throws TestException {
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }
    
    /**
     * Moves mouse approximately over the specified element and pauses for 1 second.
     * @param xpath
     * @throws TestException 
     */
    public void mouseOverElementAndPause(WebElement element) throws TestException {
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
        hardWait(1000);
    }
    
    /**
     * Moves mouse approximately over the specified element.
     * @param xpath
     * @throws TestException 
     */
    public void mouseOverElement(String xpath) throws TestException {
        WebElement element = driver.findElement(By.xpath(xpath));
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }
    
    /**
     * Moves mouse approximately over the specified element and pauses for 1 second.
     * @param xpath
     * @throws TestException 
     */
    public void mouseOverElementAndPause(String xpath) throws TestException {
        WebElement element = driver.findElement(By.xpath(xpath));
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
        hardWait(1000);
    }
    
    /**
     * Moves mouse to upper-left corner (used to prevent hover events from 
     * unrelated methods leaving the cursor over hover elements).
     * @throws TestException 
     */
    public void mouseToCorner() throws TestException {
        Point corner = new Point(1,1);
        try {
            Robot robot = new Robot();
            robot.mouseMove(corner.getX(), corner.getY());
        } catch (AWTException e) {
            throw new TestException("Error moving mouse to location: " + e.getMessage());
        }
    }
    
    /**
     * Checks that the specified element is not found on the page.
     * @param xpath
     * @throws TestException 
     */
    public void checkElementNotFound(String xpath) throws TestException {
        System.out.print("Checking element with XPath '" + xpath + "' not found... ");
        try {
            driver.findElement(By.xpath(xpath));
            throw new TestException("Element with XPath '" + xpath + "' found when not expected.");
        } catch(NoSuchElementException e) {
            //Take no catch action, since element was not supposed to be found anyway
            System.out.println("Done.");
        }        
    }
    
    /**
     * Convenience method for expanding the window to full screen without using
     * the maximize() method, allowing for dynamic resizing.
     * @throws TestException 
     */
    public void resizeWindowToFullScreen() throws TestException {
        System.out.print("Maximizing window size... ");
        driver.manage().window().setSize(new Dimension(fullWidth,fullHeight));
        System.out.println("Done.");
    }
    
}
