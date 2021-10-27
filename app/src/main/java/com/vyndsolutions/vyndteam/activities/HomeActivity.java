package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.BusinessAdapter;
import com.vyndsolutions.vyndteam.adapters.BusinessApproximiteAdapter;
import com.vyndsolutions.vyndteam.adapters.info.RegionRecyclerAdapter;
import com.vyndsolutions.vyndteam.adapters.search.RegionRecyclerAdapterAutoComplete;
import com.vyndsolutions.vyndteam.adapters.search.SearchAutocompleteRecyclerViewAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.Region;
import com.vyndsolutions.vyndteam.models.SearchObject;
import com.vyndsolutions.vyndteam.models.search.SearchAutoComplete;
import com.vyndsolutions.vyndteam.utils.CompteManagerService;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.MyLocationManager;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.EmptyDataLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vyndsolutions.vyndteam.utils.ConstSharedPreferences.TYPE;


public class HomeActivity extends AppCompatActivity {

    private String keywordToset = "";

    private Region regionToSet;
    private SearchAutoComplete searchAutoCompleteToSet;
    public final static int MY_PERMISSIONS_REQUEST_LOCALISATION = 128;
    private static final int UPDATE_INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;
    public static SearchObject searchObject = new SearchObject("", null);
    public static boolean aProximite = false;
    private final int SEARCH_RESULT_LIMIT = 20;
    //    private MainViewPagerAdapter mainViewPagerAdapter;
    RecyclerView list, listApproximite;
    RelativeLayout layoutAllCommerce;
    Switch switchCompat;
    ImageView add_button, iv_delete, ivSearchIcon, ivMenu;
    Location currentLocation;
    LinearLayout searchLayout;
    TextView textToast;
    int backpress = 0;
    ArrayList<String> regionListAnalytics, categorieListAnalytics;
    Handler handler;
    Runnable runnable;
    int displayedFragment = 0;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView mapLink, keyword1, keyword2;
    private boolean isErrorDisplayed = false;
    private GoogleApiClient mGoogleApiClient;
    private Location location;
    //check permissions
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private RecyclerView rvSortirNearby, rvSortir, rvNearbySuggestion;
    private LocationManager locationManager;
    private TextView tvHeaderUserNbPoints, tvHeaderUserNbReviews, tvNbFollowers, labelConnexion, tvSearchBar;
    private View layoutHeaderUser, layoutProximite, layoutSearchTitle, layoutNoRegions, layoutRegionAutocompleteResult, layoutSearchAutocompleteResult, layoutSearchAutocomplete, layoutHeaderUserDescription, progressBar, layoutUserInfo, layoutMainView, ivSearchBack, layoutLoadingRegionAutocomplete, layoutLoadingSearchAutocomplete;
    //    private Toolbar toolbar;
    private boolean isUserCheckedIn;
    private ProgressDialog progress;
    private FragmentManager myFragmentManager;
    private EditText etSearchAutocomplete, etRegionSearchAutocomplete;
    private CardView cardSearch;
    private RecyclerView rvRegionList, rvSearchAutocomplete, rvSearchResult, rvCategoryList;

    private JsonObject postParams = new JsonObject();
    private List<Classification> selectedSearchTags = new ArrayList<>(), subcategories = new ArrayList<>(), selectedSearchTagsToSend = new ArrayList<>();
    private HashMap<Integer, Classification> selectedSubCategoryMap = new HashMap<>();
    private List<Region> selectedRegions = new ArrayList<>(), regions = new ArrayList<>(), displayedRegions;
    private Region proximite, grandTunis;
    private ImageView ivCancelSearchAutocomplete, ivCancelRegionAutocomplete;
    private String libelleSearch;
    private boolean isAutoComplete;
    private ArrayList<String> searchHashTags;
    private Call<ResponseBody> call;
    private View layoutAutocompleteNoResults, ivSearchCancel;
    private String keyword = "";
    private TextView tvSearchTitle;
    //    private SwipeRefreshLayout swipeRefresh;
    private BusinessAdapter businessAdapter;
    private RegionRecyclerAdapterAutoComplete regionRecyclerAdapter;
    private SearchAutocompleteRecyclerViewAdapter searchBusinessAutocompleteRecyclerViewAdapter;
    private Switch switchProximite;
    private View ivFilter;
    private MyLocationManager myLocationManager = new MyLocationManager();
    private EmptyDataLayout emptyDataLayout;
    private TextView tvKeyword;
    private View layoutRetry;

    public static boolean hasPermissions(Context context, String... permissions) {

        for (String permission : permissions) {
            if (context != null)
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((AppCompatActivity) context, permissions, MY_PERMISSIONS_REQUEST_LOCALISATION);
                    return false;
                }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesFactory.initializeShared(this);
        proximite = new Region(-2, getString(R.string.near_you), 0, 0);
        grandTunis = new Region(-1, getString(R.string.label_grand_tunis), 0, 0);

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        myLocationManager.activateGps(this);
        initialiseViews();
        initializeInputs();
        setUpOnClickListeners();

        if (ConnectivityService.isOnline(HomeActivity.this)) {

            loadData();
            updateHomeView();
//            initializeCategoryView();

            if (ConnectivityService.displayGpsStatus(locationManager)) {

                updateLocation(true);
            }
        } else {
            Toast.makeText(this, "Vérifier votre connection internet et réssayer", Toast.LENGTH_LONG).show();

        }


    }

    private void updateLocation(boolean hasLocation) {
        try {
            if (hasLocation)
                myLocationManager.getLocation(HomeActivity.this, new MyLocationManager.LocationResult() {
                    @Override
                    public void gotLocation(Location locationresult) {
                        if (locationresult != null) {
                            location = locationresult;
                        }
                    }
                });
            else {
                if (businessAdapter != null)
                    businessAdapter.clear();

                progressBar.setVisibility(View.VISIBLE);
                myLocationManager.getLocation(HomeActivity.this, new MyLocationManager.LocationResult() {
                    @Override
                    public void gotLocation(Location locationresult) {
                        if (locationresult != null) {
                            location = locationresult;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectedRegions = new ArrayList<>();
                                    selectedRegions.add(proximite);
                                    updateHomeView();
                                }
                            });

                        }
                    }
                });
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateHomeView() {
        if (ConnectivityService.isOnline(HomeActivity.this)) {

            getAllSubCategories();
            getAllRegions();

            if (!ConnectivityService.displayGpsStatus(locationManager)) {
                location = null;
//                layoutProximite.setCardBackgroundColor(Color.parseColor(getString(R.color.colorPrimary)));
                switchProximite.setChecked(false);
                selectedRegions.add(grandTunis);

            } else {


//                selectedRegions.add(proximite);
            }
            prepareDataForSearch();
        }
    }

    private void loadData() {
        if (ConnectivityService.isOnline(HomeActivity.this)) {

            getAllSubCategories();
            getAllRegions();


            if (!ConnectivityService.displayGpsStatus(locationManager)) {
                location = null;
//                layoutProximite.setCardBackgroundColor(Color.parseColor(getString(R.color.colorPrimary)));
                switchProximite.setChecked(false);
                selectedRegions.add(grandTunis);

            } else {


//                selectedRegions.add(proximite);

            }
        }
    }

