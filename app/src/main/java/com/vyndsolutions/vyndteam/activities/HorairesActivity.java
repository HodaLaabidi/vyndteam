package com.vyndsolutions.vyndteam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.HoraireAdapter;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.AvailableTime;
import com.vyndsolutions.vyndteam.models.Day;
import com.vyndsolutions.vyndteam.models.HoursHoraires;

import java.util.ArrayList;

public class HorairesActivity extends AppCompatActivity {

    ImageView returnHoraires;
    RecyclerView rvHoraires;
    ArrayList<Day> days = new ArrayList<Day>();
    static ArrayList<AvailableTime> savedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaires);
        initialiseViews();
        clickReturnIcon();
        browseDaysStructure();


    }

    private void browseDaysStructure() {

        if (getIntent() != null) {
            savedArray = (ArrayList<AvailableTime>) getIntent().getSerializableExtra("listHoraires");
            if (savedArray == null) {
                savedArray = new ArrayList <AvailableTime> ();

            }
        }


        HoraireAdapter horaireAdapter = new HoraireAdapter(HorairesActivity.this, days, savedArray);
        rvHoraires.setLayoutManager(new LinearLayoutManager(HorairesActivity.this));
        rvHoraires.setAdapter(horaireAdapter);


    }

    @Override
    public void onBackPressed() {
        registerDataHoraires();
        finish();
    }


    private void initialiseViews() {

        returnHoraires = findViewById(R.id.return_horaires);
        rvHoraires = findViewById(R.id.rv_horaires);
        days.add(new Day("Lundi", 0));
        days.add(new Day("Mardi", 1));
        days.add(new Day("Mercredi", 2));
        days.add(new Day("Jeudi", 3));
        days.add(new Day("Vendredi", 4));
        days.add(new Day("Samedi", 5));
        days.add(new Day("Dimanche", 6));


    }

    private void clickReturnIcon() {

        returnHoraires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HorairesActivity.this, AddBusinessActivity.class);
                startActivity(intent);
                registerDataHoraires();
            }
        });
    }

    private void registerDataHoraires() {
        SharedPreferencesFactory.saveListHoraires(getBaseContext(), savedArray);
        if (savedArray != null) {
            if (savedArray.size() != 0) {
                for (int i = 0; i < savedArray.size(); i++) {
                    Log.e("registerDataHoraires " + i, savedArray.get(i).getOpenTime() + " " + savedArray.get(i).getCloseTime() + " " + savedArray.get(i).getDayOfTheWeek());
                }
            }


        }


    }
}

