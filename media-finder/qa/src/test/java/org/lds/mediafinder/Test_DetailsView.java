package org.lds.mediafinder;

import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Houses all details view tests.
 * @author Allen Sudweeks
 */
public class Test_DetailsView extends TestMaster {
    
    @Test(groups = "detailsview", dataProvider = "nodes", enabled = false)
    public void detailsViewMetadataProperlyPopulated(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-265", "TC - Details View: Metadata properly populated");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.checkSummaryMetadata();
            modules.detailsView.expandRightsManagementAccordion();
            modules.detailsView.checkRightsManagementMetadata();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.checkKeywordsMetadata();
            modules.detailsView.expandHistoryOfUseAccordion();
            modules.detailsView.checkHistoryOfUseMetadata();
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
    
    @Test(groups = "detailsview", dataProvider = "nodes")
    public void metadataSeparatedIntoFunctioningAccordionTabs(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-450", "TC - Details View: Metadata properly separated into functioning accordion tabs");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.closeSummaryAccordion();
            modules.detailsView.expandRightsManagementAccordion();
            modules.detailsView.closeKeywordsAccordion();
            modules.detailsView.closeRightsManagementAccordion();
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
    
    @Test(groups = "detailsview", dataProvider = "nodes")
    public void clickingKeywordStartsNewSearch(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-441", "TC - Details View: Clicking a keyword starts a new search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.clickFirstAvailableKeyword();
            modules.search.checkSearchTermEqualsSelectedText();
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
    
    @Test(groups = "detailsview", dataProvider = "nodes", enabled = false)
    public void clickingKeywordWithSpecialCharacterStartsNewSearch(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-442", "TC - Details View: Clicking a keyword with special characters successfully starts a new search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFacetContaining("(");
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.clickKeywordContaining("(");
            modules.search.checkSearchTermEqualsSelectedText();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFacetContaining(",");
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.clickKeywordContaining(",");
            modules.search.checkSearchTermEqualsSelectedText();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFacetContaining("\\");
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.clickKeywordContaining("\\");
            modules.search.checkSearchTermEqualsSelectedText();
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.keywords.applyFacetContaining("#");
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.expandKeywordsAccordion();
            modules.detailsView.clickKeywordContaining("#");
            modules.search.checkSearchTermEqualsSelectedText();
            modules.ipCode.removeIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.removeIPCodeCategory(Category.RESTRICTED);
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
    
    @Test(groups = "detailsview", dataProvider = "nodes")
    public void externalIdParsedToRepositorySpecificName(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-518", "TC - Details View: Telescope external id renamed to Telescope Id");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.detailsView.openFirstAvailableDetailsView();
            modules.detailsView.checkExternalIdLabelParsedCorrectly();
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
