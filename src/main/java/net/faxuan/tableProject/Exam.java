package net.faxuan.tableProject;

/**
 * Created by song on 2017/8/8.
 * 考试实例对象
 */
public class Exam {
    /**
     * 考试表所需信息
     */
    //发布目标单位
    private String TARGET_DOMAIN_CODE;
    //考试名称
    private String EXAM_NAME;
    //考试开始时间
    private String EXAM_BEGIN_TIME;
    //考试年份
    private String EXAM_YEAR;
    //发布到的行业
    private String INDUSTRY_CODES;
    //发布到的职务级别
    private String RANK;
    //是否发布到行业单位
    private String INDUSTRY_DOMAIN_FLAG;
    //考试的类型（主考或补考）
    private String EXAM_TYPE;
    //考试ID
    private String ID;
    //主考ID
    private String TARGET_EXAM_ID;
    //是否计入学分
    private int IS_CREDIT;
    //考试成绩所占比例
    private int PROPORTION;
    //考试总分
    private double EXAM_SCORE;


    public double getEXAM_SCORE() {
        return EXAM_SCORE;
    }

    public void setEXAM_SCORE(double EXAM_SCORE) {
        this.EXAM_SCORE = EXAM_SCORE;
    }

    public int getIS_CREDIT() {
        return IS_CREDIT;
    }

    public void setIS_CREDIT(int IS_CREDIT) {
        this.IS_CREDIT = IS_CREDIT;
    }

    public int getPROPORTION() {
        return PROPORTION;
    }

    public void setPROPORTION(int PROPORTION) {
        this.PROPORTION = PROPORTION;
    }

    public String getTARGET_EXAM_ID() {
        return TARGET_EXAM_ID;
    }

    public void setTARGET_EXAM_ID(String TARGET_EXAM_ID) {
        this.TARGET_EXAM_ID = TARGET_EXAM_ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEXAM_TYPE() {
        return EXAM_TYPE;
    }

    public void setEXAM_TYPE(String EXAM_TYPE) {
        this.EXAM_TYPE = EXAM_TYPE;
    }

    public String getINDUSTRY_DOMAIN_FLAG() {
        return INDUSTRY_DOMAIN_FLAG;
    }

    public void setINDUSTRY_DOMAIN_FLAG(String INDUSTRY_DOMAIN_FLAG) {
        this.INDUSTRY_DOMAIN_FLAG = INDUSTRY_DOMAIN_FLAG;
    }

    public String getTARGET_DOMAIN_CODE() {
        return TARGET_DOMAIN_CODE;
    }

    public void setTARGET_DOMAIN_CODE(String TARGET_DOMAIN_CODE) {
        this.TARGET_DOMAIN_CODE = TARGET_DOMAIN_CODE;
    }

    public String getEXAM_NAME() {
        return EXAM_NAME;
    }

    public void setEXAM_NAME(String EXAM_NAME) {
        this.EXAM_NAME = EXAM_NAME;
    }

    public String getEXAM_BEGIN_TIME() {
        return EXAM_BEGIN_TIME;
    }

    public void setEXAM_BEGIN_TIME(String EXAM_BEGIN_TIME) {
        this.EXAM_BEGIN_TIME = EXAM_BEGIN_TIME;
    }

    public String getEXAM_YEAR() {
        return EXAM_YEAR;
    }

    public void setEXAM_YEAR(String EXAM_YEAR) {
        this.EXAM_YEAR = EXAM_YEAR;
    }

    public String getINDUSTRY_CODES() {
        return INDUSTRY_CODES;
    }

    public void setINDUSTRY_CODES(String INDUSTRY_CODES) {
        this.INDUSTRY_CODES = INDUSTRY_CODES;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }
}
