package org.lds.mediafinder;

import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Allen Sudweeks
 */
public class Test_Faceting_Keywords extends TestMaster {

    @Test(groups = "faceting, keywords", dataProvider = "nodes")
    public void keywordFacetsGenerateProperly(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-224", "TC - Faceting: Keyword facets generate with search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.checkForKeywordFacets();
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
    
    @Test(groups = "faceting, keywords", dataProvider = "nodes", enabled = false)
    public void keywordFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-103", "TC - Faceting: Keyword facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.checkForKeywordFacets();
            modules.keywords.applyFirstAvailableFacet();
            modules.keywords.checkResultCountLesser();
            modules.keywords.applyFirstAvailableFacet();
            modules.keywords.checkResultCountLesser();
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
    
    @Test(groups = "faceting, keywords", dataProvider = "nodes", enabled = false)
    public void removedFacetsProperlyRefreshResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-125", "TC - Faceting: Removed keyword facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.checkForKeywordFacets();
            modules.keywords.applyFirstAvailableFacet();
            modules.keywords.applyFirstAvailableFacet();
            modules.keywords.applyFirstAvailableFacet();
            modules.keywords.removeLastAvailableFacet();
            modules.keywords.checkResultCountGreater();
            modules.keywords.removeLastAvailableFacet();
            modules.keywords.checkResultCountGreater();
            modules.keywords.removeLastAvailableFacet();
            modules.keywords.checkResultCountGreater();
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
    
    @Test(groups = "faceting, keywords", dataProvider = "nodes", enabled = false)
    public void facetsWithSpecialPunctuationProperlyAppliedAndRemoved(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-161", "TC - Faceting: Keyword facets with special punctuation");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFacetContaining("(");
            modules.keywords.removeFacetContaining("(");
            modules.keywords.applyFacetContaining(",");
            modules.keywords.removeFacetContaining(",");
            modules.keywords.applyFacetContaining("\\");
            modules.keywords.removeFacetContaining("\\");
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.keywords.applyFacetContaining("#");
            modules.keywords.removeFacetContaining("#");
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
    
    @Test(groups = "faceting, keywords", dataProvider = "nodes")
    public void facetsWithLeadingZeroesAreProperlyAppliedAndRemoved(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-394", "TC - Faceting: Keyword facets with leading zeros");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.keywords.applyFacetStartingWith("0");
            modules.keywords.removeFacetStartingWith("0");
            modules.keywords.checkResultCountGreater();
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
