package org.lds.mediafinder.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lds.mediafinder.utils.SearchEntry;
import org.lds.mediafinder.utils.Rendition;
import org.lds.mediafinder.utils.TestException;
import org.lds.mediafinder.utils.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Houses all download methods.
 * @author Allen Sudweeks
 */
public class Module_Download extends ModuleMaster {

    private File downloadDirectory;
    private ArrayList<File> existingFiles;
    
    public Module_Download(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        downloadDirectory = new File("C:\\Users\\therrin150\\Downloads");
        existingFiles = new ArrayList<File>();
    }
    
    /**
     * Opens the renditions panel for the first available search result.
     * @throws TestException 
     */
    public void openFirstAvailableSearchResultRenditionPanel() throws TestException {
        System.out.print("Opening the rendition panel for the first available search result... ");
        //Record external ID
        selectedExternalId = driver.findElement(By.xpath(XPath.resGenericResult)).getAttribute("data-qa-externalid");
        clickElement(XPath.resGenericResultDownloadButton);
        waitForElementToBeVisible(XPath.dowRenditionsPanel);
        System.out.println("Done.");
    }
    
    /**
     * Opens the renditions panel for the specified search result.
     * @param externalId
     * @throws TestException 
     */
    public void openSpecificSearchResultRenditionPanel(String externalId) throws TestException {
        System.out.print("Opening the rendition panel for asset with external id '" + externalId + "'... ");
        selectedExternalId = externalId;
        clickElement(XPath.resSpecificResultDownloadButton(externalId));
        waitForElementToBeVisible(XPath.dowRenditionsPanel);
        System.out.println("Done.");
    }

    /**
     * Opens the renditions panel for the open details view.
     * @throws TestException 
     */
    public void openDetailsViewRenditionPanel() throws TestException {
        System.out.print("Opening the rendition panel for the open details view... ");
        clickElement(XPath.detDownloadButton);
        waitForElementToBeVisible(XPath.dowRenditionsPanel);
        System.out.println("Done.");
    }
    
    /**
     * Downloads the first rendition listed on the currently open renditions panel.
     * @throws TestException 
     */
    public void downloadFirstAvailableRendition() throws TestException {
        System.out.print("Downloading the first available rendition... ");
        //Take inventory of existing files
        existingFiles = new ArrayList<File>();
        existingFiles.addAll(Arrays.asList(downloadDirectory.listFiles()));
        clickElement(XPath.dowGenericRenditionLink);
        System.out.println("Done.");
    }

    /**
     * Downloads the specified rendition listed on the currently open renditions 
     * panel.
     * @param index
     * @throws TestException 
     */
    public void downloadSpecificRendition(int index) throws TestException {
        System.out.print("Downloading '" + Integer.toString(index) + "' rendition... ");
        //Take inventory of existing files
        existingFiles = new ArrayList<File>();
        existingFiles.addAll(Arrays.asList(downloadDirectory.listFiles()));
        clickElement(XPath.dowSpecificRenditionLink(index));
        System.out.println("Done.");
    }
    
    /**
     * Deprecated.
     * @throws TestException 
     */
    public void checkForDownloadedFile() throws TestException {
        System.out.print("Checking download directory for downloaded file... ");
        //With 30 second timeout, check for downloaded file in download directory
        ArrayList<File> checkedFiles = new ArrayList<File>();
        int time = 0;
        File downloadedFile = null;
        while (time < 30000 && downloadedFile == null) {
            //Retrieve all files in directory
            checkedFiles.addAll(Arrays.asList(downloadDirectory.listFiles()));
            //Find one that was not found in existingFiles
            if (checkedFiles != null) {
                for (File file : checkedFiles) {
                    if (!existingFiles.contains(file)) {
                        downloadedFile = file;
                        break;
                    }
                }
            }            
            //Wait 500ms and try again
            try {
                Thread.sleep(500);
                time += 500;
            } catch(InterruptedException e) {
                throw new TestException("Error sleeping thread during check for downloaded file: " + e.getMessage());
            }
        }
        if (downloadedFile != null) {
            System.out.println("'" + downloadedFile.getName() +"' found.");
        } else {
            throw new TestException("File not downloaded within timeout period.");
        }
    }
    
    /**
     * Validates the renditions listed on the currently open renditions panel 
     * against the Catalog XML retrieved directly through the Catalog API.
     * @throws TestException 
     */
    public void checkRenditionsMetadata() throws TestException {
        System.out.print("Checking renditions against Catalog XML... ");
        ArrayList<Rendition> panelRenditions = new ArrayList<Rendition>();
        ArrayList<Rendition> catalogRenditions;
        //Get renditions panel data
        List<WebElement> renditions = driver.findElements(By.xpath(XPath.dowGenericRendition));
        for (int i = 0; i < renditions.size(); i++) {
            Rendition temp = new Rendition();
            temp.setDimensions(driver.findElement(By.xpath(XPath.dowSpecificRenditionDimensions(i + 1))).getText().trim());
            temp.setFileType(driver.findElement(By.xpath(XPath.dowSpecificRenditionType(i + 1))).getText().trim());
            panelRenditions.add(temp);
        }
        //Retrieve and parse asset from Catalog
        SearchEntry asset;
        try {
            asset = new SearchEntry(selectedExternalId);
        } catch (Throwable e) {
            throw new TestException("Error parsing Catalog asset: " + e.getMessage());
        }
        //Get catalog renditions data
        catalogRenditions = asset.getRenditions();
        //Check renditions count
        if (panelRenditions.size() != catalogRenditions.size()) {
            throw new TestException("Rendition counts do not match. Catalog count: " + Integer.toString(catalogRenditions.size()) + ", Panel count: " + Integer.toString(panelRenditions.size()));
        }
        //Check values
        for (int i = 0; i < panelRenditions.size(); i++) {
            //Check dimensions
            if (!catalogRenditions.get(i).getDimensions().contains(panelRenditions.get(i).getDimensions())) {
                throw new TestException("Catalog dimensions: " + catalogRenditions.get(i).getDimensions() + "\n" + "Panel dimensions: " + panelRenditions.get(i).getDimensions());
            }
            //Check file type
            if (!catalogRenditions.get(i).getFileType().toUpperCase().equals(panelRenditions.get(i).getFileType())) {
                throw new TestException("Catalog file type: " + catalogRenditions.get(i).getFileType() + "\n" + "Panel file type: " + panelRenditions.get(i).getFileType());
            }
        }
        System.out.println("Done.");
    }    
    
    /**
     * Temporary. Validates that the details view download button does not exist
     * in certain conditions.
     * @throws TestException 
     */
    public void checkDetailsViewDownloadButtonHidden() throws TestException {
        System.out.print("Checking that details view download button is properly hidden... ");
        WebElement element = driver.findElement(By.xpath(XPath.detDownloadButton));
        if (element.isDisplayed()) {
            throw new TestException("Download button found when not expected.");
        } else {
            System.out.println("Done.");
        }
        
    }
    
    /**
     * Temporary. Validates that the search result download button does not exist
     * in certain conditions.
     * @throws TestException 
     */
    public void checkSearchResultDownloadButtonHidden() throws TestException {
        System.out.print("Checking that search result download button is properly hidden... ");
        WebElement element = driver.findElement(By.xpath(XPath.resGenericResultDownloadButton));
        if (element.isDisplayed()) {
            throw new TestException("Download button found when not expected.");
        } else {
            System.out.println("Done.");
        }
    }
}
