package test.yejiaquan.com.sql.util;

import android.content.Context;
import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class InOutPut {

    //写数据
    public static void writeFile(Context context, String fileName, String writeStr, Integer mode, String flag){
        try{
//            System.out.println(context.toString()+fileName+"--开始写入--"+flag);
            FileOutputStream fout;
            if(mode==null) {
                fout= context.openFileOutput(fileName, Context.MODE_PRIVATE);
            }else{
                fout = context.openFileOutput(fileName, mode);
            }
            byte [] bytes = writeStr.getBytes();
            fout.write(bytes);
            fout.flush();
            fout.close();
            System.out.println(context.toString()+fileName+"--写入成功--"+flag);
        }
        catch(IOException e){
            System.out.println(context.toString()+fileName+"--写入失败--"+flag);
            e.printStackTrace();
        }
    }

    //读数据
    public static String readFile(Context context, String fileName, String flag){
        String res="";
        try{
//            System.out.println(context.toString()+fileName+"--开始读取--"+flag);
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            System.out.println(context.toString()+fileName+"--读取成功--"+flag);
        }
        catch(IOException e){
            System.out.println(context.toString()+fileName+"--读取失败--"+flag);
            e.printStackTrace();
        }
        return res;
    }
}
