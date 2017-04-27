package cn.ucai.live.data.restapi;

import java.util.List;


import cn.ucai.live.utils.I;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/13.
 */

public interface LiveService {
    @GET("findUserByUserName")
    Call<String> loadUserInfo(@Query(I.User.USER_NAME)String username);

    @GET("live/getAllGifts")
    Call<String> getAllGifts();

    @GET("live/createChatRoom")
    Call<String>createLiveRoom(@Query("auth")String auth,
                               @Query("name")String name,
                               @Query("description")String description,
                               @Query("owner")String owner,
                               @Query("maxusers")int maxusers,
                               @Query("members")String members);
    @GET("live/deleteChatRoom")
    Call<String>deleteLiveRoom(@Query("auth")String auth,
                               @Query("chatRoomId")String roomId);
    @GET("live/getBalance")
    Call<String>findMoney(@Query("uname")String uname);
}
