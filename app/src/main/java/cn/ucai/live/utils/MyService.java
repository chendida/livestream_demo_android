package cn.ucai.live.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hyphenate.chat.EMClient;

import cn.ucai.live.data.restapi.ApiManager;
import cn.ucai.live.data.restapi.LiveService;

public class MyService extends IntentService {
    private static final String TAG = "MyService";

    public MyService() {
        super(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userName = intent.getStringExtra(I.User.USER_NAME);
        ApiManager.get().getMoney(userName);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
