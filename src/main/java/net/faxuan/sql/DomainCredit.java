package net.faxuan.sql;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/7/28.
 */
public class DomainCredit {
    private static String tableName = "domain_credit";
    private static String domain_credit = "CREATE TABLE `" + tableName+"` (\n" +
            "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `DOMAIN_CODE` varchar(20) NOT NULL COMMENT '单位编号',\n" +
            "  `EXAM_ID` bigint(20) NOT NULL COMMENT '考试ID',\n" +
            "  `EXAM_YEAR` varchar(4) NOT NULL COMMENT '考试所属年份',\n" +
            "  `PASS_NUM` int(5) NOT NULL COMMENT '及格人数',\n" +
            "  `NO_PASS_NUM` int(5) NOT NULL COMMENT '未及格人数',\n" +
            "  `NO_JOIN_NUM` int(5) NOT NULL COMMENT '未参加的人数',\n" +
            "  `TOTAL_NUM` int(5) NOT NULL COMMENT '总人数',\n" +
            "  `PASS_RATE` double(6,2) NOT NULL COMMENT '及格率',\n" +
            "  `AVG_SCORE` double(6,2) NOT NULL COMMENT '平均分',\n" +
            "  `AVG_CREDIT_SCORE` double(10,2) NOT NULL COMMENT '学分平均分',\n" +
            "  `EXT_1` int(5) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_2` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_3` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_4` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_5` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  KEY `index_report_domain_code` (`DOMAIN_CODE`),\n" +
            "  KEY `examId` (`EXAM_ID`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";

    public static Boolean create(DataBase dataBase) {
        Boolean b1 = dataBase.insertSQL("DROP TABLE IF EXISTS `" + tableName+"`;");
        Boolean b2 = dataBase.insertSQL(domain_credit);
        return b1 == true && b2 == true;
    }
}
