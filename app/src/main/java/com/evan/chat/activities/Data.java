package com.evan.chat.activities;

import android.os.Handler;
import com.evan.chat.util.Client;

import java.net.Socket;

/**
 * Created by yejiaquan on 2016/12/31.
 */
public class Data {
    public final static String host = "3000";
    public final static String ip = "115.28.216.244";

    public static String first;
    public static Client client=new Client();
    public static Handler handler;
    public static Boolean flag=false;
    public static Thread thread;
    public static Socket socket;
    public static Socket socketl;

}
