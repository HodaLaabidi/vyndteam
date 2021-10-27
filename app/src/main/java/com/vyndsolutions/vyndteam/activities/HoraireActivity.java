package com.vyndsolutions.vyndteam.activities;

import android.app.TimePickerDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Availabilities;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Hour;
import com.vyndsolutions.vyndteam.models.Hours;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.utils.WidgetUtils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoraireActivity extends AppCompatActivity {

    Switch switchLundi, switchMardi, switchMercredi, switchJeudi, switchVendredi, switchSamedi, switchDimanche;
    TextView tvNameLundi, tvNameMardi, tvNameMercredi, tvNameJeudi, tvNameVendredi, tvNameSamedi, tvNameDimanche;
    TextView tvOpenDayLundi, tvOpenDayMardi, tvOpenDayMercredi, tvOpenDayJeudi, tvOpenDayVendredi, tvOpenDaySamedi, tvOpenDayDimanche;
    TextView tvCloseDayLundi, tvCloseDayMardi, tvCloseDayMercredi, tvCloseDayJeudi, tvCloseDayVendredi, tvCloseDaySamedi, tvCloseDayDimanche;
    TextView tvOpenNightLundi, tvOpenNightMardi, tvOpenNightMercredi, tvOpenNightJeudi, tvOpenNightVendredi, tvOpenNightSamedi, tvOpenNightDimanche;
    TextView tvCloseNightLundi, tvCloseNightMardi, tvCloseNightMercredi, tvCloseNightJeudi, tvCloseNightVendredi, tvCloseNightSamedi, tvCloseNightDimanche;
    Business business;
    NetworkImageView ivBack;
    private ProgressBar progressBar;
    private boolean isAlertShown = false;
    private boolean isFromProfile = false;
    private Hour[] hoursdimanche, hourslundi, hoursmardi, hoursmercredi, hoursjeudi, hoursvendredi, hourssamedi;
    private int j = 0;
    private int lundi = 0, mardi = 0, mercredi = 0, jeudi = 0, vendredi = 0, samedi = 0, dimanche = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire);
        initialize();
    }

    private void initialize() {

        if (getIntent() != null) {
            business = (Business) getIntent().getSerializableExtra("business");
            try {
                isFromProfile = getIntent().getBooleanExtra("isFromProfile", false);
            } catch (NullPointerException e) {

            }

        } else {
            business = new Business();
        }
        for (int i = 0; i < business.getWorkingHours().size(); i++) {
            System.out.println(business.getWorkingHours().get(i).toString());
        }
        switchLundi = findViewById(R.id.switchDayLundi);
        switchMardi = findViewById(R.id.switchDayMardi);
        switchMercredi = findViewById(R.id.switchDayMercredi);
        switchJeudi = findViewById(R.id.switchDayJeudi);
        switchVendredi = findViewById(R.id.switchDayVendredi);
        switchSamedi = findViewById(R.id.switchDaySamedi);
        switchDimanche = findViewById(R.id.switchDayDimanche);
        ///////////////////////////////////////////////////////////
        tvNameLundi = findViewById(R.id.tv_day_name_lundi);
        tvNameMardi = findViewById(R.id.tv_day_name_mardi);
        tvNameMercredi = findViewById(R.id.tv_day_name_mercredi);
        tvNameJeudi = findViewById(R.id.tv_day_name_jeudi);
        tvNameVendredi = findViewById(R.id.tv_day_name_vendredi);
        tvNameSamedi = findViewById(R.id.tv_day_name_samedi);
        tvNameDimanche = findViewById(R.id.tv_day_name_dimanche);
        ///////////////////////////////////////////////////////////
        tvOpenDayLundi = findViewById(R.id.lbl_heure_start_lundi);
        tvOpenDayMardi = findViewById(R.id.lbl_heure_start_mardi);
        tvOpenDayMercredi = findViewById(R.id.lbl_heure_start_mercredi);
        tvOpenDayJeudi = findViewById(R.id.lbl_heure_start_jeudi);
        tvOpenDayVendredi = findViewById(R.id.lbl_heure_start_vendredi);
        tvOpenDaySamedi = findViewById(R.id.lbl_heure_start_samedi);
        tvOpenDayDimanche = findViewById(R.id.lbl_heure_start_dimanche);
        /////////////////////////////////////////////////////////////
        tvCloseDayLundi = findViewById(R.id.lbl_heure_end_lundi);
        tvCloseDayMardi = findViewById(R.id.lbl_heure_end_mardi);
        tvCloseDayMercredi = findViewById(R.id.lbl_heure_end_mercredi);
        tvCloseDayJeudi = findViewById(R.id.lbl_heure_end_jeudi);
        tvCloseDayVendredi = findViewById(R.id.lbl_heure_end_vendredi);
        tvCloseDaySamedi = findViewById(R.id.lbl_heure_end_samedi);
        tvCloseDayDimanche = findViewById(R.id.lbl_heure_end_dimanche);
        ///////////////////////////////////////////////////////////////////
        tvOpenNightLundi = findViewById(R.id.lbl_heure_start_night_lundi);
        tvOpenNightMardi = findViewById(R.id.lbl_heure_start_night_mardi);
        tvOpenNightMercredi = findViewById(R.id.lbl_heure_start_night_mercredi);
        tvOpenNightJeudi = findViewById(R.id.lbl_heure_start_night_jeudi);
        tvOpenNightVendredi = findViewById(R.id.lbl_heure_start_night_vendredi);
        tvOpenNightSamedi = findViewById(R.id.lbl_heure_start_night_samedi);
        tvOpenNightDimanche = findViewById(R.id.lbl_heure_start_night_dimanche);
        /////////////////////////////////////////////////////////////
        tvCloseNightLundi = findViewById(R.id.lbl_heure_end__night_lundi);
        tvCloseNightMardi = findViewById(R.id.lbl_heure_end__night_mardi);
        tvCloseNightMercredi = findViewById(R.id.lbl_heure_end__night_mercredi);
        tvCloseNightJeudi = findViewById(R.id.lbl_heure_end__night_jeudi);
        tvCloseNightVendredi = findViewById(R.id.lbl_heure_end__night_vendredi);
        tvCloseNightSamedi = findViewById(R.id.lbl_heure_end__night_samedi);
        tvCloseNightDimanche = findViewById(R.id.lbl_heure_end__night_dimanche);
        progressBar = findViewById(R.id.progressBar);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchLundi.isChecked() &&
                        !switchMardi.isChecked() && !switchMercredi.isChecked() && !switchJeudi.isChecked()
                        && !switchVendredi.isChecked() && !switchSamedi.isChecked() && !switchDimanche.isChecked()) {
                    Utils.hourItem = new JsonArray();
                }
                else {
                    getDataFromInput();
                }

                finish();

            }
        });
        switchLundi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateLundiEnable();
                else
                    updateLundiDisable();
            }
        });

        switchMardi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateMardiEnable();
                else
                    updateMardiDisable();
            }
        });
        switchMercredi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateMercrediEnable();
                else
                    updateMercrediDisable();
            }
        });
        switchJeudi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateJeudiEnable();
                else
                    updateJeudiDisable();
            }
        });
        switchVendredi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateVendrediEnable();
                else
                    updateVendrediDisable();
            }
        });
        switchSamedi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateSamediEnable();
                else updateSamediDisable();
            }
        });
        switchDimanche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    updateDimancheEnable();
                else updateDimancheDisable();
            }
        });
        for (int i = 0; i < business.getWorkingHours().size(); i++) {
            if (!business.getWorkingHours().get(i).isClosed()) {
                j++;
            }
        }

        if (j > 0) {
            initializeWorkingHour();
        } else {
            try {
                initializeWorkingHourFromJson(Utils.hourItem);
            } catch (NullPointerException e) {

            }

        }


        try {
            tvNameLundi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(1).dayOfTheWeek));
            tvOpenDayLundi.setText(business.getWorkingHours().get(1).getOpenHourDay());
            tvCloseDayLundi.setText(business.getWorkingHours().get(1).getCloseHourDay());
            tvOpenNightLundi.setText(business.getWorkingHours().get(1).getOpenHourNight());
            tvCloseNightLundi.setText(business.getWorkingHours().get(1).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        try {
            tvNameMardi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(2).dayOfTheWeek));
            tvOpenDayMardi.setText(business.getWorkingHours().get(2).getOpenHourDay());
            tvCloseDayMardi.setText(business.getWorkingHours().get(2).getCloseHourDay());
            tvOpenNightMardi.setText(business.getWorkingHours().get(2).getOpenHourNight());
            tvCloseNightMardi.setText(business.getWorkingHours().get(2).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }


        try {
            tvNameMercredi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(3).dayOfTheWeek));
            tvOpenDayMercredi.setText(business.getWorkingHours().get(3).getOpenHourDay());
            tvCloseDayMercredi.setText(business.getWorkingHours().get(3).getCloseHourDay());
            tvOpenNightMercredi.setText(business.getWorkingHours().get(3).getOpenHourNight());
            tvCloseNightMercredi.setText(business.getWorkingHours().get(3).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        try {
            tvNameJeudi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(4).dayOfTheWeek));
            tvOpenDayJeudi.setText(business.getWorkingHours().get(4).getOpenHourDay());
            tvCloseDayJeudi.setText(business.getWorkingHours().get(4).getCloseHourDay());
            tvOpenNightJeudi.setText(business.getWorkingHours().get(4).getOpenHourNight());
            tvCloseNightJeudi.setText(business.getWorkingHours().get(4).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }


        try {
            tvNameVendredi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(5).dayOfTheWeek));
            tvOpenDayVendredi.setText(business.getWorkingHours().get(5).getOpenHourDay());
            tvCloseDayVendredi.setText(business.getWorkingHours().get(5).getCloseHourDay());
            tvOpenNightVendredi.setText(business.getWorkingHours().get(5).getOpenHourNight());
            tvCloseNightVendredi.setText(business.getWorkingHours().get(5).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        try {
            tvNameSamedi.setText(WidgetUtils.getDayName(business.getWorkingHours().get(6).dayOfTheWeek));
            tvOpenDaySamedi.setText(business.getWorkingHours().get(6).getOpenHourDay());
            tvCloseDaySamedi.setText(business.getWorkingHours().get(6).getCloseHourDay());
            tvOpenNightSamedi.setText(business.getWorkingHours().get(6).getOpenHourNight());
            tvCloseNightSamedi.setText(business.getWorkingHours().get(6).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        try {
            tvNameDimanche.setText(WidgetUtils.getDayName(business.getWorkingHours().get(0).dayOfTheWeek));
            tvOpenDayDimanche.setText(business.getWorkingHours().get(0).getOpenHourDay());
            tvCloseDayDimanche.setText(business.getWorkingHours().get(0).getCloseHourDay());
            tvOpenNightDimanche.setText(business.getWorkingHours().get(0).getOpenHourNight());
            tvCloseNightDimanche.setText(business.getWorkingHours().get(0).getCloseHourNight());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        ///////////////////Lundi
        tvOpenDayLundi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayLundi);
            }
        });
        tvCloseDayLundi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayLundi);
            }
        });
        tvOpenNightLundi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightLundi);
            }
        });
        tvCloseNightLundi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightLundi);
            }
        });
        /////////////////Mardi
        tvOpenDayMardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayMardi);
            }
        });
        tvCloseDayMardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayMardi);
            }
        });
        tvOpenNightMardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightMardi);
            }
        });
        tvCloseNightMardi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightMardi);
            }
        });
        //////////////Mercredi
        tvOpenDayMercredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayMercredi);
            }
        });
        tvCloseDayMercredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayMercredi);
            }
        });
        tvOpenNightMercredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightMercredi);
            }
        });
        tvCloseNightMercredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightMercredi);
            }
        });
        /////////////////////Jeudi
        tvOpenDayJeudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayJeudi);
            }
        });
        tvCloseDayJeudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayJeudi);
            }
        });
        tvOpenNightJeudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightJeudi);
            }
        });
        tvCloseNightJeudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightJeudi);
            }
        });
        ////////////////////Vendredi
        tvOpenDayVendredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayVendredi);
            }
        });
        tvCloseDayVendredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayVendredi);
            }
        });
        tvOpenNightVendredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightVendredi);
            }
        });
        tvCloseNightVendredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightVendredi);
            }
        });
        /////////////Samedi
        tvOpenDaySamedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDaySamedi);
            }
        });
        tvCloseDaySamedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDaySamedi);
            }
        });
        tvOpenNightSamedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightSamedi);
            }
        });
        tvCloseNightSamedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightSamedi);
            }
        });
        ////////////////////Dimanche
        tvOpenDayDimanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenDayDimanche);
            }
        });
        tvCloseDayDimanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseDayDimanche);
            }
        });
        tvOpenNightDimanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvOpenNightDimanche);
            }
        });
        tvCloseNightDimanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(tvCloseNightDimanche);
            }
        });
    }

    private void initializeWorkingHourFromJson(JsonArray hourItem) {
        System.out.println(hourItem.size());
        System.out.println("-*-*-*-*-*-*-*-*-*");
        for (int j = 0; j < hourItem.size(); j++) {
            JsonObject hour = hourItem.get(j).getAsJsonObject();
            int dayOfTheWeek = hour.get("dayOfTheWeek").getAsInt();
            String openTime = hour.get("openTime").getAsString();
            String closeTime = hour.get("closeTime").getAsString();
            if (dayOfTheWeek == 1 && lundi == 0) {
                switchLundi.setChecked(true);
                tvOpenDayLundi.setText(openTime);
                tvCloseDayLundi.setText(closeTime);
                lundi++;
            }
            if (dayOfTheWeek == 1 && lundi == 1) {
                switchLundi.setChecked(true);
                tvOpenNightLundi.setText(openTime);
                tvCloseNightLundi.setText(closeTime);
            }
            if (dayOfTheWeek == 2 && mardi == 0) {
                switchMardi.setChecked(true);
                tvOpenDayMardi.setText(openTime);
                tvCloseDayMardi.setText(closeTime);
                mardi++;
            }
            if (dayOfTheWeek == 2 && mardi == 1) {
                switchMardi.setChecked(true);
                tvOpenNightMardi.setText(openTime);
                tvCloseNightMardi.setText(closeTime);
            }
            if (dayOfTheWeek == 3 && mercredi == 0) {
                switchMercredi.setChecked(true);
                tvOpenDayMercredi.setText(openTime);
                tvCloseDayMercredi.setText(closeTime);
                mercredi++;
            }
            if (dayOfTheWeek == 3 && mercredi == 1) {
                switchMercredi.setChecked(true);
                tvOpenNightMercredi.setText(openTime);
                tvCloseNightMercredi.setText(closeTime);
            }
            if (dayOfTheWeek == 4 && jeudi == 0) {
                switchJeudi.setChecked(true);
                tvOpenDayJeudi.setText(openTime);
                tvCloseDayJeudi.setText(closeTime);
                jeudi++;
            }
            if (dayOfTheWeek == 4 && jeudi == 1) {
                switchJeudi.setChecked(true);
                tvOpenNightJeudi.setText(openTime);
                tvCloseNightJeudi.setText(closeTime);
            }
            if (dayOfTheWeek == 5 && vendredi == 0) {
                switchVendredi.setChecked(true);
                tvOpenDayVendredi.setText(openTime);
                tvCloseDayVendredi.setText(closeTime);
                vendredi++;
            }
            if (dayOfTheWeek == 5 && vendredi == 1) {
                switchVendredi.setChecked(true);
                tvOpenNightVendredi.setText(openTime);
                tvCloseNightVendredi.setText(closeTime);
            }

            if (dayOfTheWeek == 6 && samedi == 0) {
                switchSamedi.setChecked(true);
                tvOpenDaySamedi.setText(openTime);
                tvCloseDaySamedi.setText(closeTime);
                samedi++;
            }
            if (dayOfTheWeek == 6 && samedi == 1) {
                switchSamedi.setChecked(true);
                tvOpenNightSamedi.setText(openTime);
                tvCloseNightSamedi.setText(closeTime);
            }

            if (dayOfTheWeek == 0 && dimanche == 0) {
                switchDimanche.setChecked(true);
                tvOpenDayDimanche.setText(openTime);
                tvCloseDayDimanche.setText(closeTime);
                dimanche++;
            }
            if (dayOfTheWeek == 0 && dimanche == 1) {
                switchDimanche.setChecked(true);
                tvOpenNightDimanche.setText(openTime);
                tvCloseNightDimanche.setText(closeTime);
            }


        }
    }

    private void initializeWorkingHour() {
        try {
            if (!business.getWorkingHours().get(1).isClosed()) {
                switchLundi.setChecked(true);
                updateLundiEnable();

            } else {
                switchLundi.setChecked(false);
                updateLundiDisable();
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {

            /*switchLundi.setChecked(false);
            updateLundiDisable();*/
        }

        try {
            if (!business.getWorkingHours().get(2).isClosed()) {
                switchMardi.setChecked(true);
                updateMardiEnable();
            } else {
                switchMardi.setChecked(false);
                updateMardiDisable();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            /*switchMardi.setChecked(false);
            updateMardiDisable();*/
        }
        try {

            if (!business.getWorkingHours().get(3).isClosed()) {
                switchMercredi.setChecked(true);
                updateMercrediEnable();
            } else {
                switchMercredi.setChecked(false);
                updateMercrediDisable();
            }

        } catch (IndexOutOfBoundsException | NullPointerException e) {
            /*switchMercredi.setChecked(false);
            updateMercrediDisable();*/

        }

        try {
            if (!business.getWorkingHours().get(4).isClosed()) {
                switchJeudi.setChecked(true);
                updateJeudiEnable();
            } else {
                switchJeudi.setChecked(false);
                updateJeudiDisable();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            /*switchJeudi.setChecked(false);
            updateJeudiDisable();*/
        }
        try {
            if (!business.getWorkingHours().get(5).isClosed()) {
                switchVendredi.setChecked(true);
                updateVendrediEnable();
            } else {
                switchVendredi.setChecked(false);
                updateVendrediDisable();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          /*switchVendredi.setChecked(false);
          updateVendrediDisable();*/
        }
        try {
            if (!business.getWorkingHours().get(6).isClosed()) {
                switchSamedi.setChecked(true);
                updateSamediEnable();
            } else {
                switchSamedi.setChecked(false);
                updateSamediDisable();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            /*switchSamedi.setChecked(false);
            updateSamediDisable();*/
        }
        try {
            if (!business.getWorkingHours().get(0).isClosed()) {
                switchDimanche.setChecked(true);
                updateDimancheEnable();
            } else {
                switchDimanche.setChecked(false);
                updateDimancheDisable();
            }

        } catch (NullPointerException | IndexOutOfBoundsException e) {
           /*switchDimanche.setChecked(false);
           updateDimancheDisable();*/
        }
    }

    private void sendHourToService(Business business, JsonArray hourItem) {


        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addHoraires(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), hourItem);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {

                        Utils.hourItem = new JsonArray();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (getBaseContext() != null) {


                }
            }
        });
    }

    private void showHourPicker(final TextView textView) {
        final Calendar myCalender = Calendar.getInstance();
        final int[] hour = {myCalender.get(Calendar.HOUR_OF_DAY)};
        final int[] minuteDay = {myCalender.get(Calendar.MINUTE)};
        TimePickerDialog.OnTimeSetListener timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                textView.setText(String.format("%02d:%02d", hourOfDay, minute));

            }
        };
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeEndSetListener, hour[0], minuteDay[0], true);
        timePickerDialog.setTitle("Choisissez une heure:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!switchLundi.isChecked() &&
                !switchMardi.isChecked() && !switchMercredi.isChecked() && !switchJeudi.isChecked()
                && !switchVendredi.isChecked() && !switchSamedi.isChecked() && !switchDimanche.isChecked()) {
            Utils.hourItem = new JsonArray();
        }
        else {
            getDataFromInput();
        }
        finish();

    }

    private void updateLundiEnable() {
        tvOpenDayLundi.setEnabled(true);
        tvCloseDayLundi.setEnabled(true);
        tvOpenNightLundi.setEnabled(true);
        tvCloseNightLundi.setEnabled(true);
        tvOpenDayLundi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayLundi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightLundi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightLundi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateLundiDisable() {
        tvOpenDayLundi.setEnabled(false);
        tvCloseDayLundi.setEnabled(false);
        tvOpenNightLundi.setEnabled(false);
        tvCloseNightLundi.setEnabled(false);
        tvOpenDayLundi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayLundi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightLundi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightLundi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenDayLundi.setText("-:-");
        tvCloseDayLundi.setText("-:-");
        tvOpenNightLundi.setText("-:-");
        tvCloseNightLundi.setText("-:-");


    }

    private void updateMardiEnable() {
        tvOpenDayMardi.setEnabled(true);
        tvCloseDayMardi.setEnabled(true);
        tvOpenNightMardi.setEnabled(true);
        tvCloseNightMardi.setEnabled(true);
        tvOpenDayMardi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayMardi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightMardi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightMardi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateMardiDisable() {
        tvOpenDayMardi.setEnabled(false);
        tvCloseDayMardi.setEnabled(false);
        tvOpenNightMardi.setEnabled(false);
        tvCloseNightMardi.setEnabled(false);
        tvOpenDayMardi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayMardi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightMardi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightMardi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenDayMardi.setText("-:-");
        tvCloseDayMardi.setText("-:-");
        tvOpenNightMardi.setText("-:-");
        tvCloseNightMardi.setText("-:-");

    }

    private void updateMercrediEnable() {
        tvOpenDayMercredi.setEnabled(true);
        tvCloseDayMercredi.setEnabled(true);
        tvOpenNightMercredi.setEnabled(true);
        tvCloseNightMercredi.setEnabled(true);
        tvOpenDayMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateMercrediDisable() {
        tvOpenDayMercredi.setEnabled(false);
        tvCloseDayMercredi.setEnabled(false);
        tvOpenNightMercredi.setEnabled(false);
        tvCloseNightMercredi.setEnabled(false);
        tvOpenDayMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightMercredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenDayMercredi.setText("-:-");
        tvCloseDayMercredi.setText("-:-");
        tvOpenNightMercredi.setText("-:-");
        tvCloseNightMercredi.setText("-:-");
    }

    private void updateJeudiEnable() {
        tvOpenDayJeudi.setEnabled(true);
        tvCloseDayJeudi.setEnabled(true);
        tvOpenNightJeudi.setEnabled(true);
        tvCloseNightJeudi.setEnabled(true);
        tvOpenDayJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateJeudiDisable() {
        tvOpenDayJeudi.setEnabled(false);
        tvCloseDayJeudi.setEnabled(false);
        tvOpenNightJeudi.setEnabled(false);
        tvCloseNightJeudi.setEnabled(false);
        //.setBackground(context.getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenDayJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightJeudi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));

        tvOpenDayJeudi.setText("-:-");
        tvCloseDayJeudi.setText("-:-");
        tvOpenNightJeudi.setText("-:-");
        tvCloseNightJeudi.setText("-:-");

    }

    private void updateVendrediEnable() {
        tvOpenDayVendredi.setEnabled(true);
        tvCloseDayVendredi.setEnabled(true);
        tvOpenNightVendredi.setEnabled(true);
        tvCloseNightVendredi.setEnabled(true);
        //.setBackground(context.getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenDayVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateVendrediDisable() {
        tvOpenDayVendredi.setEnabled(false);
        tvCloseDayVendredi.setEnabled(false);
        tvOpenNightVendredi.setEnabled(false);
        tvCloseNightVendredi.setEnabled(false);
        //.setBackground(context.getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenDayVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightVendredi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));

        tvOpenDayVendredi.setText("-:-");
        tvCloseDayVendredi.setText("-:-");
        tvOpenNightVendredi.setText("-:-");
        tvCloseNightVendredi.setText("-:-");

    }

    private void updateSamediEnable() {
        tvOpenDaySamedi.setEnabled(true);
        tvCloseDaySamedi.setEnabled(true);
        tvOpenNightSamedi.setEnabled(true);
        tvCloseNightSamedi.setEnabled(true);
        tvOpenDaySamedi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDaySamedi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightSamedi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightSamedi.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateSamediDisable() {
        tvOpenDaySamedi.setEnabled(false);
        tvCloseDaySamedi.setEnabled(false);
        tvOpenNightSamedi.setEnabled(false);
        tvCloseNightSamedi.setEnabled(false);
        tvOpenDaySamedi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDaySamedi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightSamedi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightSamedi.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));

        tvOpenDaySamedi.setText("-:-");
        tvCloseDaySamedi.setText("-:-");
        tvOpenNightSamedi.setText("-:-");
        tvCloseNightSamedi.setText("-:-");


    }

    private void updateDimancheEnable() {
        tvOpenDayDimanche.setEnabled(true);
        tvCloseDayDimanche.setEnabled(true);
        tvOpenNightDimanche.setEnabled(true);
        tvCloseNightDimanche.setEnabled(true);
        //.setBackground(context.getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenDayDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseDayDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvOpenNightDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));
        tvCloseNightDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithblackborder));

    }

    private void updateDimancheDisable() {
        tvOpenDayDimanche.setEnabled(false);
        tvCloseDayDimanche.setEnabled(false);
        tvOpenNightDimanche.setEnabled(false);
        tvCloseNightDimanche.setEnabled(false);
        tvOpenDayDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseDayDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvOpenNightDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));
        tvCloseNightDimanche.setBackground(getResources().getDrawable(R.drawable.shapewithgrayborderandgraybackground));

        tvOpenDayDimanche.setText("-:-");
        tvCloseDayDimanche.setText("-:-");
        tvOpenNightDimanche.setText("-:-");
        tvCloseNightDimanche.setText("-:-");

    }

    private void getDataFromInput() {
        JsonArray arrayDays = new JsonArray();
        JsonObject jsonLundiDay = new JsonObject();
        JsonObject jsonLundiNight = new JsonObject();

        JsonObject jsonMardiDay = new JsonObject();
        JsonObject jsonMardiNight = new JsonObject();

        JsonObject jsonMercrediDay = new JsonObject();
        JsonObject jsonMercrediNight = new JsonObject();

        JsonObject jsonJeudiDay = new JsonObject();
        JsonObject jsonJeudiNight = new JsonObject();

        JsonObject jsonVendrediDay = new JsonObject();
        JsonObject jsonVendrediNight = new JsonObject();

        JsonObject jsonSamediDay = new JsonObject();
        JsonObject jsonSamediNight = new JsonObject();

        JsonObject jsonDimancheDay = new JsonObject();
        JsonObject jsonDimancheNight = new JsonObject();


        if (switchLundi.isChecked()) {

            if (tvOpenDayLundi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayLundi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {

                jsonLundiDay.addProperty("dayOfTheWeek", 1);
                jsonLundiDay.addProperty("openTime", tvOpenDayLundi.getText().toString());
                jsonLundiDay.addProperty("closeTime", tvCloseDayLundi.getText().toString());
                arrayDays.add(jsonLundiDay);
            }
            if (tvOpenNightLundi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightLundi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonLundiNight.addProperty("dayOfTheWeek", 1);
                jsonLundiNight.addProperty("openTime", tvOpenNightLundi.getText().toString());
                jsonLundiNight.addProperty("closeTime", tvCloseNightLundi.getText().toString());
                arrayDays.add(jsonLundiNight);
            }
        }

        if (switchMardi.isChecked()) {
            if (tvOpenDayMardi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayMardi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonMardiDay.addProperty("dayOfTheWeek", 2);
                jsonMardiDay.addProperty("openTime", tvOpenDayMardi.getText().toString());
                jsonMardiDay.addProperty("closeTime", tvCloseDayMardi.getText().toString());
                arrayDays.add(jsonMardiDay);
            }
            if (tvOpenNightMardi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightMardi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonMardiNight.addProperty("dayOfTheWeek", 2);
                jsonMardiNight.addProperty("openTime", tvOpenNightMardi.getText().toString());
                jsonMardiNight.addProperty("closeTime", tvCloseNightMardi.getText().toString());
                arrayDays.add(jsonMardiNight);
            }
        }

        if (switchMercredi.isChecked()) {
            if (tvOpenDayMercredi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayMercredi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonMercrediDay.addProperty("dayOfTheWeek", 3);
                jsonMercrediDay.addProperty("openTime", tvOpenDayMercredi.getText().toString());
                jsonMercrediDay.addProperty("closeTime", tvCloseDayMercredi.getText().toString());
                arrayDays.add(jsonMercrediDay);
            }
            if (tvOpenNightMercredi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightMercredi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonMercrediNight.addProperty("dayOfTheWeek", 3);
                jsonMercrediNight.addProperty("openTime", tvOpenNightMercredi.getText().toString());
                jsonMercrediNight.addProperty("closeTime", tvCloseNightMercredi.getText().toString());
                arrayDays.add(jsonMercrediNight);
            }
        }
        if (switchJeudi.isChecked()) {
            if (tvOpenDayJeudi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayJeudi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonJeudiDay.addProperty("dayOfTheWeek", 4);
                jsonJeudiDay.addProperty("openTime", tvOpenDayJeudi.getText().toString());
                jsonJeudiDay.addProperty("closeTime", tvCloseDayJeudi.getText().toString());
                arrayDays.add(jsonJeudiDay);
            }
            if (tvOpenNightJeudi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightJeudi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonJeudiNight.addProperty("dayOfTheWeek", 4);
                jsonJeudiNight.addProperty("openTime", tvOpenNightJeudi.getText().toString());
                jsonJeudiNight.addProperty("closeTime", tvCloseNightJeudi.getText().toString());
                arrayDays.add(jsonJeudiNight);
            }
        }

        if (switchVendredi.isChecked()) {
            if (tvOpenDayVendredi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayVendredi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonVendrediDay.addProperty("dayOfTheWeek", 5);
                jsonVendrediDay.addProperty("openTime", tvOpenDayVendredi.getText().toString());
                jsonVendrediDay.addProperty("closeTime", tvCloseDayVendredi.getText().toString());
                arrayDays.add(jsonVendrediDay);
            }
            if (tvOpenNightVendredi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightVendredi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonVendrediNight.addProperty("dayOfTheWeek", 5);
                jsonVendrediNight.addProperty("openTime", tvOpenNightVendredi.getText().toString());
                jsonVendrediNight.addProperty("closeTime", tvCloseNightVendredi.getText().toString());
                arrayDays.add(jsonVendrediNight);
            }
        }

        if (switchSamedi.isChecked()) {
            if (tvOpenDaySamedi.getText().toString().equalsIgnoreCase("-:-") && tvCloseDaySamedi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonSamediDay.addProperty("dayOfTheWeek", 6);
                jsonSamediDay.addProperty("openTime", tvOpenDaySamedi.getText().toString());
                jsonSamediDay.addProperty("closeTime", tvCloseDaySamedi.getText().toString());
                arrayDays.add(jsonSamediDay);
            }
            if (tvOpenNightSamedi.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightSamedi.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonSamediNight.addProperty("dayOfTheWeek", 6);
                jsonSamediNight.addProperty("openTime", tvOpenNightSamedi.getText().toString());
                jsonSamediNight.addProperty("closeTime", tvCloseNightSamedi.getText().toString());
                arrayDays.add(jsonSamediNight);
            }
        }

        if (switchDimanche.isChecked()) {
            if (tvOpenDayDimanche.getText().toString().equalsIgnoreCase("-:-") && tvCloseDayDimanche.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonDimancheDay.addProperty("dayOfTheWeek", 0);
                jsonDimancheDay.addProperty("openTime", tvOpenDayDimanche.getText().toString());
                jsonDimancheDay.addProperty("closeTime", tvCloseDayDimanche.getText().toString());
                arrayDays.add(jsonDimancheDay);
            }
            if (tvOpenNightDimanche.getText().toString().equalsIgnoreCase("-:-") && tvCloseNightDimanche.getText().toString().equalsIgnoreCase("-:-")) {

            } else {
                jsonDimancheNight.addProperty("dayOfTheWeek", 0);
                jsonDimancheNight.addProperty("openTime", tvOpenNightDimanche.getText().toString());
                jsonDimancheNight.addProperty("closeTime", tvCloseNightDimanche.getText().toString());
                arrayDays.add(jsonDimancheNight);
            }
        }
        if (business != null) {
            if (arrayDays.size() != 0) {
                Utils.hourItem = new JsonArray();
                Utils.hourItem = arrayDays;
            }
            if (isFromProfile) {
                sendHourToService(business, Utils.hourItem);
            }
        }

     /*  for(int i=0;i<business.getWorkingHours().size();i++)
        {
            if(!business.getWorkingHours().get(i).isClosed())
            {
                for(int j=0; j<arrayDays.size(); j++){
                    JsonObject oneDay = arrayDays.getAsJsonObject();
                    int dayofTheWeek = oneDay.get("dayOfTheWeek").getAsInt();
                    String closeTime = oneDay.get("closeTime").getAsString();
                    String openTime = oneDay.get("openTime").getAsString();
                    if(dayofTheWeek==business.getWorkingHours().get(i).dayOfTheWeek)
                    {
                        Hour [] hour = new Hour[business.getWorkingHours().get(i).hours.length];
                        for(int k=0;k<hour.length;k++)
                        {
                            hour[k].setCloseTime(business.getWorkingHours().get(i).hours[k].closeTime);
                            hour[k].setOpenTime(business.getWorkingHours().get(i).hours[k].openTime);
                        }
                        business.getWorkingHours().get(i).setHours(hour);
                    }

                    //  applicationSettings.put(name, value);
                }
            }
        }*/
        ArrayList<Availabilities> availabilitieslist = new ArrayList<>();
        int dimanche = 0, lundi = 0, mardi = 0, mercredi = 0, jeudi = 0, vendredi = 0, samedi = 0;
        for (int i = 0; i < arrayDays.size(); i++) {
            JsonObject oneDay = arrayDays.get(i).getAsJsonObject();
            int dayofTheWeek = oneDay.get("dayOfTheWeek").getAsInt();

            if (dayofTheWeek == 0)
                dimanche++;
            if (dayofTheWeek == 1)
                lundi++;
            if (dayofTheWeek == 2)
                mardi++;
            if (dayofTheWeek == 3)
                mercredi++;
            if (dayofTheWeek == 4)
                jeudi++;
            if (dayofTheWeek == 5)
                vendredi++;
            if (dayofTheWeek == 6)
                samedi++;
        }
        if (dimanche == 1) {
            hoursdimanche = new Hour[1];
        } else if (dimanche == 2) {
            hoursdimanche = new Hour[2];
        }
        if (lundi == 1) {
            hourslundi = new Hour[1];
        } else if (lundi == 2) {
            hourslundi = new Hour[2];
        }
        if (mardi == 1) {
            hoursmardi = new Hour[1];
        } else if (mardi == 2) {
            hoursmardi = new Hour[2];
        }
        if (mercredi == 1) {
            hoursmercredi = new Hour[1];
        } else if (mercredi == 2) {
            hoursmercredi = new Hour[2];
        }
        if (jeudi == 1) {
            hoursjeudi = new Hour[1];
        } else if (jeudi == 2) {
            hoursjeudi = new Hour[2];
        }
        if (vendredi == 1) {
            hoursvendredi = new Hour[1];
        } else if (vendredi == 2) {
            hoursvendredi = new Hour[2];
        }
        if (samedi == 1) {
            hourssamedi = new Hour[1];
        } else if (samedi == 2) {
            hourssamedi = new Hour[2];
        }
        for (int i = 0; i < arrayDays.size(); i++) {
            JsonObject oneDay = arrayDays.get(i).getAsJsonObject();
            int dayofTheWeek = oneDay.get("dayOfTheWeek").getAsInt();
            String closeTime = oneDay.get("closeTime").getAsString();
            String openTime = oneDay.get("openTime").getAsString();

            Availabilities availabilities = new Availabilities();
            availabilities.setDayOfTheWeek(dayofTheWeek);
            Hour hour = new Hour();
            hour.setOpenTime(openTime);
            hour.setCloseTime(closeTime);
            Hour[] hours = new Hour[0];
            availabilities.setHours(hours);
            availabilities.setAvailable(false);
            availabilitieslist.add(availabilities);
           /* if (dayofTheWeek == 0) {
                hours = new Hour[hoursdimanche.length];
            }
            if (dayofTheWeek == 1) {
                hours = new Hour[hourslundi.length];
            }
            if (dayofTheWeek == 2) {
                hours = new Hour[hoursmardi.length];
            }
            if (dayofTheWeek == 3) {
                hours = new Hour[hoursmercredi.length];
            }
            if (dayofTheWeek == 4) {
                hours = new Hour[hoursjeudi.length];
            }
            if (dayofTheWeek == 5) {
                hours = new Hour[hoursvendredi.length];
            }
            if (dayofTheWeek == 6) {
                hours = new Hour[hourssamedi.length];
            }
*/

        }

        //System.out.println(Utils.hourItem);
    }

}
