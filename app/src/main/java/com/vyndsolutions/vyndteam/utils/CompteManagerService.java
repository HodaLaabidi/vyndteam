package com.vyndsolutions.vyndteam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.GeneralInfo;
import com.vyndsolutions.vyndteam.models.Region;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 15/03/2018.
 */

public class CompteManagerService {

    public static void saveGeneralInfo(Context context, GeneralInfo generalInfo) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.remove("general_info");
        editor.putString("general_info", gson.toJson(generalInfo));
        editor.apply();
    }
    public static ArrayList<Classification> getAllSubCategories(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = Utils.getGsonInstance();
        Type type = new TypeToken<List<Classification>>() {
        }.getType();
        List<Classification> list = gson.fromJson(preferences.getString("allSubCategories", null), type);
        return (ArrayList<Classification>) list;

    }

    public static ArrayList<Region> getAllRegion(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = Utils.getGsonInstance();
        Type type = new TypeToken<List<Region>>() {
        }.getType();
        List<Region> list = gson.fromJson(preferences.getString("allRegions", null), type);
        return (ArrayList<Region>) list;
    }

    public static void saveAllRegion(Context context, List<Region> allRegions) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = Utils.getGsonInstance();
        editor.remove("allRegions");
        editor.putString("allRegions", gson.toJson(allRegions));
        editor.apply();
    }
    public static void saveAllSubCategories(Context context, List<Classification> allSubCategories) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = Utils.getGsonInstance();
        editor.remove("allSubCategories");
        editor.putString("allSubCategories", gson.toJson(allSubCategories));
        editor.apply();
    }
    public static GeneralInfo getGeneralInfo(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("general_info", null), GeneralInfo.class);

    }
}
