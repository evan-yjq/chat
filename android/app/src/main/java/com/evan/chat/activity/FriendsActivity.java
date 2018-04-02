package com.evan.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.evan.chat.R;
import com.evan.chat.view.FriendsButton;
import com.evan.chat.view.SearchButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.evan.chat.activity.Data.*;

public class FriendsActivity extends Activity {
    //top
    private SearchButton searchFriend;
    private ImageView addFriend;
    private ImageView logout;
    private ImageView refresh;
    private Handler h;
    private Handler h2;
    private ArrayList<FriendsButton>arrayList=new ArrayList<>();
    private String[]autos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_frag);
        buttonInit();
        autos=(String[]) getIntent().getSerializableExtra("autos");
        final String id=getIntent().getStringExtra("id");
//        InOutPut.writeFile(this,"friends.txt","",MODE_APPEND,"好友列表初始化");

        h=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 此处可以更新UI
                Bundle b = msg.getData();
                String result = b.getString("result");
                if ("true".equals(result)){
                    Bundle bundle = new Bundle();
//                    Intent intent=new Intent(FriendsActivity.this,LoginActivity.class);
//                    bundle.putSerializable("autos",autos);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    finish();
                }else if(!"false".equals(result)){
                }
            }
        };
        if (first.equals("1")){
            getFriends(id);
        }else {
            friend();
        }
        h2=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 此处可以更新UI
                Bundle b = msg.getData();
                String result = b.getString("result");
                System.out.println(result);
                if (!"".equals(result)&&result!=null){
//                    InOutPut.writeFile(FriendsActivity.this,"friends.txt",result,null,"写入好友列表");
                    friend();
                }
            }
        };
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
//                        Message msg=client.get("logout-"+id);
//                        h.sendMessage(msg);
                    }
                }.start();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriends(id);
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        Data.thread=new Thread(){
            @Override
            public void run(){
                try{
                    //3、获取输入流，并读取服务器端的响应信息
                    BufferedReader br = null;
                    br = new BufferedReader(new InputStreamReader(socketl.getInputStream()));
                    String info = null;
                    while (true) {
                        if (flag)break;
                        info = br.readLine();
                        if (info != null && !"end".equals(info)&&!"".equals(info)) {
                            Bundle b = new Bundle();
                            b.putString("result", info);
                            System.out.println("接收到" + info);
                            Message msg = new Message();
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 此处可以更新UI
                Bundle b = msg.getData();
                String result = b.getString("result");
                if(result!=null&&result!="") {
//                    Chat chat = JSON.parseObject(result, Chat.class);
//                    InOutPut.writeFile(FriendsActivity.this,chat.getUser()+".txt","",MODE_APPEND,chat.getUser()+"聊天记录初始化");
//                    String chats= InOutPut.readFile(FriendsActivity.this,chat.getUser()+".txt","读取"+chat.getUser()+"聊天记录");
//                    InOutPut.writeFile(FriendsActivity.this, chat.getUser() + ".txt", result + ",", MODE_APPEND, "写入" + chat.getUser() + "聊天记录");

                }
            }
        };

        socket(autos[0]);
    }
    public void socket(final String name){
        new Thread() {
            @Override
            public void run() {
                try {
                    //2、获取输出流，向服务器端发送信息
                    PrintWriter pw =new PrintWriter(socketl.getOutputStream());//将输出流包装成打印流
                    pw.write("come-"+name+"\nend\n");
                    pw.flush();
                } catch (IOException e) {
                    System.out.println("Client chat fail");
                }
            }
        }.start();
        thread.start();
    }
    private void buttonInit(){
        //top栏按钮
        //退出登录
        logout=(ImageView)findViewById(R.id.logout);
        refresh=(ImageView)findViewById(R.id.refresh);
        //查找好友
        searchFriend=(SearchButton)findViewById(R.id.search_friend);
        searchFriend.setText(R.string.search_friend);
        //添加好友
        addFriend=(ImageView)findViewById(R.id.add_friend);
    }

    private void getFriends(final String id){
        new Thread(){
            @Override
            public void run() {
                super.run();
//                Message msg=client.get("friend-"+id);
//                h2.sendMessage(msg);
            }
        }.start();
    }

    private void friend(){
//        String f=InOutPut.readFile(FriendsActivity.this,"friends.txt","读取好友列表");
//        if (!"".equals(f)) {
//            for (int i = 0; i < arrayList.size(); i++) {
//                ((LinearLayout) findViewById(R.id.all_friend)).removeView(arrayList.get(i));
//            }
//            List<User> allFriend = JSON.parseArray(f, User.class);
//            for (final User friend : allFriend) {
//                FriendsButton friendBtn = new FriendsButton(FriendsActivity.this, null);
//                arrayList.add(friendBtn);
////                SetImageButton.setFriendsButton(friendBtn, R.mipmap.logo, friend.getUsername(), friend.getSignature(), friend.getState());
//                DynAdd.addLayout((LinearLayout) findViewById(R.id.all_friend), friendBtn);
//                friendBtn.getButton().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
////                        bundle.putSerializable("friend", friend);
//                        bundle.putSerializable("autos", autos);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
    }

    //返回键方法
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
