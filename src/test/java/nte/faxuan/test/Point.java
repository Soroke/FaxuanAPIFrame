package nte.faxuan.test;

import net.faxuan.data.PointDomain;
import net.faxuan.data.PointUser;
import net.faxuan.data.SourceType;
import net.faxuan.sql.DomainPoint;
import net.faxuan.sql.UserPoint;
import net.faxuan.util.DataBase;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/7/28.
 */
public class Point {
    private Logger log = Logger.getLogger(this.getClass());
    private String domainCode="101002001002002";

    @Test(testName = "测试创建表",priority = 1)
    public void createTable() {
        DataBase dataBase = new DataBase(SourceType.SOURSE1);
        Boolean b1 = UserPoint.create(dataBase);
        Boolean b2 = DomainPoint.create(dataBase);
        dataBase.deconnSQL();
        Assert.assertEquals(b1 == b2 == true,true);

    }

    @Test(testName = "测试生成用户积分数据并导入测试库user_point表",priority = 2)
    public void pointUserTest() {
        DataBase dataBase1 = new DataBase(SourceType.SOURSE1);
        DataBase dataBase2 = new DataBase(SourceType.SOURSE2);
        ResultSet resultSet = dataBase2.selectSQL("SELECT * FROM `base_point_record` WHERE DOMAIN_CODE='" + domainCode + "';");
        try {
            while(resultSet.next()) {
                dataBase1.insertSQL("insert into user_point(USER_ACCOUNT,DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT,EXT_1) values(" +
                        resultSet.getString("USER_ACCOUNT") + "," +
                        resultSet.getString("DOMAIN_CODE")+ "," +
                        resultSet.getString("LPOINT") + "," +
                        resultSet.getString("IPOINT")+ "," +
                        resultSet.getString("SPOINT") + "," +
                        resultSet.getString("EPOINT")+ "," +
                        resultSet.getString("APOINT")+ "," +
                        resultSet.getString("PPOINT")+ "," +
                        resultSet.getString("TPOINT")+ "," +
                        resultSet.getString("EXT_1")+")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dataBase2.deconnSQL();
        dataBase1.deconnSQL();
    }

    @Test(testName = "测试生成单位积分数据并导入测试库domain_point表",priority = 3)
    public void pointDomainTest() {
        DataBase dataBase1 = new DataBase(SourceType.SOURSE1);

        ResultSet resultSet = dataBase1.selectSQL("SELECT * FROM user_point u WHERE DOMAIN_CODE='" + domainCode + "'");
        int lpoint = 0;
        int ipoint = 0;
        int spoint = 0;
        int epoint = 0;
        int apoint = 0;
        int ppoint = 0;
        int tpoint = 0;
        try {
            while(resultSet.next()) {
                lpoint += resultSet.getInt("LPOINT") ;
                ipoint += resultSet.getInt("IPOINT");
                spoint += resultSet.getInt("SPOINT") ;
                epoint += resultSet.getInt("EPOINT");
                apoint += resultSet.getInt("APOINT");
                ppoint += resultSet.getInt("PPOINT");
                tpoint += resultSet.getInt("TPOINT");
            }
            dataBase1.insertSQL("insert into domain_point(DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT) value(" + domainCode + "," + lpoint + "," + ipoint + ","  + spoint + ","
                    + epoint + "," + apoint + "," + ppoint + "," + tpoint +")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataBase1.deconnSQL();
    }

    @Test(testName = "对比学法报表库数据",priority = 4)
    public void contrastData() {
        DataBase dataBase1 = new DataBase(SourceType.SOURSE1);
        DataBase dataBase4 = new DataBase(SourceType.SOURSE4);

        ResultSet rs1 = dataBase1.selectSQL("SELECT * FROM user_point WHERE DOMAIN_CODE = '" + domainCode + "';");
        ResultSet rs2 = dataBase4.selectSQL("SELECT * FROM report_point_user WHERE DOMAIN_CODE = '" + domainCode + "';");
        List<PointUser> userpoint1 = new ArrayList<PointUser>();
        List<PointUser> userpoint2 = new ArrayList<PointUser>();

        try {
            while(rs1.next()) {
                PointUser pointUser = new PointUser();
                pointUser.setUSER_ACCOUNT(rs1.getString("USER_ACCOUNT"));
                pointUser.setDOMAIN_CODE(rs1.getString("DOMAIN_CODE"));
                pointUser.setLPOINT(rs1.getInt("LPOINT"));
                pointUser.setIPOINT(rs1.getInt("IPOINT"));
                pointUser.setSPOINT(rs1.getInt("SPOINT"));
                pointUser.setEPOINT(rs1.getInt("EPOINT"));
                pointUser.setAPOINT(rs1.getInt("APOINT"));
                pointUser.setPPOINT(rs1.getInt("PPOINT"));
                pointUser.setTPOINT(rs1.getInt("TPOINT"));
                pointUser.setEXT_1(rs1.getInt("EXT_1"));
                userpoint1.add(pointUser);
            }
            while(rs2.next()) {
                PointUser pointUser = new PointUser();
                pointUser.setUSER_ACCOUNT(rs2.getString("USER_ACCOUNT"));
                pointUser.setDOMAIN_CODE(rs2.getString("DOMAIN_CODE"));
                pointUser.setLPOINT(rs2.getInt("LPOINT"));
                pointUser.setIPOINT(rs2.getInt("IPOINT"));
                pointUser.setSPOINT(rs2.getInt("SPOINT"));
                pointUser.setEPOINT(rs2.getInt("EPOINT"));
                pointUser.setAPOINT(rs2.getInt("APOINT"));
                pointUser.setTPOINT(rs2.getInt("TPOINT"));
                pointUser.setPPOINT(rs2.getInt("PPOINT"));
                pointUser.setEXT_1(rs2.getInt("EXT_1"));
                userpoint2.add(pointUser);
            }
            for(PointUser pointUser1:userpoint1) {
                for (PointUser pointUser2:userpoint2) {
                    if(pointUser1.getUSER_ACCOUNT().equals(pointUser2.getUSER_ACCOUNT())){
                        try{
                            Assert.assertEquals(pointUser1.getLPOINT(),pointUser2.getLPOINT());
                            Assert.assertEquals(pointUser1.getIPOINT(),pointUser2.getIPOINT());
                            Assert.assertEquals(pointUser1.getSPOINT(),pointUser2.getSPOINT());
                            Assert.assertEquals(pointUser1.getEPOINT(),pointUser2.getEPOINT());
                            Assert.assertEquals(pointUser1.getAPOINT(),pointUser2.getAPOINT());
                            Assert.assertEquals(pointUser1.getPPOINT(),pointUser2.getPPOINT());
                            Assert.assertEquals(pointUser1.getTPOINT(),pointUser2.getTPOINT());
                            Assert.assertEquals(pointUser1.getEXT_1(),pointUser2.getEXT_1());
                        } catch (AssertionError error) {
                            log.info("学法report库report_point_user"  + "\t\t" + "测试report库user_point"  );
                            log.info("LPOINT:" + pointUser1.getLPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getLPOINT());
                            log.info("IPOINT:" + pointUser1.getIPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getIPOINT());
                            log.info("SPOINT:" + pointUser1.getSPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getSPOINT());
                            log.info("EPOINT:" + pointUser1.getEPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getEPOINT());
                            log.info("APOINT:" + pointUser1.getAPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getAPOINT());
                            log.info("PPOINT:" + pointUser1.getPPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getPPOINT());
                            log.info("TPOINT:" + pointUser1.getTPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getTPOINT());
                            log.info("EXT_1:" + pointUser1.getEXT_1()+ "\t\t\t\t\t\t\t\t\t" +  pointUser2.getEXT_1());
                            log.info("用户账号为" + pointUser1.getUSER_ACCOUNT() + "的用户对比失败");
                            Assert.assertEquals(pointUser1.getLPOINT(),pointUser2.getLPOINT());
                            Assert.assertEquals(pointUser1.getIPOINT(),pointUser2.getIPOINT());
                            Assert.assertEquals(pointUser1.getSPOINT(),pointUser2.getSPOINT());
                            Assert.assertEquals(pointUser1.getEPOINT(),pointUser2.getEPOINT());
                            Assert.assertEquals(pointUser1.getAPOINT(),pointUser2.getAPOINT());
                            Assert.assertEquals(pointUser1.getPPOINT(),pointUser2.getPPOINT());
                            Assert.assertEquals(pointUser1.getTPOINT(),pointUser2.getTPOINT());
                            Assert.assertEquals(pointUser1.getEXT_1(),pointUser2.getEXT_1());
                        }

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("对比用户积分表测通过");


        ResultSet resultSet1 = dataBase1.selectSQL("SELECT * FROM domain_point WHERE DOMAIN_CODE = '" + domainCode + "';");
        ResultSet resultSet2 = dataBase4.selectSQL("SELECT * FROM report_point_domain WHERE DOMAIN_CODE = '" + domainCode + "';");

        List<PointDomain> pointDomains1 = new ArrayList<PointDomain>();
        List<PointDomain> pointDomains2 = new ArrayList<PointDomain>();

        try {
            while(resultSet1.next()) {
                PointDomain pointDomain = new PointDomain();
                pointDomain.setDOMAIN_CODE(resultSet1.getString("DOMAIN_CODE"));
                pointDomain.setLPOINT(resultSet1.getInt("LPOINT"));
                pointDomain.setIPOINT(resultSet1.getInt("IPOINT"));
                pointDomain.setSPOINT(resultSet1.getInt("SPOINT"));
                pointDomain.setEPOINT(resultSet1.getInt("EPOINT"));
                pointDomain.setAPOINT(resultSet1.getInt("APOINT"));
                pointDomain.setPPOINT(resultSet1.getInt("PPOINT"));
                pointDomain.setTPOINT(resultSet1.getInt("TPOINT"));
                pointDomains1.add(pointDomain);

            }
            while(resultSet2.next()) {
                PointDomain pointDomain = new PointDomain();
                pointDomain.setDOMAIN_CODE(resultSet2.getString("DOMAIN_CODE"));
                pointDomain.setLPOINT(resultSet2.getInt("LPOINT"));
                pointDomain.setIPOINT(resultSet2.getInt("IPOINT"));
                pointDomain.setSPOINT(resultSet2.getInt("SPOINT"));
                pointDomain.setEPOINT(resultSet2.getInt("EPOINT"));
                pointDomain.setAPOINT(resultSet2.getInt("APOINT"));
                pointDomain.setTPOINT(resultSet2.getInt("TPOINT"));
                pointDomain.setPPOINT(resultSet2.getInt("PPOINT"));
                pointDomains2.add(pointDomain);
            }

            for(PointDomain pointDomain1:pointDomains1) {
                for(PointDomain pointDomain2:pointDomains2) {
                    if(pointDomain1.getDOMAIN_CODE().equals(pointDomain2.getDOMAIN_CODE())){
                        try{
                            Assert.assertEquals(pointDomain1.getLPOINT(),pointDomain2.getLPOINT());
                            Assert.assertEquals(pointDomain1.getIPOINT(),pointDomain2.getIPOINT());
                            Assert.assertEquals(pointDomain1.getSPOINT(),pointDomain2.getSPOINT());
                            Assert.assertEquals(pointDomain1.getEPOINT(),pointDomain2.getEPOINT());
                            Assert.assertEquals(pointDomain1.getAPOINT(),pointDomain2.getAPOINT());
                            Assert.assertEquals(pointDomain1.getPPOINT(),pointDomain2.getPPOINT());
                            Assert.assertEquals(pointDomain1.getTPOINT(),pointDomain2.getTPOINT());
                        } catch (AssertionError error) {
                            log.info("学法report库report_point_domain"  + "\t\t" + "测试report库domain_point"  );
                            log.info("LPOINT:" + pointDomain1.getLPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getLPOINT());
                            log.info("IPOINT:" + pointDomain1.getIPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getIPOINT());
                            log.info("SPOINT:" + pointDomain1.getSPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getSPOINT());
                            log.info("EPOINT:" + pointDomain1.getEPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getEPOINT());
                            log.info("APOINT:" + pointDomain1.getAPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getAPOINT());
                            log.info("PPOINT:" + pointDomain1.getPPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getPPOINT());
                            log.info("TPOINT:" + pointDomain1.getTPOINT()+ "\t\t\t\t\t\t\t\t\t" +  pointDomain2.getTPOINT());
                            log.info("学法库和测试库单位积分表单位编码为" + pointDomain2.getDOMAIN_CODE() + "的单位对比数据结果不一致");
                            Assert.assertEquals(pointDomain1.getLPOINT(),pointDomain2.getLPOINT());
                            Assert.assertEquals(pointDomain1.getIPOINT(),pointDomain2.getIPOINT());
                            Assert.assertEquals(pointDomain1.getSPOINT(),pointDomain2.getSPOINT());
                            Assert.assertEquals(pointDomain1.getEPOINT(),pointDomain2.getEPOINT());
                            Assert.assertEquals(pointDomain1.getAPOINT(),pointDomain2.getAPOINT());
                            Assert.assertEquals(pointDomain1.getPPOINT(),pointDomain2.getPPOINT());
                            Assert.assertEquals(pointDomain1.getTPOINT(),pointDomain2.getTPOINT());
                        }

                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("对比单位积分表测通过");

        dataBase1.deconnSQL();
        dataBase4.deconnSQL();
    }
}
