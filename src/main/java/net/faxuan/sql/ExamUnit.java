package net.faxuan.sql;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/7/28.
 */
public class ExamUnit {
    private static String tableName = "exam_unit";
    private static String exam_unit = "CREATE TABLE `" + tableName + "` (\n" +
            "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
            "  `DOMAIN_CODE` varchar(20) NOT NULL DEFAULT '' COMMENT '单位CODE',\n" +
            "  `EXAM_ID` bigint(20) NOT NULL COMMENT '考试ID',\n" +
            "  `EXAM_YEAR` varchar(4) NOT NULL DEFAULT '' COMMENT '考试年份',\n" +
            "  `EXAM_DOMAIN_NUM` int(5) NOT NULL COMMENT '参考单位数量',\n" +
            "  `TOTAL_NUM` int(5) NOT NULL COMMENT '总人数',\n" +
            "  `EXAM_TOTAL_NUM` int(5) NOT NULL COMMENT '参考人数',\n" +
            "  `PASS_NUM` int(5) NOT NULL COMMENT '及格人数',\n" +
            "  `NO_PASS_NUM` int(5) NOT NULL COMMENT '未及格人数',\n" +
            "  `TOTAL_SCORE` double(10,2) NOT NULL COMMENT '总分',\n" +
            "  `EXT_1` int(5) DEFAULT NULL,\n" +
            "  `EXT_2` varchar(1) DEFAULT NULL,\n" +
            "  `EXT_3` varchar(1) DEFAULT NULL,\n" +
            "  `EXT_4` varchar(1) DEFAULT NULL,\n" +
            "  `EXT_5` varchar(1) DEFAULT NULL,\n" +
            "  `REFERENCE_DOMAIN_NUM` int(5) DEFAULT NULL COMMENT '应参考单位数',\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  KEY `index_report_domain_code` (`DOMAIN_CODE`),\n" +
            "  KEY `examId` (`EXAM_ID`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
    public static Boolean create(DataBase dataBase) {
        Boolean b1 = dataBase.insertSQL("DROP TABLE IF EXISTS `" + tableName + "`;");
        Boolean b2 = dataBase.insertSQL(exam_unit);
        return b1 == true && b2 == true;
    }
}
