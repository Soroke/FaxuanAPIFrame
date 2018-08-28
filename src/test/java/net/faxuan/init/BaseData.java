package net.faxuan.init;

import net.faxuan.data.ParamInfo;
import net.faxuan.exception.CheckException;
import net.faxuan.tableProject.*;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2018/8/21.
 */
public class BaseData extends TestCase{


    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 获取基础数据并将数据存储在临时表中
     */
    public BaseData() {

        /**
         * 创建表
         */
        List<String> tableNames = new ArrayList<String>();
        tableNames.add("report_point_user");
        tableNames.add("report_exam_user");
        tableNames.add("report_point_domain");
        tableNames.add("report_exam_domain");
        tableNames.add("credit_user_report");
        tableNames.add("credit_domain_report");
        super.createTable(tableNames);

        ParamInfo paramInfo = new ParamInfo();
        List<String> ids = paramInfo.getExamIds();
        String examIds = "";
        for (String id:ids) {
            examIds = examIds + id + ",";
        }
        examIds = examIds.substring(0,examIds.length()-1);
        createTempTables();
        geExamInfo(examIds);
    }


    /**
     * 根据传入学法report库中源表名获取表字段，组装建表SQL并在测试report库汇总创建表
     * @param sourceTableName report库中表名称
     * @param newTableName  测试库中表名称
     * @param sourceType  数据源
     */
    public void fromSourceCreateTable(DataSource.SourceType sourceType , String sourceTableName, String newTableName) {
        DataBase sourceDataBase = new DataBase(sourceType);
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        String sql="";
        switch (sourceType) {
            case XFBASE:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='base';";
                break;
            case XFEXAM:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='exam';";
                break;
            case XFREPORT:
                sql = "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT,COLUMN_KEY from information_schema.columns where TABLE_NAME='" + sourceTableName + "' AND TABLE_SCHEMA='report';";
                break;
        }

        ResultSet rs = sourceDataBase.selectSQL(sql);
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
                } else if(type.equals("timestamp") || type.equals("TIMESTAMP")){
                    createTableSQL += "NULL DEFAULT NULL ";
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
        //替换SQL中表名称为用户执行表名称
        createTableSQL = createTableSQL.replace("tableName_placeholder",newTableName);

        //如果表已存在，先删除表然后再创建
        Assert.assertEquals(testReport.insertSQL("DROP TABLE IF EXISTS `" + newTableName + "`;"),true);
        Assert.assertEquals(testReport.insertSQL(createTableSQL),true);

        //关闭数据库链接
        sourceDataBase.deconnSQL();
        testReport.deconnSQL();
    }

    /**
     * 创建临时表
     * 考试信息、用户信息、用户考试成绩、单位信息
     */
    private void createTempTables() {
        //创建临时表
        fromSourceCreateTable(DataSource.SourceType.XFEXAM,"exam_exam","base_exam");
        fromSourceCreateTable(DataSource.SourceType.XFBASE,"base_user","base_user");
        fromSourceCreateTable(DataSource.SourceType.XFEXAM,"exam_exam_result","base_user_exam_result");
        fromSourceCreateTable(DataSource.SourceType.XFBASE,"base_domain","base_domain");
        fromSourceCreateTable(DataSource.SourceType.XFBASE,"base_industry_domain","base_industry_domain");
        fromSourceCreateTable(DataSource.SourceType.XFBASE,"base_point_record","user_point");
        fromSourceCreateTable(DataSource.SourceType.XFREPORT,"report_check_user","user_check");
        /**
         * 为用户考试表和用户积分表添加索引
         */
        DataBase testReport = new DataBase(DataSource.SourceType.TREPORT);
        testReport.updateSQL("ALTER TABLE `user_exam` ADD INDEX USER_ACCOUNT ( `USER_ACCOUNT` );");
        testReport.updateSQL("ALTER TABLE `user_exam2017` ADD INDEX USER_ACCOUNT ( `USER_ACCOUNT` );");
        testReport.updateSQL("ALTER TABLE `user_point` ADD INDEX USER_ACCOUNT ( `USER_ACCOUNT` );");
        testReport.deconnSQL();
    }

