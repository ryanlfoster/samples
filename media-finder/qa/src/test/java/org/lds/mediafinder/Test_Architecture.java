package org.lds.mediafinder;

import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.constants.Tab;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Houses all architecture tests.
 * @author Allen Sudweeks
 */
public class Test_Architecture extends TestMaster {

    @Test(groups = "architecture", dataProvider = "nodes")
    public void actionHistoryFunctional(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-226", "TC - Architecture: Action history functional");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFirstAvailableFacet();
            modules.architecture.navigateBackward();
            modules.keywords.checkElementNotFound(XPath.facGenericAppliedFacet);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());            
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void backgroundImageCyclesOnPageLoad(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-201", "TC - Architecture: Background image cycles through preselected set on page load");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.architecture.checkImageCycling(5);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());            
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void designIntegrityHeldSizingDownTo768(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-309", "TC - Architecture: Design integrity held when minimizing down to 768 pixels wide");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.architecture.resizeWindowToFullScreen();
            modules.search.searchFor(testTerm);
            modules.architecture.checkDesignConsistencyOnResize(768);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());            
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void feedbackLinkProperlyConnectsToAllegiance(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-419", "TC - Architecture: Feedback link properly connects to an Allegiance feedback form");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.architecture.followLandingPageAllegianceFeedbackLink();
            modules.architecture.checkUrlContains("www.allegiancetech.com");
            modules.architecture.navigateBackward();
            modules.search.searchFor(testTerm);
            modules.architecture.followSearchPageAllegianceFeedbackLink();
            modules.architecture.checkUrlContains("www.allegiancetech.com");
            modules.architecture.navigateBackward();
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void canaryPageFunctionalAndDisplayingCorrectData(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-384", "TC - Architecture: Canary page functional and displaying correct data");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.architecture.navigateToCanaryPage();
            modules.architecture.checkCanaryPageContents();
            modules.architecture.navigateToURL(Constants.homeUrl);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void searchQueryAndAppliedFacetsRememberedOnLogout(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-476", "TC - Architecture: Upon logout or timeout, search query and applied facets remembered");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.keywords.applySpecificFacet("Christ");
            modules.authentication.logout();
            modules.authentication.loginWithValidUserNameAndPassword();
            modules.search.checkSearchTermEquals(testTerm);
            modules.ipCode.checkIPCodeCategorySelected(Category.RESTRICTED);
            modules.keywords.checkSpecificKeywordFacetApplied("Christ");
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void returnToLandingPageRememberedOnLogout(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-477", "TC - Architecture: Upon logout or timeout, having returned to landing page overwrites last search parameters");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.applySpecificFacet("Christ");
            modules.architecture.navigateTo(Tab.LANDINGPAGE);
            modules.authentication.logout();
            modules.authentication.loginWithValidUserNameAndPassword();
            modules.architecture.checkOnSpecifiedTab(Tab.LANDINGPAGE);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void poweredByLogoVisibleOnLandingPage(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-516", "TC - Architecture: \"Powered by Catalog\" logo visible on landing page");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.architecture.checkForPoweredByLogo();
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void poweredByLogoVisibleOnSearchPage(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-517", "TC - Architecture: \"Powered by Catalog\" logo visible on search results page");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.architecture.checkForPoweredByLogo();
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void conditionsOfUseTooltipFunctional(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-526", "TC - Architecture: Conditions of Use tooltip functional");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.architecture.clickElement(XPath.facIPCodeGroupInfoIcon);
            modules.architecture.checkForConditionsOfUseTooltip();
            modules.architecture.mouseOverElementAndPause(modules.search.getSearchResultWithIndex(4));
            modules.architecture.checkForConditionsOfUseTooltip();
            modules.architecture.clickElement(XPath.facConditionsOfUseTooltipClose);
            modules.architecture.checkElementNotFound(XPath.facConditionsOfUseTooltipPanel);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
    
    @Test(groups = "architecture", dataProvider = "nodes")
    public void searchResultIPCodeTooltipsFunctional(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-527", "TC - Architecture: Search result IP Code tooltips functional");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.architecture.clickElement(XPath.resGenericResultIPIcon);
            modules.architecture.checkForIPCodeTooltip();
            modules.architecture.mouseOverElementAndPause(XPath.resGenericResultImage);
            modules.architecture.checkElementNotFound(XPath.resGenericResultIPTooltip);
            modules.authentication.logout();
        } catch (TestException e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Test Exception)>>> " + e.getMessage());
        } catch (Throwable e) {
            takeScreenshot();
            testCase.setResult("<<<Fail (Uncaught Exception)>>> " + e.getMessage());
        }
        //Validate test result
        if (!testCase.getResult().contains("Pass")) {
            Assert.fail(testCase.getResult());
        }
    }
}
