package org.lds.mediafinder;

import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid.Browser;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * For testing individual components only
 * @author Allen Sudweeks
 */
public class Test_Test extends TestMaster {
    
    @Test(groups = "local", dataProvider = "nodes", enabled = false)
    public void test(Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-586", "TC - Collections: Shared collection cannot be deleted");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("ShareTest");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.collections.closeShareCollectionPanel();
            modules.collections.getAllCollectionIdsForCleanup();
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("ShareTest");
            modules.collections.validateButtonDisabled(XPath.colDeleteButton);
            modules.collections.getAllCollectionIdsForCleanup();
            modules.collections.cleanupCollections();
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
