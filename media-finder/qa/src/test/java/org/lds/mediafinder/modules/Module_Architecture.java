package org.lds.mediafinder.modules;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.lds.mediafinder.constants.Environment;
import org.lds.mediafinder.constants.Tab;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all architecture (design) methods.
 * @author Allen Sudweeks
 */
public class Module_Architecture extends ModuleMaster {
    
    //Ribbon elements
    private Point startLogo;
    private Point startMediaFinder;
    private Point startEverything;
    private Point startImages;
    private Point startVideo;
    private Point startAudio;
    private Point startText;
    private Point startLogout;
    private Point newLogo;
    private Point newMediaFinder;
    private Point newEverything;
    private Point newImages;
    private Point newVideo;
    private Point newAudio;
    private Point newText;
    private Point newLogout;
    
    public Module_Architecture(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Validates the landing page background image cycling by refreshing the page
     * cycleCount number of times, recording the image name, and verifying that
     * the image names are varied.
     * @param cycleCount
     * @throws TestException 
     */
    public void checkImageCycling(int cycleCount) throws TestException {
        System.out.print("Cycling landing page image " + cycleCount + " times... ");
        //Setup
        ArrayList<String> imageNames = new ArrayList<String>();
        //Cycle X times, refreshing the page and extracting the image name into imageNames list
        for (int i = 0; i < cycleCount; i++) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPath.landBackgroundImage)));
            WebElement backgroundImage = driver.findElement(By.xpath(XPath.landBackgroundImage));
            String imageName = backgroundImage.getAttribute("src");
            imageNames.add(imageName);
            navigateRefresh();            
        }
        System.out.println("Done.");
        //Check imageNames list to ensure the names are unique
        Boolean unique = false;
        String temp = imageNames.get(0);
        String list = "";
        for (String name:imageNames) {
            list += name + ", ";
            if (!name.equals(temp)) {
                unique = true;
            }
        }
        if (!unique) {
            throw new TestException("Image names not unique: " + list);
        }       
    }
    
    /**
     * Records the start positions of header ribbon elements to be used in 
     * conjunction with getRibbonNewPositions() for ensuring elements are not 
     * moved while animating from one tab to another.
     * @throws TestException 
     */
    public void getRibbonStartPositions() throws TestException {
        try {
            //Record locations
            startLogo = driver.findElement(By.xpath(XPath.ribImgChurchLogo)).getLocation();
            startMediaFinder = driver.findElement(By.xpath(XPath.ribMediaFinderLogo)).getLocation();
            startEverything = driver.findElement(By.xpath(XPath.ribEverything)).getLocation();
            startImages = driver.findElement(By.xpath(XPath.ribImages)).getLocation();
            startVideo = driver.findElement(By.xpath(XPath.ribVideo)).getLocation();
            startAudio = driver.findElement(By.xpath(XPath.ribAudio)).getLocation();
            startText = driver.findElement(By.xpath(XPath.ribText)).getLocation();
            startLogout = driver.findElement(By.xpath(XPath.ribLogout)).getLocation();
        } catch(NoSuchElementException e) {
            throw new TestException("Element not found: " + e.getMessage());
        }
    }
    
    /**
     * Records the current positions of header ribbon elements to be used in
     * conjunction with getRibbonStartPositions() for ensuring elements are not
     * moved while animating from one tab to another.
     * @throws TestException 
     */
    private void getRibbonNewPositions() throws TestException {
        try {
            //Record locations
            newLogo = driver.findElement(By.xpath(XPath.ribImgChurchLogo)).getLocation();
            newMediaFinder = driver.findElement(By.xpath(XPath.ribMediaFinderLogo)).getLocation();
            newEverything = driver.findElement(By.xpath(XPath.ribEverything)).getLocation();
            newImages = driver.findElement(By.xpath(XPath.ribImages)).getLocation();
            newVideo = driver.findElement(By.xpath(XPath.ribVideo)).getLocation();
            newAudio = driver.findElement(By.xpath(XPath.ribAudio)).getLocation();
            newText = driver.findElement(By.xpath(XPath.ribText)).getLocation();
            newLogout = driver.findElement(By.xpath(XPath.ribLogout)).getLocation();
        } catch(NoSuchElementException e) {
            throw new TestException("Element not found: " + e.getMessage());
        }
    }
    
    /**
     * Compares header ribbon start locations against header ribbon new locations
     * to ensure that the ribbon elements have not moved after animating from 
     * one tab to another.
     * @throws TestException 
     */
    public void checkRibbonPositions() throws TestException {
        System.out.print("Verifying ribbon positions are consistent... ");
        //Setup
        getRibbonNewPositions();
        //Compare positions
        if (startLogo.getX() == newLogo.getX() && startMediaFinder.getX() == newMediaFinder.getX()
                && startEverything.getX() == newEverything.getX() && startImages.getX() == newImages.getX()
                && startVideo.getX() == newVideo.getX() && startAudio.getX() == newAudio.getX()
                && startText.getX() == newText.getX() && startLogout.getX() == newLogout.getX()
                && startLogo.getY() == newLogo.getY() && startMediaFinder.getY() == newMediaFinder.getY()
                && startEverything.getY() == newEverything.getY() && startImages.getY() == newImages.getY()
                && startVideo.getY() == newVideo.getY() && startAudio.getY() == newAudio.getY()
                && startText.getY() == newText.getY() && startLogout.getY() == newLogout.getY()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Ribbon element positions not consistent.");
        }
    }
    
    /**
     * Verifies that the WebDriver is on the specified tab.
     * @param tab
     * @throws TestException 
     */
    public void checkOnSpecifiedTab(Tab tab) throws TestException {
        System.out.print("Verifying that WebDriver is on expected tab... ");
        //Verify that the tab is selected
        try {
            switch (tab) {
                case LANDINGPAGE:
                    waitForElementToBeVisible(XPath.pageLandingPageIdentifier);
                    break;
                default:
                    waitForElementToBeVisible(XPath.pageSearchPageIdentifier);
                    break;
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Not on expected tab.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Navigates to the provided URL.
     * @param url
     * @throws TestException 
     */
    public void navigateToUrl(String url) throws TestException {
        System.out.print("Navigating to '" + url + "'... ");
        try {
            driver.navigate().to(new URL(url));
            System.out.println("Done.");
        } catch(MalformedURLException e) {
            throw new TestException ("Malformed URL: " + e.getMessage());
        }        
    }
    
    /**
     * Validates that the current page URL contains the given text.
     * @param text
     * @throws TestException 
     */
    public void checkUrlContains(String text) throws TestException {
        System.out.print("Checking URL contains text '" + text + "'... ");
        if (driver.getCurrentUrl().contains(text)) {
            System.out.println("Done.");
        } else {
            throw new TestException("URL '" + driver.getCurrentUrl() + "' does not contain expected text '" + text + "'.");
        }
    }
    
    /**
     * Checks that the header ribbon and other notable elements are not misplaced
     * when resizing from full screen down to the provided minimum supported width.
     * @param widthToResizeTo
     * @throws TestException 
     */
    public void checkDesignConsistencyOnResize(int widthToResizeTo) throws TestException {
        //Until below x wide, reduce window size incrementally and check positions
        System.out.print("Checking element positions while reducing window width to " + Integer.toString(widthToResizeTo) + " pixels... ");
        int width = driver.manage().window().getSize().getWidth();
        //Record start positions
        Point startIPHeader = driver.findElement(By.xpath(XPath.facIPCodeGroupsHeader)).getLocation();
        Point startOpenUse = driver.findElement(By.xpath(XPath.facOpenUseCategory)).getLocation();
        Point startRequiresApproval = driver.findElement(By.xpath(XPath.facRequiresApprovalCategory)).getLocation();
        Point startRestricted = driver.findElement(By.xpath(XPath.facRestrictedCategory)).getLocation();
        Point startKeywordHeader = driver.findElement(By.xpath(XPath.facKeywordsHeader)).getLocation();
        Point startFacet = driver.findElement(By.xpath(XPath.facGenericKeywordFacet)).getLocation();
        Point startSearchHeader = driver.findElement(By.xpath(XPath.srchSearchHeader)).getLocation();
        Point startSearchResultLabel = driver.findElement(By.xpath(XPath.srchResultsCount)).getLocation();
        Point startMediaFinderLogo = driver.findElement(By.xpath(XPath.ribMediaFinderLogo)).getLocation();
        Point startLogoutLink = driver.findElement(By.xpath(XPath.ribLogout)).getLocation();
        while (widthToResizeTo <= width) {
            //Reduce window size
            driver.manage().window().setSize(new Dimension(width - 50, fullHeight)); 
            width = driver.manage().window().getSize().getWidth();
            //Record current positions
            Point currentIPHeader = driver.findElement(By.xpath(XPath.facIPCodeGroupsHeader)).getLocation();
            Point currentOpenUse = driver.findElement(By.xpath(XPath.facOpenUseCategory)).getLocation();
            Point currentRequiresApproval = driver.findElement(By.xpath(XPath.facRequiresApprovalCategory)).getLocation();
            Point currentRestricted = driver.findElement(By.xpath(XPath.facRestrictedCategory)).getLocation();
            Point currentKeywordHeader = driver.findElement(By.xpath(XPath.facKeywordsHeader)).getLocation();
            Point currentFacet = driver.findElement(By.xpath(XPath.facGenericKeywordFacet)).getLocation();
            Point currentSearchHeader = driver.findElement(By.xpath(XPath.srchSearchHeader)).getLocation();
            Point currentSearchResultLabel = driver.findElement(By.xpath(XPath.srchResultsCount)).getLocation();
            Point currentMediaFinderLogo = driver.findElement(By.xpath(XPath.ribMediaFinderLogo)).getLocation();
            Point currentLogoutLink = driver.findElement(By.xpath(XPath.ribLogout)).getLocation();
            //Check elements that should remain static
            if (startIPHeader.getX() != currentIPHeader.getX() || startIPHeader.getY() != currentIPHeader.getY()) {
                throw new TestException("IP Code Header moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startIPHeader.toString() + ", End position: " + currentIPHeader.toString());
            }
            if (startOpenUse.getX() != currentOpenUse.getX() || startOpenUse.getY() != currentOpenUse.getY()) {
                throw new TestException("Open Use category moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startOpenUse.toString() + ", End position: " + currentOpenUse.toString());
            }
            if (startRequiresApproval.getX() != currentRequiresApproval.getX() || startRequiresApproval.getY() != currentRequiresApproval.getY()) {
                throw new TestException("Requires Approval category moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startRequiresApproval.toString() + ", End position: " + currentRequiresApproval.toString());
            }
            if (startRestricted.getX() != currentRestricted.getX() || startRestricted.getY() != currentRestricted.getY()) {
                throw new TestException("Restricted category moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startRestricted.toString() + ", End position: " + currentRestricted.toString());
            }
            if (startKeywordHeader.getX() != currentKeywordHeader.getX() || startKeywordHeader.getY() != currentKeywordHeader.getY()) {
                throw new TestException("Keyword Header moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startKeywordHeader.toString() + ", End position: " + currentKeywordHeader.toString());
            }
            if (startFacet.getX() != currentFacet.getX() || startFacet.getY() != currentFacet.getY()) {
                throw new TestException("Keyword facet moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startFacet.toString() + ", End position: " + currentFacet.toString());
            }
            if (startSearchHeader.getX() != currentSearchHeader.getX() || startSearchHeader.getY() != currentSearchHeader.getY()) {
                throw new TestException("Search Header moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startSearchHeader.toString() + ", End position: " + currentSearchHeader.toString());
            }
            if (startSearchResultLabel.getX() != currentSearchResultLabel.getX() || startSearchResultLabel.getY() != currentSearchResultLabel.getY()) {
                throw new TestException("Result count label moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startSearchResultLabel.toString() + ", End position: " + currentSearchResultLabel.toString());
            }
            if (startMediaFinderLogo.getX() != currentMediaFinderLogo.getX() || startMediaFinderLogo.getY() != currentMediaFinderLogo.getY()) {
                throw new TestException("Media Finder logo moved during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startMediaFinderLogo.toString() + ", End position: " + currentMediaFinderLogo.toString());
            }
            //Check logout link has not moved vertically
            if (startLogoutLink.getY() != currentLogoutLink.getY()) {
                throw new TestException("Logout link moved vertically during window resize at window width " + Integer.toString(width) + ". Start position: " + 
                        startLogoutLink.toString() + ", End position: " + currentLogoutLink.toString());
            }
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the URL reflects the environment name passed into buildargs.
     * @throws TestException 
     */
    public void checkURLAgainstEnvironment() throws TestException {
        System.out.print("Checking URL directs to correct environment... ");
        //Check url and set the expected environment
        String url = driver.getCurrentUrl().replace("#landing", "");
        //Nav to buildargs and get buildargs string
        driver.navigate().to(url + "buildargs");
        String buildargs = driver.findElement(By.xpath(XPath.pageBuildArgs)).getText().replace("The arguments to the build script are:", "").trim();
        //Nav back to Media Finder
        driver.navigate().to(url);
        //Check buildargs against url
        if (url.equals("https://dasi.ldschurch.org/") && !buildargs.equals(Environment.PROD.getBuildArg())) {
            throw new TestException("'" + url + "' URL not pointed to correct environment. Expected environment: " 
                    + Environment.PROD.getBuildArg() + ", Actual environment: " + buildargs);
        } else if (url.equals("https://dasi-stage.ldschurch.org/") && !buildargs.equals(Environment.STAGE.getBuildArg())) {
            throw new TestException("'" + url + "' URL not pointed to correct environment. Expected environment: "
                    + Environment.STAGE.getBuildArg() + ", Actual environment: " + buildargs);
        } else if (url.equals("https://dasi-uat.ldschurch.org/") && !buildargs.equals(Environment.UAT.getBuildArg())) {
            throw new TestException("'" + url + "' URL not pointed to correct environment. Expected environment: "
                    + Environment.UAT.getBuildArg() + ", Actual environment: " + buildargs);
        } else if (url.equals("https://dasi-test.ldschurch.org/") && !buildargs.equals(Environment.TEST.getBuildArg())) {
            throw new TestException("'" + url + "' URL not pointed to correct environment. Expected environment: "
                    + Environment.TEST.getBuildArg() + ", Actual environment: " + buildargs);
        } else if (url.equals("https://dasi-dev.ldschurch.org/") && !buildargs.equals(Environment.DEV.getBuildArg())) {
            throw new TestException("'" + url + "' URL not pointed to correct environment. Expected environment: "
                    + Environment.DEV.getBuildArg() + ", Actual environment: " + buildargs);
        }
        System.out.println("Done.");
    }
    
    /**
     * Finds and clicks the Allegiance feedback link on landing page.
     * @throws TestException 
     */
    public void followLandingPageAllegianceFeedbackLink() throws TestException {
        System.out.print("Following feedback link... ");
        clickElement(XPath.misLandingFeedbackLink);
        System.out.println("Done.");
    }
    
    /**
     * Finds and clicks the Allegiance feedback link on search page.
     * @throws TestException 
     */
    public void followSearchPageAllegianceFeedbackLink() throws TestException {
        System.out.print("Following feedback link... ");
        //Wait for keywords to load to avoid link shifting down while clicking
        waitForElementToBeVisible(XPath.facGenericKeywordFacet);
        clickElement(XPath.ribHelp);
        clickElement(XPath.misSearchFeedbackLink);
        System.out.println("Done.");
    }
    
    /**
     * Checks the canary page contents to ensure they exist and are not null.
     * @throws TestException 
     */
    public void checkCanaryPageContents() throws TestException {
        System.out.print("Checking the canary page contents... ");
        try {
            //Find values
            String laneName = driver.findElement(By.xpath(XPath.canLaneName)).getText();
            String revision = driver.findElement(By.xpath(XPath.canRevision)).getText();
            String catalogURL = driver.findElement(By.xpath(XPath.canCatalogURL)).getText();
            //Validate values not null
            if (laneName.isEmpty() || revision.isEmpty() || catalogURL.isEmpty()) {
                throw new TestException("Canary page contents missing.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Canary page not loaded properly.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the Powered By Catalog logo is visible on the page.
     * @throws TestException 
     */
    public void checkForPoweredByLogo() throws TestException {
        System.out.print("Checking for 'Powered By Catalog' logo... ");
        try {
            driver.findElement(By.xpath(XPath.misPoweredByLogo));
        } catch(NoSuchElementException e) {
            throw new TestException("Logo not found.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Validates that the Conditions of Use tooltip panel is open.
     * @throws TestException 
     */
    public void checkForConditionsOfUseTooltip() throws TestException {
        System.out.print("Checking for Conditions of Use tooltip... ");
        try {
            waitForElementToBeVisible(XPath.facConditionsOfUseTooltipPanel);
        } catch(NoSuchElementException e) {
            throw new TestException("Tooltip not found.");
        }
        System.out.println("Done.");
    }
    
    public void checkForIPCodeTooltip() throws TestException {
        System.out.print("Checking for IP Code tooltip... ");
        try {
            waitForElementToBeVisible(XPath.resGenericResultIPTooltip);
        } catch(NoSuchElementException e) {
            throw new TestException("Tooltip not found");
        }
        System.out.println("Done.");
    }
}
