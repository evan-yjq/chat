package com.evan.chat.tmp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.evan.chat.R;
import com.evan.chat.activities.FriendsActivity;
import com.evan.chat.util.InOutPut;

import static com.evan.chat.activities.Data.client;
import static com.evan.chat.activities.Data.first;

public class WelcomeActivity extends Activity {
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        auto();
    }

    private void auto(){
        InOutPut.writeFile(WelcomeActivity.this,"auto.txt","",MODE_APPEND,"auto初始化");
        InOutPut.writeFile(WelcomeActivity.this,"first.txt","",MODE_APPEND,"first初始化");
        String auto= InOutPut.readFile(WelcomeActivity.this,"auto.txt","auto读取");
        if ("".equals(auto)||auto==null){
//            OnClick.go(this,LoginActivity.class,new String[]{"client"},new Object[]{client});
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        final String[]autos=auto.split(",");
        if (autos[2].equals("true")){
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Message msg=client.login(autos[0], autos[1]);
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
                    String id=b.getString("id");
                    InOutPut.writeFile(WelcomeActivity.this,"first.txt","2",null,"first写入");
                    first="2";
                    if ("true".equals(result)){
                        Bundle bundle = new Bundle();
                        Intent intent=new Intent(WelcomeActivity.this,FriendsActivity.class);
                        bundle.putSerializable("autos",autos);
                        bundle.putString("id",id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else {
                        Bundle bundle = new Bundle();
                        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                        bundle.putSerializable("autos",autos);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
            };
        }else if(autos[2].equals("false")){
            Bundle bundle = new Bundle();
            Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
            bundle.putSerializable("autos",autos);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    //返回键方法
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}