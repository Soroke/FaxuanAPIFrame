package net.faxuan.tableProject;

/**
 * Created by song on 2017/8/8.
 * 考试实例对象
 */
public class Exam {
    //考试ID
    private String ID;
    //考试单位编号
    private String DOMAIN_CODE;
    //考试名称
    private String EXAM_NAME;
    //考试类型
    private String EXAM_TYPE;
    //关联正式考试的ID
    private String TARGET_EXAM_ID;
    //图示路径
    private String EXAM_PICTURE_PATH;
    //考试开始时间
    private String EXAM_BEGIN_TIME;
    //考试结束时间
    private String EXAM_END_TIME;
    //考试时长
    private int EXAM_TIME;
    //考试所需积分
    private int EXAM_NEED_SCORE;
    //考试试卷类型（0固定、1随机）
    private int EXAM_PAPER_TYPE;
    //考试总分
    private Double EXAM_SCORE;
    //考试及格分
    private Double EXAM_PASS_SCORE;
    //参考最大次数
    private int EXAM_COMMIT_NUM;
    //发布状态0未发布，1已发布
    private int EXAM_STATUS;
    //年份
    private String EXAM_YEAR;
    //关联试卷ID
    private String EXAM_PAPER_ID;
    //考试备注
    private String EXAM_DISCRIPTION;
    //修改人
    private String OPERATOR_USER_ACCOUNT;
    //修改时间
    private String OPERATOR_TIME;
    //发布目标单位编号（发布时回填）
    private String TARGET_DOMAIN_CODE;
    //考试发布到单位的父单位code
    private String TARGET_PARENT_CODE;
    //职务级别（发布时回填）
    private String RANK;
    //关联证书
    private String EXAM_DIPLOMA_ID;
    //证书标题
    private String DIPLOMA_NAME;
    //证书背景图片保存位置
    private String DIPLOMA_PICTURE_PATH;
    //行业编码
    private String INDUSTRY_CODES;
    //语言（0:全部,1:汉语,2:维语,3:蒙语,4:哈语）
    private int LANGUAGE;
    //是否发布到行业单位（1.发布到行业单位，0.普法单位）
    private String INDUSTRY_DOMAIN_FLAG;
    //学分证书（0成绩证书，1学分证书）
    private String CREDIT_DIPLOMA_FLAG;
    //成绩计入学分的字段标识（0 是，1否）
    private int IS_CREDIT;
    //成绩所占比例
    private int PROPORTION;
    //备用字段
    private String EXT3;
    //备用字段
    private String EXT4;
    //备用字段
    private String EXT5;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEXAM_BEGIN_TIME() {
        return EXAM_BEGIN_TIME;
    }

    public void setEXAM_BEGIN_TIME(String EXAM_BEGIN_TIME) {
        this.EXAM_BEGIN_TIME = EXAM_BEGIN_TIME;
    }

    public String getDOMAIN_CODE() {
        return DOMAIN_CODE;
    }

    public void setDOMAIN_CODE(String DOMAIN_CODE) {
        this.DOMAIN_CODE = DOMAIN_CODE;
    }

    public String getEXAM_NAME() {
        return EXAM_NAME;
    }

    public void setEXAM_NAME(String EXAM_NAME) {
        this.EXAM_NAME = EXAM_NAME;
    }

    public String getEXAM_TYPE() {
        return EXAM_TYPE;
    }

    public void setEXAM_TYPE(String EXAM_TYPE) {
        this.EXAM_TYPE = EXAM_TYPE;
    }

    public String getTARGET_EXAM_ID() {
        return TARGET_EXAM_ID;
    }

    public void setTARGET_EXAM_ID(String TARGET_EXAM_ID) {
        this.TARGET_EXAM_ID = TARGET_EXAM_ID;
    }

    public String getEXAM_PICTURE_PATH() {
        return EXAM_PICTURE_PATH;
    }

    public void setEXAM_PICTURE_PATH(String EXAM_PICTURE_PATH) {
        this.EXAM_PICTURE_PATH = EXAM_PICTURE_PATH;
    }

