package com.evan.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.bigkoo.alertview.AlertView;
import com.evan.chat.R;
import com.evan.chat.gen.LogUserDao;
import com.evan.chat.gen.LogUser;
import com.evan.chat.util.GreenDaoUtils;
import com.evan.chat.util.OkHttpClientManager;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/5
 * Time: 23:58
 */

@EActivity(R.layout.activity_welcome)
public class Welcome extends Base {

    private LogUserDao logUserDao;
    private final String url = "http://"+ Data.ip+":"+Data.host+"/user/sign_in_by_id";  //id登录接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private Handler h;
    @UiThread
    void init(){
        logUserDao = GreenDaoUtils.getSingleTon().getmDaoSession(this).getLogUserDao();
        long autoLoginId=autoLogin();
        if(logUserDao.count()==0&&autoLoginId==-1){
            Bundle b = new Bundle();
            b.putBoolean("is_login",true);
            openActivity(LogReg_.class,RIGHT,b);
            finish();
        }else{
            h = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    new AlertView("服务器返回结果", msg.obj.toString(), null, new String[]{"确定"}, null,
                            Welcome.this, AlertView.Style.Alert, null).show();
                }
            };
            login(autoLoginId);
        }
    }

    //登录操作
    @Background
    void login(long autoLoginId){
        Message msg = new Message();
        try {
            LogUser user = logUserDao.load(autoLoginId);
            msg.obj=OkHttpClientManager.postAsString(url,
                    new OkHttpClientManager.Param("id",user.getUser_id()+""),
                    new OkHttpClientManager.Param("password",user.getPassword()));

        } catch (IOException e) {
            e.printStackTrace();
            msg.obj="连接网络错误";
        }
        h.sendMessage(msg);
    }

    //检查是否存在自动登陆
    private long autoLogin(){
        List<LogUser> users = logUserDao.loadAll();
        if (users.isEmpty()){
            return -1;
        }else{
            for (LogUser user : users) {
                if (user.getIs_auto() == 1)
                    return user.getId();
            }
            return -1;
        }
    }
}
