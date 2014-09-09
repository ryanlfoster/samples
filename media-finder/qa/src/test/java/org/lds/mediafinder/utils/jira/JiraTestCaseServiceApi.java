package org.lds.mediafinder.utils.jira;

import java.io.File;
import java.util.ArrayList;

/**
 * API Client Service using a JiraProjectDao object to pass in TestNG automated tests back to the equivalent test case
 * in JIRA
 *
 * <p> Depends on: <ol> <li>The JIRA project using the Work Flow Scheme "Scrum Workflow Scheme 2.1 "</li> <li>The LDS
 * Service Account "aqat" to have been added as a QA member of the JIRA project</li> </ol>
 *
 * @author Matt Galloway <mgalloway@ldschurch.org>
 *
 */
public class JiraTestCaseServiceApi {

    private JiraProjectDao jiraService = new JiraProjectDao();

    public String getIssue(String issueKey) {

        return jiraService.getIssueByKey(issueKey);

    }

    /**
     * Takes a JIRA action ID and sets the workflow to this action based on the current status of the JIRA issue (test
     * case) Conditions for the following: <p> These two will hold off any JIRA updating as the test is not in a ready
     * to run state <ul> <li>Awaiting Review (approval)</li> <li>Unscheduled</li> </ul> If the JIRA issue (test case) is
     * in any of the following statuses the proper workflow will be updated to place it in the recent test execution
     * status (Passed or Failed) <ul> <li>Passed</li> <li>Failed</li> <li>Blocked</li> <li>In Progress</li> </ul>
     *
     * @param issueId
     * @param actionId
     * @return
     * @throws java.rmi.RemoteException
     * @throws Exception
     */
    public boolean setIssueWorkFlow(String issueKey, Integer actionId, String comment) throws Exception {

        String currentIssueStatus = jiraService.getCurrentStatus(issueKey);

        boolean newStatus = false;

        try {

            /**
             * Check to make sure the test is in a ready to run or already executed state before updating, if it is not
             * yet approved or scheduled do nothing
             */
            // Unscheduled Test = 10032 || Awaiting Review = 10037
            if (currentIssueStatus.equals("Unscheduled Test") || currentIssueStatus.equals("Awaiting Review")) {

                System.out.println("JIRA issue not available for update (Awaiting Approval or Unscheduled)");

            } else {

                // Passed = 10034
                if (currentIssueStatus.equals("Passed")) {

                    newStatus = transitionFromPassed(issueKey, actionId, comment);

                }
                // Failed = 10033
                if (currentIssueStatus.equals("Failed")) {

                    newStatus = transitionFromFailed(issueKey, actionId, comment);

                }
                // Blocked = 10036
                if (currentIssueStatus.equals("Blocked")) {

                    newStatus = transitionFromBlocked(issueKey, actionId, comment);

                }
                // Ready To Run = 10035
                if (currentIssueStatus.equals("Ready To Run")) {

                    newStatus = transitionFromReady(issueKey, actionId, comment);

                }
                // In Progress = 3
                if (currentIssueStatus.equals("In Progress")) {

                    newStatus = transitionFromInprogress(issueKey, actionId, comment);

                }
            }

        } catch (Exception e) {
            System.out.println("Issue status failed to update (" + issueKey + ")");
            e.printStackTrace();
        }

        return newStatus;

    }

    /**
     * @param issueKey
     * @param actionId
     * @param comment
     * @return
     * @throws Exception
     */
    private boolean transitionFromInprogress(String issueKey, Integer actionId,
            String comment) throws Exception {

        boolean newStatus;

        newStatus = jiraService.setTransition(issueKey, actionId, comment);
        System.out.println("Successfully updated issue status");

        return newStatus;

    }

    /**
     * @param issueKey
     * @param actionId
     * @param comment
     * @return
     * @throws Exception
     */
    private boolean transitionFromReady(String issueKey, Integer actionId,
            String comment) throws Exception {

        boolean newStatus;

        // Start test
        jiraService.setTransition(issueKey, 51, null);
        // Set test case new status
        newStatus = jiraService.setTransition(issueKey, actionId, comment);
        System.out.println("Successfully updated issue status");

        return newStatus;
    }

    /**
     * @param issueKey
     * @param actionId
     * @param comment
     * @return
     * @throws Exception
     */
    private boolean transitionFromBlocked(String issueKey, Integer actionId,
            String comment) throws Exception {

        boolean newStatus;

        // Rerun test			
        jiraService.setTransition(issueKey, 161, null);
        // Start test
        jiraService.setTransition(issueKey, 51, null);
        // Set test case new status
        newStatus = jiraService.setTransition(issueKey, actionId, comment);
        System.out.println("Successfully updated issue status");

        return newStatus;
    }

    /**
     * @param issueKey
     * @param actionId
     * @param comment
     * @return
     * @throws Exception
     */
    private boolean transitionFromFailed(String issueKey, Integer actionId,
            String comment) throws Exception {

        boolean newStatus;

        // Rerun test			
        jiraService.setTransition(issueKey, 131, null);
        // Start test
        jiraService.setTransition(issueKey, 51, null);
        // Set test case new status
        newStatus = jiraService.setTransition(issueKey, actionId, comment);
        System.out.println("Successfully updated issue status");

        return newStatus;

    }

    /**
     * @param issueKey
     * @param actionId
     * @param comment
     * @return
     * @throws Exception
     */
    private boolean transitionFromPassed(String issueKey, Integer actionId,
            String comment) throws Exception {

        boolean newStatus;

        // Rerun test				
        jiraService.setTransition(issueKey, 101, null);
        // Start test
        jiraService.setTransition(issueKey, 51, null);
        // Set test case new status
        newStatus = jiraService.setTransition(issueKey, actionId, comment);
        System.out.println("Successfully updated issue status");

        return newStatus;
    }

    /**
     * Provides an alternative method to update a JIRA test case than is given in updateJira() method. Uses the JIRA
     * test key (i.e. CCIF-1 or CCAT-1) instead of an automated method name to retrieve a specific test case, instead of
     * a collection of test cases that share the automated method name.
     *
     * @author Allen Sudweeks <asudweeks@ldschurch.org>
     * @param testKey
     * @param testResult
     * @throws java.rmi.RemoteException
     * @throws Exception
     */
    public void updateJira(ArrayList<String> keys, String testResult, File screenshot) throws Exception {

        //Translate test result to actionId
        Integer actionId = null;
        if (testResult.toLowerCase().trim().contains("fail") || testResult.equals(false)) {
            actionId = 71; // Failed
        } else {
            actionId = 61; // Passed
        }
        //Workflow
        for (String key : keys) {
            System.out.println("Updating Issue: " + key);
            setIssueWorkFlow(key, actionId, testResult);
            if (screenshot != null) {
                attachFile(key, screenshot);
            }
        }

    }

    public void attachFile(String issueKey, File attachment) throws Exception {

        jiraService.attachFileToIssue(issueKey, attachment);

    }
}