    public String getEXAM_END_TIME() {
        return EXAM_END_TIME;
    }

    public void setEXAM_END_TIME(String EXAM_END_TIME) {
        this.EXAM_END_TIME = EXAM_END_TIME;
    }

    public int getEXAM_TIME() {
        return EXAM_TIME;
    }

    public void setEXAM_TIME(int EXAM_TIME) {
        this.EXAM_TIME = EXAM_TIME;
    }

    public int getEXAM_NEED_SCORE() {
        return EXAM_NEED_SCORE;
    }

    public void setEXAM_NEED_SCORE(int EXAM_NEED_SCORE) {
        this.EXAM_NEED_SCORE = EXAM_NEED_SCORE;
    }

    public int getEXAM_PAPER_TYPE() {
        return EXAM_PAPER_TYPE;
    }

    public void setEXAM_PAPER_TYPE(int EXAM_PAPER_TYPE) {
        this.EXAM_PAPER_TYPE = EXAM_PAPER_TYPE;
    }

    public Double getEXAM_SCORE() {
        return EXAM_SCORE;
    }

    public void setEXAM_SCORE(Double EXAM_SCORE) {
        this.EXAM_SCORE = EXAM_SCORE;
    }

    public Double getEXAM_PASS_SCORE() {
        return EXAM_PASS_SCORE;
    }

    public void setEXAM_PASS_SCORE(Double EXAM_PASS_SCORE) {
        this.EXAM_PASS_SCORE = EXAM_PASS_SCORE;
    }

    public int getEXAM_COMMIT_NUM() {
        return EXAM_COMMIT_NUM;
    }

    public void setEXAM_COMMIT_NUM(int EXAM_COMMIT_NUM) {
        this.EXAM_COMMIT_NUM = EXAM_COMMIT_NUM;
    }

    public int getEXAM_STATUS() {
        return EXAM_STATUS;
    }

    public void setEXAM_STATUS(int EXAM_STATUS) {
        this.EXAM_STATUS = EXAM_STATUS;
    }

    public String getEXAM_YEAR() {
        return EXAM_YEAR;
    }

    public void setEXAM_YEAR(String EXAM_YEAR) {
        this.EXAM_YEAR = EXAM_YEAR;
    }

    public String getEXAM_PAPER_ID() {
        return EXAM_PAPER_ID;
    }

    public void setEXAM_PAPER_ID(String EXAM_PAPER_ID) {
        this.EXAM_PAPER_ID = EXAM_PAPER_ID;
    }

    public String getEXAM_DISCRIPTION() {
        return EXAM_DISCRIPTION;
    }

    public void setEXAM_DISCRIPTION(String EXAM_DISCRIPTION) {
        this.EXAM_DISCRIPTION = EXAM_DISCRIPTION;
    }

    public String getOPERATOR_USER_ACCOUNT() {
        return OPERATOR_USER_ACCOUNT;
    }

    public void setOPERATOR_USER_ACCOUNT(String OPERATOR_USER_ACCOUNT) {
        this.OPERATOR_USER_ACCOUNT = OPERATOR_USER_ACCOUNT;
    }

    public String getOPERATOR_TIME() {
        return OPERATOR_TIME;
    }

    public void setOPERATOR_TIME(String OPERATOR_TIME) {
        this.OPERATOR_TIME = OPERATOR_TIME;
    }

    public String getTARGET_DOMAIN_CODE() {
        return TARGET_DOMAIN_CODE;
    }

    public void setTARGET_DOMAIN_CODE(String TARGET_DOMAIN_CODE) {
        this.TARGET_DOMAIN_CODE = TARGET_DOMAIN_CODE;
    }

    public String getTARGET_PARENT_CODE() {
        return TARGET_PARENT_CODE;
    }

