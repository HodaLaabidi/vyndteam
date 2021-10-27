package com.vyndsolutions.vyndteam.activities;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.InfoCheckboxRecyclerViewAdapter;
import com.vyndsolutions.vyndteam.adapters.InfoChipViewGoodForAdapter;
import com.vyndsolutions.vyndteam.adapters.InfoChipViewRecommendedForAdapter;
import com.vyndsolutions.vyndteam.adapters.InfoChipViewTagsAdapter;
import com.vyndsolutions.vyndteam.adapters.InfoSwichRecyclerViewAdapter;
import com.vyndsolutions.vyndteam.adapters.RegionAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.GeneralInfo;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.models.ValidInfoChip;
import com.vyndsolutions.vyndteam.models.ValidInfoParent;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationsActivity extends AppCompatActivity {


    private ImageView returnInfo;
    private RecyclerView rvRegion ;
    private RegionAdapter regionAdapter ;



    private GeneralInfo generalInfo;
    private InfoCheckboxRecyclerViewAdapter infoCheckboxRecyclerViewAdapter;
    private InfoSwichRecyclerViewAdapter infoSwichRecyclerViewAdapter;
    private RecyclerView rvImportantInfo;
    Business business = new Business();
    private RecyclerView rvPrincipalInfo;
    private ProgressBar progressBar;

    ChipView cvRecommendedFor, cvGoodFor, cvTags;
    private InfoChipViewRecommendedForAdapter recommendedForChipViewAdapter;
    private InfoChipViewGoodForAdapter goodForChipViewAdapter;
    private InfoChipViewTagsAdapter tagsChipViewAdapter;

    List<ValidInfoParent> principalInfos = new ArrayList<>();
    List<ValidInfoParent> recommendedFors = new ArrayList<>();
    List<ValidInfoParent> goodFors = new ArrayList<>();
    List<Classification> tags = new ArrayList<>();
    private View layoutNoInternetInfo;
    private NestedScrollView nestedInfo;
    private boolean isFromProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        try {
            business = (Business) getIntent().getSerializableExtra("business");
        }catch (NullPointerException e)
        {

        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        cvRecommendedFor = findViewById(R.id.cv_recommended_for);
        cvGoodFor = findViewById(R.id.cv_good_for);
        nestedInfo = findViewById(R.id.nested_info);
        layoutNoInternetInfo = findViewById(R.id.layout_no_internet_info);
        cvTags = findViewById(R.id.cv_tags);
        initialiseView();

        try {
            isFromProfile =  getIntent().getBooleanExtra("isFromProfile",false);
        }catch (NullPointerException e)
        {

        }

        if (!ConnectivityService.isOnline(getBaseContext())) {
            nestedInfo.setVisibility(View.GONE);
            layoutNoInternetInfo.setVisibility(View.VISIBLE);
        } else {
            nestedInfo.setVisibility(View.VISIBLE);
            layoutNoInternetInfo.setVisibility(View.GONE);
            initializeData();
        }
        layoutNoInternetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ConnectivityService.isOnline(getBaseContext())) {
                    nestedInfo.setVisibility(View.GONE);
                    layoutNoInternetInfo.setVisibility(View.VISIBLE);
                } else {
                    nestedInfo.setVisibility(View.VISIBLE);
                    layoutNoInternetInfo.setVisibility(View.GONE);
                    initializeData();
                }
            }
        });


        getGeneralInfo();

        clickReturnIcon();
    }

    private void initializeData() {
        System.out.println("------------***-----------");
        System.out.println(business.getPrincipalInfo().size());
        System.out.println(business.getRecommendedFor().size());
        System.out.println(business.getImportantInfo().size());
        System.out.println(business.getGoodFor().size());
        System.out.println("------------***-----------");
        principalInfos = new ArrayList<>();
        recommendedFors = new ArrayList<>();
        goodFors = new ArrayList<>();
        tags = new ArrayList<>();
        try {
            principalInfos = business.getPrincipalInfo();
        } catch (NullPointerException e) {
            principalInfos = new ArrayList<>();
        }
        try {
            recommendedFors = business.getRecommendedFor();
        } catch (NullPointerException e) {
            recommendedFors = new ArrayList<>();
        }

        try {
            goodFors = business.getGoodFor();
        } catch (NullPointerException e) {
            goodFors = new ArrayList<>();
        }

        try {
            tags = Arrays.asList(business.getTags());
        } catch (NullPointerException e) {
            tags = new ArrayList<>();
        }
        getGeneralInfo();

    }

    private void initialiseView() {
       rvRegion = findViewById(R.id.list_region);
       returnInfo = findViewById(R.id.return_info);
       cvRecommendedFor = findViewById(R.id.cv_recommended_for);
       cvGoodFor = findViewById(R.id.cv_good_for);
       rvImportantInfo = findViewById(R.id.rv_important_info);
       rvPrincipalInfo = findViewById(R.id.rv_principal_info);


    }


    private void getGeneralInfo() {

        Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getGeneralInfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = response.code();
                    if (code == 200) {

                        Gson gson = Utils.getGsonInstance();
                        generalInfo = gson.fromJson(jsonObject.toString(), GeneralInfo.class);


                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                        rvImportantInfo.setLayoutManager(layoutManager);

                        infoCheckboxRecyclerViewAdapter = new InfoCheckboxRecyclerViewAdapter(getBaseContext(), generalInfo.getImportantInfo(), business.getImportantInfo());

                        rvImportantInfo.setAdapter(infoCheckboxRecyclerViewAdapter);
                        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                        rvPrincipalInfo.setLayoutManager(layoutManager1);
                        infoSwichRecyclerViewAdapter = new InfoSwichRecyclerViewAdapter(getBaseContext(), generalInfo.getPrincipalInfo(), business.getPrincipalInfo());

                        rvPrincipalInfo.setAdapter(infoSwichRecyclerViewAdapter);
                        initializeRecommendedfor();
                        initializeGoodFor();
                        initializeTags();
                      //  initializeFilterLists();


                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void initializeRecommendedfor() {
        if (generalInfo != null) {
            List<Chip> chipRecommendedForList = new ArrayList<>();
            ValidInfo[] recommendedFors = generalInfo.getRecommendedFor();
            for (ValidInfo v : recommendedFors) {
                chipRecommendedForList.add(new ValidInfoChip(v.getLabel()));
            }
            recommendedForChipViewAdapter = new InfoChipViewRecommendedForAdapter(getBaseContext(), business.getRecommendedFor(), Arrays.asList(generalInfo.getRecommendedFor()));
            recommendedForChipViewAdapter.setChipList(chipRecommendedForList);
            cvRecommendedFor.setAdapter(recommendedForChipViewAdapter);
        }
    }

    private void initializeGoodFor() {
        if (generalInfo != null) {
            List<Chip> chipGoodForList = new ArrayList<>();
            ValidInfo[] goodFors = generalInfo.getGoodFor();
            for (ValidInfo v : goodFors) {
                chipGoodForList.add(new ValidInfoChip(v.getLabel()));
            }
            goodForChipViewAdapter = new InfoChipViewGoodForAdapter(getBaseContext(), business.getGoodFor(), Arrays.asList(generalInfo.getGoodFor()));
            goodForChipViewAdapter.setChipList(chipGoodForList);
            cvGoodFor.setAdapter(goodForChipViewAdapter);
        }
    }
    private void initializeTags() {
        if (generalInfo != null) {
            List<Chip> chipGoodForList = new ArrayList<>();
            Classification[] tags = generalInfo.getTags();
            for (Classification v : tags) {
                chipGoodForList.add(new ValidInfoChip(v.getLabel()));
            }
            tagsChipViewAdapter = new InfoChipViewTagsAdapter(getBaseContext(), business.getTags(), Arrays.asList(generalInfo.getTags()));
            tagsChipViewAdapter.setChipList(chipGoodForList);
            cvTags.setAdapter(tagsChipViewAdapter);
        }
    }
   /* private void getGeneralInfo() {

        if (CompteManagerService.getGeneralInfo(this) != null) {
            generalInfo = CompteManagerService.getGeneralInfo(this);
            initializeFilterLists();
        } else {
            Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getGeneralInfo();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = response.code();
                        if (code == 200) {

                            Gson gson = new Gson();
                            generalInfo = gson.fromJson(jsonObject.toString(), GeneralInfo.class);
                            CompteManagerService.saveGeneralInfo(InformationsActivity.this, generalInfo);
                            initializeFilterLists();


                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }*/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerDataInformations();

    }

    private void registerDataInformations() {

        refreshBusinessData();
        Intent intent = new Intent();
        intent.putExtra("businessInformations", business);
        intent.setAction("refresh_state_informations");
        sendBroadcast(intent);
        finish();

    }

    private void refreshBusinessData() {
        try {
            business.setRecommendedFor(recommendedForChipViewAdapter.getRecommendedFors());

        }catch (NullPointerException e )
        {
            System.out.println("1");
        }
        try {
            business.setGoodFor(goodForChipViewAdapter.getGoodFors());

        }catch (NullPointerException e)
        {
            System.out.println("2");
        }
        try {
            business.setImportantInfo(infoCheckboxRecyclerViewAdapter.getImportantsInfos());

        }catch (NullPointerException e)
        {
            System.out.println("3");
        }

        try {
            business.setPrincipalInfo(infoSwichRecyclerViewAdapter.getPrincipalInfos());

        }catch (NullPointerException e)
        {
            System.out.println("4");
        }
       /* try {
            Classification[] tags = tagsChipViewAdapter.getTagsRet().toArray(new Classification[tagsChipViewAdapter.getTagsRet().size()]);
            business.setTags(tags);
        }catch (NullPointerException e)
        {

        }*/

        if(isFromProfile)
        {
            preapreForEditAddInfo(business);
        }



    }
    private void preapreForEditAddInfo(Business business) {
        JsonArray jsonArray = new JsonArray();
        JsonObject validInfoJson = new JsonObject();
        try {
            if (business.getRecommendedFor().size() != 0) {
                for (int i = 0; i < business.getRecommendedFor().size(); i++) {

                    validInfoJson = new JsonObject();
                    validInfoJson.addProperty("id", business.getRecommendedFor().get(i).getValidInfo().getId());
                    validInfoJson.addProperty("value", true);
                    jsonArray.add(validInfoJson);
                }
            }
        } catch (NullPointerException e) {

        }
        try {
            if (business.getPrincipalInfo().size() != 0) {
                for (int i = 0; i < business.getPrincipalInfo().size(); i++) {
                    validInfoJson = new JsonObject();
                    validInfoJson.addProperty("id", business.getPrincipalInfo().get(i).getValidInfo().getId());
                    validInfoJson.addProperty("value", true);
                    jsonArray.add(validInfoJson);

                }
            }

        } catch (NullPointerException e) {

        }
        try {
            if (business.getImportantInfo().size() != 0) {
                for (int i = 0; i < business.getImportantInfo().size(); i++) {

                    validInfoJson = new JsonObject();
                    validInfoJson.addProperty("id", business.getImportantInfo().get(i).getValidInfo().getId());
                    validInfoJson.addProperty("value", true);
                    jsonArray.add(validInfoJson);
                }
            }

        } catch (NullPointerException e) {

        }
        try {
            if (business.getGoodFor().size() != 0) {
                for (int i = 0; i < business.getGoodFor().size(); i++) {
                    validInfoJson = new JsonObject();
                    validInfoJson.addProperty("id", business.getGoodFor().get(i).getValidInfo().getId());
                    validInfoJson.addProperty("value", true);
                    jsonArray.add(validInfoJson);
                }
            }

        } catch (NullPointerException e) {

        }
        System.out.println(jsonArray);
        sendAddInfo(jsonArray, business);

    }
    private void sendAddInfo(JsonArray jsonArray, Business business) {
        System.out.println(jsonArray);
        System.out.println("-------");
        System.out.println(business.getId());
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addAdditionalInfosToBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), jsonArray);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (getBaseContext() != null) {
                    System.out.println(response.message());
                    if (code != 200) {

                        System.out.println("erreur");

                    } else {

                        System.out.println("good");

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    //
   private void clickReturnIcon() {

      returnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDataInformations();
            }
        });
    }

}
