package net.faxuan.newData;

import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.FromSourceCreateTable;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Calendar;

/**
 * 统计当年、往年单位学分信息
 * Created by song on 2018/8/24.
 */
public class DomainCredit {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    public DomainCredit() {
        log.info("---------------------开始统计单位学分信息START---------------------");
        //创建单位学分往年表
        FromSourceCreateTable.create(DataSource.SourceType.TREPORT,"domain_credit","domain_credit" + (yearInt -1));
        getDomainCredit();
        log.info("----------------------统计单位学分信息完毕END----------------------");
    }

    private void getDomainCredit() {
        String domainCredit = "INSERT INTO domain_credit ( " +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "PASS_NUM," +
                "NO_PASS_NUM," +
                "NO_JOIN_NUM," +
                "TOTAL_NUM," +
                "PASS_RATE," +
                "AVG_SCORE," +
                "AVG_CREDIT_SCORE " +
                ") SELECT " +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "IFNULL(SUM(EXAM_STATUS = 0), 0) AS PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS = 1), 0) AS NO_PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS IS NULL), 0) AS NO_JOIN_NUM," +
                "IFNULL(COUNT(id), 0) AS TOTAL_NUM," +
                "IFNULL(ROUND(((sum(EXAM_STATUS = 0) / COUNT(id)) * 100) ,2),0 ) AS PASS_RATE," +
                "IFNULL(ROUND(CAST(AVG(EXAM_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_SCORE," +
                "IFNULL(ROUND(CAST(AVG(CREDIT_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_CREDIT_SCORE " +
                "FROM " +
                "user_credit " +
                "GROUP BY " +
                "EXAM_YEAR," +
                "EXAM_ID," +
                "DOMAIN_CODE;";

        String domainCredit2017 = "INSERT INTO domain_credit" + (yearInt-1) + " ( " +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "PASS_NUM," +
                "NO_PASS_NUM," +
                "NO_JOIN_NUM," +
                "TOTAL_NUM," +
                "PASS_RATE," +
                "AVG_SCORE," +
                "AVG_CREDIT_SCORE " +
                ") SELECT " +
                "DOMAIN_CODE," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "IFNULL(SUM(EXAM_STATUS = 0), 0) AS PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS = 1), 0) AS NO_PASS_NUM," +
                "IFNULL(SUM(EXAM_STATUS IS NULL), 0) AS NO_JOIN_NUM," +
                "IFNULL(COUNT(id), 0) AS TOTAL_NUM," +
                "IFNULL(ROUND(((sum(EXAM_STATUS = 0) / COUNT(id)) * 100) ,2),0 ) AS PASS_RATE," +
                "IFNULL(ROUND(CAST(AVG(EXAM_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_SCORE," +
                "IFNULL(ROUND(CAST(AVG(CREDIT_SCORE) AS DECIMAL (15, 6)),2),0) AS AVG_CREDIT_SCORE " +
                "FROM " +
                "user_credit" + (yearInt-1) +
                " GROUP BY " +
                "EXAM_YEAR," +
                "EXAM_ID," +
                "DOMAIN_CODE;";

        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(domainCredit),true);
        Assert.assertEquals(testReport.insertSQL(domainCredit2017),true);
        testReport.deconnSQL();
    }
}
