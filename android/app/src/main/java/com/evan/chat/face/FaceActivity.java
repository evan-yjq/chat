package com.evan.chat.face;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.evan.chat.R;
import com.evan.chat.data.source.User.model.User;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:53
 */
@SuppressLint("Registered")
@Fullscreen
@EActivity(R.layout.face_act)
public class FaceActivity extends AppCompatActivity {

    public final static String EXTRA_FACE_VIEW = "EXTRA_FACE_VIEW";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        int type = getIntent().getIntExtra(EXTRA_FACE_VIEW,0);
        FaceFragment view = (FaceFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (view == null) {
            view = FaceFragment.newInstance(type);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), view, R.id.contentFrame);
        }
        new FacePresenter(view,user);
    }
}
