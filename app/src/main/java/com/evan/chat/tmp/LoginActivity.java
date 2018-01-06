package com.evan.chat.tmp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.evan.chat.R;
import com.evan.chat.activities.FriendsActivity;
import com.evan.chat.util.InOutPut;

import static com.evan.chat.activities.Data.client;
import static com.evan.chat.activities.Data.first;

/**
 * Created by yejiaquan on 12/27/16.
 */
public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;
    private boolean autoLogin=true;
    private Button login;
    private Button register;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_log_reg);
        super.onCreate(savedInstanceState);
        final String[] autos;
        if (getIntent().getSerializableExtra("autos")!=null) {
            autos = (String[]) getIntent().getSerializableExtra("autos");
        }else{
            autos=new String[3];
        }
        init();
        if (autos!=null){
            username.setText(autos[0]);
            password.setText(autos[1]);
            if ("true".equals(autos[2])) {
                autoLogin=true;
            }
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(username.getText().toString().trim()) && !"".equals(password.getText().toString().trim())) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Message msg=client.login(username.getText().toString(), password.getText().toString());
                            h.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        h=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 此处可以更新UI
                Bundle b = msg.getData();
                String result = b.getString("result");
                String id = b.getString("id");
                if ("true".equals(result)){
                    autos[0]=username.getText().toString();
                    autos[1]=password.getText().toString();
                    if (autoLogin) {
                        InOutPut.writeFile(LoginActivity.this, "auto.txt", username.getText().toString() + "," + password.getText().toString() + ",true", null,"写入用户信息");
                        autos[2]="true";
                    } else {
                        InOutPut.writeFile(LoginActivity.this, "auto.txt", username.getText().toString() + "," + password.getText().toString() + ",false", null,"写入用户信息");
                        autos[2]="false";
                    }
                    InOutPut.writeFile(LoginActivity.this,"first.txt","1",null,"first写入");
                    first="1";
                    Bundle bundle = new Bundle();
                    Intent intent=new Intent(LoginActivity.this,FriendsActivity.class);
                    bundle.putSerializable("autos",autos);
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    showToast("用户名或密码错误!!");
                }
            }
        };
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void init(){
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
    }
}
