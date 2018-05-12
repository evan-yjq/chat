package com.evan.chat.searchaddfriend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.util.ActivityUtils;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/26
 * Time: 13:22
 */
@SuppressLint("Registered")
public class SearchAddFriendActivity extends AppCompatActivity {

    private SearchAddFriendPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchfriend_act);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(R.string.search_friend);

        SearchAddFriendFragment searchAddFriendFragment = (SearchAddFriendFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (searchAddFriendFragment == null){
            searchAddFriendFragment = SearchAddFriendFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),searchAddFriendFragment,R.id.contentFrame);
        }

        presenter = new SearchAddFriendPresenter(searchAddFriendFragment,
                Injection.provideUseCaseHandler(),
                Injection.provideSearchInAllUser(getApplicationContext()),
                Injection.provideAddFriend(getApplicationContext()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_add_friend_hint));
        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                presenter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
