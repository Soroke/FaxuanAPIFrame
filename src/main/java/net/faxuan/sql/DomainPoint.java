package net.faxuan.sql;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/7/28.
 */
public class DomainPoint {
    private static String tableName = "domain_point";
    private static String domain_point = "CREATE TABLE `" + tableName + "` (\n" +
            "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `DOMAIN_CODE` varchar(20) NOT NULL COMMENT '单位编号',\n" +
            "  `LPOINT` int(6) DEFAULT NULL COMMENT '登陆积分',\n" +
            "  `IPOINT` int(6) DEFAULT NULL COMMENT '修改个人信息积分',\n" +
            "  `SPOINT` int(6) DEFAULT NULL COMMENT '学习积分',\n" +
            "  `EPOINT` int(6) DEFAULT NULL COMMENT '做练习题积分',\n" +
            "  `APOINT` int(6) DEFAULT NULL COMMENT '活动积分',\n" +
            "  `PPOINT` int(6) DEFAULT NULL COMMENT '门户其它积分',\n" +
            "  `TPOINT` int(10) DEFAULT NULL COMMENT '单位总积分',\n" +
            "  `EXT_1` int(11) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_2` int(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_3` int(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_4` int(1) DEFAULT NULL COMMENT '预留',\n" +
            "  `EXT_5` int(1) DEFAULT NULL COMMENT '预留',\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  KEY `index_report_domain_code` (`DOMAIN_CODE`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\n";
    public static Boolean create(DataBase dataBase) {
        Boolean b1 = dataBase.insertSQL("DROP TABLE IF EXISTS `" + tableName + "`;");
        Boolean b2 = dataBase.insertSQL(domain_point);
        return b1 == true && b2 == true;
    }
}
