package org.lds.mediafinder.testcase;

import org.lds.mediafinder.utils.jira.JiraTestCaseServiceApi;
import org.lds.stack.utils.StringUtils;

/**
 *
 * @author Allen Sudweeks
 */
public class JiraTestCase extends TestCase {

    private String issue;

    public JiraTestCase(String key, String browserName, String browserVersion, String platformName) {
        super(browserName, browserVersion, platformName);
        this.key = key;
        issue = new JiraTestCaseServiceApi().getIssue(key);
        if (StringUtils.isNotBlank(issue)) {
            getJiraTestCaseName();
            getJiraTestCasePriority();
            getJiraTestCaseComponent();
        }
    }
    
    /**
     * Returns the JIRA test case name, as parsed out of getJIRAIssue().
     * @param issueKey
     * @return Test case name
     */
    private String getJiraTestCaseName() {
        String summary = issue.substring(issue.indexOf("summary"), issue.indexOf("timetracking"));
        name = summary.substring(summary.indexOf("TC"));
        name = name.substring(0, name.indexOf("\","));
        return name;
    }
    
    /**
     * Returns the JIRA test case priority, as parsed out of getJIRAIssue().
     * @param issueKey
     * @return Test case priority
     */
    private String getJiraTestCasePriority() {
        if (issue.contains("P1- High")) {
            priority = "P1";
        } else if (issue.contains("P2- Medium")) {
            priority = "P2";
        } else if (issue.contains("P3- Low")) {
            priority = "P3";
        }
        return priority;
    }
    
    /**
     * Returns the JIRA test case component, as parsed out of getJIRAIssue().
     * @return Test case component
     */
    private String getJiraTestCaseComponent() {
        component = name.replace("TC - ", "");
        component = component.substring(0, component.indexOf(":"));
        return component;
    }

}