    private void setUpOnClickListeners() {

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupMenu popupMenu = new PopupMenu(HomeActivity.this, ivMenu);
                Context wrapper = new ContextThemeWrapper(HomeActivity.this, R.style.popup);
                PopupMenu popupMenu = new PopupMenu(wrapper, ivMenu);

                popupMenu.getMenuInflater().inflate(R.menu.add_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.deconnexion))) {
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            SharedPreferencesFactory.pref.edit().remove("token").apply();
                            SharedPreferencesFactory.pref.edit().remove("userSession").apply();
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, AddBusinessActivity.class);
                            Utils.isAdded = true;
                            if (searchLayout.getVisibility() == View.VISIBLE) {
                                finish();
                            }
                            Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);
                            startActivity(intent);
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });


        layoutProximite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRegions.contains(proximite)) {
                    selectedRegions = new ArrayList<Region>();
//                    layoutProximite.setCardBackgroundColor(Color.parseColor(getString(R.color.colorPrimary)));
                    switchProximite.setChecked(false);
                    prepareDataForSearch();
                } else {

                    if (hasPermissions(HomeActivity.this, PERMISSIONS)) {
                        if (ConnectivityService.displayGpsStatus(locationManager)) {

                            selectedRegions = new ArrayList<Region>();
                            selectedRegions.add(proximite);
                            switchProximite.setChecked(true);

                            if (location != null && location.getLatitude() != 0d) {
                                prepareDataForSearch();
                            } else {
                                updateLocation(false);
                            }

                        } else {
                            myLocationManager.activateGps(HomeActivity.this);

                        }
                    } else {
                        myLocationManager.activateGps(HomeActivity.this);

                    }
//                        layoutProximite.setCardBackgroundColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                }
            }
        });
    }

    private void browseNearbyBusinessList(final Location currentLocation) {


        list.setVisibility(View.GONE);
        listApproximite.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);


        JsonObject postParams = new JsonObject();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", 0);

        jsonObject.addProperty("label", "");
        jsonObject.addProperty(TYPE, 0);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);
        postParams.add("tags", jsonArray);
        postParams.addProperty("latitude", currentLocation.getLatitude());
        postParams.addProperty("longitude", currentLocation.getLongitude());
        postParams.addProperty("distance", 1);
        postParams.addProperty("open", 0);
        postParams.addProperty("coupon", 0);
        postParams.addProperty("budget", 0);
        postParams.addProperty("searchId", 0);
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().getDefaultBusinesses(postParams, 0, Utils.LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                listApproximite.setVisibility(View.VISIBLE);
                list.setAdapter(null);
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.code() != 200) {

                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject jObject = new JSONObject(response.body().string());
                        JSONArray jArray = jObject.getJSONArray("items");
                        //Type type = new TypeToken<ArrayList<Business>>(){}.getType();
                        List<Business> businessListApproximite = new ArrayList<Business>();
                        for (int i = 0; i < jArray.length(); i++) {
                            Business business = gson.fromJson(jArray.getJSONObject(i).toString() + "", Business.class);
                            businessListApproximite.add(business);

                        }
                        //List<Business> businessesList = gson.fromJson(jArray.toString(), type);

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                        listApproximite.setLayoutManager(mLayoutManager);
                        final BusinessApproximiteAdapter businessApproximiteAdapter = new BusinessApproximiteAdapter(HomeActivity.this, businessListApproximite, currentLocation);
                        listApproximite.setAdapter(businessApproximiteAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("  erreur connecxion", "error");

            }
        });
    }

    private void initialiseViews() {


        ivMenu = findViewById(R.id.iv_menu);
        textToast = (TextView) findViewById(R.id.text_toast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        list = findViewById(R.id.list);
        keyword1 = findViewById(R.id.keyword1);
        keyword2 = findViewById(R.id.keyword2);
        ivSearchIcon = findViewById(R.id.iv_search_icon);
        searchLayout = findViewById(R.id.search_layout);
        tvKeyword = findViewById(R.id.tv_keyword);
        layoutAllCommerce = findViewById(R.id.layout_all_commerce);

        switchProximite = findViewById(R.id.switch_proximite);


        layoutLoadingRegionAutocomplete = findViewById(R.id.layout_loading_region_autocomplete);
        layoutLoadingSearchAutocomplete = findViewById(R.id.layout_loading_search_autocomplete);

        layoutAutocompleteNoResults = findViewById(R.id.layout_autocomplete_no_results);


        layoutMainView = findViewById(R.id.layout_main_view);
        ivSearchBack = findViewById(R.id.iv_search_back);


        etSearchAutocomplete = findViewById(R.id.et_search_autocomplete);
        etRegionSearchAutocomplete = findViewById(R.id.et_region_search_autocomplete);
        ivCancelSearchAutocomplete = findViewById(R.id.iv_cancel_search_autocomplete);
        ivCancelRegionAutocomplete = findViewById(R.id.iv_cancel_region_autocomplete);


        //region autocomplete selectedRegions
        layoutRegionAutocompleteResult = findViewById(R.id.layout_region_autocomplete_result);
        layoutSearchAutocompleteResult = findViewById(R.id.layout_search_autocomplete_result);
        rvRegionList = findViewById(R.id.rv_region_list);
        rvRegionList.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        rvRegionList.setHasFixedSize(false);
        rvRegionList.setNestedScrollingEnabled(false);
        layoutNoRegions = findViewById(R.id.layout_no_regions);


        //region autcomplete search
        layoutSearchAutocomplete = findViewById(R.id.layout_search_autocomplete);

        rvSearchAutocomplete = findViewById(R.id.rv_search_autocomplete);
        rvSearchAutocomplete.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        rvSearchAutocomplete.setHasFixedSize(false);
        rvSearchAutocomplete.setNestedScrollingEnabled(false);


        rvSearchResult = findViewById(R.id.rv_search_result);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        rvSearchResult.setHasFixedSize(false);
        rvSearchResult.setNestedScrollingEnabled(false);


        emptyDataLayout = findViewById(R.id.empty_data_layout);
        emptyDataLayout.setContext(HomeActivity.this);
        emptyDataLayout.getEmptyToolbar().setVisibility(View.GONE);
        emptyDataLayout.getMainToolbar().setVisibility(View.GONE);
        emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
            @Override
            public void onRetryListener() {
                Intent intent = new Intent(HomeActivity.this, AddBusinessActivity.class);
                startActivity(intent);
            }
        });


        selectedRegions = new ArrayList<>();
        /*filtreLayout = (FiltreLayout) rootView.findViewById(R.id.filtre_layout);
        filtreLayout.setSubmitOnClickListener(new FiltreLayout.SubmitOnClickListener() {
            @Override
            public void OnSubmitClickListener(FiltreEntity filtreEntity) {
                HomeFragmentV3.this.filtreEntity = filtreEntity;
                prepareDataForSearch();
            }
        });
        filtreLayout.setFilterStateListener(this);
        filtreLayout.setmContext(HomeActivity.this);*/

        layoutProximite = findViewById(R.id.layout_proximite);
    }

    private void initializeInputs() {


        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                layoutMainView.setVisibility(View.GONE);
                layoutSearchAutocomplete.setVisibility(View.VISIBLE);
                layoutSearchAutocomplete.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                etSearchAutocomplete.requestFocus();
                if (regions.get(0).equals(proximite)) {

                } else regions.add(0, proximite);
                regionRecyclerAdapter = new RegionRecyclerAdapterAutoComplete(HomeActivity.this, regions);
                rvRegionList.setAdapter(regionRecyclerAdapter);
                setUpOnRegionClick();


                InputMethodManager imm = (InputMethodManager) HomeActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearchAutocomplete, InputMethodManager.SHOW_IMPLICIT);
                triggerSuggestionAutoComplete();

            }
        });


        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvKeyword.setText(" ");
                layoutSearchAutocomplete.setVisibility(View.GONE);
                layoutMainView.setVisibility(View.VISIBLE);
                Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);
                etSearchAutocomplete.setText("");
                tvKeyword.setText("Tous les commerces");
            }
        });

        ivCancelRegionAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRegionSearchAutocomplete.setText("");
                selectedRegions = new ArrayList<Region>();
            }
        });

        ivCancelSearchAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchAutocomplete.setText("");
            }
        });


        etRegionSearchAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (regions != null && regions.size() != 0) {
                    if (s.toString().length() == 0) {
                        regionRecyclerAdapter = new RegionRecyclerAdapterAutoComplete(HomeActivity.this, regions);
                        rvRegionList.setAdapter(regionRecyclerAdapter);
                        ivCancelRegionAutocomplete.setVisibility(View.GONE);
                    } else {
                        ivCancelRegionAutocomplete.setVisibility(View.VISIBLE);
                        displayedRegions = new ArrayList<Region>();
                        displayedRegions.add(regions.get(0));
                        for (int i = 1; i < regions.size(); i++) {
                            if (regions.get(i).getLabel().toLowerCase().contains(s.toString().toLowerCase())) {
                                displayedRegions.add(regions.get(i));
                            }
                        }
                        regionRecyclerAdapter = new RegionRecyclerAdapterAutoComplete(HomeActivity.this, displayedRegions);
                        rvRegionList.setAdapter(regionRecyclerAdapter);
                    }
                    setUpOnRegionClick();
                }
            }
        });

        //region autocomplete search
        etSearchAutocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                layoutLoadingSearchAutocomplete.setVisibility(View.VISIBLE);
                if (ConnectivityService.isOnline(HomeActivity.this)) {

                    libelleSearch = editable.toString();
                    /*if (!isAutoComplete && libelleSearch.length() != 1) {
                        isAutoComplete = true;
                        return;
                    }*/

                    //region tag search #fater ancien
/*
                    if (libelleSearch.length() > 0 && libelleSearch.charAt(0) == '#') {
                        Log.d("layoutVisibility", "onCreateView: 5");
                        searchHashTags = new ArrayList<String>();
                        searchHashTags.add("#match");
                        HashTagsRecyclerAdapter searchTagsRecyclerAdapter = new HashTagsRecyclerAdapter(HomeActivity.this, searchHashTags);
                        searchTagsRecyclerAdapter.setOnTagClickListener(new HashTagsRecyclerAdapter.OnFollowClickListener() {
                            @Override
                            public void onTagItemClickListener(String hashTag) {
                                libelleSearch = hashTag;
                                searchEditText.setText(libelleSearch);
                                searchEditText.setSelection(libelleSearch.length());
                                setSuggestionslayoutVisibility(false, false, true);
                                setBusinessInputFocusability(false);
                                Utils.hideKeyboard(searchEditText);
                                Log.d("triggerSuggestionSearch", "onCreateView: 9");
                                triggerSearch();
                            }
                        });
                        autocompleteLoading.setVisibility(View.GONE);
                        listBusinessAutocomplete.setAdapter(searchTagsRecyclerAdapter);
                        listBusinessAutocomplete.setVisibility(View.VISIBLE);
                        return;
                    }*/
                    //endregion  #fater

                    List<Classification> tagsAutoCompleteResult;
                    if (regions == null || subcategories == null) {
                        getAllSubCategories();
                        getAllRegions();
                        triggerSuggestionAutoComplete();
                    } else {


                        if (call != null) {
                            call.cancel();
                        }
                    }
                    //trigger search when no text in input
                    if (editable.length() == 0) {
                        ivCancelSearchAutocomplete.setVisibility(View.GONE);
                        triggerSuggestionAutoComplete();
//                        Toast.makeText(HomeActivity.this,libelleSearch.length()+" "+libelleSearch+" wtf",Toast.LENGTH_SHORT).show();
                    } else {
                        ivCancelSearchAutocomplete.setVisibility(View.VISIBLE);
                        rvSearchAutocomplete.setAdapter(null);
                        layoutLoadingSearchAutocomplete.setVisibility(View.VISIBLE);
                        String keyword = editable.toString();
//                    Toast.makeText(HomeActivity.this,libelleSearch.length()+" "+libelleSearch+" wtf",Toast.LENGTH_SHORT).show();

                        //region tags autocomplete


                        call = RetrofitServiceFacotry.getBusinessService().autoCompleteSearch(keyword);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {

                                    Log.d("autocomplete", response.body().toString());
                                    Log.d("autocomplete", "" + response.isSuccessful());
                                    Log.d("autocomplete", "" + response.body());


                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    int code = response.code();
                                    if (code == 200) {
                                        Gson gson = Utils.getGsonInstance();
                                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                                        ArrayList<SearchAutoComplete> searchAutoCompletes = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            SearchAutoComplete searchAutoComplete = gson.fromJson(jsonArray.get(i).toString(), SearchAutoComplete.class);
//                                            business.random = (int) (Math.random() * 3);
                                            searchAutoCompletes.add(searchAutoComplete);
                                        }
                                        if (searchAutoCompletes.size() == 0) {
                                            searchBusinessAutocompleteRecyclerViewAdapter = new SearchAutocompleteRecyclerViewAdapter(HomeActivity.this, searchAutoCompletes);
                                            rvSearchAutocomplete.setAdapter(searchBusinessAutocompleteRecyclerViewAdapter);
                                            layoutLoadingSearchAutocomplete.setVisibility(View.GONE);
                                            layoutAutocompleteNoResults.setVisibility(View.VISIBLE);

                                        } else {
                                            layoutAutocompleteNoResults.setVisibility(View.GONE);
                                            searchBusinessAutocompleteRecyclerViewAdapter = new SearchAutocompleteRecyclerViewAdapter(HomeActivity.this, searchAutoCompletes);
                                            rvSearchAutocomplete.setAdapter(searchBusinessAutocompleteRecyclerViewAdapter);
                                            layoutLoadingSearchAutocomplete.setVisibility(View.GONE);

                                        }
                                        setUpSearchResultClick();
                                    }
                                } catch (JSONException | IOException | NullPointerException e) {
                                    if (HomeActivity.this != null) {
                                        //region error event log
                                        Bundle params = new Bundle();
                                        params.putString("type", "Catch exception");
                                        params.putString("source", "Search Fragment");
                                        params.putString("value", "Autocomplete catch exception : " + e.getMessage());
                                        //endregion
                                        e.printStackTrace();
                                    }
                                }
                                layoutLoadingSearchAutocomplete.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                if (!call.isCanceled()) {
                                    layoutLoadingSearchAutocomplete.setVisibility(View.GONE);

                                    if (!isErrorDisplayed)
                                        if (t instanceof SocketTimeoutException) {
                                            if (HomeActivity.this != null) {
                                                //region error event log
                                                Bundle params = new Bundle();
                                                params.putString("type", "Timeout");
                                                params.putString("source", "Search Fragment");
                                                params.putString("value", t.getMessage());

                                                //endregion
                                            }
                                        } else if (t instanceof UnknownHostException) {
                                            if (HomeActivity.this != null) {
                                                //region error event log
                                                Bundle params = new Bundle();
                                                params.putString("type", "Uknown Host Exception");
                                                params.putString("source", "Search Fragment");
                                                params.putString("value", t.getMessage());

                                                //endregion

                                            }
                                        } else {
                                            if (HomeActivity.this != null) {
                                                //region error event log
                                                Bundle params = new Bundle();
                                                params.putString("type", "Server Error");
                                                params.putString("source", "Search Fragment");
                                                params.putString("value", t.getMessage());

                                                //endregion

                                            }
                                        }
                                    isErrorDisplayed = true;
                                }
                            }
                        });

                    }
                }
            }
        });

        //endregion


        etSearchAutocomplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutRegionAutocompleteResult.setVisibility(View.GONE);
                    layoutSearchAutocompleteResult.setVisibility(View.VISIBLE);
                    if (!etSearchAutocomplete.getText().toString().equals(""))
                        ivCancelSearchAutocomplete.setVisibility(View.VISIBLE);

                } else {

                    ivCancelSearchAutocomplete.setVisibility(View.GONE);
                }
            }

        });

        etRegionSearchAutocomplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutRegionAutocompleteResult.setVisibility(View.VISIBLE);
                    layoutSearchAutocompleteResult.setVisibility(View.GONE);
                    if (!etRegionSearchAutocomplete.getText().toString().equals(""))
                        ivCancelRegionAutocomplete.setVisibility(View.VISIBLE);
                } else {
                    ivCancelRegionAutocomplete.setVisibility(View.GONE);
                }
            }

        });


        etSearchAutocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.GONE);
                    }
                    tvKeyword.setText(" ");
                    if (selectedSearchTags != null && selectedSearchTags.size() != 0 && selectedSearchTags.get(0).getLabel().equals(etSearchAutocomplete.getText().toString())) {
                        keyword = "";
                        selectedSearchTagsToSend = new ArrayList<>(selectedSearchTags);
                    } else
                        keyword = etSearchAutocomplete.getText().toString();
                    if (!etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("À proximité de toi")) {
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                            tvKeyword.setText(etSearchAutocomplete.getText().toString() + " " + etRegionSearchAutocomplete.getText().toString());
                        } else {
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                            tvKeyword.setText(etRegionSearchAutocomplete.getText().toString() + " à " + etSearchAutocomplete.getText().toString());
                        }
                    } else {

                        layoutAllCommerce.setVisibility(View.VISIBLE);
                        tvKeyword.setText(etSearchAutocomplete.getText().toString() + " à Tous les commerces");
                    }
                    //reset catégories when search triggered with keyword
