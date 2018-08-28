package net.faxuan.test;

import net.faxuan.data.CreditDomain;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.DomainCredit;
import net.faxuan.tableProject.Exam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/16.
 */
public class DomainCreditTest extends TestCase{
    @BeforeClass(description = "连接数据库；获取数据库credit_domain_report2016表信息然后创建测试report库中的domain_credit表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("credit_domain_report2016");
        super.createTable(tableNames);
    }

    @Test(description = "创建单位学分统计数据",priority = 1)
    public void createData() {
        new CreditDomain();
    }

    @Test(description = "对比学法库和测试库中单位学分统计数据的一致性",priority = 2)
    public void contrastData() {
        String domainCode = ConfirmExecutionData.getDomainCode();
        List<Exam> exams = ConfirmExecutionData.getExams();
        String examIds = "";
        for (Exam exam:exams) {
            examIds += (exam.getID() + ",");
        }
        examIds = examIds.substring(0,examIds.length()-1);

        /**
         * 学法库和测试库的链接对象
         */
        DataBase xfReport = new DataBase(DataSource.SourceType.XFREPORT);
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        List<DomainCredit> testDomainCredit = GetData.getDomainCreditInfo(domainCode,examIds,testReport,"domain_credit");
        List<DomainCredit> xfDomainCredit = GetData.getDomainCreditInfo(domainCode,examIds,xfReport,"credit_domain_report");
        xfReport.deconnSQL();
        testReport.deconnSQL();

        if (testDomainCredit == null || xfDomainCredit ==null) {
            if (testDomainCredit == null && xfDomainCredit==null) {
                return;
            } else if (xfDomainCredit != null && testDomainCredit == null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfDomainCredit.size() + "个");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testDomainCredit.size() + "个");
            }
        }else {
            if (testDomainCredit.size() != xfDomainCredit.size()) {
                if (testDomainCredit.size() > xfDomainCredit.size()) {
                    String superfluousUserInfo = "";
                    for (DomainCredit domainCredit1:testDomainCredit) {
                        int isSame = 0;
                        for (DomainCredit domainCredit2:xfDomainCredit) {
                            if (domainCredit1.getEXAM_ID().equals(domainCredit2.getEXAM_ID()) && domainCredit1.getDOMAIN_CODE().equals(domainCredit2.getDOMAIN_CODE())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (domainCredit1.toString() + "\n");
                        }
                    }
                    throw new CheckException("统计用户积分数据测试report库用户考试统计数量比学法report库多，多出单位学分信息如下：\n"+superfluousUserInfo);
                }else {
                    String superfluousUserInfo = "";
                    for (DomainCredit domainCredit1:xfDomainCredit) {
                        int isSame = 0;
                        for (DomainCredit domainCredit2:testDomainCredit) {
                            if (domainCredit1.getEXAM_ID().equals(domainCredit2.getEXAM_ID()) && domainCredit1.getDOMAIN_CODE().equals(domainCredit2.getDOMAIN_CODE())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (domainCredit1.toString() + "\n");
                        }
                    }
                    throw new CheckException("统计用户积分数据学法report库用户考试统计数量比测试report库多，多出单位学分信息如下：\n"+superfluousUserInfo);
                }
            }
        }

        /**
         * 对比学法和测试库的测试数据
         */
        for (DomainCredit domainCredit1:testDomainCredit) {
            for (DomainCredit domainCredit2:xfDomainCredit) {
                if (domainCredit1.getDOMAIN_CODE().equals(domainCredit2.getDOMAIN_CODE()) && domainCredit1.getEXAM_ID().equals(domainCredit2.getEXAM_ID())) {
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getAVG_CREDIT_SCORE(),domainCredit2.getAVG_CREDIT_SCORE());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getEXAM_YEAR(),domainCredit2.getEXAM_YEAR());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getPASS_NUM(),domainCredit2.getPASS_NUM());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getNO_PASS_NUM(),domainCredit2.getNO_PASS_NUM());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getNO_JOIN_NUM(),domainCredit2.getNO_JOIN_NUM());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getTOTAL_NUM(),domainCredit2.getTOTAL_NUM());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getPASS_RATE(),domainCredit2.getPASS_RATE());
                    Check.check(domainCredit1,domainCredit2,domainCredit1.getAVG_SCORE(),domainCredit2.getAVG_SCORE());
                }
            }
        }

    }
}
