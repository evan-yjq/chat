package com.evan.chat.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.R;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 14:54
 */
@SuppressLint("Registered")
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);
    }
}
