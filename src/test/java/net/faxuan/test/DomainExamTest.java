package net.faxuan.test;

import com.beust.jcommander.Parameter;
import net.faxuan.data.ExamDomain;
import net.faxuan.data.ExamUser;
import net.faxuan.data.ParamInfo;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.DomainExam;
import net.faxuan.tableProject.Exam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by song on 2017/8/8.
 */
public class DomainExamTest extends TestCase{

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private int yearInt = date.get(Calendar.YEAR);

    @BeforeClass(description = "连接数据库；获取数据库report_exam_domain2016表信息然后创建测试report库中的domain_exam表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_exam_domain" + (yearInt-1));
        super.createTable(tableNames);
    }

    @Test(description = "创建单位考试统计数据",priority = 1)
    public void createData() {
        new ExamDomain();
    }

    @Test(description = "对比学法库和测试库中单位考试统计数据的一致性",priority = 2)
    public void contrastData() {
        /**
         * 获取考试ID
         */
        String examIDs = "";
        List<Exam> exams = ConfirmExecutionData.getExams();
        for (Exam exam:exams) {
            examIDs += (exam.getID() + ",");
        }
        examIDs= examIDs.substring(0,examIDs.length()-1);

        /**
         * 获取xf和test的report库中的数据
         */
        DataBase xfReport = new DataBase(DataSource.SourceType.SOURSE4);
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        List<DomainExam> testDomainExams = GetData.getDomainExamInfo(examIDs,testReport,"domain_exam");
        List<DomainExam> testDomainExams2016 = GetData.getDomainExamInfo(examIDs,testReport,"domain_exam" + (yearInt-1));
        List<DomainExam> xfDomainExams = GetData.getDomainExamInfo(examIDs,xfReport,"report_exam_domain");
        List<DomainExam> xfDomainExams2016 = GetData.getDomainExamInfo(examIDs,xfReport,"report_exam_domain" + (yearInt-1));
        xfReport.deconnSQL();
        testReport.deconnSQL();
        /**
         * 对比当年单位考试报表
         */
        if (testDomainExams == null || xfDomainExams ==null) {
            if (testDomainExams == null && xfDomainExams == null) {
                return;
            } else if (testDomainExams == null && xfDomainExams!=null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfDomainExams.size() + "个对象");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testDomainExams.size() + "个对象");
            }
        }else {
            /**
             * 对比单位考试统计的单位数量
             * 如果不相等输出多的单位信息
             */
            if (testDomainExams.size() != xfDomainExams.size()) {
                if (testDomainExams.size() > xfDomainExams.size()) {
                    String superfluousDomainInfo = "";
                    for (DomainExam domainExam1:testDomainExams) {
                        int isSame = 0;
                        for (DomainExam domainExam2:xfDomainExams) {
                            if (domainExam1.getDOMAIN_CODE().equals(domainExam2.getDOMAIN_CODE()) && domainExam1.getEXAM_ID().equals(domainExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousDomainInfo += (domainExam1.toString()+"\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计单位考试数据测试report库考试统计单位数量比学法report库多，多出单位考试信息如下：\n"+superfluousDomainInfo);
                } else {
                    String superfluousDomainInfo = "";
                    for (DomainExam domainExam1:xfDomainExams) {
                        int isSame = 0;
                        for (DomainExam domainExam2:testDomainExams) {
                            if (domainExam1.getDOMAIN_CODE().equals(domainExam2.getDOMAIN_CODE()) && domainExam1.getEXAM_ID().equals(domainExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousDomainInfo += (domainExam1.toString()+"\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计单位考试数据学法report库考试统计单位数量比测试report库多，多出单位考试信息如下：\n"+superfluousDomainInfo);
                }
            }
            for (DomainExam testDomainExam:testDomainExams) {
                for (DomainExam xfDomainExam:xfDomainExams) {
                    if (testDomainExam.getDOMAIN_CODE().equals(xfDomainExam.getDOMAIN_CODE()) && testDomainExam.getEXAM_ID().equals(xfDomainExam.getEXAM_ID())) {
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getAVG_SCORE(),xfDomainExam.getAVG_SCORE());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getPASS_RATE(),xfDomainExam.getPASS_RATE());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getTOTAL_NUM(),xfDomainExam.getTOTAL_NUM());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getNO_JOIN_NUM(),xfDomainExam.getNO_JOIN_NUM());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getNO_PASS_NUM(),xfDomainExam.getNO_PASS_NUM());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getPASS_NUM(),xfDomainExam.getPASS_NUM());
                        Check.check(testDomainExam,xfDomainExam,testDomainExam.getEXAM_YEAR(),xfDomainExam.getEXAM_YEAR());
                    }
                }
            }
        }


        /**
         * 对比往年单位考试报表
         */
        if (testDomainExams2016 == null || xfDomainExams2016 ==null) {
            if (testDomainExams2016 == null && xfDomainExams2016==null) {
                return;
            } else if (testDomainExams2016 == null && xfDomainExams2016!=null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfDomainExams2016.size() + "个对象");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testDomainExams2016.size() + "个对象");
            }
        }else {
            /**
             * 对比单位考试统计的单位数量
             * 如果不相等输出多的单位信息
             */
            if (testDomainExams2016.size() != xfDomainExams2016.size()) {
                if (testDomainExams2016.size() > xfDomainExams2016.size()) {
                    String superfluousDomainInfo = "";
                    for (DomainExam domainExam1:testDomainExams2016) {
                        int isSame = 0;
                        for (DomainExam domainExam2:xfDomainExams2016) {
                            if (domainExam1.getDOMAIN_CODE().equals(domainExam2.getDOMAIN_CODE()) && domainExam1.getEXAM_ID().equals(domainExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousDomainInfo += (domainExam1.toString()+"\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计单位考试数据测试report库考试统计单位数量比学法report库多，多出单位考试考试信息如下：\n"+superfluousDomainInfo);
                } else {
                    String superfluousDomainInfo = "";
                    for (DomainExam domainExam1:xfDomainExams2016) {
                        int isSame = 0;
                        for (DomainExam domainExam2:testDomainExams2016) {
                            if (domainExam1.getDOMAIN_CODE().equals(domainExam2.getDOMAIN_CODE()) && domainExam1.getEXAM_ID().equals(domainExam2.getEXAM_ID())) {
                                isSame = 1;

                            }
                        }
                        if (isSame == 0) {
                            superfluousDomainInfo += (domainExam1.toString()+"\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计单位考试数据学法report库考试统计单位数量比测试report库多，多出单位考试信息如下：\n"+superfluousDomainInfo);
                }
            }
            for (DomainExam testDomainExam2016:testDomainExams2016) {
                for (DomainExam xfDomainExam2016:xfDomainExams2016) {
                    if (testDomainExam2016.getDOMAIN_CODE().equals(xfDomainExam2016.getDOMAIN_CODE()) && testDomainExam2016.getEXAM_ID().equals(xfDomainExam2016.getEXAM_ID())) {
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getAVG_SCORE(),xfDomainExam2016.getAVG_SCORE());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getPASS_RATE(),xfDomainExam2016.getPASS_RATE());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getTOTAL_NUM(),xfDomainExam2016.getTOTAL_NUM());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getNO_PASS_NUM(),xfDomainExam2016.getNO_PASS_NUM());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getNO_JOIN_NUM(),xfDomainExam2016.getNO_JOIN_NUM());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getPASS_NUM(),xfDomainExam2016.getPASS_NUM());
                        Check.check(testDomainExam2016,xfDomainExam2016,testDomainExam2016.getEXAM_YEAR(),xfDomainExam2016.getEXAM_YEAR());
                    }
                }
            }
        }
    }
}
