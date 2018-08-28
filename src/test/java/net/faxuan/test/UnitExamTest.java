package net.faxuan.test;

import net.faxuan.data.ExamUnit;
import net.faxuan.exception.Check;
import net.faxuan.exception.CheckException;
import net.faxuan.init.TestCase;
import net.faxuan.tableProject.UnitExam;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import net.faxuan.util.GetData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/11.
 */
public class UnitExamTest extends TestCase {


    @BeforeClass(description = "连接数据库；获取数据库report_exam_domain_unit表信息然后创建测试report库中的unit_exam表")
    public void before() {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_exam_domain_unit");
        super.createTable(tableNames);
    }

    @Test(description = "创建考试汇总统计数据",priority = 1)
    public void createData() {
        new ExamUnit();
    }

    @Test(description = "对比学法库和测试库中考试汇总统计数据的一致性",priority = 2)
    public void contrastData() {
        DataBase xfReport = new DataBase(DataSource.SourceType.XFREPORT);
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        List<UnitExam> testUnitExams = GetData.getUnitExamInfo(testReport,"unit_exam");
        List<UnitExam> xfUnitExams = GetData.getUnitExamInfo(xfReport,"report_exam_domain_unit");
        xfReport.deconnSQL();
        testReport.deconnSQL();

        /**
         * 对比考试汇总单位数量是否为空或者一致
         */
        if (testUnitExams == null || xfUnitExams ==null) {
            if (testUnitExams == null && xfUnitExams==null) {
                return;
            } else if (testUnitExams != null && xfUnitExams == null) {
                throw new CheckException("测试环境考试汇总数据和学法环境数据不一致\t测试环境为空；学法环境为" + xfUnitExams.size() + "个");
            } else {
                throw new CheckException("测试环境考试汇总数据和学法环境数据不一致\t学法环境为空；测试环境为" + testUnitExams.size() + "个");
            }
        } else {
            if (testUnitExams.size() != xfUnitExams.size()) {
                if (testUnitExams.size() > xfUnitExams.size()) {
                    String superfluous = "";
                    for (UnitExam unitExam:testUnitExams) {
                        int isSame = 0;
                        for (UnitExam unitExam1:xfUnitExams) {
                            if (unitExam.getDOMAIN_CODE().equals(unitExam1.getDOMAIN_CODE()) && unitExam.getEXAM_ID().equals(unitExam1.getEXAM_ID())) {
                                isSame = 1;
                            }
                        }
                        if (isSame == 0) {
                            superfluous += (unitExam + "\n");
                        }
                    }
                    throw new CheckException("测试统计库考试汇总统计数据多于学法统计库，多出汇总统计数据如下：\n" + superfluous);
                } else {
                    String superfluous = "";
                    for (UnitExam unitExam:xfUnitExams) {
                        boolean asSame = true;
                        for (UnitExam unitExam1:testUnitExams) {
                            if (unitExam.getDOMAIN_CODE().equals(unitExam1.getDOMAIN_CODE()) && unitExam.getEXAM_ID().equals(unitExam1.getEXAM_ID())) {
                                asSame = false;
                            }
                        }
                        if (asSame) {
                            superfluous += (unitExam + "\n");
                        }
                    }
                    throw new CheckException("学法统计库考试汇总统计数据多于测试统计库，多出汇总统计数据如下：\n" + superfluous);
                }
            }
        }

        /**
         * 对比数据一致性
         */
        for (UnitExam unitExam1:testUnitExams) {
            for (UnitExam unitExam2:xfUnitExams) {
                if (unitExam1.getEXAM_ID().equals(unitExam2.getEXAM_ID()) && unitExam1.getDOMAIN_CODE().equals(unitExam2.getDOMAIN_CODE())) {
                    Check.check(unitExam1,unitExam2,unitExam1.getEXAM_YEAR(),unitExam2.getEXAM_YEAR());
                    Check.check(unitExam1,unitExam2,unitExam1.getEXAM_DOMAIN_NUM(),unitExam2.getEXAM_DOMAIN_NUM());
                    Check.check(unitExam1,unitExam2,unitExam1.getTOTAL_NUM(),unitExam2.getTOTAL_NUM());
                    Check.check(unitExam1,unitExam2,unitExam1.getEXAM_TOTAL_NUM(),unitExam2.getEXAM_TOTAL_NUM());
                    Check.check(unitExam1,unitExam2,unitExam1.getPASS_NUM(),unitExam2.getPASS_NUM());
                    Check.check(unitExam1,unitExam2,unitExam1.getNO_PASS_NUM(),unitExam2.getNO_PASS_NUM());
                    Check.check(unitExam1,unitExam2,unitExam1.getTOTAL_SCORE(),unitExam2.getTOTAL_SCORE());
                    Check.check(unitExam1,unitExam2,unitExam1.getREFERENCE_DOMAIN_NUM(),unitExam2.getREFERENCE_DOMAIN_NUM());
                }
            }
        }

    }
}
