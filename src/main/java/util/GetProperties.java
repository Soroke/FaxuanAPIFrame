package util;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件（fiddler保存文件的路径）
 * Created by song on 17/5/17.
 */
public class GetProperties {

    java.util.Properties properties = new java.util.Properties();

    public GetProperties(String pripertiesName) {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(pripertiesName + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getPropertie(String key) {
        return properties.getProperty(key);
    }


}

