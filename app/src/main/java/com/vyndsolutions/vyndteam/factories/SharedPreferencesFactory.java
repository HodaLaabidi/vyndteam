package com.vyndsolutions.vyndteam.factories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vyndsolutions.vyndteam.models.Admin;
import com.vyndsolutions.vyndteam.models.AvailableTime;
import com.vyndsolutions.vyndteam.models.HoursHoraires;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.models.MenuType;
import com.vyndsolutions.vyndteam.utils.ConstSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Hoda on 09/03/2018.
 */

public class SharedPreferencesFactory {

    public static  SharedPreferences pref; // 0 - for private mode
    public static  SharedPreferences.Editor editor;
    public static void initializeShared(Context context)
    {
        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    public  static   void saveAdminData(Context context , Admin admin ){

        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString("token" , admin.getToken());
        editor.commit();

    }
    public  static   void removeSession(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userSession");
        editor.apply();

    }

    public static Admin getAdminData (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        String email = sharedPreferences.getString("mail", null);
        String token = sharedPreferences.getString("token", null);


        //Admin admin = new Admin(email);
        Admin admin = new Admin(email ,token);
        return admin;

    }

    public static void saveAdminPasswd(Context context, String passwd , Admin admin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        admin.setPassword(passwd);
        editor.putString("password",passwd);
        editor.commit();
    }



    public static String getAdminPasswd (Context context ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        String password = sharedPreferences.getString("password", null);

        return password;

    }

    public static void saveAdminToken(Context context,Admin admin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        admin.setToken("Bearer "+ admin.getToken());
        Log.e("token = ", admin.getToken());
        editor.putString("token",admin.getToken());
        editor.commit();
    }

    public static String getAdminToken (Context context ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstSharedPreferences.ADMIN_DATA,context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    // List photos gallerie


    public static void saveListPhotosGallerie(Context context , ArrayList<ImageGalerie> imagesGallerie) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(imagesGallerie);
        editor.putString(ConstSharedPreferences.ARRAYLIST_PHOTOGALLERIE, json);
        editor.apply();

    }

    public static ArrayList<ImageGalerie> getListPhotosGallerie(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(ConstSharedPreferences.ARRAYLIST_PHOTOGALLERIE, null);
        Type type = new TypeToken<ArrayList<ImageGalerie>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // list photo menu
    public static void saveListPhotosMenu(Context context , ArrayList<ImageGalerie> PhotosMenu) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(PhotosMenu);
        editor.putString(ConstSharedPreferences.ARRAYLIST_PHOTOGALLERIE, json);
        editor.apply();

    }

    public static ArrayList<ImageGalerie> getListPhotosMenu(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(ConstSharedPreferences.ARRAYLIST_PHOTOMENU, null);
        Type type = new TypeToken<ArrayList<ImageGalerie>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // list MenuType
    public static void saveListMenuType(Context context , ArrayList<MenuType> menuTypes) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(menuTypes);
        editor.putString(ConstSharedPreferences.ARRAYLIST_MENUTYPE, json);
        editor.apply();

    }

    public static ArrayList<MenuType> getListMenuType(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(ConstSharedPreferences.ARRAYLIST_PHOTOGALLERIE, null);
        Type type = new TypeToken<ArrayList<ImageGalerie>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // arrayList of horaireObject

    public static void saveListHoraires(Context context , ArrayList<AvailableTime> HoursHoraires) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(HoursHoraires);
        editor.putString(ConstSharedPreferences.ARRAYLIST_HORAIRES, json);
        editor.apply();

    }

    public static ArrayList<HoursHoraires> getListHoraires(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(ConstSharedPreferences.ARRAYLIST_HORAIRES, null);
        Type type = new TypeToken<ArrayList<HoursHoraires>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
