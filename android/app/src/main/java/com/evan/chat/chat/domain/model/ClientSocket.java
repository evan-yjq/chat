package com.evan.chat.chat.domain.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/19
 * Time: 11:34
 */
public class ClientSocket extends Socket {

    public ClientSocket(java.net.Socket socket) {
        super(socket);
        this.address = socket.getLocalAddress().toString()+":"+socket.getLocalPort();
    }

    @Override
    public void send(String s) {
        try {
            OutputStream output = getSocket().getOutputStream();
            s = getAddress()+ "/-/" + s + "\n";
            output.write(s.getBytes("utf-8"));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeSocket(String s) {
        System.out.println(s);
    }
}
