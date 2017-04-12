package cn.ucai.live;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import cn.ucai.live.data.model.IUserModel;
import cn.ucai.live.data.model.OnCompleteListener;
import cn.ucai.live.data.model.UserModel;
import cn.ucai.live.ui.activity.MainActivity;
import cn.ucai.live.utils.L;
import cn.ucai.live.utils.Result;
import cn.ucai.live.utils.ResultUtils;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.User;
import com.ucloud.ulive.UStreamingContext;

/**
 * Created by wei on 2016/5/27.
 */
public class LiveApplication extends Application{
  private static final String TAG = "LiveApplication";
  private static LiveApplication instance;



  String username;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;

    initChatSdk();

    //UEasyStreaming.initStreaming("publish3-key");

    UStreamingContext.init(getApplicationContext(), "publish3-key");
  }

  public static LiveApplication getInstance(){
    return instance;
  }

  private void initChatSdk(){
    //EMOptions options = new EMOptions();
    //options.enableDNSConfig(false);
    //options.setRestServer("120.26.4.73:81");
    //options.setIMServer("120.26.4.73");
    //options.setImPort(6717);

    EaseUI.getInstance().init(this, null);
    EMClient.getInstance().setDebugMode(true);

    EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
      @Override public void onConnected() {

      }

      @Override public void onDisconnected(int errorCode) {
        if(errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE)
        {
          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("conflict", true);
          startActivity(intent);
        }
      }
    });
  }
  /**
   * get current user's id
   */
  public String getCurrentUsernName(){
    return username;
  }

  /**
   * set current username
   * @param username
   */
  public void setCurrentUserName(String username){
    this.username = username;
  }

}
