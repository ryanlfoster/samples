package org.lds.mediafinder.modules;

import org.lds.mediafinder.utils.SearchEntry;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all asset preview methods.
 * @author Allen Sudweeks
 */
public class Module_Preview extends ModuleMaster {
    
    public Module_Preview(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Opens the preview panel of the first available search result asset by 
     * hovering the mouse over the result image.
     * @throws TestException 
     */
    public void openFirstAvailablePreview() throws TestException {
        System.out.print("Opening first available preview panel... ");
        //Record external id
        selectedExternalId = driver.findElement(By.xpath(XPath.resGenericResult)).getAttribute("data-qa-externalid");
        //Mouse over result image and wait for panel
        mouseOverElement(XPath.resGenericResultImage);
        waitForElementToBeVisible(XPath.prePreviewPanel);
        if (driver.findElement(By.xpath(XPath.prePreviewPanel)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Preview panel not found when expected.");
        }
    }
    
    /**
     * Opens the preview panel of the asset with the specified external id by
     * hovering the mouse over the result image.
     * @param externalId
     * @throws TestException 
     */
    public void openSpecificAssetPreview(String externalId) throws TestException {
        System.out.print("Opening preview panel for asset with external id '" + externalId + "'... ");
        //Record external id
        selectedExternalId = driver.findElement(By.xpath(XPath.resSpecificResultImage(externalId))).getAttribute("data-qa-externalid");
        //Mouse over result image and wait for panel
        mouseOverElement(XPath.resSpecificResultImage(externalId));
        waitForElementToBeVisible(XPath.prePreviewPanel);
        if (driver.findElement(By.xpath(XPath.prePreviewPanel)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Preview panel not found when expected.");
        }
    }
   
    /**
     * Checks that the required elements are populated on the preview panel.
     * @throws TestException 
     */
    public void checkPanelForBasicElements() throws TestException {
        System.out.print("Validating basic elements present on preview panel... ");
        if (!driver.findElement(By.xpath(XPath.preImage)).isDisplayed()) {
            throw new TestException("Image not found on preview panel.");
        }
        if (!driver.findElement(By.xpath(XPath.preTitle)).isDisplayed()) {
            throw new TestException("Title not found on preview panel.");
        }
        if (!driver.findElement(By.xpath(XPath.preDescription)).isDisplayed()) {
            throw new TestException("Description not found on preview panel.");
        }
        System.out.println("Done.");
    }
    
    /**
     * Gathers data from all required preview panel fields, conducts an API call
     * to the Catalog for the asset with the same external id, and compares the
     * two data values. Cleanup is applied to data only where necessary to produce 
     * similar format to that which Media Finder actually displays.
     * @throws TestException 
     */
    public void checkMetadata() throws TestException {
        System.out.print("Checking preview panel metadata against Catalog XML... ");
        //Retrieve and clean up the title
        String title = driver.findElement(By.xpath(XPath.preTitle)).getText();
        if (title.contains("...")) {
            title = title.replace("...", "").trim();
        } else if (title.startsWith("Name")) {
            title = title.replaceFirst("Name", "").trim();
        }
        //Retrieve and clean up the description
        String description = driver.findElement(By.xpath(XPath.preDescription)).getText().trim();
        if (description.contains("...")) {
            description = description.replace("...", "").trim();
        } else if (description.startsWith("Description")) {
            description = description.replaceFirst("Description", "").trim();
        }
        //Retrieve and parse asset from Catalog
        SearchEntry asset;
        try {
            asset = new SearchEntry(selectedExternalId);
        } catch(Throwable e) {
            throw new TestException("Error parsing catalog asset: " + e.getMessage());
        }
        //Check values
        if (!asset.getTitle().contains(title)) {
            throw new TestException("Preview title: " + title + "\n" + "Catalog asset title: " + asset.getTitle());
        }
        if (!asset.getDescription().contains(description)) {
            throw new TestException("Preview description: " + description + "\n" + "Catalog asset description: " + asset.getDescription());
        }
        System.out.println("Done.");
    }
    
    /**
     * Expands the preview panel to the details view by clicking the preview
     * image.
     * @throws TestException 
     */
    public void expandToDetailsView() throws TestException {
        System.out.print("Expanding preview to details view... ");
        //Click preview image and wait for details view
        clickElement(XPath.preImage);
        waitForElementToBeVisible(XPath.detDetailsPanel);
        if (driver.findElement(By.xpath(XPath.detDetailsPanel)).isDisplayed()) {
            System.out.println("Done.");
        } else {
            throw new TestException("Details panel never displayed.");
        }
    }
}
