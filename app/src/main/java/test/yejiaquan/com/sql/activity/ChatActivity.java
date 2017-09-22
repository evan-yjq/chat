package test.yejiaquan.com.sql.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import test.yejiaquan.com.sql.R;
import test.yejiaquan.com.sql.btn.DialogButton;
import test.yejiaquan.com.sql.btn.EditButton;
import test.yejiaquan.com.sql.btn.TopTitleButton;
import test.yejiaquan.com.sql.model.Chat;
import test.yejiaquan.com.sql.model.User;
import test.yejiaquan.com.sql.util.*;

import java.util.ArrayList;
import java.util.List;

import static test.yejiaquan.com.sql.activity.Data.client;
import static test.yejiaquan.com.sql.activity.Data.handler;

public class ChatActivity extends Activity {

    private LinearLayout body;
    private User friend;
    private List<Chat> allChat;
    private String[]autos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_general);
        super.onCreate(savedInstanceState);
        friend=(User)getIntent().getSerializableExtra("friend");
        autos=(String[]) getIntent().getSerializableExtra("autos");
        ScrollView center = (ScrollView) findViewById(R.id.center);
        TopTitleButton top = (TopTitleButton) findViewById(R.id.top);
        body=(LinearLayout) findViewById(R.id.body);
        SetImageButton.setTopTitleButton(top,R.mipmap.back,friend.getUsername(),R.mipmap.category);
        top.onClickBack(this);
        top.onClickSeeMore(this);
        top.setTitleTextSize(18);

        final ScrollView mScrollView = (ScrollView) findViewById(R.id.center);
        final EditButton editChat=new EditButton(this,null);
        editChat.setSendText("发送");

        InOutPut.writeFile(ChatActivity.this,friend.getUsername()+".txt","",MODE_APPEND,friend.getUsername()+"聊天记录初始化");
        String chats= InOutPut.readFile(ChatActivity.this,friend.getUsername()+".txt","读取"+friend.getUsername()+"聊天记录");
        if (!"".equals(chats)) {
            chats ="["+chats.substring(0, chats.lastIndexOf(","))+"]";
            allChat = JSON.parseArray(chats, Chat.class);
        }else {
            allChat=null;
        }
        if (allChat != null) {
            int i = allChat.size() - 7;
            for (Chat chat : allChat) {
                if (i > 0) {
                    i--;
                    continue;
                }
                if (i == 0) {
                    DialogButton button = new DialogButton(ChatActivity.this, null);
                    button.setBackground(R.drawable.title_small);
                    SetImageButton.setDialogButton(button, "聊天记录", 2, null);
                    DynAdd.addView(body, button);
                    i--;
                }
                DialogButton button = new DialogButton(ChatActivity.this, null);
                button.setBackground(R.drawable.dialog_my);
                SetImageButton.setDialogButton(button, chat.getContent(),chat.getUser().equals(autos[0])?0:1, R.mipmap.logo);
                DynAdd.addView(body, button);
            }
        }
        editChat.getSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!editChat.getChat().getText().toString().equals(""))&&editChat.getChat().getText()!=null) {
                    DialogButton button = new DialogButton(ChatActivity.this, null);
                    button.setBackground(R.drawable.dialog_my);
                    SetImageButton.setDialogButton(button,editChat.getChat().getText().toString(),0,R.mipmap.logo);
                    DynAdd.addView(body, button);
                    Chat chat=new Chat();
                    chat.setUser(autos[0]);
                    chat.setTime(GetTime.getFormatTime());
                    chat.setContent(editChat.getChat().getText().toString());
                    final String str;
                    str =JSON.toJSONString(chat);
                    new Thread() {
                        @Override
                        public void run() {
                            client.get("chat-"+friend.getUsername()+"-"+str);
                        }
                    }.start();
                    InOutPut.writeFile(ChatActivity.this,friend.getUsername()+".txt",str+",",MODE_APPEND,"读取"+friend.getUsername()+"聊天记录");
                    editChat.getChat().setText("");
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }else{showToast("发送的消息不能为空");}
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                // 此处可以更新UI
                Bundle b = msg.getData();
                String result = b.getString("result");
                if(result!=null&&result!="") {
                    Chat chat = JSON.parseObject(result, Chat.class);
                    if (chat.getUser().equals(friend.getUsername())) {
                        write(chat);
                    }else{
                        InOutPut.writeFile(ChatActivity.this,chat.getUser()+".txt","",MODE_APPEND,chat.getUser()+"聊天记录初始化");
                        String chats= InOutPut.readFile(ChatActivity.this,chat.getUser()+".txt","读取"+chat.getUser()+"聊天记录");
                        InOutPut.writeFile(ChatActivity.this, chat.getUser() + ".txt", result + ",", MODE_APPEND, "写入" + chat.getUser() + "聊天记录");
                    }
                }
            }
        };

        DynAdd.addLayout((RelativeLayout)findViewById(R.id.bottom),editChat);

    }

    private void write(Chat chat){
        DialogButton button = new DialogButton(ChatActivity.this, null);
        button.setBackground(R.drawable.dialog_my);
        SetImageButton.setDialogButton(button, chat.getContent(), chat.getUser().equals(autos[0]) ? 0 : 1, R.mipmap.logo);
        DynAdd.addView(body, button);
        String str=JSON.toJSONString(chat);
        if (allChat==null||"".equals(allChat)) {
            allChat=new ArrayList<>();
            allChat.add(chat);
        }
        System.out.println(str);
        InOutPut.writeFile(ChatActivity.this, chat.getUser() + ".txt", str + ",", MODE_APPEND, "写入" + chat.getUser() + "聊天记录");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        //返回键方法
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
