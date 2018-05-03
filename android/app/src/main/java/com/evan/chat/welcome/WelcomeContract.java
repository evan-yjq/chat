package com.evan.chat.welcome;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;
import com.evan.chat.data.source.model.User;

import java.io.File;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:29
 */
public interface WelcomeContract {

    interface View extends BaseView<Presenter> {

        void setTitle(String title);

        void setWelcomeIV(Bitmap bitmap);

        void showMessage(String msg);

        void showNextView(Intent intent);

        File getFile();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void getNextView();

        void timeStop() throws InterruptedException;
    }
}
