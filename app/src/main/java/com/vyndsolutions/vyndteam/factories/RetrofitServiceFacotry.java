package com.vyndsolutions.vyndteam.factories;

import com.vyndsolutions.vyndteam.services.BusinessServices;
import com.vyndsolutions.vyndteam.services.GeneralInfoServices;
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
 * Created by Hoda on 15/03/2018.
 */

public class RetrofitServiceFacotry {

    static OnConnectionTimeoutListener onConnectionTimeOutListener;
    private static BusinessServices businessService;

    private static GeneralInfoServices generalInfoService;

    public static GeneralInfoServices getGeneralInfoService() {

        if (generalInfoService == null) {
            OkHttpClient client = new OkHttpClient();

            client.newBuilder().connectTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().readTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return onOnIntercept(chain);
                }
            }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralInfoServices.DATA_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            generalInfoService = retrofit.create(GeneralInfoServices.class);

        }
        return generalInfoService;
    }
   /* public static BusinessServices getBusinessApiRetrofitServiceClient() {

        if (businessService == null) {
            OkHttpClient client = new OkHttpClient();
            client.newBuilder().connectTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().readTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return onOnIntercept(chain);
                }
            }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(businessService.BUSINESS_ENDPOINT)
//                    .client(new OkHttpClient().newBuilder().connectTimeout(Utils.TIMEOUT, TimeUnit.SECONDS).readTimeout(Utils.TIMEOUT, TimeUnit.SECONDS).build())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            businessService = retrofit.create(businessService.class);
        }
        return businessService;
    }
*/
    public static Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            String content = response.body().toString();
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();

        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            if (onConnectionTimeOutListener != null)
                onConnectionTimeOutListener.onConnectionTimeout();

        }
        return chain.proceed(chain.request());
    }


    public static BusinessServices getBusinessService() {

        if (businessService == null) {
            OkHttpClient client = new OkHttpClient();

            client.newBuilder().connectTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().readTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return onOnIntercept(chain);
                }
            }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BusinessServices.DATA_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            businessService = retrofit.create(BusinessServices.class);

        }
        return businessService;
    }


    public interface OnConnectionTimeoutListener {
        void onConnectionTimeout();
    }


}
