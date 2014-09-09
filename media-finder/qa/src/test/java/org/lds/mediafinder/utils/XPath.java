package org.lds.mediafinder.utils;

/**
 * Stores XPath strings for locating elements.
 * @author Allen Sudweeks
 */
public class XPath {
    
    //SSO page elements
    public static String ssoTxtUserName = "//input[@id='username']";
    public static String ssoTxtPassword = "//input[@id='password']";
    public static String ssoBtnSubmit = "//input[@id='submit']";
    
    //Landing page elements
    public static String landBackgroundImage = "//img[@class='background']";
    
    //Canary page elements
    public static String canLaneName = "//table/tbody/tr[1]/td[2]";
    public static String canRevision = "//table/tbody/tr[3]/td[2]";
    public static String canCatalogURL = "//table/tbody/tr[5]/td[2]";
    
    //Page identifier elements
    public static String pageLandingPageIdentifier = "//body[@class='modeA route-is-landing']";
    public static String pageSearchPageIdentifier = "//body[@class='modeA route-is-search']";
    public static String pageBuildArgs = "//body";
    
    //Ribbon elements
    public static String ribImgChurchLogo = "//svg[@class='logo']";
    public static String ribMediaFinderLogo = "//a[@href='#landing']/p/span";
    public static String ribEverything = "//div[@data-name='all']";
    public static String ribImages = "//div[@data-name='image']";
    public static String ribVideo = "//div[@data-name='video']";
    public static String ribAudio = "//div[@data-name='audio']";
    public static String ribText = "//div[@data-name='document']";
    public static String ribLogout = "//a[@class='logout']";
    public static String ribHelp = "//div[@id='help']/div[@class='action']";
    
    //Search elements
    public static String srchSearchHeader = "//h3[@class='searchFor']";
    public static String srchSearchField = "//input[@id='searchTerm']";
    public static String srchSearchButton = "//button[@id='startSearch']";
    public static String srchAdvancedSearch = "//a[text()='Advanced Search']";
    public static String srchResultsCount = "//div[@class='nResults']";
    public static String srchResultsTerm = srchResultsCount + "/span[@class='term']";
    public static String srchLoading = "//div[@class='loading']";
    public static String srchLoadMore = "//button[@id='loadMore']";
    
    //Sort elements
    public static String sortDropDown = "//div[@id='sorting']/div/span[@class='option']";
    public static String sortDropDownClosedIdentifier = "//div[@class='options' and @style='display: none;']";
    public static String sortOptionNewest = "//div[text()='Newest']";
    public static String sortOptionOldest = "//div[text()='Oldest']";
    public static String sortOptionRelevance = "//div[text()='Relevance']";
    public static String sortOptionNewestSelected = sortDropDown + "/div[text()='Newest']";
    public static String sortOptionOldestSelected = sortDropDown + "/div[text()='Newest']";
    public static String sortOptionRelevanceSelected = sortDropDown + "/div[text()='Newest']";
    
