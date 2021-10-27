package com.vyndsolutions.vyndteam.widgets.images;

/**
 * Created by Hoda on 16/04/2018.
 */


import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;

public class EmptyDataLayout extends CoordinatorLayout {
    public static boolean retryTrigred;
    public OnRetryListener onRetryListener;
    public OnConnectionOnListener onConnectionOnListener;
    public TextView feedLabel;
    private View rootView;
    private Context context;
    private ImageView img;
    private TextView label;
    private TextView tvRetry;
    private boolean isOpen = false;
    private TextView oops;
    private View emptyToolbar;
    private RecyclerView list;
    private LinearLayout layoutMain;
    private LinearLayout layoutFeed;
    private LinearLayout layoutRetry;
    private View mainToolbar;
    private TextView feedOops;
 //   private User user;
    private FragmentManager myFragmentManager;


    public EmptyDataLayout(Context context) {
        super(context);
        this.context = context;
        if (!isInEditMode())
            init();
    }

    /*public EmptyDataLayout(Context context, User user) {
        super(context);
        if (!isInEditMode()) {
            this.context = context;
            this.user = user;

            init();
        }
    }*/


    public EmptyDataLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init();
    }


    public EmptyDataLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            this.context = context;
            init();
        }
    }


 /*public void setListVynders(List<User> listVynders) {
        VynderItemRecyclerAdapter listContactFollowRecyclerAdapter = new VynderItemRecyclerAdapter(getContext(), true);
        listContactFollowRecyclerAdapter.swap(listVynders);
        list.setAdapter(listContactFollowRecyclerAdapter);
    }*/



