package org.lds.mediafinder.constants;

import java.util.ArrayList;
import java.util.Arrays;
import org.lds.mediafinder.utils.XPath;

/**
 * Enum for sharing IP Code categories easily for certain functions in Tests and Modules.
 * @author Allen Sudweeks
 */
public enum Category {
    
    OPENUSE("Open Use", XPath.facOpenUseCategory, new String[] {"0", "4", "7", "9"}),
    REQUIRESAPPROVAL("Requires Approval", XPath.facRequiresApprovalCategory, new String[] {"0E", "0MR", "1", "1G", "1AV", "4MR", "4AV", "8", "10"}),
    RESTRICTED("Restricted", XPath.facRestrictedCategory, new String[] {"5"});
    
    private String name;
    private ArrayList<String> values;
    private String xpath;
    private String backgroundColor;
    private String borderBottomColor; //Only for Restricted
    
    Category(String name, String xpath, String[] values) {
        this.name = name;
        this.values = new ArrayList<String>();
        this.values.addAll(Arrays.asList(values));
        this.xpath = xpath;
        if (name.equals("Open Use")) {
            backgroundColor = "rgb(66, 144, 61)";
        } else if (name.equals("Requires Approval")) {
            backgroundColor = "#F1ED51";
        } else if (name.equals("Restricted")) {
            backgroundColor = "#F2F0F0";
            borderBottomColor = "#CC2927";
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getValues() {
        return values;
    }
    
    public String getXPath() {
        return xpath;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
    
    public String getBorderBottomColor() {
        return borderBottomColor;
    }
}