//                    if (!keyword.equals("")) {
                    try {
                        etRegionSearchAutocomplete.setText(etRegionSearchAutocomplete.getText());
                    } catch (NullPointerException e) {

                    }
                    try {
                        etSearchAutocomplete.setText(etSearchAutocomplete.getText());
                    } catch (NullPointerException e) {

                    }
                    selectedSearchTags = new ArrayList<Classification>();
                    selectedSearchTagsToSend = new ArrayList<Classification>();
                    layoutSearchAutocomplete.setVisibility(View.GONE);
                    layoutMainView.setVisibility(View.VISIBLE);
                    Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);
//                        CompteManagerService.saveSearchHistory(HomeActivity.this, new SearchAutoComplete(0, keyword, 3));
//                    }
                    prepareDataForSearch();
                    tvKeyword.setText(" ");

                    if (etSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase(""))
                        layoutAllCommerce.setVisibility(View.GONE);
                    else if (etSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && !etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.VISIBLE);
                        if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("À proximité de toi")) {
                            tvKeyword.setText("Tous les commerces " + etRegionSearchAutocomplete.getText().toString());
                        } else
                            tvKeyword.setText("Tous les commerces à " + etRegionSearchAutocomplete.getText().toString());
                    } else if (!etSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.VISIBLE);
                        tvKeyword.setText(etSearchAutocomplete.getText().toString());
                    } else if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.GONE);
                        tvKeyword.setText("");
                    } else {
                        layoutAllCommerce.setVisibility(View.VISIBLE);
                        tvKeyword.setText(etSearchAutocomplete.getText().toString() + " à " + etRegionSearchAutocomplete.getText().toString());
                    }
                }
                return actionId == EditorInfo.IME_ACTION_SEARCH;

            }
        });

        etRegionSearchAutocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    keyword = etSearchAutocomplete.getText().toString();
                    if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.GONE);
                        tvKeyword.setText("");
                    }

                    //reset catégories when search triggered with keyword
                    if (!keyword.equals("")) {
                        selectedSearchTags = new ArrayList<Classification>();
                        selectedSearchTagsToSend = new ArrayList<Classification>();
//                        CompteManagerService.saveSearchHistory(HomeActivity.this, new SearchAutoComplete(0, keyword, 3));
                    }
                    layoutSearchAutocomplete.setVisibility(View.GONE);
                    layoutMainView.setVisibility(View.VISIBLE);
                    if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                            && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                        layoutAllCommerce.setVisibility(View.GONE);
                        tvKeyword.setText("");
                    }
                    Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);
                    prepareDataForSearch();
                }
                return actionId == EditorInfo.IME_ACTION_SEARCH;
            }
        });
    }

    private void triggerSuggestionAutoComplete() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//        if (!etSearchAutocomplete.getText().toString().equals(""))
