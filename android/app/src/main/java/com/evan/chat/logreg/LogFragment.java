package com.evan.chat.logreg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.evan.chat.R;
import com.evan.chat.friends.FriendsActivity;

import java.util.Objects;

import static com.evan.chat.logreg.LogRegActivity.EXTRA_USER_ID;
import static com.evan.chat.logreg.LogRegActivity.REG_FRAG;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/23
 * Time: 下午8:58
 */
public class LogFragment extends Fragment implements LogRegContract.LogView {

    private LogRegContract.Presenter presenter;

    private EditText mAccountView;  //用户名输入
    private EditText mPasswordView; //密码输入
    private View mProgressView; //加载动画
    private Button mSignInButton;  //确认按钮

    public LogFragment(){

    }

    static LogFragment newInstance(){
        return new LogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.log_frag,container,false);
        mAccountView = root.findViewById(R.id.account);
        mPasswordView = root.findViewById(R.id.password);
        mProgressView = root.findViewById(R.id.login_progress);
        mSignInButton = root.findViewById(R.id.sign_in_button);
        TextView mSwitchRegView = root.findViewById(R.id.switch_);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptLog(mAccountView.getText().toString(),mPasswordView.getText().toString());
            }
        });
        mSwitchRegView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.Switching(REG_FRAG);
            }
        });
        return root;
    }

    @Override
    public void setPresenter(LogRegContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showPasswordError(int errorRes) {
        showError(mPasswordView,getString(errorRes));
    }

    @Override
    public void showAccountError(int errorRes) {
        showError(mAccountView,getString(errorRes));
    }

    @Override
    public void signInSuccess(Long id) {
        Intent intent = new Intent(getContext(), FriendsActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showSignInError() {
        showMessage(getString(R.string.error_incorrect));
    }

    private void showMessage(String msg){
        Snackbar.make(Objects.requireNonNull(getView()),msg, Snackbar.LENGTH_LONG).show();
    }

    private void showError(EditText et, String error) {
        et.setError(error);
        et.requestFocus();
    }

    //加载动画的实现
    @Override
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mSignInButton.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
