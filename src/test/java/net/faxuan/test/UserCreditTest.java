package net.faxuan.test;

import net.faxuan.data.CreditUser;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UserCredit;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/15.
 */
public class UserCreditTest extends TestCase{
    @BeforeClass(description = "连接数据库；获取数据库credit_user_report2016表信息然后创建测试report库中的user_credit表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("credit_user_report2017");
        super.createTable(tableNames);
    }

    @Test(description = "创建学员学分统计数据",priority = 1)
    public void createData() {
        new CreditUser();
    }

    @Test(description = "对比学法库和测试库中学员学分统计数据的一致性",priority = 2)
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
        List<UserCredit> testUserCredit = GetData.getUserCreditInfo(domainCode,examIds,testReport,"user_credit");
        List<UserCredit> xfUserCredit = GetData.getUserCreditInfo(domainCode,examIds,xfReport,"credit_user_report");
        xfReport.deconnSQL();
        testReport.deconnSQL();

        /**
         * 对比当前学员学分报表是否为空
         */
        if (testUserCredit == null || xfUserCredit ==null) {
            if (testUserCredit == null && xfUserCredit==null) {
                return;
            } else if (xfUserCredit != null && testUserCredit == null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfUserCredit.size() + "个");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testUserCredit.size() + "个");
            }
        }
        if (testUserCredit.size() != xfUserCredit.size()) {
            if (testUserCredit.size() > xfUserCredit.size()) {
                String superfluousUserInfo = "";
                for (UserCredit userCredit1:testUserCredit) {
                    int isSame = 0;
                    for (UserCredit userCredit2:xfUserCredit) {
                        if (userCredit1.getUSER_ACCOUNT().equals(userCredit2.getUSER_ACCOUNT()) && userCredit1.getEXAM_ID().equals(userCredit2.getEXAM_ID())) {
                            isSame = 1;
                        }
                    }
                    if (isSame == 0) {
                        superfluousUserInfo += (userCredit1.toString() + "\n");
                    }
                }
                throw new CheckException("统计用户学分数据测试report库用户考试统计数量比学法report库多，多出用户学分信息如下：\n"+superfluousUserInfo);
            }else {
                String superfluousUserInfo = "";
                for (UserCredit userCredit1:xfUserCredit) {
                    int isSame = 0;
                    for (UserCredit userCredit2:testUserCredit) {
                        if (userCredit1.getUSER_ACCOUNT().equals(userCredit2.getUSER_ACCOUNT()) && userCredit1.getEXAM_ID().equals(userCredit2.getEXAM_ID())) {
                            isSame = 1;
                        }
                    }
                    if (isSame == 0) {
                        superfluousUserInfo += (userCredit1.toString() + "\n");
                    }
                }
                throw new CheckException("统计用户学分数据学法report库用户考试统计数量比测试report库多，多出用户学分信息如下：\n"+superfluousUserInfo);
            }
        }

        /**
         * 对比当前学员学分报表数据
         */

        for (UserCredit userCredit1:testUserCredit) {
            for (UserCredit userCredit2:xfUserCredit) {
                if (userCredit1.getUSER_ACCOUNT().equals(userCredit2.getUSER_ACCOUNT()) && userCredit1.getEXAM_ID().equals(userCredit2.getEXAM_ID())) {
                    Check.check(userCredit1,userCredit2,userCredit1.getDOMAIN_CODE(),userCredit2.getDOMAIN_CODE());
                    Check.check(userCredit1,userCredit2,userCredit1.getRANK_ID(),userCredit2.getRANK_ID());
                    Check.check(userCredit1,userCredit2,userCredit1.getEXAM_YEAR(),userCredit2.getEXAM_YEAR());
                    Check.check(userCredit1,userCredit2,userCredit1.getEXAM_SCORE(),userCredit2.getEXAM_SCORE());
                    Check.check(userCredit1,userCredit2,userCredit1.getEXAM_STATUS(),userCredit2.getEXAM_STATUS());
                    Check.check(userCredit1,userCredit2,userCredit1.getPOINT_SCORE(),userCredit2.getPOINT_SCORE());
                    Check.check(userCredit1,userCredit2,userCredit1.getCREDIT_SCORE(),userCredit2.getCREDIT_SCORE());
                    Check.check(userCredit1,userCredit2,userCredit1.getUSER_NAME(),userCredit2.getUSER_NAME());
                }
            }
        }


    }
}
