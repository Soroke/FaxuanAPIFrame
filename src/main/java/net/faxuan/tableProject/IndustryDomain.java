package net.faxuan.tableProject;

/**
 * Created by song on 2018/8/16.
 */
public class IndustryDomain {

    private String ID;
    private String DOMAIN_CODE;
    private String PARENT_CODE;
    private String BASE_DOMAIN_CODE;
    private String UNIT_ID;
    private String AREA_CODE;
    private String DOMAIN_NAME;
    private String DOMAIN_TYPE;
    private String LINK_MAN;
    private String PHONE;
    private String ADDRESS;
    private String STATUS;
    private String CREATE_TIME;
    private String UPDATE_TIME;
    private String USER_ACCOUNT;
    private String EXT_1;
    private String EXT_2;
    private String EXT_3;
    private String EXT_4;
    private String EXT_5;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDOMAIN_CODE() {
        return DOMAIN_CODE;
    }

    public void setDOMAIN_CODE(String DOMAIN_CODE) {
        this.DOMAIN_CODE = DOMAIN_CODE;
    }

    public String getPARENT_CODE() {
        return PARENT_CODE;
    }

    public void setPARENT_CODE(String PARENT_CODE) {
        this.PARENT_CODE = PARENT_CODE;
    }

    public String getBASE_DOMAIN_CODE() {
        return BASE_DOMAIN_CODE;
    }

    public void setBASE_DOMAIN_CODE(String BASE_DOMAIN_CODE) {
        this.BASE_DOMAIN_CODE = BASE_DOMAIN_CODE;
    }

    public String getUNIT_ID() {
        return UNIT_ID;
    }

    public void setUNIT_ID(String UNIT_ID) {
        this.UNIT_ID = UNIT_ID;
    }

    public String getAREA_CODE() {
        return AREA_CODE;
    }

    public void setAREA_CODE(String AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getDOMAIN_NAME() {
        return DOMAIN_NAME;
    }

    public void setDOMAIN_NAME(String DOMAIN_NAME) {
        this.DOMAIN_NAME = DOMAIN_NAME;
    }

    public String getDOMAIN_TYPE() {
        return DOMAIN_TYPE;
    }

    public void setDOMAIN_TYPE(String DOMAIN_TYPE) {
        this.DOMAIN_TYPE = DOMAIN_TYPE;
    }

    public String getLINK_MAN() {
        return LINK_MAN;
    }

    public void setLINK_MAN(String LINK_MAN) {
        this.LINK_MAN = LINK_MAN;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public String getUSER_ACCOUNT() {
        return USER_ACCOUNT;
    }

    public void setUSER_ACCOUNT(String USER_ACCOUNT) {
        this.USER_ACCOUNT = USER_ACCOUNT;
    }

    public String getEXT_1() {
        return EXT_1;
    }

    public void setEXT_1(String EXT_1) {
        this.EXT_1 = EXT_1;
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

    @Override
    public String toString() {
        return ID + "\t" + DOMAIN_CODE + "\t" + PARENT_CODE + "\t" + BASE_DOMAIN_CODE + "\t" + UNIT_ID + "\t" + AREA_CODE + "\t" + DOMAIN_NAME + "\t" + DOMAIN_TYPE + "\t" + LINK_MAN + "\t" + PHONE + "\t" + ADDRESS + "\t" + STATUS + "\t" + CREATE_TIME + "\t" + UPDATE_TIME + "\t" + USER_ACCOUNT + "\t" + EXT_1 + "\t" + EXT_2 + "\t" + EXT_3 + "\t" + EXT_4 + "\t" + EXT_5;
    }
}
