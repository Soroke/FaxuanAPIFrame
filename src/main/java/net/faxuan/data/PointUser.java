package net.faxuan.data;

import net.faxuan.exception.CheckException;
import net.faxuan.tableProject.UserPoint;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户积分测试数据生成插入数据库
 * Created by song on 2017/8/15.
 */
public class PointUser {

    String domainCodes = "";
    //Map<String,Object> domainInfos = new HashMap<String, Object>();

    List<UserPoint> userPoints = new ArrayList<UserPoint>();

    public PointUser() {
        domainCodes = ConfirmExecutionData.getDomainCode();
        getUserPoint();
        insertDatabase();
    }
    /**
     * 初始化位编码
     */

    /**
     * 获取所有统计单位的用户积分信息
     */
    private void getUserPoint() {

        /**
         * 储存单位积分规则总分
         */
        Map<String,String> domainPointTotal = new HashMap<String,String>();

        /**
         * 为待统计单位的编码，加上双引号
         * 为待统计单位的编码，加上双引号
         */
        String[] domains = domainCodes.split(",");
        domainCodes = "";
        for (String domain:domains) {
            domainCodes += ("'" + domain + "',");
        }
        domainCodes = domainCodes.substring(0,domainCodes.length()-1);

        /**
         * 连接学法base库
         */
        DataBase xfBase = new DataBase(DataSource.SourceType.SOURSE2);

        /**
         * 获取单位使用积分规则总分
         */
        for (String domain:domains) {
            ResultSet resultSet = xfBase.selectSQL("SELECT RULE_NUM FROM `base_point_mapping` WHERE TARGET_DOMAIN_CODE='" + domain + "'  AND STATUS=1;");

            try {
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    int count = 15;
                    for(int i=0;i<5;i++) {
                        count = count-3;
                        String code = domain.substring(0,count);
                        if (count==0) {
                            code = "000000000000000";
                        } else {
                            for (int j=0; j<(15-count); j++) {
                                code += '0';
                            }
                        }

                        ResultSet resultSet1 = xfBase.selectSQL("SELECT RULE_NUM FROM `base_point_mapping` WHERE TARGET_DOMAIN_CODE='" + code + "'  AND STATUS=1;");

                        resultSet1.last();
                        if (resultSet1.getRow() == 0) {
                            continue;
                        } else {
                            domainPointTotal.put(domain,resultSet1.getString("RULE_NUM"));
                            break;
                        }
                    }

                } else {
                    domainPointTotal.put(domain,resultSet.getString("RULE_NUM"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /**
         * 获取用户积分信息
         */
        ResultSet resultSet = xfBase.selectSQL("SELECT * FROM base_point_record WHERE USER_ACCOUNT IN(SELECT USER_ACCOUNT FROM `base_user` WHERE DOMAIN_CODE IN(" + domainCodes + "));");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return;
            }
            resultSet.beforeFirst();

            while(resultSet.next()) {
                UserPoint userPoint = new UserPoint();
                userPoint.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                userPoint.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                userPoint.setLPOINT(resultSet.getInt("LPOINT"));
                userPoint.setIPOINT(resultSet.getInt("IPOINT"));
                userPoint.setSPOINT(resultSet.getInt("SPOINT"));
                userPoint.setEPOINT(resultSet.getInt("EPOINT"));
                userPoint.setAPOINT(resultSet.getInt("APOINT"));
                userPoint.setPPOINT(resultSet.getInt("PPOINT"));
                userPoint.setTPOINT(resultSet.getInt("TPOINT"));
                userPoint.setEXT_1(Integer.valueOf(domainPointTotal.get(resultSet.getString("DOMAIN_CODE"))));
//System.err.println(userPoint);
                userPoints.add(userPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfBase.deconnSQL();
    }

    /**
     * 插入用户积分数据到数据库
     */
    private boolean insertDatabase() {
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        for (UserPoint userPoint:userPoints) {
            String insertSQL = "insert into user_point(USER_ACCOUNT,DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT,EXT_1) values('" +
                    userPoint.getUSER_ACCOUNT() + "'," +
                    userPoint.getDOMAIN_CODE() + "," +
                    userPoint.getLPOINT() + "," +
                    userPoint.getIPOINT() + "," +
                    userPoint.getSPOINT() + "," +
                    userPoint.getEPOINT() + "," +
                    userPoint.getAPOINT() + "," +
                    userPoint.getPPOINT() + "," +
                    userPoint.getTPOINT() + "," +
                    userPoint.getEXT_1() + ");";
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
        }
        testReport.deconnSQL();
        return true;
    }




}
