package com.evan.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.evan.chat.R;

public class EditButton extends RelativeLayout {

    private EditText chat;
    private Button send;

    public EditButton(Context context) {
        super(context);
    }

    public EditButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_bottom_text, this,true);

        chat= findViewById(R.id.chat);
        send= findViewById(R.id.send);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public Button getSend() {
        return send;
    }

    public void setSendText(String text){
        send.setText(text);
    }

    public EditText getChat() {
        return chat;
    }
}
