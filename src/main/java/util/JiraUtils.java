package util;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.io.IOException;
import java.net.URI;

public class JiraUtils {

    private final static String JIRA_URL = "http://jira.sinosafe.com.cn";
    private final static String JIRA_USER = "wuhaopeng";
    private final static String JIRA_PASSWORD = "ha-170934";

    private static JiraRestClient jiraRestClient;
//    private static IssueRestClient issueClient;

    public static JiraRestClient initJiraRestClient() {
        AsynchronousJiraRestClientFactory asyncClientFactory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = asyncClientFactory.createWithBasicHttpAuthentication(
                URI.create(JIRA_URL), JIRA_USER, JIRA_PASSWORD
        );

//        issueClient = jiraRestClient.getIssueClient();
        return jiraRestClient;
    }

    public static void closeJiraRestClient() {
        try {
            if (jiraRestClient != null) {
                jiraRestClient.close();
            }
        } catch (IOException e) {
            System.out.println("closeJiraRestClient 异常: ");
        }
    }

}
