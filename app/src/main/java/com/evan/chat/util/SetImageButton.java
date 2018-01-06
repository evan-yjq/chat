package com.evan.chat.util;


import com.evan.chat.view.*;

public class SetImageButton {
    public static boolean setDialogButton(DialogButton button,String content,int user,Integer userHead){
        button.setContent(content);
        button.setUser(user);
        if(userHead!=null)
        button.setUserHead(userHead);
        return true;
    }

    public static boolean setTopTitleButton(TopTitleButton button, int back, Object title, int more){
        button.setBack(back);
        button.setMore(more);
        if(title.getClass().getSimpleName().equals("String")){
            button.setTitle((String)title);return true;
        }else if(title.getClass().getSimpleName().equals("Integer")){
            button.setTitle((int)title);return true;
        }else{
            button.setTitle("");return false;
        }
    }

//    public static boolean setHeadLineButton(SmallTitleButton button, Object title, Object seeMore, int arrow){
////        button.setImgTitle(imgTitle);
//        if(title.getClass().getSimpleName().equals("String")){
//            button.setTitle((String)title);
//        }else if(title.getClass().getSimpleName().equals("Integer")){
//            button.setTitle((int)title);
//        }else{
//            button.setTitle("");
//        }
//        if(seeMore.getClass().getSimpleName().equals("String")){
//            button.setSeeMore((String)seeMore);
//        }else if(seeMore.getClass().getSimpleName().equals("Integer")){
//            button.setSeeMore((int)seeMore);
//        }else{
//            button.setSeeMore("");
//        }
//        button.setArrow(arrow);
//        return true;
//    }

    public static boolean setImageTextButton(ImageTextButton button, Object text, int image, Integer textColor){
        if(text.getClass().getSimpleName().equals("String")){
            button.setText((String)text);
        }else if(text.getClass().getSimpleName().equals("Integer")){
            button.setText((int)text);
        }else{
            button.setText("");
        }
        button.setImgResource(image);
        if(textColor!=null)
        button.setTextColor(textColor);
        return true;
    }

    public static boolean setFriendsButton(FriendsButton button, int userHead, String username, String signature, String state){
        button.setUserHead(userHead);
        button.setUsername(username);
        button.setSignature(signature);
        button.setState(state);
        return true;
    }

//    public static boolean setBBSLineButton(BBSLineButton button, int img, String title, String content){
//        button.setImg(img);
//        button.setTitle(title);
//        button.setContent(content);
//        return true;
//    }
}
