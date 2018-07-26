package net.faxuan.util;

import net.faxuan.tableProject.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2017/8/7.
 * 从数据库获取数据
 */
public class GetData {




    /**
     * 从report库中获取用户考试信息集合
     * @param examID
     * @param dataBase
     * @return
     */
    public static List<UserExam> getUserExamInfo(String examID,DataBase dataBase,String tableName){
        List<UserExam> userExams = new ArrayList<UserExam>();
        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM " + tableName + " WHERE EXAM_ID IN(" + examID + ");");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                UserExam userExam = new UserExam();
                userExam.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                userExam.setUSER_NAME(resultSet.getString("USER_NAME"));
                userExam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                userExam.setRANK_ID(resultSet.getString("RANK_ID"));
                userExam.setEXAM_NAME(resultSet.getString("EXAM_NAME"));
                userExam.setEXAM_ID(resultSet.getString("EXAM_ID"));
                userExam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                userExam.setEXAM_BEGIN_TIME(resultSet.getString("EXAM_BEGIN_TIME"));
                userExam.setEXAM_SCORE(resultSet.getInt("EXAM_SCORE"));
                userExam.setEXAM_STATUS(resultSet.getString("EXAM_STATUS"));
                userExams.add(userExam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExams;
    }


    /**
     * report库中获取单位考试数据
     * @param examIds
     * @param dataBase
     * @param tableName
     * @return
     */
    public static List<DomainExam> getDomainExamInfo(String examIds,DataBase dataBase,String tableName) {
        List<DomainExam> domainExams = new ArrayList<DomainExam>();

        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "` WHERE EXAM_ID IN("+examIds+");");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                DomainExam domainExam = new DomainExam();
                domainExam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                domainExam.setEXAM_ID(resultSet.getString("EXAM_ID"));
                domainExam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                domainExam.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                domainExam.setAVG_SCORE(resultSet.getDouble("AVG_SCORE"));
                domainExam.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                domainExam.setPASS_RATE(resultSet.getDouble("PASS_RATE"));
                domainExam.setNO_JOIN_NUM(resultSet.getInt("NO_JOIN_NUM"));
                domainExam.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                domainExams.add(domainExam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return domainExams;
    }

    /**
     * 获取考试汇总信息
     * @param dataBase
     * @param tableName
     * @return
     */
    public static List<UnitExam> getUnitExamInfo(DataBase dataBase,String tableName) {
        List<UnitExam> unitExams = new ArrayList<UnitExam>();

        List<Exam> exams = ConfirmExecutionData.getExams();
        for (Exam exam:exams) {
            //获取考试发布的单位编码
            String domainCode = exam.getTARGET_DOMAIN_CODE();

            for (int i=0;i<6;i++) {
                if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals("000")) {
                    domainCode = domainCode.substring(0,domainCode.length()-3);
                }
            }

            ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "` WHERE DOMAIN_CODE='" + domainCode  + "' OR DOMAIN_CODE LIKE '" + domainCode + "%' AND EXAM_ID=" + exam.getID() + ";");

            try {
                resultSet.last();
                if (resultSet.getRow() == 0) {
                    return null;
                }
                resultSet.beforeFirst();

                while (resultSet.next()) {
                    UnitExam unitExam = new UnitExam();
                    unitExam.setREFERENCE_DOMAIN_NUM(resultSet.getInt("REFERENCE_DOMAIN_NUM"));
                    unitExam.setEXAM_DOMAIN_NUM(resultSet.getInt("EXAM_DOMAIN_NUM"));
                    unitExam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                    unitExam.setEXAM_ID(resultSet.getString("EXAM_ID"));
                    unitExam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                    unitExam.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                    unitExam.setEXAM_TOTAL_NUM(resultSet.getInt("EXAM_TOTAL_NUM"));
                    unitExam.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                    unitExam.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                    unitExam.setTOTAL_SCORE(resultSet.getInt("TOTAL_SCORE"));
                    unitExams.add(unitExam);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return unitExams;
    }

    /**
     * report库中获取用户积分数据
     * @return
     */
    public static List<UserPoint> getUserPointInfo(String domainCodes,DataBase dataBase,String tableName) {
        List<UserPoint> userPoints = new ArrayList<UserPoint>();

        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "` WHERE DOMAIN_CODE IN(" + domainCodes + ");");
        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
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
                userPoint.setEXT_1(resultSet.getInt("EXT_1"));
                userPoints.add(userPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPoints;
    }

    /**
     * report库中获取单位的积分信息
     * @param domainCodes
     * @param dataBase
     * @param tableName
     * @return
     */
    public static List<DomainPoint> getDomainPointInfo(String domainCodes,DataBase dataBase,String tableName) {
        List<DomainPoint> domainPoints = new ArrayList<DomainPoint>();

        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "`  WHERE DOMAIN_CODE IN(" + domainCodes + ");");

        try {
            resultSet.last();
            if (resultSet.getRow() == 0 ) {
                return null;
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
        return domainPoints;
    }

    /**
     * report库获取用户学分信息
     * @param domainCodes
     * @param examIds
     * @param dataBase
     * @param tableName
     * @return
     */
    public static List<UserCredit> getUserCreditInfo(String domainCodes,String examIds,DataBase dataBase,String tableName) {
        List<UserCredit> userCredits = new ArrayList<UserCredit>();

        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "` WHERE DOMAIN_CODE IN(" + domainCodes + ") AND EXAM_ID IN(" + examIds + ");");

        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                UserCredit userCredit = new UserCredit();
                userCredit.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                userCredit.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                userCredit.setRANK_ID(resultSet.getString("RANK_ID"));
                userCredit.setEXAM_ID(resultSet.getString("EXAM_ID"));
                userCredit.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                userCredit.setEXAM_SCORE(resultSet.getDouble("EXAM_SCORE"));
                userCredit.setEXAM_STATUS(resultSet.getString("EXAM_STATUS"));
                userCredit.setPOINT_SCORE(resultSet.getDouble("POINT_SCORE"));
                userCredit.setCREDIT_SCORE(resultSet.getDouble("CREDIT_SCORE"));
                userCredit.setUSER_NAME(resultSet.getString("USER_NAME"));
                userCredits.add(userCredit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userCredits;
    }

    /**
     * report库获取单位学分信息
     * @param domainCodes
     * @param examIds
     * @param dataBase
     * @param tableName
     * @return
     */
    public static List<DomainCredit> getDomainCreditInfo(String domainCodes,String examIds,DataBase dataBase,String tableName) {
        List<DomainCredit> domainCredits = new ArrayList<DomainCredit>();

        ResultSet resultSet = dataBase.selectSQL("SELECT * FROM `" + tableName + "` WHERE DOMAIN_CODE IN(" + domainCodes + ") AND EXAM_ID IN(" + examIds + ");");

        try {
            resultSet.last();
            if (resultSet.getRow() == 0) {
                return null;
            }
            resultSet.beforeFirst();

            while (resultSet.next()) {
                DomainCredit domainCredit = new DomainCredit();
                domainCredit.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                domainCredit.setEXAM_ID(resultSet.getString("EXAM_ID"));
                domainCredit.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                domainCredit.setPASS_NUM(resultSet.getInt("PASS_NUM"));
                domainCredit.setNO_PASS_NUM(resultSet.getInt("NO_PASS_NUM"));
                domainCredit.setNO_JOIN_NUM(resultSet.getInt("NO_JOIN_NUM"));
                domainCredit.setTOTAL_NUM(resultSet.getInt("TOTAL_NUM"));
                domainCredit.setPASS_RATE(resultSet.getDouble("PASS_RATE"));
                domainCredit.setAVG_SCORE(resultSet.getDouble("AVG_SCORE"));
                domainCredit.setAVG_CREDIT_SCORE(resultSet.getDouble("AVG_CREDIT_SCORE"));
                domainCredits.add(domainCredit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return domainCredits;
    }

}