/*public void setUser(User user) {
        this.user = user;
    }*/


    public void setContext(Context context) {
        this.context = context;
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.empty_data_layout, EmptyDataLayout.this, true);



        rootView = findViewById(R.id.root_view);
        img = findViewById(R.id.img);
        label = findViewById(R.id.label);
        oops = findViewById(R.id.oops);
        layoutRetry = findViewById(R.id.layout_retry);
        tvRetry = findViewById(R.id.tv_retry);
        emptyToolbar = findViewById(R.id.empty_layout);
        mainToolbar = findViewById(R.id.maintoolbar);
        layoutMain = findViewById(R.id.layout_main);
        layoutFeed = findViewById(R.id.layout_my_feed);
        feedLabel = findViewById(R.id.feed_label);
        feedOops = findViewById(R.id.feed_oops);
        Log.e("init  ", isInEditMode()+"");


    }

    public TextView getLabel() {
        return label;
    }

    public void openView(int mode) {
        oops.setVisibility(VISIBLE);
        emptyToolbar.setVisibility(GONE);
        layoutFeed.setVisibility(GONE);
        layoutRetry.setVisibility(VISIBLE);
        layoutRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRetryListener != null) {
                    onRetryListener.onRetryListener();
                }
                if (ConnectivityService.isOnline(context)) {
                    if (onConnectionOnListener != null)
                        onConnectionOnListener.onConnectionOnListener();
                }
            }
        });
        switch (mode) {
            //no connection
            case 0:
                oops.setText(context.getString(R.string.connectivity_error));

                //img.setImageResource(R.drawable.no_internet);
                label.setText(context.getString(R.string.message_connectivity_error));
                label.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                setOnConnectionOnListener(new OnConnectionOnListener() {
                    @Override
                    public void onConnectionOnListener() {
                        if (!ConnectivityService.isOnline(context)) {
                            if (onRetryListener != null)
                                onRetryListener.onRetryListener();

                        } else {
                           // checkForUpdates();
                        }

                    }
                });
                break;
            //no gps
            case 1:
                oops.setText(context.getString(R.string.connectivity_gps_error_title));
                //oops.setVisibility(GONE);
                //img.setImageResource(R.drawable.no_gps);
                label.setText(context.getString(R.string.connectivity_gps_error));

                layoutRetry.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                break;
            //no business result
            case 2:

                oops.setText(context.getString(R.string.title_no_result_found));
                oops.setVisibility(GONE);
               // img.setImageResource(R.drawable.no_result_found);
                label.setText(context.getString(R.string.business_notfound));
                tvRetry.setText(context.getString(R.string.add_business));
                tvRetry.setTextColor(getResources().getColor(R.color.white));
                layoutRetry.setBackgroundResource(R.drawable.bg_rounded_primary);
//                layoutRetry.setBackgroundColor(Color.parseColor(context.getString(R.color.colorPrimary)));

                break;
            //no business result nearby
          /*  case 15:
                layoutRetry.setVisibility(GONE);
                oops.setText(context.getString(R.string.title_no_result_found_nearby));
                //img.setImageResource(R.drawable.no_result_found);
                label.setText(context.getString(R.string.message_no_result_found_nearby));
                break;*/
            //server error
            case 3:
                oops.setText(context.getString(R.string.technical_error_title));
                //img.setImageResource(R.drawable.technical_error);
                label.setText(context.getString(R.string.technical_error));
                layoutRetry.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvRetry.setTextColor(getResources().getColor(R.color.white));
                /*layoutRetry.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (context, context.getClass());
                        context.startActivity(intent);
                    }
                });*/
                break;
            // email or password error
            case 4:
                oops.setText(context.getString(R.string.user_account_error));
                //img.setImageResource(R.drawable.technical_error);
                label.setText(context.getString(R.string.account_error));
                layoutRetry.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tvRetry.setTextColor(getResources().getColor(R.color.white));
                layoutRetry.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (context, context.getClass());
                        context.startActivity(intent);
                    }
                });
                break;

            //server error with 2nd toolabr empty space
            /*case 24:
                emptyToolbar.setVisibility(VISIBLE);
                img.setImageResource(R.drawable.technical_error);
                label.setText(context.getString(R.string.technical_error));
                break;
            //server error with 2nd toolabr empty space
            case 25:
                mainToolbar.setVisibility(GONE);
                img.setImageResource(R.drawable.technical_error);
                label.setText(context.getString(R.string.technical_error));
                break;
            //no business result roulette
            case 26:
                oops.setText(context.getString(R.string.nothing_nearby));
                img.setImageResource(R.drawable.no_result_found);
                label.setText(context.getString(R.string.business_notfound_roulette));
                break;

            case 29:
                oops.setText(context.getString(R.string.label_no_internet));
                img.setImageResource(R.drawable.no_internet);
                label.setText(context.getString(R.string.message_connectivity_error));
                break;
            case 30:

                oops.setText(context.getString(R.string.not_found));
                oops.setVisibility(GONE);
                img.setImageResource(R.drawable.no_result_found);
                label.setText(context.getString(R.string.business_notfound_filter));
                layoutRetry.setVisibility(GONE);

                break;

            case 31:

                oops.setVisibility(GONE);
                img.setImageResource(R.drawable.login_illustration);
                label.setText(context.getString(R.string.message_login));
                layoutRetry.setBackgroundResource(R.drawable.bg_rounded_primary);
                layoutRetry.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
                tvRetry.setText(context.getString(R.string.connect));
                tvRetry.setTextColor(Color.WHITE);

                break;

            case 35:
                layoutRetry.setBackgroundColor(Color.parseColor(context.getString(R.color.colorPrimary)));
                tvRetry.setTextColor(Color.parseColor(context.getString(R.color.white)));
                oops.setVisibility(GONE);
                img.setImageResource(R.drawable.no_data);
                label.setText(context.getString(R.string.label_cannot_load_data));
                break;*/
        }
        rootView.setVisibility(VISIBLE);
        isOpen = true;
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void closeView() {
        if (!isOpen) {
            return;
        }
        rootView.setVisibility(GONE);
        isOpen = false;
    }

    /*private void checkForUpdates() {


  if(!ConnectivityService.isOnline(context))
        {
            openView(0);

        }
        *//**//*

        try {
            myFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            JsonObject postParamToken = new JsonObject();
            postParamToken.addProperty("token", "");
            Call<ResponseBody> call = RetrofitServiceFacotry.getAccountApiRetrofitServiceClient().token(postParamToken);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {

                        System.out.println("BODYY" + response.body());


                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = response.code();
                        if (code == 200) {

                            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            String version = pInfo.versionName;
                            Version a = new Version(version);
                            Version b = new Version(jsonObject.getString("app_version"));
                            switch (jsonObject.getInt("type")) {
                                case 1:
                                    if (a.compareTo(b) == -1) {
                                        if (jsonObject.getInt("state") == 1) {
                                            EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(9);
                                            FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                            transaction.add(emptyDataDialog, "loading");
                                            transaction.commitAllowingStateLoss();
                                        }
                                        if (jsonObject.getInt("state") == 2) {
                                            EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(8);
                                            FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                            transaction.add(emptyDataDialog, "loading");
                                            transaction.commitAllowingStateLoss();
                                        }
                                    }
                                    break;
                            }
                        }


                    } catch (JSONException | IOException | NullPointerException | PackageManager.NameNotFoundException e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        } catch (ClassCastException e) {
        }
    }*/


    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public void setOnConnectionOnListener(OnConnectionOnListener onConnectionOnListener) {
        this.onConnectionOnListener = onConnectionOnListener;
    }

    public View getMainToolbar() {
        return mainToolbar;
    }

    public View getEmptyToolbar() {
        return emptyToolbar;
    }

    public interface OnRetryListener {
        void onRetryListener();
    }

    public interface OnConnectionOnListener {
        void onConnectionOnListener();
    }
}


