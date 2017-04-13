package cn.ucai.live.utils;

import android.content.Context;


public class LiveModel {
    protected Context context = null;
   // protected Map<Key,Object> valueCache = new HashMap<Key,Object>();
    
    public LiveModel(Context ctx){
        context = ctx;
        PreferenceManager.init(context);
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

}
