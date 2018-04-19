package com.evan.chat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午7:59
 */
public class PropertiesUtils {

    private static PropertiesUtils INSTANCE;

    private Properties properties;

    private PropertiesUtils() {

    }

    public static PropertiesUtils getInstance() {
        if (INSTANCE == null){
            INSTANCE = new PropertiesUtils();
        }
        return INSTANCE;
    }

    public void setPath(InputStream path) throws IOException {
        properties = new Properties();
        properties.load(path);
        path.close();
    }

    public String getIP(){
        return properties.getProperty("ip");
    }

    public String getProperty(String key, boolean isMine) {
        if (isMine){
            return properties.getProperty("protocol")+
                    properties.getProperty("ip")+
                    properties.getProperty("host")+
                    properties.getProperty(key);
        }
        return properties.getProperty(key);
    }
}
