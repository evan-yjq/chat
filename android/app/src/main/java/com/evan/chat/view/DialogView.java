package com.evan.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.evan.chat.R;

public class DialogView extends RelativeLayout {

    public static final int MY = 0;
    public static final int FRIEND = 1;
    public static final int PROMPT = 2;


    private CircleImageView userHead;
    private TextView dialogContent;

    private RelativeLayout head;
    private RelativeLayout dialog;


    public DialogView(Context context) {
        this(context, null);
    }

    public DialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.dialog, this,true);

        userHead= findViewById(R.id.user_head);
        dialogContent= findViewById(R.id.dialog_content);

        head= findViewById(R.id.head);
        dialog= findViewById(R.id.dialog);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setUser(int rule){
        LayoutParams hd= (LayoutParams) this.head.getLayoutParams();
        LayoutParams dl= (LayoutParams) this.dialog.getLayoutParams();
        if(rule==MY){
            hd.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            dl.addRule(RelativeLayout.LEFT_OF,R.id.head);
            dl.leftMargin=160;
            setBackground(R.drawable.dialog_my);
        }else if(rule==FRIEND){
            hd.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            dl.addRule(RelativeLayout.RIGHT_OF,R.id.head);
            dl.rightMargin=160;
        }else if(rule==PROMPT){
            dl.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
    }

    public void setContent(String content) {
        this.dialogContent.setText(content);
    }

    public void setBackground(int drawable){
        dialog.setBackground(getResources().getDrawable(drawable));
    }

    public void setTextColor(int color){
        this.dialogContent.setTextColor(color);
    }

    public void setUserHead(int userHead) {
        this.userHead.setImageResource(userHead);
    }

    public TextView getDialog(){
        return dialogContent;
    }
}
