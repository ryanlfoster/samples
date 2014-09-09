package org.lds.mediafinder.testcase;

/**
 * Stores test case data.
 * @author Allen Sudweeks
 */
public class TestCase {

    protected String key;
    protected String name;
    protected String priority;
    protected String component;
    protected String browserName;
    protected String browserVersion;
    protected String platformName;
    protected String screenshotUrl;
    protected String result;
    
    public TestCase() {
        result = "Pass";
        screenshotUrl = "None";
    }
    
    public TestCase(TestData data, String browserName, String browserVersion, String platformName) {
        this(browserName, browserVersion, platformName);
        this.key = data.getKey();
        this.name = data.getName();
    }

    public TestCase(String browserName, String browserVersion, String platformName) {
        this();
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.platformName = platformName;
    }
    
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }
    
}
