package com.evan.chat.activity;

import android.os.Handler;
import com.evan.chat.util.Client;

import java.net.Socket;

/**
 * Created by yejiaquan on 2016/12/31.
 */
public class Data {
    public static String host;
    public static String ip;

    public static String first;
//    public static Client client=new Client();
    public static Handler handler;
    public static Boolean flag=false;
    public static Thread thread;
    public static Socket socket;
    public static Socket socketl;

}