    /**
     * 根据传入考试ID，获取考试信息并存储在考试临时表中
     * @param examIds
     */
    private void geExamInfo(String examIds) {
        //连接exam库
        DataBase xfexam =  new DataBase(DataSource.SourceType.XFEXAM);
        DataBase testReport = null;
        /**
         * 记录所有符合条件的主考ID
         */
        String mastExamIds = "";
        //根据传入考试ID获取考试信息
        String sql = "SELECT * FROM exam_exam WHERE ID IN (" + examIds + ") AND EXAM_STATUS = 1 AND EXAM_TYPE = 0 AND STR_TO_DATE(EXAM_END_TIME, '%Y-%m-%d') >= STR_TO_DATE(SYSDATE(), '%Y-%m-%d') AND STR_TO_DATE(EXAM_BEGIN_TIME, '%Y-%m-%d') <= STR_TO_DATE(SYSDATE(), '%Y-%m-%d');";
        ResultSet resultSet = xfexam.selectSQL(sql);
        List<Exam> exams = new ArrayList<Exam>();

        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("考试ID：" + examIds + "或在库里不存在，或未发布，或不在考试时间内，或不是主考ID");
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                Exam exam = new Exam();
                mastExamIds = mastExamIds + resultSet.getString("ID") + ",";
                exam.setID(resultSet.getString("ID"));
                exam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                exam.setEXAM_NAME(resultSet.getString("EXAM_NAME"));
                exam.setEXAM_TYPE(resultSet.getString("EXAM_TYPE"));
                exam.setTARGET_EXAM_ID(resultSet.getString("TARGET_EXAM_ID"));
                exam.setEXAM_PICTURE_PATH(resultSet.getString("EXAM_PICTURE_PATH"));
                exam.setEXAM_BEGIN_TIME(resultSet.getString("EXAM_BEGIN_TIME"));
                exam.setEXAM_END_TIME(resultSet.getString("EXAM_END_TIME"));
                exam.setEXAM_TIME(resultSet.getInt("EXAM_TIME"));
                exam.setEXAM_NEED_SCORE(resultSet.getInt("EXAM_NEED_SCORE"));
                exam.setEXAM_PAPER_TYPE(resultSet.getInt("EXAM_PAPER_TYPE"));
                exam.setEXAM_SCORE(resultSet.getDouble("EXAM_SCORE"));
                exam.setEXAM_PASS_SCORE(resultSet.getDouble("EXAM_PASS_SCORE"));
                exam.setEXAM_COMMIT_NUM(resultSet.getInt("EXAM_COMMIT_NUM"));
                exam.setEXAM_STATUS(resultSet.getInt("EXAM_STATUS"));
                exam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                exam.setEXAM_PAPER_ID(resultSet.getString("EXAM_PAPER_ID"));
                exam.setEXAM_DISCRIPTION(resultSet.getString("EXAM_DISCRIPTION"));
                exam.setOPERATOR_USER_ACCOUNT(resultSet.getString("OPERATOR_USER_ACCOUNT"));
                exam.setOPERATOR_TIME(resultSet.getString("OPERATOR_TIME"));
                exam.setTARGET_DOMAIN_CODE(resultSet.getString("TARGET_DOMAIN_CODE"));
                exam.setTARGET_PARENT_CODE(resultSet.getString("TARGET_PARENT_CODE"));
                exam.setRANK(resultSet.getString("RANK"));
                exam.setEXAM_DIPLOMA_ID(resultSet.getString("EXAM_DIPLOMA_ID"));
                exam.setDIPLOMA_NAME(resultSet.getString("DIPLOMA_NAME"));
                exam.setDIPLOMA_PICTURE_PATH(resultSet.getString("DIPLOMA_PICTURE_PATH"));
                exam.setINDUSTRY_CODES(resultSet.getString("INDUSTRY_CODES"));
                exam.setLANGUAGE(resultSet.getInt("LANGUAGE"));
                exam.setINDUSTRY_DOMAIN_FLAG(resultSet.getString("INDUSTRY_DOMAIN_FLAG"));
                exam.setCREDIT_DIPLOMA_FLAG(resultSet.getString("CREDIT_DIPLOMA_FLAG"));
                exam.setIS_CREDIT(resultSet.getInt("EXT1"));
                exam.setPROPORTION(resultSet.getInt("EXT2"));
                exams.add(exam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfexam.deconnSQL();

        /**
         * 去掉最后的逗号
         */
        mastExamIds = mastExamIds.substring(0,mastExamIds.length()-1);
        /**
         * 获取符合条件的主考的补考考试信息
         */
        List<Exam> makeupExams =  makeupExam(mastExamIds);
        /**
         * 建立新集合将所有主考和补考都放入当前集合用于统计
         * 能看到考试单位、用户信息，以及用户的考试成绩
         */
        List<Exam> allExams = new ArrayList<Exam>();
        for (Exam exam:exams) {
            allExams.add(exam);
        }
        /**
         * 确认补考信息是否为空
         */
        try{
            makeupExams.equals(null);
            for (Exam exam:makeupExams) {
                allExams.add(exam);
            }
        }  catch (NullPointerException e) {
            log.info("考试ID：" + mastExamIds +"中都不包含补考信息，不做补考的处理");
        }



        //连接测试report库
        testReport = new DataBase(DataSource.SourceType.TREPORT);
        for (Exam exam:allExams) {
            String DIPLOMA_PICTURE_PATH = null;
            try {
                if (!exam.getDIPLOMA_PICTURE_PATH().equals("") || exam.getDIPLOMA_PICTURE_PATH() != "") {
                    DIPLOMA_PICTURE_PATH = "'" + exam.getDIPLOMA_PICTURE_PATH() + "'";
                }
            } catch (NullPointerException n) {

            }
            //组装插入SQL
            String examInsertSql = "INSERT INTO base_exam VALUES(" + exam.getID() + ",'" + exam.getDOMAIN_CODE() + "','" +
                    exam.getEXAM_NAME() + "'," + exam.getEXAM_TYPE() + "," + exam.getTARGET_EXAM_ID() + ",'" + exam.getEXAM_PICTURE_PATH() +
                    "','" + exam.getEXAM_BEGIN_TIME() + "','" + exam.getEXAM_END_TIME() + "'," + exam.getEXAM_TIME() + "," + exam.getEXAM_NEED_SCORE() +
                    "," + exam.getEXAM_PAPER_TYPE() + "," + exam.getEXAM_SCORE() + "," + exam.getEXAM_PASS_SCORE() + "," + exam.getEXAM_COMMIT_NUM() +
                    "," + exam.getEXAM_STATUS() + "," + exam.getEXAM_YEAR() + "," + exam.getEXAM_PAPER_ID() + ",'" + exam.getEXAM_DISCRIPTION() +
                    "','" + exam.getOPERATOR_USER_ACCOUNT() + "','" + exam.getOPERATOR_TIME() + "','" + exam.getTARGET_DOMAIN_CODE() + "','" +
                    exam.getTARGET_PARENT_CODE() + "','" + exam.getRANK() + "'," + exam.getEXAM_DIPLOMA_ID() + ",'" + exam.getDIPLOMA_NAME() + "'," +
                    DIPLOMA_PICTURE_PATH + ",'" + exam.getINDUSTRY_CODES() + "'," + exam.getLANGUAGE() + "," + exam.getINDUSTRY_DOMAIN_FLAG() +
                    "," + exam.getCREDIT_DIPLOMA_FLAG() + "," + exam.getIS_CREDIT() + "," + exam.getPROPORTION() + "," + exam.getEXT3() + "," + exam.getEXT4()
                    + "," + exam.getEXT5() + ");";
            //执行插入
            testReport.insertSQL(examInsertSql);
        }
        /**
         * 关闭测试report库连接并调用获取单位、用户、考试成绩数据的方法
         */
        testReport.deconnSQL();
        getDomainUserInfo(allExams);
        getExamResult();
    }

    /**
     * 获取当前考试的补考信息
     * @param examIds
     * @return
     */
    private List<Exam> makeupExam(String examIds) {
        DataBase xfexam =  new DataBase(DataSource.SourceType.XFEXAM);

        String sql = "SELECT * FROM exam_exam WHERE TARGET_EXAM_ID IN(" + examIds + ") AND EXAM_TYPE = 1 AND STR_TO_DATE(EXAM_END_TIME, '%Y-%m-%d') >= STR_TO_DATE(SYSDATE(), '%Y-%m-%d' ) AND STR_TO_DATE(EXAM_BEGIN_TIME, '%Y-%m-%d') <= STR_TO_DATE(SYSDATE(), '%Y-%m-%d' )";
        ResultSet resultSet = xfexam.selectSQL(sql);
        List<Exam> exams = new ArrayList<Exam>();
        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount <= 0) {
                return null;
            }
            Assert.assertEquals(rowCount > 0, true);
            while (resultSet.next()) {
                Exam exam = new Exam();
                exam.setID(resultSet.getString("ID"));
                exam.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                exam.setEXAM_NAME(resultSet.getString("EXAM_NAME"));
                exam.setEXAM_TYPE(resultSet.getString("EXAM_TYPE"));
                exam.setTARGET_EXAM_ID(resultSet.getString("TARGET_EXAM_ID"));
                exam.setEXAM_PICTURE_PATH(resultSet.getString("EXAM_PICTURE_PATH"));
                exam.setEXAM_BEGIN_TIME(resultSet.getString("EXAM_BEGIN_TIME"));
                exam.setEXAM_END_TIME(resultSet.getString("EXAM_END_TIME"));
                exam.setEXAM_TIME(resultSet.getInt("EXAM_TIME"));
                exam.setEXAM_NEED_SCORE(resultSet.getInt("EXAM_NEED_SCORE"));
                exam.setEXAM_PAPER_TYPE(resultSet.getInt("EXAM_PAPER_TYPE"));
                exam.setEXAM_SCORE(resultSet.getDouble("EXAM_SCORE"));
                exam.setEXAM_PASS_SCORE(resultSet.getDouble("EXAM_PASS_SCORE"));
                exam.setEXAM_COMMIT_NUM(resultSet.getInt("EXAM_COMMIT_NUM"));
                exam.setEXAM_STATUS(resultSet.getInt("EXAM_STATUS"));
                exam.setEXAM_YEAR(resultSet.getString("EXAM_YEAR"));
                exam.setEXAM_PAPER_ID(resultSet.getString("EXAM_PAPER_ID"));
                exam.setEXAM_DISCRIPTION(resultSet.getString("EXAM_DISCRIPTION"));
                exam.setOPERATOR_TIME(resultSet.getString("OPERATOR_TIME"));
                exam.setOPERATOR_USER_ACCOUNT(resultSet.getString("OPERATOR_USER_ACCOUNT"));
                exam.setTARGET_DOMAIN_CODE(resultSet.getString("TARGET_DOMAIN_CODE"));
                exam.setTARGET_PARENT_CODE(resultSet.getString("TARGET_PARENT_CODE"));
                exam.setRANK(resultSet.getString("RANK"));
                exam.setEXAM_DIPLOMA_ID(resultSet.getString("EXAM_DIPLOMA_ID"));
                exam.setDIPLOMA_NAME(resultSet.getString("DIPLOMA_NAME"));
                exam.setDIPLOMA_PICTURE_PATH(resultSet.getString("DIPLOMA_PICTURE_PATH"));
                exam.setINDUSTRY_CODES(resultSet.getString("INDUSTRY_CODES"));
                exam.setLANGUAGE(resultSet.getInt("LANGUAGE"));
                exam.setINDUSTRY_DOMAIN_FLAG(resultSet.getString("INDUSTRY_DOMAIN_FLAG"));
                exam.setCREDIT_DIPLOMA_FLAG(resultSet.getString("CREDIT_DIPLOMA_FLAG"));
                exam.setIS_CREDIT(resultSet.getInt("EXT1"));
                exam.setPROPORTION(resultSet.getInt("EXT2"));
                exams.add(exam);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    /**
     * 已有临时表base_exam的情况下
     * 统计所有能看到考试的单位信息,包括普法普通单位和行业单位并将信息存储在临时表中（base_domain）
     * 根据获取的单位信息，获取单位行业对应表，并存储在临时表base_domain_industry中
     * 根据获取的单位信息，所有能看到考试的用户信息，并存储在临时表base_user中
     */
    private void getDomainUserInfo(List<Exam> exams) {

        /**
         * 连接学法base库和测试report库
         */
        DataBase xfbase =  null;

        //单位信息
        List<Domain> domains = new ArrayList<Domain>();
        List<IndustryDomain> industryDomains = new ArrayList<IndustryDomain>();
        /**
         * 记录用户信息
         */
        List<User> users = new ArrayList<User>();

        for (Exam exam:exams) {
            xfbase =  new DataBase(DataSource.SourceType.XFBASE);
            ResultSet resultSet = null;
            String industryCode = exam.getINDUSTRY_CODES();
            String domainCode = exam.getTARGET_DOMAIN_CODE();
            String sql = "";
            //记录所有能看到该考试的单位编码
            String domainCodes = "";
            if (domainCode.length() == 15) {
                if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals("000")) {
                    domainCode = splitDomainCode(domainCode);
                    if (industryCode.contains(";")) {
                        String[] ioc = industryCode.split(";");
                        industryCode = "";
                        for (String i : ioc) {
                            industryCode += (i + ",");
                        }
                        industryCode = industryCode.substring(0, industryCode.length() - 1);
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else if (!industryCode.equals("00000000")) {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + domainCode + "%' AND DOMAIN_TYPE='1'))";
                        resultSet = xfbase.selectSQL(sql);
                    }
                }else {
                    if (industryCode.contains(";")) {
                        String[] ioc = industryCode.split(";");
                        industryCode = "";
                        for (String i : ioc) {
                            industryCode += (i + ",");
                        }
                        industryCode = industryCode.substring(0, industryCode.length() - 1);

                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + domainCode + "' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else if (!industryCode.equals("00000000")) {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + domainCode + "' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + domainCode + "' AND DOMAIN_TYPE='1'))";
                        resultSet = xfbase.selectSQL(sql);
                    }
                }

            } else {
                String baseDomainCode = "";
                ResultSet resultSet1 = xfbase.selectSQL("SELECT * FROM base_industry_domain WHERE DOMAIN_CODE = '"+ domainCode + "'");
                try {
                    resultSet1.last();
                    int rowCount = resultSet1.getRow();
                    resultSet1.beforeFirst();
                    if (rowCount<=0) {
                        log.info("考试ID：" + exam.getID() + "没有单位可以看到该考试");
                        return;
                    }
                    Assert.assertEquals(rowCount>0,true);

                    while (resultSet1.next()) {
                        IndustryDomain industryDomain = new IndustryDomain();
                        industryDomain.setID(resultSet1.getString("ID"));
                        industryDomain.setDOMAIN_CODE(resultSet1.getString("DOMAIN_CODE"));
                        industryDomain.setPARENT_CODE(resultSet1.getString("PARENT_CODE"));
                        baseDomainCode = resultSet1.getString("BASE_DOMAIN_CODE");
                        industryDomain.setBASE_DOMAIN_CODE(baseDomainCode);
                        industryDomain.setUNIT_ID(resultSet1.getString("UNIT_ID"));
                        industryDomain.setAREA_CODE(resultSet1.getString("AREA_CODE"));
                        industryDomain.setDOMAIN_NAME(resultSet1.getString("DOMAIN_NAME"));
                        industryDomain.setDOMAIN_TYPE(resultSet1.getString("DOMAIN_TYPE"));
                        industryDomain.setLINK_MAN(resultSet1.getString("LINK_MAN"));
                        industryDomain.setPHONE(resultSet1.getString("PHONE"));
                        industryDomain.setADDRESS(resultSet1.getString("ADDRESS"));
                        industryDomain.setSTATUS(resultSet1.getString("STATUS"));
                        industryDomain.setCREATE_TIME(resultSet1.getString("CREATE_TIME"));
                        industryDomain.setUPDATE_TIME(resultSet1.getString("UPDATE_TIME"));
                        industryDomain.setUSER_ACCOUNT(resultSet1.getString("USER_ACCOUNT"));
                        industryDomain.setEXT_1(resultSet1.getString("EXT_1"));
                        industryDomain.setEXT_2(resultSet1.getString("EXT_2"));
                        industryDomain.setEXT_3(resultSet1.getString("EXT_3"));
                        industryDomain.setEXT_4(resultSet1.getString("EXT_4"));
                        industryDomain.setEXT_5(resultSet1.getString("EXT_5"));
                        industryDomains.add(industryDomain);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                /**
                 * 断开xfbase连接后重新连接，防止出现长时间连接数据库自动断开连接的错误
                 */
                xfbase.deconnSQL();
                xfbase =  new DataBase(DataSource.SourceType.XFBASE);
                if (baseDomainCode.substring(baseDomainCode.length()-3,baseDomainCode.length()).equals("000")) {
                    baseDomainCode = splitDomainCode(baseDomainCode);
                    if (industryCode.contains(";")) {
                        String[] ioc = industryCode.split(";");
                        industryCode = "";
                        for (String i : ioc) {
                            industryCode += (i + ",");
                        }
                        industryCode = industryCode.substring(0, industryCode.length() - 1);
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + baseDomainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else if (!industryCode.equals("00000000")) {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + baseDomainCode + "%' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE LIKE '" + baseDomainCode + "%' AND DOMAIN_TYPE='1'))";
                        resultSet = xfbase.selectSQL(sql);
                    }
                }else {
                    if (industryCode.contains(";")) {
                        String[] ioc = industryCode.split(";");
                        industryCode = "";
                        for (String i : ioc) {
                            industryCode += (i + ",");
                        }
                        industryCode = industryCode.substring(0, industryCode.length() - 1);

                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + baseDomainCode + "' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else if (!industryCode.equals("00000000")) {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + baseDomainCode + "' AND DOMAIN_TYPE='1') AND INDUSTRY_CODE IN(" + industryCode + "))";
                        resultSet = xfbase.selectSQL(sql);
                    } else {
                        sql = "SELECT * FROM base_domain WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain_industry WHERE DOMAIN_CODE IN(SELECT DOMAIN_CODE FROM base_domain WHERE DOMAIN_CODE = '" + baseDomainCode + "' AND DOMAIN_TYPE='1'))";
                        resultSet = xfbase.selectSQL(sql);
                    }
                }
            }
            try {
                resultSet.last();
                int rowCount = resultSet.getRow();
                resultSet.beforeFirst();
                if (rowCount<=0) {
                    log.info("考试ID：" + exam.getID() + "没有单位可以看到该考试");
                    return;
                }
                Assert.assertEquals(rowCount>0,true);

                while (resultSet.next()) {
                    Domain domain = new Domain();
                    domain.setID(resultSet.getString("ID"));
                    domain.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
                    domain.setPARENT_CODE(resultSet.getString("PARENT_CODE"));
                    domain.setUNIT_ID(resultSet.getString("UNIT_ID"));
                    domain.setAREA_CODE(resultSet.getString("AREA_CODE"));
                    domain.setDOMAIN_NAME(resultSet.getString("DOMAIN_NAME"));
                    domain.setDOMAIN_TYPE(resultSet.getInt("DOMAIN_TYPE"));
                    domain.setLINK_MAN(resultSet.getString("LINK_MAN"));
                    domain.setPHONE(resultSet.getString("PHONE"));
                    domain.setADDRESS(resultSet.getString("ADDRESS"));
                    domain.setPEOPLE_COUNT(resultSet.getInt("PEOPLE_COUNT"));
                    domain.setSTATUS(resultSet.getInt("STATUS"));
                    domain.setSTART_TIME(resultSet.getString("START_TIME"));
                    domain.setUSE_PERIOD(resultSet.getInt("USE_PERIOD"));
                    domain.setUPDATE_TIME(resultSet.getString("UPDATE_TIME"));
                    domain.setUSER_ACCOUNT(resultSet.getString("USER_ACCOUNT"));
                    domain.setFREEZE_TIME(resultSet.getString("FREEZE_TIME"));
                    domain.setFREEZE_OPERATOR(resultSet.getString("FREEZE_OPERATOR"));
                    domain.setEXT_1(resultSet.getString("EXT_1"));
                    domain.setEXT_2(resultSet.getString("EXT_2"));
                    domain.setEXT_3(resultSet.getString("EXT_3"));
                    domain.setEXT_4(resultSet.getString("EXT_4"));
                    domain.setEXT_5(resultSet.getString("EXT_5"));
                    domains.add(domain);
                    //System.out.println(domain);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            /**
             * 断开xfbase连接，防止出现长时间连接数据库自动断开连接的错误
             */
            xfbase.deconnSQL();

            /**
             * 去重普法单位信息
             */
            for (int i=0;i<domains.size()-1;i++) {
                for (int j=domains.size()-1;j>i ;j--) {
                    if (domains.get(j).getID().equals(domains.get(i).getID())){
                        domains.remove(j);
                    }
                }
            }

            /**
             * 去重行业单位信息
             */
            for (int i=0;i<industryDomains.size()-1;i++) {
                for (int j=industryDomains.size()-1;j>i ;j--) {
                    if (industryDomains.get(j).getDOMAIN_CODE().equals(industryDomains.get(i).getDOMAIN_CODE())){
                        industryDomains.remove(j);
                    }
                }
            }

            /**
             * 循环获取所有能看到考试的单位编码
             */
            for (Domain domain:domains) {
                domainCodes = domainCodes + domain.getDOMAIN_CODE() + ",";
            }
            /**
             * 去掉最后一个逗号
             */
            domainCodes = domainCodes.substring(0,domainCodes.length()-1);

            /**
             * 重组考试的RANK供sql使用
             */
            String rankid = exam.getRANK();
            if (rankid.contains(";")) {
                String[] rankIDs = rankid.split(";");
                rankid = "";
                for (String rank:rankIDs) {
                    rankid += ( "'" + rank + "',");
                }
                rankid = rankid.substring(0,rankid.length()-1);
            } else {
                rankid = "'" + rankid + "'";
            }

            List<User> getUsers = getUserInfo(domainCodes,rankid,exam.getID());
            try {
                getUsers.size();
                for (User user:getUsers) {
                    users.add(user);
                }
            } catch (NullPointerException ne) {
            }

        }

        /**
         * 所有用户信息去重
         */
        for (int i=0;i<users.size()-1;i++) {
            for (int j=users.size()-1;j>i ;j--) {
                if (users.get(j).getUser_account().equals(users.get(i).getUser_account()) && users.get(j).getExt_5().equals(users.get(i).getExt_5())){
                    users.remove(j);
                }
            }
        }

        /**
         * 所有普法单位信息去重
         */
        for (int i=0;i<domains.size()-1;i++) {
            for (int j=domains.size()-1;j>i ;j--) {
                if (domains.get(j).getID().equals(domains.get(i).getID())){
                    domains.remove(j);
                }
            }
        }

        /**
         * 去重行业单位信息
         */
        for (int i=0;i<industryDomains.size()-1;i++) {
            for (int j=industryDomains.size()-1;j>i ;j--) {
                if (industryDomains.get(j).getDOMAIN_CODE().equals(industryDomains.get(i).getDOMAIN_CODE())){
                    industryDomains.remove(j);
                }
            }
        }


        //所有单位编码，包括行业单位
        String domainCodeAll = "";
        /**
         * 分别插入普法和行业单位
         */
        DataBase testReport = null;
        String domainsSQL = "INSERT INTO base_domain VALUES";
        int count = 0;
        for (Domain domain:domains) {
            if (count >= 2000) {
                testReport =  new DataBase(DataSource.SourceType.TREPORT);
                domainsSQL = domainsSQL.substring(0,domainsSQL.length()-1);
                domainsSQL += ";";
                testReport.insertSQL(domainsSQL);
                testReport.deconnSQL();
                domainsSQL = "INSERT INTO base_domain VALUES";
                count = 0;
            }
            domainCodeAll = domainCodeAll + domain.getDOMAIN_CODE() + ",";
            String ext2 = null;
            String startTime = null;
            String updateTime = null;
            String freezeTime = null;
            try {
                if (!domain.getEXT_2().equals("") || domain.getEXT_2() != "") {
                    ext2 = "'" + domain.getEXT_2() + "'";
                }
                if (!domain.getSTART_TIME().equals("") || domain.getSTART_TIME() != "") {
                    startTime = "'" + domain.getSTART_TIME() + "'";
                }
                if (!domain.getUPDATE_TIME().equals("") || domain.getUPDATE_TIME() != "") {
                    updateTime = "'" + domain.getUPDATE_TIME() + "'";
                }
                if (!domain.getFREEZE_TIME().equals("") || domain.getFREEZE_TIME() != "") {
                    freezeTime = "'" + domain.getFREEZE_TIME() + "'";
                }
            } catch (NullPointerException n) {

            }


            domainsSQL+="("
                    + domain.getID() + ",'" + domain.getDOMAIN_CODE() + "','" + domain.getPARENT_CODE() +
                    "','" + domain.getUNIT_ID() + "','" + domain.getAREA_CODE() + "','" + domain.getDOMAIN_NAME() + "'," + domain.getDOMAIN_TYPE() +
                    ",'" + domain.getLINK_MAN() + "','" + domain.getPHONE() + "','" + domain.getADDRESS() + "'," + domain.getPEOPLE_COUNT() +
                    "," + domain.getSTATUS() + "," + startTime + "," + domain.getUSE_PERIOD() + "," + updateTime +
                    ",'" + domain.getUSER_ACCOUNT() + "'," + freezeTime + ",'" + domain.getFREEZE_OPERATOR() +
                    "'," + domain.getEXT_1() + "," + ext2 + "," + domain.getEXT_3() + "," + domain.getEXT_4() + "," + domain.getEXT_5() + "),";
            count ++;
        }
        //连接测试report库
        if (count >= 1) {
            testReport =  new DataBase(DataSource.SourceType.TREPORT);
            domainsSQL = domainsSQL.substring(0,domainsSQL.length()-1);
            domainsSQL += ";";
            testReport.insertSQL(domainsSQL);
        }


        /**
         * 组装SQL并插入到测试report库的base_user表中
         */
        testReport.deconnSQL();
        testReport =  new DataBase(DataSource.SourceType.TREPORT);
        String updateUserTableExt = "alter table base_user modify EXT_5 varchar(100) DEFAULT NULL COMMENT ' 预留 ';";
        testReport.updateSQL(updateUserTableExt);
        testReport.deconnSQL();


        /**
         * 组装SQL并插入到测试report库的base_user表中
         */
log.info("全部待插入用户数量为：" + users.size());
        count = 0;
        String usersSQL = "INSERT INTO base_user ( USER_ACCOUNT, user_password, USER_NAME, USER_SEX, USER_BIRTHDAY, USER_EMAIL, USER_PHONE, USER_POST, AREA_CODE, USER_ADDRESS, USER_ID_CARD, USER_TYPE, ROLE_ID, DOMAIN_CODE, RANK_ID, LOCK_FLAG, CREATE_TIME, LAST_LOGIN, UPDATE_TIME, OPERATOR, EXT_1, EXT_2, EXT_3, EXT_4, EXT_5, USER_BINDING_TYPE, USER_QQ, POLITICS_CODE, IMAGE_URL, PCIMAGE_URL ) VALUES";
        for (User user:users) {
            if (count >= 2000) {
                testReport =  new DataBase(DataSource.SourceType.TREPORT);
                usersSQL = usersSQL.substring(0,usersSQL.length()-1);
                usersSQL += ";";
                testReport.insertSQL(usersSQL);
                testReport.deconnSQL();
                count = 0;
                usersSQL = "INSERT INTO base_user ( USER_ACCOUNT, user_password, USER_NAME, USER_SEX, USER_BIRTHDAY, USER_EMAIL, USER_PHONE, USER_POST, AREA_CODE, USER_ADDRESS, USER_ID_CARD, USER_TYPE, ROLE_ID, DOMAIN_CODE, RANK_ID, LOCK_FLAG, CREATE_TIME, LAST_LOGIN, UPDATE_TIME, OPERATOR, EXT_1, EXT_2, EXT_3, EXT_4, EXT_5, USER_BINDING_TYPE, USER_QQ, POLITICS_CODE, IMAGE_URL, PCIMAGE_URL ) VALUES";
            }
            String userBirthday = null;
            String createTime = null;
            String lostLogin = null;
            String updateTime = null;
            String phone = null;
            String post = null;
            String address = null;
            String idCard = null;
            try {
                if (!user.getUser_birthday().equals("") || user.getUser_birthday() != "") {
                    userBirthday = "'" + user.getUser_birthday() + "'";
                }
                if (!user.getCreate_time().equals("") || user.getCreate_time() != "") {
                    createTime = "'" + user.getCreate_time() + "'";
                }
                if (!user.getLast_login().equals("") || user.getLast_login() != "") {
                    lostLogin = "'" + user.getLast_login() + "'";
                }
                if (!user.getUpdate_time().equals("") || user.getUpdate_time() != "") {
                    updateTime = "'" + user.getUpdate_time() + "'";
                }
                if (!user.getUser_phone().equals("") || user.getUser_phone() != "") {
                    phone = "'" + user.getUser_phone() + "'";
                }
                if (!user.getUser_post().equals("") || user.getUser_post() != "") {
                    post = "'" + user.getUser_post() + "'";
                }
                if (!user.getUser_address().equals("") || user.getUser_address() != "") {
                    address = "'" + user.getUser_address() + "'";
                }
                if (!user.getUser_id_card().equals("") || user.getUser_id_card() != "") {
                    idCard = "'" + user.getUser_id_card() + "'";
                }
            } catch (NullPointerException npe) {

            }

            usersSQL += "('"+ user.getUser_account() + "','" + user.getUser_password() + "','" +
                    user.getUser_name() + "'," + user.getUser_sex() + "," + userBirthday + ",'" + user.getUser_email() + "'," + phone +
                    "," + post + "," + user.getArea_code() + "," + address + "," + idCard + "," +
                    user.getUser_type() + ",'" + user.getRole_id() + "','" + user.getDomain_code() + "','" + user.getRank_id() + "'," + user.getLock_flag()
                    + "," + createTime + "," + lostLogin + "," + updateTime + ",'" + user.getOperator() + "'," + user.getExt_1() + "," + user.getExt_2()
                    + "," + user.getExt_3() + "," + user.getExt_4() + "," + user.getExt_5() + "," + user.getUser_binding_type() + ",'" + user.getUser_qq()
                    + "','" + user.getPolitics_code() + "','" + user.getImage_url() + "','" + user.getPcimage_url() + "'),";
            count ++;
        }
System.out.println("SQL拼装完毕");
        if (count >= 1) {
            usersSQL = usersSQL.substring(0,usersSQL.length()-1);
            usersSQL += ";";
            testReport =  new DataBase(DataSource.SourceType.TREPORT);
            testReport.insertSQL(usersSQL);
            testReport.deconnSQL();
        }


        String industryDomainsSQL = "INSERT INTO base_industry_domain VALUES";
        count = 0;
        if (industryDomains.size() > 0) {
            for (IndustryDomain industryDomain : industryDomains) {
                if (count >=2000) {
                    testReport = new DataBase(DataSource.SourceType.TREPORT);
                    industryDomainsSQL = industryDomainsSQL.substring(0, industryDomainsSQL.length() - 1);
                    industryDomainsSQL += ";";
                    testReport.insertSQL(industryDomainsSQL);
                    testReport.deconnSQL();
                    count = 0;
                    industryDomainsSQL = "INSERT INTO base_industry_domain VALUES";
                }
                domainCodeAll = domainCodeAll + industryDomain.getDOMAIN_CODE() + ",";
                industryDomainsSQL += "(" + industryDomain.getID() + ",'" + industryDomain.getDOMAIN_CODE() + "','" +
                        industryDomain.getPARENT_CODE() + "','" + industryDomain.getBASE_DOMAIN_CODE() + "','" + industryDomain.getUNIT_ID() +
                        "','" + industryDomain.getAREA_CODE() + "','" + industryDomain.getDOMAIN_NAME() + "'," + industryDomain.getDOMAIN_TYPE() +
                        ",'" + industryDomain.getLINK_MAN() + "','" + industryDomain.getPHONE() + "','" + industryDomain.getADDRESS() +
                        "'," + industryDomain.getSTATUS() + ",'" + industryDomain.getCREATE_TIME() + "','" + industryDomain.getUPDATE_TIME() +
                        "','" + industryDomain.getUSER_ACCOUNT() + "'," + industryDomain.getEXT_1() + "," + industryDomain.getEXT_2() +
                        "," + industryDomain.getEXT_3() + "," + industryDomain.getEXT_4() + "," + industryDomain.getEXT_5() + "),";
                count ++;
            }
            /**
             * testReport，防止出现长时间连接数据库自动断开连接的错误
             */
            if (count >= 1) {
                testReport = new DataBase(DataSource.SourceType.TREPORT);
                industryDomainsSQL = industryDomainsSQL.substring(0, industryDomainsSQL.length() - 1);
                industryDomainsSQL += ";";
                testReport.insertSQL(industryDomainsSQL);
            }

        }
        /**
         * 断开数据连接testReport，防止出现长时间连接数据库自动断开连接的错误
         */
        testReport.deconnSQL();

//        domainCodeAll=domainCodeAll.substring(0,(domainCodeAll.length()-1));
//        xfbase =  new DataBase(DataSource.SourceType.XFBASE);
//        /**
//         * 获取单位对应的行业并保存在临时表base_domain_industry
//         */
//        ResultSet resultSet=null;
//        resultSet = xfbase.selectSQL("SELECT * FROM `base_domain_industry` WHERE DOMAIN_CODE IN(" + domainCodeAll + ");");
//        List<DomainIndusty> domainIndusties = new ArrayList<DomainIndusty>();
//        try {
//            resultSet.last();
//            int rowCount = resultSet.getRow();
//            if (rowCount<=0) {
//                log.info("传入单位编码没有对应的行业");
//                return;
//            }
//            resultSet.beforeFirst();
//            Assert.assertEquals(rowCount>0,true);
//
//            while (resultSet.next()) {
//                DomainIndusty domainIndusty = new DomainIndusty();
//                domainIndusty.setID(resultSet.getString("ID"));
//                domainIndusty.setDOMAIN_CODE(resultSet.getString("DOMAIN_CODE"));
//                domainIndusty.setINDUSTRY_CODE(resultSet.getString("INDUSTRY_CODE"));
//                domainIndusties.add(domainIndusty);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        testReport =  new DataBase(DataSource.SourceType.TREPORT);
//        for (DomainIndusty domainIndusty:domainIndusties) {
//            String sql ="INSERT INTO base_domain_industry VALUES(" + domainIndusty.getID() + ",'" + domainIndusty.getDOMAIN_CODE() + "','" + domainIndusty.getINDUSTRY_CODE() + "',NULL,NULL,NULL,NULL,NULL)";
//            testReport.insertSQL(sql);
//        }

        /**
         * 断开xfbase连接，防止出现长时间连接数据库自动断开连接的错误
         */
//        xfbase.deconnSQL();
//        testReport.deconnSQL();

    }

    /**
     * 根据单位编码和职务级别或成绩面貌编码 获取用户信息
     * @param domainCodes 单位编码
     * @param rankid 职务级别或政治面貌编码
     * @return 用户信息list集合
     */
    private List<User> getUserInfo(String domainCodes,String rankid,String examId) {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM `base_user` WHERE DOMAIN_CODE IN(" + domainCodes + ") AND USER_TYPE = 1 AND (POLITICS_CODE IN(" + rankid +") OR RANK_ID IN(" + rankid +")) AND LOCK_FLAG = '0';";
        /**
         * 连接base库并查询所有能看到该考试的用户信息
         */
        DataBase xfbase =  new DataBase(DataSource.SourceType.XFBASE);
        ResultSet resultSet = xfbase.selectSQL(sql);

        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                log.error("单位编码：" + domainCodes +"下的所有的用户都无法看到考试信息");
                return null;
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("ID"));
                user.setUser_account(resultSet.getString("USER_ACCOUNT"));
                user.setUser_password(resultSet.getString("user_password"));
                user.setUser_name(resultSet.getString("USER_NAME"));
                user.setUser_sex(resultSet.getInt("USER_SEX"));
                user.setUser_birthday(resultSet.getString("USER_BIRTHDAY"));
                user.setUser_email(resultSet.getString("USER_EMAIL"));
                user.setUser_phone(resultSet.getString("USER_PHONE"));
                user.setUser_post(resultSet.getString("USER_POST"));
                user.setArea_code(resultSet.getString("AREA_CODE"));
                user.setUser_address(resultSet.getString("USER_ADDRESS"));
                user.setUser_id_card(resultSet.getString("USER_ID_CARD"));
                user.setUser_type(resultSet.getInt("USER_TYPE"));
                user.setRole_id(resultSet.getString("ROLE_ID"));
                user.setDomain_code(resultSet.getString("DOMAIN_CODE"));
                user.setRank_id(resultSet.getString("RANK_ID"));
                user.setLock_flag(resultSet.getInt("LOCK_FLAG"));
                user.setCreate_time(resultSet.getString("CREATE_TIME"));
                user.setLast_login(resultSet.getString("LAST_LOGIN"));
                user.setUpdate_time(resultSet.getString("UPDATE_TIME"));
                user.setOperator(resultSet.getString("OPERATOR"));
                user.setExt_1(resultSet.getInt("EXT_1"));
                user.setExt_2(resultSet.getString("EXT_2"));
                user.setExt_3(resultSet.getString("EXT_3"));
                user.setExt_4(resultSet.getString("EXT_4"));
                user.setExt_5(examId);
                user.setUser_binding_type(resultSet.getInt("USER_BINDING_TYPE"));
                user.setUser_qq(resultSet.getString("USER_QQ"));
                user.setPolitics_code(resultSet.getString("POLITICS_CODE"));
                user.setImage_url(resultSet.getString("IMAGE_URL"));
                user.setPcimage_url(resultSet.getString("PCIMAGE_URL"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfbase.deconnSQL();

        return users;
    }

    /**
     * 根据临时表中 用户和考试信息查询exam库考试成绩表
     * 获取用户考试成绩，然后将结果存储到考试结果临时表中base_user_exam_result
     */
    private void getExamResult() {
        /**
         * 连接学法base库
         */
        DataBase testReport =  new DataBase(DataSource.SourceType.TREPORT);
        //记录所有用户账号,和考试ID
        String userAccounts = "";
        List<String> userAccou = new ArrayList<>();
        String exams = "";

        ResultSet resultSet = testReport.selectSQL("SELECT USER_ACCOUNT FROM `base_user`;");
        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("测试库中临时表base_user为空");
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                userAccou.add(resultSet.getString("USER_ACCOUNT"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /**
         * 用户去重
         */
        for (int i=0;i<userAccou.size()-1;i++) {
            for (int j=userAccou.size()-1;j>i ;j--) {
                if (userAccou.get(j).equals(userAccou.get(i))){
                    userAccou.remove(j);
                }
            }
        }

        for (String userAccount:userAccou) {
            userAccounts = userAccounts + userAccount + ",";
        }
        userAccounts = userAccounts.substring(0,userAccounts.length()-1);


        /**
         * 断开测试report库的连接
         */
        testReport.deconnSQL();
        /**
         * 调用获取并插入用户的积分信息
         */
        Assert.assertEquals(getUserPoint(userAccounts),true);

        /**
         * 重新连接测试report库
         */
        testReport =  new DataBase(DataSource.SourceType.TREPORT);

        resultSet = testReport.selectSQL("SELECT ID FROM `base_exam`;");
        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("测试库中临时表base_exam为空");
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                exams = exams + (resultSet.getString("ID")) + ",";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        exams = exams.substring(0,exams.length()-1);

        //断开测试report库连接
        testReport.deconnSQL();
        //连接考试库
        DataBase xfExam =  new DataBase(DataSource.SourceType.XFEXAM);
        //所有考试成绩信息
        List<ExamResult> examResults = new ArrayList<ExamResult>();

        resultSet = xfExam.selectSQL("SELECT * FROM `exam_exam_result` WHERE USER_ACCOUNT IN(" + userAccounts + ") AND EXAM_ID IN(" + exams + ");");
        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                throw new CheckException("临时表中用户没有指定考试的考试成绩");
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                ExamResult examResult = new ExamResult();
                examResult.setId(resultSet.getString("ID"));
                examResult.setUser_account(resultSet.getString("USER_ACCOUNT"));
                examResult.setExam_id(resultSet.getInt("EXAM_ID"));
                examResult.setDomain_code(resultSet.getString("DOMAIN_CODE"));
                examResult.setExam_result_score(resultSet.getDouble("EXAM_RESULT_SCORE"));
                examResult.setIs_pass(resultSet.getInt("IS_PASS"));
                examResult.setExam_join_num(resultSet.getInt("EXAM_JOIN_NUM"));
                examResults.add(examResult);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        xfExam.deconnSQL();


        String examResultSQL="INSERT INTO base_user_exam_result VALUES";
        int count = 0;
        for (ExamResult examResult:examResults) {
            if (count >= 2000) {
                testReport =  new DataBase(DataSource.SourceType.TREPORT);
                examResultSQL = examResultSQL.substring(0,examResultSQL.length()-1);
                examResultSQL += ";";
                testReport.insertSQL(examResultSQL);
                testReport.deconnSQL();
                count = 0;
                examResultSQL="INSERT INTO base_user_exam_result VALUES";
            }
            examResultSQL+="(" + examResult.getId() + ",'" + examResult.getUser_account() + "'," + examResult.getExam_id() +
                    ",'" + examResult.getDomain_code() + "'," + examResult.getExam_result_score() + "," + examResult.getIs_pass() + "," +
                    examResult.getExam_join_num() + "),";
            count ++;
        }
        if (count >= 1) {
            testReport =  new DataBase(DataSource.SourceType.TREPORT);
            examResultSQL = examResultSQL.substring(0,examResultSQL.length()-1);
            examResultSQL += ";";
            testReport.insertSQL(examResultSQL);
            testReport.deconnSQL();
        }
    }

    /**
     * 获取用户积分数据，并将数据插入到测试统计库的用户积分表中
     * @param userAccounts 单位编码
     */
    private boolean getUserPoint(String userAccounts) {
        List<UserPoint> userPoints = new ArrayList<UserPoint>();

        DataBase xfbase = new DataBase(DataSource.SourceType.XFBASE);
        ResultSet resultSet = xfbase.selectSQL("SELECT * FROM `base_point_record` WHERE USER_ACCOUNT IN(" + userAccounts + ");");

        try {
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            if (rowCount<=0) {
                return false;
            }
            Assert.assertEquals(rowCount>0,true);

            while (resultSet.next()) {
                UserPoint userPoint = new UserPoint();
                userPoint.setID(resultSet.getString("ID"));
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
                userPoint.setEXT_2(resultSet.getInt("EXT_2"));
                userPoint.setEXT_3(resultSet.getInt("EXT_3"));
                userPoint.setEXT_4(resultSet.getInt("EXT_4"));
                userPoint.setEXT_5(resultSet.getInt("EXT_5"));
                userPoints.add(userPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //断开学法base库的连接
        xfbase.deconnSQL();

        DataBase testReport = null;
        String insertSQL = "insert into user_point(USER_ACCOUNT,DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT,EXT_1) values";
        int count = 0;
        for (UserPoint userPoint:userPoints) {
            if (count >= 2000) {
                testReport = new DataBase(DataSource.SourceType.TREPORT);
                insertSQL = insertSQL.substring(0,insertSQL.length()-1);
                insertSQL += ";" ;
                Assert.assertEquals(testReport.insertSQL(insertSQL),true);
                testReport.deconnSQL();
                count = 0;
                insertSQL = "insert into user_point(USER_ACCOUNT,DOMAIN_CODE,LPOINT,IPOINT,SPOINT,EPOINT,APOINT,PPOINT,TPOINT,EXT_1) values";
            }
            insertSQL += "('" +
                    userPoint.getUSER_ACCOUNT() + "'," +
                    userPoint.getDOMAIN_CODE() + "," +
                    userPoint.getLPOINT() + "," +
                    userPoint.getIPOINT() + "," +
                    userPoint.getSPOINT() + "," +
                    userPoint.getEPOINT() + "," +
                    userPoint.getAPOINT() + "," +
                    userPoint.getPPOINT() + "," +
                    userPoint.getTPOINT() + "," +
                    userPoint.getEXT_1() + "),";
            count ++;

        }
        //连接测试report库，插入积分信息
        if (count >= 0) {
            testReport = new DataBase(DataSource.SourceType.TREPORT);
            insertSQL = insertSQL.substring(0,insertSQL.length()-1);
            insertSQL += ";" ;
            Assert.assertEquals(testReport.insertSQL(insertSQL),true);
            testReport.deconnSQL();
        }
        return true;
    }


    /**
     * 分割单位编码，如果单位编码为：101001002000000
     * 那么就是返回101001002
     * @param domainCode 传入单位编码
     * @return 返回分割后的单位编码
     */
    private String splitDomainCode(String domainCode) {
        String code = "000";
        if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
            for (int i=0;i<5;i++) {
                if (domainCode.substring(domainCode.length()-3,domainCode.length()).equals(code)) {
                    domainCode = domainCode.substring(0,domainCode.length()-3);
                }
            }
        }
        return domainCode;
    }


}
