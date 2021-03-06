package com.evan.chat.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.face.FaceActivity;
import com.evan.chat.logreg.LogRegActivity;
import com.evan.chat.domain.usecase.User.DeleteAllUser;
import com.evan.chat.settings.SettingsActivity;
import com.evan.chat.util.ActivityUtils;
import com.evan.chat.wrapper.LanguageContextWrapper;

import static com.evan.chat.face.FaceActivity.EXTRA_FACE_VIEW;
import static com.evan.chat.face.FaceFragment.DIST;
import static com.evan.chat.face.FaceFragment.TRAIN;
import static com.evan.chat.PublicData.user;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
@SuppressLint("Registered")
public class FriendsActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_act);

        if (user == null){
            Intent intent = new Intent(this, LogRegActivity.class);
            startActivity(intent);
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.friends_list_title);
        }

        FriendsFragment friendsFragment = (FriendsFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (friendsFragment == null){
            friendsFragment = FriendsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),friendsFragment,R.id.contentFrame);
        }

        FriendsPresenter mFriendsPresenter = new FriendsPresenter(friendsFragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetFriends(getApplicationContext()));

        //设置侧边栏
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null && user != null) {
            setupDrawerContent(navigationView);
            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
            TextView text = headerLayout.findViewById(R.id.nav_header_text);
            ImageView image = headerLayout.findViewById(R.id.nav_header_image);
            String nickname = user.getNickname();
            if (nickname==null||nickname.isEmpty()){
                text.setText(user.getAccount());
            }else text.setText(nickname);
            if (user.getHead() != null) {
                image.setImageBitmap(user.getHead());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //从工具栏中选择主图标时，打开导航抽屉。
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String lang = preferences.getString(getString(R.string.pref_locale), "zh_CN");
                switch (item.getItemId()){
                    case R.id.sign_out_menu_item:
                        signOut();
                        break;
                    case R.id.bind_face_menu_item:
                        intent = new Intent(FriendsActivity.this, FaceActivity.class);
                        intent.putExtra(EXTRA_FACE_VIEW,TRAIN);
                        startActivity(intent);
                        break;
                    case R.id.judg_face_menu_item:
                        intent = new Intent(FriendsActivity.this, FaceActivity.class);
                        intent.putExtra(EXTRA_FACE_VIEW,DIST);
                        startActivity(intent);
                        break;
                    case R.id.change_language:
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                        if (lang.equals("zh_CN"))lang = "en";
                        else lang = "zh_CN";
                        editor.putString(getString(R.string.pref_locale),lang);
                        editor.apply();
                        LanguageContextWrapper.wrap(getApplicationContext(),lang);
                        intent = new Intent(FriendsActivity.this, FriendsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.settings_menu_item:
                        intent = new Intent(FriendsActivity.this, SettingsActivity.class);
                        intent.putExtra("lan",lang);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void signOut(){
        Injection.provideUseCaseHandler().execute(Injection.provideDeleteAllUser(getApplicationContext()),
                new DeleteAllUser.RequestValues(), new UseCase.UseCaseCallback<DeleteAllUser.ResponseValue>() {
                    @Override
                    public void onSuccess(DeleteAllUser.ResponseValue response) {

                    }

                    @Override
                    public void onError() {

                    }
                });
        Intent intent = new Intent(this,LogRegActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawers();
                return true;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
