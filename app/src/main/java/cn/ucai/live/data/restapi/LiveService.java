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
}