    public void setTARGET_PARENT_CODE(String TARGET_PARENT_CODE) {
        this.TARGET_PARENT_CODE = TARGET_PARENT_CODE;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getEXAM_DIPLOMA_ID() {
        return EXAM_DIPLOMA_ID;
    }

    public void setEXAM_DIPLOMA_ID(String EXAM_DIPLOMA_ID) {
        this.EXAM_DIPLOMA_ID = EXAM_DIPLOMA_ID;
    }

    public String getDIPLOMA_NAME() {
        return DIPLOMA_NAME;
    }

    public void setDIPLOMA_NAME(String DIPLOMA_NAME) {
        this.DIPLOMA_NAME = DIPLOMA_NAME;
    }

    public String getDIPLOMA_PICTURE_PATH() {
        return DIPLOMA_PICTURE_PATH;
    }

    public void setDIPLOMA_PICTURE_PATH(String DIPLOMA_PICTURE_PATH) {
        this.DIPLOMA_PICTURE_PATH = DIPLOMA_PICTURE_PATH;
    }

    public String getINDUSTRY_CODES() {
        return INDUSTRY_CODES;
    }

    public void setINDUSTRY_CODES(String INDUSTRY_CODES) {
        this.INDUSTRY_CODES = INDUSTRY_CODES;
    }

    public int getLANGUAGE() {
        return LANGUAGE;
    }

    public void setLANGUAGE(int LANGUAGE) {
        this.LANGUAGE = LANGUAGE;
    }

    public String getINDUSTRY_DOMAIN_FLAG() {
        return INDUSTRY_DOMAIN_FLAG;
    }

    public void setINDUSTRY_DOMAIN_FLAG(String INDUSTRY_DOMAIN_FLAG) {
        this.INDUSTRY_DOMAIN_FLAG = INDUSTRY_DOMAIN_FLAG;
    }

    public String getCREDIT_DIPLOMA_FLAG() {
        return CREDIT_DIPLOMA_FLAG;
    }

    public void setCREDIT_DIPLOMA_FLAG(String CREDIT_DIPLOMA_FLAG) {
        this.CREDIT_DIPLOMA_FLAG = CREDIT_DIPLOMA_FLAG;
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

    public String getEXT3() {
        return EXT3;
    }

    public void setEXT3(String EXT3) {
        this.EXT3 = EXT3;
    }

    public String getEXT4() {
        return EXT4;
    }

    public void setEXT4(String EXT4) {
        this.EXT4 = EXT4;
    }

    public String getEXT5() {
        return EXT5;
    }

    public void setEXT5(String EXT5) {
        this.EXT5 = EXT5;
    }

    @Override
    public String toString() {
        return ID + "\t" + DOMAIN_CODE + "\t" + EXAM_NAME + "\t" + EXAM_TYPE + "\t" + TARGET_EXAM_ID + "\t" + EXAM_PICTURE_PATH + "\t" + EXAM_BEGIN_TIME + "\t" + EXAM_END_TIME + "\t" + EXAM_TIME + "\t" + EXAM_NEED_SCORE + "\t" + EXAM_PAPER_TYPE + "\t" + EXAM_SCORE + "\t" + EXAM_PASS_SCORE + "\t" + EXAM_COMMIT_NUM + "\t" + EXAM_STATUS + "\t" + EXAM_YEAR + "\t" + EXAM_PAPER_ID + "\t" + EXAM_DISCRIPTION + "\t" + OPERATOR_USER_ACCOUNT + "\t" + OPERATOR_TIME + "\t" + TARGET_DOMAIN_CODE + "\t" + TARGET_PARENT_CODE + "\t" + RANK + "\t" + EXAM_DIPLOMA_ID + "\t" + DIPLOMA_NAME + "\t" + DIPLOMA_PICTURE_PATH + "\t" + INDUSTRY_CODES + "\t" + LANGUAGE + "\t" + INDUSTRY_DOMAIN_FLAG + "\t" + CREDIT_DIPLOMA_FLAG + "\t" + IS_CREDIT + "\t" + PROPORTION + "\t" + EXT3 + "\t" + EXT4 + "\t" + EXT5;
    }
}
