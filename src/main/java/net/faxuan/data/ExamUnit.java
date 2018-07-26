package net.faxuan.data;

import net.faxuan.tableProject.Exam;
import net.faxuan.tableProject.UnitExam;
import net.faxuan.util.ConfirmExecutionData;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by song on 2017/8/10.
 * 考试汇总统计
 */
public class ExamUnit {

    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);
    /**
     * 储存获取的考试信息
     */
    List<Exam> exams = new ArrayList<Exam>();


    public ExamUnit() {
        exams = ConfirmExecutionData.getExams();
        insert();
    }

    /**
     * 获取考试汇总信息并插入数据库
     */
    private void insert() {
        List<UnitExam> unitExams = new ArrayList<UnitExam>();

        for (Exam exam:exams) {
            if (exam.getTARGET_DOMAIN_CODE().length() == 19 && exam.getINDUSTRY_DOMAIN_FLAG().equals("1")) {
                unitExams = releaseToIndustryDomain(exam);
            } else {
                unitExams = releaseToDomain(exam);
            }

            if (unitExams == null) {
                continue;
            }
            insertDatabase(unitExams);
        }

    }


    /**
     * 发布到行业单位的考试
     * @param exam 考试信息
     * @return
     */
    private List<UnitExam> releaseToIndustryDomain(Exam exam) {

        List<UnitExam> unitExams = new ArrayList<UnitExam>();
        /**
         * 分割行业单位编码，供sql查询使用
         */
        String domainCode = exam.getTARGET_DOMAIN_CODE();
        //String domainCode = "2002001001001000000";
        String code = "000";
        for (int i=0;i<6;i++) {
            if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
                domainCode = domainCode.substring(0,domainCode.length()-3);
            }
        }

        /**
         * 获取应统计的单位的编码
         */
        DataBase xfBase = new DataBase(DataSource.SourceType.SOURSE2);

        if (domainCode.length() == 19) {
            UnitExam unitExam = new UnitExam();
            unitExam.setDOMAIN_CODE(domainCode);
            unitExam.setEXAM_ID(exam.getID());
            unitExam.setEXAM_YEAR(exam.getEXAM_YEAR());
            unitExams.add(unitExam);
        } else {
            ResultSet countDomainResultSet = xfBase.selectSQL("SELECT DOMAIN_CODE FROM base_industry_domain WHERE DOMAIN_CODE LIKE '"+domainCode+"%' AND DOMAIN_TYPE='2'");
            try {
                countDomainResultSet.last();
                if (countDomainResultSet.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的行业单位下没有“行业普通管理单位”");
                    return null;
                }
                countDomainResultSet.beforeFirst();
                while (countDomainResultSet.next()) {
                    UnitExam unitExam = new UnitExam();
                    unitExam.setDOMAIN_CODE(countDomainResultSet.getString("DOMAIN_CODE"));
                    unitExam.setEXAM_ID(exam.getID());
                    unitExam.setEXAM_YEAR(exam.getEXAM_YEAR());
                    unitExams.add(unitExam);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * 学法库连接
         */
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);


        /**
         * 待移除汇总统计单位数据
         */
        List<UnitExam> removeUnitExam = new ArrayList<UnitExam>();

        for (UnitExam unitExam:unitExams) {
            String subunitDomainCodes = "";


            /**
             * 获取待统计行业单位关联的普通单位的编码
             */
            ResultSet resultSet = xfBase.selectSQL("SELECT BASE_DOMAIN_CODE FROM base_domain_relevance WHERE DOMAIN_CODE='" + unitExam.getDOMAIN_CODE() + "';");
            try {
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的行业单位没有关联普通单位");
                    removeUnitExam.add(unitExam);
                    continue;
                }
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    subunitDomainCodes += (resultSet.getString("BASE_DOMAIN_CODE") + ",");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            subunitDomainCodes = subunitDomainCodes.substring(0,subunitDomainCodes.length()-1);

            /**
             * 统计应参考单位数量
             */

            ResultSet examDomainCount = null;
            if (exam.getEXAM_YEAR().equals(yearString)) {
                examDomainCount = testReport.selectSQL("SELECT *  FROM `user_exam` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "' GROUP BY DOMAIN_CODE;");
            } else {
                examDomainCount = testReport.selectSQL("SELECT *  FROM `user_exam" + (yearInt-1) + "` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "' GROUP BY DOMAIN_CODE;");
            }


            try {
                examDomainCount.last();
                if (examDomainCount.getRow() == 0) {
                    removeUnitExam.add(unitExam);
                    continue;
                }
                examDomainCount.beforeFirst();
                int count = 0;
                while (examDomainCount.next()) {
                    count++;
                }
                unitExam.setREFERENCE_DOMAIN_NUM(count);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            /**
             * 获取统计单位下所有用户的总分
             */

            ResultSet resultSet1 = null;
            if (exam.getEXAM_YEAR().equals(yearString)) {
                resultSet1 = testReport.selectSQL("SELECT SUM(EXAM_SCORE) TOTAL_SCORE FROM `user_exam` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "';");
            } else {
                resultSet1 = testReport.selectSQL("SELECT SUM(EXAM_SCORE) TOTAL_SCORE FROM `user_exam" + (yearInt-1) + "` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "';");
            }


            try {
                resultSet1.last();
                if (resultSet1.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的行业单位下没有用户可以看到该考试");
                    continue;
                }
                resultSet1.beforeFirst();
                while (resultSet1.next()) {
                    unitExam.setTOTAL_SCORE(resultSet1.getDouble("TOTAL_SCORE"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /**
             * 汇总单位统计数据
             */
            ResultSet resultSet2 = null;

            if (exam.getEXAM_YEAR().equals(yearString)) {
                resultSet2 = testReport.selectSQL("SELECT COUNT(*) REFERENCE_DOMAIN_NUM,SUM(PASS_NUM>0 OR NO_PASS_NUM>0) EXAM_DOMAIN_NUM,SUM(TOTAL_NUM) TOTAL_NUM,(SUM(PASS_NUM)+SUM(NO_PASS_NUM)) EXAM_TOTAL_NUM,SUM(PASS_NUM) PASS_NUM,SUM(NO_PASS_NUM) NO_PASS_NUM FROM domain_exam WHERE DOMAIN_CODE IN ("+ subunitDomainCodes +") AND EXAM_ID = '"+ exam.getID() + "';");
            } else {
                resultSet2 = testReport.selectSQL("SELECT COUNT(*) REFERENCE_DOMAIN_NUM,SUM(PASS_NUM>0 OR NO_PASS_NUM>0) EXAM_DOMAIN_NUM,SUM(TOTAL_NUM) TOTAL_NUM,(SUM(PASS_NUM)+SUM(NO_PASS_NUM)) EXAM_TOTAL_NUM,SUM(PASS_NUM) PASS_NUM,SUM(NO_PASS_NUM) NO_PASS_NUM FROM domain_exam" + (yearInt-1) + " WHERE DOMAIN_CODE IN ("+ subunitDomainCodes +") AND EXAM_ID = '"+ exam.getID() + "';");
            }

            try {
                resultSet2.last();
                if (resultSet2.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的行业单位下没有单位统计数据");
                    continue;
                }
                resultSet2.beforeFirst();

                while (resultSet2.next()) {
                    unitExam.setNO_PASS_NUM(resultSet2.getInt("NO_PASS_NUM"));
                    unitExam.setEXAM_DOMAIN_NUM(resultSet2.getInt("EXAM_DOMAIN_NUM"));
                    unitExam.setTOTAL_NUM(resultSet2.getInt("TOTAL_NUM"));
                    unitExam.setPASS_NUM(resultSet2.getInt("PASS_NUM"));
                    unitExam.setEXAM_TOTAL_NUM(resultSet2.getInt("EXAM_TOTAL_NUM"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        /**
         * 关闭学法base和report库连接
         */
        testReport.deconnSQL();
        xfBase.deconnSQL();

        /**
         * 移除没有考试的汇总信息
         */
        for (int i = unitExams.size()-1;i>=0;i--) {
            for (UnitExam unitExam1:removeUnitExam) {
                if (unitExams.get(i).getDOMAIN_CODE().equals(unitExam1.getDOMAIN_CODE()) && unitExams.get(i).getEXAM_ID().equals(unitExam1.getEXAM_ID())) {
                    unitExams.remove(i);
                    break;
                }
            }
        }

        return unitExams;
    }


    /**
     * 发布到普通单位的考试
     * @param exam
     * @return
     */
    private List<UnitExam> releaseToDomain(Exam exam) {
        /**
         * 储存考试汇总单位信息
         */
        List<UnitExam> unitExams = new ArrayList<UnitExam>();

        /**
         * 连接学法Base库
         */
        DataBase xfBase = new DataBase(DataSource.SourceType.SOURSE2);

        String domainCode = exam.getTARGET_DOMAIN_CODE();
        String code = "000";
        ResultSet resultSet = null;
        if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
            /**
             * 普法管理单位分割单位编码
             */
            for (int i = 0; i < 5; i++) {
                if (domainCode.substring(domainCode.length() - 3, domainCode.length()).equals(code)) {
                    domainCode = domainCode.substring(0, domainCode.length() - 3);
                }
            }
            resultSet = xfBase.selectSQL("SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '"+domainCode+"%' AND DOMAIN_TYPE='2';");
        } else {
            resultSet = xfBase.selectSQL("SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE='"+domainCode+"' AND DOMAIN_TYPE='2';");
        }
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的单位没有可以统计的单位");
                return null;
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                UnitExam unitExam = new UnitExam();
                unitExam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                unitExam.setEXAM_YEAR(exam.getEXAM_YEAR());
                unitExam.setEXAM_ID(exam.getID());
                unitExams.add(unitExam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /**
         * 学法库连接
         */
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);

        /**
         * 待移除汇总统计单位数据
         */
        List<UnitExam> removeUnitExam = new ArrayList<UnitExam>();

        for (UnitExam unitExam:unitExams) {
            //待统计的普通单位编码
            String subunitDomainCodes = "";
            //统计单位的编码
            String domain = unitExam.getDOMAIN_CODE();
            for (int i = 0; i < 5; i++) {
                if (domain.substring(domain.length() - 3, domain.length()).equals(code)) {
                    domain = domain.substring(0, domain.length() - 3);
                }
            }
            ResultSet resultSet1 = xfBase.selectSQL("SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '"+domain+"%' AND DOMAIN_TYPE=1;");

            try {
                resultSet1.last();
                if (resultSet1.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的单位没有可以统计的单位");
                    removeUnitExam.add(unitExam);
                    continue;
                }
                resultSet1.beforeFirst();

                while (resultSet1.next()) {
                    subunitDomainCodes += (resultSet1.getString("DOMAIN_CODE") + ",");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            subunitDomainCodes = subunitDomainCodes.substring(0,subunitDomainCodes.length()-1);

            /**
             * 统计应参考单位数量
             */

            ResultSet examDomainCount = null;
            if (exam.getEXAM_YEAR().equals(yearString)) {
                examDomainCount = testReport.selectSQL("SELECT * FROM `user_exam` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "' GROUP BY DOMAIN_CODE;");
            } else {
                examDomainCount = testReport.selectSQL("SELECT * FROM `user_exam" + (yearInt-1) + "` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "' GROUP BY DOMAIN_CODE;");
            }

            try {
                examDomainCount.last();
                if (examDomainCount.getRow() == 0) {
                    removeUnitExam.add(unitExam);
                    continue;
                }
                examDomainCount.beforeFirst();
                int count = 0;
                while (examDomainCount.next()) {
                    count++;
                }
                unitExam.setREFERENCE_DOMAIN_NUM(count);
            } catch (SQLException e) {
                e.printStackTrace();
            }



            /**
             * 获取统计单位下所有用户的总分
             */
            ResultSet resultSet2 = null;
            if (exam.getEXAM_YEAR().equals(yearString)) {
                resultSet2 = testReport.selectSQL("SELECT SUM(EXAM_SCORE) TOTAL_SCORE FROM `user_exam` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "';");
            } else {
                resultSet2 = testReport.selectSQL("SELECT SUM(EXAM_SCORE) TOTAL_SCORE FROM `user_exam" + (yearInt-1) + "` WHERE DOMAIN_CODE IN(" + subunitDomainCodes + ") AND EXAM_ID = '" + exam.getID() + "';");
            }

            try {
                resultSet2.last();
                if (resultSet2.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的单位下没有用户可以看到该考试");
                    continue;
                }
                resultSet2.beforeFirst();
                while (resultSet2.next()) {
                    unitExam.setTOTAL_SCORE(resultSet2.getDouble("TOTAL_SCORE"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /**
             * 汇总单位统计数据
             */
            ResultSet resultSet3 = null;

            if (exam.getEXAM_YEAR().equals(yearString)) {
                resultSet3 = testReport.selectSQL("SELECT COUNT(*) REFERENCE_DOMAIN_NUM,SUM(PASS_NUM>0 OR NO_PASS_NUM>0) EXAM_DOMAIN_NUM,SUM(TOTAL_NUM) TOTAL_NUM,(SUM(PASS_NUM)+SUM(NO_PASS_NUM)) EXAM_TOTAL_NUM,SUM(PASS_NUM) PASS_NUM,SUM(NO_PASS_NUM) NO_PASS_NUM FROM domain_exam WHERE DOMAIN_CODE IN ("+ subunitDomainCodes +") AND EXAM_ID = '"+ exam.getID() + "';");
            } else {
                resultSet3 = testReport.selectSQL("SELECT COUNT(*) REFERENCE_DOMAIN_NUM,SUM(PASS_NUM>0 OR NO_PASS_NUM>0) EXAM_DOMAIN_NUM,SUM(TOTAL_NUM) TOTAL_NUM,(SUM(PASS_NUM)+SUM(NO_PASS_NUM)) EXAM_TOTAL_NUM,SUM(PASS_NUM) PASS_NUM,SUM(NO_PASS_NUM) NO_PASS_NUM FROM domain_exam" + (yearInt-1) + " WHERE DOMAIN_CODE IN ("+ subunitDomainCodes +") AND EXAM_ID = '"+ exam.getID() + "';");
            }

            try {
                resultSet3.last();
                if (resultSet3.getRow() == 0) {
                    System.err.println("考试ID" + exam.getID() + "--考试名称：" + exam.getEXAM_NAME() + "发布的单位下没有单位统计数据");
                    continue;
                }
                resultSet3.beforeFirst();

                while (resultSet3.next()) {
                    unitExam.setEXAM_DOMAIN_NUM(resultSet3.getInt("EXAM_DOMAIN_NUM"));
                    unitExam.setTOTAL_NUM(resultSet3.getInt("TOTAL_NUM"));
                    unitExam.setEXAM_TOTAL_NUM(resultSet3.getInt("EXAM_TOTAL_NUM"));
                    unitExam.setPASS_NUM(resultSet3.getInt("PASS_NUM"));
                    unitExam.setNO_PASS_NUM(resultSet3.getInt("NO_PASS_NUM"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        /**
         * 关闭学法base和report库连接
         */
        testReport.deconnSQL();
        xfBase.deconnSQL();

        /**
         * 移除没有考试的汇总信息
         */

        for (int i = unitExams.size()-1;i>=0;i--) {
            for (UnitExam unitExam1:removeUnitExam) {
                if (unitExams.get(i).getDOMAIN_CODE().equals(unitExam1.getDOMAIN_CODE()) && unitExams.get(i).getEXAM_ID().equals(unitExam1.getEXAM_ID())) {
                    unitExams.remove(i);
                    break;
                }
            }
        }

        return  unitExams;
    }

    /**
     * 数据插入到数据库
     * @param unitExams
     */
    private void insertDatabase(List<UnitExam> unitExams) {
        /**
         * 连接测试库
         */
        DataBase testReport = new DataBase(DataSource.SourceType.SOURSE1);

        for (UnitExam unitExam:unitExams) {
            String insertSQL = "INSERT INTO unit_exam(DOMAIN_CODE,EXAM_ID,EXAM_YEAR,EXAM_DOMAIN_NUM,TOTAL_NUM,EXAM_TOTAL_NUM,PASS_NUM,NO_PASS_NUM,TOTAL_SCORE,REFERENCE_DOMAIN_NUM) VALUES("+
                    unitExam.getDOMAIN_CODE() +"," +
                    unitExam.getEXAM_ID() +"," +
                    unitExam.getEXAM_YEAR() +"," +
                    unitExam.getEXAM_DOMAIN_NUM() +"," +
                    unitExam.getTOTAL_NUM() +"," +
                    unitExam.getEXAM_TOTAL_NUM() +"," +
                    unitExam.getPASS_NUM() +"," +
                    unitExam.getNO_PASS_NUM() +"," +
                    unitExam.getTOTAL_SCORE() +"," +
                    unitExam.getREFERENCE_DOMAIN_NUM() +");";
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
        }
        testReport.deconnSQL();
    }

}
