package org.lds.mediafinder.modules;

import java.util.ArrayList;
import java.util.List;
import org.lds.mediafinder.constants.AssetType;
import org.lds.mediafinder.constants.RepositoryName;
import org.lds.mediafinder.utils.Rendition;
import org.lds.mediafinder.utils.SearchEntry;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.lds.stack.utils.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all asset preview methods.
 * @author Allen Sudweeks
 */
public class Module_DetailsView extends ModuleMaster {

    private SearchEntry entry;
    
    public Module_DetailsView(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Opens the details panel for the first available search result asset by
     * clicking the search result image directly (not waiting for preview panel).
     * @throws TestException 
     */
    public void openFirstAvailableDetailsView() throws TestException {
        System.out.print("Opening first available details view... ");
        //Record external ID
        waitForElementToBeVisible(XPath.resGenericResultImage);
        selectedExternalId = driver.findElement(By.xpath(XPath.resGenericResult)).getAttribute("data-qa-externalid");
        //Click search result image and wait for details panel
        clickElement(XPath.resGenericResultImage);
        try {
            waitForElementToBeVisible(XPath.detDetailsPanel);
        } catch(TestException e) {
            //Retry (for Chrome)
            clickElement(XPath.resGenericResultImage);
        }        
        if (driver.findElement(By.xpath(XPath.detDetailsPanel)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Details panel never displayed.");
        }
    }
    
    /**
     * Opens the detailed panel for the asset with the specified external id by 
     * clicking the search result image directly (not waiting for preview panel).
     * @param externalId
     * @throws TestException 
     */
    public void openSpecificAssetDetailsView(String externalId) throws TestException {
        System.out.print("Opening details view for asset with external ID '" + externalId + "'... ");
        //Record external ID
        selectedExternalId = driver.findElement(By.xpath(XPath.resSpecificResultImage(externalId))).getAttribute("data-qa-externalid");
        //Click search result image and wait for details panel
        clickElement(XPath.resSpecificResultImage(externalId));
        waitForElementToBeVisible(XPath.detDetailsPanel);
        if (driver.findElement(By.xpath(XPath.detDetailsPanel)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Details panel never displayed.");
        }
    }
    
    /**
     * Checks to ensure that all required fields on the details panel are populated.
     * @throws TestException 
     */
    public void checkPanelForBasicElements() throws TestException {
        System.out.print("Validating basic elements present on details view panel... ");
        if (!driver.findElement(By.xpath(XPath.detImage)).isDisplayed()) {
            throw new TestException("Image not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detCloseButton)).isDisplayed()) {
            throw new TestException("Close button not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detDownloadButton)).isDisplayed()) {
            throw new TestException("Download button not displayed.");
        }
//        if (!driver.findElement(By.xpath(XPath.detBundleButton)).isDisplayed()) {
//            throw new TestException("Bundle button not displayed.");
//        }
        if (!driver.findElement(By.xpath(XPath.detIPCode)).isDisplayed()) {
            throw new TestException("IP code not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detFullTitle)).isDisplayed()) {
            throw new TestException("Full title not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detFullDescription)).isDisplayed()) {
            throw new TestException("Full description not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detLanguage)).isDisplayed()) {
            throw new TestException("Language not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detAssetType)).isDisplayed()) {
            throw new TestException("Asset type not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detExternalId)).isDisplayed()) {
            throw new TestException("External ID not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detRepositoryName)).isDisplayed()) {
            throw new TestException("Repository name not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detContractId)).isDisplayed()) {
            throw new TestException("Contract ID not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detTextLimitations)).isDisplayed()) {
            throw new TestException("Text limitations not displayed.");
        }
        if (!driver.findElement(By.xpath(XPath.detLocked)).isDisplayed()) {
            throw new TestException("Locked value not displayed.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Gathers data from all required details panel fields, conducts an API call
     * to the Catalog for the asset with the same external id, and compares the
     * two data values. Cleanup is applied to data only where necessary to produce 
     * similar format to that which Media Finder actually displays.
     * @throws TestException 
     */
    public void checkSummaryMetadata() throws TestException {
        System.out.print("Checking details view Summary metadata against Catalog XML... ");
        //Wait for elements to load
        waitForElementToBeVisible(XPath.detFullTitle);
        //Retrieve details panel values
        String fullTitle = driver.findElement(By.xpath(XPath.detFullTitle)).getText().trim();
        String fullDescription = driver.findElement(By.xpath(XPath.detFullDescription)).getText().trim();
        //Language is not always present
        String language = null;
        try {
            language = driver.findElement(By.xpath(XPath.detLanguage)).getText().trim();
            if (StringUtils.isNotBlank(language)) {
                language = language.trim();
            }
        } catch(NoSuchElementException e) {
            //Fail silently
        }
        String filename = driver.findElement(By.xpath(XPath.detFilename)).getText();
        if (StringUtils.isNotBlank(filename)) {
            filename = filename.trim();
        }
        String seoFilename = driver.findElement(By.xpath(XPath.detSEOFilename)).getText();
        if (StringUtils.isNotBlank(seoFilename)) {
            seoFilename = seoFilename.trim();
        }
        String additionalInfo = driver.findElement(By.xpath(XPath.detAdditionalInfo)).getText();
        if (StringUtils.isNotBlank(additionalInfo)) {
            additionalInfo = additionalInfo.trim();
        }
        String callNumber = driver.findElement(By.xpath(XPath.detCallNumber)).getText();
        if (StringUtils.isNotBlank(callNumber)) {
            callNumber = callNumber.trim();
        }
        AssetType assetType = AssetType.valueOf(driver.findElement(By.xpath(XPath.detAssetType)).getText().trim());
        RepositoryName repositoryName = RepositoryName.valueOf(driver.findElement(By.xpath(XPath.detRepositoryName)).getText().trim());
        //Retrieve and parse asset from Catalog
        if (entry == null || !entry.getExternalId().equals(selectedExternalId)) {
            try {
                entry = new SearchEntry(selectedExternalId);
            } catch (Throwable e) {
                throw new TestException("Error parsing Catalog asset: " + e.getMessage());
            }
        }
        //Validate
        if (!entry.getTitle().equals(fullTitle)) {
            throw new TestException("Details full title: " + fullTitle + ", Catalog asset full title: " + entry.getTitle());
        }
        if (!entry.getDescription().equals(fullDescription)) {
            throw new TestException("Details full description: " + fullDescription + ", Catalog asset full description: " + entry.getDescription());
        }
        if (StringUtils.isNotBlank(language) && !entry.getLanguage().equals(language)) {
            throw new TestException("Details language: " + language + ", Catalog asset language: " + entry.getLanguage());
        }
        if (entry.getAssetType() != assetType) {
            throw new TestException("Details asset type: " + assetType + ", Catalog asset type: " + entry.getAssetType());
        }
        if (!entry.getRepositoryName().equals(repositoryName)) {
            throw new TestException("Details repository name: " + assetType + ", Catalog asset repository name: " + entry.getRepositoryName());
        }
        for (Rendition rendition: entry.getRenditions()) {
            if (StringUtils.isNotBlank(rendition.getFileName())) {
                if (!filename.contains(rendition.getFileName())) {
                    throw new TestException("Catalog asset filename '" + rendition.getFileName() + "' not found on details panel.");
                }
            }                
        }
        if (!entry.getSeoFilename().equals(seoFilename)) {
            throw new TestException("Details SEO filename: " + seoFilename + ", Catalog asset SEO filename: " + entry.getSeoFilename());
        }
        if (!entry.getAdditionalInfo().equals(additionalInfo)) {
            throw new TestException("Details additional info: " + additionalInfo + ", Catalog asset additional info: " + entry.getAdditionalInfo());
        }
        if (!entry.getCallNumber().equals(callNumber)) {
            throw new TestException("Details call number: " + callNumber + ", Catalog asset call number: " + entry.getCallNumber());
        }
        System.out.println("Done.");
    }
    
    /**
     * Gathers data from all required details panel fields, conducts an API call
     * to the Catalog for the asset with the same external id, and compares the
     * two data values. Cleanup is applied to data only where necessary to
     * produce similar format to that which Media Finder actually displays.
     *
     * @throws TestException
     */
    public void checkKeywordsMetadata() throws TestException {
        System.out.print("Checking details view Keywords metadata against Catalog XML... ");
        //Wait for elements to load
        waitForElementToBeVisible(XPath.detGenericKeyword);
        //Retrieve details panel keywords
        ArrayList<String> keywords = new ArrayList<String>();
        List<WebElement> keywordElements = driver.findElements(By.xpath(XPath.detGenericKeyword));
        for (WebElement element : keywordElements) {
            keywords.add(element.getText().trim());
        }
        //Retrieve and parse asset from Catalog
        if (entry == null || !entry.getExternalId().equals(selectedExternalId)) {
            try {
                entry = new SearchEntry(selectedExternalId);
            } catch (Throwable e) {
                throw new TestException("Error parsing Catalog asset: " + e.getMessage());
            }
        }
        //Validate
        for (String keyword : entry.getKeywords()) {
            if (!keywords.contains(keyword)) {
                throw new TestException("Catalog asset keyword '" + keyword + "' not displayed.");
            }
        }
        System.out.println("Done.");
    }
    
    /**
     * Gathers data from all required details panel fields, conducts an API call
     * to the Catalog for the asset with the same external id, and compares the
     * two data values. Cleanup is applied to data only where necessary to
     * produce similar format to that which Media Finder actually displays.
     *
     * @throws TestException
     */
    public void checkRightsManagementMetadata() throws TestException {
        System.out.print("Checking details view Rights Management metadata against Catalog XML... ");
        //Wait for elements to load
        waitForElementToBeVisible(XPath.detIPCode);
        //Retrieve details panel values
        String ipCode = driver.findElement(By.xpath(XPath.detIPCode)).getText().trim();
        String contractId = driver.findElement(By.xpath(XPath.detContractId)).getText().trim();
        String textLimitations = driver.findElement(By.xpath(XPath.detTextLimitations)).getText().trim();
        String locked = driver.findElement(By.xpath(XPath.detLocked)).getText().trim();
        String creditLine = driver.findElement(By.xpath(XPath.detCreditLine)).getText();
        if (StringUtils.isNotBlank(creditLine)) {
            creditLine = creditLine.trim();
        }
        String creditLineRequired = driver.findElement(By.xpath(XPath.detCreditLineRequired)).getText();
        if (StringUtils.isNotBlank(creditLineRequired)) {
            creditLineRequired = creditLineRequired.trim();
        }
        //Retrieve and parse asset from Catalog
        if (entry == null || !entry.getExternalId().equals(selectedExternalId)) {
            try {
                entry = new SearchEntry(selectedExternalId);
            } catch (Throwable e) {
                throw new TestException("Error parsing Catalog asset: " + e.getMessage());
            }
        }
        //Validate
        if (!entry.getIpCode().equals(ipCode)) {
            throw new TestException("Details IP code: " + ipCode + ", Catalog asset IP code: " + entry.getIpCode());
        }
        if (!entry.getContractId().equals(contractId)) {
            throw new TestException("Details contract ID: " + contractId + ", Catalog asset contract ID: " + entry.getContractId());
        }
        if (!entry.getTextLimitations().equals(textLimitations)) {
            throw new TestException("Details text limitations: " + textLimitations + ", Catalog asset text limitations: " + entry.getTextLimitations());
        }
        if (!entry.getLocked().equals(locked)) {
            throw new TestException("Details locked: " + locked + ", Catalog asset locked: " + entry.getLocked());
        }
        if (!entry.getCreditLine().equals(creditLine)) {
            throw new TestException("Details credit line: " + creditLine + ", Catalog asset credit line: " + entry.getCreditLine());
        }
        if (!entry.getCreditLineRequired().equals(creditLineRequired)) {
            throw new TestException("Details credit line required: " + creditLineRequired + ", Catalog asset credit line required: " + entry.getCreditLineRequired());
        }
        System.out.println("Done.");
    }
    
    /**
     * Gathers data from all required details panel fields, conducts an API call
     * to the Catalog for the asset with the same external id, and compares the
     * two data values. Cleanup is applied to data only where necessary to
     * produce similar format to that which Media Finder actually displays.
     *
     * @throws TestException
     */
    public void checkHistoryOfUseMetadata() throws TestException {
        System.out.print("Checking details view History of Use metadata against Catalog XML... ");
        //Wait for elements to load
        waitForElementToBeVisible(XPath.detHistoryOfUse);
        //Retrieve details panel values
        String history = driver.findElement(By.xpath(XPath.detHistoryOfUse)).getText().trim();
        //Retrieve and parse asset from Catalog
        if (entry == null || !entry.getExternalId().equals(selectedExternalId)) {
            try {
                entry = new SearchEntry(selectedExternalId);
            } catch (Throwable e) {
                throw new TestException("Error parsing Catalog asset: " + e.getMessage());
            }
        }
        //Validate
        if (!entry.getHistoryOfUse().equals(history)) {
            throw new TestException("Details history of use: " + history + ", Catalog asset history of use: " + entry.getHistoryOfUse());
        }
        System.out.println("Done.");
    }
    
    /**
     * Closes the details panel by clicking the close button.
     * @throws TestException 
     */
    public void closeDetailsView() throws TestException {
        //Click close button
        clickElement(XPath.detCloseButton);
        //Make sure panel is not still visible
        if (driver.findElement(By.xpath(XPath.detDetailsPanel)).isDisplayed()) {
            throw new TestException("Details view panel not closed as expected.");
        }
    }
    
    /**
     * Checks that each keyword displayed on the details view does not overlap
     * the bottom margin of the details view panel.
     * @throws TestException 
     */
    public void checkAllKeywordsVisible() throws TestException {
        //Get bounds of details view
        WebElement detailsPanel = driver.findElement(By.xpath(XPath.detDetailsPanel));
        //Get upper right corner and add vertical size to find lower margin y value
        int detLowerYValue = detailsPanel.getLocation().getY() + detailsPanel.getSize().getHeight();
        //Check each keyword's lowerYValue is not lower than the details view's lowerYValue
        List<WebElement> keywords = driver.findElements(By.xpath(XPath.detGenericKeyword));
        for (WebElement keyword: keywords) {
            //Get upper right corner and add vertical size to find lower margin y value
            int keyLowerYValue = keyword.getLocation().getY() + keyword.getSize().getHeight();
            //Fail if the keyword bottom margin is lower than the details view bottom margin
            if (keyLowerYValue >= detLowerYValue) {
                throw new TestException("Keyword '" + keyword.getText() + "' for asset with "
                        + "external id '" + selectedExternalId + "' "
                        + "overlaps details view bottom margin.");
            }
        }
    }
    
    /**
     * Returns the URL found as the deep link on an asset's details view.
     * @return url
     * @throws TestException 
     */
    public String getDeepLinkURL() throws TestException {
        //Get URL
        String url = driver.findElement(By.xpath(XPath.detDeepLink)).getText().trim();
        return url;
    }
    
    /**
     * Clicks the first available keyword and waits for load to complete.
     * @throws TestException 
     */
    public void clickFirstAvailableKeyword() throws TestException {
        System.out.print("Clicking first available keyword... ");
        waitForElementToBeVisible(XPath.detGenericKeyword);
        WebElement keyword = driver.findElement(By.xpath(XPath.detGenericKeyword));
        selectedText = keyword.getText().trim();
        keyword.click();
        waitForLoadingIndicatorToDisappear();
        System.out.println("Done.");
    }
    
    /**
     * Clicks the keyword with the specified text and waits for load to complete.
     * @param text
     * @throws TestException 
     */
    public void clickSpecificKeyword(String text) throws TestException {
        System.out.print("Clicking the '" + text + "' keyword... ");
        waitForElementToBeVisible(XPath.detGenericKeyword);
        WebElement keyword = driver.findElement(By.xpath(XPath.detSpecificKeyword(text)));
        selectedText = keyword.getText().trim();
        keyword.click();
        waitForLoadingIndicatorToDisappear();
        System.out.println("Done.");
    }
    
    /**
     * Clicks the first keyword containing the specified text, and waits for 
     * load to complete.
     * @param text
     * @throws TestException 
     */
    public void clickKeywordContaining(String text) throws TestException {
        System.out.print("Finding a keyword containing '" + text + "'... ");
        //Get keywords
        waitForElementToBeVisible(XPath.detGenericKeyword);
        List<WebElement> keywords = driver.findElements(By.xpath(XPath.detGenericKeyword));
        //Find one containing text
        Boolean found = false;
        for (WebElement keyword: keywords) {
            if (keyword.getText().contains(text)) {
                found = true;
                System.out.println("Done.");
                clickSpecificKeyword(keyword.getText());
                break;
            }
        }
        //If not found, fail test
        if (!found) {
            throw new TestException("No keyword containing '" + text + "' found.");
        }
    }
    
    /**
     * Opens the Keywords section in the details view accordion, if not already
     * open.
     * @throws TestException 
     */
    public void expandKeywordsAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detKeywordsSection));
        //Determine if closed before attempting to expand.
        if (section.getAttribute("class").contains("closed")) {
            System.out.print("Expanding details view Keywords section... ");
            clickElement(XPath.detKeywordsSection);
            //Validate section opened
            if (section.getAttribute("class").contains("open")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to expand details view Keywords section.");
            }            
        } else {
            System.out.print("Details view Keywords section already expanded.");
        }
    }
    
    /**
     * Closes the Keywords section in the details view accordion, if not already
     * closed.
     *
     * @throws TestException
     */
    public void closeKeywordsAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detKeywordsSection));
        //Determine if open before attempting to close.
        if (section.getAttribute("class").contains("open")) {
            System.out.print("Closing details view Keywords section... ");
            clickElement(XPath.detKeywordsSectionHeader);
            //Validate section closed
            if (section.getAttribute("class").contains("closed")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to close details view Keywords section.");
            }
        } else {
            System.out.print("Details view Keywords section already closed.");
        }
    }
    
    /**
     * Opens the Summary section in the details view accordion, if not already
     * open.
     *
     * @throws TestException
     */
    public void expandSummaryAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detSummarySection));
        //Determine if closed before attempting to expand.
        if (section.getAttribute("class").contains("closed")) {
            System.out.print("Expanding details view Summary section... ");
            clickElement(XPath.detSummarySectionHeader);
            //Validate section opened
            if (section.getAttribute("class").contains("open")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to expand details view Summary section.");
            }
        } else {
            System.out.print("Details view Summary section already expanded.");
        }
    }
    
    /**
     * Closes the Summary section in the details view accordion, if not already
     * closed.
     *
     * @throws TestException
     */
    public void closeSummaryAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detSummarySection));
        //Determine if open before attempting to close.
        if (section.getAttribute("class").contains("open")) {
            System.out.print("Closing details view Summary section... ");
            clickElement(XPath.detSummarySectionHeader);
            //Validate section closed
            if (section.getAttribute("class").contains("closed")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to close details view Summary section.");
            }
        } else {
            System.out.print("Details view Summary section already closed.");
        }
    }
    
    /**
     * Opens the Rights Management section in the details view accordion, if not
     * already open.
     *
     * @throws TestException
     */
    public void expandRightsManagementAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detRightsManagementSection));
        //Determine if closed before attempting to expand.
        if (section.getAttribute("class").contains("closed")) {
            System.out.print("Expanding details view Rights Management section... ");
            clickElement(XPath.detRightsManagementSectionHeader);
            //Validate section opened
            if (section.getAttribute("class").contains("open")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to expand details view Rights Management section.");
            }
        } else {
            System.out.print("Details view Rights Management section already expanded.");
        }
    }
    
    /**
     * Closes the Rights Management section in the details view accordion, if not 
     * already closed.
     *
     * @throws TestException
     */
    public void closeRightsManagementAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detRightsManagementSection));
        //Determine if open before attempting to close.
        if (section.getAttribute("class").contains("open")) {
            System.out.print("Closing details view Rights Management section... ");
            clickElement(XPath.detRightsManagementSectionHeader);
            //Validate section closed
            if (section.getAttribute("class").contains("closed")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to close details view Rights Management section.");
            }
        } else {
            System.out.print("Details view Rights Management section already closed.");
        }
    }
    
    /**
     * Opens the History of Use section in the details view accordion, if not
     * already open.
     *
     * @throws TestException
     */
    public void expandHistoryOfUseAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detHistoryOfUseSection));
        //Determine if closed before attempting to expand.
        if (section.getAttribute("class").contains("closed")) {
            System.out.print("Expanding details view History of Use section... ");
            clickElement(XPath.detHistoryOfUseSectionHeader);
            //Validate section opened
            if (section.getAttribute("class").contains("open")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to expand details view History of Use section.");
            }
        } else {
            System.out.print("Details view History of Use section already expanded.");
        }
    }
    
    /**
     * Closes the History of Use section in the details view accordion, if not 
     * already closed.
     *
     * @throws TestException
     */
    public void closeHistoryOfUseAccordion() throws TestException {
        WebElement section = driver.findElement(By.xpath(XPath.detHistoryOfUseSection));
        //Determine if open before attempting to close.
        if (section.getAttribute("class").contains("open")) {
            System.out.print("Closing details view History of Use section... ");
            clickElement(XPath.detHistoryOfUseSectionHeader);
            //Validate section closed
            if (section.getAttribute("class").contains("closed")) {
                System.out.println("Done.");
            } else {
                throw new TestException("Failed to close details view History of Use section.");
            }
        } else {
            System.out.print("Details view History of Use section already closed.");
        }
    }
    
    /**
     * Validates the parsing of the External ID field to the proper repository-specific ID label.
     * @throws TestException 
     */
    public void checkExternalIdLabelParsedCorrectly() throws TestException {
        System.out.print("Checking External ID field label has been parsed correctly... ");
        RepositoryName repository = RepositoryName.valueOf(driver.findElement(By.xpath(XPath.detRepositoryName)).getText().trim());
        switch (repository) {
            case TELESCOPE:
                if (!driver.findElement(By.xpath(XPath.detExternalIdLabel)).getText().trim().equalsIgnoreCase("Telescope ID")) {
                    throw new TestException("Label not parsed correctly.");
                }
                break;
            default:
                throw new TestException("Unknown repository name.");
        }
        System.out.println("Done.");
    }
}
