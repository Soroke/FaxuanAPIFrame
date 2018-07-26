package net.faxuan.data;

import net.faxuan.tableProject.DomainExam;
import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UserExam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by song on 2017/8/2.
 * 单位考试统计，根据用户考试统计表
 */
public class ExamDomain {

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    /**
     * 储存获取的考试信息
     */
    List<Exam> exams = new ArrayList<Exam>();


    /**
     * 所有待统计的单位编码
     */
    List<String> domainCodes = new ArrayList<String>();

    /**
     * 所有待统计的往年单位编码
     */
    List<String> previousDomainCodes = new ArrayList<String>();

    /**
     * 数据库连接
     */
    private DataBase testReport;

    /**
     * 构造方法
     */
    public ExamDomain() {
        this.testReport = new DataBase(DataSource.SourceType.SOURSE1);
        exams = ConfirmExecutionData.getExams();
        initDomainCode();
        insert();
        testReport.deconnSQL();
    }

    /**
     * 初始化单位编码信息
     */
    private void initDomainCode(){
        /**
         * 当年单位编码信息
         */
        ResultSet resultSet = testReport.selectSQL("SELECT DOMAIN_CODE FROM user_exam GROUP BY DOMAIN_CODE;");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                domainCodes.add(resultSet.getString("DOMAIN_CODE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * 往年单位编码
         */
        ResultSet previousResultSet = testReport.selectSQL("SELECT DOMAIN_CODE FROM user_exam" + (yearInt-1) + " GROUP BY DOMAIN_CODE;");
        try {
            previousResultSet.last();
            if (previousResultSet.getRow() == 0) {
                return;
            }
            previousResultSet.beforeFirst();

            while (previousResultSet.next()) {
                previousDomainCodes.add(previousResultSet.getString("DOMAIN_CODE"));
            }
        } catch (SQLException ae) {
            ae.printStackTrace();
        }

    }


    public void insert() {
        List<DomainExam> thenYearDomainExam = getDomainExamInfo();
        List<DomainExam> passYearDomainExam = getPassYearDomainExamInfo();
        if (thenYearDomainExam != null) {
            insertDatabase(thenYearDomainExam,true);
        }
        if (passYearDomainExam != null) {
            insertDatabase(passYearDomainExam,false);
        }
    }


    /**
     * 当年单位考试信息
     * @return
     */
    private List<DomainExam> getDomainExamInfo() {
        List<DomainExam> domainExams = new ArrayList<DomainExam>();
        for (Exam exam:exams) {
            for (String domainCode:domainCodes) {
                ResultSet resultSet = testReport.selectSQL("SELECT CAST(AVG(EXAM_SCORE) AS DECIMAL(38,2)) AVG_SCORE, COUNT(*) TOTAL_NUM, SUM(EXAM_STATUS IS NULL) NO_JOIN_NUM, SUM(EXAM_STATUS = 0) PASS_NUM, SUM(EXAM_STATUS = 1) NO_PASS_NUM, SUM(EXAM_STATUS = 0) / COUNT(*) PASS_RATE FROM user_exam WHERE EXAM_ID = '"+ exam.getID() +"' AND DOMAIN_CODE = '" + domainCode + "';");
                try {
                    resultSet.last();
                    if (resultSet.getRow() == 0) {
                        return null;
                    }
                    resultSet.beforeFirst();
                    while (resultSet.next()){
                        if (resultSet.getInt("TOTAL_NUM") == 0) {
                            break;
                        } else {
                            DomainExam domainExam = new DomainExam();
                            domainExam.setAVG_SCORE(resultSet.getDouble("AVG_SCORE"));
                            domainExam.setEXAM_ID(exam.getID());
                            domainExam.setEXAM_YEAR(exam.getEXAM_YEAR());
                            domainExam.setDOMAIN_CODE(domainCode);
                            domainExam.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                            domainExam.setNO_JOIN_NUM(resultSet.getInt("NO_JOIN_NUM"));
                            domainExam.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                            domainExam.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                            domainExam.setPASS_RATE(resultSet.getDouble("PASS_RATE")*100);
                            domainExams.add(domainExam);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return domainExams;
    }

    /**
     * 往年单位考试信息
     * @return
     */
    private List<DomainExam> getPassYearDomainExamInfo() {
        List<DomainExam> domainExams = new ArrayList<DomainExam>();
        for (Exam exam:exams) {
            for (String domainCode:previousDomainCodes) {
                ResultSet resultSet = testReport.selectSQL("SELECT CAST(AVG(EXAM_SCORE) AS DECIMAL(38,2)) AVG_SCORE, COUNT(*) TOTAL_NUM, SUM(EXAM_STATUS IS NULL) NO_JOIN_NUM, SUM(EXAM_STATUS = 0) PASS_NUM, SUM(EXAM_STATUS = 1) NO_PASS_NUM, SUM(EXAM_STATUS = 0) / COUNT(*) PASS_RATE FROM user_exam" + (yearInt-1) + " WHERE EXAM_ID = '"+ exam.getID() +"' AND DOMAIN_CODE = '" + domainCode + "';");
                try {
                    resultSet.last();
                    if (resultSet.getRow() == 0) {
                        return null;
                    }
                    resultSet.beforeFirst();
                    while (resultSet.next()){
                        if (resultSet.getInt("TOTAL_NUM") == 0) {
                            break;
                        } else {
                            DomainExam domainExam = new DomainExam();
                            domainExam.setAVG_SCORE(resultSet.getDouble("AVG_SCORE"));
                            domainExam.setEXAM_ID(exam.getID());
                            domainExam.setEXAM_YEAR(exam.getEXAM_YEAR());
                            domainExam.setDOMAIN_CODE(domainCode);
                            domainExam.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                            domainExam.setNO_JOIN_NUM(resultSet.getInt("NO_JOIN_NUM"));
                            domainExam.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                            domainExam.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                            domainExam.setPASS_RATE(resultSet.getDouble("PASS_RATE")*100);
                            domainExams.add(domainExam);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return domainExams;
    }

    /**
     * 插入数据库
     * @param domainExams
     */
    private void insertDatabase(List<DomainExam> domainExams,boolean thenYear) {
        if (thenYear) {
            for (DomainExam domainExam:domainExams) {
                String insertSQL = "INSERT INTO domain_exam(DOMAIN_CODE,EXAM_ID,EXAM_YEAR,PASS_NUM,NO_PASS_NUM,NO_JOIN_NUM,TOTAL_NUM,PASS_RATE,AVG_SCORE) VALUES(";
                insertSQL += ("'" + domainExam.getDOMAIN_CODE() + "',");
                insertSQL += ("'" + domainExam.getEXAM_ID() + "',");
                insertSQL += ("'" + domainExam.getEXAM_YEAR() + "',");
                insertSQL += ("'" + domainExam.getPASS_NUM() + "',");
                insertSQL += ("'" + domainExam.getNO_PASS_NUM() + "',");
                insertSQL += ("'" + domainExam.getNO_JOIN_NUM() + "',");
                insertSQL += ("'" + domainExam.getTOTAL_NUM() + "',");
                insertSQL += ("'" + domainExam.getPASS_RATE() + "',");
                insertSQL += ("'" + domainExam.getAVG_SCORE() + "');");
                Assert.assertEquals(testReport.insertSQL(insertSQL),true);
            }
        } else {
            for (DomainExam domainExam:domainExams) {
                String insertSQL = "INSERT INTO domain_exam" + (yearInt-1) + "(DOMAIN_CODE,EXAM_ID,EXAM_YEAR,PASS_NUM,NO_PASS_NUM,NO_JOIN_NUM,TOTAL_NUM,PASS_RATE,AVG_SCORE) VALUES(";
                insertSQL += ("'" + domainExam.getDOMAIN_CODE() + "',");
                insertSQL += ("'" + domainExam.getEXAM_ID() + "',");
                insertSQL += ("'" + domainExam.getEXAM_YEAR() + "',");
                insertSQL += ("'" + domainExam.getPASS_NUM() + "',");
                insertSQL += ("'" + domainExam.getNO_PASS_NUM() + "',");
                insertSQL += ("'" + domainExam.getNO_JOIN_NUM() + "',");
                insertSQL += ("'" + domainExam.getTOTAL_NUM() + "',");
                insertSQL += ("'" + domainExam.getPASS_RATE() + "',");
                insertSQL += ("'" + domainExam.getAVG_SCORE() + "');");
                Assert.assertEquals(testReport.insertSQL(insertSQL),true);
            }
        }

    }

}
