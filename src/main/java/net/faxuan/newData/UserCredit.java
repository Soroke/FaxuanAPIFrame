package net.faxuan.newData;

import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.FromSourceCreateTable;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Calendar;

/**
 * 统计用户的当年往年信息
 * Created by song on 2018/8/23.
 */
public class UserCredit {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    public UserCredit () {
        log.info("---------------------开始统计学员学分 当年和往年的信息START---------------------");
        //创建学分往年表
        FromSourceCreateTable.create(DataSource.SourceType.TREPORT,"user_credit","user_credit" + (yearInt -1));
        //统计学分往年和往年信息
        userCredit();
        log.info("----------------------学员学分 当年和往年的信息统计完毕END----------------------");
    }

    public void userCredit() {
        /**
         * 统计当年用户学分信息SQL
         */
        String credit = "INSERT INTO user_credit ( " +
                "USER_ACCOUNT," +
                "DOMAIN_CODE," +
                "RANK_ID," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "EXAM_SCORE," +
                "EXAM_STATUS," +
                "POINT_SCORE," +
                "CREDIT_SCORE," +
                "USER_NAME " +
                ") SELECT " +
                "t.USER_ACCOUNT," +
                "t.DOMAIN_CODE," +
                "t.RANK_ID," +
                "t.EXAM_ID," +
                "t.EXAM_YEAR," +
                "t.EXAM_SCORE," +
                "IF(t.EXAM_STATUS IS NULL,NULL,IF(ROUND(((t.EXAM_SCORE / e.EXAM_SCORE) * e.EXT2) + IF(t.POINT_SCORE>t.RULE_POINT,(100-e.EXT2),((t.POINT_SCORE / T.RULE_POINT) * (100 - E.EXT2))),2) >= e.EXAM_PASS_SCORE ,0,1)) AS EXAM_STATUS," +
                "t.POINT_SCORE," +
                "ROUND(((t.EXAM_SCORE / e.EXAM_SCORE) * e.EXT2) + IF(t.POINT_SCORE>t.RULE_POINT,(100-e.EXT2),((t.POINT_SCORE / T.RULE_POINT) * (100 - E.EXT2))),2) AS CREDIT_SCORE," +
                "t.USER_NAME " +
                "FROM " +
                "( " +
                "SELECT " +
                "e.USER_ACCOUNT," +
                "e.DOMAIN_CODE," +
                "e.RANK_ID," +
                "e.EXAM_ID," +
                "e.EXAM_YEAR," +
                "e.EXAM_SCORE," +
                "e.EXAM_STATUS," +
                "p.TPOINT AS POINT_SCORE," +
                "e.USER_NAME," +
                "p.EXT_1 AS RULE_POINT " +
                "FROM " +
                "user_exam e " +
                "LEFT JOIN user_point p ON e.USER_ACCOUNT = p.USER_ACCOUNT " +
                ") t " +
                "LEFT JOIN base_exam e ON t.EXAM_ID = e.ID";


        /**
         * 统计往年用户学分信息sql
         */
        String credit2017 = "INSERT INTO user_credit" + (yearInt -1) + " ( " +
                "USER_ACCOUNT," +
                "DOMAIN_CODE," +
                "RANK_ID," +
                "EXAM_ID," +
                "EXAM_YEAR," +
                "EXAM_SCORE," +
                "EXAM_STATUS," +
                "POINT_SCORE," +
                "CREDIT_SCORE," +
                "USER_NAME " +
                ") SELECT " +
                "t.USER_ACCOUNT," +
                "t.DOMAIN_CODE," +
                "t.RANK_ID," +
                "t.EXAM_ID," +
                "t.EXAM_YEAR," +
                "t.EXAM_SCORE," +
                "IF(t.EXAM_STATUS IS NULL,NULL,IF(ROUND(((t.EXAM_SCORE / e.EXAM_SCORE) * e.EXT2) + IF(t.POINT_SCORE>t.RULE_POINT,(100-e.EXT2),((t.POINT_SCORE / T.RULE_POINT) * (100 - E.EXT2))),2) >= e.EXAM_PASS_SCORE ,0,1)) AS EXAM_STATUS," +
                "t.POINT_SCORE," +
                "ROUND(((t.EXAM_SCORE / e.EXAM_SCORE) * e.EXT2) + IF(t.POINT_SCORE>t.RULE_POINT,(100-e.EXT2),((t.POINT_SCORE / T.RULE_POINT) * (100 - E.EXT2))),2) AS CREDIT_SCORE," +
                "t.USER_NAME " +
                "FROM " +
                "( " +
                "SELECT " +
                "e.USER_ACCOUNT," +
                "e.DOMAIN_CODE," +
                "e.RANK_ID," +
                "e.EXAM_ID," +
                "e.EXAM_YEAR," +
                "e.EXAM_SCORE," +
                "e.EXAM_STATUS," +
                "p.TPOINT AS POINT_SCORE," +
                "e.USER_NAME," +
                "p.EXT_1 AS RULE_POINT " +
                "FROM " +
                "user_exam" + (yearInt -1) + " e " +
                "LEFT JOIN user_point p ON e.USER_ACCOUNT = p.USER_ACCOUNT " +
                ") t " +
                "LEFT JOIN base_exam e ON t.EXAM_ID = e.ID";

        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(credit),true);
        Assert.assertEquals(testReport.insertSQL(credit2017),true);
        testReport.deconnSQL();
    }

}
