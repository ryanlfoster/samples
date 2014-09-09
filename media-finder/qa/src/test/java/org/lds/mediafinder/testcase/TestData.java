package org.lds.mediafinder.testcase;

/**
 *
 * @author Allen Sudweeks
 */
public class TestData {

    private String key;
    private String name;
    private String component;
    private String priority;
    
    public TestData() {}
    
    public TestData(String key, String name) {
        this.key = key;
        this.name = name;
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
    
}
