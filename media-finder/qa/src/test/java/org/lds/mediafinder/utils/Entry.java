package org.lds.mediafinder.utils;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Entry {

    private String id;
    private String name;
    private String assetType;
    private List<String> parentCollections;
    private List<String> childCollections;
    private String versionId;
    
    public Entry(String xml) throws Exception {
        Document document = XMLUtil.buildDocument(xml);
        Node root;
        javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
        //Validate document
        if (document == null) {
            throw new TestException("Cannot parse a null Document");
        } else {
            root = document.getDocumentElement();
        }
        //Parse simple attributes
        id = xpath.evaluate("/id", root);
        name = xpath.evaluate("/metadata/name", root);
        assetType = xpath.evaluate("/metadata/assetType", root);
        versionId = xpath.evaluate("/versionId", root);
        //Parse parent collection ids
        parentCollections = new ArrayList<String>();
        NodeList nodes = (NodeList) xpath.evaluate("/parentCollection/item", root, XPathConstants.NODESET);
        for (int i = 1; i <= nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            parentCollections.add(node.getAttribute("id"));
        }
        //Parse child collection ids
        childCollections = new ArrayList<String>();
        nodes = (NodeList) xpath.evaluate("/childCollection/item", root, XPathConstants.NODESET);
        for (int i = 1; i <= nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            childCollections.add(node.getAttribute("id"));
        }
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public List<String> getChildCollections() {
        return childCollections;
    }

    public void setChildCollections(List<String> childCollections) {
        this.childCollections = childCollections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParentCollections() {
        return parentCollections;
    }

    public void setParentCollections(List<String> parentCollections) {
        this.parentCollections = parentCollections;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}
