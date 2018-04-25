package com.evan.chat.face;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;
import com.evan.chat.PublicData;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;

import static com.evan.chat.face.FaceFragment.DIST;


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

    public final static String EXTRA_IS_RESULT = "EXTRA_IS_RESULT";
    public static final int REQUEST_FACE_DISK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        int type = getIntent().getIntExtra(EXTRA_FACE_VIEW,0);
        boolean isResult = getIntent().getBooleanExtra(EXTRA_IS_RESULT, false);
        System.out.println(PublicData.user);
        if (type == DIST && !PublicData.user.getIs_bind_face()) {
            Toast toast = Toast.makeText(this, "请先绑定", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            this.finish();
        }
        FaceFragment view = (FaceFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (view == null) {
            view = FaceFragment.newInstance(type);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), view, R.id.contentFrame);
        }
        new FacePresenter(view, isResult, type);
    }
}
