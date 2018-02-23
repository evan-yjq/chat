package com.evan.chat.logreg;

import android.support.v4.app.Fragment;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午8:58
 */
public class LogRegFragment extends Fragment implements LogRegContract.View {
    @Override
    public void setPresenter(LogRegContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
