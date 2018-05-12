package com.evan.chat.face;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;
import com.evan.chat.PublicData;
import com.evan.chat.R;
import com.evan.chat.logreg.LogRegActivity;
import com.evan.chat.util.ActivityUtils;

import static com.evan.chat.PublicData.user;
import static com.evan.chat.face.FaceFragment.DIST;


/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:53
 */
@SuppressLint("Registered")
public class FaceActivity extends AppCompatActivity {

    public final static String EXTRA_FACE_VIEW = "EXTRA_FACE_VIEW";

    public final static String EXTRA_IS_RESULT = "EXTRA_IS_RESULT";
    public static final int REQUEST_FACE_DISK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_act);

        if (user == null){
            Intent intent = new Intent(this, LogRegActivity.class);
            startActivity(intent);
            finish();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        int type = getIntent().getIntExtra(EXTRA_FACE_VIEW,0);
        boolean isResult = getIntent().getBooleanExtra(EXTRA_IS_RESULT, false);
        if (type == DIST && !PublicData.user.getIs_bind_face()) {
            Toast toast = Toast.makeText(this, getString(R.string.bind_first), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            this.finish();
        }
        FaceFragment view = (FaceFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (view == null) {
            view = FaceFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), view, R.id.contentFrame);
        }
        if (user != null) {
            new FacePresenter(view, isResult, type);
        }
    }
}