    //Facet elements
    public static String facIPCodeGroupsHeader = "//div[@id='ipGroups']/h3";
    public static String facIPCodeGroupInfoIcon = "//div[@id='ipGroups']/div[@class='info']";
    public static String facConditionsOfUseTooltipPanel = "//div[@id='ipInfo']";
    public static String facConditionsOfUseTooltipClose = facConditionsOfUseTooltipPanel + "/div[@class='header']/div[@class='close']";
    public static String facOpenUseCategory = "//input[@data-groupname='open']";
    public static String facRequiresApprovalCategory = "//input[@data-groupname='approval']";
    public static String facRestrictedCategory = "//input[@data-groupname='restricted']";
    public static String facKeywordsHeader = "//div[@id='facetOptions']/h3";
    public static String facKeywordsCarat = facKeywordsHeader + "/div[@class='arrow']";
    public static String facImageTypeHeader = "//div[@id='imageType']/h3";
    public static String facImageTypeCarat = facImageTypeHeader + "/div[@class='arrow']";
    public static String facGenericImageType = "//div[@id='imageType']/div/div/input";
    public static String facSpecificImageType(String name) {return "//input[@name='" + name + "']";}
    public static String facImageOrientationHeader = "//div[@id='imageOrientation']/h3";
    public static String facImageOrientationCarat = facImageOrientationHeader + "/div[@class='arrow']";
    public static String facGenericImageOrientation = "//div[@class='orientation']/input";
    public static String facImageOrientationLandscape = "//div[@class='orientation']/input[@id='imageOrientation-landscape']";
    public static String facImageOrientationPortrait = "//div[@class='orientation']/input[@id='imageOrientation-portrait']";
    public static String facImageOrientationSquare = "//div[@class='orientation']/input[@id='imageOrientation-square']";
    public static String facImageOrientationUnspecified = "//div[@class='orientation']/input[@id='imageOrientation-unspecified']";
    public static String facImageFacetDataCount(String name) {return "//span[@data-count='" + name + "']";}
    public static String facGenericKeywordFacet = "//div[@class='keyword']";
    public static String facSpecificFacet(String name) {return "//div[@class='keywordsList']/div[@title='" + name + "']";}
    public static String facSpecificFacetCount(String name) {return "//div[@class='keywordsList']/div[@title='" + name + "']/span[@class='count']";}
    public static String facGenericAppliedFacet = "//span[@class='keyword']";
    public static String facSpecificAppliedFacet(String name) {return "//span[@data-name='" + name + "']";}
    public static String facFilter = "//input[@class='filterInput']";

    //Results elements
    public static String resResultsGrid = "//div[@id='results']/div[@id='resultsGrid']";
    public static String resGenericResult = resResultsGrid + "/div[@class='result jstree-draggable']";
    private static String resImage = "/div[@class='imageBox']/img[@class='resultImage']";
    private static String resTitle = "/p";
    private static String resIPIcon = "/div/div[@class='ip']";
    private static String resDownloadButton = "/div/div[@class='download']";
    private static String resCollectionButton = "/div/div[@class='collect']";
    private static String resDeleteButton = "/div/div[@class='delete']";
    public static String resGenericResultImage = resGenericResult + resImage;
    public static String resGenericResultTitle = resGenericResult + resTitle;
    public static String resGenericResultIPIcon = resGenericResult + resIPIcon;
    public static String resGenericResultIPTooltip = "//div[@id='ipInfo']";
    public static String resGenericResultIPTooltipClose = resGenericResultIPTooltip + "/div[@class='header']/div[@class='close']";
    public static String resGenericResultDownloadButton = resGenericResult + resDownloadButton;
    public static String resSpecificResult(String externalId) {return resResultsGrid + "/div[@class='result' and @data-qa-externalid='" + externalId + "']";}
    public static String resSpecificResultImage(String externalId) {return resSpecificResult(externalId) + resImage;}
    public static String resSpecificResultTitle(String externalId) {return resSpecificResult(externalId) + resTitle;}
    public static String resSpecificResultIPIcon(String externalId) {return resSpecificResult(externalId) + resIPIcon;}
    public static String resSpecificResultDownloadButton(String externalId) {return resSpecificResult(externalId) + resDownloadButton;}    
    public static String resGenericResultCollectionButton = resGenericResult + resCollectionButton;
    public static String resSpecificResultCollectionButton(String externalId) {return resSpecificResult(externalId) + resCollectionButton;}
    public static String resGenericResultDeleteButton = resGenericResult + resDeleteButton;
    public static String resSpecificResultDeleteButton(String externalId) {return resSpecificResult(externalId) + resDeleteButton;}
    
    //Chrome Frame elements
    public static String chrInstallButton = "//button[@class='install']";
    public static String chrActivateButton = "//a[@name='submitbutton']";
    public static String chrAcceptButton = "//input[@id='submit1']";
    
    //Preview elements
    public static String prePreviewPanel = "//div[@id='preview']";
    public static String preTitle = "//p[@data-qa-field='name']";
    public static String preDescription = "//p[@data-qa-field='description']";
    public static String preImage = prePreviewPanel + "/img[@class='previewImage']";
    
