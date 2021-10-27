package com.vyndsolutions.vyndteam.activities;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.PlaceAutocompleteAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.PlaceInfo;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.MyLocationManager;
import com.vyndsolutions.vyndteam.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdressActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    Marker marker;
    ProgressBar progressBar;
    Location lastLocationToSend;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    Button rlMyPosition;
    LocationManager locationManager;
    CameraPosition cameraPosition;
    CameraUpdate cameraUpdate;
    public static AutoCompleteTextView commerceAdresse;
    private static LatLngBounds LAT_LNG_BOUNDS;
    private Location currentLocation;
    private Business business = new Business();
    private GoogleMap map;
    private static final float DEFAULT_ZOOM = 15f;
    private PlaceInfo mPlace;
    private ImageView icReturn, icCancel;
    ImageView ivLocationFound;
    RelativeLayout layoutChoseDirectCarte, rlDirectCarte;
    SupportPlaceAutocompleteFragment autocompleteFragment;
    private String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private MyLocationManager myLocationManager = new MyLocationManager();
    BitmapDescriptor bitmapDescriptor;
    Geocoder geocoder;
    List<Address> addresses;
    private String address = "";
    private boolean isFromProfile = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adress);
        initMap();
        initialiseViews();

    }

    private void initMap() {
        if (locationManager == null) {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        commerceAdresse = findViewById(R.id.commerceAdressee);
        autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Recherche sur la carte");
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(16.0f);
        (findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        layoutChoseDirectCarte = findViewById(R.id.layout_chose_direct_carte);

        layoutChoseDirectCarte.setOnClickListener(this);


        if (getIntent() != null) {
            try {
                business = (Business) getIntent().getSerializableExtra("business");
                isFromProfile = getIntent().getBooleanExtra("isFromProfile", false);
                System.out.println(business.getLatitude());
                System.out.println(business.getLongitude());
                System.out.println(business.getDescription());
                System.out.println(business.getTel());

                try {

                    if (!business.getLatitude().equalsIgnoreCase("") && !business.getLongitude().equalsIgnoreCase("")) {
                        layoutChoseDirectCarte.setVisibility(View.GONE);
                        Utils.longitude = Double.parseDouble(business.getLongitude());
                        Utils.latitude = Double.parseDouble(business.getLatitude());
                        //Log.e("vvv", Utils.longitude + "" + Utils.latitude + "");
                       // System.out.println(Utils.longitude+"---"+Utils.latitude);
                        try {
                            if(!business.getAdress().equalsIgnoreCase(""))
                            {
                                Utils.adress=business.getAdress();
                                commerceAdresse.setText(business.getAdress());
                            }
                            else{
                                try {
                                    addresses = geocoder.getFromLocation(Utils.latitude, Utils.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    address = addresses.get(0).getAddressLine(0);
                                    // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    if (business.getName() != "" || business.getName() != null) {

                                    } else {

                                    }
                                    if (business.getAdress() != null && !business.getAdress().equalsIgnoreCase("")) {
                                        Utils.adress = business.getAdress();
                                        commerceAdresse.setText(business.getAdress());
                                    } else {
                                        Utils.adress = address;
                                        commerceAdresse.setText(address);
                                    }


                                } catch (IOException|IndexOutOfBoundsException|NullPointerException e) {
                                    try {
                                        Utils.adress = business.getAdress();
                                        commerceAdresse.setText(business.getAdress());
                                    } catch (NullPointerException f) {

                                    }
                                    e.printStackTrace();
                                }
                            }
                        }catch (NullPointerException e)
                        {

                        }

                        onMapReady(map);


                    } else {

                        layoutChoseDirectCarte.setVisibility(View.VISIBLE);
                   /* Utils.latitude = Utils.latitude;
                    Utils.longitude = Utils.longitude;
                    address = "";
                    autocompleteFragment.setText("");*/
                        commerceAdresse.setText(Utils.adress);
                        onMapReady(map);
                        ivLocationFound.setImageResource(R.drawable.text_warning_icon);


                    }
                } catch (NullPointerException e) {

                }
            } catch (NullPointerException e) {

                layoutChoseDirectCarte.setVisibility(View.VISIBLE);
                   /* Utils.latitude = Utils.latitude;
                    Utils.longitude = Utils.longitude;
                    address = "";
                    autocompleteFragment.setText("");*/
                commerceAdresse.setText(Utils.adress);
                onMapReady(map);
                ivLocationFound.setImageResource(R.drawable.text_warning_icon);
            }


        }


    }


    private void sendBasiqueInfo(Business business) {
        JsonObject postParam = new JsonObject();
        if (!Utils.adress.equalsIgnoreCase("") && Utils.latitude != 0d && Utils.longitude != 0d) {
            if (business != null) {
                if (business.getId() != 0) {
                    postParam.addProperty("name", business.getName());
                    postParam.addProperty("description", business.getDescription().toString());
                    postParam.addProperty("address", Utils.adress);
                    postParam.addProperty("regionId", business.getRegion().getId());
                    postParam.addProperty("latitude", Utils.latitude);
                    postParam.addProperty("longitude", Utils.longitude);
                    postParam.addProperty("tel", business.getTel());
                    postParam.addProperty("postalCode", business.getPostalCode());
                    System.out.println(postParam);
                    Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editBasicBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), postParam);
                    call.clone().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            int code = response.code();


                            if (getBaseContext() != null) {
                                if (code != 200) {



                                } else {


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
            }

        }

    }

    private void initialiseViews() {

        icReturn = findViewById(R.id.ic_return_address);
       /* Intent intent = getIntent();
        business = (Business) intent.getSerializableExtra("business");*/
        ivLocationFound = findViewById(R.id.iv_location_found);
        rlDirectCarte = findViewById(R.id.rl_direct_carte);
        rlMyPosition = findViewById(R.id.rl_my_position);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Utils.latitude = place.getLatLng().latitude;
                Utils.longitude = place.getLatLng().longitude;
                onMapReady(map);
                address = place.getAddress() + "";

                ivLocationFound.setImageResource(R.drawable.check);
                commerceAdresse.setText(address);
                Utils.adress = address;

            }

            @Override
            public void onError(Status status) {

                ivLocationFound.setImageResource(R.drawable.text_warning_icon);


            }
        });
        bitmapDescriptor
                = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        rlDirectCarte.setOnClickListener(this);
        ivLocationFound = findViewById(R.id.iv_location_found);
        rlMyPosition.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        //initializeData();
        commerceAdresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                business.setAdress(charSequence + "");
                if ((charSequence + "").equals("")) {
                    ivLocationFound.setImageResource(R.drawable.text_warning_icon);
                } else {
                    ivLocationFound.setImageResource(R.drawable.check);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        commerceAdresse.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    startPlacePickerActivity();

                    return true;
                }
                return false;
            }
        });

        icReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    if (isFromProfile) {
                        sendBasiqueInfo(business);
                        registerDataAdress();
                        finish();
                    } else {
                        registerDataAdress();

                    }


                } catch (NullPointerException e) {

                }


                //registerDataAdress();

            }
        });

    }

    public static boolean hasPermissions(Context context, int permission_request, String... permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                return false;
            }
        }
        return true;
    }

    private void initializeData() {
        try {
            if (business.getName() != null) {
                if (!business.getName().equalsIgnoreCase("")) {
                    commerceAdresse.setText(business.getName());

                    ivLocationFound.setImageResource(R.drawable.check);
                } else {
                    ivLocationFound.setImageResource(R.drawable.text_warning_icon);
                }
            }
        } catch (NullPointerException e) {
            //  ivLocationFound.setBackground(getContext().getResources().getDrawable(R.drawable.text_warning));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == layoutChoseDirectCarte) {
            layoutChoseDirectCarte.setVisibility(View.GONE);
        }
        if (view == rlDirectCarte) {
            progressBar.setVisibility(View.VISIBLE);
            //WidgetUtils.disableUserInteraction(getActivity());
            map.clear();

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    address = "";
                    Utils.latitude = latLng.latitude;
                    Utils.longitude = latLng.longitude;
                    try {
                        addresses = geocoder.getFromLocation(Utils.latitude, Utils.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        commerceAdresse.setText(address);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (address.equalsIgnoreCase("")) {
                        //  ivLocationFound.setImageResource(R.drawable.text_warning);
                    } else {
                        //  ivLocationFound.setImageResource(R.drawable.tick_green);
                    }

                    if (marker != null)
                        marker.remove();


                    marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptor));


                }
            });
            progressBar.setVisibility(View.GONE);
            //WidgetUtils.enableUserInteraction(getActivity());
        }
        if (view == rlMyPosition) {
            layoutChoseDirectCarte.setVisibility(View.GONE);
            if (hasPermissions(AdressActivity.this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_LOCATION)) {
                if (ConnectivityService.displayGpsStatus(locationManager)) {
                    progressBar.setVisibility(View.VISIBLE);
                    // WidgetUtils.disableUserInteraction(getActivity());
                    myLocationManager.getLocation(getBaseContext(), new MyLocationManager.LocationResult() {
                        @Override
                        public void gotLocation(Location location) {
                            if (location != null) {
                                lastLocationToSend = location;
                                address = "";
                                Utils.latitude = location.getLatitude();
                                Utils.longitude = location.getLongitude();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            addresses = geocoder.getFromLocation(Utils.latitude, Utils.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                            commerceAdresse.setText(address);
                                            Utils.adress = address;

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (address.equalsIgnoreCase("")) {
                                            //   ivLocationFound.setImageResource(R.drawable.text_warning);
                                        } else {
                                            //   ivLocationFound.setImageResource(R.drawable.tick_green);
                                        }
                                        onMapReady(map);
                                        progressBar.setVisibility(View.GONE);
                                        //WidgetUtils.enableUserInteraction(getActivity());

                                    }
                                });

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    //WidgetUtils.enableUserInteraction(getActivity());

                                }
                            });

                        }
                    });
                } else {
                    showGpsDialog();
                }
            }
        }
    }

    private void showGpsDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdressActivity.this);

        View viewAlerte = this.getLayoutInflater().inflate(R.layout.active_gps, null);


        View layoutActiveGps = viewAlerte.findViewById(R.id.rl_active_gps);
        dialogBuilder.setView(viewAlerte);


        dialogBuilder.create();
        final AlertDialog alertDialog = dialogBuilder.show();
        layoutActiveGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                if (alertDialog.isShowing())
                    alertDialog.dismiss();


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (layoutChoseDirectCarte.isShown())
                    layoutChoseDirectCarte.setVisibility(View.GONE);
                address = "";
                Utils.latitude = latLng.latitude;
                Utils.longitude = latLng.longitude;
                try {
                    addresses = geocoder.getFromLocation(Utils.latitude, Utils.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    commerceAdresse.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if ((commerceAdresse.getText() + "").length() == 0) {
                    ivLocationFound.setImageResource(R.drawable.text_warning_icon);
                } else {
                    ivLocationFound.setImageResource(R.drawable.check);
                }

                if (marker != null)
                    marker.remove();
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(bitmapDescriptor));


            }
        });

        LatLng pp = new LatLng(Utils.latitude, Utils.longitude);
        String businessName = "";
        if (business.getName().length() > 3) {
            businessName = business.getName();
        }


        try {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pp)
                    .title(businessName)
                    .icon(bitmapDescriptor);
            if (marker != null)
                marker.remove();
            marker = map.addMarker(markerOptions);
            moveCamera(pp, DEFAULT_ZOOM, "");
            if ((commerceAdresse.getText() + "").length() == 0) {
                ivLocationFound.setImageResource(R.drawable.text_warning_icon);
            } else {
                ivLocationFound.setImageResource(R.drawable.check);
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @OnClick({R.id.layout_my_position, R.id.layout_direct_carte})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_my_position:
                break;
            case R.id.layout_direct_carte:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromProfile) {
            sendBasiqueInfo(business);
            registerDataAdress();
            finish();
        } else {
            registerDataAdress();
        }

    }

    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = intentBuilder.build(AdressActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        Place placeSelected = PlacePicker.getPlace(data, AdressActivity.this);

        String name = placeSelected.getName().toString();
        address = placeSelected.getAddress().toString();
        commerceAdresse.setText(address);
        Utils.longitude = placeSelected.getLatLng().longitude;
        Utils.latitude = placeSelected.getLatLng().latitude;
        onMapReady(map);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }

    private void registerDataAdress() {
        refreshBusinessData();
        Intent intent = new Intent();
        intent.putExtra("businessAddress", business);
        intent.setAction("refresh_state_address");
        sendBroadcast(intent);
        finish();

    }

    private void refreshBusinessData() {

        business.setLongitude(Utils.longitude + "");
        business.setLatitude(Utils.latitude + "");
        business.setAdress(Utils.adress);

    }

}

