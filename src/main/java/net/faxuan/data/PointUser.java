package net.faxuan.data;

/**
 * Created by song on 2017/7/28.
 */
public class PointUser {
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

    public int getEXT_1() {
        return EXT_1;
    }

    public void setEXT_1(int EXT_1) {
        this.EXT_1 = EXT_1;
    }



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

    @Override
    public String toString() {
        return USER_ACCOUNT + "," + DOMAIN_CODE + "," + LPOINT + "," + IPOINT + "," + SPOINT + "," + EPOINT + "," + APOINT + "," + PPOINT + "," + TPOINT;
    }
}
