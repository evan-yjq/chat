package test.yejiaquan.com.sql.util;

import android.os.Bundle;
import android.os.Message;

import java.io.*;
import java.net.Socket;

import static test.yejiaquan.com.sql.activity.Data.socket;
import static test.yejiaquan.com.sql.activity.Data.socketl;

/**
 * Created by yejiaquan on 12/27/16.
 */
//客户端
public class Client implements Serializable {

    private PrintWriter pw;
    private BufferedReader br;

    public Client(){
        try {
            socket=new Socket("115.28.216.244",10086);
            socketl=new Socket("115.28.216.244",10086);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message login(String username, String password) {
        String result = "false";
        String id = "";
        try {
            //2、获取输出流，向服务器端发送信息
            pw = new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
            pw.write("login-" + username + "-" + password + "\nend\n");
            pw.flush();
            //3、获取输入流，并读取服务器端的响应信息
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info = null;
            while (true) {
                info = br.readLine();
                if (info != null && !"end".equals(info)) {
                    id = info;
                    break;
                }
            }
            String[] infos = id.split("-");
            if ("true".equals(infos[0])) {
                id = infos[1];
                result = "true";
            }
        } catch (IOException e) {
            System.out.println("Client login fail");
        }
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("result", result);
        b.putString("id", id);
        msg.setData(b);
        return msg;
    }

    public Message get(String content) {
        String result = "";
        String key = content.split("-")[0];
        try {
            //2、获取输出流，向服务器端发送信息
            pw = new PrintWriter(socket.getOutputStream());//将输出流包装成打印流
            pw.write(content + "\nend\n");
            pw.flush();
            if (!key.equals("chat")) {
                //3、获取输入流，并读取服务器端的响应信息
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String info = null;
                while (true) {
                    info = br.readLine();
                    if (info != null && !"end".equals(info)) {
                        result = info;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + key + " fail");
        }
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("result", result);
        msg.setData(b);
        return msg;
    }
}