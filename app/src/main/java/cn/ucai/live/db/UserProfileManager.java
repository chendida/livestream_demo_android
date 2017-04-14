package cn.ucai.live.db;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.live.data.model.IUserModel;
import cn.ucai.live.data.model.OnCompleteListener;
import cn.ucai.live.data.model.UserModel;
import cn.ucai.live.data.restapi.ApiManager;
import cn.ucai.live.data.restapi.LiveException;
import cn.ucai.live.utils.I;
import cn.ucai.live.utils.L;
import cn.ucai.live.utils.PreferenceManager;
import cn.ucai.live.utils.Result;
import cn.ucai.live.utils.ResultUtils;

public class UserProfileManager {
	private static final String TAG = UserProfileManager.class.getSimpleName();

	/**
	 * application context
	 */
	protected Context appContext = null;

	/**
	 * init flag: test if the sdk has been inited before, we don't need to init
	 * again
	 */
	private boolean sdkInited = false;
	IUserModel userModel;

	private User currentAppUser;//App服务器当前用户
	public UserProfileManager() {
	}

	public synchronized boolean init(Context context) {
		if (sdkInited) {
			return true;
		}
		sdkInited = true;
		userModel = new UserModel();
		appContext = context;
		return true;
	}

	public synchronized void reset() {
		currentAppUser = null;
		PreferenceManager.getInstance().removeCurrentUserInfo();
	}

	public synchronized User getCurrentAppUserInfo(){
		if (currentAppUser == null || currentAppUser.getMUserName() == null){
			String username = EMClient.getInstance().getCurrentUser();//获取环信服务器上的用户名
			currentAppUser = new User(username);//创建我们服务器所要保存的User对象
			String nick = getCurrentUserNick();//获取环信服务器上的用户昵称
			currentAppUser.setMUserNick((nick != null) ? nick : username);//设置服务器保存的用户昵称
			currentAppUser.setAvatar(getCurrentUserAvatar());//设置服务器保存的用户头像
		}
		return currentAppUser;
	}

	public void uploadUserAvatar(File file) {
		userModel.updateUserAvatar(appContext, EMClient.getInstance().getCurrentUser(), file,
				new OnCompleteListener<String>() {
					@Override
					public void onSuccess(String r) {
						L.e(TAG, "uploadAppUserAvatar,r = " + r);
						boolean success = false;
						if (r != null) {
							Result result = ResultUtils.getResultFromJson(r, User.class);
							L.e(TAG, "uploadAppUserAvatar,result = " + result);
							if (result != null && result.isRetMsg()) {
								final User user = (User) result.getRetData();
								L.e(TAG, "uploadAppUserAvatar,user = " + user);
								if (user != null) {
									success = true;
									L.e(TAG, "uploadAppUserAvatar,user = " + user);
									setCurrentAppUserAvatar(user.getAvatar());
								}
							}
						}
						appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
								.putExtra(I.Avatar.UPDATE_TIME, success));
					}

					@Override
					public void onError(String error) {
						L.e(TAG,"onError,error = " + error);
						appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
								.putExtra(I.Avatar.UPDATE_TIME, false));
					}
				});
	}
	public void asyncGetAppCurrentUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					User user = ApiManager.get().loadUserInfo(EMClient.getInstance().getCurrentUser());
					L.e(TAG,"asyncGetAppCurrentUserInfo(),user = " + user);
					if (user != null){
						updateCurrentAppUserInfo(user);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (LiveException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void updateCurrentAppUserInfo(User user) {
		currentAppUser = user;
		setCurrentAppUserNick(user.getMUserNick());
		L.e(TAG,"user.getMUserNick() = " + user.getMUserNick());
		setCurrentAppUserAvatar(user.getAvatar());
	}

	private void setCurrentAppUserNick(String nickname){
		getCurrentAppUserInfo().setMUserNick(nickname);//保存用户昵称到内存中
		PreferenceManager.getInstance().setCurrentUserNick(nickname);//保存用户昵称到Shareprefrence中
	}
	private void setCurrentAppUserAvatar(String avatar){
		getCurrentAppUserInfo().setAvatar(avatar);//保存用户头像到内存中
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);//保存用户头像到Shareprefrence中
	}

	private String getCurrentUserNick() {
		return PreferenceManager.getInstance().getCurrentUserNick();
	}

	private String getCurrentUserAvatar() {
		return PreferenceManager.getInstance().getCurrentUserAvatar();
	}
}
