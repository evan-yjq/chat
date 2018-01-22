package com.evan.chat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.evan.chat.R;
import com.evan.chat.gen.LogUserDao;
import com.evan.chat.gen.LogUser;
import com.evan.chat.gen.RequestUrl;
import com.evan.chat.gen.RequestUrlDao;
import com.evan.chat.json.ServerReturnValue;
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
        getUrl();
    }

    @Background
    void getUrl(){
        String route;
        Message msg = new Message();
        msg.arg1 = 1;
        RequestUrlDao requestUrlDao = GreenDaoUtils.getSingleTon().getmDaoSession(this).getRequestUrlDao();
        if (requestUrlDao.count()==0){
            Data.ip = "115.28.216.244";
            Data.host = "3000";
            route = "/get_request_url";
        }else{
            Data.ip = requestUrlDao.queryBuilder().where(RequestUrlDao.Properties.Key
                    .eq("ip")).build().unique().getValue();
            Data.host = requestUrlDao.queryBuilder().where(RequestUrlDao.Properties.Key
                    .eq("host")).build().unique().getValue();
            route = requestUrlDao.queryBuilder().where(RequestUrlDao.Properties.Key
                    .eq("get_request_url")).build().unique().getValue();
        }
        String url = "http://" + Data.ip + ":" + Data.host + route;
        try {
            String re =OkHttpClientManager.getAsString(url);
            JSON.parseArray(re, ServerReturnValue.class);

            msg.obj = true;
        } catch (IOException e) {
            msg.obj = false;
        }
        h.sendMessage(msg);
    }

    private void tryLogin(){
        logUserDao = GreenDaoUtils.getSingleTon().getmDaoSession(this).getLogUserDao();
        long autoLoginId=autoLogin();
        if(logUserDao.count()==0&&autoLoginId==-1){
            Bundle b = new Bundle();
            b.putBoolean("is_login",true);
            openActivity(LogReg_.class,RIGHT,b);
            finish();
        }else{
            login(autoLoginId);
        }
    }

    private Handler h;

    @UiThread
    void init(){
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case 0 :
                        new AlertView("服务器返回结果", msg.obj.toString(), null, new String[]{"确定"},
                                null, Welcome.this, AlertView.Style.Alert, null).show();
                        break;
                    case 1 :
                        if (msg.obj.equals(false)) {
                            new AlertView("APP版本过低", "请从应用商店或官网下载最新版本", null,
                                    new String[]{"确定"}, null, Welcome.this, AlertView.Style.Alert,
                                    new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            finish();
                                        }
                                    }).show();
                            return;
                        }else{
                            tryLogin();
                        }
                        break;
                }
            }
        };
    }

    //登录操作
    @Background
    void login(long autoLoginId){
        Message msg = new Message();
        msg.arg1 = 0;
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
