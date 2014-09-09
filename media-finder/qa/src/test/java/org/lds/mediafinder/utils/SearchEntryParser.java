package org.lds.mediafinder.utils;

import java.util.ArrayList;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.lds.stack.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Takes in the search results' XML Document object, locates the correct search entry to parse, and enables parsing
 * @author Allen Sudweeks
 */
public class SearchEntryParser {

    private javax.xml.xpath.XPath xpath;
    private Node root;
    private int index;
    
    public SearchEntryParser(Document document, String externalId) throws Exception {
        xpath = XPathFactory.newInstance().newXPath();
        //Validate document and get root node
        if (document == null) {
            throw new TestException("Cannot parse a null Document");
        } else {
            root = document.getDocumentElement();
        }
        //Extract result entries
        NodeList entries = (NodeList) xpath.evaluate("/results/entries/searchEntry", root, XPathConstants.NODESET);
        //Validate entries
        if (entries.getLength() == 0) {
            throw new TestException("No entry found in Catalog XML.");
        }
        //Find correct entry
        index = 0;
        for (int i = 1; i <= entries.getLength(); i++) {
            String entryId = xpath.evaluate(XPath.catExternalId(i), root);
            if (entryId.equals(externalId)) {
                index = i;
                break;
            }
        }
        //Validate correct entry found
        if (index == 0) {
            throw new TestException("No entry with specified externalId found in Catalog XML");
        }
    }
    
    //Returns the index (within the search results found in the Document) of the search entry to be parsed
    public int getIndex() {
        return index;
    }
    
    /**
     * Evaluates the given XPath expression
     * @param xpath
     * @return
     * @throws Exception 
     */
    public String evaluate(String xpath) throws Exception {
        String result = this.xpath.evaluate(xpath, root);
        if (StringUtils.isNotBlank(result)) {
            result = result.trim();
        }
        return result;
    }
    
    /**
     * Evaluates the given XPath expression, returning a list
     * @param xpath
     * @return
     * @throws Exception 
     */
    public ArrayList<String> evaluateList(String xpath) throws Exception {
        ArrayList<String> results = new ArrayList<String>();
        NodeList nodes = (NodeList) this.xpath.evaluate(xpath, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            String result = nodes.item(i).getTextContent();
            if (StringUtils.isNotBlank(result)) {
                results.add(result.trim());
            }            
        }
        return results;
    }
    
    /**
     * Returns the search entry's asset renditions
     * @return
     * @throws Exception 
     */
    public ArrayList<Rendition> evaluateRenditions() throws Exception {
        ArrayList<Rendition> renditions = new ArrayList<Rendition>();
        NodeList renditionNodes = (NodeList) xpath.evaluate(XPath.catRendition(index), root, XPathConstants.NODESET);
        for (int i = 1; i <= renditionNodes.getLength(); i++) {
            Rendition temp = new Rendition();
            //Rendition only exists if it has a file name
            String renditionFileName = xpath.evaluate(XPath.catRenditionFileName(index, i), root);
            if (StringUtils.isNotBlank(renditionFileName)) {
                temp.setFileName(renditionFileName.trim());
                String dimensions = xpath.evaluate(XPath.catRenditionDimensions(index, i), root);
                if (StringUtils.isNotBlank(dimensions)) {
                    temp.setDimensions(dimensions.trim());
                }
                String fileSize = xpath.evaluate(XPath.catRenditionFileSize(index, i), root);
                if (StringUtils.isNotBlank(fileSize)) {
                    temp.setFileSize(fileSize.trim());
                }
                String fileType = xpath.evaluate(XPath.catRenditionFileType(index, i), root);
                if (StringUtils.isNotBlank(fileType)) {
                    temp.setFileType(fileType.trim());
                }                    
                renditions.add(temp);
            }                   
        }
        return renditions;
    }
}
