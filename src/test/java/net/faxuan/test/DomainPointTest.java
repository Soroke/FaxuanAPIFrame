package net.faxuan.test;

import net.faxuan.data.ExamDomain;
import net.faxuan.data.PointDomain;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.DomainExam;
import net.faxuan.tableProject.DomainPoint;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/15.
 */
public class DomainPointTest extends TestCase{
    @BeforeClass(description = "连接数据库；获取数据库report_point_domain表信息然后创建测试report库中的domain_point表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_point_domain");
        super.createTable(tableNames);
    }

    @Test(description = "创建单位考试统计数据",priority = 1)
    public void createData() {
        new PointDomain();
    }

    @Test(description = "对比学法库和测试库中单位积分统计数据的一致性",priority = 2)
    public void contrastData() {
        String domainCode = ConfirmExecutionData.getDomainCode();
        /**
         * 获取xf和test的report库中的数据
         */
        DataBase xfReport = new DataBase(DataSource.SourceType.SOURSE4);
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);
        List<DomainPoint> testDomainPoints = GetData.getDomainPointInfo(domainCode,testReport,"domain_point");
        List<DomainPoint> xfDomainPoints = GetData.getDomainPointInfo(domainCode,xfReport,"report_point_domain");
        xfReport.deconnSQL();
        testReport.deconnSQL();

        if (testDomainPoints == null || xfDomainPoints == null) {
            if (testDomainPoints == null && xfDomainPoints == null) {
                return;
            } else if (testDomainPoints == null && xfDomainPoints!=null) {
                throw new CheckException("测试环境数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfDomainPoints.size() + "个对象");
            } else {
                throw new CheckException("测试环境数据和学法环境数据不一致\t学法环境为空；测试环境为" + testDomainPoints.size() + "个对象");
            }
        }
        /**
         * 对比单位考试统计的单位数量
         * 如果不相等输出多的单位信息
         */
        if (testDomainPoints.size() != xfDomainPoints.size()) {
            if (testDomainPoints.size() > xfDomainPoints.size()) {
                String superfluousDomainInfo = "";
                for (DomainPoint domainPoint1 : testDomainPoints) {
                    int isSame = 0;
                    for (DomainPoint domainPoint2 : xfDomainPoints) {
                        if (domainPoint1.getDOMAIN_CODE().equals(domainPoint2.getDOMAIN_CODE())) {
                            isSame = 1;
                        }
                    }
                    if (isSame == 0) {
                        superfluousDomainInfo += (domainPoint1.toString() + "\n");
                    }
                }
                throw new CheckException("统计单位积分数据测试report库考试统计单位数量比学法report库多，多出单位积分信息如下：\n" + superfluousDomainInfo);
            } else {
                String superfluousDomainInfo = "";
                for (DomainPoint domainPoint1 : xfDomainPoints) {
                    int isSame = 0;
                    for (DomainPoint domainPoint2 : testDomainPoints) {
                        if (domainPoint1.getDOMAIN_CODE().equals(domainPoint2.getDOMAIN_CODE())) {
                            isSame = 1;
                        }
                    }
                    if (isSame == 0) {
                        superfluousDomainInfo += (domainPoint1.toString() + "\n");
                    }
                }
                throw new CheckException("统计单位积分数据学法report库考试统计单位数量比测试report库多，多出单位积分信息如下：\n" + superfluousDomainInfo);
            }

        }

        for (DomainPoint domainPoint1:testDomainPoints) {
            for (DomainPoint domainPoint2:xfDomainPoints) {
                if (domainPoint1.getDOMAIN_CODE().equals(domainPoint2.getDOMAIN_CODE())) {
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getLPOINT(),domainPoint2.getLPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getIPOINT(),domainPoint2.getIPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getSPOINT(),domainPoint2.getSPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getEPOINT(),domainPoint2.getEPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getAPOINT(),domainPoint2.getAPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getPPOINT(),domainPoint2.getPPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getTPOINT(),domainPoint2.getTPOINT());
                    Check.check(domainPoint1,domainPoint2,domainPoint1.getAVG_POINT(),domainPoint2.getAVG_POINT());
                }
            }
        }
    }
}
