package org.lds.mediafinder;

import org.lds.mediafinder.utils.jira.JiraTestCaseServiceApi;
import org.lds.mediafinder.modules.ModuleManager;
import java.net.MalformedURLException;
import java.util.ArrayList;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.testcase.TestCase;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid;
import org.lds.stack.utils.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

/**
 * Class to be extended by all Media Finder automated test classes. 
 * Provides common resources needed by all tests.
 * @author Allen Sudweeks
 */
public abstract class TestMaster {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ModuleManager modules;
    protected TestCase testCase;
    protected SeleniumGrid grid;
    protected String testTerm = "telescope";

    @DataProvider(name = "nodes")
    public Object[][] dataset() {
        return new Object[][]{
//            {Browser.CHROME, "", Platform.VISTA},
            {Browser.FIREFOX, "", Platform.VISTA},
        };
    }

    @DataProvider(name = "local")
    public Object[][] localDataset() {
        return new Object[][]{
            {null, "", null}
        };
    }

    protected void startTest(TestData data, Browser browser, String version, Platform platform) {
        //Start logging
        testCase = new TestCase(data, browser.toString(), version, platform.toString());
        System.out.println("***********************************");
        System.out.println("Starting test: " + testCase.getName() + "\n");

        //Set up WebDriver
        if (browser != null) {
            try {
                grid = new SeleniumGrid(browser, version, platform);
                driver = grid.driver;
            } catch(MalformedURLException e) {}
        }
        driver.navigate().to(Constants.homeUrl);
        //Set up WebDriverWait and specify to ignore NoSuchElementException
        wait = new WebDriverWait(driver, Constants.defaultWaitTime, 500);
        wait.ignoring(NoSuchElementException.class);
        //Module setup
        modules = new ModuleManager(driver, wait);
    }

    @AfterMethod(alwaysRun = true)
    public void destroy() {
        //End logging
        System.out.println("\nEnding test: " + testCase.getName());
        System.out.println("RESULT: " + testCase.getResult());
        System.out.println("BROWSER: " + testCase.getBrowserName() + " " + testCase.getBrowserVersion() + " on " + testCase.getPlatformName());
        System.out.println("SCREENSHOT: " + testCase.getScreenshotUrl());
        System.out.println("***********************************");
//        updateJIRA();   
        try {
            driver.quit();
        } catch (WebDriverException e) {
            System.out.println("WARNING: Error closing driver! " + e.getMessage());
        }
    }

    private void updateJIRA() {
        //Skip update if testKey is null, which should only happen in Test_Test 
        if (StringUtils.isNotBlank(testCase.getKey())) {
            System.out.println("Start JIRA update");
            //Perform update
            try {
                ArrayList<String> keys = new ArrayList<String>();
                keys.add(testCase.getKey());
                new JiraTestCaseServiceApi().updateJira(keys, testCase.getResult(), null);
            } catch (Exception e) {
                System.out.println("Error updating JIRA: " + e.getMessage());
            }
            System.out.println("JIRA update complete");
        } else {
            System.out.println("JIRA update skipped.");
        }
    }

    public void takeScreenshot() {
        try {
            testCase.setScreenshotUrl(grid.captureScreen());
        } catch (Throwable e) {
            testCase.setScreenshotUrl("Failed to capture screenshot.");
        }
        
    }
}
