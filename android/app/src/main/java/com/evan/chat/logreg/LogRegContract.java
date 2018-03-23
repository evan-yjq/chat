package com.evan.chat.logreg;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午8:58
 */
public interface LogRegContract {
    interface LogView extends BaseView<Presenter>{

        void showPasswordError(int errorRes);

        void showAccountError(int errorRes);

        void signInSuccess();

        void showSignInError();

        void showProgress(boolean show);
    }

    interface RegView extends BaseView<Presenter>{

        void showPasswordError(int errorRes);

        void showAccountError(int errorRes);

        void showEmailError(int errorRes);

        void showRegSuccess();

        void showRegError();

        void showProgress(boolean show);
    }

    interface Presenter extends BasePresenter{

        void attemptLog(String account, String password);

        void attemptReg(String account, String password, String email);
    }


}
