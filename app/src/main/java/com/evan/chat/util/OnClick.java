package com.evan.chat.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnClick {

    /**
     * @param activity 当前Activity
     * @param layout 点击事件主体
     * @param i Class
     * @param finish 是否结束当前Activity
     */
    //OnClickChangePage
    public static void setUrl(final Activity activity, final View layout, final int i, final boolean finish){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, Url.classes[i]);
                activity.startActivity(intent);
                if(finish){
                    activity.finish();
                }
            }
        });
    }

    public static void print(final Activity activity , final View layout){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(layout.getId());
            }
        });
    }
}
