package net.faxuan.tableProject;

/**
 * Created by song on 2017/7/28.
 * 考试汇总表实例对象
 */
public class UnitExam {
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

    public int getEXAM_DOMAIN_NUM() {
        return EXAM_DOMAIN_NUM;
    }

    public void setEXAM_DOMAIN_NUM(int EXAM_DOMAIN_NUM) {
        this.EXAM_DOMAIN_NUM = EXAM_DOMAIN_NUM;
    }

    public int getTOTAL_NUM() {
        return TOTAL_NUM;
    }

    public void setTOTAL_NUM(int TOTAL_NUM) {
        this.TOTAL_NUM = TOTAL_NUM;
    }

    public int getEXAM_TOTAL_NUM() {
        return EXAM_TOTAL_NUM;
    }

    public void setEXAM_TOTAL_NUM(int EXAM_TOTAL_NUM) {
        this.EXAM_TOTAL_NUM = EXAM_TOTAL_NUM;
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

    public double getTOTAL_SCORE() {
        return TOTAL_SCORE;
    }

    public void setTOTAL_SCORE(double TOTAL_SCORE) {
        this.TOTAL_SCORE = TOTAL_SCORE;
    }

    public int getREFERENCE_DOMAIN_NUM() {
        return REFERENCE_DOMAIN_NUM;
    }

    public void setREFERENCE_DOMAIN_NUM(int REFERENCE_DOMAIN_NUM) {
        this.REFERENCE_DOMAIN_NUM = REFERENCE_DOMAIN_NUM;
    }

    private String DOMAIN_CODE;
    private String EXAM_ID;
    private String EXAM_YEAR;
    private int EXAM_DOMAIN_NUM;
    private int TOTAL_NUM;
    private int EXAM_TOTAL_NUM;
    private int PASS_NUM;
    private int NO_PASS_NUM;
    private double TOTAL_SCORE;
    private int REFERENCE_DOMAIN_NUM;
    @Override
    public String toString() {
        return "单位编码：" + DOMAIN_CODE + "\t考试ID：" + EXAM_ID + "\t考试年份：" + EXAM_YEAR + "\t应参考单位数量：" + REFERENCE_DOMAIN_NUM +
                "\t实际参考单位数量：" + EXAM_DOMAIN_NUM + "\t总人数：" + TOTAL_NUM + "\t参考总人数：" + EXAM_TOTAL_NUM + "\t通过人数：" + EXAM_TOTAL_NUM +
                "\t未通过人数：" + NO_PASS_NUM + "\t总分" + TOTAL_SCORE;
    }
}
