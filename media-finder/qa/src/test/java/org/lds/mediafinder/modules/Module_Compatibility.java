package org.lds.mediafinder.modules;

import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all browser compatibility methods.
 * @author Allen Sudweeks
 */
public class Module_Compatibility extends ModuleMaster {
    
    public Module_Compatibility(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Checks for the prompt to install Chrome Frame. Only used for IE 8 and 9.
     * Check that the plugin is not already installed prior to running this method.
     * @throws TestException 
     */
    public void checkForChromeFramePrompt() throws TestException {
        System.out.print("Checking for Chrome Frame prompt... ");
        //Check for Chrome Frame install button
        waitForElementToBeVisible(XPath.chrInstallButton);
        if (driver.findElement(By.xpath(XPath.chrInstallButton)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Chrome Frame prompt never displayed.");
        }
    }
    
    /**
     * Checks that the Chrome Frame prompt is not displayed (after installation).
     * Only used for IE 8 and 9.
     * @throws TestException 
     */
    public void checkChromeFramePromptNotFound() throws TestException {
        System.out.print("Checking that Chrome Frame prompt not found... ");
        //Check for Chrome Frame install button
        waitForElementToBeVisible(XPath.chrInstallButton);
        if (driver.findElement(By.xpath(XPath.chrInstallButton)).isDisplayed()) {
            throw new TestException("Chrome Frame prompt never displayed.");
        } else {
            System.out.println("Done.");
        }
    }
    
    /**
     * Installs Chrome Frame plugin. Only used for IE 8 and 9.
     * @throws TestException 
     */
    public void installChromeFrame() throws TestException {
        //Wait for and click Install button
        waitForElementToBeVisible(XPath.chrInstallButton);
        clickElement(XPath.chrInstallButton);
        //Wait for and click next button
        waitForElementToBeVisible(XPath.chrActivateButton);
        clickElement(XPath.chrActivateButton);
        //Wait for and click final button
        waitForElementToBeVisible(XPath.chrAcceptButton);
        clickElement(XPath.chrAcceptButton);
        //Wait and make sure returned to Media Finder after installation
        waitForElementToBeVisible(XPath.srchSearchField);
        if (!driver.findElement(By.xpath(XPath.srchSearchField)).isDisplayed()) {
            throw new TestException("Failed to find search field after Chrome Frame installation.");
        }
    }
}
