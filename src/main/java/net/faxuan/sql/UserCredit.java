package net.faxuan.sql;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/7/28.
 */
public class UserCredit {
    private static String tableName = "user_credit";
    private static String user_credit = "CREATE TABLE `" + tableName + "` (\n" +
            "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `USER_ACCOUNT` varchar(20) NOT NULL COMMENT '会员账号',\n" +
            "  `DOMAIN_CODE` varchar(20) NOT NULL COMMENT '所属单位编号',\n" +
            "  `RANK_ID` varchar(50) DEFAULT NULL COMMENT '职务级别',\n" +
            "  `EXAM_ID` bigint(20) NOT NULL COMMENT '考试ID',\n" +
            "  `EXAM_YEAR` varchar(5) DEFAULT NULL COMMENT '考试所属年份',\n" +
            "  `EXAM_SCORE` double(5,2) DEFAULT NULL COMMENT '考试分数',\n" +
            "  `EXAM_STATUS` int(5) DEFAULT NULL COMMENT '考试状态',\n" +
            "  `EXT_1` int(11) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_2` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_3` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_4` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_5` varchar(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `POINT_SCORE` int(7) DEFAULT '0' COMMENT '积分',\n" +
            "  `CREDIT_SCORE` double(6,2) DEFAULT '0.00' COMMENT '学分',\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  KEY `index_credit_report_domain_code` (`DOMAIN_CODE`),\n" +
            "  KEY `index_credit_report_user_account` (`USER_ACCOUNT`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
    public static Boolean create(DataBase dataBase) {
        Boolean b1 = dataBase.insertSQL("DROP TABLE IF EXISTS `" + tableName + "`;");
        Boolean b2 = dataBase.insertSQL(user_credit);
        return b1 == true && b2 == true;
    }
}
