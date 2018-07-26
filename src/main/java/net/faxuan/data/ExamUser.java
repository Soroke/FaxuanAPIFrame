package net.faxuan.data;

import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UserExam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by song on 2017/8/2.
 */
public class ExamUser {

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 储存获取的考试信息
     */
    List<Exam> exams = new ArrayList<Exam>();

    /**
     * 储存获取的补考考试信息
     */
    List<Exam> makeUpexams = new ArrayList<Exam>();




    public ExamUser() {
        exams = ConfirmExecutionData.getExams();
        makeUpexams = ConfirmExecutionData.getMakeUpexams();
        insert();
    }


    /**
     * 对比主考补考信息并插入数据库
     */
    private void insert() {
        for (Exam exam:exams) {
            List<UserExam> mainExamAndUsers = new ArrayList<UserExam>();
            List<UserExam> makeUpExamAndUsers = new ArrayList<UserExam>();
            if (exam.getINDUSTRY_DOMAIN_FLAG().equals("1") && exam.getTARGET_DOMAIN_CODE().length() == 19) {
                mainExamAndUsers = releaseToIndustryDomain(exam);
            } else {
                mainExamAndUsers = releaseToDomain(exam);
            }
            if (mainExamAndUsers == null) {
                continue;
            }

            if (makeUpexams != null) {
                for (Exam exam1:makeUpexams) {
                    if (exam1.getTARGET_EXAM_ID().equals(exam.getID())){
                        if (exam1.getINDUSTRY_DOMAIN_FLAG().equals("1") && exam1.getTARGET_DOMAIN_CODE().length() == 19) {
                            makeUpExamAndUsers = releaseToIndustryDomain(exam1);
                        } else {
                            makeUpExamAndUsers = releaseToDomain(exam1);
                        }
                        if (makeUpExamAndUsers == null) {
                            return;
                        }


                        mainExamAndUsers = contrastResult(mainExamAndUsers,makeUpExamAndUsers);
                    }
                }
            }
            insertDatabase(exam,mainExamAndUsers);
        }
    }


    /**
     * 发布到普法单位的处理
     * @param exam
     */
    private List<UserExam> releaseToDomain(Exam exam) {

        List<UserExam> examAndUsers = new ArrayList<UserExam>();

        /**
         * 重组考试的RANK供sql使用
         */
        String rankId = exam.getRANK();
        if (rankId.contains(";")) {
            String[] rankIds = rankId.split(";");
            rankId = "";
            for (String rank:rankIds) {
                rankId += ("'" + rank + "',");
            }
            rankId = rankId.substring(0,rankId.length()-1);
        } else {
            rankId = "'" + rankId + "'";
        }


        /**
         * 查询获取所有可以看到该考试的单位下的会员信息考试信息
         */
        String domainCode = exam.getTARGET_DOMAIN_CODE();
        String code = "000";
        //判断是否为管理单位

        ResultSet resultSet = null;
        DataBase xfBase = new DataBase(DataSource.SourceType.SOURSE2);
        if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
            /**
             * 普法管理单位分割单位编码
             */
            for (int i=0;i<5;i++) {
                if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
                    domainCode = domainCode.substring(0,domainCode.length()-3);
                }
            }

            /**
             * 判断考试发布的行业编码，并执行SQL获取所有能看到该考试用户信息
             */
            String industryCode = exam.getINDUSTRY_CODES();

