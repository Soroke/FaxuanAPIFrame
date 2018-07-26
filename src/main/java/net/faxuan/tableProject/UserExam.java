package net.faxuan.tableProject;

/**
 * Created by song on 2017/7/28.
 * 用户考试表实例对象
 */
public class UserExam {
    /**
     * 数据库列属性
     */
    private String USER_ACCOUNT;
    private String USER_NAME;
    private String DOMAIN_CODE;
    private String RANK_ID;
    private String EXAM_ID;
    private String EXAM_YEAR;
    private double EXAM_SCORE;
    private String EXAM_STATUS;
    private String EXT_2;
    private String EXT_3;
    private String EXT_4;
    private String EXT_5;
    private String EXAM_NAME;
    private String EXAM_BEGIN_TIME;


    public String getUSER_ACCOUNT() {
        return USER_ACCOUNT;
    }

    public void setUSER_ACCOUNT(String USER_ACCOUNT) {
        this.USER_ACCOUNT = USER_ACCOUNT;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getDOMAIN_CODE() {
        return DOMAIN_CODE;
    }

    public void setDOMAIN_CODE(String DOMAIN_CODE) {
        this.DOMAIN_CODE = DOMAIN_CODE;
    }

    public String getRANK_ID() {
        return RANK_ID;
    }

    public void setRANK_ID(String RANK_ID) {
        this.RANK_ID = RANK_ID;
    }

    public String getEXAM_ID() {
        return EXAM_ID;
    }

    public void setEXAM_ID(String EXAM_ID) {
        this.EXAM_ID = EXAM_ID;
    }

    public String getEXAM_YEAR() {
        return EXAM_YEAR;
    }

    public void setEXAM_YEAR(String EXAM_YEAR) {
        this.EXAM_YEAR = EXAM_YEAR;
    }

    public double getEXAM_SCORE() {
        return EXAM_SCORE;
    }

    public void setEXAM_SCORE(double EXAM_SCORE) {
        this.EXAM_SCORE = EXAM_SCORE;
    }

    public String getEXAM_STATUS() {
        return EXAM_STATUS;
    }

    public void setEXAM_STATUS(String EXAM_STATUS) {
        this.EXAM_STATUS = EXAM_STATUS;
    }

    public String getEXT_2() {
        return EXT_2;
    }

    public void setEXT_2(String EXT_2) {
        this.EXT_2 = EXT_2;
    }

    public String getEXT_3() {
        return EXT_3;
    }

    public void setEXT_3(String EXT_3) {
        this.EXT_3 = EXT_3;
    }

    public String getEXT_4() {
        return EXT_4;
    }

    public void setEXT_4(String EXT_4) {
        this.EXT_4 = EXT_4;
    }

    public String getEXT_5() {
        return EXT_5;
    }

    public void setEXT_5(String EXT_5) {
        this.EXT_5 = EXT_5;
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

    @Override
    public String toString() {
        return "用户账号：" + USER_ACCOUNT + "\t用户名称：" + USER_NAME + "\t所属单位编号：" + DOMAIN_CODE + "\t职务级别：" + RANK_ID +
                "\t考试id：" + EXAM_ID + "\t考试名称：" + EXAM_NAME + "\t考试所属年份：" + EXAM_YEAR + "\t考试开始时间：" + EXAM_BEGIN_TIME +
                "\t考试分数：" + EXAM_SCORE + "\t考试状态：" + EXAM_STATUS;
    }
}
