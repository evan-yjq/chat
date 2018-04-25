package com.evan.chat.face;

import android.graphics.Bitmap;
import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;

import java.io.File;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:52
 */
public interface FaceContratct {


    interface View extends BaseView<Presenter>{

        void showProgress(boolean show);

        void showMessage(String msg);

        void showResult(boolean success);

        void captrue(String name);

        void setAnimation(int mProgressBar, int millis);
    }

    interface Presenter extends BasePresenter {

        long getUserId();

        void buttonOnClick();

        void upload(Bitmap bitmap, File pictureFile);
    }
}
