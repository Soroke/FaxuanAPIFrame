package net.faxuan.tableProject;

/**
 * Created by song on 2017/8/12.
 * 用户积分表实例对象
 */
public class UserPoint {
    public String getUSER_ACCOUNT() {
        return USER_ACCOUNT;
    }

    public void setUSER_ACCOUNT(String USER_ACCOUNT) {
        this.USER_ACCOUNT = USER_ACCOUNT;
    }

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

    public int getEXT_1() {
        return EXT_1;
    }

    public void setEXT_1(int EXT_1) {
        this.EXT_1 = EXT_1;
    }


    private String USER_ACCOUNT;
    private String DOMAIN_CODE;
    private int LPOINT;
    private int IPOINT;
    private int SPOINT;
    private int EPOINT;
    private int APOINT;
    private int PPOINT;
    private int TPOINT;
    private int EXT_1;

    @Override
    public String toString() {
        return "用户账户" + USER_ACCOUNT + "\t单位编号:" + DOMAIN_CODE + "\t登陆积分:" + LPOINT + "\t修改个人信息积分:" + IPOINT + "\t学习积分:" + SPOINT +
                "\t做练习题积分:" + EPOINT + "\t活动积分:" + APOINT + "\t门户其它积分:" + PPOINT + "\t总积分:" + TPOINT + "\t积分规则总分：" + EXT_1;
    }
}
