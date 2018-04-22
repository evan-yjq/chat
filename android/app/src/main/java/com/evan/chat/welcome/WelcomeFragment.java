package com.evan.chat.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.evan.chat.PublicData;
import com.evan.chat.R;
import com.evan.chat.friends.FriendsActivity;
import com.evan.chat.logreg.LogRegActivity;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/19
 * Time: 下午1:33
 */
public class WelcomeFragment extends Fragment implements WelcomeContract.View{

    private WelcomeContract.Presenter presenter;

    private ImageView welcomeIV;
    private TextView titleTV;

    public WelcomeFragment(){

    }

    static WelcomeFragment newInstance(){
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.welcome_frag,container,false);
        welcomeIV = root.findViewById(R.id.welcome_image);
        titleTV = root.findViewById(R.id.welcome_title);
        titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    presenter.timeStop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    @Override
    public void setWelcomeIV(Bitmap bitmap) {
        welcomeIV.setImageBitmap(bitmap);
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(Objects.requireNonNull(getView()),msg,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNextView() {
        Intent intent;
        if (presenter.getAutoUser()==null)
            intent = new Intent(getContext(), LogRegActivity.class);
        else {
            intent = new Intent(getContext(), FriendsActivity.class);
            PublicData.user = presenter.getAutoUser();
        }
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void setTitle(String title){
        titleTV.setText(title);
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
