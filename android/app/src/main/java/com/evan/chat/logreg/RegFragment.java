package com.evan.chat.logreg;

import android.support.v4.app.Fragment;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/27
 * Time: 下午1:16
 */
public class RegFragment extends Fragment implements LogRegContract.RegView{


    public RegFragment(){

    }

    public static RegFragment newInstance(){
        return new RegFragment();
    }

    @Override
    public void setPresenter(LogRegContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
