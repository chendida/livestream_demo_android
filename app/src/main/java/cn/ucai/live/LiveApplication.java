package cn.ucai.live;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import cn.ucai.live.data.model.IUserModel;
import cn.ucai.live.data.model.OnCompleteListener;
import cn.ucai.live.data.model.UserModel;
import cn.ucai.live.ui.activity.MainActivity;
import cn.ucai.live.utils.L;
import cn.ucai.live.utils.LiveHelper;
import cn.ucai.live.utils.PreferenceManager;
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

  @Override public void onCreate() {
    super.onCreate();
    instance = this;

    LiveHelper.getInstance().init(this);

    UStreamingContext.init(getApplicationContext(), "publish3-key");
  }

  public static LiveApplication getInstance(){
    return instance;
  }
}
