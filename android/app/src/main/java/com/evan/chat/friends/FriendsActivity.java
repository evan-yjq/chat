package com.evan.chat.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import com.evan.chat.Injection;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.User.model.User;
import com.evan.chat.face.FaceActivity_;
import com.evan.chat.logreg.LogRegActivity;
import com.evan.chat.logreg.domain.usecase.DeleteAllUser;
import com.evan.chat.settings.SettingsActivity;
import com.evan.chat.util.ActivityUtils;
import static com.evan.chat.face.FaceActivity.EXTRA_FACE_VIEW;
import static com.evan.chat.face.FaceFragment.DIST;
import static com.evan.chat.face.FaceFragment.TRAIN;
import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/2
 * Time: 13:50
 */
@SuppressLint("Registered")
public class FriendsActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private User user;

    private FriendsPresenter mFriendsPresenter;
    public static final String EXTRA_FRIEND = "EXTRA_FRIEND";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_act);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        user = (User) getIntent().getSerializableExtra(EXTRA_USER);

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

        mFriendsPresenter = new FriendsPresenter(friendsFragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetFriends(getApplicationContext()),
                user);

        //设置侧边栏
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
            TextView text = headerLayout.findViewById(R.id.nav_header_text);
            String nickname = user.getNickname();
            if (nickname==null||nickname.isEmpty()){
                text.setText(user.getAccount());
            }else{
                text.setText(nickname);
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
                switch (item.getItemId()){
                    case R.id.sign_out_menu_item:
                        signOut();
                        break;
                    case R.id.bind_face_menu_item:
                        intent = new Intent(FriendsActivity.this, FaceActivity_.class);
                        intent.putExtra(EXTRA_USER,user);
                        intent.putExtra(EXTRA_FACE_VIEW,TRAIN);
                        startActivity(intent);
                        break;
                    case R.id.judg_face_menu_item:
                        intent = new Intent(FriendsActivity.this, FaceActivity_.class);
                        intent.putExtra(EXTRA_USER,user);
                        intent.putExtra(EXTRA_FACE_VIEW,DIST);
                        startActivity(intent);
                        break;
                    case R.id.settings_menu_item:
                        intent = new Intent(FriendsActivity.this, SettingsActivity.class);
                        startActivity(intent);
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
