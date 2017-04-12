package cn.ucai.live.data.model;

import android.content.Context;

import java.io.File;

import cn.ucai.live.utils.I;
import cn.ucai.live.utils.OkHttpUtils;


/**
 * Created by Administrator on 2017/3/29.
 */

public class UserModel implements IUserModel {
    @Override
    public void register(Context context, String userName, String nick, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD,password)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void unregister(Context context, String userName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void login(Context context, String userName, String password, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void loadUserInfo(Context context, String userName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void updateUserNick(Context context, String userName, String userNick, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,userNick)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void updateUserAvatar(Context context, String userName, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,userName)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void addContact(Context context, String userName, String contactName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,userName)
                .addParam(I.Contact.CU_NAME,contactName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void deleteContact(Context context, String userName, String contactName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,userName)
                .addParam(I.Contact.CU_NAME,contactName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void loadContactList(Context context, String userName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }
}
