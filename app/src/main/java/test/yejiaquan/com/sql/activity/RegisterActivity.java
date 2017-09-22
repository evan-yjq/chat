package test.yejiaquan.com.sql.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import test.yejiaquan.com.sql.R;
import test.yejiaquan.com.sql.util.Client;
import test.yejiaquan.com.sql.util.OnClick;

import static test.yejiaquan.com.sql.activity.Data.client;

/**
 * Created by yejiaquan on 12/27/16.
 */
public class RegisterActivity extends Activity {
    private EditText username;
    private EditText password;
    private EditText passwordA;
    private Button register;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(username.getText().toString().trim())||"".equals(password.getText().toString().trim())||"".equals(passwordA.getText().toString().trim())){
                    showToast("请填写完整");
                }else if (!password.getText().toString().equals(passwordA.getText().toString())){
                    showToast("两次密码不相同");
                }else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Message msg=client.get("reg-"+username.getText().toString()+"-"+password.getText().toString());
                            h.sendMessage(msg);
                        }
                    }.start();
                    h=new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            // 此处可以更新UI
                            Bundle b = msg.getData();
                            String result = b.getString("result");
                            if ("true".equals(result)){
                                showToast("注册成功");
                                Bundle bundle = new Bundle();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                bundle.putSerializable("autos",new Object[]{new String[]{username.getText().toString(),"","false"}});
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }else if("repetition".equals(result)){
                                showToast("用户名重复");
                            }
                        }
                    };
                }
            }
        });
    }
    public void init(){
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        passwordA=(EditText)findViewById(R.id.passwordA);
        register=(Button)findViewById(R.id.register);
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
