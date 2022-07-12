package publish;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import io.atlassian.util.concurrent.Promise;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import util.JiraUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AutoPublish {

    public abstract BasicProject getProject()throws Exception;

    public abstract IssueType getIssueType() throws Exception;

    public abstract String systemName();

    public String getSummary(String publishNum){
        return systemName() + publishNum + "上线发布申请";
    }

    public final Issue createPublish(String reqId) throws Exception {

        JiraRestClient jiraRestClient = JiraUtils.initJiraRestClient();
        IssueRestClient issueClient = jiraRestClient.getIssueClient();
        BasicProject project = getProject();
        IssueType issueType = getIssueType();

        //当前传入Req的内容
        Issue issue = issueClient.getIssue(reqId).claim();

        //开发版本号
        IssueField issueField1 = issue.getFieldByName("开发版本号");
        JSONArray jSONArray = (JSONArray)issueField1.getValue();
        JSONObject jSONObject = (JSONObject) jSONArray.get(0);
        String versionId = (String) jSONObject.get("name");
        System.out.println(versionId);


        //publish的标题
        String summary = getSummary(versionId);

        if (1 == 1) {
            JiraUtils.closeJiraRestClient();
            return null;
        }

        try {

            IssueInputBuilder issueBuilder = new IssueInputBuilder(project, getIssueType());
            issueBuilder.setProjectKey(project.getKey());
//            issueBuilder.setProject(project);
//            issueBuilder.setIssueType(issueType);
//            issueBuilder.setDescription(description);
            issueBuilder.setSummary(summary);

            //版本编号
            issueBuilder.setFieldValue("customfield_10719", versionId);
            //发布包的SVN路径
            issueBuilder.setFieldValue("customfield_10723", "http://svn.sinosafe.com.cn:1580/svn/NEWPCIS/06.上线发布/ILOG/理赔/CILOG_V4.6.1");
            //申请发布时间
            issueBuilder.setFieldValue("customfield_10721", "2022-06-14T21:00:00.000+0800");
            //发布内容
            issueBuilder.setFieldValue("customfield_10720", "REQ-14196 关于三者车只有挂车的情况下核损提交提示回写立案错误问题修复");
            //排期类型
            issueBuilder.setFieldValue("customfield_10760", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11325", "常规", "11325"));
            //自测结论
            issueBuilder.setFieldValue("customfield_10727", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11255", "自测通过", "11255"));
            //包含新产品
            issueBuilder.setFieldValue("customfield_12600", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/14205", "不包含", "14205"));
            //风险等级
            issueBuilder.setFieldValue("customfield_12300", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/13902", "低", "13902"));

            //系统
            HashMap customfield_10501_child = new HashMap();
            customfield_10501_child.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/14119");
            customfield_10501_child.put("value", "B07 理赔iLog");
            customfield_10501_child.put("id", "14119");
            HashMap customfield_10501 = new HashMap();
            customfield_10501.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10852");
            customfield_10501.put("value", "B理赔服务类（*新核心）");
            customfield_10501.put("id", "10852");
            customfield_10501.put("child", new ComplexIssueInputFieldValue(customfield_10501_child));
            issueBuilder.setFieldValue("customfield_10501", new ComplexIssueInputFieldValue(customfield_10501));

            //发布选择项
            List<ComplexIssueInputFieldValue> JiraDtos = new ArrayList<>();
            Map<String, Object> jiraDto1 = new HashMap<>();
            jiraDto1.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11400");
            jiraDto1.put("id", "11400");
            jiraDto1.put("Value", "发布升级包文件");
            Map<String, Object> jiraDto2 = new HashMap<>();
            jiraDto2.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/14404");
            jiraDto2.put("id", "14404");
            jiraDto2.put("Value", "发布ilog规则");
            JiraDtos.add(new ComplexIssueInputFieldValue(jiraDto1));
            JiraDtos.add(new ComplexIssueInputFieldValue(jiraDto2));
            issueBuilder.setFieldValue("customfield_10722", JiraDtos);

            //版本类型
            List<ComplexIssueInputFieldValue> JiraDtos3 = new ArrayList<>();
            Map<String, Object> customfield_10759 = new HashMap<>();
            customfield_10759.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11323");
            customfield_10759.put("id", "11323");
            customfield_10759.put("value", "修复");
            JiraDtos3.add(new ComplexIssueInputFieldValue(customfield_10759));
            issueBuilder.setFieldValue("customfield_10759", JiraDtos3);


            IssueInput issueInput = issueBuilder.build();

            BasicIssue newBasicIssue = issueClient.createIssue(issueInput).claim();
            Promise<Issue> newIssue = issueClient.getIssue(newBasicIssue.getKey());
//            Issue newIssue = issueClient.getIssue(newBasicIssue.getKey());
//            if (attachment != null && newIssue != null)
            System.out.println(newIssue.get());
            System.out.println(" 成功创建publish : " + newIssue.get().getKey());
//                issueClient.addAttachments(pm, newIssue.getAttachmentsUri(), attachment);
            return newIssue.get();
        } catch (RestClientException e) {
            System.out.println("Error while creating new Jira issue for input paramenters project : " + (project != null ? project.getName() : null)
                    + " issueType : " + (issueType != null ? issueType.getName() : null) + " summary : " + summary);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static ComplexIssueInputFieldValue getFieldString(String self,String value,String id){
        HashMap customfield = new HashMap();
        customfield.put("self", self);
        customfield.put("value", value);
        customfield.put("id", id);
        return new ComplexIssueInputFieldValue(customfield);
    }
}
