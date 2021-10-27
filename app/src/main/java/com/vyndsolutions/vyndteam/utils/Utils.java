package com.vyndsolutions.vyndteam.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.models.MenuType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 09/03/2018.
 */

public class Utils {

    public static final int TIMEOUT = 15;
    public static final int LIMIT = 10;
    public  static  String token ="";
        public  static JsonArray hourItem = new JsonArray();
        public  static JsonArray HappyHour = new JsonArray();
        public static List<MenuType> menuTypes = new ArrayList<>();
        public static List<ImageGalerie> imageGaleriesMenu = new ArrayList<>();
        public static double latitude = 36.806495;
        public static double longitude  = 10.181532;
        public static String adress  = "";
        public static boolean isModifiable = false ;
        public static boolean isAdded = false ;
        public static List<ImageGalerie> imageGaleries ;
        private static Gson gson;

        public static Gson getGsonInstance() {
                if (gson == null) {
                        gson = new Gson();
                }
                return gson;
        }

    public static void hideKeyboard(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) appCompatActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if (appCompatActivity.getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(appCompatActivity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }


}

