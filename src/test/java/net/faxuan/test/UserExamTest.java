package net.faxuan.test;


import net.faxuan.data.ExamUser;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UserExam;
import net.faxuan.exception.Check;
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
 * Created by song on 2017/8/2.
 */
public class UserExamTest extends TestCase {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private int yearInt = date.get(Calendar.YEAR);


    @BeforeClass(description = "连接数据库；获取数据库report_exam_user2016表信息然后创建测试report库中的user_exam表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_exam_user" + (yearInt-1));
        super.createTable(tableNames);
    }

    @Test(description = "创建学员考试统计数据",priority = 1)
    public void createData() {
        new ExamUser();
    }


    @Test(description = "对比学法库和测试库中学员考试统计数据的一致性",priority = 2)
    public void contrastData() {
        String examIDs = "";
        List<Exam> exams = ConfirmExecutionData.getExams();
        for (Exam exam:exams) {
            examIDs += (exam.getID() + ",");
        }
        examIDs= examIDs.substring(0,examIDs.length()-1);
        DataBase xfReport = new DataBase(DataSource.SourceType.SOURSE4);
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        List<UserExam> testUserExams = GetData.getUserExamInfo(examIDs, testReport,"user_exam");
        List<UserExam> xfUserExams = GetData.getUserExamInfo(examIDs, xfReport,"report_exam_user");
        List<UserExam> testUserExams2016 = GetData.getUserExamInfo(examIDs, testReport,"user_exam" +(yearInt-1));
        List<UserExam> xfUserExams2016 = GetData.getUserExamInfo(examIDs, xfReport,"report_exam_user" +(yearInt-1));
        xfReport.deconnSQL();
        testReport.deconnSQL();
        /**
         * 对比当前学员考试报表
         */
        if (testUserExams == null || xfUserExams ==null) {
            if (testUserExams == null && xfUserExams==null) {
                return;
            } else if (xfUserExams != null && testUserExams == null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfUserExams.size() + "个");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testUserExams.size() + "个");
            }
        }else {
            /**
             * 对比用户考试统计数量是否相同
             */
            if (testUserExams.size() != xfUserExams.size()) {
                if (testUserExams.size() > xfUserExams.size()) {
                    String superfluousUserInfo = "";
                    for (UserExam userExam1:testUserExams) {
                        int isSame = 0;
                        for (UserExam userExam2:xfUserExams) {
                            if (userExam1.getUSER_ACCOUNT().equals(userExam2.getUSER_ACCOUNT()) && userExam1.getEXAM_ID().equals(userExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userExam1.toString() + "\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计用户考试数据测试report库用户考试统计数量比学法report库多，多出用户考试信息如下：\n"+superfluousUserInfo);
                }else {
                    String superfluousUserInfo = "";
                    for (UserExam userExam1:xfUserExams) {
                        int isSame = 0;
                        for (UserExam userExam2:testUserExams) {
                            if (userExam1.getUSER_ACCOUNT().equals(userExam2.getUSER_ACCOUNT()) && userExam1.getEXAM_ID().equals(userExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userExam1.toString() + "\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计用户考试数据学法report库用户考试统计数量比测试report库多，多出用户考试信息如下：\n"+superfluousUserInfo);
                }
            }
            for (UserExam testUserexam:testUserExams) {
                for (UserExam xfUserExam:xfUserExams) {
                    if (testUserexam.getUSER_ACCOUNT().equals(xfUserExam.getUSER_ACCOUNT()) && testUserexam.getEXAM_ID().equals(xfUserExam.getEXAM_ID())) {
                        Check.check(testUserexam,xfUserExam,testUserexam.getDOMAIN_CODE(),xfUserExam.getDOMAIN_CODE());
                        Check.check(testUserexam,xfUserExam,testUserexam.getUSER_NAME(),xfUserExam.getUSER_NAME());
                        Check.check(testUserexam,xfUserExam,testUserexam.getEXAM_SCORE(),xfUserExam.getEXAM_SCORE());
                        Check.check(testUserexam,xfUserExam,testUserexam.getEXAM_BEGIN_TIME(),xfUserExam.getEXAM_BEGIN_TIME());
                        Check.check(testUserexam,xfUserExam,testUserexam.getEXAM_NAME(),xfUserExam.getEXAM_NAME());
                        Check.check(testUserexam,xfUserExam,testUserexam.getEXAM_STATUS(),xfUserExam.getEXAM_STATUS());
                        Check.check(testUserexam,xfUserExam,testUserexam.getEXAM_YEAR(),xfUserExam.getEXAM_YEAR());
                    }
                }
            }

        }

        /**
         * 对比2016年学员考试报表
         */
        if (testUserExams2016 == null || xfUserExams2016==null) {
            if (testUserExams2016 == null && xfUserExams2016==null) {
                return;
            } else if (testUserExams2016 == null && xfUserExams2016!=null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfUserExams2016.size() + "个");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testUserExams2016.size() + "个");
            }
        } else {
            /**
             * 对比用户考试统计数量是否相同
             */
            if (testUserExams2016.size() != xfUserExams2016.size()) {
                if (testUserExams2016.size() > xfUserExams2016.size()) {
                    String superfluousUserInfo = "";
                    for (UserExam userExam1:testUserExams2016) {
                        int isSame = 0;
                        for (UserExam userExam2:xfUserExams2016) {
                            if (userExam1.getUSER_ACCOUNT().equals(userExam2.getUSER_ACCOUNT()) && userExam1.getEXAM_ID().equals(userExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userExam1.toString() + "\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计用户考试数据测试report库用户考试统计数量比学法report库多，多出用户考试信息如下：\n"+superfluousUserInfo);
                }else {
                    String superfluousUserInfo = "";
                    for (UserExam userExam1:xfUserExams2016) {
                        int isSame = 0;
                        for (UserExam userExam2:testUserExams2016) {
                            if (userExam1.getUSER_ACCOUNT().equals(userExam2.getUSER_ACCOUNT()) && userExam1.getEXAM_ID().equals(userExam2.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userExam1.toString() + "\n");
                        }
                    }
                    throw new CheckException("考试ID" +examIDs + "统计用户考试数据学法report库用户考试统计数量比测试report库多，多出用户考试信息如下：\n"+superfluousUserInfo);
                }
            }

            for (UserExam testUserexam2016:testUserExams2016) {
                for (UserExam xfUserExam2016:xfUserExams2016) {
                    if (testUserexam2016.getUSER_ACCOUNT().equals(xfUserExam2016.getUSER_ACCOUNT()) && testUserexam2016.getEXAM_ID().equals(xfUserExam2016.getEXAM_ID())) {
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getDOMAIN_CODE(),xfUserExam2016.getDOMAIN_CODE());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getUSER_NAME(),xfUserExam2016.getUSER_NAME());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getEXAM_BEGIN_TIME(),xfUserExam2016.getEXAM_BEGIN_TIME());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getEXAM_NAME(),xfUserExam2016.getEXAM_NAME());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getEXAM_SCORE(),xfUserExam2016.getEXAM_SCORE());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getEXAM_STATUS(),xfUserExam2016.getEXAM_STATUS());
                        Check.check(testUserexam2016,xfUserExam2016,testUserexam2016.getEXAM_YEAR(),xfUserExam2016.getEXAM_YEAR());
                    }
                }
            }
        }

    }
}