package test.yejiaquan.com.sql.activity;

import android.os.Handler;
import test.yejiaquan.com.sql.model.Chat;
import test.yejiaquan.com.sql.model.User;
import test.yejiaquan.com.sql.util.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yejiaquan on 2016/12/31.
 */
public class Data {
    public static String first;
    public static Client client=new Client();
    public static Handler handler;
    public static Boolean flag=false;
    public static Thread thread;
    public static Socket socket;
    public static Socket socketl;

}
