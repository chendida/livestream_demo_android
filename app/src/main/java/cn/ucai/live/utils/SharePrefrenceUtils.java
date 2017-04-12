package cn.ucai.live.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ucai.live.LiveApplication;


/**
 * Created by Administrator on 2017/3/21.
 */

public class SharePrefrenceUtils {
    private static final String SHARE_PREFRENCE_NAME = "cn.ucai.fulicenter_save_userinfo";
    private static final String SAVE_USERINFO_USERNAME = "m_user_username";
    static SharePrefrenceUtils instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SharePrefrenceUtils() {
        sharedPreferences = LiveApplication.getInstance().
                getSharedPreferences(SHARE_PREFRENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePrefrenceUtils getInstance() {
        if (instance == null){
            instance = new SharePrefrenceUtils();
        }
        return instance;
    }

    public void setUserName(String userName){
        editor.putString(SAVE_USERINFO_USERNAME,userName).commit();
    }
    public String getUserName(){
        return sharedPreferences.getString(SAVE_USERINFO_USERNAME,null);
    }
    public void removeUser(){
        editor.remove(SAVE_USERINFO_USERNAME).commit();
    }
}