//            etSearchAutocomplete.setText("");
        /*List<SearchAutoComplete> searchAutoCompletes
                = CompteManagerService.getSearchHistory(HomeActivity.this);*/
        List<SearchAutoComplete> searchAutoCompletes = null;
        if (searchAutoCompletes == null)
            searchAutoCompletes = new ArrayList<>();
        List<Business> historyBusinesses = new ArrayList<>();
//                CompteManagerService.getLastViewedBusinesses(HomeActivity.this);
        if (historyBusinesses != null && historyBusinesses.size() != 0)
            for (int i = 0; i < historyBusinesses.size(); i++) {
                Business historyBusiness = historyBusinesses.get(i);
                Region historyBusinessRegion = historyBusiness.getRegion();
                String region = "", classification = "";
                if (historyBusinessRegion.getLabel() != null)
                    region = historyBusinessRegion.getLabel();
                if (historyBusiness.getSubCategory() != null && historyBusiness.getSubCategory().length != 0 && historyBusiness.getSubCategory()[0] != null)
                    classification = historyBusiness.getSubCategory()[0].getLabel();
                searchAutoCompletes.add(new SearchAutoComplete(historyBusiness.getId(), historyBusiness.getName(), region, classification, historyBusiness.getImage(), 1));
            }
        if (subcategories != null && subcategories.size() != 0)
            for (int i = 0; i < subcategories.size(); i++) {
                Classification subCategory = subcategories.get(i);
                searchAutoCompletes.add((new SearchAutoComplete(subCategory.getId(), subCategory.getLabel(), "", "", subCategory.getIcon(), 2)));
            }
        searchBusinessAutocompleteRecyclerViewAdapter = new SearchAutocompleteRecyclerViewAdapter(HomeActivity.this, searchAutoCompletes);
        rvSearchAutocomplete.setAdapter(searchBusinessAutocompleteRecyclerViewAdapter);
        rvSearchAutocomplete.getRecycledViewPool().setMaxRecycledViews(0, 0);
        layoutLoadingSearchAutocomplete.setVisibility(View.GONE);
        layoutAutocompleteNoResults.setVisibility(View.GONE);
        setUpSearchResultClick();
    }

    private void setUpSearchResultClick() {
        searchBusinessAutocompleteRecyclerViewAdapter.setOnTagClickListener(new SearchAutocompleteRecyclerViewAdapter.OnTagItemClickListener() {
            @Override
            public void onTagItemClickListener(SearchAutoComplete searchAutoComplete) {

                switch (searchAutoComplete.getType()) {
                    case 2:
                        tvKeyword.setText(" ");
                        keywordToset = searchAutoComplete.getLabel();
                        searchAutoCompleteToSet = searchAutoComplete;
                        etSearchAutocomplete.setText(searchAutoComplete.getLabel());
                        selectedSearchTags = new ArrayList<Classification>();
                        selectedSearchTags.add(new Classification(searchAutoComplete.getId(), searchAutoComplete.getLabel(), 0, null, 2));
                        keyword = "";
                        layoutAllCommerce.setVisibility(View.VISIBLE);
                        if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                            tvKeyword.setText(etSearchAutocomplete.getText().toString());
                        } else if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("À proximité de toi")) {
                            tvKeyword.setText("Tous les commerces " + etRegionSearchAutocomplete.getText().toString());
                        } else {
                            tvKeyword.setText(keywordToset + " à " + etRegionSearchAutocomplete.getText().toString());
                        }
                        /*for (int i = 0; i < subcategories.size(); i++) {
                            if (subcategories.get(i).getId() == searchAutoComplete.getId()) {
                                selectedSubCategoryMap = new HashMap<Integer, Classification>();
                                selectedSubCategoryMap.put(i, subcategories.get(i));
                            }
                        }*/

                       /* layoutSearchAutocomplete.setVisibility(View.GONE);
                        layoutMainView.setVisibility(View.VISIBLE);
                        Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);*/
                        break;
                    case 3:
                        keyword = searchAutoComplete.getLabel();

                        keywordToset = searchAutoComplete.getLabel();
                        //reset catégories when keyword is selected
                        selectedSearchTags = new ArrayList<Classification>();

                        /*layoutSearchAutocomplete.setVisibility(View.GONE);
                        layoutMainView.setVisibility(View.VISIBLE);
                        Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);*/
                        break;
                }
                layoutSearchAutocomplete.setVisibility(View.GONE);
                layoutMainView.setVisibility(View.VISIBLE);
                Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);
                prepareDataForSearch();
            }
        });
    }

    private void setUpOnRegionClick() {
        regionRecyclerAdapter.setOnTagClickListener(new RegionRecyclerAdapterAutoComplete.OnTagItemClickListener() {
            @Override
            public void onTagItemClickListener(Region region) {
                tvKeyword.setText(" ");
                if (region != null) {

                    if (tvKeyword.getText() != null) {
                        if (!etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                            keywordToset = etSearchAutocomplete.getText().toString();
                        }
                    }
                    try {
                        if (keywordToset.equalsIgnoreCase(""))
                            keywordToset = "Tous les commerces";

                        if (region.getLabel().equalsIgnoreCase(proximite.getLabel())) {
                            tvKeyword.setText(keywordToset + " " + region.getLabel());
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                        } else {
                            tvKeyword.setText(keywordToset + " à " + region.getLabel());
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                        }
                        if (region.getLabel().equalsIgnoreCase("") && !keywordToset.equalsIgnoreCase("Tous les commerces")) {
                            tvKeyword.setText(keywordToset + " à " + "Tous les commerces");
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                        }

                        if (keywordToset.equalsIgnoreCase("") && region.getLabel().equalsIgnoreCase("")) {
                            layoutAllCommerce.setVisibility(View.GONE);
                        }
                        if (keywordToset.equalsIgnoreCase("Tous les commerces") && region.getLabel().equalsIgnoreCase("")) {
                            layoutAllCommerce.setVisibility(View.GONE);
                        }


                    } catch (NullPointerException e) {

                    }

                }
                if (!region.equals(proximite)) {

                    selectedRegions = new ArrayList<Region>();
                    selectedRegions.add(region);
                    etRegionSearchAutocomplete.setText(region.getLabel());
                    etSearchAutocomplete.requestFocus();
                    /*prepareDataForSearch();
                    layoutRegionAutocompleteResult.setVisibility(View.GONE);
                    layoutMainView.setVisibility(View.VISIBLE);
                    Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);*/
                } else {
                    if (hasPermissions(HomeActivity.this, PERMISSIONS)) {
                        if (ConnectivityService.displayGpsStatus(locationManager)) {

                            selectedRegions = new ArrayList<Region>();
                            selectedRegions.add(proximite);
                            switchProximite.setChecked(true);
                            etRegionSearchAutocomplete.setText(region.getLabel());
                            etSearchAutocomplete.requestFocus();
                            Utils.hideKeyboard((AppCompatActivity) HomeActivity.this);


                            if (location != null && location.getLatitude() != 0d) {
                                prepareDataForSearch();
                            } else {
                                updateLocation(false);
                            }

                        } else {
                            myLocationManager.activateGps(HomeActivity.this);

                        }
                    } else {
                        myLocationManager.activateGps(HomeActivity.this);

                    }

                }
                if (!etSearchAutocomplete.getText().toString().equals("")) {
                    keyword = etSearchAutocomplete.getText().toString();
                    if (!keyword.equals("")) {
                        selectedSearchTags = new ArrayList<Classification>();
                        selectedSearchTagsToSend = new ArrayList<Classification>();
//                        CompteManagerService.saveSearchHistory(HomeActivity.this, new SearchAutoComplete(0, keyword, 3));
                    }
                    layoutSearchAutocomplete.setVisibility(View.GONE);
                    layoutMainView.setVisibility(View.VISIBLE);
                    Utils.hideKeyboard(HomeActivity.this);
                    regionToSet = region;
                    etRegionSearchAutocomplete.setText(region.getLabel());

                    prepareDataForSearch();
                }
            }
        });
    }

    private boolean getAllRegions() {

        if (CompteManagerService.getAllRegion(HomeActivity.this) != null) {
            regions = CompteManagerService.getAllRegion(this);
        } else if (ConnectivityService.isOnline(HomeActivity.this)) {

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
                            regions = CompteManagerService.getAllRegion(HomeActivity.this);

                            CompteManagerService.saveAllRegion(HomeActivity.this, regions);
                            regions.add(0, grandTunis);
                            regions.add(0, proximite);

                            displayedRegions = new ArrayList<Region>(regions);
                            layoutLoadingRegionAutocomplete.setVisibility(View.GONE);


                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        if (HomeActivity.this != null) {
                            //region error event log
                            Bundle params = new Bundle();
                            params.putString("type", "Catch exception");
                            params.putString("source", "Search Fragment");
                            params.putString("value", "Get All selectedRegions catch exception : " + e.getMessage());
                            //endregion
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (!call.isCanceled()) {
                        if (!isErrorDisplayed)
                            if (t instanceof SocketTimeoutException) {
                                if (HomeActivity.this != null) {
                                    //region error event log
                                    Bundle params = new Bundle();
                                    params.putString("type", "Timeout");
                                    params.putString("source", "Search Fragment");
                                    params.putString("value", t.getMessage());

                                }
                                //endregion
                            } else if (t instanceof UnknownHostException) {
                                if (HomeActivity.this != null) {
                                    //region error event log
                                    Bundle params = new Bundle();
                                    params.putString("type", "Uknown Host Exception");
                                    params.putString("source", "Search Fragment");
                                    params.putString("value", t.getMessage());

                                    //endregion

                                }
                            } else {
                                if (HomeActivity.this != null) {
                                    //region error event log
                                    Bundle params = new Bundle();
                                    params.putString("type", "Server Error");
                                    params.putString("source", "Search Fragment");
                                    params.putString("value", t.getMessage());

                                    //endregion

                                }

                            }
                        isErrorDisplayed = true;
                    }
                }

            });

            return true;
        }
        return false;

    }

    private boolean getAllSubCategories() {
        //ToDo 1 Get categories from SQL lite if not get sub categories from service
        subcategories = new ArrayList<Classification>();
        subcategories = CompteManagerService.getAllSubCategories(this);
        if (subcategories != null) {
            progressBar.setVisibility(View.GONE);
            return true;
        } else {
            Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getClassificationByType(2, 0, 100);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        subcategories = new ArrayList<Classification>();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = response.code();
                        if (code == 200) {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            Gson gson = Utils.getGsonInstance();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                subcategories.add(gson.fromJson(jsonArray.getString(i), Classification.class));
                            }
                            CompteManagerService.saveAllSubCategories(HomeActivity.this, subcategories);


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
        return true;

    }

    private void prepareDataForSearch() {
        JsonArray tags = new JsonArray();
        categorieListAnalytics = new ArrayList<>();
        /*if (selectedSearchTags.size() != 0) {
            tvNbActiveCategories.setText(selectedSubCategoryMap.size() + "");
            tvNbActiveCategories.setVisibility(View.VISIBLE);
            cardCategories.setCardBackgroundColor(Color.parseColor(getString(R.color.transparent_black)));
            tvCardCategory.setTextColor(Color.parseColor(getString(R.color.white)));
        } else {
            tvNbActiveCategories.setVisibility(View.GONE);
            cardCategories.setCardBackgroundColor(Color.TRANSPARENT);
            tvCardCategory.setTextColor(Color.parseColor(getString(R.color.white_disabled)));
        }*/
        try {

            if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                    && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                layoutAllCommerce.setVisibility(View.GONE);
                tvKeyword.setText("");
            } else if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("À proximité de toi")) {
                layoutAllCommerce.setVisibility(View.VISIBLE);
                tvKeyword.setText(etSearchAutocomplete.getText().toString() + " " + etRegionSearchAutocomplete.getText().toString());
            }

        } catch (NullPointerException e) {
            layoutAllCommerce.setVisibility(View.GONE);
            tvKeyword.setText("");
        }

        if (selectedSearchTags != null && selectedSearchTags.size() != 0) {


            for (Classification selectedSearchTag : selectedSearchTags) {
                System.out.println(selectedSearchTag.getLabel());
                JsonObject classification = new JsonObject();
                classification.addProperty("id", selectedSearchTag.getId());
                classification.addProperty("label", selectedSearchTag.getLabel());
                if (selectedSearchTag.getType() == 1)
                    classification.addProperty("type", 1);
                else
                    classification.addProperty("type", 2);
                categorieListAnalytics.add(selectedSearchTag.getLabel());
                tags.add(classification);
            }
        }
        if (selectedRegions != null && selectedRegions.size() != 0) {
            System.out.println("-*-*-*-*-*-");
            JsonArray regionsJsonArray = new JsonArray();
            regionListAnalytics = new ArrayList<String>();
            for (Region r : selectedRegions) {
                if (r.getId() != -1 && r.getId() != -2 && r.getId() != 0) {
                    JsonObject region = new JsonObject();
                    System.out.println(r.getLabel());
                    region.addProperty("id", r.getId());
                    region.addProperty("label", r.getLabel());
                    region.addProperty("type", 3);
                    tags.add(region);
                    regionsJsonArray.add(region);
                    regionListAnalytics.add(r.getLabel());
                }
            }
        }
        if (keyword != null && !keyword.equals("")) {
            JsonObject region = new JsonObject();
            region.addProperty("id", 0);
            region.addProperty("label", keyword);
            region.addProperty("type", 0);
            tags.add(region);


            /*StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD);
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(getString(R.string.result_for) + " ");
            sb.append(keyword);
            sb.setSpan(b, sb.length() - keyword.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (selectedSearchTags != null && selectedSearchTags.size() != 0 && selectedSearchTags.get(0) != null && selectedSearchTags.get(0).getLabel() != null && !selectedSearchTags.get(0).getLabel().equals("")) {
                b = new StyleSpan(android.graphics.Typeface.BOLD);
                sb.append(" " + getString(R.string.on_category) + " ");
                sb.append(selectedSearchTags.get(0).getLabel());
                sb.setSpan(b, sb.length() - selectedSearchTags.get(0).getLabel().length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (selectedRegions != null && selectedRegions.size() != 0 && selectedRegions.get(0) != null && selectedRegions.get(0).getLabel() != null && !selectedRegions.get(0).getLabel().equals("")) {
                b = new StyleSpan(android.graphics.Typeface.BOLD);
                if (selectedRegions.get(0).equals(proximite) || selectedRegions.get(0).equals(grandTunis))
                    sb.append(" " + selectedRegions.get(0).getLabel());
                else {
                    sb.append(" " + getString(R.string.in_region) + " ");
                    sb.append(selectedRegions.get(0).getLabel());
                }
                sb.setSpan(b, sb.length() - selectedRegions.get(0).getLabel().length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }*/

        } else {

        }
        /*postParams.addProperty("distance", filtreEntity.getDistance());
        postParams.addProperty("open", filtreEntity.getOpenNow());
        postParams.addProperty("coupon", filtreEntity.getHasCoupon());
        postParams.addProperty("budget", filtreEntity.getSortBudget());
        postParams.addProperty("popularity", filtreEntity.getSortPopularity());
        postParams.addProperty("note", filtreEntity.getSortRate());*/
        if (switchProximite.isChecked())
            postParams.addProperty("distance", 1);
        else postParams.addProperty("distance", 0);
        postParams.addProperty("open", 0);
