package net.faxuan.data;

import net.faxuan.tableProject.DomainPoint;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/7/28.
 */
public class PointDomain {
    private String domainCodes;
    private List<DomainPoint> domainPoints = new ArrayList<DomainPoint>();

    public  PointDomain() {
        domainCodes = ConfirmExecutionData.getDomainCode();
        getDomainPointInfo();
        insertDataBase(domainPoints);
    }

    /**
     * 从用户积分表获取单位积分信息
     */
    public void getDomainPointInfo() {
        String[] domain = domainCodes.split(",");

        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        for (String domainCode:domain) {
            ResultSet resultSet = testReport.selectSQL("SELECT DOMAIN_CODE,SUM(LPOINT) LPOINT,SUM(IPOINT) IPOINT,SUM(SPOINT) SPOINT,SUM(EPOINT) EPOINT,SUM(APOINT) APOINT,SUM(PPOINT) PPOINT,SUM(TPOINT) TPOINT,EXT_1,CAST((SUM(LPOINT)+SUM(IPOINT)+SUM(SPOINT)+SUM(EPOINT)+SUM(APOINT)+SUM(PPOINT))/COUNT(*) AS DECIMAL(38,2)) AVG_POINT FROM `user_point` WHERE DOMAIN_CODE='" + domainCode + "';");

            try {
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    continue;
                }
                resultSet.beforeFirst();

                while (resultSet.next()) {
                    DomainPoint domainPoint = new DomainPoint();
                    domainPoint.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                    domainPoint.setLPOINT(resultSet.getInt("LPOINT"));
                    domainPoint.setIPOINT(resultSet.getInt("IPOINT"));
                    domainPoint.setSPOINT(resultSet.getInt("SPOINT"));
                    domainPoint.setEPOINT(resultSet.getInt("EPOINT"));
                    domainPoint.setAPOINT(resultSet.getInt("APOINT"));
                    domainPoint.setPPOINT(resultSet.getInt("PPOINT"));
                    domainPoint.setTPOINT(resultSet.getInt("TPOINT"));
                    domainPoint.setAVG_POINT(resultSet.getFloat("AVG_POINT"));
                    domainPoints.add(domainPoint);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        testReport.deconnSQL();
    }

    /**
     * 将单位积分信息插入测试数据库
     * @param domainPoints
     */
    private void insertDataBase(List<DomainPoint> domainPoints) {
        if (domainPoints == null) {
            return;
        }
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        for (DomainPoint domainPoint:domainPoints) {
            String insertSQL = "INSERT INTO domain_point(DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT,AVG_POINT) VALUES(" +
                    domainPoint.getDOMAIN_CODE() + "," +
                    domainPoint.getLPOINT() + "," +
                    domainPoint.getIPOINT() + "," +
                    domainPoint.getSPOINT() + "," +
                    domainPoint.getEPOINT() + "," +
                    domainPoint.getAPOINT() + "," +
                    domainPoint.getPPOINT() + "," +
                    domainPoint.getTPOINT() + "," +
                    domainPoint.getAVG_POINT() + ");";
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
        }
        testReport.deconnSQL();
    }

}
