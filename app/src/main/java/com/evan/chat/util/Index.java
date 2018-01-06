package com.evan.chat.util;

import android.os.Bundle;
import android.os.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Index {

    public static Message http(ArrayList<NameValuePair> nameValuePairs, String url){
        /*存放http请求得到的结果*/
        String result = "";
        String ss = null;
        InputStream is = null;
        //http post
        try{
            /*创建一个HttpClient的一个对象*/
            HttpClient httpclient = new DefaultHttpClient();
            /*创建一个HttpPost的对象*/
            HttpPost httppost = new HttpPost(url);
            /*设置请求的数据*/
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
            /*创建HttpResponse对象*/
            HttpResponse response = httpclient.execute(httppost);
            /*获取这次回应的消息实体*/
            HttpEntity entity = response.getEntity();
            /*创建一个指向对象实体的数据流*/
            is = entity.getContent();
        }catch(Exception e){
            System.out.println("连接误差");
        }

        //convert response to string
        try{
            assert is != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
            System.out.println("get = " + result);
        }catch(Exception e){
            System.out.println("Error converting to String");
        }
        Message msg=new Message();
        Bundle b = new Bundle();// 存放数据
        b.putString("result",result);
        msg.setData(b);
        return msg;
    }
}
