package com.vyndsolutions.vyndteam.factories;

import com.vyndsolutions.vyndteam.services.BusinessServices;
import com.vyndsolutions.vyndteam.utils.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hoda on 22/03/2018.
 */

public  class BusinessServiceFactory {

    public interface OnConnectionTimeoutListener{
        void onConnectionTimeout();
    }

}