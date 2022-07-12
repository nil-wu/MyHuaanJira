import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import io.atlassian.util.concurrent.Promise;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void readIssue() throws Exception{
        JiraProxyImpl jiraProxyImpl = new JiraProxyImpl();
        JiraRestClient jiraRestClient = jiraProxyImpl.initJiraRestClient();
        IssueRestClient issueRestClient = jiraRestClient.getIssueClient();
        Issue issue = issueRestClient.getIssue("PUBLISH-3496").claim();
        System.out.println(issue);
        jiraRestClient.close();
    }

    public static void main(String[] args) throws Exception{

//        String issueNum = "TEST-45792";
        JiraProxyImpl jiraProxyImpl = new JiraProxyImpl();
        JiraRestClient jiraRestClient = jiraProxyImpl.initJiraRestClient();
        IssueRestClient issueRestClient = jiraRestClient.getIssueClient();

        BasicProject project = new BasicProject(new URI("http://jira.sinosafe.com.cn/rest/api/2/project/10402"),"PUBLISH",null,null);
        IssueType issueType = new IssueType(new URI("http://jira.sinosafe.com.cn/rest/api/2/issuetype/10404"),new Long(10404),"生产环境系统发布",false,null,null);
        String summary = "核心理赔ILOG系统常规版本CILOG_V4.6.1上线发布申请(测试用)";
        BasicUser assignee = new BasicUser(new URI("http://jira.sinosafe.com.cn/rest/api/2/user?username=wuhaopeng"), "wuhaopeng", "吴浩鹏");
        String description = null;
        String parentKey = null;
        File attachment = null;

//        final int buildNumber = issueRestClient.getMetadataClient().getServerInfo().claim().getBuildNumber();

        createNewIssue(issueRestClient, project, assignee, issueType, summary, description, parentKey, attachment);

        jiraRestClient.close();
    }

    public static ComplexIssueInputFieldValue getFieldString(String self,String value,String id){
        HashMap customfield = new HashMap();
        customfield.put("self", self);
        customfield.put("value", value);
        customfield.put("id", id);
        return new ComplexIssueInputFieldValue(customfield);
    }

    public static Issue createNewIssue(IssueRestClient issueClient,BasicProject project, BasicUser assignee, IssueType issueType, String summary, String description, String parentKey, File attachment) {
        try {
            ComplexIssueInputFieldValue.with("", "");
            IssueInputBuilder issueBuilder = new IssueInputBuilder(project, issueType);
            issueBuilder.setDescription(description);
            issueBuilder.setSummary(summary);
            issueBuilder.setProjectKey(project.getKey());
            issueBuilder.setProject(project);
            issueBuilder.setIssueType(issueType);
//            issueBuilder.setAssignee(assignee);
            if (parentKey != null) {
                Map<String, Object> parent = new HashMap<String, Object>();
                parent.put("key", parentKey);
                FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
                issueBuilder.setFieldInput(parentField);
            }

            //版本编号
            issueBuilder.setFieldValue("customfield_10719", "CILOG_V4.6.1");
            //发布包的SVN路径
            issueBuilder.setFieldValue("customfield_10723", "http://svn.sinosafe.com.cn:1580/svn/NEWPCIS/06.上线发布/ILOG/理赔/CILOG_V4.6.1");
            //申请发布时间
            issueBuilder.setFieldValue("customfield_10721", "2022-06-14T21:00:00.000+0800");
            //发布内容
            issueBuilder.setFieldValue("customfield_10720", "REQ-14196 关于三者车只有挂车的情况下核损提交提示回写立案错误问题修复");
            //排期类型
            issueBuilder.setFieldValue("customfield_10760", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11325","常规","11325"));
            //自测结论
            issueBuilder.setFieldValue("customfield_10727", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11255","自测通过","11255"));
            //包含新产品
            issueBuilder.setFieldValue("customfield_12600", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/14205","不包含","14205"));
            //风险等级
            issueBuilder.setFieldValue("customfield_12300", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/13902","低","13902"));

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
            Map<String,Object> jiraDto1 = new HashMap<>();
            jiraDto1.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11400");
            jiraDto1.put("id", "11400");
            jiraDto1.put("Value", "发布升级包文件");
            Map<String,Object> jiraDto2 = new HashMap<>();
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

//            BasicPriority basicPriority = new BasicPriority(new URI("http://jira.sinosafe.com.cn/rest/api/2/priority/3"), new Long(3), "一般");
//            issueBuilder.setPriority(basicPriority);
//            issueBuilder.setReporter(assignee);

//            HashMap customfield_10307 = new HashMap();
//            customfield_10307.put("self", "http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10558");
//            customfield_10307.put("value", "普通");
//            customfield_10307.put("id", "10558");
            //紧急程度
//            issueBuilder.setFieldValue("customfield_10307", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10558","普通","10558"));
//
//            //服务分类
//            issueBuilder.setFieldValue("customfield_10308", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10561","资源申请","10561"));
//
//            //测试阶段
//            issueBuilder.setFieldValue("customfield_10212", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10406","UAT测试","10406"));
//
//            //问题分类

//            //系统需求类型
//            issueBuilder.setFieldValue("customfield_10704", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11213","一般需求","11213"));
//
//            //代理环境
//            issueBuilder.setFieldValue("customfield_10806", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/11411","生产","11411"));
//

//
//            //需求分类（REQU）
////            issueBuilder.setFieldValue("customfield_10314", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10585","业务部门需求","10585"));
//
//            //桌面问题分类
//            issueBuilder.setFieldValue("customfield_10318", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10606","其他桌面问题","10606"));

//            //creator
//            HashMap<String, Object> creator = new HashMap<>();
//            creator.put("self", "http://jira.sinosafe.com.cn/rest/api/2/user?username=wuhaopeng");
//            creator.put("name", "wuhaopeng");
//            creator.put("emailAddress", "wuhaopeng@sinosafe.com.cn");
////            creator.put("avatarUrls", "");
//            creator.put("displayName", "吴浩鹏");
////            creator.put("active", true);
////            issueBuilder.setFieldValue("creator", new ComplexIssueInputFieldValue(creator));

//            //计划发布时间
//            issueBuilder.setFieldValue("customfield_10735", "2022-06-14T21:00:00.000+0800");

//            //满意度
//            issueBuilder.setFieldValue("customfield_10516", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/10822","5-非常满意","10822"));

//            //是否商车改革问题
//            issueBuilder.setFieldValue("customfield_12814", getFieldString("http://jira.sinosafe.com.cn/rest/api/2/customFieldOption/14381","否","14381"));

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
            System.out.println("Error while creating new Jira issue for input paramenters project : " + (project != null ? project.getName() : null) + " assignee : " + (assignee != null ? assignee.getName() : null)
                    + " issueType : " + (issueType != null ? issueType.getName() : null) + " summary : " + summary + " description : " + description);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
