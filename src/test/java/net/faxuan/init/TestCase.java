package net.faxuan.init;

import io.qameta.allure.Description;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by song on 2017/8/3.
 */
public class TestCase {

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    //@BeforeSuite(description = "连接数据库；获取数据库表信息然后创建测试report库中的表")
    public void createTable(List<String> tableNames) {
        DataBase xfReport = new DataBase(DataSource.SourceType.SOURSE4);
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);


        for(String tableName:tableNames) {
            ResultSet rs = xfReport.selectSQL("select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + tableName + "';");
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

            String createTableSQL_copy = createTableSQL;
            String newTableName = "";
            String newTableName1 = "";
            if (tableName.contains("exam")) {
                if (tableName.contains("user")) {
                    newTableName = "user_exam";
                    newTableName1="user_exam" + (yearInt-1);
                    createTableSQL = createTableSQL.replace("tableName_placeholder",newTableName);
                    createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                } else if (tableName.contains("domain_unit")) {
                    newTableName = "unit_exam";
                    createTableSQL = createTableSQL.replaceAll(tbn,newTableName);
                } else {
                    newTableName = "domain_exam";
                    newTableName1="domain_exam" + (yearInt-1);
                    createTableSQL = createTableSQL.replace(tbn,newTableName);
                    createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                }
            } else if (tableName.contains("point")) {
                if (tableName.contains("user")) {
                    newTableName = "user_point";
//newTableName1="user_point2016";
                    createTableSQL = createTableSQL.replace(tbn,newTableName);
//createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                } else{
                    newTableName = "domain_point";
//newTableName1="domain_point2016";
                    createTableSQL = createTableSQL.replace(tbn,newTableName);
//createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                }
            } else {
                if (tableName.contains("user")) {
                    newTableName = "user_credit";
//newTableName1="user_credit2016";
                    createTableSQL = createTableSQL.replace(tbn,newTableName);
//createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                } else{
                    newTableName = "domain_credit";
//newTableName1="domain_credit2016";
                    createTableSQL = createTableSQL.replace(tbn,newTableName);
//createTableSQL_copy = createTableSQL_copy.replace(tbn,newTableName1);
                }
            }

            Assert.assertEquals(testReport.insertSQL("DROP TABLE IF EXISTS `" + newTableName + "`;"),true);
            Assert.assertEquals(testReport.insertSQL(createTableSQL),true);
            if(newTableName1 !="" || !newTableName1.equals("")) {
                Assert.assertEquals(testReport.insertSQL("DROP TABLE IF EXISTS `" + newTableName1 + "`;"),true);
                Assert.assertEquals(testReport.insertSQL(createTableSQL_copy),true);
            }

        }
        xfReport.deconnSQL();
        testReport.deconnSQL();
    }
}
