package org.lds.mediafinder.constants;

import org.lds.mediafinder.utils.XPath;

/**
 * Enum for sharing tabs easily for certain functions in Tests and Modules.
 * @author Allen Sudweeks
 */
public enum Tab {

    EVERYTHING(XPath.ribEverything, "Everything"), 
    IMAGES(XPath.ribImages, "Images"), 
    VIDEO(XPath.ribVideo, "Video"), 
    AUDIO(XPath.ribAudio, "Audio"), 
    TEXT(XPath.ribText, "Text"), 
    LANDINGPAGE(XPath.ribMediaFinderLogo, "Landing Page");
    
    private String xPath;
    private String name;

    Tab(String xpath, String name) {
        this.xPath = xpath;
        this.name = name;
    }

    public String getXPath() {
        return xPath;
    }
    
    public String getName() {
        return name;
    }
}
