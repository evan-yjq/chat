package com.evan.chat.chat.domain.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/9
 * Time: 20:05
 */
public abstract class Socket {

    private java.net.Socket socket;

    private Socket(){}

    public Socket(java.net.Socket socket){
        this.socket = socket;
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(10);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.address = socket.getInetAddress().toString()+":"+socket.getPort();
    }

    protected String address;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    private SocketCallback mSocketCallback;

    public void setSocketCallback(SocketCallback mSocketCallback) {
        this.mSocketCallback = mSocketCallback;
    }

    public SocketCallback getSocketCallback() {
        return mSocketCallback;
    }

    void run() {
        try {
            InputStream input = socket.getInputStream();
            // 从客户端获取信息
            BufferedReader bff = new BufferedReader(new InputStreamReader(input));
            while (isConnection()) {
                // 获取客户端的信息
                while (input.available()!=0){
                    String line = bff.readLine();
                    if (line!=null) {
                        executeSocket(line);
                    }
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.net.Socket getSocket() {
        return socket;
    }

    public abstract void send(String str);

    public abstract void executeSocket(String str);

//    public void heartbeatPackage() {
//        Runnable runnable = () -> {
//            try {
//                while (true) {
//                    socket.sendUrgentData(0xFF); // 发送心跳包
//                    System.out.println("目前处于链接状态！");
//                    Thread.sleep(1 * 1000);//线程睡眠3秒
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("断开链接！");
//                mSocketCallback.onDisconnect(address);
//                try {
//                    socket.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        };
//        runnable.run();
//    }

    //判断当前是否处于连接
    public boolean isConnection(){
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * 传入的数据
     */
    public interface RequestValues{
    }

    public interface SocketCallback{

        void onDisconnect(String address);
    }
}
