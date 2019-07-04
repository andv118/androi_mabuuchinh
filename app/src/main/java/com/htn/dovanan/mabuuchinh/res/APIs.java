package com.htn.dovanan.mabuuchinh.res;

import com.htn.dovanan.mabuuchinh.pojo.MaBuuChinh;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIs {

    /*
    Ma buu chinh cua recyclerview filter
     */
    @FormUrlEncoded // ma hoa du lieu
    @POST("search_all.php")
    Call<MaBuuChinh> getMaBuuChinh(@Field("text") String text);

    /*
    Ma buu chinh detail
     */
    @FormUrlEncoded
    @POST("search_detail.php")
    Call<MaBuuChinh> getMbcDetail(@Field("mbc") String mbc, @Field("donvi") String donVi);

    /* user for search location map activity */
    @FormUrlEncoded
    @POST("search_map.php")
    Call<MaBuuChinh> getMbcMap(@Field("text") String text);

}