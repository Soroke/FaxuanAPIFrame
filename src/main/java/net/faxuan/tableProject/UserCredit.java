package net.faxuan.tableProject;

import net.faxuan.util.DataBase;

/**
 * Created by song on 2017/8/12.
 * 用户学分表实例对象
 */
public class UserCredit {


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

    public double getPOINT_SCORE() {
        return POINT_SCORE;
    }

    public void setPOINT_SCORE(double POINT_SCORE) {
        this.POINT_SCORE = POINT_SCORE;
    }

    public double getCREDIT_SCORE() {
        return CREDIT_SCORE;
    }

    public void setCREDIT_SCORE(double CREDIT_SCORE) {
        this.CREDIT_SCORE = CREDIT_SCORE;
    }

    private String USER_ACCOUNT;
    private String USER_NAME;
    private String DOMAIN_CODE;
    private String RANK_ID;
    private String EXAM_ID;
    private String EXAM_YEAR;
    private double EXAM_SCORE;
    private String EXAM_STATUS;
    private double POINT_SCORE;
    private double CREDIT_SCORE;

    @Override
    public String toString() {
        return "会员账号" + USER_ACCOUNT + "\t用户姓名:" + USER_NAME + "\t所属单位编号:" + DOMAIN_CODE +
                "\t职务级别/政治面貌:" + RANK_ID + "\t考试ID:" + EXAM_ID + "\t考试所属年份:" + EXAM_YEAR +
                "\t考试分数" + EXAM_SCORE + "\t考试状态" + EXAM_STATUS + "\t积分:" + POINT_SCORE +
                "\t学分:" + CREDIT_SCORE;
    }
}
