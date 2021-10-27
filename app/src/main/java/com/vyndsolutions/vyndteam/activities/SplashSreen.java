package com.vyndsolutions.vyndteam.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.models.GeneralInfo;
import com.vyndsolutions.vyndteam.models.Region;
import com.vyndsolutions.vyndteam.utils.CompteManagerService;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SplashSreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;
    private ArrayList<Region> regions=new ArrayList<>();

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_sreen);

            runAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                Intent intent = new Intent(SplashSreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }}, SPLASH_TIME_OUT);
           getGeneralInfo() ;
           getAllRegions(this);

       }

    private boolean getAllRegions(final Context context) {

        if (CompteManagerService.getAllRegion(context) != null) {
            regions = CompteManagerService.getAllRegion(context);
        } else if (ConnectivityService.isOnline(context)) {
            Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getAllRegions(1, 0, 1000);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        regions = new ArrayList<Region>();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = response.code();
                        if (code == 200) {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            Gson gson = Utils.getGsonInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                regions.add(gson.fromJson(jsonArray.getString(i), Region.class));
                            }
//                            CompteManagerService.saveAllSubCategories(context, listSubCategories);
                            CompteManagerService.saveAllRegion(context, regions);


                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        if (context != null) {

                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (!call.isCanceled()) {

                    }
                }

            });
        }

        return true;

    }

    private void runAnimation(){

        Animation animation  = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        animation.reset();
        LinearLayout linearLayout = findViewById(R.id.text_splash_sreen);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);
    }
    private void getGeneralInfo() {

        Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getGeneralInfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = response.code();
                    if (code == 200) {

                        Gson gson = new Gson();
                       GeneralInfo generalInfo = gson.fromJson(jsonObject.toString(), GeneralInfo.class);
                        CompteManagerService.saveGeneralInfo(SplashSreen.this, generalInfo);

                        //InformationsActivity.initializeFilterLists();


                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



}

