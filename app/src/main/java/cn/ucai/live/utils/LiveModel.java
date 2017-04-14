package cn.ucai.live.utils;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;
import java.util.Map;

import cn.ucai.live.data.model.Gift;
import cn.ucai.live.db.GiftDao;


public class LiveModel {
    protected Context context = null;
    GiftDao dao;
    
    public LiveModel(Context ctx){
        context = ctx;
        PreferenceManager.init(context);
        dao = new GiftDao(context);
    }
    /**
     * save current username
     * @param username
     */
    public void setCurrentUserName(String username){
        PreferenceManager.getInstance().setCurrentUserName(username);
    }

    public String getCurrentUsernName(){
        return PreferenceManager.getInstance().getCurrentUsername();
    }

    public void saveGiftList(List<Gift> giftList) {
        dao.saveAppGiftList(giftList);
    }

    public Map<Integer, Gift> getGiftList() {
        return dao.getAppGiftList();
    }
}
