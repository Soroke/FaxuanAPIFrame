package net.faxuan.data;

import net.faxuan.exception.CheckException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/3.
 */
public class ParamInfo {
    private String defaultEnviroment = "测试环境";
    private String defaultDomainCode = "101002000000000";
    private String defaultExamId = "1929,1931,1908,1689";
    private String environment;

    public String getEnviroment() {
         environment = System.getProperty("environment") == null || System.getProperty("environment").equals("")?defaultEnviroment:System.getProperty("environment");
        return environment;
    }

    public List<String> getDomainCodes() {
        List<String> domainCodes = new ArrayList<String>();

        String domain = System.getProperty("domainCode") == null || System.getProperty("domainCode").equals("")?defaultDomainCode:System.getProperty("domainCode");
        if (domain.contains(",")) {
            String[] domains = domain.split(",");
            for(String d:domains) {
                domainCodes.add(d);
            }
        } else {
            domainCodes.add(domain);
        }

        return domainCodes;
    }

    public List<String> getExamIds() {
        try {
            if (System.getProperty("examId") == "" || System.getProperty("examId").equals("")) {
                throw new CheckException("传入考试ID为空，停止执行");
            } else if (System.getProperty("examId").contains("，")) {
                throw new CheckException("多个考试ID之间不能用中文的逗号！！！");
            }
        } catch (NullPointerException e) {
            throw new CheckException("传入考试ID为空，停止执行");
        }

        List<String> examIds = new ArrayList<String>();
        String exam = System.getProperty("examId") == null || System.getProperty("examId").equals("")?defaultExamId:System.getProperty("examId");

        if (exam.contains(",")) {
            String [] exams = exam.split(",");

            for(String ex:exams) {
                examIds.add(ex);
            }
        } else examIds.add(exam);
        return examIds;
    }
}