    //Details View elements
    public static String detDetailsPanel = "//div[@id='details']";
    public static String detImage = detDetailsPanel + "/div/img[@class='previewImage']";
    public static String detCloseButton = "//div[@class='close']";
    public static String detDownloadButton = detDetailsPanel + "/div[@class='actions']/div[@class='download action']";
    public static String detIPCode = "//tr[@data-qa-field='ipcode']/td[@class='value']";
    public static String detFullTitle = "//tr[@data-qa-field='fulltitle']/td[@class='value']";
    public static String detFullDescription = "//tr[@data-qa-field='fulldescription']/td[@class='value']";
    public static String detLanguage = "//tr[@data-qa-field='language']/td[@class='value']";
    public static String detExternalId = "//tr[@data-qa-field='externalId']/td[@class='value']";
    public static String detExternalIdLabel = "//tr[@data-qa-field='externalId']/td[@class='label capitalize']";
    public static String detAssetType = "//tr[@data-qa-field='assetType']/td[@class='value']";
    public static String detRepositoryName = "//tr[@data-qa-field='repositoryName']/td[@class='value']";
    public static String detContractId = "//tr[@data-qa-field='contractId']/td[@class='value']";
    public static String detTextLimitations = "//tr[@data-qa-field='textLimitations']/td[@class='value']";
    public static String detLocked = "//tr[@data-qa-field='locked']/td[@class='value']";
    public static String detGenericKeyword = "//span[@class='keyword value']";
    public static String detSpecificKeyword(String name) {return "//span[@class='keyword value']/p[@title='" + name + "']";}
    public static String detDeepLink = "//tr[@data-qa-field='deepLink']/td[@class='value']";
    public static String detHistoryOfUse = "//div[@data-qa-field='historyOfUse section']/div[@class='fields']";
    public static String detFilename = "//tr[@data-qa-field='filename']/td[@class='value']";
    public static String detSEOFilename = "//tr[@data-qa-field='seoName']/td[@class='value']";
    public static String detAdditionalInfo = "//tr[@data-qa-field='addInfo']/td[@class='value']";
    public static String detCallNumber = "//tr[@data-qa-field='callNum']/td[@class='value']";
    public static String detCreditLine = "//tr[@data-qa-field='creditLine']/td[@class='value']";
    public static String detCreditLineRequired = "//tr[@data-qa-field='creditLineRequired']/td[@class='value']";
    public static String detSummarySection = "//div[@data-qa-field='summary section']";
    public static String detKeywordsSection = "//div[@data-qa-field='keywords section']";
    public static String detRightsManagementSection = "//div[@data-qa-field='rights management section']";
    public static String detHistoryOfUseSection = "//div[@data-qa-field='historyOfUse section']";
    public static String detSummarySectionHeader = detSummarySection + "/h3";
    public static String detKeywordsSectionHeader = detKeywordsSection + "/h3";
    public static String detRightsManagementSectionHeader = detRightsManagementSection + "/h3";
    public static String detHistoryOfUseSectionHeader = detHistoryOfUseSection + "/h3";
    public static String detGenericKeywordSearchIcon = detGenericKeyword + "/div[@class='icon']";
    public static String detSpecificKeywordSearchIcon(String name) {return detSpecificKeyword(name) + "/div[@class='icon']";}
        
