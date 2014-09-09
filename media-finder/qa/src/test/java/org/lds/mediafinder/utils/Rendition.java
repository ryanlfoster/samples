package org.lds.mediafinder.utils;

/**
 * Represents an asset's rendition.
 * @author Allen Sudweeks
 */
public class Rendition {
    
    private String dimensions;
    private String fileSize;
    private String fileType;
    private String fileName;
    
    public Rendition() {
        
    }
    
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
    
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileType() {
        return fileType;
    }
}
