package org.lds.mediafinder.modules;

import org.lds.mediafinder.constants.Tab;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all authentication methods.
 * @author Allen Sudweeks
 */
public class Module_Authentication extends ModuleMaster {
    
    public Module_Authentication(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    public void loginWithValidUserNameAndPassword() throws TestException {
        loginWithValidUserNameAndPassword(Constants.username, Constants.passwordValid);
    }
    
    /**
     * Attempts to sign in using valid Media Finder credentials.
     * @throws TestException 
     */
    public void loginWithValidUserNameAndPassword(String username, String password) throws TestException {
        System.out.print("Signing in with valid credentials... ");
        //Enter credentials
        sendKeysToElement(XPath.ssoTxtUserName, username);
        sendKeysToElement(XPath.ssoTxtPassword, password);
        //Click submit
        clickElement(XPath.ssoBtnSubmit);
        //Wait for page load and ensure delivered to landing page
        waitForElementToBeVisible(XPath.ribMediaFinderLogo); 
        System.out.println("Done.");
    }
    
    public void loginAndGoToLandingPage() throws TestException {
        loginAndGoToLandingPage(Constants.username, Constants.passwordValid);
    }
    /**
     * Convenience method for signing in using valid Media Finder credentials
     * and ensuring the user is brought to the landing page.
     * 
     * @throws TestException 
     */
    public void loginAndGoToLandingPage(String username, String password) throws TestException {
        resizeWindowToFullScreen();
        loginWithValidUserNameAndPassword(username, password);
        try {
            //If you are on the landing page, the Church logo is animating in.
            //No other wait method works here, so timed wait must be used to 
            //allow time for animation to complete before continuing.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                //Fail silently
            }
            driver.findElement(By.xpath(XPath.pageLandingPageIdentifier));
        } catch(NoSuchElementException e) {
            navigateTo(Tab.LANDINGPAGE);   
            //Again, allow time for Church logo animation to complete.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                //Fail silently
            }
        }        
    }
    
    /**
     * Attempts to sign in using valid Media Finder credentials and ensures the
     * user is brought to an asset's details view panel as expected.
     * @throws TestException 
     */
    public void loginForDetailsViewDeepLink() throws TestException {
        System.out.print("Signing in, expecting details view... ");
        //Enter credentials
        sendKeysToElement(XPath.ssoTxtUserName, Constants.username);
        sendKeysToElement(XPath.ssoTxtPassword, Constants.passwordValid);
        //Click submit
        clickElement(XPath.ssoBtnSubmit);
        //Wait for page load and ensure delivered to landing page
        waitForElementToBeVisible(XPath.detDetailsPanel);
        System.out.println("Done.");
    }
    
    /**
     * Attempts to sign in using invalid Media Finder credentials and ensures 
     * the user is returned to the WAM sign in screen.
     * @throws TestException 
     */
    public void loginWithInvalidUserNameAndPassword() throws TestException {
        System.out.print("Attempting to sign in with invalid credentials... ");
        //Enter credentials
        sendKeysToElement(XPath.ssoTxtUserName, Constants.username);
        sendKeysToElement(XPath.ssoTxtPassword, Constants.passwordInvalid);
        //Click submit
        clickElement(XPath.ssoBtnSubmit);
        try { //Custom wait to cause failure if the element is ever found
            waitForElementToBeVisible(XPath.ribMediaFinderLogo);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPath.ribImgChurchLogo)));
            throw new TestException("Signed in with invalid credentials!");
        } catch (Throwable e) {
            System.out.println("Login attempt failed as expected.");
        }
    }
    
    /**
     * Logs out of Media Finder by clicking the Logout link in the header ribbon
     * and ensuring the user is returned to the WAM sign in screen. Resets mouse
     * pointer to corner to avoid hover issues with subsequent tests.
     * @throws TestException 
     */
    public void logout() throws TestException {
        System.out.print("Logging out... ");
        //Give time to make sure logout link isn't moving, due to branding banner on landing page
        hardWait(1000);
        //Click logout and wait for SSO page to be visible
        clickElement(XPath.ribLogout);
        waitForElementToBeVisible(XPath.ssoTxtUserName);
        System.out.println("Done.");
    }
}
