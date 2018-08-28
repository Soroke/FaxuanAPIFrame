package net.faxuan.newData;

import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Calendar;

/**
 * 统计当年、往年单位考试信息
 * Created by song on 2018/8/22.
 */
public class DomainExam {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    public DomainExam() {
        log.info("---------------------开始统计单位考试信息START---------------------");
        insertDomainExam();
        log.info("----------------------统计单位考试信息完毕END----------------------");
    }

    /**
     * 统计并插入数据
     */
    private void insertDomainExam() {
        String domainExam2018 = "INSERT INTO domain_exam (" +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "PASS_NUM," +
                "NO_PASS_NUM," +
                "NO_JOIN_NUM," +
                "TOTAL_NUM," +
                "PASS_RATE," +
                "AVG_SCORE," +
                "EXT_1" +
                ") SELECT" +
                " DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "IFNULL(SUM(EXAM_STATUS = 0), 0) AS PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS = 1), 0) AS NO_PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS IS NULL), 0) AS NO_JOIN_NUM," +
                "IFNULL(COUNT(id), 0) AS TOTAL_NUM," +
                "IFNULL((sum(EXAM_STATUS = 0) / COUNT(id)) * 100,0) AS PASS_RATE," +
                "IFNULL(ROUND(CAST(AVG(EXAM_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_SCORE," +
                "SUM(EXAM_SCORE) AS EXT_1" +
                " FROM " +
                "user_exam" +
                " GROUP BY " +
                "EXAM_YEAR," +
                "EXAM_ID," +
                "DOMAIN_CODE;";

        String domainExam2017 = "INSERT INTO domain_exam"+ (yearInt-1) + " (" +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "PASS_NUM," +
                "NO_PASS_NUM," +
                "NO_JOIN_NUM," +
                "TOTAL_NUM," +
                "PASS_RATE," +
                "AVG_SCORE" +
                ") SELECT" +
                " DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "IFNULL(SUM(EXAM_STATUS = 0), 0) AS PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS = 1), 0) AS NO_PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS IS NULL), 0) AS NO_JOIN_NUM," +
                "IFNULL(COUNT(id), 0) AS TOTAL_NUM," +
                "IFNULL((sum(EXAM_STATUS = 0) / COUNT(id)) * 100,0) AS PASS_RATE," +
                "IFNULL(ROUND(CAST(AVG(EXAM_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_SCORE" +
                " FROM " +
                "user_exam2017" +
                " GROUP BY " +
                "EXAM_YEAR," +
                "EXAM_ID," +
                "DOMAIN_CODE;";

        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(domainExam2018),true);
        testReport.deconnSQL();
        testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(domainExam2017),true);
        testReport.deconnSQL();
    }
}
