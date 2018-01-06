package com.evan.chat.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.evan.chat.R;

public class ImageTextButton extends RelativeLayout {

    private ImageView imgView;
    private TextView  textView;
    private RelativeLayout layout;

    public ImageTextButton(Context context) {
        super(context,null);
    }

    public ImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.img_txt_bt, this,true);

        this.layout=(RelativeLayout)findViewById(R.id.layout);
        this.imgView = (ImageView)findViewById(R.id.imgView);
        this.textView = (TextView)findViewById(R.id.textView);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setImgSize(int resourceID1,int resourceID2){
        int l1=getResources().getDimensionPixelOffset(resourceID1);
        int l2=getResources().getDimensionPixelOffset(resourceID2);
        LayoutParams lyt= (LayoutParams) this.layout.getLayoutParams();
        LayoutParams img= (LayoutParams) this.imgView.getLayoutParams();
        LayoutParams txt= (LayoutParams) this.textView.getLayoutParams();
        lyt.height=l1;
        lyt.width=l1;
        this.layout.setLayoutParams(lyt);
        img.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        img.height=l2;
        img.width=l2;
        txt.addRule(RelativeLayout.CENTER_IN_PARENT);
        txt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.imgView.setLayoutParams(img);
        this.textView.setLayoutParams(txt);
    }

    public void setWeight(int weight){
        LayoutParams lyt= (LayoutParams) this.layout.getLayoutParams();
        layout.setLayoutParams(new LinearLayout.LayoutParams(lyt.width, lyt.height, weight));
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setText(int resourceID) {
        this.textView.setText(getResources().getString(resourceID));
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(getResources().getColor(color));
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }
}