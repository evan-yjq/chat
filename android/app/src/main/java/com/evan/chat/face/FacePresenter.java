package com.evan.chat.face;

import android.support.annotation.NonNull;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:53
 */
public class FacePresenter implements FaceContratct.Presenter{

    private final FaceContratct.View view;
    private final long userId;

    FacePresenter(@NonNull FaceContratct.View view, long userId){
        this.view = checkNotNull(view,"view cannot be null!");
        this.userId = userId;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public long getUserId() {
        return userId;
    }
}
