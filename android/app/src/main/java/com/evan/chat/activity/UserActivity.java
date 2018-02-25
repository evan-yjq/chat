package com.evan.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.evan.chat.R;
import com.evan.chat.view.TopTitleButton;
import com.evan.chat.util.SetImageButton;

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
        top = findViewById(R.id.top);
        SetImageButton.setTopTitleButton(top,R.mipmap.back,"个人简介",R.mipmap.transparent);
        top.onClickBack(this);
        body = findViewById(R.id.body);
        bottom = findViewById(R.id.bottom);
    }
}
