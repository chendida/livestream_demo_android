package cn.ucai.live.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

import cn.ucai.live.data.model.Gift;
import cn.ucai.live.data.restapi.ApiManager;
import cn.ucai.live.data.restapi.LiveException;

public class GiftService extends Service {
    private final IBinder binder = new GiftBinder();
    public GiftService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class GiftBinder extends Binder{
        public GiftService getService(){
            return GiftService.this;
        }
    }

    public List<Gift> getGiftList(){
        try {
            return ApiManager.get().getAllGifts();
        } catch (LiveException e) {
            e.printStackTrace();
        }
        return null;
    }
}
