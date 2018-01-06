package com.evan.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import com.bigkoo.alertview.AlertView;
import com.evan.chat.R;
import org.androidannotations.annotations.EActivity;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/6
 * Time: 1:29
 */
@EActivity(R.layout.activity_welcome)
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getBooleanExtra("show_log",false)){
            new AlertView("服务器返回结果", getIntent().getStringExtra("log_value"), null,
                    new String[]{"确定"}, null, this, AlertView.Style.Alert, null).show();
        }
    }
}
