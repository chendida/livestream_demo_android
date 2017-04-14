package cn.ucai.live.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.HanziToPinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.ucai.live.LiveApplication;
import cn.ucai.live.data.model.Gift;

public class DBManager {
    private static final String TAG = "SuperWeChatDBManager";
    static private DBManager dbMgr = new DBManager();
    private DbOpenHelper dbHelper;

    private DBManager(){
        dbHelper = DbOpenHelper.getInstance(LiveApplication.getInstance().getApplicationContext());
    }

    public static synchronized DBManager getInstance(){
        if(dbMgr == null){
            dbMgr = new DBManager();
        }
        return dbMgr;
    }
    /**
     * save gift list
     * 
     * @param giftList
     */
    synchronized public void saveGiftList(List<Gift> giftList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(GiftDao.GIFT_TABLE_NAME, null, null);
            for (Gift gift : giftList) {
                ContentValues values = new ContentValues();
                if (gift.getGname() != null){
                    values.put(GiftDao.GIFT_COLUMN_NAME,gift.getGname());
                }
                if (gift.getId() != null){
                    values.put(GiftDao.GIFT_COLUMN_ID,gift.getId());
                }
                if (gift.getGurl() != null){
                    values.put(GiftDao.GIFT_COLUMN_URL,gift.getGurl());
                }
                if (gift.getGprice() != null){
                    values.put(GiftDao.GIFT_COLUMN_PRICE,gift.getGprice());
                }
                db.replace(GiftDao.GIFT_TABLE_NAME, null, values);
            }
        }
    }

    /**
     * get gift list
     * 
     * @return
     */
    synchronized public Map<Integer, Gift> getGiftList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<Integer, Gift> gifts = new Hashtable<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + GiftDao.GIFT_TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(GiftDao.GIFT_COLUMN_NAME));
                String url = cursor.getString(cursor.getColumnIndex(GiftDao.GIFT_COLUMN_URL));
                int price = cursor.getInt(cursor.getColumnIndex(GiftDao.GIFT_COLUMN_PRICE));
                int id = cursor.getInt(cursor.getColumnIndex(GiftDao.GIFT_COLUMN_ID));
                Gift gift = new Gift();
                gift.setGname(name);
                gift.setGprice(price);
                gift.setGurl(url);
                gift.setId(id);
                gifts.put(id,gift);
            }
            cursor.close();
        }
        return gifts;
    }
    
    synchronized public void closeDB(){
        if(dbHelper != null){
            dbHelper.closeDB();
        }
        dbMgr = null;
    }
}
