package org.lds.mediafinder;

import org.lds.mediafinder.testcase.TestData;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.seleniumgrid.SeleniumGrid;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.utils.XPath;

public class Test_Collections extends TestMaster {

    @Test(groups = "collections", dataProvider = "nodes")
    public void favoritesCreated(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-552", "TC - Collections: On first sign-in, Favorites collection created");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.validateCollectionExists("Favorites");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void favoritesCreatedWhenSharedCollectionExists(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-600", "TC - Collections: Favorites created when user has existing shared collections");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.getAllCollectionIdsForCleanup();
            modules.authentication.logout();
            modules.collections.cleanupCollections();
            modules.collections.createCollectionViaApi("Share");
            modules.collections.shareCollectionViaApi(Constants.cmcInternalUsername);
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.validateCollectionExists("Favorites");     
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void favoritesCannotBeDeleted(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-553", "TC - Collections: Favorites collection cannot be deleted");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void favoritesCannotBeShared(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-556", "TC - Collections: Favorites collection cannot be shared");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
            modules.collections.validateButtonDisabled(XPath.colShareButton);
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void collectionSuccessfullyCreated(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-564", "TC - Collections: Collected successfully created with user as owner");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("CreateTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void currentCollectionChangesToNewCollectionOnSave(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-566", "TC - Collections: Current Collection changes to new collection on save");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("CreateTest");
            modules.collections.validateCurrentCollection("CreateTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void clickingCollectionOpensTray(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-561", "TC - Collections: Clicking a collection opens the collection tray and displays contents");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
            modules.collections.validateCollectionTrayOpen();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void existingCollectionsRetrieved(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-558", "TC - Collections: Existing collections retrieved when loading tree");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("A");
            modules.collections.createCollection("B");
            modules.collections.createCollection("C");
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.validateCollectionExists("A");
            modules.collections.validateCollectionExists("B");
            modules.collections.validateCollectionExists("C");  
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void collectionRemainsWhenLastAssetRemoved(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-592", "TC - Collections: Collection remains when last asset is removed");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
            modules.collections.openCollectionTray();
            modules.collections.clickSearchResultDeleteButton();
            modules.collections.removeAssetFromCollection();
            modules.collections.validateCollectionExists("Favorites");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToFavorites(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-588", "TC - Collections: Asset successfully added to Favorites");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetRemovedFromFavorites(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-589", "TC - Collections: Asset successfully removed from Favorites");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("Favorites");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
            modules.collections.openCollectionTray();
            modules.collections.clickSearchResultDeleteButton();
            modules.collections.removeAssetFromCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToMyCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-573", "TC - Collections: Asset successfully added to My Collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToNewCollection("AddTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetRemovedFromMyCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-590", "TC - Collections: Asset successfully removed from My Collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToNewCollection("RemoveTest");
            modules.collections.selectCollection("RemoveTest");
            modules.collections.clickSearchResultDeleteButton();
            modules.collections.removeAssetFromCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToSharedCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-574", "TC - Collections: Asset successfully added to Shared collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("MyShared");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.collections.closeShareCollectionPanel();
            modules.collections.getAllCollectionIdsForCleanup();
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("MyShared");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetRemovedFromSharedCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-591", "TC - Collections: Asset successfully removed from Shared collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("MyShared");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.collections.closeShareCollectionPanel();
            modules.collections.getAllCollectionIdsForCleanup();
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("MyShared");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
            modules.collections.selectCollection("MyShared");
            modules.collections.clickSearchResultDeleteButton();
            modules.collections.removeAssetFromCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToNewCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-578", "TC - Collections: Asset successfully added to new collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToNewCollection("AddNewTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToOtherCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-577", "TC - Collections: Asset successfully added to other collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("OtherTest");
            modules.collections.selectCollection("Favorites");
            modules.collections.closeCollectionTray();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToNonCurrentCollection("OtherTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void assetAddedToCurrentCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-576", "TC - Collections: Asset successfully added to current collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.clickSearchResultCollectionIcon();
            modules.collections.addAssetToCurrentCollection();
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void unableToEditSharedCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-572", "TC - Collections: User is unable to edit a shared collection");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("EditTest");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.selectCollection("EditTest");
            modules.collections.validateButtonDisabled(XPath.colShareButton);
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
 
    @Test(groups = "collections", dataProvider = "nodes")
    public void collectionCannotBeRenamedToNull(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-570", "TC - Collections: Collection cannot be renamed to null or empty value");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("RenameTest");
            modules.collections.renameCollection("");
            modules.collections.validateCollectionDoesNotExist("");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void renameCollection(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-568", "TC - Collections: Rename a collection user is owner of");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("RenameTest");
            modules.collections.renameCollection("Renamed");
            modules.collections.validateCollectionExists("Renamed");
            modules.collections.validateCollectionDoesNotExist("RenameTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void collectionProperlyDeleted(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-593", "TC - Collections: My Collection properly deleted");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("DeleteTest");
            modules.collections.deleteCollection("DeleteTest");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void sharedCollectionCannotBeDeleted(SeleniumGrid.Browser browser, String version, Platform platform) {
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void sharedMyCollectionProperlyDeleted(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-595", "TC - Collections: Shared My Collection properly deleted");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("DeleteShare");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.collections.closeShareCollectionPanel();
            modules.collections.deleteCollection("DeleteShare");
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
    
    @Test(groups = "collections", dataProvider = "nodes")
    public void deletedCollectionRemovedFromShareeList(SeleniumGrid.Browser browser, String version, Platform platform) {
        try {
            //Test setup
            TestData data = new TestData("CCIF-596", "TC - Collections: Deleted collection properly removed from sharee's list");
            startTest(data, browser, version, platform);
            
            //Test execution
            modules.authentication.loginAndGoToLandingPage();
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.createCollection("DeleteShare");
            modules.collections.openShareCollectionPanel();
            modules.collections.addUsersByUsername(Constants.cmcInternalUsername);
            modules.collections.closeShareCollectionPanel();
            modules.collections.deleteCollection("DeleteShare");
            modules.collections.getAllCollectionIdsForCleanup();
            modules.authentication.logout();
            modules.authentication.loginAndGoToLandingPage(Constants.cmcInternalUsername, Constants.passwordValid);
            modules.search.searchFor(testTerm);
            modules.collections.navigateToCollectionsTab();
            modules.collections.validateCollectionDoesNotExist("DeleteShare");
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
