package com.vyndsolutions.vyndteam.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.factories.ConnexionServiceFactory;
import com.vyndsolutions.vyndteam.models.Admin;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.utils.WidgetUtils;
import com.vyndsolutions.vyndteam.widgets.images.EmptyDataLayout;
import com.vyndsolutions.vyndteam.widgets.images.ui.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory.saveAdminToken;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button buttonConnexion;
    Context context;
    EmptyDataLayout emptyDataLayout;
    private View animateView;
    private LoadingButton LoadingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferencesFactory.initializeShared(this);
        getStoreUser();
        inisialiseInputs();
        inisialiseViews();

        LoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingButton.startLoading();
                WidgetUtils.hideKeyboard(getCurrentFocus());
                validateAdminData(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

    }

    private void inisialiseInputs() {
        context = getBaseContext();
        etEmail = findViewById(R.id.et_email_adress);
        etPassword = findViewById(R.id.et_password);
        //buttonConnexion = findViewById(R.id.LoadingButton);
        animateView = findViewById(R.id.animate_view);
        LoadingButton = findViewById(R.id.LoadingButton);
        LoadingButton.setResetAfterFailed(true);
    }

    private boolean getStoreUser() {
        Gson gson = new Gson();
        try {
            String json = SharedPreferencesFactory.pref.getString("userSession", "");
            Admin userFromSession = gson.fromJson(json, Admin.class);


            if (userFromSession != null) {

                if (!userFromSession.getEmail().equalsIgnoreCase("")) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
                return true;
            } else return false;
        } catch (NullPointerException e) {
            return false;
        }


    }

    private void storeUser(Admin user) {

        Gson gson = new Gson();
        String json = gson.toJson(user);
        SharedPreferencesFactory.editor.putString("userSession", json);
        SharedPreferencesFactory.editor.commit();
        System.out.println("User stored");
    }

    private void inisialiseViews() {
        if (SharedPreferencesFactory.getAdminData(context) != null) {
            etEmail.setText(SharedPreferencesFactory.getAdminData(context).getEmail());
        }

        LoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAdminData(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

    }


    private void toNextPage(final Admin admin) {

        int cx = (LoadingButton.getLeft() + LoadingButton.getRight()) / 2;
        int cy = (LoadingButton.getTop() + LoadingButton.getBottom()) / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(animateView, cx, cy, 0, getResources().getDisplayMetrics().heightPixels * 1.2f);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animateView.setVisibility(View.VISIBLE);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                storeUser(admin);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                LoadingButton.reset();
                  animateView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LoadingButton.reset();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void validateAdminData(final String emailAdress, final String passwd) {

        // "vyndee@vynd.com"
        // "vynd1234"


        JsonObject postParams = new JsonObject();
        postParams.addProperty("email", emailAdress);
        postParams.addProperty("password", passwd);

        Call<ResponseBody> call = ConnexionServiceFactory.getAdminData().getAdminData(postParams);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() != 201) {
                    new CustomToast(LoginActivity.this, getResources().getString(R.string.erreur), getResources().getString(R.string.verify_info_login), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    //LoadingButton.loadingFailed();
                    LoadingButton.loadingSuccessful();
                    LoadingButton.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
                        @Override
                        public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                            toNextPage(new Admin("vyndee@vynd.om", "vynd1234"));
                        }
                    });

                } else {
                    JSONObject jsonObject = null;

                    try {
                        final Admin admin;
                        Gson gson = new Gson();
                        jsonObject = new JSONObject(response.body().string());
                        admin = gson.fromJson(jsonObject.toString(), Admin.class);

                        Log.e("        test admin ", admin.getEmail());

                        Log.e("           test", "token = " + admin.getToken() + "     email " + admin.getEmail());
                        SharedPreferencesFactory.saveAdminPasswd(context, passwd, admin);
                        SharedPreferencesFactory.saveAdminData(context, admin);
                        saveAdminToken(context, admin);
                        LoadingButton.loadingSuccessful();
                        LoadingButton.setAnimationEndListener(new LoadingButton.AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(LoadingButton.AnimationType animationType) {
                                toNextPage(admin);
                            }
                        });


                    } catch (IOException e) {
                        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-");
                        e.printStackTrace();
                        LoadingButton.loadingFailed();
                        LoadingButton.reset();
                    } catch (JSONException e) {
                        System.out.println("/*/*/*/*/*/*/*/*/*/*/*/*/*/");
                        e.printStackTrace();
                        LoadingButton.loadingFailed();
                        LoadingButton.reset();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("111111111111");
                LoadingButton.loadingFailed();
                LoadingButton.reset();
            }
        });
        emptyDataLayout = new EmptyDataLayout(getBaseContext());
        emptyDataLayout.closeView();

    }
}


