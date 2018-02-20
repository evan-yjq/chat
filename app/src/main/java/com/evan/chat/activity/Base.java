package com.evan.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.evan.chat.R;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/9
 * Time: 13:23
 */
public class Base extends AppCompatActivity {
    public static final int RIGHT = 0;
    public static final int LEFT = 1;

    protected void openActivity(Class<?> cls, int w, Bundle b){
        openActivity(this,cls,b);
        if (w==RIGHT){
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else if(w==LEFT){
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
    }

    public static void openActivity(Context context,Class<?> cls,Bundle b){
        Intent intent = new Intent(context,cls);
        if (b!=null) intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
