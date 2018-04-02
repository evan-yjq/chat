package com.evan.chat.logreg;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.logreg.domain.usecase.RegisterUser;
import com.evan.chat.logreg.domain.usecase.SignInUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.evan.chat.logreg.LogRegActivity.LOG_FRAG;
import static com.evan.chat.logreg.LogRegActivity.REG_FRAG;
import static com.evan.chat.logreg.LogRegActivity.LOG_REG_SWITCH;
import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午8:58
 */
public class LogRegPresenter implements LogRegContract.Presenter{

    private final LogRegContract.LogView logView;
    private final LogRegContract.RegView regView;
    private final SignInUser signInUser;
    private final RegisterUser registerUser;

    private final UseCaseHandler mUseCaseHandler;

    private final Long userId;

    LogRegPresenter(@NonNull LogRegContract.LogView logView, @NonNull LogRegContract.RegView regView,
                    @NonNull SignInUser signInUser, @NonNull RegisterUser registerUser,
                    @NonNull UseCaseHandler useCaseHandler, Long userId) {
        this.logView = checkNotNull(logView,"LogView cannot be null!");
        this.regView = checkNotNull(regView,"RegView cannot be null!");
        this.signInUser = checkNotNull(signInUser,"signInUser cannot be null!");
        this.registerUser = checkNotNull(registerUser,"registerUser cannot be null!");
        mUseCaseHandler = checkNotNull(useCaseHandler,"useCaseHandler cannot be null!");
        this.userId = checkNotNull(userId,"userId cannot be null!");
        logView.setPresenter(this);
        regView.setPresenter(this);
    }

    @Override
    public void start() {
//        if (userId != 0){
            // todo 1.从本地获取账号信息 2.if(联网){然后执行attemptLog()}，else{登陆本地账号}
//        }
    }

    //切换登陆注册界面
    @Override
    public void Switching(String key){
        if (LOG_FRAG.equals(key)){
            FragmentManager mManager = ((Fragment)regView).getFragmentManager();
            FragmentTransaction ft = mManager.beginTransaction();
            ft.setCustomAnimations(R.anim.in_from_left,R.anim.out_to_right);
            ft.replace(R.id.contentFrame, (Fragment) logView);
            ft.commit();
            LOG_REG_SWITCH = (Fragment) regView;
        }else if(REG_FRAG.equals(key)){
            FragmentManager mManager = ((Fragment)logView).getFragmentManager();
            FragmentTransaction ft = mManager.beginTransaction();
            ft.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
            ft.replace(R.id.contentFrame, (Fragment) regView);
            ft.commit();
            LOG_REG_SWITCH = (Fragment) logView;
        }
    }

    //登录前各个输入框内容的判定
    @Override
    public void attemptLog(String account, String password) {
        boolean cancel = false;
        if (!isPasswordValid(password)) {
            logView.showPasswordError(R.string.error_invalid_password);
            cancel = true;
        }
        if (TextUtils.isEmpty(account)) {
            logView.showAccountError(R.string.error_field_required);
            cancel = true;
        }
        if (!cancel) {
            if (logView.isActive()) {
                logView.showProgress(true);
            }
            signIn(account, password);
        }
    }

    //注册前各个输入框内容的判定
    @Override
    public void attemptReg(String account, String password, String email) {
        boolean cancel = false;
        if (!isPasswordValid(password)) {
            regView.showPasswordError(R.string.error_invalid_password);
            cancel = true;
        }
        if (TextUtils.isEmpty(account)) {
            regView.showAccountError(R.string.error_field_required);
            cancel = true;
        }
        if(!TextUtils.isEmpty(email)&&!isEmail(email)){
            regView.showEmailError(R.string.error_invalid_email);
            cancel = true;
        }
        if (!cancel) {
            if (regView.isActive()) {
                regView.showProgress(true);
            }
            register(account, password, email);
        }
    }

    //登录操作
    private void signIn(String account, String password){
        mUseCaseHandler.execute(signInUser, new SignInUser.RequestValues(account, password),
                new UseCase.UseCaseCallback<SignInUser.ResponseValue>() {
                    @Override
                    public void onSuccess(SignInUser.ResponseValue response) {
                        if (logView.isActive()){
                            logView.signInSuccess(response.getUser().getId());
                            logView.showProgress(false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (logView.isActive()){
                            logView.showSignInError();
                            logView.showProgress(false);
                        }
                    }
                });
    }

    //注册操作
    private void register(String account, String password, String email){
        mUseCaseHandler.execute(registerUser, new RegisterUser.RequestValues(account, password, email),
                new UseCase.UseCaseCallback<RegisterUser.ResponseValue>() {
                    @Override
                    public void onSuccess(RegisterUser.ResponseValue response) {
                        if (regView.isActive()){
                            regView.showRegSuccess();
                            regView.showProgress(false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (regView.isActive()){
                            regView.showRegError();
                            regView.showProgress(false);
                        }
                    }
                });
    }

    //设定密码长度
    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    //检测邮箱格式
    private boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        return m.matches();
    }
}
