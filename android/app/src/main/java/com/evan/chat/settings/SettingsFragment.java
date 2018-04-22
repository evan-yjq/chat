package com.evan.chat.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.evan.chat.R;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 14:55
 */
public class SettingsFragment extends Fragment implements SettingsContract.View {

    private SettingsContract.Presenter presenter;

    public SettingsFragment(){

    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_frag,container,false);
        return root;
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