    //Catalog image asset elements
    private static String base(int index) {return "/results/entries/searchEntry[" + Integer.toString(index) + "]";}
    public static String catTitle(int index) {return base(index) + "/metadata/name";}
    public static String catDescription(int index) {return base(index) + "/metadata/description";}
    public static String catIPCode(int index) {return base(index) + "/rightsManagement/ipCode";}
    public static String catLanguageCode(int index) {return base(index) + "/metadata/language/code";}
    public static String catLanguageText(int index) {return base(index) + "/metadata/language/text";}
    public static String catExternalId(int index) {return base(index) + "/metadata/externalId";}
    public static String catAssetType(int index) {return base(index) + "/metadata/assetType";}
    public static String catRepositoryName(int index) {return base(index) + "/metadata/repositoryName";}
    public static String catContractId(int index) {return base(index) + "/rightsManagement/rmId";}
    public static String catTextLimitations(int index) {return base(index) + "/rightsManagement/textLimitations";}
    public static String catLocked(int index) {return base(index) + "/rightsManagement/locked";}
    public static String catHistoryOfUse(int index) {return base(index) + "/metadata/keyValues/historyOfUse";}
    public static String catImageOrientation(int index) {return base(index) + "/metadata/keyValues/imageOrientation";}
    public static String catSEOFilename(int index) {return base(index) + "/metadata/keyValues/seoName";}
    public static String catTelescopeAssetType(int index) {return base(index) + "/metadata/keyValues/telescopeAssetType";}
    public static String catCreditLine(int index) {return base(index) + "/metadata/keyValues/creditLine";}
    public static String catCreditLineRequired(int index) {return base(index) + "/metadata/keyValues/creditLineRequired";}
    public static String catKeyword(int index) {return base(index) + "/keywords/keyword";}
    public static String catKeywordsSecondary(int index) {return base(index) + "/metadata/keyValues/keywordsSecondary";}
    public static String catRendition(int index) {return base(index) + "/assets/asset";}
    public static String catAdditionalInfo(int index) {return base(index) + "/metadata/keyValues/addInfo";}
    public static String catCallNumber(int index) {return base(index) + "/metadata/keyValues/callNum";}
    public static String catRenditionFileName(int entryIndex, int assetIndex) {return base(entryIndex) + "/assets/asset[" + Integer.toString(assetIndex) + "]/filename";}
    public static String catRenditionDimensions(int entryIndex, int assetIndex) {return base(entryIndex) + "/assets/asset[" + Integer.toString(assetIndex) + "]/resolution";}
    public static String catRenditionFileSize(int entryIndex, int assetIndex) {return base(entryIndex) + "/assets/asset[" + Integer.toString(assetIndex) + "]/fileSize";}
    public static String catRenditionFileType(int entryIndex, int assetIndex) {return base(entryIndex) + "/assets/asset[" + Integer.toString(assetIndex) + "]/format/extension";}
    
    //Download elements
    public static String dowRenditionsPanel = "//div[@id='download']";
    public static String dowGenericRendition = "//div[@class='downloadOptions']/table/tbody/tr[@class='downloadRow']";
    public static String dowGenericRenditionLink = "//div[@class='downloadOptions']/table/tbody/tr/td/div[@class='download']";
    public static String dowSpecificRenditionLink(int index) {return "//div[@class='downloadOptions']/table/tbody/tr[@class='downloadRow'][" + Integer.toString(index) + "]/td/div[@class='download']";}
    public static String dowSpecificRenditionDimensions(int index) {return "//div[@class='downloadOptions']/table/tbody/tr[@class='downloadRow'][" + Integer.toString(index) + "]/td[2]";}
    public static String dowSpecificRenditionSize(int index) {return "//div[@class='downloadOptions']/table/tbody/tr[@class='downloadRow'][" + Integer.toString(index) + "]/td[3]";}
    public static String dowSpecificRenditionType(int index) {return "//div[@class='downloadOptions']/table/tbody/tr[@class='downloadRow'][" + Integer.toString(index) + "]/td[4]";}
    public static String dowAlert = "//div[@class='downloadOptions']/p[@class='alert']";

    //Misc elements
    public static String misLandingFeedbackLink = "//a[@class='feedback']";
    public static String misSearchFeedbackLink = "//div[@id='help']/div/span[2]";
    public static String misPoweredByLogo = "//img[@id='poweredBy']";
    
    //Collections elements
    public static String colCollectionTab = "//div[@id='tabs']/div/span[text()='Collections']";
    public static String colFiltersTab = "//div[@id='tabs']/div/span[text()='Filters']";
    public static String colNewButton = "//span[@data-action='new']";
    public static String colRenameButton = "//span[@data-action='rename']";
    public static String colDeleteButton = "//span[@data-action='delete']";
    public static String colShareButton = "//span[@data-action='share']";
    public static String colMyCollections = "//a[text()='My Collections']";
    public static String colSharedCollections = "//a[text()='Shared Collections']";
    public static String colCollection(String name) {return "//a[text()='" + name + "']";}
    public static String colCollectionTray = "//div[@id='collectionSummary']";
    public static String colGenericCollectionData = "//ul/li/ul/li";
    public static String colSpecificCollectionData(String name) {return colGenericCollectionData + "[@data-name='" + name + "']";}
    
