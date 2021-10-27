package com.vyndsolutions.vyndteam.factories;

import com.vyndsolutions.vyndteam.services.BusinessServices;
import com.vyndsolutions.vyndteam.services.SubCategoryServices;
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
 * Created by Hoda on 26/03/2018.
 */


    public  class SubCategoriesServiceFactory {
        static com.vyndsolutions.vyndteam.factories.SubCategoriesServiceFactory.OnConnectionTimeoutListener onConnectionTimeOutListener;

        private static SubCategoryServices subCategoryService;

        public static SubCategoryServices getSubCategoryService(){

            if (subCategoryService == null) {
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
                        .baseUrl(SubCategoryServices.DATA_ENDPOINT)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                subCategoryService = retrofit.create(SubCategoryServices.class);

            }
            return subCategoryService;
        }
        public static Response onOnIntercept(Interceptor.Chain chain )throws IOException {
            try {
                Response response = chain.proceed(chain.request());
                String content = response.body().toString();
                return response.newBuilder().body(ResponseBody.create(response.body().contentType(),content)).build();

            }catch(SocketTimeoutException exception){
                exception.printStackTrace();
                if(onConnectionTimeOutListener != null)
                    onConnectionTimeOutListener.onConnectionTimeout();

            }
            return chain.proceed(chain.request());
        }
        public interface OnConnectionTimeoutListener{
            void onConnectionTimeout();
        }

    }
