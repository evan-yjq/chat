package test.yejiaquan.com.sql.btn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import test.yejiaquan.com.sql.R;
import test.yejiaquan.com.sql.activity.UserActivity;
import test.yejiaquan.com.sql.util.OnClick;
import test.yejiaquan.com.sql.util.Url;

public class TopTitleButton extends RelativeLayout {

    private ImageView back;
    private TextView title;
    private ImageView more;

    public TopTitleButton(Context context) {
        super(context,null);
    }

    public TopTitleButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.title_top_btn, this,true);

        this.back=(ImageView)findViewById(R.id.back);
        this.title = (TextView)findViewById(R.id.title);
        this.more = (ImageView)findViewById(R.id.more);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void onClickBack(final Activity activity){
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
    }

    public void onClickSeeMore(final Activity activity){
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, UserActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void setBack(int resourceID) {
        this.back.setImageResource(resourceID);
    }

    public void setMore(int resourceID) {
        this.more.setImageResource(resourceID);
    }

    public void setTitle(int resourceID) {
        this.title.setText(resourceID);
    }

    public void setTitle(String text) {
        this.title.setText(text);
    }

    public void setTitleTextSize(float size){
        this.title.setTextSize(size);
    }
}
