package net.faxuan.util;

import net.faxuan.data.SourceType;

import java.io.IOException;
import java.util.*;

/**
 * Created by song on 2017/7/25.
 */
public class DataSource {
    private Properties properties = new Properties();
    private SourceType sourceType;
    private Map<Object,Object> dbinfo = new HashMap<Object,Object>();

    public DataSource(SourceType sourceType) {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sourceType = sourceType;
    }

    public Map<Object,Object> getDBInfo() {
        dbinfo.put("driver",getPropertie("driver"));
        switch (sourceType) {
            case SOURSE1:
                dbinfo.put("url",getPropertie("db1_url"));
                dbinfo.put("user",getPropertie("db1_username"));
                dbinfo.put("password",getPropertie("db1_password"));
                break;
            case SOURSE2:
                dbinfo.put("url",getPropertie("db2_url"));
                dbinfo.put("user",getPropertie("db2_username"));
                dbinfo.put("password",getPropertie("db2_password"));
                break;
            case SOURSE3:
                dbinfo.put("url",getPropertie("db3_url"));
                dbinfo.put("user",getPropertie("db3_username"));
                dbinfo.put("password",getPropertie("db3_password"));
                break;
            case SOURSE4:
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
}
