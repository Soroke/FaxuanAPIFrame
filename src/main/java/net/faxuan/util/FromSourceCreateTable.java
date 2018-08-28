package net.faxuan.util;

import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by song on 2018/8/24.
 */
public class FromSourceCreateTable {
    /**
     * 根据传入学法report库中源表名获取表字段，组装建表SQL并在测试report库汇总创建表
     * @param sourceTableName report库中表名称
     * @param newTableName  测试库中表名称
     * @param sourceType  数据源
     */
    public static void create(DataSource.SourceType sourceType ,String sourceTableName, String newTableName) {
        DataBase sourceDataBase = new DataBase(sourceType);
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        String sql="";
        switch (sourceType) {
            case XFBASE:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='base';";
                break;
            case XFEXAM:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='exam';";
                break;
            case XFREPORT:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='report';";
                break;
            default:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='qa_report';";
        }

        ResultSet rs = sourceDataBase.selectSQL(sql);
        String key = "";
        String tbn = "tableName_placeholder";
        String createTableSQL = "CREATE TABLE `" + tbn + "` (";

        try {
            while (rs.next()) {
                String column = rs.getString("COLUMN_NAME");
                String type = rs.getString("COLUMN_TYPE");
                String isnull = rs.getString("IS_NULLABLE");
                String comment = rs.getString("COLUMN_COMMENT");
                String columnKey = rs.getString("COLUMN_KEY");
                if (columnKey.equals("PRI")) {
                    key = key + "PRIMARY KEY (`" + column + "`)";
                }

                String colum = "`" + column + "` ";
                createTableSQL = createTableSQL + " " + colum + type +" ";
                if (isnull.equals("NO")) {
                    createTableSQL += "NOT NULL ";
                } else if(type.equals("timestamp") || type.equals("TIMESTAMP")){
                    createTableSQL += "NULL DEFAULT NULL ";
                } else {
                    createTableSQL += "DEFAULT NULL ";
                }
                if (column.equals("ID")) {
                    createTableSQL += "AUTO_INCREMENT ";
                }
                createTableSQL += "COMMENT '" + comment + "',";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTableSQL = createTableSQL + key + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
        //替换SQL中表名称为用户执行表名称
        createTableSQL = createTableSQL.replace("tableName_placeholder",newTableName);

        //如果表已存在，先删除表然后再创建
        Assert.assertEquals(testReport.insertSQL("DROP TABLE IF EXISTS `" + newTableName + "`;"),true);
        Assert.assertEquals(testReport.insertSQL(createTableSQL),true);

        //关闭数据库链接
        sourceDataBase.deconnSQL();
        testReport.deconnSQL();
    }
}
