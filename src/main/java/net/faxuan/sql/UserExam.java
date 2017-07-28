package net.faxuan.sql;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/7/28.
 */
public class UserExam {
    private static String tableName = "user_exam";
    private static String user_exam = "CREATE TABLE `" + tableName + "` (\n" +
            "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `USER_ACCOUNT` varchar(20) NOT NULL COMMENT '会员账号',\n" +
            "  `DOMAIN_CODE` varchar(20) NOT NULL COMMENT '所属单位编号',\n" +
            "  `RANK_ID` varchar(50) DEFAULT NULL COMMENT '职务级别/政治面貌',\n" +
            "  `EXAM_ID` bigint(20) NOT NULL COMMENT '考试ID',\n" +
            "  `EXAM_YEAR` varchar(5) DEFAULT NULL COMMENT '考试所属年份',\n" +
            "  `EXAM_SCORE` double(5,2) DEFAULT NULL COMMENT '考试分数',\n" +
            "  `EXAM_STATUS` int(5) DEFAULT NULL COMMENT '考试状态(0及格1不及格。空缺考)',\n" +
            "  `EXT_1` int(11) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_2` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_3` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_4` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_5` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXAM_NAME` varchar(300) DEFAULT NULL COMMENT '考试名称',\n" +
            "  `EXAM_BEGIN_TIME` varchar(50) DEFAULT NULL COMMENT '考试开始时间',\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  KEY `index_report_domain_code` (`DOMAIN_CODE`),\n" +
            "  KEY `index_report_user_account` (`USER_ACCOUNT`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
    public static Boolean create(DataBase dataBase) {
        Boolean b1 = dataBase.insertSQL("DROP TABLE IF EXISTS `" + tableName + "`;");
        Boolean b2 = dataBase.insertSQL(user_exam);
        return b1 == true && b2 == true;
    }
}