    //Collection results elements
    public static String colResultsGrid = "//div[@id='collectionResults']/div[@id='resultsGrid']";
    public static String colGenericResult = colResultsGrid + "/div[@class='result jstree-draggable']";
    public static String colGenericResultImage = colGenericResult + resImage;
    public static String colGenericResultTitle = colGenericResult + resTitle;
    public static String colGenericResultIPIcon = colGenericResult + resIPIcon;
    public static String colGenericResultDownloadButton = colGenericResult + resDownloadButton;
    public static String colSpecificResult(String externalId) {return colResultsGrid + "/div[@class='result jstree-draggable' and @data-qa-externalid='" + externalId + "']";}
    public static String colSpecificResultImage(String externalId) {return colSpecificResult(externalId) + resImage;}
    public static String colSpecificResultTitle(String externalId) {return colSpecificResult(externalId) + resTitle;}
    public static String colSpecificResultIPIcon(String externalId) {return colSpecificResult(externalId) + resIPIcon;}
    public static String colSpecificResultDownloadButton(String externalId) {return colSpecificResult(externalId) + resDownloadButton;}    
    public static String colGenericResultCollectionButton = colGenericResult + resCollectionButton;
    public static String colSpecificResultCollectionButton(String externalId) {return colSpecificResult(externalId) + resCollectionButton;}
    public static String colGenericResultDeleteButton = colGenericResult + resDeleteButton;
    public static String colSpecificResultDeleteButton(String externalId) {return colSpecificResult(externalId) + resDeleteButton;}
    
    //Delete collection dialog elements
    public static String colDeleteDialog = "//div[@id='DeletePopup']";
    public static String colDeleteDialogCloseButton = colDeleteDialog + "/div/div[@class='close']";
    public static String colDeleteDialogYesButton = colDeleteDialog + "/div/div/button[@class='btn yes']";
    public static String colDeleteDialogCancelButton = colDeleteDialog + "/div/div/a[@class='no']";
    
    //Share collection panel elements
    public static String colGenericSharedUser = "//ul[@class='sharedWith']/li";
    public static String colSharedUserDeleteButton = "/span[@class='action delete']";
    public static String colGenericSharedUserDeleteButton = colGenericSharedUser + colSharedUserDeleteButton;
    public static String colSpecificSharedUser(String name) {return "//ul[@class='sharedWith']/li[@data-username='" + name + "']";}
    public static String colSpecificSharedUserDeleteButton(String name) {return colSpecificSharedUser(name) + colSharedUserDeleteButton;}
    public static String colShareUserSearch = "//input[@class='shareWith']";
    public static String colShareBulkAddLink = "//div[@class='options']/span[text()='Add by LDS Account']";
    public static String colShareBulkUserSearch = "//textarea";
    public static String colShareBulkAddButton = "//div[@class='addBulk']";
    public static String colShareBulkCancelButton = "//span[text()='Cancel']";
    public static String colShareCloseButton = "//div[@id='shareCollections']/h2/span[@class='close']";
    public static String colGenericUserSearchResult = "//ul[@class='searchResults']/li";
    public static String colUserAddButton = "/span[@class='action share']";
    public static String colGenericUserSearchResultAddButton = colGenericUserSearchResult + colUserAddButton;
    public static String colSpecificUserSearchResult(String username) {return colGenericUserSearchResult + "[@data-username='" + username + "']";}
    public static String colSpecificUserSearchResultAddButton(String username) {return colSpecificUserSearchResult(username) + colUserAddButton;}
    
    //Add to collection dialog elements
    public static String colAddDialog = "//div[@id='collectionsPopup']";
    public static String colAddDialogAddButton = colAddDialog + "/div[@class='content']/div/span[@data-action='add']";
    public static String colAddDialogAddButtonDeep = colAddDialog + "/div/div[@class='allInfo']/span[@data-action='add']";
    public static String colAddDialogChangeCollectionButton = colAddDialog + "/div/div/span[@data-action='change']";
    public static String colAddDialogCancelButton = colAddDialog + "/div/div/span[@data-action='cancel']";
    public static String colAddDialogCollection(String name) {return colAddDialog + "/div/div/div/div/ul/li/ul/li[@data-name='" + name + "']/a";}
    public static String colAddDialogNewCollectionButton = colAddDialog + "/div/div/span[@data-action='new']";

}