            if (industryCode.contains(";")) {
                String[] ioc = industryCode.split(";");
                industryCode = "";
                for (String i:ioc) {
                    industryCode += (i + ",");
                }
                industryCode = industryCode.substring(0,industryCode.length()-1);

                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + ")) AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            } else if (!industryCode.equals("00000000")){
                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + ")) AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            } else {
                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1')) AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            }
        } else {
            /**
             * 非普法管理单位
             * 获取考试发布的行业编码，并执行SQL获取所有能看到该考试的用户信息
             */
            String industryCode = exam.getINDUSTRY_CODES();
            if (industryCode.contains(";")) {
                String[] ioc = industryCode.split(";");
                industryCode = "";
                for (String i:ioc) {
                    industryCode += (i + ",");
                }
                industryCode = industryCode.substring(0,industryCode.length()-1);

                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE='" + domainCode + "' AND INDUSTRY_CODE IN(" + industryCode + ")) AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            } else if (!industryCode.equals("00000000")){
                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE='" + domainCode + "' AND INDUSTRY_CODE IN(" + industryCode + ")) AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            } else {
                resultSet = xfBase.selectSQL("SELECT * FROM base_user WHERE DOMAIN_CODE IN(\n" +
                        "SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE='" + domainCode + "') AND (POLITICS_CODE IN(" + rankId + ") OR RANK_ID IN(" + rankId + ")) AND LOCK_FLAG = '0';");
            }
        }

        /**
         * 储存用户信息
         */

        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                log.info("考试：" + exam.getEXAM_NAME() + "----考试ID：" + exam.getID() + "没有用户可以看到该考试");
                return null;
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                UserExam examAndUser = new UserExam();
                //User user = new User();
                examAndUser.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                if (exam.getRANK().contains(resultSet.getString("RANK_ID"))) {
                    examAndUser.setRANK_ID(resultSet.getString("RANK_ID"));
                } else {
                    examAndUser.setRANK_ID(resultSet.getString("POLITICS_CODE"));
                }

                examAndUser.setUSER_NAME(resultSet.getString("USER_NAME"));
                examAndUser.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                examAndUsers.add(examAndUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        xfBase.deconnSQL();

        /**
         * 组装所有可以参加该考试用户的UserAccount
         * 然后查询用户的成绩
         */
        DataBase xfExam = new DataBase(DataSource.SourceType.SOURSE3);
        String userAccounts = "";
        for (UserExam examAndUser:examAndUsers) {
            userAccounts += ("'" + examAndUser.getUSER_ACCOUNT() + "',");
        }
        userAccounts = userAccounts.substring(0,userAccounts.length()-1);
        ResultSet examResult = xfExam.selectSQL("SELECT * FROM `exam_exam_result` WHERE USER_ACCOUNT IN(" + userAccounts + ") AND EXAM_ID IN(" + exam.getID() + ");");

        /**
         * 根据用户的UserAccount储存成绩和考试信息
         */
        try {
            examResult.last();
            if (examResult.getRow() == 0) {
                for (UserExam examAndUser:examAndUsers) {
                    examAndUser.setEXAM_NAME(exam.getEXAM_NAME());
                    examAndUser.setEXAM_YEAR(exam.getEXAM_YEAR());
                    examAndUser.setEXAM_BEGIN_TIME(exam.getEXAM_BEGIN_TIME());
                    examAndUser.setEXAM_ID(exam.getID());
                }
            }
            examResult.beforeFirst();
            while (examResult.next()) {
                for (UserExam examAndUser:examAndUsers) {
                    examAndUser.setEXAM_NAME(exam.getEXAM_NAME());
                    examAndUser.setEXAM_YEAR(exam.getEXAM_YEAR());
                    examAndUser.setEXAM_BEGIN_TIME(exam.getEXAM_BEGIN_TIME());
                    examAndUser.setEXAM_ID(exam.getID());
                    if (examAndUser.getUSER_ACCOUNT().equals(examResult.getString("USER_ACCOUNT"))) {
                        examAndUser.setEXAM_SCORE(examResult.getInt("EXAM_RESULT_SCORE"));
                        examAndUser.setEXAM_STATUS(examResult.getString("IS_PASS"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfExam.deconnSQL();
        return examAndUsers;
    }


    /**
     * 发布到行业管理单位的处理
     * @param exam
     */
    private List<UserExam> releaseToIndustryDomain(Exam exam) {

        List<UserExam> examAndUsers = new ArrayList<UserExam>();

        /**
         * 分割行业单位编码，供sql查询使用
         */
        String domainCode = exam.getTARGET_DOMAIN_CODE();
        //String domainCode = "2002001001001000000";
        String code = "000";
        for (int i=0;i<6;i++) {
            if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
                domainCode = domainCode.substring(0,domainCode.length()-3);
            }
        }
        /**
         * 重组考试的RANK供sql使用
         */
        String rankid = exam.getRANK();
        if (rankid.contains(";")) {
            String[] rankIDs = rankid.split(";");
            rankid = "";
            for (String rank:rankIDs) {
                rankid += ( "'" + rank + "',");
            }
            rankid = rankid.substring(0,rankid.length()-1);
        } else {
            rankid = "'" + rankid + "'";
        }

        /**
         * 判断考试发布的行业编码，并执行SQL获取所有能看到该考试用户信息
         */
        DataBase xfBase = new DataBase(DataSource.SourceType.SOURSE2);
        String industryCode = exam.getINDUSTRY_CODES();
        ResultSet resultSet = null;
        if (industryCode.contains(";")) {
            String[] ic = industryCode.split(";");
            industryCode = "";
            for (String i:ic) {
                industryCode += (i + ",");
            }
            industryCode = industryCode.substring(0,industryCode.length()-1);
            resultSet = xfBase.selectSQL("SELECT USER_ACCOUNT,USER_NAME,DOMAIN_CODE,RANK_ID,POLITICS_CODE FROM base_user WHERE DOMAIN_CODE IN" +
                    " (SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN" +
                    " (SELECT BASE_DOMAIN_CODE FROM base_domain_relevance WHERE DOMAIN_CODE IN " +
                    " (SELECT DOMAIN_CODE FROM base_industry_domain WHERE DOMAIN_CODE LIKE '"+ domainCode + "%' AND DOMAIN_TYPE='2')) AND INDUSTRY_CODE IN("+ industryCode +")) AND (POLITICS_CODE IN(" + rankid +") OR RANK_ID IN(" + rankid +")) AND LOCK_FLAG = '0';");
        } else if (!industryCode.equals("00000000")) {
            resultSet = xfBase.selectSQL("SELECT USER_ACCOUNT,USER_NAME,DOMAIN_CODE,RANK_ID,POLITICS_CODE FROM base_user WHERE DOMAIN_CODE IN" +
                    "(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN" +
                    "(SELECT BASE_DOMAIN_CODE FROM base_domain_relevance WHERE DOMAIN_CODE IN " +
                    "(SELECT DOMAIN_CODE FROM base_industry_domain WHERE DOMAIN_CODE LIKE '"+ domainCode + "%' AND DOMAIN_TYPE='2')) AND INDUSTRY_CODE IN("+ industryCode +")) AND (POLITICS_CODE IN(" + rankid +") OR RANK_ID IN(" + rankid +")) AND LOCK_FLAG = '0';");
        } else {
            resultSet = xfBase.selectSQL("SELECT USER_ACCOUNT,USER_NAME,DOMAIN_CODE,RANK_ID,POLITICS_CODE FROM base_user WHERE DOMAIN_CODE IN" +
                    "(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN" +
                    "(SELECT BASE_DOMAIN_CODE FROM base_domain_relevance WHERE DOMAIN_CODE IN " +
                    "(SELECT DOMAIN_CODE FROM base_industry_domain WHERE DOMAIN_CODE LIKE '"+ domainCode + "%' AND DOMAIN_TYPE='2'))) AND (POLITICS_CODE IN(" + rankid +") OR RANK_ID IN(" + rankid +")) AND LOCK_FLAG = '0';");
        }

        /**
         * 储存用户信息
         */

        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                log.info("考试：" + exam.getEXAM_NAME() + "----考试ID：" + exam.getID()+ "没有用户可以看到该考试");
                return null;
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                UserExam examAndUser = new UserExam();
                //User user = new User();
                examAndUser.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                examAndUser.setUSER_NAME(resultSet.getString("USER_NAME"));
                if (exam.getRANK().contains(resultSet.getString("RANK_ID"))) {
                    examAndUser.setRANK_ID(resultSet.getString("RANK_ID"));
                } else {
                    examAndUser.setRANK_ID(resultSet.getString("POLITICS_CODE"));
                }
                examAndUser.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                examAndUsers.add(examAndUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfBase.deconnSQL();

        /**
         * 组装所有可以参加该考试用户的UserAccount
         * 然后查询用户的成绩
         */
        DataBase xfExam = new DataBase(DataSource.SourceType.SOURSE3);
        String userAccounts = "";
        for (UserExam examAndUser:examAndUsers) {
            userAccounts += (examAndUser.getUSER_ACCOUNT() + ",");
        }
        userAccounts = userAccounts.substring(0,userAccounts.length()-1);
        ResultSet examResult = xfExam.selectSQL("SELECT USER_ACCOUNT,EXAM_RESULT_SCORE,IS_PASS FROM `exam_exam_result` WHERE USER_ACCOUNT IN(" + userAccounts + ") AND EXAM_ID IN(" + exam.getID() + ");");

        try {
            examResult.last();
            if (examResult.getRow() == 0) {
                for (UserExam examAndUser:examAndUsers) {
                    examAndUser.setEXAM_NAME(exam.getEXAM_NAME());
                    examAndUser.setEXAM_YEAR(exam.getEXAM_YEAR());
                    examAndUser.setEXAM_BEGIN_TIME(exam.getEXAM_BEGIN_TIME());
                    examAndUser.setEXAM_ID(exam.getID());
                }
            }
            examResult.beforeFirst();
            while (examResult.next()) {
                for (UserExam examAndUser:examAndUsers) {
                    examAndUser.setEXAM_NAME(exam.getEXAM_NAME());
                    examAndUser.setEXAM_BEGIN_TIME(exam.getEXAM_BEGIN_TIME());
                    examAndUser.setEXAM_YEAR(exam.getEXAM_YEAR());
                    examAndUser.setEXAM_ID(exam.getID());
                    if (examAndUser.getUSER_ACCOUNT().equals(examResult.getString("USER_ACCOUNT"))) {
                        examAndUser.setEXAM_SCORE(examResult.getInt("EXAM_RESULT_SCORE"));
                        examAndUser.setEXAM_STATUS(examResult.getString("IS_PASS"));
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfExam.deconnSQL();
        return examAndUsers;

    }

    /**
     * 用户考试信息插入数据库
     * @param exam
     * @param examAndUsers
     */
    private void insertDatabase(Exam exam,List<UserExam> examAndUsers) {
        /**
         * 根据不同年份的考试插入该考试的用户考试信息
         */
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        if (exam.getEXAM_YEAR().equals(yearString)) {
            for (UserExam examAndUser:examAndUsers) {
                String insertSQL="INSERT INTO user_exam(USER_ACCOUNT,DOMAIN_CODE,RANK_ID,EXAM_ID,EXAM_YEAR,EXAM_SCORE,EXAM_STATUS,EXAM_NAME,EXAM_BEGIN_TIME,USER_NAME) VALUES(";
                insertSQL += ("\"" + examAndUser.getUSER_ACCOUNT() + "\",");
                insertSQL += (examAndUser.getDOMAIN_CODE() + ",");
                insertSQL += ("'" + examAndUser.getRANK_ID() + "',");
                insertSQL += (examAndUser.getEXAM_ID() + ",");
                insertSQL += (examAndUser.getEXAM_YEAR() + ",");
                insertSQL += (examAndUser.getEXAM_SCORE() + ",");
                insertSQL += (examAndUser.getEXAM_STATUS() + ",");
                insertSQL += ("\"" + examAndUser.getEXAM_NAME() + "\",");
                insertSQL += ("\"" + examAndUser.getEXAM_BEGIN_TIME() + "\",");
                insertSQL += ("\"" + examAndUser.getUSER_NAME() + "\");");
                Assert.assertEquals(testReport.insertSQL(insertSQL),true);

            }
        } else {
            for (UserExam examAndUser:examAndUsers) {
                String insertSQL="INSERT INTO user_exam" + (yearInt-1) + "(USER_ACCOUNT,DOMAIN_CODE,RANK_ID,EXAM_ID,EXAM_YEAR,EXAM_SCORE,EXAM_STATUS,EXAM_NAME,EXAM_BEGIN_TIME,USER_NAME) VALUES(";
                insertSQL += ("\"" + examAndUser.getUSER_ACCOUNT() + "\",");
                insertSQL += (examAndUser.getDOMAIN_CODE() + ",");
                insertSQL += ("'" + examAndUser.getRANK_ID() + "',");
                insertSQL += (examAndUser.getEXAM_ID() + ",");
                insertSQL += (examAndUser.getEXAM_YEAR() + ",");
                insertSQL += (examAndUser.getEXAM_SCORE() + ",");
                insertSQL += (examAndUser.getEXAM_STATUS() + ",");
                insertSQL += ( "\"" + examAndUser.getEXAM_NAME() + "\",");
                insertSQL += ( "\"" + examAndUser.getEXAM_BEGIN_TIME() + "\",");
                insertSQL += ("\"" + examAndUser.getUSER_NAME() + "\");");
                Assert.assertEquals(testReport.insertSQL(insertSQL),true);
            }
        }
        testReport.deconnSQL();
    }

    /**
     * 对比学员主考和补考成绩信息，哪个成绩高就赋值哪个成绩
     * @param mainExamAndUsers 主考
     * @param makeUpExamAndUsers 补考
     * @return  List<ExamAndUser> 对比过后取对打成绩的用户成绩信息
     */
    private List<UserExam> contrastResult(List<UserExam> mainExamAndUsers,List<UserExam> makeUpExamAndUsers) {
        for (UserExam examAndUser1:mainExamAndUsers) {
            for (UserExam examAndUser2:makeUpExamAndUsers) {
                if (examAndUser1.getUSER_ACCOUNT().equals(examAndUser2.getUSER_ACCOUNT())) {
                    if (examAndUser1.getEXAM_SCORE() < examAndUser2.getEXAM_SCORE()) {
                        examAndUser1.setEXAM_SCORE(examAndUser2.getEXAM_SCORE());
                        examAndUser1.setEXAM_STATUS(examAndUser2.getEXAM_STATUS());
                    }
                    continue;
                }
            }
        }
        return mainExamAndUsers;
    }

}
