package publish;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.IssueType;

public class ILOGAutoPublish extends AutoPublish{

    @Override
    public String systemName() {
        return "核心理赔ILOG系统";
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
