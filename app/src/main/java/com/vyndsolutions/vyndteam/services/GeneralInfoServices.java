package com.vyndsolutions.vyndteam.services;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hoda on 15/03/2018.
 */

public interface GeneralInfoServices {
    public static final String DATA_ENDPOINT ="http://vynd-generalinfo-api-vynd-generalinfo-api-dev.azurewebsites.net/";


    @GET("classification/general-info")
    Call<ResponseBody> getGeneralInfo();

    @GET("location/type/{id}")
    Call<ResponseBody> getAllRegions(@Path("id") int id, @Query("offset") int offset, @Query("limit") int limit);
    @GET("classification/type/{id}")
    Call<ResponseBody> getClassificationByType(@Path("id") int id, @Query("offset") int offset, @Query("limit") int limit);

    @POST("location/filter")
    Call<ResponseBody> getAllRegions(@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

    @POST("location/filter")
    Call<ResponseBody> getSearchBusinesses(@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

    @POST("location/autocompele")
    Call<ResponseBody> getSearchAutoRegions(@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

}
