package net.faxuan.newData;

import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * 统计单位积分信息
 * 用户积分在获取临时表数据时已经统计完成
 * Created by song on 2018/8/22.
 */
public class Point {
    private Logger log = Logger.getLogger(this.getClass());

    public Point() {
        log.info("---------------------开始统计单位积分信息START---------------------");
        insertDomainPoint();
        log.info("----------------------单位积分信息统计完毕END----------------------");
    }

    private void insertDomainPoint() {
        String sql = "INSERT INTO domain_point (" +
                " DOMAIN_CODE," +
                "LPOINT," +
                "IPOINT," +
                "SPOINT," +
                "EPOINT," +
                "PPOINT," +
                "APOINT," +
                "TPOINT," +
                "AVG_POINT," +
                "EXT_1" +
                ") SELECT " +
                "DOMAIN_CODE," +
                "sum(LPOINT) AS LPOINT," +
                "sum(IPOINT) AS IPOINT," +
                "sum(SPOINT) AS SPOINT," +
                "sum(EPOINT) AS EPOINT," +
                "sum(PPOINT) AS APOINT," +
                "sum(APOINT) AS APOINT," +
                "sum(TPOINT) AS TPOINT," +
                "SUM(TPOINT) / COUNT(*)," +
                "count(USER_ACCOUNT)" +
                " FROM " +
                "user_point" +
                " GROUP BY " +
                "DOMAIN_CODE;";
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        Assert.assertEquals(testReport.insertSQL(sql),true);
        testReport.deconnSQL();
    }
}
