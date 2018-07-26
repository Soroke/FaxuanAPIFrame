package net.faxuan.util;

import net.faxuan.data.ParamInfo;
import net.faxuan.exception.CheckException;
import net.faxuan.tableProject.Exam;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/4.
 * 获取执行数据
 */
public class ConfirmExecutionData {


    /**
     * 获取补考的考试信息
     */
    public static List<Exam> getMakeUpexams() {

        /**
         * 数据库链接
         */
        DataBase xfExam = new DataBase(DataSource.SourceType.SOURSE3);

        List<Exam> exams = new ArrayList<Exam>();

        ParamInfo paramInfo = new ParamInfo();
        List<String> examIDs = paramInfo.getExamIds();

        String examids = "";
        for (String examID:examIDs) {
            examids += (examID + ",");
        }
        examids = examids.substring(0,examids.length()-1);
        /**
         * 获取所有考试的补考信息
         */
        ResultSet makeUpExamResultSet = xfExam.selectSQL("SELECT * FROM `exam_exam` WHERE TARGET_EXAM_ID IN(" + examids + ") AND EXAM_STATUS = 1 AND STR_TO_DATE(EXAM_END_TIME, '%Y-%m-%d') >= STR_TO_DATE( DATE_ADD(SYSDATE(), INTERVAL - 1 DAY), '%Y-%m-%d' ) AND STR_TO_DATE(EXAM_BEGIN_TIME, '%Y-%m-%d') <= STR_TO_DATE( DATE_ADD(SYSDATE(), INTERVAL - 1 DAY), '%Y-%m-%d' );");

        try {
            makeUpExamResultSet.last();
            if (makeUpExamResultSet.getRow() == 0) {
                return null;
            }
            makeUpExamResultSet.beforeFirst();
            while (makeUpExamResultSet.next()) {
                Exam exam = new Exam();
                exam.setID(makeUpExamResultSet.getString("ID"));
                exam.setTARGET_DOMAIN_CODE(makeUpExamResultSet.getString("TARGET_DOMAIN_CODE"));
                exam.setEXAM_YEAR(makeUpExamResultSet.getString("EXAM_YEAR"));
                exam.setEXAM_NAME(makeUpExamResultSet.getString("EXAM_NAME"));
                String examBeginTime = makeUpExamResultSet.getString("EXAM_BEGIN_TIME");
                exam.setEXAM_BEGIN_TIME(examBeginTime.substring(0,examBeginTime.length()-5));
                exam.setINDUSTRY_CODES(makeUpExamResultSet.getString("INDUSTRY_CODES"));
                exam.setRANK(makeUpExamResultSet.getString("RANK"));
                exam.setINDUSTRY_DOMAIN_FLAG(makeUpExamResultSet.getString("INDUSTRY_DOMAIN_FLAG"));
                exam.setEXAM_TYPE(makeUpExamResultSet.getString("EXAM_TYPE"));
                exam.setTARGET_EXAM_ID(makeUpExamResultSet.getString("TARGET_EXAM_ID"));
                exam.setIS_CREDIT(makeUpExamResultSet.getInt("EXT1"));
                exam.setPROPORTION(makeUpExamResultSet.getInt("EXT2"));
                exam.setEXAM_SCORE(makeUpExamResultSet.getDouble("EXAM_SCORE"));
                exams.add(exam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        xfExam.deconnSQL();
        return exams;
    }


    /**
     * 获取主考考试信息
     * @return 可以进行统计的考试ID
     */
    public static List<Exam> getExams() {

        /**
         * 数据库链接
         */
        DataBase xfExam = new DataBase(DataSource.SourceType.SOURSE3);
        /**
         * 主考考试信息储存
         */
        List<Exam> exams = new ArrayList<Exam>();

        ParamInfo paramInfo = new ParamInfo();
        List<String> examIDs = paramInfo.getExamIds();

        String examids = "";
        for (String examID:examIDs) {
            examids += (examID + ",");
        }
        examids = examids.substring(0,examids.length()-1);

        ResultSet resultSet = xfExam.selectSQL("SELECT * FROM exam_exam WHERE ID IN(" + examids + ") AND EXAM_STATUS = 1 AND EXAM_TYPE = 0 AND STR_TO_DATE(EXAM_END_TIME, '%Y-%m-%d') >= STR_TO_DATE( DATE_ADD(SYSDATE(), INTERVAL - 1 DAY), '%Y-%m-%d' ) AND STR_TO_DATE(EXAM_BEGIN_TIME, '%Y-%m-%d') <= STR_TO_DATE( DATE_ADD(SYSDATE(), INTERVAL - 1 DAY), '%Y-%m-%d' );");

        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("考试ID：" + examids + "或在库里不存在，或未发布，或不在考试时间内，或不是主考ID");
            }
            Assert.assertEquals(rowCount>0,true);
            while (resultSet.next()) {
                if (resultSet.getString("EXAM_STATUS").equals("1")) {
                    Exam exam = new Exam();
                    exam.setID(resultSet.getString("ID"));
                    exam.setTARGET_DOMAIN_CODE(resultSet.getString("TARGET_DOMAIN_CODE"));
                    exam.setEXAM_NAME(resultSet.getString("EXAM_NAME"));
                    exam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                    String examBeginTime = resultSet.getString("EXAM_BEGIN_TIME");
                    exam.setEXAM_BEGIN_TIME(examBeginTime.substring(0,examBeginTime.length()-5));
                    exam.setINDUSTRY_CODES(resultSet.getString("INDUSTRY_CODES"));
                    exam.setRANK(resultSet.getString("RANK"));
                    exam.setINDUSTRY_DOMAIN_FLAG(resultSet.getString("INDUSTRY_DOMAIN_FLAG"));
                    exam.setEXAM_TYPE(resultSet.getString("EXAM_TYPE"));
                    exam.setIS_CREDIT(resultSet.getInt("EXT1"));
                    exam.setPROPORTION(resultSet.getInt("EXT2"));
                    exam.setEXAM_SCORE(resultSet.getDouble("EXAM_SCORE"));
                    exams.add(exam);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        xfExam.deconnSQL();
        return exams;
    }


    /**
     * 获取考试统计的单位code
     */
    public static String getDomainCode() {
        String domainCodes = "";
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        ResultSet resultSet = testReport.selectSQL("SELECT * FROM `domain_exam` GROUP BY DOMAIN_CODE;");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                domainCodes += (resultSet.getString("DOMAIN_CODE") + ",");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        testReport.deconnSQL();
        if (domainCodes.length() == 0) {
            throw new CheckException("单位考试统计表为空,不进行用户积分统计");
        }
        domainCodes = domainCodes.substring(0,domainCodes.length()-1);
        testReport.deconnSQL();
        return domainCodes;
    }
}
