package com.evan.chat.face;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;

import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER_ID;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:53
 */
public class FaceAcitivity extends AppCompatActivity {

    public final static String EXTRA_FACE_VIEW = "EXTRA_FACE_VIEW";
    public final static int EXTRA_FACE_LOGIN = 1;
    public final static int EXTRA_FACE_REGISTER = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_act);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        Long userId = getIntent().getLongExtra(EXTRA_USER_ID,0);
        int type = getIntent().getIntExtra(EXTRA_FACE_VIEW,0);
        FaceContratct.View view;
        view = (FaceRegisterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (view == null) {
            if (type == EXTRA_FACE_REGISTER) {
                view = FaceRegisterFragment.getInstance();
            }else{
                view = FaceLoginFragment.getInstance();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), (Fragment) view, R.id.contentFrame);
        }
        new FacePresenter(view,userId);
    }
}
