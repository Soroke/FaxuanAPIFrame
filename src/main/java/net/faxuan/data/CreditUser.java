package net.faxuan.data;

import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UserCredit;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/15.
 * 用户学分库测试
 */
public class CreditUser {
    private String domainCodes;
    List<Exam> exams = new ArrayList<Exam>();

    List<UserCredit> userCredits = new ArrayList<UserCredit>();

    public CreditUser() {
        domainCodes = ConfirmExecutionData.getDomainCode();
        exams = ConfirmExecutionData.getExams();
        getCresitUserInfo();
        insertDataBase(userCredits);
    }

    /**
     * 获取用户的学分信息
     */
    private void getCresitUserInfo() {
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        for (Exam exam:exams) {
            double examProportion = Double.valueOf(exam.getPROPORTION())/100;
            double pointProportion = 1 - examProportion;
            if (exam.getIS_CREDIT() == 0) {
                ResultSet resultSet = testReport.selectSQL("SELECT ue.USER_ACCOUNT,ue.USER_NAME,ue.DOMAIN_CODE,ue.RANK_ID,ue.EXAM_ID,ue.EXAM_YEAR,ue.EXAM_SCORE,ue.EXAM_STATUS,up.TPOINT,up.EXT_1 FROM user_exam ue JOIN user_point up ON ue.DOMAIN_CODE=up.DOMAIN_CODE AND ue.USER_ACCOUNT=up.USER_ACCOUNT AND ue.EXAM_ID=" + exam.getID() + ";");
                try {
                    resultSet.last();
                    if (resultSet.getRow() == 0 ) {
                        continue;
                    }
                    resultSet.beforeFirst();

                    while (resultSet.next()) {
                        UserCredit userCredit = new UserCredit();
                        userCredit.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                        userCredit.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                        userCredit.setRANK_ID(resultSet.getString("RANK_ID"));
                        userCredit.setEXAM_ID(resultSet.getString("EXAM_ID"));
                        userCredit.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                        double examScore = resultSet.getDouble("EXAM_SCORE");
                        userCredit.setEXAM_SCORE(examScore);
                        userCredit.setEXAM_STATUS(resultSet.getString("EXAM_STATUS"));
                        double pointScore = resultSet.getInt("TPOINT");
                        userCredit.setPOINT_SCORE(pointScore);
                        userCredit.setUSER_NAME(resultSet.getString("USER_NAME"));
                        double tPoint = resultSet.getInt("EXT_1");
                        double creditScore;
                        if (resultSet.getString("USER_ACCOUNT").equals("4444444660007") && resultSet.getString("EXAM_ID").equals("1920")) {
                            int i = 111;
                        }
                        if (pointScore > tPoint) {
                            creditScore = (pointProportion + (examScore/exam.getEXAM_SCORE())*examProportion)*100;
                        } else {
                            creditScore = ((pointScore/tPoint)*pointProportion + (examScore/exam.getEXAM_SCORE())*examProportion)*100;
                        }
//creditScore = Math.rint(creditScore);
                        creditScore = (double)Math.round(creditScore*100)/100;
                        userCredit.setCREDIT_SCORE(creditScore);
                        userCredits.add(userCredit);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        testReport.deconnSQL();
    }

    private void insertDataBase(List<UserCredit> userCredits) {
        if (userCredits == null) return;
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        for (UserCredit userCredit:userCredits) {
            String insertSQL="INSERT INTO user_credit(USER_ACCOUNT,USER_NAME,DOMAIN_CODE,RANK_ID,EXAM_ID,EXAM_YEAR,EXAM_SCORE,EXAM_STATUS,POINT_SCORE,CREDIT_SCORE) VALUES(" +
                    userCredit.getUSER_ACCOUNT() + ",'" +
                    userCredit.getUSER_NAME() + "'," +
                    userCredit.getDOMAIN_CODE() + "," +
                    "'" + userCredit.getRANK_ID() + "'," +
                    userCredit.getEXAM_ID() + "," +
                    userCredit.getEXAM_YEAR() + "," +
                    userCredit.getEXAM_SCORE() + "," +
                    userCredit.getEXAM_STATUS() + "," +
                    userCredit.getPOINT_SCORE() + "," +
                    userCredit.getCREDIT_SCORE() + ");";
//System.out.println(insertSQL);
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
        }
    }

}
