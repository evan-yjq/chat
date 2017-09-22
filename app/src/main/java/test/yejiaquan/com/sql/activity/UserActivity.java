package test.yejiaquan.com.sql.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import test.yejiaquan.com.sql.R;
import test.yejiaquan.com.sql.btn.TopTitleButton;
import test.yejiaquan.com.sql.util.SetImageButton;

/**
 * Created by yejiaquan on 2017/1/21.
 */
public class UserActivity extends Activity {

    private LinearLayout body;
    private RelativeLayout bottom;
    private TopTitleButton top;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        top=(TopTitleButton)findViewById(R.id.top);
        SetImageButton.setTopTitleButton(top,R.mipmap.back,"个人简介",R.mipmap.transparent);
        top.onClickBack(this);
        body=(LinearLayout) findViewById(R.id.body);
        bottom=(RelativeLayout) findViewById(R.id.bottom);
    }
}
