import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;


public class JiraProxyImpl {

    private final String JIRA_URL = "http://jira.sinosafe.com.cn";
    private final String JIRA_USER = "wuhaopeng";
    private final String JIRA_PASSWORD = "ha-170934";

    private JiraRestClient jiraRestClient;
    private IssueRestClient issueClient;

//    public IssueRestClient getIssueClient() {
//        return initJiraRestClient() ;
//    }

    /**
     * 登录JIRA并返回指定的JiraRestClient对象
     * 这里不要每次都new，会造成 ApacheAsyncHttpClient 内存泄漏
     * 用统一的一个client就可以了
     */
    @PostConstruct
    public JiraRestClient initJiraRestClient() {
        AsynchronousJiraRestClientFactory asyncClientFactory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = asyncClientFactory.createWithBasicHttpAuthentication(
                URI.create(JIRA_URL), JIRA_USER, JIRA_PASSWORD
        );

//        issueClient = jiraRestClient.getIssueClient();
        return jiraRestClient;
    }

    @PreDestroy
    public void closeJiraRestClient() {
        try {
            if (jiraRestClient != null) {
                jiraRestClient.close();
            }
        } catch (IOException e) {
            System.out.println("closeJiraRestClient 异常: ");
        }
    }
}
