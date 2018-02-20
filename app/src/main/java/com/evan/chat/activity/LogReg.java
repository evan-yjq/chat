package com.evan.chat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.evan.chat.R;
import com.evan.chat.bus.UseUserBus;
import com.evan.chat.bus.UserBus;
import com.evan.chat.data.source.dao.LogUserDao;
import com.evan.chat.data.source.dao.UserDao;
import com.evan.chat.gen.LogUser;
import com.evan.chat.json.ServerReturnValue;
import com.evan.chat.json.UserInfo;
import com.evan.chat.util.GreenDaoUtils;
import com.evan.chat.util.MD5Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 0:34
 */
@EActivity(R.layout.activity_log_reg)
public class LogReg extends Base implements UseUserBus{

    private final String login_url="http://"+Data.ip+":"+Data.host+"/user/sign_in_by_username"; //用户名登录接口
    private final String reg_url="http://"+Data.ip+":"+Data.host+"/user/register";  //注册接口
    private UserLogRegTask mAuthTask = null;    //登录/注册异步器
    private Bundle bundle;
    private boolean isLogin; //是否显示为登陆界面


    @ViewById(R.id.account)
    EditText mAccountView;  //用户名输入
    @ViewById(R.id.password)
    EditText mPasswordView; //密码输入
    @ViewById(R.id.login_progress)
    View mProgressView; //加载动画
    @ViewById(R.id.switch_)
    TextView mSwitchView;   //更换登陆注册操作
    @ViewById(R.id.email_sign_in_button)
    Button mEmailSignInButton;  //确认按钮
    @ViewById(R.id.login_form)
    ScrollView myView;    //all
    @ViewById(R.id.forget)
    TextView mForgetView;   //忘记密码
    @ViewById(R.id.email_login_form)
    LinearLayout mFormView; //为了删除忘记密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserBus.init().add_activity("LogReg",this); //将界面添加到用户总线
        super.onCreate(savedInstanceState);
        bundle = this.getIntent().getExtras();
        isLogin=bundle.getBoolean("is_login",true);
        init();
    }

    @Override
    protected void onDestroy() {
        UserBus.init().remove_activity("LogReg"); //将界面移除用户总线
        super.onDestroy();
    }

    //初始化界面
    @UiThread
    void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color6));
        }
//        setGestureListener();
        if (isLogin){
            mEmailSignInButton.setText(R.string.action_sign_in);
            mSwitchView.setText(R.string.action_reg);
            mSwitchView.setText("<<<"+mSwitchView.getText());
        }else{
            mEmailSignInButton.setText(R.string.action_reg);
            mSwitchView.setText(R.string.action_sign_in);
            mSwitchView.setText(mSwitchView.getText()+">>>");
            mFormView.removeView(mForgetView);
        }
        mAccountView.setHintTextColor(Color.WHITE);
        mSwitchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                if (isLogin) {
                    b.putBoolean("is_login",false);
                    openActivity(LogReg_.class,RIGHT,b);
                } else {
                    b.putBoolean("is_login",true);
                    openActivity(LogReg_.class,LEFT,b);
                }
                finish();
            }
        });
        mPasswordView.setHintTextColor(Color.WHITE);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogReg();
            }
        });
        if(bundle.getBoolean("show_log",false)){
            new AlertView("服务器返回结果", bundle.getString("log_value"), null,
                    new String[]{"确定"}, null, this, AlertView.Style.Alert, null).show();
        }
    }

    //登录前各个输入框内容的判定
    private void attemptLogReg() {
        if (mAuthTask != null) {
            return;
        }
        mAccountView.setError(null);
        mPasswordView.setError(null);
        String email = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLogRegTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //设定密码长度
    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    //加载动画的实现
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mEmailSignInButton.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    //左右滑动作监听器
    private void setGestureListener(){
        myView.setOnTouchListener(new View.OnTouchListener() {
            float mStartX=0,mStartY=0,mEndX=0,mEndY=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = event.getX();
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndX = event.getX();
                        mEndY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Bundle b = new Bundle();
                        if (!isLogin && mEndX - mStartX > 0 && (Math.abs(mEndX - mStartX) > 25)) {//向右
                            b.putBoolean("is_login",true);
                            openActivity(LogReg_.class,LEFT,b);
                        } else if (isLogin && mEndX - mStartX < 0 && (Math.abs(mEndX - mStartX) > 25)) {//向左
                            b.putBoolean("is_login",false);
                            openActivity(LogReg_.class,RIGHT,b);
                        }
                        break;
                }
                return true;
            }
        });
    }

    //实现接口方法
    @UiThread
    @Override
    public void user_update(UserInfo userInfo) {
        //do nothing
    }

    //实现接口方法
    @Override
    public Context get_context() {
        return this;
    }

    //登陆注册异步器类
    public class UserLogRegTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAccount;
        private final String mPassword;

        //初始化并对密码加密
        UserLogRegTask(String account, String password) {
            mAccount = account;
            mPassword = MD5Util.getMD5(password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String[] result = new String[1];
            PostFormBuilder builder;
            if (isLogin) {
                builder= OkHttpUtils.post().url(login_url);
            }else{
                builder = OkHttpUtils.post().url(reg_url);
            }
            builder.addParams("username", mAccount)
                    .addParams("password", mPassword)
                    .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }
                        @Override
                        public void onResponse(String response, int id) {
                            result[0] = response;
                            System.out.println("LogReg:"+ result[0]);
                            ServerReturnValue va = JSON.parseObject(result[0],ServerReturnValue.class);
                            if (va.isSucceed()) {
                                UserDao userDao = GreenDaoUtils.getSingleTon().getmDaoSession(LogReg.this).getUserDao();
//                                  User user = new User(null,(int)va.getArg1(),mAccount,"");
//                                  userDao.insert(user);
                                if (isLogin) {
//                                      UserBus.init().update_user_info(new UserInfo(user.getUser_id(),mAccount,"",null));
                                    LogUserDao logUserDao = GreenDaoUtils.getSingleTon().getmDaoSession(LogReg.this).getLogUserDao();
                                    List<LogUser> logUsers = logUserDao.loadAll();
                                    LogUser log_user = new LogUser();
                                    boolean is_new = true;
                                    for (LogUser logUser : logUsers) {
                                        if (logUser.getUser_id() == (int)va.getArg1()) {
                                            log_user = new LogUser(null, (int)va.getArg1(), mPassword, new Date(), 1);
                                            is_new = false;
                                            break;
                                        }
                                    }
                                    if (is_new) {
                                        log_user = new LogUser(null, (int)va.getArg1(), mPassword, new Date(), 1);
                                        logUserDao.insert(log_user);
                                    } else {
                                        logUserDao.update(log_user);
                                    }
                                }
                            }
                        }
                    });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Bundle b = new Bundle();
                if (isLogin) {
                    b.putBoolean("show_log",true);
                    b.putString("log_value","登录成功");
                    openActivity(Test_.class,RIGHT,b);
                }else{
                    b.putBoolean("is_login",true);
                    b.putBoolean("show_log",true);
                    b.putString("log_value","注册成功");
                    openActivity(LogReg_.class,LEFT,b);
                }
                finish();
            } else {
                showProgress(false);
                if (isLogin) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }else{
                    mAccountView.setError(getString(R.string.error_invalid_email));
                    mAccountView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
