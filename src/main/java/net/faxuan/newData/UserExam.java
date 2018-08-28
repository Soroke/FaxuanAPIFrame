package net.faxuan.newData;

import net.faxuan.exception.CheckException;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 统计用户考试的当年、往年信息
 * Created by song on 2018/8/21.
 */
public class UserExam {

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    public UserExam() {
        log.info("---------------------开始统计学员考试 当年和往年的信息START---------------------");
        getUserExamResult();
        log.info("----------------------学员考试 当年和往年的信息统计完毕END----------------------");
    }

    /**
     * 查询临时表获取用户考试统计表数据，并插入到数据库中
     */
    private void getUserExamResult() {
        Map<String,String> examInfo = getMastMakeupExamInfo();

        for (String key:examInfo.keySet()) {
            String examIds = "";
            if (examInfo.get(key).equals("") || examInfo.get(key) == "") {
                examIds = key;
            } else {
                examIds = key + "," + examInfo.get(key);
            }
            DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
            String exam2018 = "INSERT INTO user_exam (" +
                    " USER_ACCOUNT," +
                    "DOMAIN_CODE," +
                    "RANK_ID," +
                    "EXAM_ID," +
                    "EXAM_YEAR," +
                    "EXAM_SCORE," +
                    "EXAM_STATUS," +
                    "EXAM_NAME," +
                    "EXAM_BEGIN_TIME," +
                    "USER_NAME" +
                    ") SELECT" +
                    " u.USER_ACCOUNT," +
                    "u.DOMAIN_CODE," +
                    "u.RANK_ID," +
                    "u.EXAM_ID," +
                    "b.EXAM_YEAR," +
                    "u.EXAM_SCORE," +
                    "u.EXAM_STATUS," +
                    "b.EXAM_NAME," +
                    "b.EXAM_BEGIN_TIME," +
                    "u.USER_NAME" +
                    " FROM " +
                    "(" +
                    " SELECT " +
                    "u.USER_ACCOUNT," +
                    "u.DOMAIN_CODE," +
                    "u.RANK_ID," +
                    "u.EXT_5 AS EXAM_ID," +
                    "MAX(" +
                    "IFNULL(r.EXAM_RESULT_SCORE, 0)" +
                    ") AS EXAM_SCORE," +
                    "MIN(IFNULL(r.IS_PASS, NULL)) AS EXAM_STATUS," +
                    "u.USER_NAME" +
                    " FROM " +
                    "base_user u" +
                    " LEFT JOIN base_user_exam_result r ON r.USER_ACCOUNT = u.USER_ACCOUNT" +
                    " WHERE " +
                    "u.EXT_5 IN (" + examIds + ")" +
                    " GROUP BY " +
                    "USER_ACCOUNT" +
                    ") u" +
                    " LEFT JOIN base_exam b ON u.EXAM_ID = b.ID WHERE b.EXAM_YEAR >= 2018;";



            String exam2017 = "INSERT INTO user_exam" + (yearInt-1) + " (" +
                    " USER_ACCOUNT," +
                    "DOMAIN_CODE," +
                    "RANK_ID," +
                    "EXAM_ID," +
                    "EXAM_YEAR," +
                    "EXAM_SCORE," +
                    "EXAM_STATUS," +
                    "EXAM_NAME," +
                    "EXAM_BEGIN_TIME," +
                    "USER_NAME" +
                    ") SELECT" +
                    " u.USER_ACCOUNT," +
                    "u.DOMAIN_CODE," +
                    "u.RANK_ID," +
                    "u.EXAM_ID," +
                    "b.EXAM_YEAR," +
                    "u.EXAM_SCORE," +
                    "u.EXAM_STATUS," +
                    "b.EXAM_NAME," +
                    "b.EXAM_BEGIN_TIME," +
                    "u.USER_NAME" +
                    " FROM " +
                    "(" +
                    " SELECT " +
                    "u.USER_ACCOUNT," +
                    "u.DOMAIN_CODE," +
                    "u.RANK_ID," +
                    "u.EXT_5 AS EXAM_ID," +
                    "MAX(" +
                    "IFNULL(r.EXAM_RESULT_SCORE, 0)" +
                    ") AS EXAM_SCORE," +
                    "MIN(IFNULL(r.IS_PASS, NULL)) AS EXAM_STATUS," +
                    "u.USER_NAME" +
                    " FROM " +
                    "base_user u" +
                    " LEFT JOIN base_user_exam_result r ON r.USER_ACCOUNT = u.USER_ACCOUNT" +
                    " WHERE " +
                    "u.EXT_5 IN (" + examIds + ")" +
                    " GROUP BY " +
                    "USER_ACCOUNT" +
                    ") u" +
                    " LEFT JOIN base_exam b ON u.EXAM_ID = b.ID WHERE b.EXAM_YEAR < 2018;";

            Assert.assertEquals(testReport.insertSQL(exam2018),true);
            testReport.deconnSQL();
            testReport = new DataBase(DataSource.SourceType.TREPORT);
            Assert.assertEquals(testReport.insertSQL(exam2017),true);
            testReport.deconnSQL();
        }
    }

    /**
     * 获取测试report库考试临时表中，主考和补考的对应ID
     * @return map<主考ID,补考ID>
     */
    private Map<String,String> getMastMakeupExamInfo() {
        Map<String,String> examInfo =  new HashMap<String, String>();
        List<String> examInfos = new ArrayList<String>();
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);

        ResultSet resultSet = testReport.selectSQL("SELECT ID,TARGET_EXAM_ID,EXAM_TYPE,EXAM_YEAR FROM `base_exam`;");
        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("临时表base_exam中没有主考信息，统计结束");
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                String exam = "";
                exam = exam + resultSet.getString("ID") + "," + resultSet.getString("EXAM_TYPE") + "," + resultSet.getString("TARGET_EXAM_ID") + "," + resultSet.getString("EXAM_YEAR");
                examInfos.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String exam:examInfos) {
            String[] info = exam.split(",");
            String id = info[0];
            if (info[1].equals("0")) {
                String makeupExam = "";
                for (String exam1:examInfos) {
                    String[] info1 = exam1.split(",");
                    if (id.equals(info1[2])) {
                        makeupExam = makeupExam + info1[0] + ",";
                    }
                }
                if (makeupExam != "" || !makeupExam.equals("")) {
                    makeupExam = makeupExam.substring(0,makeupExam.length()-1);
                    examInfo.put(id,makeupExam);
                } else {
                    examInfo.put(id,"");
                }
            }


        }
        return examInfo;
    }



}
