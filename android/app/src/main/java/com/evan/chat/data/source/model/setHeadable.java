package com.evan.chat.data.source.model;

import android.graphics.Bitmap;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 22:29
 */
public interface setHeadable {

    Long getId();

    void setHead(Bitmap head);

    Bitmap getHead();
}
