package com.evan.chat.chat;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.data.source.Friend.model.Friend;
import com.evan.chat.util.ActivityUtils;

import static com.evan.chat.friends.FriendsActivity.EXTRA_FRIEND_ID;
import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER_ID;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Friend friend = (Friend) getIntent().getBundleExtra("friend").getSerializable("friend");

        Long userId = getIntent().getLongExtra(EXTRA_USER_ID,0);
        Long friendID = getIntent().getLongExtra(EXTRA_FRIEND_ID,0);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        if ("".equals(friend.getNickname()))
            ab.setTitle(friend.getAccount());
        else ab.setTitle(friend.getNickname());

        ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (chatFragment == null){
            chatFragment = ChatFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),chatFragment,R.id.contentFrame);
        }

        new ChatPresenter(chatFragment, userId, friendID,
                Injection.provideSendMessage(getApplicationContext()),
                Injection.provideUseCaseHandler());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
