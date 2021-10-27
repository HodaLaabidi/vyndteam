package com.vyndsolutions.vyndteam.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.models.HappyHour;
import com.vyndsolutions.vyndteam.models.Hour;

import java.util.ArrayList;

/**
 * Created by Hoda on 18/05/2018.
 */

public class Conversion {

    public static ArrayList<HappyHour> covertJsonToHappyHour(JsonArray houtItem) {
        ArrayList<HappyHour> happyHours = new ArrayList<>();
        for (int i = 0; i < houtItem.size(); i++) {
            JsonObject oneHappy = houtItem.get(i).getAsJsonObject();
            JsonArray hours = oneHappy.get("hours").getAsJsonArray();
            Hour[] hours1 = new Hour[hours.size()];
            if (hours != null) {
                hours1 = new Hour[hours.size()];
                for (int j = 0; j < hours.size(); j++)
                {
                    JsonObject hour =  hours.get(j).getAsJsonObject();
                    String openTime = hour.get("openTime").getAsString()+":00";
                    String closeTime = hour.get("closeTime").getAsString()+":00";
                    Hour oneHoure= new Hour();
                    oneHoure.setOpenTime(openTime);
                    oneHoure.setCloseTime(closeTime);
                    hours1[j]=oneHoure;
                }
            }
            int dayOfTheWeek = oneHappy.get("dayOfTheWeek").getAsInt();
            String description = oneHappy.get("description").getAsString();
            boolean isAvailable = oneHappy.get("isAvailable").getAsBoolean();
            HappyHour happyHour = new HappyHour();
            happyHour.setAvailable(isAvailable);
            happyHour.setDayOfTheWeek(dayOfTheWeek);
            happyHour.setDescription(description);
            happyHour.setHours(hours1);
            happyHours.add(happyHour);

            for(int m=0;m<happyHours.size();m++)
            {
                System.out.println(happyHours.get(m).toString());
            }
        }
        return happyHours;
    }

}
