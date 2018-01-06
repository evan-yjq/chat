package com.evan.chat.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.evan.chat.R;

/**
 * Created by yejiaquan on 16/7/7.
 */
public class SearchButton extends RelativeLayout {
    private ImageView imgView;
    private TextView textView;

    public SearchButton(Context context) {
        super(context,null);
    }

    public SearchButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.img_search_bt, this,true);

        this.imgView = (ImageView)findViewById(R.id.imgView);
        this.textView = (TextView)findViewById(R.id.textView);

        this.setClickable(true);
        this.setFocusable(true);
    }
    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setText(int resourceID) {
        this.textView.setText(getResources().getString(resourceID));
    }
}
