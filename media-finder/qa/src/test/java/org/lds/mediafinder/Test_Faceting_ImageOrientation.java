package org.lds.mediafinder;

import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Houses all faceting tests.
 * @author Allen Sudweeks
 */
public class Test_Faceting_ImageOrientation extends TestMaster {
    
    @Test(groups = "faceting, imageOrientation", dataProvider = "nodes")
    public void imageOrientationFacetsGenerateProperly(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-519", "TC - Faceting: Image orientation facets generate with search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.imageOrientation.checkForImageOrientationFacets();
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
    
    @Test(groups = "faceting, imageOrientation", dataProvider = "nodes")
    public void imageOrientationFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-520", "TC - Faceting: Image orientation facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.imageOrientation.checkForImageOrientationFacets();
            modules.imageOrientation.applyFirstAvailableImageOrientationFacet();
            modules.imageOrientation.checkImageOrientationAppiedCounts();
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
    
    @Test(groups = "faceting, imageOrientation", dataProvider = "nodes")
    public void removedImageOrientationFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-521", "TC - Faceting: Removed image orientation facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.imageOrientation.checkForImageOrientationFacets();
            modules.imageOrientation.applyImageOrientationFacet(XPath.facImageOrientationLandscape);
            modules.imageOrientation.removeImageOrientationFacet(XPath.facImageOrientationLandscape);
            modules.imageOrientation.checkImageOrientationRemovedCounts();
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
