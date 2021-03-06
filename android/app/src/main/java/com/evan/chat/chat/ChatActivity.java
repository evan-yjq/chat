package com.evan.chat.chat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.logreg.LogRegActivity;
import com.evan.chat.util.ActivityUtils;

import java.util.Objects;

import static com.evan.chat.PublicData.friend;
import static com.evan.chat.PublicData.user;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/13
 * Time: 22:30
 */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);

        if (user == null){
            Intent intent = new Intent(this, LogRegActivity.class);
            startActivity(intent);
            finish();
        }

        if (friend == null) onBackPressed();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        if ("".equals(friend.getNickname()))
            ab.setTitle(friend.getAccount());
        else ab.setTitle(friend.getNickname());

        ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (chatFragment == null){
            chatFragment = ChatFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),chatFragment,R.id.contentFrame);
        }

        if (friend != null)
            new ChatPresenter(chatFragment, friend,
                    Injection.provideSendMessage(getApplicationContext()),
                    Injection.provideUseCaseHandler());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
