/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.live.db;

import android.content.Context;

import java.util.List;
import java.util.Map;

import cn.ucai.live.data.model.Gift;

public class GiftDao {
	public static final String GIFT_TABLE_NAME = "t_live_gifts";
	public static final String GIFT_COLUMN_ID = "m_gift_id";
	public static final String GIFT_COLUMN_NAME= "m_gift_name";
	public static final String GIFT_COLUMN_URL = "m_gift_url";
	public static final String GIFT_COLUMN_PRICE= "m_gift_price";
	public GiftDao(Context context) {
	}
	/**
	 * save gift list
	 *
	 * @param giftList
	 */
	public void saveAppGiftList(List<Gift> giftList) {
		DBManager.getInstance().saveGiftList(giftList);
	}

	/**
	 * get gift list
	 *
	 * @return
	 */
	public Map<Integer,Gift> getAppGiftList() {
		return DBManager.getInstance().getGiftList();
	}
}
