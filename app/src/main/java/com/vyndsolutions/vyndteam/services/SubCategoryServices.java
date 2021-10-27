package com.vyndsolutions.vyndteam.services;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hoda on 26/02/2018.
 */

public interface SubCategoryServices{

        public static final String DATA_ENDPOINT = "http://vynd-generalinfo-api-vynd-generalinfo-api-dev.azurewebsites.net/";

        @POST("classification/search")
        Call<ResponseBody> getSubCategories (@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

}
