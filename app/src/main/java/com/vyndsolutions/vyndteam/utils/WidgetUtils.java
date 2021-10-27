package com.vyndsolutions.vyndteam.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

/**
 * Created by Hoda on 13/05/2018.
 */

public class WidgetUtils {
    public static int today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 6) % 7;
    public static String getDayName(int dayOfTheWeek) {

        switch (dayOfTheWeek) {
            case 0:
                return "Dimanche";
            case 1:
                return "Lundi";
            case 2:
                return "Mardi";
            case 3:
                return "Mercredi";
            case 4:
                return "Jeudi";
            case 5:
                return "Vendredi";
            case 6:
                return "Samedi";
            default:
                return "";
        }
    }
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
