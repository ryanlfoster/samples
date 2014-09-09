package org.lds.mediafinder.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.lds.mediafinder.constants.AssetType;
import org.lds.mediafinder.constants.ImageOrientation;
import org.lds.mediafinder.constants.RepositoryName;
import org.lds.stack.utils.StringUtils;

/**
 * Represents an asset from the Catalog.
 * @author Allen Sudweeks
 */
public class SearchEntry {
    
    private Document xmlDoc;
    private String title;
    private String description;
    private String ipCode;
    private String language;
    private String externalId;
    private AssetType assetType;
    private RepositoryName repositoryName;
    private String contractId;
    private String textLimitations;
    private String locked;
    private String callNumber;
    private String historyOfUse;
    private ImageOrientation imageOrientation;
    private String seoFilename;
    private String telescopeAssetType;
    private String creditLine;
    private String creditLineRequired;
    private String additionalInfo;
    private ArrayList<String> keywords;
    private ArrayList<String> secondaryKeywords;
    private ArrayList<Rendition> renditions;
    
    /**
     * Overloaded Constructor. Retrieves the XML from the Catalog in preparation for parse()
     * @param externalId 
     */
    public SearchEntry(String externalId) throws Exception {
        this.externalId = externalId;
        keywords = new ArrayList<String>(); 
        renditions = new ArrayList<Rendition>();
        secondaryKeywords = new ArrayList<String>();
        xmlDoc = XMLUtil.buildDocument(CatalogDao.search(externalId));
        SearchEntryParser parser = new SearchEntryParser(xmlDoc, externalId);
        //Get basic attributes
        title = parser.evaluate(XPath.catTitle(parser.getIndex()));
        description = parser.evaluate(XPath.catDescription(parser.getIndex())).replace("&apos;", "'").trim();
        ipCode = parser.evaluate(XPath.catIPCode(parser.getIndex()));
        language = (parser.evaluate(XPath.catLanguageText(parser.getIndex())) + " (" + parser.evaluate(XPath.catLanguageCode(parser.getIndex())) + ")");
        assetType = AssetType.valueOf(parser.evaluate(XPath.catAssetType(parser.getIndex())));
        repositoryName = RepositoryName.valueOf(parser.evaluate(XPath.catRepositoryName(parser.getIndex())));
        contractId = parser.evaluate(XPath.catContractId(parser.getIndex()));
        textLimitations = parser.evaluate(XPath.catTextLimitations(parser.getIndex()));
        locked = parser.evaluate(XPath.catLocked(parser.getIndex()));
        if (locked.equals("true")) {
            locked = "Yes";
        } else if (locked.equals("false")) {
            locked = "No";
        }
        callNumber = parser.evaluate(XPath.catCallNumber(parser.getIndex()));
        historyOfUse = parser.evaluate(XPath.catHistoryOfUse(parser.getIndex()));
        if (StringUtils.isNotBlank(historyOfUse)) {
            historyOfUse = historyOfUse.replace("^|^", "\n");
        }
        imageOrientation = ImageOrientation.valueOf(parser.evaluate(XPath.catImageOrientation(parser.getIndex())));
        seoFilename = parser.evaluate(XPath.catSEOFilename(parser.getIndex()));
        telescopeAssetType = parser.evaluate(XPath.catTelescopeAssetType(parser.getIndex()));
        creditLine = parser.evaluate(XPath.catCreditLine(parser.getIndex()));
        creditLineRequired = parser.evaluate(XPath.catCreditLineRequired(parser.getIndex()));
        additionalInfo = parser.evaluate(XPath.catAdditionalInfo(parser.getIndex()));
        //Get keywords (combine with crowd/secondary keywords, since all are displayed with no discrimination)
        keywords = parser.evaluateList(XPath.catKeyword(parser.getIndex()));
        //Get secondary keywords
        String secValue = parser.evaluate(XPath.catKeywordsSecondary(parser.getIndex()));
        String[] secValues = secValue.split("\\^\\|\\^");
        secondaryKeywords.addAll(Arrays.asList(secValues));
        keywords.addAll(secondaryKeywords);
        //Get renditions
        renditions = parser.evaluateRenditions();
    }
    
    public AssetType getAssetType() {
        return assetType;
    }

    public String getContractId() {
        return contractId;
    }

    public String getDescription() {
        return description;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getIpCode() {
        return ipCode;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocked() {
        return locked;
    }

    public RepositoryName getRepositoryName() {
        return repositoryName;
    }

    public String getTextLimitations() {
        return textLimitations;
    }

    public String getTitle() {
        return title;
    }   
    
    public ArrayList<Rendition> getRenditions() {
        return renditions;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getCreditLine() {
        return creditLine;
    }

    public String getCreditLineRequired() {
        return creditLineRequired;
    }

    public String getHistoryOfUse() {
        return historyOfUse;
    }

    public ImageOrientation getImageOrientation() {
        return imageOrientation;
    }

    public ArrayList<String> getSecondaryKeywords() {
        return secondaryKeywords;
    }

    public String getSeoFilename() {
        return seoFilename;
    }

    public String getTelescopeAssetType() {
        return telescopeAssetType;
    }

    public String getCallNumber() {
        return callNumber;
    }
}
