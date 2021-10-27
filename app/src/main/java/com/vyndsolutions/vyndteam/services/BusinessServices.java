package com.vyndsolutions.vyndteam.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hoda on 22/02/2018.
 */

public interface BusinessServices {

    public static final String DATA_ENDPOINT = "http://vynd-business-api-vynd-business-api-dev.azurewebsites.net/";

    @POST("search/v2")
    Call<ResponseBody> searchBusinessV3(@Body JsonObject requestObj, @Query("offset") int offset, @Query("limit") int limit);

    @POST("search/test")
    Call<ResponseBody> getDefaultBusinesses(@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

    @GET("autocomplete/{search}")
    Call<ResponseBody> getAutocomplete(@Path("search") String search, @Query("limit") int limit);

    @DELETE("{id}")
    Call<ResponseBody> deleteBusiness(@Header("Authorization") String token, @Path("id") int id);

    @POST("{id}/subcategories")
    Call<ResponseBody> editSubCatefory(@Header("Authorization") String token, @Path("id") int businessId, @Body JsonObject postParams);

    @GET("autocomplete/{q}")
    Call<ResponseBody> getAutocompleteBusiness(@Path("q") String q);

    @POST("{id}/beer/price")
    Call<ResponseBody> editBeerPriceBusiness(@Header("Authorization") String token, @Path("id") int businessID, @Body JsonObject requestObj);


    @GET("auto/{q}")
    Call<ResponseBody> autoCompleteSearch(@Path("q") String q);

    @POST("search")
    Call<ResponseBody> getFilteredBusinesses(@Body JsonObject body, @Query("offset") int offset, @Query("limit") int limit);

    @GET("{businessID}")
    Call<ResponseBody> getBusinessById(@Path("businessID") int businessID);

    @GET("{id}/images")
    Call<ResponseBody> getImagesFromId(@Path("id") int id, @Query("offset") int offset, @Query("limit") int limit);

    @POST("/add")
    Call<ResponseBody> addBusiness(@Body JsonObject body);

    @Multipart
    @POST("{id}/image/cover")
    Call<ResponseBody> addImageCover(@Path("id") int id, @Part MultipartBody.Part body);

    @Multipart
    @POST("{id}/image")
    Call<ResponseBody> addImagesGallerie(@Path("id") int id, @Part MultipartBody.Part body);

    @POST("{id}/additional-info-business")
    Call<ResponseBody> addAdditionalInfosToBusiness(@Header("Authorization") String accessToken, @Path("id") int id, @Body JsonArray body);

    @POST("{id}/edit/basic")
    Call<ResponseBody> editBasicBusiness(@Header("Authorization") String accessToken, @Path("id") int businessID, @Body JsonObject requestObj);

    @POST("{id}/happyhours")
    Call<ResponseBody> editHappyHours(@Header("Authorization") String token, @Path("id") int businessId, @Body JsonArray postParams);
    @POST("{id}/validate")
    Call<ResponseBody> validateBusiness(@Header("Authorization") String token, @Path("id") int businessId);


    @POST("menu/{businessId}")
    Call<ResponseBody> sendBusinessMenu(@Path("businessId") int businessID, @Body JsonArray requestObj);


    @Multipart
    @POST("{id}/image")
    Call<ResponseBody> addListImage(@Header("Authorization") String authorization,
                                    @Path("id") int businessId,
                                    @Part MultipartBody.Part[] file);

    @GET("{id}/additional-info-business")
    Call<ResponseBody> getAdditionnalInfosForBusiness(@Path("id") int id);

    @POST("/{id}/edit/hours")
    Call<ResponseBody> addHoraires(@Header("Authorization") String token, @Path("id") int id, @Body JsonArray body);

    @GET("menu/business/{businessId}")
    Call<ResponseBody> getMenuByBusiness(@Path("businessId") int businessId);

    @DELETE("menu/image/{id}")
    Call<ResponseBody> deleteMenuImage(@Header("Authorization") String token, @Path("id") int id);

    @DELETE("{id}/image/{imageId}")
    Call<ResponseBody> deleteBusinessPhoto(@Header("Authorization") String authorization, @Path("id") int businessID, @Path("imageId") int imageId);

    @Multipart
    @POST("menu/image/business/{id}")
    Call<ResponseBody> addPhotoBusinessMenu(@Header("Authorization") String authorization,
                                            @Path("id") int businessID,
                                            @Part MultipartBody.Part file);

    @Multipart
    @POST("menu/image/business/{id}")
    Call<ResponseBody> addListPhotoBusinessMenu(@Header("Authorization") String authorization,
                                                @Path("id") int businessID,
                                                @Part MultipartBody.Part[] file);

    @Multipart
    @POST("{id}/image/cover")
    Call<ResponseBody> updateProfileCover(@Path("id") int businessID,
                                          @Part MultipartBody.Part file);

    @Multipart
    @POST("{id}/image/profile")
    Call<ResponseBody> updateProfileImage(@Path("id") int businessID,
                                          @Part MultipartBody.Part file);

    @GET("{businessID}")
    Call<ResponseBody> getBusinessById(@Header("Authorization") String authorization, @Path("businessID") int businessID);


    @GET("{id}/stats")
    Call<ResponseBody> getByIdStat(@Header("Authorization") String token, @Path("id") int businessId);
}


