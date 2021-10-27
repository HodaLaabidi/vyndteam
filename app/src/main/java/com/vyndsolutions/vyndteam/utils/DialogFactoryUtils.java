package com.vyndsolutions.vyndteam.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.info.CategoriesRecyclerAdapter;
import com.vyndsolutions.vyndteam.adapters.info.RegionRecyclerAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.Region;
import com.vyndsolutions.vyndteam.models.ValidInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Hoda on 10/05/2018.
 */

public class DialogFactoryUtils {

    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    private OnConfirmSubCategoryConfirmListener onConfirmSubCategoryConfirmListener;
    private OnConfirmRegionSelect onConfirmRegionSelectListener;
    private RecyclerView rvRegionAutoComplete;
    private CardView cardRegionAutocomplete;
    private TextView etRegion;
    private Region selectedRegion;
    private RegionRecyclerAdapter regionRecyclerAdapter;
    private List<Region> regions = new ArrayList<>(), displayedRegions = new ArrayList<>();
    private boolean isCardAutoVisible = false;
    private OnConfirmSpecialityConfirmListener onConfirmSpecialityConfirmListener;

    private boolean getAllRegions(final Context context) {

        if (CompteManagerService.getAllRegion(context) != null) {
            regions = CompteManagerService.getAllRegion(context);
        } else if (ConnectivityService.isOnline(context)) {
            Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getAllRegions(1, 0, 1000);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        regions = new ArrayList<Region>();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = response.code();
                        if (code == 200) {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            Gson gson = Utils.getGsonInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                regions.add(gson.fromJson(jsonArray.getString(i), Region.class));
                            }
//                            CompteManagerService.saveAllSubCategories(context, listSubCategories);
                            CompteManagerService.saveAllRegion(context, regions);


                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        if (context != null) {
                            //region error event log
                           /* Bundle params = new Bundle();
                            params.putString("type", "Catch exception");
                            params.putString("source", "User Edit Fragment");
                            params.putString("value", "Get All selectedRegions catch exception : " + e.getMessage());
                            if (context != null)
                                vyndGlobal.logEvent("Error_event", params);
                            //endregion
                            EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(2);
                            emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                @Override
                                public void onMainButtonClickListener() {
                                    getAllRegions();
                                }
                            });
                            FragmentTransaction transaction = myFragmentManager.beginTransaction();
                            transaction.add(emptyDataDialog, "loading");
                            transaction.commitAllowingStateLoss();*/
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (!call.isCanceled()) {

                       /* if (t instanceof SocketTimeoutException) {
                            if (context != null) {
                                //region error event log
                                Bundle params = new Bundle();
                                params.putString("type", "Timeout");
                                params.putString("source", "User Edit Fragment");
                                params.putString("value", t.getMessage());
                                if (context != null)
                                  //  vyndGlobal.logEvent("Error_event", params);
                            }
                            //endregion
                        } else if (t instanceof UnknownHostException) {
                            if (context != null) {
                                //region error event log
                              /*  Bundle params = new Bundle();
                                params.putString("type", "Uknown Host Exception");
                                params.putString("source", "User Edit Fragment");
                                params.putString("value", t.getMessage());
                                if (context != null)
                                   // vyndGlobal.logEvent("Error_event", params);
                                //endregion
                                EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(6);
                                emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                    @Override
                                    public void onMainButtonClickListener() {
                                        getAllRegions();
                                    }
                                });
                                FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                transaction.add(emptyDataDialog, "loading");
                                transaction.commitAllowingStateLoss();*/
                    /*}
                        } else {
                            if (context != null) {
                                //region error event log
                                Bundle params = new Bundle();
                                params.putString("type", "Server Error");
                                params.putString("source", "User Edit Fragment");
                                params.putString("value", t.getMessage());
                                if (context != null)
                                    vyndGlobal.logEvent("Error_event", params);
                                //endregion
                                EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(2);
                                emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                    @Override
                                    public void onMainButtonClickListener() {
                                        getAllRegions();
                                    }
                                });
                                FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                transaction.add(emptyDataDialog, "loading");
                                transaction.commitAllowingStateLoss();
                            }

                        }*/

                    }
                }

            });
        }

        return true;

    }

    public void showCategoryListDialog(final Context context, List<Classification> classificationList, HashMap<Integer, Classification> selectedSubCategoryMap, List<Classification> subCategorieDesfault) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final AlertDialog.Builder mainBuilder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyleSuggest);
        final View custom_dialog = inflater.inflate(
                R.layout.dialog_categories, null, false);

        final AlertDialog alertDialog = mainBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvConfirm, tvRetour, tvNbrCat, labelTtCategories;
        RecyclerView rvCategoryList;

        alertDialog.setView(custom_dialog);

        tvRetour = custom_dialog.findViewById(R.id.tv_retour);
        labelTtCategories = custom_dialog.findViewById(R.id.label_tt_categories);
        tvRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        tvConfirm = custom_dialog.findViewById(R.id.tv_confirm);

        rvCategoryList = custom_dialog.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context));
        rvCategoryList.setHasFixedSize(true);
        System.out.println(classificationList.size());
        System.out.println(subCategorieDesfault);
        final CategoriesRecyclerAdapter selectedCategoriesRecyclerAdapter = new CategoriesRecyclerAdapter(context, classificationList, selectedSubCategoryMap,subCategorieDesfault);
        rvCategoryList.setAdapter(selectedCategoriesRecyclerAdapter);
        labelTtCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedCategoriesRecyclerAdapter.resetSelectedSubCategories();
                onConfirmSubCategoryConfirmListener.OnConfirmSubCategoryConfirmListener(selectedCategoriesRecyclerAdapter.getSelectedSubCategoryMap());
                alertDialog.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onConfirmSubCategoryConfirmListener.OnConfirmSubCategoryConfirmListener(selectedCategoriesRecyclerAdapter.getSelectedSubCategoryMap());
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }

    public void showRegionListDialog(final Context context) {
        getAllRegions(context);


        final LayoutInflater inflater = LayoutInflater.from(context);
        final AlertDialog.Builder mainBuilder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyleSuggest);
        final View custom_dialog = inflater.inflate(
                R.layout.dialog_region, null, false);

        final AlertDialog alertDialog = mainBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView tvConfirm, tvTunis, tvRetour;
        RecyclerView rvCategoryList;

        alertDialog.setView(custom_dialog);


        tvConfirm = custom_dialog.findViewById(R.id.tv_confirm);
        tvTunis = custom_dialog.findViewById(R.id.label_tunis);
        etRegion = custom_dialog.findViewById(R.id.tv_region_place);
        tvRetour = custom_dialog.findViewById(R.id.tv_retour);
        tvRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        tvTunis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmRegionSelectListener.OnConfirmRegionSelectListener(new Region(0, context.getString(R.string.label_grand_tunis)));
                alertDialog.dismiss();
            }
        });
        rvCategoryList = custom_dialog.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context));
        rvCategoryList.setHasFixedSize(true);
        rvRegionAutoComplete = custom_dialog.findViewById(R.id.rv_region_autocomplete);
        rvRegionAutoComplete.setLayoutManager(new LinearLayoutManager(context));
        rvRegionAutoComplete.setHasFixedSize(false);
        rvRegionAutoComplete.setNestedScrollingEnabled(false);
        cardRegionAutocomplete = custom_dialog.findViewById(R.id.card_region_autocomplete);
        etRegion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    regionRecyclerAdapter = new RegionRecyclerAdapter(context, DialogFactoryUtils.this.regions);
                    rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);

                } else {
                    isCardAutoVisible = false;
                    cardRegionAutocomplete.setVisibility(View.GONE);
                }
            }
        });


        etRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (DialogFactoryUtils.this.regions != null && DialogFactoryUtils.this.regions.size() != 0) {
                    if (s.toString().length() == 0) {
                        regionRecyclerAdapter = new RegionRecyclerAdapter(context, DialogFactoryUtils.this.regions);
                        rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);
                        cardRegionAutocomplete.setVisibility(View.GONE);


                    } else {

                        displayedRegions = new ArrayList<Region>();
                        try {


                            for (int i = 0; i < DialogFactoryUtils.this.regions.size(); i++) {
                                if (DialogFactoryUtils.this.regions.get(i).getLabel().toLowerCase().contains(s.toString().toLowerCase())) {
                                    displayedRegions.add(DialogFactoryUtils.this.regions.get(i));
                                }
                            }
                        }catch (NullPointerException e)
                        {

                        }
                        if (displayedRegions.size() == 0) {
                            cardRegionAutocomplete.setVisibility(View.GONE);
                        } else {
                            cardRegionAutocomplete.setVisibility(View.VISIBLE);
                            regionRecyclerAdapter = new RegionRecyclerAdapter(context, displayedRegions);
                            rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);
                        }
                    }

                }
            }

        });

        final RegionRecyclerAdapter regionRecyclerAdapter = new RegionRecyclerAdapter(context, regions);
        rvCategoryList.setAdapter(regionRecyclerAdapter);



        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("6");
                if (onConfirmRegionSelectListener != null)
                    try {
                        onConfirmRegionSelectListener.OnConfirmRegionSelectListener(selectedRegion);
                    } catch (NullPointerException e) {

                    }
                alertDialog.dismiss();
            }
        });


        RegionRecyclerAdapter.setOnTagClickListener(new RegionRecyclerAdapter.OnTagItemClickListener() {
            @Override
            public void onTagItemClickListener(Region region) {
                if (onConfirmRegionSelectListener != null)
                    try {
                        onConfirmRegionSelectListener.OnConfirmRegionSelectListener(region);
                    } catch (NullPointerException e) {

                    }
                alertDialog.dismiss();
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCardAutoVisible) {
                        cardRegionAutocomplete.setVisibility(View.GONE);
                        isCardAutoVisible = false;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        });

        alertDialog.show();

    }
    public void setOnConfirmSubCategoryConfirmListener(OnConfirmSubCategoryConfirmListener onConfirmSubCategoryConfirmListener) {
        this.onConfirmSubCategoryConfirmListener = onConfirmSubCategoryConfirmListener;
    }

    public void setOnConfirmRegionSelectListener(OnConfirmRegionSelect onConfirmRegionSelectListener) {
        this.onConfirmRegionSelectListener = onConfirmRegionSelectListener;
    }



    public interface OnConfirmSubCategoryConfirmListener {
        void OnConfirmSubCategoryConfirmListener(HashMap<Integer, Classification> selectedSubCategoryMap);
    }

    public interface OnConfirmSpecialityConfirmListener {
        void OnConfirmClickListener(HashMap<Integer, ValidInfo> specialities);
    }
    public interface OnConfirmRegionSelect {
        void OnConfirmRegionSelectListener(Region region);
    }

}
