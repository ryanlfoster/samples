package org.lds.mediafinder.modules;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Allen Sudweeks
 */
public class ModuleManager {
    
    public Module_Authentication authentication;
    public Module_Architecture architecture;
    public Module_Search search;
    public Module_Compatibility compatibility;
    public Module_Faceting_Keywords keywords;
    public Module_Faceting_IPCode ipCode;
    public Module_Faceting_ImageType imageType;
    public Module_Faceting_ImageOrientation imageOrientation;
    public Module_Preview preview;
    public Module_DetailsView detailsView;
    public Module_Download download;
    public Module_Collections collections;
    
    public ModuleManager(WebDriver driver, WebDriverWait wait) {
        authentication = new Module_Authentication(driver, wait);
        architecture = new Module_Architecture(driver, wait);
        search = new Module_Search(driver, wait);
        compatibility = new Module_Compatibility(driver, wait);
        keywords = new Module_Faceting_Keywords(driver, wait);
        ipCode = new Module_Faceting_IPCode(driver, wait);
        imageType = new Module_Faceting_ImageType(driver, wait);
        imageOrientation = new Module_Faceting_ImageOrientation(driver, wait);
        preview = new Module_Preview(driver, wait);
        detailsView = new Module_DetailsView(driver, wait);
        download = new Module_Download(driver, wait);
        collections = new Module_Collections(driver, wait);
    }
    
}
