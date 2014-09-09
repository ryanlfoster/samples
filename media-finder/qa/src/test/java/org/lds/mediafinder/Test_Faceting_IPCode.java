package org.lds.mediafinder;

import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.constants.Tab;
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
public class Test_Faceting_IPCode extends TestMaster {

    @Test(groups = "faceting, ipCode", dataProvider = "nodes")
    public void ipCodeFacetsGenerateProperly(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-275", "TC - Faceting: IP code facets generate properly with search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.checkForIPCodeCategories();
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
    
    @Test(groups = "faceting, ipCode", dataProvider = "nodes")
    public void validateOpenUseComposition(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-190", "TC - Faceting: Validate Open Use category composition");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.checkResultsIPCodes(100, Category.OPENUSE);
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
    
    @Test(groups = "faceting, ipCode", dataProvider = "nodes")
    public void validateRequiresApprovalComposition(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-191", "TC - Faceting: Validate Requires Approval category composition");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.removeIPCodeCategory(Category.OPENUSE);
            modules.ipCode.checkResultsIPCodes(100, Category.REQUIRESAPPROVAL);
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
    
    @Test(groups = "faceting, ipCode", dataProvider = "nodes")
    public void validateRestrictedComposition(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-192", "TC - Faceting: Validate Restricted category composition");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.ipCode.removeIPCodeCategory(Category.OPENUSE);
            modules.ipCode.checkResultsIPCodes(100, Category.RESTRICTED);
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
    
    @Test(groups = "faceting, ipCode", dataProvider = "nodes")
    public void ipCodeFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-189", "TC - Faceting: IP code facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.checkForIPCodeCategories();
            modules.ipCode.applyIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.checkResultCountGreater();
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.ipCode.checkResultCountGreater();
            modules.ipCode.removeIPCodeCategory(Category.OPENUSE);
            modules.ipCode.checkResultCountLesser();
            modules.ipCode.removeIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.checkResultCountLesser();
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
    
    @Test(groups = "faceting, ipCode", dataProvider = "nodes", enabled = false)
    public void ipCodeCategoriesProperlyResetWhenReturnedToLandingPage(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-346", "TC - Faceting: IP code categories reset to default on return to landing page");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.ipCode.applyIPCodeCategory(Category.REQUIRESAPPROVAL);
            modules.ipCode.applyIPCodeCategory(Category.RESTRICTED);
            modules.architecture.navigateTo(Tab.LANDINGPAGE);
            modules.search.searchFor(testTerm);
            modules.ipCode.checkIPCodeCategorySelected(Category.OPENUSE);
            modules.ipCode.checkIPCodeCategoryNotSelected(Category.REQUIRESAPPROVAL);
            modules.ipCode.checkIPCodeCategoryNotSelected(Category.RESTRICTED);
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
