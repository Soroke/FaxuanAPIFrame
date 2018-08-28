package net.faxuan.util;

import net.faxuan.data.ParamInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by song on 2017/7/25.
 */
public class DataSource {
    private Properties properties = new Properties();
    private SourceType sourceType;
    private Map<Object,Object> dbinfo = new HashMap<Object,Object>();
    private ParamInfo paramInfo = new ParamInfo();
    private String environment = paramInfo.getEnviroment();


    public DataSource(SourceType sourceType) {
        try {
            if (environment.equals("测试环境")) {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("qaDB.properties"));
            } else if(environment.equals("预上线环境")) {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("rcDB.properties"));
            } else if (environment.equals("线上环境")) {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("onlineDB.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sourceType = sourceType;
    }

    public Map<Object,Object> getDBInfo() {
        dbinfo.put("driver",getPropertie("driver"));
        switch (sourceType) {
            case TREPORT:
                dbinfo.put("url",getPropertie("db1_url"));
                dbinfo.put("user",getPropertie("db1_username"));
                dbinfo.put("password",getPropertie("db1_password"));
                break;
            case XFBASE:
                dbinfo.put("url",getPropertie("db2_url"));
                dbinfo.put("user",getPropertie("db2_username"));
                dbinfo.put("password",getPropertie("db2_password"));
                break;
            case XFEXAM:
                dbinfo.put("url",getPropertie("db3_url"));
                dbinfo.put("user",getPropertie("db3_username"));
                dbinfo.put("password",getPropertie("db3_password"));
                break;
            case XFREPORT:
                dbinfo.put("url",getPropertie("db4_url"));
                dbinfo.put("user",getPropertie("db4_username"));
                dbinfo.put("password",getPropertie("db4_password"));
                break;
        }
        return dbinfo;
    }

    private String getPropertie(String key) {
        return properties.getProperty(key);
    }
    public enum SourceType {
        TREPORT, XFBASE, XFEXAM, XFREPORT;
    }
}
