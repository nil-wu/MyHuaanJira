package publish;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.IssueType;

public class ThirdPart extends AutoPublish{

    @Override
    public String systemName() {
        return "第三方供应商管理平台";
    }

    @Override
    public BasicProject getProject() throws Exception {
        return null;
    }

    @Override
    public IssueType getIssueType() throws Exception {
        return null;
    }


}
