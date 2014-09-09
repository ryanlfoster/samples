package org.lds.mediafinder;

import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Allen Sudweeks
 */
public class Test_Faceting_ImageType extends TestMaster {

    @Test(groups = "faceting, imageType", dataProvider = "nodes")
    public void imageTypeFacetsGenerateProperly(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-445", "TC - Faceting: Image type facets generate with search");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.imageType.checkForImageTypeFacets();
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
    
    @Test(groups = "faceting, imageType", dataProvider = "nodes")
    public void imageTypeFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-446", "TC - Faceting: Image type facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.imageType.checkForImageTypeFacets();
            modules.imageType.applyFirstAvailableImageTypeFacet();
            modules.imageType.checkImageTypeAppiedCounts();
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
    
    @Test(groups = "faceting, imageType", dataProvider = "nodes")
    public void removedImageTypeFacetsProperlyFilterResults(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-447", "TC - Faceting: Removed image type facets properly filter results");
            startTest(data, browser, version, platform);

            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor("temple");
            modules.imageType.checkForImageTypeFacets();
            modules.imageType.applyImageTypeFacet(XPath.facSpecificImageType("Photograph"));
            modules.imageType.removeImageTypeFacet(XPath.facSpecificImageType("Photograph"));
            modules.imageType.checkImageTypeRemovedCounts();
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
