package com.evan.chat.welcome;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.evan.chat.R;

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

    public static WelcomeFragment newInstance(){
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.welcome_frag,container,false);
        welcomeIV = root.findViewById(R.id.welcome_image);
        titleTV = root.findViewById(R.id.welcome_title);
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
