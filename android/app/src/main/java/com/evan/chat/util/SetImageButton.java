package com.evan.chat.util;


import com.evan.chat.view.*;

public class SetImageButton {

    public static void setDialogButton(DialogButton button,String content,int user,Integer userHead){
        button.setContent(content);
        button.setUser(user);
        if(userHead!=null)
        button.setUserHead(userHead);
    }

    public static boolean setImageTextButton(ImageTextButton button, Object text, int image, Integer textColor){
        switch (text.getClass().getSimpleName()) {
            case "String":
                button.setText((String) text);
                break;
            case "Integer":
                button.setText((int) text);
                break;
            default:
                button.setText("");
                break;
        }
        button.setImgResource(image);
        if(textColor!=null)
        button.setTextColor(textColor);
        return true;
    }
}
