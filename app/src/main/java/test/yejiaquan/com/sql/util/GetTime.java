package test.yejiaquan.com.sql.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yejiaquan on 16/7/15.
 */
public class GetTime {
    public static String getFormatTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }
}
