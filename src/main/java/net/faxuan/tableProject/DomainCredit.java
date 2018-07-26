package net.faxuan.tableProject;

/**
 * Created by song on 2017/8/12.
 * 单位学分表实例对象
 */
public class DomainCredit {
    public String getDOMAIN_CODE() {
        return DOMAIN_CODE;
    }

    public void setDOMAIN_CODE(String DOMAIN_CODE) {
        this.DOMAIN_CODE = DOMAIN_CODE;
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

    public int getPASS_NUM() {
        return PASS_NUM;
    }

    public void setPASS_NUM(int PASS_NUM) {
        this.PASS_NUM = PASS_NUM;
    }

    public int getNO_PASS_NUM() {
        return NO_PASS_NUM;
    }

    public void setNO_PASS_NUM(int NO_PASS_NUM) {
        this.NO_PASS_NUM = NO_PASS_NUM;
    }

    public int getNO_JOIN_NUM() {
        return NO_JOIN_NUM;
    }

    public void setNO_JOIN_NUM(int NO_JOIN_NUM) {
        this.NO_JOIN_NUM = NO_JOIN_NUM;
    }

    public int getTOTAL_NUM() {
        return TOTAL_NUM;
    }

    public void setTOTAL_NUM(int TOTAL_NUM) {
        this.TOTAL_NUM = TOTAL_NUM;
    }

    public double getPASS_RATE() {
        return PASS_RATE;
    }

    public void setPASS_RATE(double PASS_RATE) {
        this.PASS_RATE = PASS_RATE;
    }

    public double getAVG_SCORE() {
        return AVG_SCORE;
    }

    public void setAVG_SCORE(double AVG_SCORE) {
        this.AVG_SCORE = AVG_SCORE;
    }

    public double getAVG_CREDIT_SCORE() {
        return AVG_CREDIT_SCORE;
    }

    public void setAVG_CREDIT_SCORE(double AVG_CREDIT_SCORE) {
        this.AVG_CREDIT_SCORE = AVG_CREDIT_SCORE;
    }

    private String DOMAIN_CODE;
    private String EXAM_ID;
    private String EXAM_YEAR;
    private int PASS_NUM;
    private int NO_PASS_NUM;
    private int NO_JOIN_NUM;
    private int TOTAL_NUM;
    private double PASS_RATE;
    private double AVG_SCORE;
    private double AVG_CREDIT_SCORE;

    @Override
    public String toString() {
        return "单位编号" + DOMAIN_CODE + "\t考试ID:" + EXAM_ID + "\t考试所属年份:" + EXAM_YEAR + "\t及格人数:" + PASS_NUM +
                "\t未及格人数:" + NO_PASS_NUM + "\t未参加的人数:" + NO_JOIN_NUM + "\t总人数:" + TOTAL_NUM +
                "\t及格率:" + PASS_RATE + "\t平均分:" + AVG_SCORE + "\t学分平均分:" + AVG_CREDIT_SCORE;
    }
}
