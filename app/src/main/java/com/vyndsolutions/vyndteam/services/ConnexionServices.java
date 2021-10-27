package com.vyndsolutions.vyndteam.services;


import com.google.gson.JsonObject;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Hoda on 09/02/2018.
 */

public interface ConnexionServices {
    public static final String DATA_ENDPOINT = "http://vynd-account-api-vynd-account-api-dev.azurewebsites.net/";


    @POST("login/backoffice")
    Call<ResponseBody> getAdminData (@Body JsonObject body);

    @POST("test")
    Call<ResponseBody> test (@Header("Authorization") String token, @Body JsonObject body);
}
