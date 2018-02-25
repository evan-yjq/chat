package com.evan.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.evan.chat.R;

public class FriendsButton extends LinearLayout {
    private CircleImageView userHead;
    private TextView username;
    private TextView signature;
    private TextView state;
    private RelativeLayout friend;
//    private Button button;

    public FriendsButton(Context context) {
        super(context,null);
    }

    public FriendsButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.friends_btn, this,true);

        userHead= findViewById(R.id.user_head);
        username= findViewById(R.id.username);
        signature= findViewById(R.id.signature);
        state= findViewById(R.id.state);
        friend= findViewById(R.id.friend);
//        button= (Button) findViewById(R.id.button);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public RelativeLayout getButton() {
        return friend;
    }

    public void setUserHead(int userHead) {
        this.userHead.setImageResource(userHead);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setSignature(String signature) {
        this.signature.setText(signature);
    }

    public void setState(String state) {
        this.state.setText(state);
    }
}
