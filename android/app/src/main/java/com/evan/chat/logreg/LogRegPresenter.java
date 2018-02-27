package com.evan.chat.logreg;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.UseCaseHandler;
import com.evan.chat.logreg.domain.usecase.SignInUser;

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

    private final UseCaseHandler mUseCaseHandler;

    private final int userId;

    public LogRegPresenter(@NonNull LogRegContract.LogView logView, @NonNull LogRegContract.RegView regView,
                           @NonNull SignInUser signInUser, @NonNull UseCaseHandler useCaseHandler,
                           @NonNull int userId) {
        this.logView = checkNotNull(logView,"LogView cannot be null!");
        this.regView = checkNotNull(regView,"RegView cannot be null!");
        this.signInUser = checkNotNull(signInUser,"signInUser cannot be null!");
        mUseCaseHandler = checkNotNull(useCaseHandler,"useCaseHandler cannot be null!");
        this.userId = checkNotNull(userId,"userId cannot be null!");
        logView.setPresenter(this);
        regView.setPresenter(this);
    }

    @Override
    public void start() {
        if (userId != 0){
            // todo 1.从本地获取账号信息 2.if(联网){然后执行attemptLog()}，else{登陆本地账号}
        }
    }

    //登录前各个输入框内容的判定
    @Override
    public void attemptLog(String account, String password) {
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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

    private void signIn(String account, String password){
        mUseCaseHandler.execute(signInUser, new SignInUser.RequestValues(account, password),
                new UseCase.UseCaseCallback<SignInUser.ResponseValue>() {
                    @Override
                    public void onSuccess(SignInUser.ResponseValue response) {
                        if (logView.isActive()){
                            logView.signInSuccess();
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

    //设定密码长度
    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }
}
