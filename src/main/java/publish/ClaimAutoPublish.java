package publish;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.IssueType;

import java.net.URI;

/**
 * 理赔核心的自动生成publish
 */
public class ClaimAutoPublish extends AutoPublish{

    @Override
    public String systemName() {
        return "核心理赔系统";
    }

    @Override
    public BasicProject getProject() throws Exception{
        return new BasicProject(new URI("http://jira.sinosafe.com.cn/rest/api/2/project/10402"),"PUBLISH",null,null);
    }

    @Override
    public IssueType getIssueType() throws Exception{
        return new IssueType(new URI("http://jira.sinosafe.com.cn/rest/api/2/issuetype/10404"),new Long(10404),"生产环境系统发布",false,null,null);
    }

}
