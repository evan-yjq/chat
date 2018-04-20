package com.evan.chat.face;

import com.evan.chat.BasePresenter;
import com.evan.chat.BaseView;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:52
 */
public interface FaceContratct {


    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter {
        long getUserId();
    }
}
