package org.lds.mediafinder.modules;

import java.util.ArrayList;
import java.util.List;
import org.lds.mediafinder.utils.CatalogDao;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.lds.mediafinder.settings.Constants;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.UserType;
import org.lds.mediafinder.utils.XPath;
import org.lds.stack.utils.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class Module_Collections extends ModuleMaster {

    private List<String> collectionIds;
    private String selectedCollectionId;
    
    public Module_Collections(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        collectionIds = new ArrayList<String>();
    }
    
    public void cleanupCollections() throws Exception {      
        System.out.println("Cleaning up " + collectionIds.size() + " collections... ");
        for (String collectionId: collectionIds) {
            System.out.print("Deleting collection with entryId " + collectionId + "... ");
            CatalogDao.deleteCollection(collectionId, Constants.username);
            System.out.println("Done.");
        }
        collectionIds.clear();
        System.out.println("Done cleaning up collections.");
    }
    
    public void navigateToCollectionsTab() throws TestException {
        System.out.print("Navigating to Collections tab... ");
        clickElement(XPath.colCollectionTab);
        waitForElementToBeVisible(XPath.colNewButton);
        System.out.println("Done.");
    }
    
    public void navigateToFiltersTab() throws TestException {
        System.out.print("Navigating to Filters tab... ");
        clickElement(XPath.colFiltersTab);
        waitForElementToBeVisible(XPath.facIPCodeGroupsHeader);
        System.out.println("Done.");
    }
    
    public void openCollectionTray() throws TestException {
        System.out.print("Opening collection tray... ");
        WebElement tray = driver.findElement(By.xpath(XPath.colCollectionTray));
        if (tray.getAttribute("class").contains("up")) {
            System.out.println("Collection tray already open.");
        } else {
            tray.click();
            hardWait(500);
        }
        if (!tray.getAttribute("class").contains("up")) {
            throw new TestException("Collection tray not opened properly.");
        }
        System.out.println("Done.");
    }
    
    public void validateCollectionTrayOpen() throws TestException {
        System.out.print("Checking collection tray open... ");
        WebElement tray = driver.findElement(By.xpath(XPath.colCollectionTray));
        if (!tray.getAttribute("class").contains("up")) {
            throw new TestException("Tray not open when expected.");
        }
        System.out.println("Done.");
    }
    
    public void closeCollectionTray() throws TestException {
        System.out.print("Closing collection tray... ");
        WebElement tray = driver.findElement(By.xpath(XPath.colCollectionTray));
        if (!tray.getAttribute("class").contains("up")) {
            System.out.println("Collection tray already closed.");
        } else {
            tray.click();
            hardWait(1000);
        }
        if (tray.getAttribute("class").contains("up")) {
            throw new TestException("Collection tray not closed properly.");
        }
        System.out.println("Done.");
    }
    
    public void validateCollectionTrayClosed() throws TestException {
        System.out.print("Checking collection tray closed... ");
        WebElement tray = driver.findElement(By.xpath(XPath.colCollectionTray));
        if (tray.getAttribute("class").contains("up")) {
            throw new TestException("Tray not closed when expected.");
        }
        System.out.println("Done.");
    }
    
    public void selectCollection(String name) throws TestException {
        System.out.print("Selecting collection '" + name + "'... ");
        waitForElementToBeVisible(XPath.colCollection(name));
        //Select collection
        WebElement collection = driver.findElement(By.xpath(XPath.colCollection(name)));
        selectedCollectionId = collection.getAttribute("id");
        collection.click();
        //Validate tray opened
        WebElement tray = driver.findElement(By.xpath(XPath.colCollectionTray));
        if (!tray.getAttribute("class").contains("up")) {
            throw new TestException("Collection tray not opened on select as expected.");
        }
        hardWait(1000);
        System.out.println("Done.");
    }
    
    public void createCollection(String name) throws TestException {
        System.out.print("Creating collection '" + name + "'... ");
        //Create collection
        clickElement(XPath.colNewButton);
        WebElement newCollection = driver.switchTo().activeElement();
        newCollection.sendKeys(name);
        newCollection.sendKeys(Keys.ENTER);
        hardWait(1000);
        //Validate created
        try {
            waitForElementToBeVisible(XPath.colSpecificCollectionData(name));
        } catch(NoSuchElementException e) {
            throw new TestException("Collection not created properly.");
        }
        System.out.println("Done.");
    }
    
    public void validateCurrentCollection(String name) throws TestException {
        System.out.print("Checking Current Collection is '" + name + "'... ");
        try {
            WebElement summary = driver.findElement(By.xpath(XPath.colCollectionTray));
            if (!summary.getAttribute("data-name").equals(name)) {
                throw new TestException("Current Collection is not '" + name + "' as expected.");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Collection tray not found.");
        }
        System.out.println("Done.");
    }
    
    public void createCollectionViaApi(String name) throws TestException {
        System.out.print("Creating collection '" + name + "' via API... ");
        try {
            String result = CatalogDao.createCollection(name, Constants.username, Constants.username);
            if (StringUtils.isNotBlank(result) && result.contains("<id>")) {
                result = result.substring(result.indexOf("<id>"), result.indexOf("</id>")).replace("<id>", "");
                collectionIds.add(result);
                selectedCollectionId = result;
            } else {
                throw new Exception("No response from API.");
            }
        } catch(Exception e) {
            throw new TestException("Error creating collection: " + e.getMessage());
        }
        System.out.println("Done.");
    }
    
    public void shareCollectionViaApi(String user) throws TestException {
        System.out.print("Sharing collection id '" + selectedCollectionId + "' with user '" + user + "'... ");
        try {
            CatalogDao.addUserToCollection(selectedCollectionId, user, UserType.CONTRIBUTOR, Constants.username);
        } catch(Exception e) {
            throw new TestException("Error sharing collection: " + e.getMessage());
        }
        System.out.println("Done.");
    }
    
    public void renameCollection(String name) throws TestException {
        System.out.print("Renaming collection to '" + name + "'...");
        //Rename collection
        clickElement(XPath.colRenameButton);
        WebElement collection = driver.switchTo().activeElement();
        collection.sendKeys(name);
        collection.sendKeys(Keys.ENTER);
        hardWait(500);
        System.out.println("Done.");
    }
    
    public void deleteCollection(String name) throws TestException {
        System.out.print("Deleting collection... ");
        //Delete collection
        clickElement(XPath.colDeleteButton);
        waitForElementToBeVisible(XPath.colDeleteDialog);
        clickElement(XPath.colDeleteDialogYesButton);
        hardWait(1000);
        //Validate deleted
        try {
            driver.findElement(By.xpath(XPath.colCollection(name)));
            throw new TestException("Collection not deleted properly.");
        } catch(NoSuchElementException e) {
            //fail silently
        }
        System.out.println("Done.");
    }
    
    public void getAllCollectionIdsForCleanup() throws TestException {
        System.out.print("Retrieving all collection ids for cleanup... ");
        try {
            List<WebElement> collections = driver.findElements(By.xpath(XPath.colGenericCollectionData));
            for (WebElement collection: collections) {
                if (!collectionIds.contains(collection.getAttribute("id"))) {
                    collectionIds.add(collection.getAttribute("id"));
                }                
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Error retrieving collection ids: " + e.getMessage());
        }
        System.out.println("Done.");
    }
    
    public void validateCollectionDoesNotExist(String name) throws TestException {
        System.out.print("Checking that collection '" + name + "' does not exist... ");
        try {
            driver.findElement(By.xpath(XPath.colCollection(name)));
            throw new TestException("Collection exists when not expected.");
        } catch(NoSuchElementException e) {
            //fail silently
        }
        System.out.println("Done.");
    }
    
    public void validateCollectionExists(String name) throws TestException {
        System.out.print("Checking that collection '" + name + "' exists... ");
        try {
            driver.findElement(By.xpath(XPath.colCollection(name)));
        } catch(NoSuchElementException e) {
            throw new TestException("Collection '" + name + "' does not exist when expected.");
        }
        System.out.println("Done.");
    }
    
    public void validateButtonDisabled(String xpath) throws TestException {
        System.out.print("Checking that button '" + xpath + "' is disabled... ");
        try {
            WebElement button = driver.findElement(By.xpath(xpath));
            if (!button.getAttribute("class").contains("disabled")) {
                throw new TestException("Button not disabled when expected. ");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Button not found.");
        }
        System.out.println("Done.");
    }
    
    public void validateButtonEnabled(String xpath) throws TestException {
        System.out.print("Checking that button '" + xpath + "' is enabled... ");
        try {
            WebElement button = driver.findElement(By.xpath(xpath));
            if (StringUtils.isNotBlank(button.getAttribute("class")) && button.getAttribute("class").contains("disabled")) {
                throw new TestException("Button not enabled when expected. ");
            }
        } catch(NoSuchElementException e) {
            throw new TestException("Button not found.");
        }
        System.out.println("Done.");
    }
    
    public void openShareCollectionPanel() throws TestException {
        System.out.print("Opening share panel for collection... ");
        clickElement(XPath.colShareButton);
        waitForLoadingIndicatorToDisappear();
        try {
            driver.findElement(By.xpath(XPath.colShareUserSearch));
        } catch(NoSuchElementException e) {
            throw new TestException("Share panel not opened properly.");
        }
        System.out.println("Done.");
    }
    
    public void closeShareCollectionPanel() throws TestException {
        System.out.print("Closing share panel for collection... ");
        clickElement(XPath.colShareCloseButton);
        try {
            driver.findElement(By.xpath(XPath.colShareUserSearch));
            throw new TestException("Share panel not closed properly.");
        } catch(NoSuchElementException e) {
            //fail silently
        }
        System.out.println("Done.");
    }
    
    public void searchForUser(String searchTerm) throws TestException {
        System.out.print("Searching for user '" + searchTerm + "'... ");
        WebElement searchField = driver.findElement(By.xpath(XPath.colShareUserSearch));
        searchField.sendKeys(searchTerm);
        searchField.sendKeys(Keys.ENTER);
        waitForLoadingIndicatorToDisappear();
        List<WebElement> searchResults = driver.findElements(By.xpath(XPath.colGenericUserSearchResult));
        System.out.println(searchResults.size() + " results found.");
    }
    
    public void addUserToCollection(String name) throws TestException {
        System.out.println("Adding user '" + name + "' to collection... ");
        //Add to collection
        clickElement(XPath.colSpecificUserSearchResultAddButton(name));
        waitForLoadingIndicatorToDisappear();
        //Verify added
        try {
            driver.findElement(By.xpath(XPath.colSpecificSharedUser(name)));
        } catch(NoSuchElementException e) {
            throw new TestException("User not added to collection properly.");
        }
        System.out.println("Done.");
    }
    
    public void removeUserFromCollection(String username) throws TestException {
        System.out.print("Removing user '" + username +"' from collection... ");
        //Remove from collection
        clickElement(XPath.colSpecificSharedUserDeleteButton(username));
        waitForLoadingIndicatorToDisappear();
        //Verify removed
        try {
            driver.findElement(By.xpath(XPath.colSpecificSharedUser(username)));
            throw new TestException("User not removed from collection properly.");
        } catch(NoSuchElementException e) {
            //fail silently
        }
        System.out.println("Done.");
    }
    
    public void addUsersByUsername(String users) throws TestException {
        System.out.print("Sharing collection with users '" + users + "'... ");
        //Share collection
        clickElement(XPath.colShareBulkAddLink);
        WebElement textArea = driver.findElement(By.xpath(XPath.colShareBulkUserSearch));
        textArea.sendKeys(users);
        clickElement(XPath.colShareBulkAddButton);
        waitForLoadingIndicatorToDisappear();
        //Verify shared
        String[] names = users.replaceAll(" ", "").split(",");
        for (String name: names) {
            try {
                driver.findElement(By.xpath(XPath.colSpecificSharedUser(name)));
            } catch(NoSuchElementException e) {
                throw new TestException("User '" + name + "' not added to collection properly.");
            }
        }
        System.out.println("Done.");
    }
    
    public void closeBulkShare() throws TestException {
        System.out.print("Closing bulk share... ");
        clickElement(XPath.colShareBulkCancelButton);
        try {
            driver.findElement(By.xpath(XPath.colShareUserSearch));
        } catch(NoSuchElementException e) {
            throw new TestException("Bulk share not closed properly.");
        }
        System.out.println("Done.");
    }
    
    public void clickSearchResultCollectionIcon() throws TestException {
        System.out.print("Clicking first available search result collection button... ");
        WebElement result = driver.findElement(By.xpath(XPath.resGenericResult));
        selectedExternalId = result.getAttribute("data-qa-externalid");
        clickElement(XPath.resGenericResultCollectionButton);
        waitForElementToBeVisible(XPath.colAddDialog);
        System.out.println("Done.");
    }
    
    public void clickSearchResultDeleteButton() throws TestException {
        System.out.print("Clicking selected asset search result delete button... ");
        clickElement(XPath.colSpecificResultDeleteButton(selectedExternalId));
        waitForElementToBeVisible(XPath.colDeleteDialog);
        System.out.println("Done.");
    }
    
    public void closeAddToCollectionDialog() throws TestException {
        System.out.print("Closing add to collection dialog... ");
        clickElement(XPath.colAddDialogCancelButton);
        System.out.println("Done.");
    }
    
    public void addAssetToCurrentCollection() throws TestException {
        System.out.println("Adding asset to current collection... ");
        //Add to collection
        clickElement(XPath.colAddDialogAddButton);
        hardWait(500);
        //Verify added
        openCollectionTray();
        System.out.print("Verifying asset added to collection... ");
        try {
            driver.findElement(By.xpath(XPath.colSpecificResult(selectedExternalId)));
        } catch(NoSuchElementException e) {
            throw new TestException("Asset not added to current collection properly.");
        }
        System.out.println("Done.");
        closeCollectionTray();
        System.out.println("Done adding asset to current collection.");
    }
    
    public void addAssetToNonCurrentCollection(String name) throws TestException {
        System.out.println("Adding asset to collection '" + name + "'... ");
        //Add to collection
        clickElement(XPath.colAddDialogChangeCollectionButton);
        hardWait(500);
        clickElement(XPath.colAddDialogCollection(name));
        hardWait(500);
        clickElement(XPath.colAddDialogAddButtonDeep);
        hardWait(500);
        //Verify added
        System.out.println("Verifying asset added to collection '" + name + "'... ");
        selectCollection(name);
        hardWait(500);
        try {
            driver.findElement(By.xpath(XPath.colSpecificResult(selectedExternalId)));
        } catch(NoSuchElementException e) {
            throw new TestException("Asset not added to collection '" + name + "' properly.");
        }
        System.out.println("Done verifying asset added to collection '" + name + "'.");
        closeCollectionTray();
        System.out.println("Done adding asset to collection '" + name + "'.");
    }
    
    public void addAssetToNewCollection(String name) throws TestException {
        System.out.println("Adding asset to new collection '" + name + "'... ");
        //Create collection
        clickElement(XPath.colAddDialogChangeCollectionButton);
        hardWait(500);
        clickElement(XPath.colAddDialogNewCollectionButton);
        WebElement collection = driver.switchTo().activeElement();
        collection.sendKeys(name);
        collection.sendKeys(Keys.ENTER);
        hardWait(1000);
        //Verify created
        try {
            driver.findElement(By.xpath(XPath.colAddDialogCollection(name)));
        } catch(NoSuchElementException e) {
            throw new TestException("New collection '" + name + "' not created properly.");
        }
        //Add to collection
        clickElement(XPath.colAddDialogCollection(name));
        clickElement(XPath.colAddDialogAddButtonDeep);
        hardWait(1500);
        //Verify added
        System.out.println("Verifying asset added to collection '" + name + "'... ");
        selectCollection(name);
        hardWait(500);
        try {
            driver.findElement(By.xpath(XPath.colSpecificResult(selectedExternalId)));
        } catch(NoSuchElementException e) {
            throw new TestException("Asset not added to new collection '" + name + "' properly.");
        }
        System.out.println("Done verifying asset added to new collection '" + name + "'.");
        closeCollectionTray();
        System.out.println("Done adding asset to new collection '" + name + "'.");
    }
    
    public void removeAssetFromCollection() throws TestException {
        System.out.print("Removing asset from collection... ");
        //Remove from collection
        clickElement(XPath.colDeleteDialogYesButton);
        hardWait(500);
        //Verify removed
        try {
            driver.findElement(By.xpath(XPath.colSpecificResult(selectedExternalId)));
            throw new TestException("Asset not removed from collection properly.");
        } catch(NoSuchElementException e) {
            //fail silently
        }
        System.out.println("Done.");
    }
}