//        postParams.addProperty("coupon", filtreEntityV2.getHasCoupon());
//        postParams.addProperty("budget", filtreEntityV2.getSortBudget());
        postParams.addProperty("popularity", 0);
        postParams.addProperty("note", 0);
        postParams.addProperty("hasEvents", 0);
        postParams.addProperty("hasHappyHour", 0);
        postParams.addProperty("hasMenu", 0);
        postParams.addProperty("beerPrice", 0);
        postParams.add("tags", tags);
        System.out.println(postParams);
//        filtreLayout.setFiltreEntity(filtreEntity);
//        filtreLayout.refresh();

        if (!ConnectivityService.isOnline(HomeActivity.this)) {
            hideView();
//            showDialog(2);
            emptyDataLayout.openView(0);

            emptyDataLayout.setVisibility(View.VISIBLE);
            layoutAllCommerce.setVisibility(View.VISIBLE);
            if (etSearchAutocomplete.getText().toString().equalsIgnoreCase(""))
                tvKeyword.setText("Tous les commerces" + " à " +
                        etRegionSearchAutocomplete.getText().toString());
            if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase(""))
                tvKeyword.setText(etSearchAutocomplete.getText().toString());
            if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("À proximité de toi")
                    && etSearchAutocomplete.getText().toString().equalsIgnoreCase(""))
                tvKeyword.setText("Tous les commerces À proximité de toi");
            if (etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                    && etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                layoutAllCommerce.setVisibility(View.GONE);
                tvKeyword.setText("");
            }
            if (!etRegionSearchAutocomplete.getText().toString().equalsIgnoreCase("")
                    && !etSearchAutocomplete.getText().toString().equalsIgnoreCase("")) {
                tvKeyword.setText(etSearchAutocomplete.getText().toString() + " à " + etRegionSearchAutocomplete.getText().toString());
            }


        } else getSearchResult();
    }

    private void hideView() {
        rvSearchResult.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void getSearchResult() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ConnectivityService.isOnline(HomeActivity.this)) {
                    hideView();
//            showDialog(2);
                    emptyDataLayout.openView(0);
                    emptyDataLayout.setVisibility(View.VISIBLE);
                    layoutAllCommerce.setVisibility(View.VISIBLE);
//            swipeRefresh.setRefreshing(false);
                } else {

//            viewPager.setVisibility(View.VISIBLE);
                    if (businessAdapter != null)
                        businessAdapter.clear();

                    progressBar.setVisibility(View.VISIBLE);
                    rvSearchResult.setVisibility(View.VISIBLE);
//            layoutSwitch.setVisibility(View.VISIBLE);
                    emptyDataLayout.setVisibility(View.GONE);

                    if (location != null /*&& ConnectivityService.displayGpsStatus(locationManager)*/ /*&& selectedRegions.contains(proximite)*/) {
                        postParams.addProperty("longitude", location.getLongitude() + "");
                        postParams.addProperty("latitude", location.getLatitude() + "");
                    } else {
                        postParams.addProperty("longitude", "");
                        postParams.addProperty("latitude", "");
                    }


//                    System.out.println("Home Proximity params:" + postParams.toString());


                    call = RetrofitServiceFacotry.getBusinessService().searchBusinessV3(postParams, 0, SEARCH_RESULT_LIMIT);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressBar.setVisibility(View.GONE);
//                    swipeRefresh.setRefreshing(false);
                            Gson gson = Utils.getGsonInstance();
                            List<Business> businesses;
                            int code = response.code();
                            if (code != 200) {
                                hideView();
                                emptyDataLayout.openView(3);
                                emptyDataLayout.setVisibility(View.VISIBLE);
                                layoutAllCommerce.setVisibility(View.VISIBLE);
                                emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                    @Override
                                    public void onRetryListener() {
                                        updateHomeView();
                                    }
                                });
                            } else {

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    final JSONArray jsonArray, jsonArrayAds;

                                    jsonArray = jsonObject.getJSONArray("items");
                                    jsonArrayAds = jsonObject.getJSONArray("ads");

                                    int businessesSize = jsonArray.length();
                                    businesses = new ArrayList<Business>();

                                    //region event log
                                    Bundle params = new Bundle();
                                    params.putString("result_count", jsonObject.getInt("nbrElements") + "");
                                    if (regionListAnalytics != null && regionListAnalytics.size() != 0)
                                        params.putString("selectedRegions", regionListAnalytics.toString());
                                    if (selectedSearchTags != null && selectedSearchTags.size() != 0) {
                                        params.putString("selected_classifications", categorieListAnalytics.toString());
                                    }

                                    //endregion

                                    if (businessesSize == 0) {
                                        hideView();

                                        emptyDataLayout.setVisibility(View.VISIBLE);
                                        layoutAllCommerce.setVisibility(View.VISIBLE);

                                        emptyDataLayout.openView(2);
                                    } else {
                                        if (businessesSize <= 3) {
//                                    if (filtreEntity.getOpenNow() == 0 && filtreEntity.getHasCoupon() == 0)
//                                        addBusiness.setVisibility(View.VISIBLE);
                                        }
                                        /*for (int i = 0; i < jsonArrayAds.length(); i++) {
                                            Business business = gson.fromJson(jsonArrayAds.get(i).toString(), Business.class);
//                                business.random = (int) (Math.random() * 3);
                                            businesses.add(business);
                                        }*/
                                        for (int i = 0; i < businessesSize; i++) {
                                            Business business = gson.fromJson(jsonArray.get(i).toString(), Business.class);
//                                business.random = (int) (Math.random() * 3);
                                            businesses.add(business);
                                        }

                                        postParams.addProperty("searchId", jsonObject.optString("searchId"));
                                        businessAdapter = new BusinessAdapter(HomeActivity.this, businesses, postParams, progressBar);
                                        emptyDataLayout.closeView();
                                        rvSearchResult.setAdapter(businessAdapter);
//                                searchViewPagerAdapter.getMapSearchFragment().setBusinesses(businesses);
//                                resultSearchLayout.setVisibility(View.VISIBLE);


                                    }
                                } catch (JSONException e) {
                                    if (HomeActivity.this != null) {
                                        //region error event log
                                        Bundle params = new Bundle();
                                        params.putString("type", "Catch exception");
                                        params.putString("source", "Search Result Fragment");
                                        params.putString("value", "Search business catch JsonException : " + e.getMessage());

                                        //endregion
                                        e.printStackTrace();
                                        hideView();
                                        emptyDataLayout.setVisibility(View.VISIBLE);
                                        layoutAllCommerce.setVisibility(View.VISIBLE);
                                        emptyDataLayout.openView(3);
                                        emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                            @Override
                                            public void onRetryListener() {
                                                updateHomeView();
                                            }
                                        });
                                    }

                                } catch (IOException e) {
                                    if (HomeActivity.this != null) {
                                        //region error event log
                                        Bundle params = new Bundle();
                                        params.putString("type", "Catch exception");
                                        params.putString("source", "Search Result Fragment");
                                        params.putString("value", "Search business catch IOException : " + e.getMessage());

                                        //endregion
                                        e.printStackTrace();
                                        hideView();
                                        emptyDataLayout.setVisibility(View.VISIBLE);
                                        layoutAllCommerce.setVisibility(View.VISIBLE);
                                        emptyDataLayout.openView(3);
                                        emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                            @Override
                                            public void onRetryListener() {
                                                updateHomeView();
                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            progressBar.setVisibility(View.GONE);
                            if (!call.isCanceled()) {
                                emptyDataLayout.setVisibility(View.VISIBLE);
                                layoutAllCommerce.setVisibility(View.VISIBLE);
                                emptyDataLayout.openView(35);
                                emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                    @Override
                                    public void onRetryListener() {
                                        getSearchResult();
                                    }
                                });
                                hideView();
                            }
                            if (t instanceof SocketTimeoutException) {
                                //region error event log
                                if (HomeActivity.this != null) {
                                    Bundle params = new Bundle();
                                    params.putString("type", "Timeout");
                                    params.putString("source", "Search Result Fragment");
                                    params.putString("value", t.getMessage());

                                    //endregion

                                    /*EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(6);
                                    emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                        @Override
                                        public void onMainButtonClickListener() {
                                            getSearchResult();
                                        }
                                    });
                                    FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                    transaction.add(emptyDataDialog, "loading");
                                    transaction.commitAllowingStateLoss();*/
                                }
                            } else if (t instanceof UnknownHostException) {
                                //region error event log
                                if (HomeActivity.this != null) {
                                    Bundle params = new Bundle();
                                    params.putString("type", "Uknown Host Exception");
                                    params.putString("source", "Search Result Fragment");
                                    params.putString("value", t.getMessage());

                                    //endregion
                                    /*EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(6);
                                    emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                        @Override
                                        public void onMainButtonClickListener() {
                                            getSearchResult();
                                        }
                                    });
                                    FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                    transaction.add(emptyDataDialog, "loading");
                                    transaction.commitAllowingStateLoss();*/
                                }
                            } else {

                                //region error event log
                                if (HomeActivity.this != null) {
                                    Bundle params = new Bundle();
                                    params.putString("type", "Server Error");
                                    params.putString("source", "Search Result Fragment");
                                    params.putString("value", t.getMessage());

                                    //endregion
                           /* EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(2);
                            emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                @Override
                                public void onMainButtonClickListener() {
                                    getSearchResult();
                                }
                            });
                            FragmentTransaction transaction = myFragmentManager.beginTransaction();
                transaction.add(emptyDataDialog, "loading");
                transaction.commitAllowingStateLoss();*/

                                }
                            }

                        }

                    });
                }
            }
        });

    }

    /*@Override
    public void onBackPressed() {


        backpress = (backpress + 1);
       // Toast.makeText(getApplicationContext(), " Cliquez une autre fois pour sortir ", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast));

        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText("Cliquez une autre fois pour sortir !");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        if (backpress > 1) {
            finish();
        }

    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {


        if (layoutSearchAutocomplete.isShown()) {
            layoutSearchAutocomplete.setVisibility(View.GONE);
            layoutMainView.setVisibility(View.VISIBLE);
        } else if (layoutAllCommerce.isShown()) {
            layoutAllCommerce.setVisibility(View.GONE);

            if (ConnectivityService.isOnline(HomeActivity.this)) {

                getAllSubCategories();
                getAllRegions();

                if (!ConnectivityService.displayGpsStatus(locationManager)) {
                    location = null;
//                layoutProximite.setCardBackgroundColor(Color.parseColor(getString(R.color.colorPrimary)));
                    switchProximite.setChecked(false);
                    selectedRegions.add(grandTunis);

                } else {


//                selectedRegions.add(proximite);
                }
                JsonObject jsonObject = new JsonObject();
                JsonArray tags = new JsonArray();
                jsonObject.addProperty("distance", 0);
                jsonObject.addProperty("open", 0);
                jsonObject.addProperty("popularity", 0);
                jsonObject.addProperty("note", 0);
                jsonObject.addProperty("hasEvents", 0);
                jsonObject.addProperty("hasHappyHour", 0);
                jsonObject.addProperty("hasMenu", 0);
                jsonObject.addProperty("beerPrice", 0);
                jsonObject.add("tags", tags);
                sendDate(jsonObject);
            }

        } else finish();
    }

    private void sendDate(final JsonObject postParams) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            rvSearchResult.setVisibility(View.GONE);
        } catch (NullPointerException e) {

        }

        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().searchBusinessV3(postParams, 0, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    progressBar.setVisibility(View.GONE);

                } catch (NullPointerException e) {

                }

//                    swipeRefresh.setRefreshing(false);
                Gson gson = Utils.getGsonInstance();
                List<Business> businesses;
                int code = response.code();
                if (code != 200) {
                    hideView();
                    emptyDataLayout.openView(3);
                    emptyDataLayout.setVisibility(View.VISIBLE);
                    layoutAllCommerce.setVisibility(View.VISIBLE);

                    emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                        @Override
                        public void onRetryListener() {
                            updateHomeView();
                        }
                    });
                } else {
                    rvSearchResult.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        final JSONArray jsonArray, jsonArrayAds;

                        jsonArray = jsonObject.getJSONArray("items");
                        jsonArrayAds = jsonObject.getJSONArray("ads");

                        int businessesSize = jsonArray.length();
                        businesses = new ArrayList<Business>();

                        //region event log
                        Bundle params = new Bundle();
                        params.putString("result_count", jsonObject.getInt("nbrElements") + "");
                        if (regionListAnalytics != null && regionListAnalytics.size() != 0)
                            params.putString("selectedRegions", regionListAnalytics.toString());
                        if (selectedSearchTags != null && selectedSearchTags.size() != 0) {
                            params.putString("selected_classifications", categorieListAnalytics.toString());
                        }

                        //endregion

                        if (businessesSize == 0) {
                            hideView();

                            emptyDataLayout.setVisibility(View.VISIBLE);
                            layoutAllCommerce.setVisibility(View.VISIBLE);

                            emptyDataLayout.openView(2);
                        } else {
                            if (businessesSize <= 3) {
//                                    if (filtreEntity.getOpenNow() == 0 && filtreEntity.getHasCoupon() == 0)
//                                        addBusiness.setVisibility(View.VISIBLE);
                            }
                                        /*for (int i = 0; i < jsonArrayAds.length(); i++) {
                                            Business business = gson.fromJson(jsonArrayAds.get(i).toString(), Business.class);
//                                business.random = (int) (Math.random() * 3);
                                            businesses.add(business);
                                        }*/
                            for (int i = 0; i < businessesSize; i++) {
                                Business business = gson.fromJson(jsonArray.get(i).toString(), Business.class);
//                                business.random = (int) (Math.random() * 3);
                                businesses.add(business);
                            }

                            postParams.addProperty("searchId", jsonObject.optString("searchId"));
                            businessAdapter = new BusinessAdapter(HomeActivity.this, businesses, postParams, progressBar);
                            emptyDataLayout.closeView();
                            rvSearchResult.setAdapter(businessAdapter);
