package net.faxuan.test;

import net.faxuan.data.PointUser;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.UserPoint;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/14.
 */
public class UserPointTest extends TestCase {
    @BeforeClass(description = "连接数据库；获取数据库report_point_user表信息然后创建测试report库中的user_point表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_point_user");
        super.createTable(tableNames);
    }

    @Test(description = "创建学员积分统计数据",priority = 1)
    public void createData() {
        new PointUser();
    }

    @Test(description = "对比学法库和测试库中学员积分统计数据的一致性",priority = 2)
    public void contrastData() {
        String domainCodes = ConfirmExecutionData.getDomainCode();

        /**
         * 学法库和测试库的链接对象
         */
        DataBase xfReport = new DataBase(DataSource.SourceType.XFREPORT);
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);

        List<UserPoint> testUserPoints = GetData.getUserPointInfo(domainCodes,testReport,"user_point");
        List<UserPoint> xfUserPoints = GetData.getUserPointInfo(domainCodes,xfReport,"report_point_user");
        xfReport.deconnSQL();
        xfReport.deconnSQL();

        if (testUserPoints == null || xfUserPoints ==null) {
            if (testUserPoints == null && xfUserPoints==null) {
                return;
            } else if (xfUserPoints != null && testUserPoints == null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfUserPoints.size() + "个");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testUserPoints.size() + "个");
            }
        }else {
            if (testUserPoints.size() != xfUserPoints.size()) {
                if (testUserPoints.size() > xfUserPoints.size()) {
                    String superfluousUserInfo = "";
                    for (UserPoint userPoint1:testUserPoints) {
                        int isSame = 0;
                        for (UserPoint userPoint2:xfUserPoints) {
                            if (userPoint1.getUSER_ACCOUNT().equals(userPoint2.getUSER_ACCOUNT()) && userPoint1.getDOMAIN_CODE().equals(userPoint2.getDOMAIN_CODE())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userPoint1.toString() + "\n");
                        }
                    }
                    throw new CheckException("统计用户积分数据测试report库用户考试统计数量比学法report库多，多出用户积分信息如下：\n"+superfluousUserInfo);
                }else {
                    String superfluousUserInfo = "";
                    for (UserPoint userPoint1:xfUserPoints) {
                        int isSame = 0;
                        for (UserPoint userPoint2:testUserPoints) {
                            if (userPoint1.getUSER_ACCOUNT().equals(userPoint2.getUSER_ACCOUNT()) && userPoint1.getDOMAIN_CODE().equals(userPoint2.getDOMAIN_CODE())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluousUserInfo += (userPoint1.toString() + "\n");
                        }
                    }
                    throw new CheckException("统计用户积分数据学法report库用户考试统计数量比测试report库多，多出用户积分信息如下：\n"+superfluousUserInfo);
                }
            }
        }

        for (UserPoint userPoint1:testUserPoints) {
            for (UserPoint userPoint2:xfUserPoints) {
                if (userPoint1.getUSER_ACCOUNT().equals(userPoint2.getUSER_ACCOUNT()) && userPoint1.getDOMAIN_CODE().equals(userPoint2.getDOMAIN_CODE())) {
                    Check.check(userPoint1,userPoint2,userPoint1.getLPOINT(),userPoint2.getLPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getIPOINT(),userPoint2.getIPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getSPOINT(),userPoint2.getSPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getEPOINT(),userPoint2.getEPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getAPOINT(),userPoint2.getAPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getPPOINT(),userPoint2.getPPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getTPOINT(),userPoint2.getTPOINT());
                    Check.check(userPoint1,userPoint2,userPoint1.getEXT_1(),userPoint2.getEXT_1());
                }
            }
        }

    }
}
