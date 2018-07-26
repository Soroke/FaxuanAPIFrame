package net.faxuan.data;

import net.faxuan.tableProject.DomainCredit;
import net.faxuan.tableProject.Exam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/16.
 */
public class CreditDomain {
    /**
     * 储存单位学分信息
     */
    List<DomainCredit> domainCredits= new ArrayList<DomainCredit>();

    /**
     * 统计单位编码
     */
    String domainCodes = "";

    /**
     * 考试
     */
    List<Exam> exams = new ArrayList<Exam>();

    public CreditDomain() {
        exams = ConfirmExecutionData.getExams();
        domainCodes = ConfirmExecutionData.getDomainCode();
        getCreditDomainInfo();
        insertDataBase();
    }

    /**
     * 获取单位学分信息
     */
    private void getCreditDomainInfo() {


        List<String> domainCode = new ArrayList<String>();
        if (domainCodes.contains(",")) {
            String[] domain = domainCodes.split(",");
            for (String d:domain) {
                domainCode.add(d);
            }
        } else domainCode.add(domainCodes);
        /**
         * 测试库连接
         */
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        for (String domain:domainCode) {
            for (Exam exam:exams) {
                ResultSet resultSet = testReport.selectSQL("SELECT * FROM domain_exam WHERE DOMAIN_CODE=" + domain + " AND EXAM_ID=" + exam.getID() + ";");

                try {
                    resultSet.last();
                    if (resultSet.getRow() == 0) {
                        continue;
                    }
                    resultSet.beforeFirst();

                    while (resultSet.next()) {
                        DomainCredit domainCredit = new DomainCredit();
                        domainCredit.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                        domainCredit.setEXAM_ID(exam.getID());
                        domainCredit.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                        domainCredit.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                        domainCredit.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                        domainCredit.setNO_JOIN_NUM(resultSet.getInt("NO_JOIN_NUM"));
                        domainCredit.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                        domainCredit.setPASS_RATE(resultSet.getInt("PASS_RATE"));
                        domainCredit.setAVG_SCORE(resultSet.getInt("AVG_SCORE"));
                        domainCredits.add(domainCredit);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ResultSet resultSet1 = testReport.selectSQL("SELECT DOMAIN_CODE,CAST(AVG(CREDIT_SCORE) AS DECIMAL(38,2)) AVG_CREDIT_SCORE FROM `user_credit` WHERE DOMAIN_CODE='" + domain +"' AND EXAM_ID=" + exam.getID() + ";");
                try {
                    resultSet1.last();
                    if (resultSet1.getRow() == 0) {
                        continue;
                    }
                    resultSet1.beforeFirst();

                    while (resultSet1.next()) {
                        for (DomainCredit domainCredit:domainCredits) {
                            if (domainCredit.getDOMAIN_CODE().equals(resultSet1.getString("DOMAIN_CODE")) && domainCredit.getEXAM_ID().equals(exam.getID())) {
                                domainCredit.setAVG_CREDIT_SCORE(resultSet1.getDouble("AVG_CREDIT_SCORE"));
                                continue;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        testReport.deconnSQL();
    }

    /**
     * 插入到数据库
     */
    private void insertDataBase() {
        //测试库连接
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        for (DomainCredit domainCredit:domainCredits) {
            String insertSQL="INSERT INTO domain_credit (DOMAIN_CODE,EXAM_ID,EXAM_YEAR,PASS_NUM,NO_PASS_NUM,NO_JOIN_NUM,TOTAL_NUM,PASS_RATE,AVG_SCORE,AVG_CREDIT_SCORE) VALUES(" +
                    domainCredit.getDOMAIN_CODE() + "," +
                    domainCredit.getEXAM_ID() + "," +
                    domainCredit.getEXAM_YEAR() + "," +
                    domainCredit.getPASS_NUM() + "," +
                    domainCredit.getNO_PASS_NUM() + "," +
                    domainCredit.getNO_JOIN_NUM() + "," +
                    domainCredit.getTOTAL_NUM() + "," +
                    domainCredit.getPASS_RATE() + "," +
                    domainCredit.getAVG_SCORE() + "," +
                    domainCredit.getAVG_CREDIT_SCORE() + ");";
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
        }
        testReport.deconnSQL();
    }
}
