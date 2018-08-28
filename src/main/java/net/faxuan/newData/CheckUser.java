package net.faxuan.newData;

import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Calendar;

/**
 * 统计学员考核表
 * Created by song on 2018/8/24.
 */
public class CheckUser {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    private Logger log = Logger.getLogger(this.getClass());

    public CheckUser(String examIds) {
        CheckUser(examIds);
    }


    private void CheckUser(String examIds) {
        String sql1 = "INSERT INTO user_check ( USER_ACCOUNT, DOMAIN_CODE, USER_NAME, EXAM_ID, EXAM_SCORE, TPOINT, MPOINT ) " +
                "SELECT e.USER_ACCOUNT AS USER_ACCOUNT, e.DOMAIN_CODE AS DOMAIN_CODE, e.USER_NAME AS USER_NAME, e.EXAM_ID AS EXAM_ID," +
                " e.EXAM_SCORE AS EXAM_SCORE, p.TPOINT AS TPOINT, p.EXT_1 AS MPOINT FROM user_exam e LEFT JOIN user_point p" +
                " ON e.USER_ACCOUNT = p.USER_ACCOUNT WHERE e.EXAM_ID IN (" + examIds + ");";

        String sql2 = "INSERT INTO user_check ( USER_ACCOUNT, DOMAIN_CODE, USER_NAME, EXAM_ID, EXAM_SCORE, TPOINT, MPOINT ) " +
                "SELECT e.USER_ACCOUNT AS USER_ACCOUNT, e.DOMAIN_CODE AS DOMAIN_CODE, e.USER_NAME AS USER_NAME, e.EXAM_ID AS EXAM_ID," +
                " e.EXAM_SCORE AS EXAM_SCORE, p.TPOINT AS TPOINT, p.EXT_1 AS MPOINT FROM user_exam" + (yearInt -1) + " e LEFT JOIN user_point p" +
                " ON e.USER_ACCOUNT = p.USER_ACCOUNT WHERE e.EXAM_ID IN (" + examIds + ");";
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(sql1),true);
        Assert.assertEquals(testReport.insertSQL(sql2),true);
        testReport.deconnSQL();
    }

//
//    @Test
//    public void createTable() {
//        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
//        String sql1 = "DROP TABLE IF EXISTS `user_check`;";
//        String sql2 = "CREATE TABLE `user_check` (\n" +
//                "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
//                "  `USER_ACCOUNT` varchar(20) NOT NULL COMMENT '学员账号',\n" +
//                "  `DOMAIN_CODE` varchar(20) DEFAULT NULL COMMENT '单位编码',\n" +
//                "  `USER_NAME` varchar(50) NOT NULL COMMENT ' 用户姓名 ',\n" +
//                "  `EXAM_ID` bigint(20) DEFAULT NULL COMMENT '考试id',\n" +
//                "  `EXAM_SCORE` double(5,0) DEFAULT NULL COMMENT '考试分数',\n" +
//                "  `TPOINT` int(7) DEFAULT NULL COMMENT '总积分',\n" +
//                "  `MPOINT` int(11) DEFAULT NULL COMMENT '最大积分',\n" +
//                "  PRIMARY KEY (`ID`),\n" +
//                "  KEY `USER_ACCOUNT` (`USER_ACCOUNT`) USING BTREE,\n" +
//                "  KEY `EXAM_ID` (`EXAM_ID`) USING BTREE,\n" +
//                "  KEY `DOMAIN_CODE` (`DOMAIN_CODE`) USING BTREE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//
//        Assert.assertEquals(testReport.insertSQL(sql1),true);
//        Assert.assertEquals(testReport.insertSQL(sql2),true);
//        testReport.deconnSQL();
//    }
}
