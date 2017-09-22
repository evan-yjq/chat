package test.yejiaquan.com.sql.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import test.yejiaquan.com.sql.R;

public class DialogButton extends RelativeLayout {

    private CircleImageView userHead;
    private TextView dialogContent;

    private RelativeLayout head;
    private RelativeLayout dialog;


    public DialogButton(Context context) {
        super(context);
    }

    public DialogButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.dialog, this,true);

        userHead= (CircleImageView) findViewById(R.id.user_head);
        dialogContent= (TextView) findViewById(R.id.dialog_content);

        head= (RelativeLayout) findViewById(R.id.head);
        dialog= (RelativeLayout) findViewById(R.id.dialog);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setUser(int i){
        LayoutParams hd= (LayoutParams) this.head.getLayoutParams();
        LayoutParams dl= (LayoutParams) this.dialog.getLayoutParams();
        if(i==0){
            hd.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            dl.addRule(RelativeLayout.LEFT_OF,R.id.head);
            dl.leftMargin=160;
        }else if(i==1){
            hd.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            dl.addRule(RelativeLayout.RIGHT_OF,R.id.head);
            dl.rightMargin=160;
        }else if(i==2){
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
