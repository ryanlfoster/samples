package org.lds.mediafinder;

import org.lds.mediafinder.constants.AssetType;
import org.lds.mediafinder.constants.Category;
import org.lds.mediafinder.constants.SortOption;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Houses all search tests.
 * @author Allen Sudweeks
 */
public class Test_Search extends TestMaster {

    @Test(groups = "search, faceting", dataProvider = "nodes")
    public void simpleSearchDefaultsToOpenUseCategory(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-186", "TC - Search: Simple search defaults to Open Use category");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
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
    
    @Test(groups = "search", dataProvider = "nodes")
    public void imageFileSearch(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-88", "TC - Search: Image file search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.search.checkResultsAssetType(100, AssetType.IMAGE);
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
    
    @Test(groups = "search", dataProvider = "nodes")
    public void resultCountPresentAndAccurate(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-200", "TC - Search: Result count present and accurate");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor("frog");
            modules.search.checkSearchResultCount();
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

    @Test(groups = "search", dataProvider = "nodes")
    public void defaultSortTypeIsNewest(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-513", "TC - Search: Default sort type is Newest");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.search.checkSelectedSortOption(SortOption.NEWEST);
            modules.search.checkSortOptionIsApplied(SortOption.NEWEST);
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

    @Test(groups = "search", dataProvider = "nodes")
    public void searchResultSorting(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-514", "TC - Search: Search result sorting");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.search.selectSortOption(SortOption.NEWEST);
            modules.search.checkSortOptionIsApplied(SortOption.NEWEST);
            modules.search.selectSortOption(SortOption.OLDEST);
            modules.search.checkSortOptionIsApplied(SortOption.OLDEST);
            modules.search.selectSortOption(SortOption.RELEVANCE);
            modules.search.checkSortOptionIsApplied(SortOption.RELEVANCE);
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

    @Test(groups = "search", dataProvider = "nodes")
    public void searchResultSortingAppliedOnContinuedScroll(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-515", "TC - Search: Search result sorting applied on continued scrolling");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.search.selectSortOption(SortOption.NEWEST);
            modules.search.checkSortOptionIsApplied(SortOption.NEWEST);
            modules.search.loadMoreResults();
            modules.search.checkSortOptionIsApplied(SortOption.NEWEST);
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