//                                searchViewPagerAdapter.getMapSearchFragment().setBusinesses(businesses);
//                                resultSearchLayout.setVisibility(View.VISIBLE);


                        }
                    } catch (JSONException e) {
                        if (HomeActivity.this != null) {
                            //region error event log
                            Bundle params = new Bundle();
                            params.putString("type", "Catch exception");
                            params.putString("source", "Search Result Fragment");
                            params.putString("value", "Search business catch JsonException : " + e.getMessage());

                            //endregion
                            e.printStackTrace();
                            hideView();
                            emptyDataLayout.setVisibility(View.VISIBLE);
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                            emptyDataLayout.openView(3);
                            emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                @Override
                                public void onRetryListener() {
                                    updateHomeView();
                                }
                            });
                        }

                    } catch (IOException e) {
                        if (HomeActivity.this != null) {
                            //region error event log
                            Bundle params = new Bundle();
                            params.putString("type", "Catch exception");
                            params.putString("source", "Search Result Fragment");
                            params.putString("value", "Search business catch IOException : " + e.getMessage());

                            //endregion
                            e.printStackTrace();
                            hideView();
                            emptyDataLayout.setVisibility(View.VISIBLE);
                            layoutAllCommerce.setVisibility(View.VISIBLE);
                            emptyDataLayout.openView(3);
                            emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                                @Override
                                public void onRetryListener() {
                                    updateHomeView();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    progressBar.setVisibility(View.GONE);
                    rvSearchResult.setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {

                }

                if (!call.isCanceled()) {
                    emptyDataLayout.setVisibility(View.VISIBLE);
                    layoutAllCommerce.setVisibility(View.VISIBLE);
                    emptyDataLayout.openView(35);
                    emptyDataLayout.setOnRetryListener(new EmptyDataLayout.OnRetryListener() {
                        @Override
                        public void onRetryListener() {
                            getSearchResult();
                        }
                    });
                    hideView();
                }
                if (t instanceof SocketTimeoutException) {
                    //region error event log
                    if (HomeActivity.this != null) {
                        Bundle params = new Bundle();
                        params.putString("type", "Timeout");
                        params.putString("source", "Search Result Fragment");
                        params.putString("value", t.getMessage());

                        //endregion

                                    /*EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(6);
                                    emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                        @Override
                                        public void onMainButtonClickListener() {
                                            getSearchResult();
                                        }
                                    });
                                    FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                    transaction.add(emptyDataDialog, "loading");
                                    transaction.commitAllowingStateLoss();*/
                    }
                } else if (t instanceof UnknownHostException) {
                    //region error event log
                    if (HomeActivity.this != null) {
                        Bundle params = new Bundle();
                        params.putString("type", "Uknown Host Exception");
                        params.putString("source", "Search Result Fragment");
                        params.putString("value", t.getMessage());

                        //endregion
                                    /*EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(6);
                                    emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                        @Override
                                        public void onMainButtonClickListener() {
                                            getSearchResult();
                                        }
                                    });
                                    FragmentTransaction transaction = myFragmentManager.beginTransaction();
                                    transaction.add(emptyDataDialog, "loading");
                                    transaction.commitAllowingStateLoss();*/
                    }
                } else {

                    //region error event log
                    if (HomeActivity.this != null) {
                        Bundle params = new Bundle();
                        params.putString("type", "Server Error");
                        params.putString("source", "Search Result Fragment");
                        params.putString("value", t.getMessage());

                        //endregion
                           /* EmptyDataDialog emptyDataDialog = EmptyDataDialog.newInstance(2);
                            emptyDataDialog.setOnMainButtonClickListener(new EmptyDataDialog.OnMainButtonClickListener() {
                                @Override
                                public void onMainButtonClickListener() {
                                    getSearchResult();
                                }
                            });
                            FragmentTransaction transaction = myFragmentManager.beginTransaction();
                transaction.add(emptyDataDialog, "loading");
                transaction.commitAllowingStateLoss();*/

                    }
                }

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}




