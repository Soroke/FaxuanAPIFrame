package net.faxuan.tableProject;
/**
 * Created by song on 2017/8/12.
 * 单位积分表实例对象
 */
public class DomainPoint {
    public String getDOMAIN_CODE() {
        return DOMAIN_CODE;
    }

    public void setDOMAIN_CODE(String DOMAIN_CODE) {
        this.DOMAIN_CODE = DOMAIN_CODE;
    }

    public int getLPOINT() {
        return LPOINT;
    }

    public void setLPOINT(int LPOINT) {
        this.LPOINT = LPOINT;
    }

    public int getIPOINT() {
        return IPOINT;
    }

    public void setIPOINT(int IPOINT) {
        this.IPOINT = IPOINT;
    }

    public int getSPOINT() {
        return SPOINT;
    }

    public void setSPOINT(int SPOINT) {
        this.SPOINT = SPOINT;
    }

    public int getEPOINT() {
        return EPOINT;
    }

    public void setEPOINT(int EPOINT) {
        this.EPOINT = EPOINT;
    }

    public int getAPOINT() {
        return APOINT;
    }

    public void setAPOINT(int APOINT) {
        this.APOINT = APOINT;
    }

    public int getPPOINT() {
        return PPOINT;
    }

    public void setPPOINT(int PPOINT) {
        this.PPOINT = PPOINT;
    }

    public int getTPOINT() {
        return TPOINT;
    }

    public void setTPOINT(int TPOINT) {
        this.TPOINT = TPOINT;
    }

    public float getAVG_POINT() {
        return AVG_POINT;
    }

    public void setAVG_POINT(float AVG_POINT) {
        this.AVG_POINT = AVG_POINT;
    }

    private String DOMAIN_CODE;
    private int LPOINT;
    private int IPOINT;
    private int SPOINT;
    private int EPOINT;
    private int APOINT;
    private int PPOINT;
    private int TPOINT;
    private float AVG_POINT;

    @Override
    public String toString() {
        return "单位编号:" + DOMAIN_CODE + "\t登陆积分:" + LPOINT + "\t修改个人信息积分:" + IPOINT + "\t学习积分:" + SPOINT +
                "\t做练习题积分:" + EPOINT + "\t活动积分:" + APOINT + "\t门户其它积分:" + PPOINT + "\t单位总积分:" + TPOINT +
                "\t平均积分:" + AVG_POINT;
    }
}
