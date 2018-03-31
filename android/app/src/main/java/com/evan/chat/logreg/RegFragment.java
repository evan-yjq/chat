package com.evan.chat.logreg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.evan.chat.R;

import java.util.Objects;

import static com.evan.chat.logreg.LogRegActivity.LOG_FRAG;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/2/27
 * Time: 下午1:16
 */
public class RegFragment extends Fragment implements LogRegContract.RegView{

    private LogRegContract.Presenter presenter;

    private EditText mAccountView;  //用户名输入
    private EditText mPasswordView; //密码输入
    private EditText mEmailView; //邮箱输入
    private View mProgressView; //加载动画
    private Button mRegButton;  //确认按钮
    private TextView mSwitchLogView;//跳转登录界面

    public RegFragment(){

    }

    public static RegFragment newInstance(){
        return new RegFragment();
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
        View root = inflater.inflate(R.layout.reg_frag,container,false);
        mAccountView = root.findViewById(R.id.account);
        mPasswordView = root.findViewById(R.id.password);
        mEmailView = root.findViewById(R.id.email);
        mProgressView = root.findViewById(R.id.reg_progress);
        mRegButton = root.findViewById(R.id.reg_button);
        mSwitchLogView = root.findViewById(R.id.switch_);

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptReg(mAccountView.getText().toString(),mPasswordView.getText().toString(),mEmailView.getText().toString());
            }
        });
        mSwitchLogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.Switching(LOG_FRAG);
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
    public void showEmailError(int errorRes) {
        showError(mEmailView,getString(errorRes));
    }

    private void showError(EditText et, String error) {
        et.setError(error);
        et.requestFocus();
    }

    @Override
    public void showRegSuccess() {
        showMessage("注册成功");
        mPasswordView.setText(null);
        mAccountView.setText(null);
        mEmailView.setText(null);
    }

    @Override
    public void showRegError() {
        showMessage(getString(R.string.error_invalid_account_or_email));
    }

    private void showMessage(String msg){
        Snackbar.make(Objects.requireNonNull(getView()),msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegButton.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegButton.setVisibility(show ? View.GONE : View.VISIBLE);
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
