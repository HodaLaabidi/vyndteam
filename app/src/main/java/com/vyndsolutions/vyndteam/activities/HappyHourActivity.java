package com.vyndsolutions.vyndteam.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.HappyHourArrayAdapter;
import com.vyndsolutions.vyndteam.models.Availabilities;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.HappyHour;
import com.vyndsolutions.vyndteam.models.Hour;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Conversion;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.utils.WidgetUtils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HappyHourActivity extends AppCompatActivity implements View.OnClickListener {
    View addView, layoutNoInternetConnection;
    RelativeLayout layoutEmptyHappyHour, rlLayoutAddHappyHour;
    Toolbar toolbar, toolbarUpdate;
    NetworkImageView ivBack, ivEdit, ivBackUpdate;
    List<HappyHour> happyHours = new ArrayList<>();
    RecyclerView rvHappyHour;
    HappyHourArrayAdapter happyHourArrayAdapter;
    EditText etLundi, etMardi, etMercredi, etJeudi, etVendredi, etSamedi, etDimanche;
    CheckBox cbLundi, cbMardi, cbMercredi, cbJeudi, cbVendredi, cbSamedi, cbDimanche;
    TextView tvLundiName, tvMardiName, tvMercrediName, tvJeudiName, tvVendrediName, tvSamediName, tvDimancheName;
    TextView tvLundiDe, tvMardiDe, tvMercrediDe, tvJeudiDe, tvVendrediDe, tvSamediDe, tvDimancheDe;
    TextView tvLundiDeTime, tvMardiDeTime, tvMercrediDeTime, tvJeudiDeTime, tvVendrediDeTime, tvSamediDeTime, tvDimancheDeTime;
    TextView tvLundiA, tvMardiA, tvMercrediA, tvJeudiA, tvVendrediA, tvSamediA, tvDimancheA;
    TextView tvLundiATime, tvMardiATime, tvMercrediATime, tvJeudiATime, tvVendrediATime, tvSamediATime, tvDimancheATime;
    TextView tvSave;
    RelativeLayout layoutAddHappyHour;
    private boolean isAddOpen = false;
    private Business business;
    Hour[] hours1;
    private View rlResseayer;
    private Availabilities availabilitiesLundi = new Availabilities();
    private Availabilities availabilitiesMardi = new Availabilities();
    private Availabilities availabilitiesMercredi = new Availabilities();
    private Availabilities availabilitiesJeudi = new Availabilities();
    private Availabilities availabilitiesVendredi = new Availabilities();
    private Availabilities availabilitiesSamedi = new Availabilities();
    private Availabilities availabilitiesDimanche = new Availabilities();
    private Business businessAfter;
    private ProgressBar progressBar;
    private boolean isAlertShown = false;
    int isLastChecked = -1;

    private void initializeAddView() {
        etLundi = findViewById(R.id.et_lundi_description);
        etMardi = findViewById(R.id.et_mardi_description);
        etMercredi = findViewById(R.id.et_mercredi_description);
        etJeudi = findViewById(R.id.et_jeudi_description);
        etVendredi = findViewById(R.id.et_vendredi_description);
        etSamedi = findViewById(R.id.et_samedi_description);
        etDimanche = findViewById(R.id.et_dimanche_description);
        /////////////////checkbox
        cbLundi = findViewById(R.id.cb_lundi);
        cbMardi = findViewById(R.id.cb_mardi);
        cbMercredi = findViewById(R.id.cb_mercredi);
        cbJeudi = findViewById(R.id.cb_jeudi);
        cbVendredi = findViewById(R.id.cb_vendredi);
        cbSamedi = findViewById(R.id.cb_samedi);
        cbDimanche = findViewById(R.id.cb_dimanche);
        ///////////////////dayName
        tvLundiName = findViewById(R.id.lbl_lundi);
        tvMardiName = findViewById(R.id.lbl_mardi);
        tvMercrediName = findViewById(R.id.lbl_mercredi);
        tvJeudiName = findViewById(R.id.lbl_jeudi);
        tvVendrediName = findViewById(R.id.lbl_vendredi);
        tvSamediName = findViewById(R.id.lbl_samedi);
        tvDimancheName = findViewById(R.id.lbl_dimanche);
        ///////////////////dayde
        tvLundiDe = findViewById(R.id.lbl_de_lundi);
        tvMardiDe = findViewById(R.id.lbl_de_mardi);
        tvMercrediDe = findViewById(R.id.lbl_de_mercredi);
        tvJeudiDe = findViewById(R.id.lbl_de_jeudi);
        tvVendrediDe = findViewById(R.id.lbl_de_vendredi);
        tvSamediDe = findViewById(R.id.lbl_de_samedi);
        tvDimancheDe = findViewById(R.id.lbl_de_dimanche);
        //////////////////daya
        tvLundiA = findViewById(R.id.lbl_a_lundi);
        tvMardiA = findViewById(R.id.lbl_a_mardi);
        tvMercrediA = findViewById(R.id.lbl_a_mercredi);
        tvJeudiA = findViewById(R.id.lbl_a_jeudi);
        tvVendrediA = findViewById(R.id.lbl_a_vendredi);
        tvSamediA = findViewById(R.id.lbl_a_samedi);
        tvDimancheA = findViewById(R.id.lbl_a_dimanche);
        ////////////////timede
        tvLundiDeTime = findViewById(R.id.lbl_de_time_lundi);
        tvMardiDeTime = findViewById(R.id.lbl_de_time_mardi);
        tvMercrediDeTime = findViewById(R.id.lbl_de_time_mercredi);
        tvJeudiDeTime = findViewById(R.id.lbl_de_time_jeudi);
        tvVendrediDeTime = findViewById(R.id.lbl_de_time_vendredi);
        tvSamediDeTime = findViewById(R.id.lbl_de_time_samedi);
        tvDimancheDeTime = findViewById(R.id.lbl_de_time_dimanche);
        /////////////////timea
        tvLundiATime = findViewById(R.id.lbl_a_time_lundi);
        tvMardiATime = findViewById(R.id.lbl_a_time_mardi);
        tvMercrediATime = findViewById(R.id.lbl_a_time_mercredi);
        tvJeudiATime = findViewById(R.id.lbl_a_time_jeudi);
        tvVendrediATime = findViewById(R.id.lbl_a_time_vendredi);
        tvSamediATime = findViewById(R.id.lbl_a_time_samedi);
        tvDimancheATime = findViewById(R.id.lbl_a_time_dimanche);

        cbLundi.setOnClickListener(this);
        cbMardi.setOnClickListener(this);
        cbMercredi.setOnClickListener(this);
        cbJeudi.setOnClickListener(this);
        cbVendredi.setOnClickListener(this);
        cbSamedi.setOnClickListener(this);
        cbDimanche.setOnClickListener(this);
        tvLundiDe.setOnClickListener(this);
        tvMardiDe.setOnClickListener(this);
        tvMercrediDe.setOnClickListener(this);
        tvJeudiDe.setOnClickListener(this);
        tvVendrediDe.setOnClickListener(this);
        tvSamediDe.setOnClickListener(this);
        tvDimancheDe.setOnClickListener(this);
        tvLundiDeTime.setOnClickListener(this);
        tvMardiDeTime.setOnClickListener(this);
        tvMercrediDeTime.setOnClickListener(this);
        tvJeudiDeTime.setOnClickListener(this);
        tvVendrediDeTime.setOnClickListener(this);
        tvSamediDeTime.setOnClickListener(this);
        tvDimancheDeTime.setOnClickListener(this);
        /////////////////////////////////////////
        tvLundiA.setOnClickListener(this);
        tvMardiA.setOnClickListener(this);
        tvMercrediA.setOnClickListener(this);
        tvJeudiA.setOnClickListener(this);
        tvVendrediA.setOnClickListener(this);
        tvSamediA.setOnClickListener(this);
        tvDimancheA.setOnClickListener(this);
        tvLundiATime.setOnClickListener(this);
        tvMardiATime.setOnClickListener(this);
        tvMercrediATime.setOnClickListener(this);
        tvJeudiATime.setOnClickListener(this);
        tvVendrediATime.setOnClickListener(this);
        tvSamediATime.setOnClickListener(this);
        tvDimancheATime.setOnClickListener(this);

    }

    private void UpdateViewLundi() {
        if (cbLundi.isChecked()) {

            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etVendredi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etLundi.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 1;
            tvLundiName.setTextColor(getResources().getColor(R.color.black));
            tvLundiDe.setTextColor(getResources().getColor(R.color.black));
            tvLundiA.setTextColor(getResources().getColor(R.color.black));
            tvLundiDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvLundiATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etLundi.requestFocus();
            etLundi.setEnabled(true);

        } else {
            isLastChecked = -1;
            tvLundiName.setTextColor(getResources().getColor(R.color.gray_2));
            tvLundiDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvLundiA.setTextColor(getResources().getColor(R.color.gray_2));
            tvLundiDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvLundiATime.setTextColor(getResources().getColor(R.color.gray_2));
            etLundi.setEnabled(false);
        }
    }

    private void UpdateViewMardi() {
        if (cbMardi.isChecked()) {
            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etVendredi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etMardi.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 2;
            tvMardiName.setTextColor(getResources().getColor(R.color.black));
            tvMardiDe.setTextColor(getResources().getColor(R.color.black));
            tvMardiA.setTextColor(getResources().getColor(R.color.black));
            tvMardiDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMardiATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etMardi.requestFocus();
            etMardi.setEnabled(true);
        } else {

            if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }

            tvMardiName.setTextColor(getResources().getColor(R.color.gray_2));
            tvMardiDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvMardiA.setTextColor(getResources().getColor(R.color.gray_2));
            tvMardiDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvMardiATime.setTextColor(getResources().getColor(R.color.gray_2));
            etMardi.setEnabled(false);

        }
    }

    private void UpdateViewMercredi() {
        if (cbMercredi.isChecked()) {
            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etVendredi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etMercredi.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 3;
            tvMercrediName.setTextColor(getResources().getColor(R.color.black));
            tvMercrediDe.setTextColor(getResources().getColor(R.color.black));
            tvMercrediA.setTextColor(getResources().getColor(R.color.black));
            tvMercrediDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMercrediATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etMercredi.requestFocus();
            etMercredi.setEnabled(true);
        } else {
            if (isLastChecked == 2) {
                isLastChecked = 2;
            } else if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }
            tvMercrediName.setTextColor(getResources().getColor(R.color.gray_2));
            tvMercrediDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvMercrediA.setTextColor(getResources().getColor(R.color.gray_2));
            tvMercrediDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvMercrediATime.setTextColor(getResources().getColor(R.color.gray_2));
            etMercredi.setEnabled(false);
        }
    }

    private void UpdateViewJeudi() {
        if (cbJeudi.isChecked()) {
            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etVendredi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etJeudi.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 4;
            tvJeudiName.setTextColor(getResources().getColor(R.color.black));
            tvJeudiDe.setTextColor(getResources().getColor(R.color.black));
            tvJeudiA.setTextColor(getResources().getColor(R.color.black));
            tvJeudiDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvJeudiATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etJeudi.requestFocus();
            etJeudi.setEnabled(true);
        } else {
            if (isLastChecked == 3) {
                isLastChecked = 3;
            } else if (isLastChecked == 2) {
                isLastChecked = 2;
            } else if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }
            tvJeudiName.setTextColor(getResources().getColor(R.color.gray_2));
            tvJeudiDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvJeudiA.setTextColor(getResources().getColor(R.color.gray_2));
            tvJeudiDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvJeudiATime.setTextColor(getResources().getColor(R.color.gray_2));
            etJeudi.setEnabled(false);
        }
    }

    private void UpdateViewVendredi() {
        if (cbVendredi.isChecked()) {
            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etVendredi.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 5;
            tvVendrediName.setTextColor(getResources().getColor(R.color.black));
            tvVendrediDe.setTextColor(getResources().getColor(R.color.black));
            tvVendrediA.setTextColor(getResources().getColor(R.color.black));
            tvVendrediDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvVendrediATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etVendredi.requestFocus();
            etVendredi.setEnabled(true);
        } else {

            if (isLastChecked == 4) {
                isLastChecked = 4;
            } else if (isLastChecked == 3) {
                isLastChecked = 3;
            } else if (isLastChecked == 2) {
                isLastChecked = 2;
            } else if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }
            tvVendrediName.setTextColor(getResources().getColor(R.color.gray_2));
            tvVendrediDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvVendrediA.setTextColor(getResources().getColor(R.color.gray_2));
            tvVendrediDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvVendrediATime.setTextColor(getResources().getColor(R.color.gray_2));
            etVendredi.setEnabled(false);
        }
    }

    private void UpdateViewSamedi() {
        if (cbSamedi.isChecked()) {
            if (isLastChecked == 0) {
                if (!etDimanche.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etDimanche.getText().toString());
                }
            } else if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etSamedi.setText(etVendredi.getText().toString());
                }
            }
            isLastChecked = 6;
            tvSamediName.setTextColor(getResources().getColor(R.color.black));
            tvSamediDe.setTextColor(getResources().getColor(R.color.black));
            tvSamediA.setTextColor(getResources().getColor(R.color.black));
            tvSamediDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvSamediATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etSamedi.requestFocus();
            etSamedi.setEnabled(true);
        } else {
            if (isLastChecked == 5) {
                isLastChecked = 5;
            } else if (isLastChecked == 4) {
                isLastChecked = 4;
            } else if (isLastChecked == 3) {
                isLastChecked = 3;
            } else if (isLastChecked == 2) {
                isLastChecked = 2;
            } else if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }
            tvSamediName.setTextColor(getResources().getColor(R.color.gray_2));
            tvSamediDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvSamediA.setTextColor(getResources().getColor(R.color.gray_2));
            tvSamediDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvSamediATime.setTextColor(getResources().getColor(R.color.gray_2));
            etSamedi.setEnabled(false);
        }
    }

    private void UpdateViewDimanche() {
        if (cbDimanche.isChecked()) {
            if (isLastChecked == 1) {
                if (!etLundi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etLundi.getText().toString());
                }
            } else if (isLastChecked == 2) {
                if (!etMardi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etMardi.getText().toString());
                }
            } else if (isLastChecked == 3) {
                if (!etMercredi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etMercredi.getText().toString());
                }
            } else if (isLastChecked == 4) {
                if (!etJeudi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etJeudi.getText().toString());
                }
            } else if (isLastChecked == 5) {
                if (!etVendredi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etVendredi.getText().toString());
                }
            } else if (isLastChecked == 6) {
                if (!etSamedi.getText().toString().equalsIgnoreCase("")) {
                    etDimanche.setText(etSamedi.getText().toString());
                }
            }
            isLastChecked = 0;
            tvDimancheName.setTextColor(getResources().getColor(R.color.black));
            tvDimancheDe.setTextColor(getResources().getColor(R.color.black));
            tvDimancheA.setTextColor(getResources().getColor(R.color.black));
            tvDimancheDeTime.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvDimancheATime.setTextColor(getResources().getColor(R.color.colorPrimary));
            etDimanche.requestFocus();
            etDimanche.setEnabled(true);
        } else {
            if (isLastChecked == 6) {
                isLastChecked = 6;
            } else if (isLastChecked == 5) {
                isLastChecked = 5;
            } else if (isLastChecked == 4) {
                isLastChecked = 4;
            } else if (isLastChecked == 3) {
                isLastChecked = 3;
            } else if (isLastChecked == 2) {
                isLastChecked = 2;
            } else if (isLastChecked == 1) {
                isLastChecked = 1;
            } else {
                isLastChecked = -1;
            }
            tvDimancheName.setTextColor(getResources().getColor(R.color.gray_2));
            tvDimancheDe.setTextColor(getResources().getColor(R.color.gray_2));
            tvDimancheA.setTextColor(getResources().getColor(R.color.gray_2));
            tvDimancheDeTime.setTextColor(getResources().getColor(R.color.gray_2));
            tvDimancheATime.setTextColor(getResources().getColor(R.color.gray_2));
            etDimanche.setEnabled(false);
        }
    }

    private void showListHours() {
        try {
            if (business.getHours().size() == 0) {
                hideListHours();
            } else {
                rlLayoutAddHappyHour.setVisibility(View.GONE);
                rvHappyHour.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.VISIBLE);
                layoutEmptyHappyHour.setVisibility(View.GONE);

                happyHourArrayAdapter = new HappyHourArrayAdapter(getBaseContext(), happyHours);
                try {
                    rvHappyHour.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                } catch (NullPointerException e) {

                }
                try {

                    rvHappyHour.setAdapter(happyHourArrayAdapter);


                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());

                }
            }

        } catch (NullPointerException e) {
            hideListHours();
            System.out.println(e.getMessage());

        }

    }

    private void hideListHours() {
        rvHappyHour.setVisibility(View.GONE);
        ivEdit.setVisibility(View.GONE);
        layoutEmptyHappyHour.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_hour);
        layoutEmptyHappyHour = findViewById(R.id.layout_empty_happy_hour);
        toolbar = findViewById(R.id.toolbar);
        toolbarUpdate = findViewById(R.id.toolbar_update);
        rvHappyHour = findViewById(R.id.rv_happy_hours);
        ivBack = findViewById(R.id.iv_back);
        ivBackUpdate = findViewById(R.id.iv_back_update);
        tvSave = findViewById(R.id.tv_save);

        ivBackUpdate.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivEdit = findViewById(R.id.iv_edit);
        rlLayoutAddHappyHour = findViewById(R.id.rl_layout_add_happy_hour);
        addView = findViewById(R.id.layout_happy_hours_add);
        layoutAddHappyHour = findViewById(R.id.layout_add_happy_hour);

        layoutAddHappyHour.setOnClickListener(this);
        initializeAddView();

        progressBar = findViewById(R.id.progressBar);
        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        if (getIntent() != null) {
            business = (Business) getIntent().getSerializableExtra("business");

            happyHours = business.getHours();

            if (happyHours.size() == 0) {
                hideListHours();
            } else {
                showView();

            }
        }
    }

    private void showView() {
        rvHappyHour.setVisibility(View.VISIBLE);
        showListHours();
    }

    private void hideView() {

        rvHappyHour.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == tvLundiDeTime || v == tvLundiDe) {
            if (cbLundi.isChecked())
                showHourPicker(tvLundiDeTime);
        }
        if (v == tvMardiDeTime || v == tvMardiDe) {
            if (cbMardi.isChecked())
                showHourPicker(tvMardiDeTime);
        }
        if (v == tvMercrediDeTime || v == tvMercrediDe) {
            if (cbMercredi.isChecked())
                showHourPicker(tvMercrediDeTime);
        }
        if (v == tvJeudiDeTime || v == tvJeudiDe) {
            if (cbJeudi.isChecked())
                showHourPicker(tvJeudiDeTime);
        }
        if (v == tvVendrediDeTime || v == tvVendrediDe) {
            if (cbVendredi.isChecked())
                showHourPicker(tvVendrediDeTime);
        }
        if (v == tvSamediDeTime || v == tvSamediDe) {
            if (cbSamedi.isChecked())
                showHourPicker(tvSamediDeTime);
        }
        if (v == tvDimancheDeTime || v == tvDimancheDe) {
            if (cbDimanche.isChecked())
                showHourPicker(tvDimancheDeTime);
        }
        //////////////////////////
        if (v == tvLundiATime || v == tvLundiA) {
            if (cbLundi.isChecked())
                showHourPicker(tvLundiATime);
        }
        if (v == tvMardiATime || v == tvMardiA) {
            if (cbMardi.isChecked())
                showHourPicker(tvMardiATime);
        }
        if (v == tvMercrediATime || v == tvMercrediA) {
            if (cbMercredi.isChecked())
                showHourPicker(tvMercrediATime);
        }
        if (v == tvJeudiATime || v == tvJeudiA) {
            if (cbJeudi.isChecked())
                showHourPicker(tvJeudiATime);
        }
        if (v == tvVendrediATime || v == tvVendrediA) {
            if (cbVendredi.isChecked())
                showHourPicker(tvVendrediATime);
        }
        if (v == tvSamediATime || v == tvSamediA) {
            if (cbSamedi.isChecked())
                showHourPicker(tvSamediATime);
        }
        if (v == tvDimancheATime || v == tvDimancheA) {
            if (cbDimanche.isChecked())
                showHourPicker(tvDimancheATime);
        }
        if (v == cbLundi) {
            UpdateViewLundi();
        }
        if (v == cbMardi) {
            UpdateViewMardi();
        }
        if (v == cbMercredi) {
            UpdateViewMercredi();
        }
        if (v == cbJeudi) {
            UpdateViewJeudi();
        }
        if (v == cbVendredi) {
            UpdateViewVendredi();
        }
        if (v == cbSamedi) {
            UpdateViewSamedi();
        }
        if (v == cbDimanche) {
            UpdateViewDimanche();
        }
        if (v == layoutAddHappyHour) {
            openAddHours(rlLayoutAddHappyHour);
        }
        if (v == ivBack) {
            if(tvSave.isShown())
                closeAddHours(rlLayoutAddHappyHour);
            else {
                finish();
            }
        }
        if (v == ivEdit) {
            isAddOpen = true;
            editHappyHour();
        }
        if (v == ivBackUpdate) {
            if (isAddOpen)
                closeAddHours(rlLayoutAddHappyHour);

        }
        if (v == tvSave) {
            WidgetUtils.hideKeyboard(getCurrentFocus());
            isAddOpen = false;
            sendHappyHour();
            registerDataAdress();
            finish();
        }
    }


    private void sendHappyHour() {
        if (!ConnectivityService.isOnline(this)) {
            hideView();
        } else {
            showView();
            JsonArray hourItem = new JsonArray();
            JsonObject hourObject = new JsonObject();

            JsonObject objectLundi = new JsonObject();
            JsonObject objectMardi = new JsonObject();
            JsonObject objectMercredi = new JsonObject();
            JsonObject objectJeudi = new JsonObject();
            JsonObject objectVendredi = new JsonObject();
            JsonObject objectSamedi = new JsonObject();
            JsonObject objectDimanche = new JsonObject();


            JsonArray hoursLundi = new JsonArray();
            JsonObject hourOpenCloseLundi = new JsonObject();
            JsonObject isAvailableLundi = new JsonObject();
            JsonObject descriptionLundi = new JsonObject();
            JsonObject dayOfTheWeekLundi = new JsonObject();
            JsonArray hoursMardi = new JsonArray();
            JsonObject hourOpenCloseMardi = new JsonObject();
            JsonObject isAvailableMardi = new JsonObject();
            JsonObject descriptionMardi = new JsonObject();
            JsonObject dayOfTheWeekMardi = new JsonObject();
            JsonArray hoursMercredi = new JsonArray();
            JsonObject hourOpenCloseMercredi = new JsonObject();
            JsonObject isAvailableMercredi = new JsonObject();
            JsonObject descriptionMercredi = new JsonObject();
            JsonObject dayOfTheWeekMercredi = new JsonObject();
            JsonArray hoursJeudi = new JsonArray();
            JsonObject hourOpenCloseJeudi = new JsonObject();
            JsonObject isAvailableJeudi = new JsonObject();
            JsonObject descriptionJeudi = new JsonObject();
            JsonObject dayOfTheWeekJeudi = new JsonObject();
            JsonArray hoursVendredi = new JsonArray();
            JsonObject hourOpenCloseVendredi = new JsonObject();
            JsonObject isAvailableVendredi = new JsonObject();
            JsonObject descriptionVendredi = new JsonObject();
            JsonObject dayOfTheWeekVendredi = new JsonObject();
            JsonArray hoursSamedi = new JsonArray();
            JsonObject hourOpenCloseSamedi = new JsonObject();
            JsonObject isAvailableSamedi = new JsonObject();
            JsonObject descriptionSamedi = new JsonObject();
            JsonObject dayOfTheWeekSamedi = new JsonObject();
            JsonArray hoursDimanche = new JsonArray();
            JsonObject hourOpenCloseDimanche = new JsonObject();
            JsonObject isAvailableDimanche = new JsonObject();
            JsonObject descriptionDimanche = new JsonObject();
            JsonObject dayOfTheWeekDimanche = new JsonObject();

            if (cbDimanche.isChecked()) {
                hourOpenCloseDimanche.addProperty("openTime", tvDimancheDeTime.getText().toString());
                hourOpenCloseDimanche.addProperty("closeTime", tvDimancheATime.getText().toString());
                hoursDimanche.add(hourOpenCloseDimanche);

            } else {
                hourOpenCloseDimanche.addProperty("openTime", "00:00");
                hourOpenCloseDimanche.addProperty("closeTime", "00:00");
                hoursDimanche.add(hourOpenCloseDimanche);

            }
            if (cbLundi.isChecked()) {
                hourOpenCloseLundi.addProperty("openTime", tvLundiDeTime.getText().toString());
                hourOpenCloseLundi.addProperty("closeTime", tvLundiATime.getText().toString());
                hoursLundi.add(hourOpenCloseLundi);

            } else {
                hourOpenCloseLundi.addProperty("openTime", "00:00");
                hourOpenCloseLundi.addProperty("closeTime", "00:00");
                hoursLundi.add(hourOpenCloseLundi);

            }
            if (cbMardi.isChecked()) {
                hourOpenCloseMardi.addProperty("openTime", tvMardiDeTime.getText().toString());
                hourOpenCloseMardi.addProperty("closeTime", tvMardiATime.getText().toString());
                hoursMardi.add(hourOpenCloseMardi);

            } else {
                hourOpenCloseMardi.addProperty("openTime", "00:00");
                hourOpenCloseMardi.addProperty("closeTime", "00:00");
                hoursMardi.add(hourOpenCloseMardi);

            }
            if (cbMercredi.isChecked()) {
                hourOpenCloseMercredi.addProperty("openTime", tvMercrediDeTime.getText().toString());
                hourOpenCloseMercredi.addProperty("closeTime", tvMercrediATime.getText().toString());
                hoursMercredi.add(hourOpenCloseMercredi);

            } else {
                hourOpenCloseMercredi.addProperty("openTime", "00:00");
                hourOpenCloseMercredi.addProperty("closeTime", "00:00");
                hoursMercredi.add(hourOpenCloseMercredi);

            }
            if (cbJeudi.isChecked()) {
                hourOpenCloseJeudi.addProperty("openTime", tvJeudiDeTime.getText().toString());
                hourOpenCloseJeudi.addProperty("closeTime", tvJeudiATime.getText().toString());
                hoursJeudi.add(hourOpenCloseJeudi);


            } else {
                hourOpenCloseJeudi.addProperty("openTime", "00:00");
                hourOpenCloseJeudi.addProperty("closeTime", "00:00");
                hoursJeudi.add(hourOpenCloseJeudi);

            }
            if (cbVendredi.isChecked()) {
                hourOpenCloseVendredi.addProperty("openTime", tvVendrediDeTime.getText().toString());
                hourOpenCloseVendredi.addProperty("closeTime", tvVendrediATime.getText().toString());
                hoursVendredi.add(hourOpenCloseVendredi);

            } else {
                hourOpenCloseVendredi.addProperty("openTime", "00:00");
                hourOpenCloseVendredi.addProperty("closeTime", "00:00");
                hoursVendredi.add(hourOpenCloseVendredi);

            }
            if (cbSamedi.isChecked()) {
                hourOpenCloseSamedi.addProperty("openTime", tvSamediDeTime.getText().toString());
                hourOpenCloseSamedi.addProperty("closeTime", tvSamediATime.getText().toString());
                hoursSamedi.add(hourOpenCloseSamedi);

            } else {
                hourOpenCloseSamedi.addProperty("openTime", "00:00");
                hourOpenCloseSamedi.addProperty("closeTime", "00:00");
                hoursSamedi.add(hourOpenCloseSamedi);

            }

            objectLundi.add("hours", hoursLundi);
            objectLundi.addProperty("dayOfTheWeek", 1);
            objectLundi.addProperty("description", etLundi.getText().toString());
            if (cbLundi.isChecked())
                objectLundi.addProperty("isAvailable", true);
            else
                objectLundi.addProperty("isAvailable", false);
            ////////
            objectMardi.add("hours", hoursMardi);
            objectMardi.addProperty("dayOfTheWeek", 2);
            objectMardi.addProperty("description", etMardi.getText().toString());
            if (cbMardi.isChecked())
                objectMardi.addProperty("isAvailable", true);
            else
                objectMardi.addProperty("isAvailable", false);
            ////////
            objectMercredi.add("hours", hoursMercredi);
            objectMercredi.addProperty("dayOfTheWeek", 3);
            objectMercredi.addProperty("description", etMercredi.getText().toString());
            if (cbMercredi.isChecked())
                objectMercredi.addProperty("isAvailable", true);
            else
                objectMercredi.addProperty("isAvailable", false);
            ////////
            objectJeudi.add("hours", hoursJeudi);
            objectJeudi.addProperty("dayOfTheWeek", 4);
            objectJeudi.addProperty("description", etJeudi.getText().toString());
            if (cbJeudi.isChecked())
                objectJeudi.addProperty("isAvailable", true);
            else
                objectJeudi.addProperty("isAvailable", false);
            ////////
            objectVendredi.add("hours", hoursVendredi);
            objectVendredi.addProperty("dayOfTheWeek", 5);
            objectVendredi.addProperty("description", etVendredi.getText().toString());
            if (cbVendredi.isChecked())
                objectVendredi.addProperty("isAvailable", true);
            else
                objectVendredi.addProperty("isAvailable", false);
            ////////
            objectSamedi.add("hours", hoursSamedi);
            objectSamedi.addProperty("dayOfTheWeek", 6);
            objectSamedi.addProperty("description", etSamedi.getText().toString());
            if (cbSamedi.isChecked())
                objectSamedi.addProperty("isAvailable", true);
            else
                objectSamedi.addProperty("isAvailable", false);
            ////////
            objectDimanche.add("hours", hoursDimanche);
            objectDimanche.addProperty("dayOfTheWeek", 0);
            objectDimanche.addProperty("description", etDimanche.getText().toString());
            if (cbDimanche.isChecked())
                objectDimanche.addProperty("isAvailable", true);
            else
                objectDimanche.addProperty("isAvailable", false);
            ////////
            hourItem.add(objectDimanche);
            hourItem.add(objectLundi);
            hourItem.add(objectMardi);
            hourItem.add(objectMercredi);
            hourItem.add(objectJeudi);
            hourItem.add(objectVendredi);
            hourItem.add(objectSamedi);
            Utils.HappyHour = hourItem;
            business.setHours(Conversion.covertJsonToHappyHour(hourItem));
        }
  }
    private void registerDataAdress() {
        Intent intent = new Intent();
        intent.putExtra("businessHappyHour", business);
        intent.setAction("refresh_state_happy_hour");
        sendBroadcast(intent);
        finish();

    }



    /*private void sendHourToService(JsonArray hourItem) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editHappyHours(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), hourItem);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                int code = response.code();
                System.out.println(response.code());
                System.out.println(response.message());

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {



                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getBaseContext() != null) {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("fai");
                    System.out.println(t.getMessage());
                }
            }
        });
    }*/

    private void openAddHours(RelativeLayout view) {
        view.startAnimation(AnimationUtils.loadAnimation(HappyHourActivity.this,
                R.anim.slide_up));
        view.setVisibility(View.VISIBLE);
        toolbarUpdate.setVisibility(View.VISIBLE);
        rvHappyHour.setVisibility(View.GONE);
        layoutEmptyHappyHour.setVisibility(View.GONE);
        isAddOpen = true;

    }


    private void closeAddHours(RelativeLayout view) {
        view.startAnimation(AnimationUtils.loadAnimation(HappyHourActivity.this,
                R.anim.slide_down));
        view.setVisibility(View.GONE);
        toolbarUpdate.setVisibility(View.GONE);
        isAddOpen = false;
        if (happyHours.size() == 0) {
            layoutEmptyHappyHour.setVisibility(View.VISIBLE);
            rvHappyHour.setVisibility(View.GONE);
        } else {
            layoutEmptyHappyHour.setVisibility(View.GONE);
            rvHappyHour.setVisibility(View.VISIBLE);
        }


    }


    public void showHourPicker(final TextView textView) {
        final Calendar myCalender = Calendar.getInstance();
        final int[] hour = {myCalender.get(Calendar.HOUR_OF_DAY)};
        final int[] minuteDay = {myCalender.get(Calendar.MINUTE)};
        TimePickerDialog.OnTimeSetListener timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                textView.setText(String.format("%02d:%02d", hourOfDay, minute));

            }
        };
        final TimePickerDialog timePickerDialog = new TimePickerDialog(HappyHourActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeEndSetListener, hour[0], minuteDay[0], true);
        timePickerDialog.setTitle("Choisissez une heure:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tvSave.isShown())
            closeAddHours(rlLayoutAddHappyHour);
        else {
           finish();
        }

    }

    public void editHappyHour() {
        HappyHour lundi;
        HappyHour mardi;
        HappyHour mercredi;
        HappyHour jeudi;
        HappyHour vendredi;
        HappyHour samedi;
        HappyHour dimanche;
        try {

            for (int i = 0; i < happyHours.size(); i++) {
                if (happyHours.get(i).getDayOfTheWeek() == 0) {
                    dimanche = happyHours.get(i);
                    try {
                     setDimanche(dimanche,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }

                }
                if (happyHours.get(i).getDayOfTheWeek() == 1) {
                    lundi = happyHours.get(i);
                    try {
                    setLundi(lundi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }

                }
                if (happyHours.get(i).getDayOfTheWeek() == 2) {
                    mardi = happyHours.get(i);
                    try {
                    setMardi(mardi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }

                }
                if (happyHours.get(i).getDayOfTheWeek() == 3) {
                    mercredi = happyHours.get(i);
                    try {
                    setMercredi(mercredi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }

                }
                if (happyHours.get(i).getDayOfTheWeek() == 4) {
                    jeudi = happyHours.get(i);
                    try {
                    setJeudi(jeudi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }
                }
                if (happyHours.get(i).getDayOfTheWeek() == 5) {
                    vendredi = happyHours.get(i);

                    try {
                    setVendredi(vendredi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }
                }
                if (happyHours.get(i).getDayOfTheWeek() == 6) {
                    samedi = happyHours.get(i);
                    try {
                        setSamedi(samedi,i);
                    }catch (IndexOutOfBoundsException e)
                    {

                    }

                }
                openAddHours(rlLayoutAddHappyHour);
            }

        } catch (NullPointerException e) {

        }

    }

    private void setSamedi(HappyHour samedi, int i) {
        if (samedi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && samedi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {

            cbSamedi.setChecked(false);
            UpdateViewSamedi();
        } else {
            etSamedi.setText(samedi.getDescription());
            tvSamediDeTime.setText(samedi.getHourss()[0].getOpenTime().toString());
            tvSamediATime.setText(samedi.getHourss()[0].getCloseTime().toString());
            cbSamedi.setChecked(true);
            UpdateViewSamedi();
        }
    }

    private void setVendredi(HappyHour vendredi, int i) {
        if (vendredi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && vendredi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {
            cbVendredi.setChecked(false);
            UpdateViewVendredi();
        } else {
            etVendredi.setText(vendredi.getDescription());
            tvVendrediDeTime.setText(vendredi.getHourss()[0].getOpenTime().toString());
            tvVendrediATime.setText(vendredi.getHourss()[0].getCloseTime().toString());

            cbVendredi.setChecked(true);
            UpdateViewVendredi();
        }
    }

    private void setJeudi(HappyHour jeudi, int i) {
        if (jeudi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && jeudi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {
            cbJeudi.setChecked(false);
            UpdateViewJeudi();
        } else {
            cbJeudi.setChecked(true);
            etJeudi.setText(jeudi.getDescription());
            tvJeudiDeTime.setText(jeudi.getHourss()[0].getOpenTime().toString());
            tvJeudiATime.setText(jeudi.getHourss()[0].getCloseTime().toString());

            UpdateViewJeudi();
        }
    }

    private void setMercredi(HappyHour mercredi, int i) {
        if (mercredi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && mercredi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {

            cbMercredi.setChecked(false);
            UpdateViewMercredi();
        } else {
            etMercredi.setText(mercredi.getDescription());
            tvMercrediDeTime.setText(mercredi.getHourss()[0].getOpenTime().toString());
            tvMercrediATime.setText(mercredi.getHourss()[0].getCloseTime().toString());
            cbMercredi.setChecked(true);
            UpdateViewMercredi();
        }
    }

    private void setMardi(HappyHour mardi, int i) {
        if (mardi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && mardi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {
            cbMardi.setChecked(false);
            UpdateViewMardi();
        } else {
            etMardi.setText(mardi.getDescription());
            tvMardiDeTime.setText(mardi.getHourss()[0].getOpenTime().toString());
            tvMardiATime.setText(mardi.getHourss()[0].getCloseTime().toString());

            cbMardi.setChecked(true);
            UpdateViewMardi();
        }
    }

    private void setDimanche(HappyHour dimanche, int i) {



        if (dimanche.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && dimanche.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {

            cbDimanche.setChecked(false);
            UpdateViewDimanche();
        } else {
            etDimanche.setText(dimanche.getDescription());
            tvDimancheDeTime.setText(dimanche.getHourss()[0].getOpenTime().toString());
            tvDimancheATime.setText(dimanche.getHourss()[0].getCloseTime().toString());
            cbDimanche.setChecked(true);
            UpdateViewDimanche();
        }
    }

    private void setLundi(HappyHour lundi, int i) {
        if (lundi.getHourss()[0].getOpenTime().equalsIgnoreCase("00:00") && lundi.getHourss()[0].getCloseTime().equalsIgnoreCase("00:00")) {

            cbLundi.setChecked(false);
            UpdateViewLundi();
        } else {
            cbLundi.setChecked(true);
            etLundi.setText(lundi.getDescription());
            tvLundiDeTime.setText(lundi.getHourss()[0].getOpenTime().toString());
            tvLundiATime.setText(lundi.getHourss()[0].getCloseTime().toString());
            UpdateViewLundi();
        }
    }
}
