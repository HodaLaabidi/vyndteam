package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.HoursHoraires;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.models.MenuType;
import com.vyndsolutions.vyndteam.models.Region;
import com.vyndsolutions.vyndteam.utils.CompteManagerService;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.DialogFactoryUtils;
import com.vyndsolutions.vyndteam.utils.TextStyle.LatoEditText;
import com.vyndsolutions.vyndteam.utils.TextStyle.LatoTextView;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerCallback {

    @BindView(R.id.iv_back)
    NetworkImageView ivBack;
    @BindView(R.id.tv_terminer)
    LatoTextView tvTerminer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_business_name)
    LatoTextView lblBusinessName;
    @BindView(R.id.commerceName)
    LatoEditText commerceName;
    @BindView(R.id.ic_name)
    NetworkImageView icName;
    @BindView(R.id.layout_commerce_name)
    RelativeLayout layoutCommerceName;
    @BindView(R.id.lbl_business_region)
    LatoTextView lblBusinessRegion;
    @BindView(R.id.commerceregion)
    LatoTextView commerceregion;
    @BindView(R.id.ic_region)
    NetworkImageView icRegion;
    @BindView(R.id.layout_commerce_region)
    RelativeLayout layoutCommerceRegion;
    @BindView(R.id.lbl_business_categorie)
    LatoTextView lblBusinessCategorie;
    @BindView(R.id.commercecategorie)
    LatoTextView commercecategorie;
    @BindView(R.id.ic_categorie)
    NetworkImageView icCategorie;
    @BindView(R.id.layout_commerce_categorie)
    RelativeLayout layoutCommerceCategorie;
    @BindView(R.id.lbl_business_beerprice)
    LatoTextView lblBusinessBeerprice;
    @BindView(R.id.commercebeerprice)
    LatoEditText commercebeerprice;
    @BindView(R.id.ic_price)
    NetworkImageView icPrice;
    @BindView(R.id.layout_commerce_beerprice)
    RelativeLayout layoutCommerceBeerprice;
    @BindView(R.id.iv_profile)
    NetworkImageView ivProfile;
    @BindView(R.id.lbl_profile_image)
    LatoTextView lblProfileImage;
    @BindView(R.id.ic_profile)
    NetworkImageView icProfile;
    @BindView(R.id.iv_cover)
    NetworkImageView ivCover;
    @BindView(R.id.lbl_cover_image)
    LatoTextView lblCoverImage;
    @BindView(R.id.ic_couverture)
    NetworkImageView icCouverture;
    @BindView(R.id.lbl_location)
    LatoTextView lblLocation;
    @BindView(R.id.ic_location)
    NetworkImageView icLocation;
    @BindView(R.id.lbl_gallerie)
    LatoTextView lblGallerie;
    @BindView(R.id.ic_gallerie)
    NetworkImageView icGallerie;
    @BindView(R.id.tv_nbr_photo)
    LatoTextView tvNbrPhoto;
    @BindView(R.id.layout_image_gallerie)
    RelativeLayout layoutImageGallerie;
    @BindView(R.id.lbl_horaire)
    LatoTextView lblHoraire;
    @BindView(R.id.ic_horaire)
    NetworkImageView icHoraire;
    @BindView(R.id.layout_horaire)
    RelativeLayout layoutHoraire;
    @BindView(R.id.lbl_menu)
    LatoTextView lblMenu;
    @BindView(R.id.ic_menu)
    NetworkImageView icMenu;
    @BindView(R.id.layout_menu)
    RelativeLayout layoutMenu;
    @BindView(R.id.lbl_information_supplementaire)
    LatoTextView lblInformationSupplementaire;
    @BindView(R.id.ic_info_supp)
    NetworkImageView icInfoSupp;
    @BindView(R.id.layout_informations)
    RelativeLayout layoutInformations;
    @BindView(R.id.lbl_business_phone)
    LatoTextView lblBusinessPhone;
    @BindView(R.id.commercePhone)
    LatoEditText commercePhone;
    @BindView(R.id.ic_phone)
    NetworkImageView icPhone;
    @BindView(R.id.layout_commerce_phone)
    RelativeLayout layoutCommercePhone;
    @BindView(R.id.lbl_business_postal)
    LatoTextView lblBusinessPostal;
    @BindView(R.id.commercePostal)
    LatoEditText commercePostal;
    @BindView(R.id.ic_postal)
    NetworkImageView icPostal;
    @BindView(R.id.layout_commerce_postal)
    RelativeLayout layoutCommercePostal;
    @BindView(R.id.lbl_business_description)
    LatoTextView lblBusinessDescription;
    @BindView(R.id.commerceDescription)
    LatoEditText commerceDescription;
    @BindView(R.id.ic_description)
    NetworkImageView icDescription;
    @BindView(R.id.layout_commerce_description)
    RelativeLayout layoutCommerceDescription;
    @BindView(R.id.nested_edit)
    NestedScrollView nestedEdit;
    @BindView(R.id.iv_arrow_loading_accueil)
    NetworkImageView ivArrowLoadingAccueil;
    @BindView(R.id.layout_no_internet_edit)
    LinearLayout layoutNoInternetEdit;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    List<Classification> subCategorieDesfault = new ArrayList<>();
    private String[] PERMISSIONS_PHOTO = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private Uri croppedImage;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    boolean pickImageProfile = false;
    boolean pickImageCover = false;
    private List<Classification> selectedSearchTags = new ArrayList<>(), subcategories = new ArrayList<>();
    private Classification[] selectedRegionsList;
    private HashMap<Integer, Classification> selectedSubCategoryMap = new HashMap<>(), selectedSubCategoryMap2 = new HashMap<>();
    private List<Region> selectedRegions, regions = new ArrayList<>(), displayedRegions;
    private ImageGalerie[] imageGaleries;
    private List<ImageGalerie> imageGaleriesMenu;
    private List<MenuType> menuTypes = new ArrayList<>();
    Region regionToSend;
    private boolean isAlertShown = false;
    private Business business;
    private ImageGalerie imgProfile;
    private ImageGalerie imgCover;
    private Business businessReady = new Business();
    Business businessInfos = new Business();
    Business businessAdd = new Business();
    Business businessHor = new Business();
    RefreshReceiverFromInformations refreshReceiverFromInformations;
    RefreshReceiverFromHoraires refreshReceiverFromHoraires;
    RefreshReceiverFromAddress refreshReceiverFromAddress;
    RefreshReceiverFromImage refreshReceiverFromImage;
    RefreshReceiverFromHappyHour refreshReceiverFromHappyHour;
    private Business businessImage;
    private ArrayList<ImageGalerie> img = new ArrayList<>();
    private ArrayList<ImageGalerie> imgMenu = new ArrayList<>();
    private boolean isFromProfile = false;
    private NetworkImageView icHappyHour;
    private View layoutHappyHour;
    private Business businessHappyHour;

    @Override
    protected void onResume() {
        super.onResume();
        refreshReceiverFromInformations = new UpdateActivity.RefreshReceiverFromInformations();
        refreshReceiverFromHoraires = new UpdateActivity.RefreshReceiverFromHoraires();
        refreshReceiverFromAddress = new UpdateActivity.RefreshReceiverFromAddress();
        refreshReceiverFromImage = new UpdateActivity.RefreshReceiverFromImage();
        refreshReceiverFromHappyHour = new UpdateActivity.RefreshReceiverFromHappyHour();
        IntentFilter intentFilter = new IntentFilter("refresh_state_informations");
        IntentFilter intentFilter1 = new IntentFilter("refresh_state_horaires");
        IntentFilter intentFilterAdd = new IntentFilter("refresh_state_address");
        IntentFilter intentFilterImage = new IntentFilter("refresh_state_image");
        IntentFilter intentFilterHappyHour = new IntentFilter("refresh_state_happy_hour");
        getApplicationContext().registerReceiver(refreshReceiverFromInformations, intentFilter);
        getApplicationContext().registerReceiver(refreshReceiverFromHoraires, intentFilter1);
        getApplicationContext().registerReceiver(refreshReceiverFromAddress, intentFilterAdd);
        getApplicationContext().registerReceiver(refreshReceiverFromImage, intentFilterImage);
        getApplicationContext().registerReceiver(refreshReceiverFromHappyHour, intentFilterHappyHour);
    }
    private void showAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        dialogBuilder.setTitle(getResources().getString(R.string.quitter_withour_save) +
                "\n");
        dialogBuilder.setMessage("Êtes vous sur de vouloir quitter la page sans sauvegarder vos changements ?\n" +
                "\n" +
                "Tous les changements que vous avez faits seront perdus.");
        dialogBuilder.setPositiveButton("QUITTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Utils.adress = "";
                Utils.longitude = 0;
                Utils.latitude = 0;
                Utils.imageGaleriesMenu = new ArrayList<>();
                Utils.menuTypes = new ArrayList<>();
                Utils.hourItem = new JsonArray();
                Utils.HappyHour = new JsonArray();
                finish();



            }
        });
        dialogBuilder.setNegativeButton("RESTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_3));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        menuTypes = new ArrayList<>();
        imageGaleriesMenu = new ArrayList<>();
        ButterKnife.bind(this);
        if (getIntent() != null) {
            try {
                if (!ConnectivityService.isOnline(getBaseContext())) {
                    nestedEdit.setVisibility(View.GONE);
                    layoutNoInternetEdit.setVisibility(View.VISIBLE);
                } else {
                    businessReady = (Business) getIntent().getSerializableExtra("modifBusiness");
                    business = businessReady;
                    nestedEdit.setVisibility(View.VISIBLE);
                    layoutNoInternetEdit.setVisibility(View.GONE);
                    initializeData(business);
                }


            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
                System.out.println("mchit");
            }


        }
        icHappyHour = findViewById(R.id.ic_happy_hour);
        lblProfileImage.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        lblCoverImage.setOnClickListener(this);
        ivCover.setOnClickListener(this);
        lblLocation.setOnClickListener(this);
        layoutCommerceCategorie.setOnClickListener(this);
        layoutCommerceRegion.setOnClickListener(this);
        layoutMenu.setOnClickListener(this);
        layoutHoraire.setOnClickListener(this);
        layoutInformations.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvTerminer.setOnClickListener(this);
        tvTerminer.setEnabled(true);
        layoutImageGallerie.setOnClickListener(this);
        layoutHappyHour = findViewById(R.id.layout_happy_hour);
        layoutHappyHour.setOnClickListener(this);
        layoutNoInternetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ConnectivityService.isOnline(getBaseContext())) {
                    nestedEdit.setVisibility(View.GONE);
                    layoutNoInternetEdit.setVisibility(View.VISIBLE);
                } else {
                    nestedEdit.setVisibility(View.VISIBLE);
                    layoutNoInternetEdit.setVisibility(View.GONE);
                    initializeData(businessReady);
                }
            }
        });
    }

    private void addImagesList(Business business) {
        if (img != null)
            if (img.size() != 0) {
                final MultipartBody.Part[] parts = new MultipartBody.Part[img.size()];
                for (int i = 0; i < img.size(); i++) {

                    File file = new File(Uri.parse(img.get(i).getImage()).getPath());
                    RequestBody reqFileBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imageToupload =
                            MultipartBody.Part.createFormData("image", file.getName(), reqFileBody);
                    parts[i] = imageToupload;
                }
                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addListImage(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), parts);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        System.out.println("failure");
                        System.out.println(call.toString());
                        System.out.println(t.getMessage());
                    }
                });
            }
    }

    private void addImagesMenuList(Business business) {
        if (imgMenu != null)
            if (imgMenu.size() != 0) {
                final MultipartBody.Part[] parts = new MultipartBody.Part[imgMenu.size()];
                for (int i = 0; i < imgMenu.size(); i++) {

                    File file = new File(Uri.parse(imgMenu.get(i).getImage()).getPath());
                    RequestBody reqFileBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imageToupload =
                            MultipartBody.Part.createFormData("image", file.getName(), reqFileBody);
                    parts[i] = imageToupload;
                }
                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addListPhotoBusinessMenu(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), parts);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        int code = response.code();
                        System.out.println(response.code());
                        System.out.println(response.message());
                        if (getBaseContext() != null) {
                            if (code != 200) {

                            } else {

                                Utils.imageGaleriesMenu = new ArrayList<>();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("failure");
                        System.out.println(call.toString());
                        System.out.println(t.getMessage());
                    }
                });
            }
    }

    private void refreshBusinessData() {


        if (businessHappyHour != null) {
            try {
                int j = 0;
                business.setHours(businessHappyHour.getHours());
                businessReady.setHours(businessHappyHour.getHours());
                for (int i = 0; i < business.getHours().size(); i++) {
                    if (business.getHours().get(i).isAvailable()) {
                        j++;
                    }
                }
                if (j != 0) {
                    icHappyHour.setImageResource(R.drawable.tick_green);
                } else icHappyHour.setImageResource(R.drawable.text_warning_icon);
            } catch (NullPointerException e) {
                icHappyHour.setImageResource(R.drawable.text_warning_icon);
            }

        }

        if (businessImage != null) {

            try {
                img = new ArrayList<>();
                String toRemove = "https";
                for (int i = 0; i < businessImage.getImages().size(); i++) {
                    if (!businessImage.getImages().get(i).getImage().substring(0, 5).equalsIgnoreCase(toRemove)) {
                        img.add(businessImage.getImages().get(i));
                    }

                }
                try {
                    if (business.getImages().size() != 0) {
                        business.getImages().clear();
                    }

                } catch (NullPointerException e) {

                }
                business.setImages(businessImage.getImages());


            } catch (NullPointerException e) {

                System.out.println(e.getMessage());

            }
        }
        if (Utils.imageGaleriesMenu != null) {
            if (Utils.imageGaleriesMenu.size() != 0) {
                try {

                    String toRemove = "https";
                    for (int i = 0; i < Utils.imageGaleriesMenu.size(); i++) {
                        if (!Utils.imageGaleriesMenu.get(i).getImage().substring(0, 5).equalsIgnoreCase(toRemove)) {
                            imgMenu.add(Utils.imageGaleriesMenu.get(i));
                        }

                    }
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());

                }
            }
        }
        if (businessInfos != null) {
            try {

                business.setRecommendedFor(businessInfos.getRecommendedFor());
                businessReady.setRecommendedFor(businessInfos.getRecommendedFor());
                business.setGoodFor(businessInfos.getGoodFor());
                businessReady.setGoodFor(businessInfos.getGoodFor());
                business.setPrincipalInfo(businessInfos.getPrincipalInfo());
                businessReady.setPrincipalInfo(businessInfos.getPrincipalInfo());
                business.setImportantInfo(businessInfos.getImportantInfo());
                businessReady.setImportantInfo(businessInfos.getImportantInfo());


            } catch (NullPointerException e) {

            }
        }
        if (Utils.hourItem.size() != 0) {
            sendHourToService(business, Utils.hourItem);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (refreshReceiverFromInformations != null && UpdateActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromInformations);

            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromHoraires != null && UpdateActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromHoraires);
            } catch (IllegalArgumentException e) {
            }
        } else if (refreshReceiverFromAddress != null && UpdateActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromAddress);
            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromImage != null && UpdateActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromImage);
            } catch (IllegalArgumentException e) {

            }

        } else if (refreshReceiverFromHappyHour != null && UpdateActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromHappyHour);
            } catch (IllegalArgumentException e) {

            }
        }else {

        }
    }

    private void initializeData(Business business) {
        try {
            if (business.getHasHappyHour() == 1) {
                icHappyHour.setImageResource(R.drawable.tick_green);
            } else {
                icHappyHour.setImageResource(R.drawable.text_warning_icon);
            }
        } catch (NullPointerException e) {

        }
        try {
            if (business.getHasHappyHour() == 1) {
                icHappyHour.setImageResource(R.drawable.tick_green);
            } else icHappyHour.setImageResource(R.drawable.text_warning_icon);

        } catch (NullPointerException e) {

        }
        ///////////////initializeBusinessName
        try {
            if (business.getName() != null) {
                if (!business.getName().equalsIgnoreCase("")) {
                    commerceName.setText(business.getName());
                    icName.setImageResource(R.drawable.tick_green);
                } else {
                    icName.setImageResource(R.drawable.text_warning);
                }
            }
        } catch (NullPointerException e) {
            icName.setImageResource(R.drawable.text_warning);
        }
        ////////////////////initializeLocation
        try {
            if (business.getRegion() != null) {
                if (!business.getRegion().getLabel().equalsIgnoreCase("")) {
                    commerceregion.setText(business.getRegion().getLabel());
                    icRegion.setImageResource(R.drawable.tick_green);
                } else {
                    icRegion.setImageResource(R.drawable.text_warning);
                }
            }
        } catch (NullPointerException e) {
            icRegion.setImageResource(R.drawable.text_warning);
        }
        ////////////////initializeCatégorie
        try {
            String subCategory = "";
            if (business.getSubCategory() != null) {
                if (business.getSubCategory().length != 0) {
                    icCategorie.setImageResource(R.drawable.tick_green);
                    if (business.getSubCategory().length > 1) {
                        for (int i = 0; i < business.getSubCategory().length; i++) {
                            subCategory += business.getSubCategory()[i].getLabel() + '.';

                        }
                        if (subCategory.length() > 25) {
                            commercecategorie.setText(subCategory.substring(0, 25) + "...");
                        } else {
                            commercecategorie.setText(subCategory);
                        }
                    } else {
                        commercecategorie.setText(business.getSubCategory()[0].getLabel());
                    }
                    String bar = "Bar";

                    if (subCategory.indexOf(bar) != -1) {
                        commercebeerprice.setEnabled(true);
                        layoutCommerceBeerprice.setVisibility(View.VISIBLE);
                    }
                    else {
                        commercebeerprice.setEnabled(false);
                        layoutCommerceBeerprice.setVisibility(View.GONE);
                    }

                } else {
                    icCategorie.setImageResource(R.drawable.text_warning);
                }
            }
        } catch (NullPointerException e) {
            icCategorie.setImageResource(R.drawable.text_warning);
        }
        ////////////initialize beer price
        try {
            if (business.getBeerPrice() != 0) {
                DecimalFormat df = new DecimalFormat("###.##");
                commercebeerprice.setText(df.format(business.getBeerPrice()));
                icPrice.setImageResource(R.drawable.tick_green);

            } else {
                icPrice.setImageResource(R.drawable.text_warning);
            }
        } catch (NullPointerException e) {
            icPrice.setImageResource(R.drawable.text_warning);
        }
        //////////////initialize profile image
        try {
            if (business.getImage() != null) {
                if (!business.getImage().equalsIgnoreCase("")) {
                    ivProfile.setImageUriWithoutProgress(business.getImage(), getBaseContext());
                    icProfile.setImageResource(R.drawable.tick_green);

                } else {
                    ivProfile.setImageResource(R.drawable.icon_business_default);
                    icProfile.setImageResource(R.drawable.text_warning);

                }
            }
        } catch (NullPointerException e) {
            ivProfile.setImageResource(R.drawable.icon_business_default);
            icProfile.setImageResource(R.drawable.text_warning);

        }
        //////////////initialize cover image
        try {
            if (business.getCoverImg() != null) {
                if (!business.getCoverImg().getImage().equalsIgnoreCase("")) {
                    ivCover.setImageUriWithoutProgress(business.getCoverImg().getImage(), getBaseContext());
                    icCouverture.setImageResource(R.drawable.tick_green);

                } else {
                    ivCover.setImageResource(R.drawable.icon_business_default);
                    icCouverture.setImageResource(R.drawable.text_warning);

                }
            }
        } catch (NullPointerException e) {
            icCouverture.setImageResource(R.drawable.icon_business_default);
            icCouverture.setImageResource(R.drawable.text_warning);

        }
        ///////InitializeLocation
        try {
            if (business.getLongitude() != null && business.getLatitude() != null) {
                if (!business.getLatitude().equalsIgnoreCase("") && !business.getLongitude().equalsIgnoreCase("")) {
                    icLocation.setImageResource(R.drawable.tick_green);
                } else {
                    icLocation.setImageResource(R.drawable.text_warning);
                }
            }
        } catch (NullPointerException e) {
            icLocation.setImageResource(R.drawable.text_warning);
        }
        ///////Initialize gallerieCount
        try {
            if (business.getImages().size() != 0) {


                icGallerie.setImageResource(R.drawable.tick_green);
            } else {

                icGallerie.setImageResource(R.drawable.text_warning);
            }

        } catch (NullPointerException e) {

            icGallerie.setImageResource(R.drawable.text_warning);
        }
        ////Initialize Horaire
        try {
            if (business.getWorkingHours() != null) {
                if (business.getWorkingHours().size() > 0) {
                    icHoraire.setImageResource(R.drawable.tick_green);
                } else {
                    icHoraire.setImageResource(R.drawable.text_warning);
                }
            }
        } catch (NullPointerException e) {
            icHoraire.setImageResource(R.drawable.text_warning);
        }
        //////iNITIALIZE MENU
        try {
            if (business.isHasMenu()) {
                icMenu.setImageResource(R.drawable.tick_green);
            } else {
                icMenu.setImageResource(R.drawable.text_warning);
            }
        } catch (NullPointerException e) {
            icMenu.setImageResource(R.drawable.text_warning);
        }


        /////Initialize Supp Info
        try {
            if (business.getRecommendedFor().size() > 0 || business.getGoodFor().size() > 0 || business.getImportantInfo().size() > 0 || business.getPrincipalInfo().size() > 0) {
                icInfoSupp.setImageResource(R.drawable.tick_green);
            } else {
                icInfoSupp.setImageResource(R.drawable.text_warning);
            }
        } catch (NullPointerException e) {
            icInfoSupp.setImageResource(R.drawable.text_warning);
        }
        ///InitializePhone
        try {
            if (business.getTel() != null && !business.getTel().equalsIgnoreCase("")) {
                commercePhone.setText(business.getTel());
                icPhone.setImageResource(R.drawable.tick_green);

            } else {
                icPhone.setImageResource(R.drawable.text_warning);
            }
        } catch (NullPointerException e) {
            icPhone.setImageResource(R.drawable.text_warning);
        }
        ////InitializePostalCode
        try {
            if (business.getPostalCode() != 0) {
                commercePostal.setText(String.valueOf(business.getPostalCode()));
                icPostal.setImageResource(R.drawable.tick_green);

            } else {
                icPostal.setImageResource(R.drawable.text_warning);
            }
        } catch (NullPointerException e) {
            icPostal.setImageResource(R.drawable.text_warning);
        }
        /////InitializeDescription
        try {
            if (business.getDescription() != null && !business.getDescription().equalsIgnoreCase("")) {
                commerceDescription.setText(business.getDescription());
                icDescription.setImageResource(R.drawable.tick_green);

            }
        } catch (NullPointerException e) {
            icDescription.setImageResource(R.drawable.text_warning);
        }
        getAllRegions();
        getAllSubCategories();
        listenerInput();
    }

    private boolean getAllRegions() {


        Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getAllRegions(1, 0, 1000);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

                        displayedRegions = new ArrayList<Region>(regions);


                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    if (getBaseContext() != null) {

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });

        return true;

    }

    @Override
    protected void onRestart() {
        refreshBusinessData();


        super.onRestart();
    }

    private boolean getAllSubCategories() {

        subcategories = new ArrayList<Classification>();
        subcategories = CompteManagerService.getAllSubCategories(this);
        if (subcategories != null) {
            //loadingLayout.setVisibility(View.GONE);
            return true;
        } else {
            Call<ResponseBody> call = RetrofitServiceFacotry.getGeneralInfoService().getClassificationByType(2, 0, 100);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            CompteManagerService.saveAllSubCategories(getBaseContext(), subcategories);
                            for (int i = 0; i < subcategories.size(); i++) {
                                for (int j = 0; j < selectedSearchTags.size(); j++) {
                                    if (subcategories.get(i).getId() == selectedSearchTags.get(j).getId()) {
                                        selectedSubCategoryMap = new HashMap<Integer, Classification>();
                                        selectedSubCategoryMap.put(i, subcategories.get(i));
                                    }
                                }
                            }


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

    private void listenerInput() {
        commerceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icName.setImageResource(R.drawable.text_warning);
                } else {
                    icName.setImageResource(R.drawable.tick_green);
                }
            }
        });
        commerceregion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icRegion.setImageResource(R.drawable.text_warning);
                } else {
                    icRegion.setImageResource(R.drawable.tick_green);
                }
            }
        });
        commercecategorie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icCategorie.setImageResource(R.drawable.text_warning);
                } else {
                    icCategorie.setImageResource(R.drawable.tick_green);
                }
            }
        });
        commercebeerprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (commercebeerprice.isEnabled())
                    if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                        icPrice.setImageResource(R.drawable.text_warning);
                    } else {
                        icPrice.setImageResource(R.drawable.tick_green);
                    }
            }
        });
        commercePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icPhone.setImageResource(R.drawable.text_warning);
                } else {
                    icPhone.setImageResource(R.drawable.tick_green);
                }
            }
        });
        commercePostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icPostal.setImageResource(R.drawable.text_warning);
                } else {
                    icPostal.setImageResource(R.drawable.tick_green);
                }
            }
        });
        commerceDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equalsIgnoreCase("") && editable.length() == 0) {
                    icDescription.setImageResource(R.drawable.text_warning);
                } else {
                    icDescription.setImageResource(R.drawable.tick_green);
                }
            }
        });
    }

    private void sendBusinessMenu(Business business) {
        JsonArray menuToSend = new JsonArray();
        JsonArray productsSend = new JsonArray();
        JsonObject categorieJson;
        JsonObject productJson;

        MenuType menuType = new MenuType();
        try {

        } catch (NullPointerException e) {

        }
        menuTypes = Utils.menuTypes;
        if (menuTypes != null) {
            if (menuTypes.size() != 0) {
                for (int i = 0; i < menuTypes.size(); i++) {
                    menuType = menuTypes.get(i);
                    categorieJson = new JsonObject();
                    productsSend = new JsonArray();
                    categorieJson.addProperty("label", menuType.getLabel());
                    categorieJson.addProperty("description", "");
                    categorieJson.addProperty("businessId", business.getId());
                    for (int j = 0; j < menuType.getProducts().size(); j++) {
                        productJson = new JsonObject();
                        productJson.addProperty("label", menuType.getProducts().get(j).getLabel());
                        productJson.addProperty("description", "");
                        productJson.addProperty("price", menuType.getProducts().get(j).getPrice());
                        productJson.addProperty("categoryId", menuType.getProducts().get(j).getCategoryId());
                        productsSend.add(productJson);
                    }
                    categorieJson.add("products", productsSend);
                    menuToSend.add(categorieJson);

                }
                Utils.menuTypes = menuTypes;
                sendBusinessMenuToServer(business, menuToSend);
            }


        }

    }

    private void sendBusinessMenuToServer(Business business, JsonArray menuToSend) {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().sendBusinessMenu(business.getId(), menuToSend);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {
                        Utils.menuTypes = new ArrayList<>();

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

    public static boolean hasPermissions(Context context, int permission_request, String... permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                return false;
            }
        }
        return true;
    }

    private void imageFromGallerie() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private void imageFromCamera() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }

    private void uploadImageDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);

        builder.setTitle("Ajouter une photo");
        builder.setMessage("Prenez une photo ou bien sélectionnez depuis vos photos");
        builder.setPositiveButton("GALLERIE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromGallerie();
                dialog.dismiss();


            }
        });

        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromCamera();
                dialog.dismiss();

            }
        });


        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pickImageProfile = false;
                pickImageCover = false;
            }
        });

    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        if (list != null) {


            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.blanb));


            UCrop.of(Uri.fromFile(new File((list.get(0).getOriginalPath()))),
                    Uri.fromFile(new File((list.get(0).getOriginalPath()))))
                    .withOptions(options)
                    .withMaxResultSize(1000, 1000)
                    .start(this);


        }

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onClick(View view) {
        if (view == tvTerminer) {
            if (!ConnectivityService.isOnline(getBaseContext())) {
                nestedEdit.setVisibility(View.GONE);
                layoutNoInternetEdit.setVisibility(View.VISIBLE);
            } else {

                nestedEdit.setVisibility(View.VISIBLE);
                layoutNoInternetEdit.setVisibility(View.GONE);
                if (getBaseContext() != null)
                    layoutLoading.setVisibility(View.VISIBLE);

                tvTerminer.setEnabled(false);
                if (commerceName.getText().toString().equalsIgnoreCase("") && commercebeerprice
                        .getText().toString().equalsIgnoreCase("")) {

                } else {
                    prepareForEditBasic();
                    prepareToeditSubCategorie();
                    try {
                        if (!commercebeerprice.getText().toString().equalsIgnoreCase("")) {
                            prepareTosendBeerPrice();
                        }
                    } catch (NullPointerException e) {

                    }
                    if (Utils.hourItem != null) {
                        if (Utils.hourItem.size() != 0) {
                            sendHourToService(business, Utils.hourItem);
                        }
                    }
                    if (Utils.menuTypes != null) {
                        if (Utils.menuTypes.size() != 0) {
                            sendBusinessMenu(business);
                        }
                    }

                    if (imgMenu != null) {
                        if (imgMenu.size() != 0) {

                            addImagesMenuList(business);
                        }
                    }
                    if (img != null) {
                        if (img.size() != 0) {
                            addImagesList(business);
                        }
                    }

                    try {
                        if (business.getImportantInfo().size() != 0
                                || business.getRecommendedFor().size() != 0 || business.getPrincipalInfo().size() != 0
                                || business.getGoodFor().size() != 0) {
                            preapreForEditAddInfo(business);
                        }
                    } catch (NullPointerException e) {

                    }


                }
                try {
                    try {
                        if (Utils.HappyHour.size() != 0)
                            sendHappyHourToService(Utils.HappyHour, business);
                    } catch (NullPointerException e) {

                    }
                } catch (NullPointerException e) {

                }
                try {
                    if (imgCover == null && imgCover.getThumbnailPath().equalsIgnoreCase("")) {

                    } else {
                        prepareForUploadImageCover();

                    }
                } catch (NullPointerException e) {

                }
                try {
                    if (imgProfile == null && imgProfile.getThumbnailPath().equalsIgnoreCase("")) {

                    } else {
                        prepareForUploadImageProfile();
                    }
                } catch (NullPointerException e) {

                }

            }

            Utils.adress = "";
            Utils.longitude = 0;
            Utils.latitude = 0;
            Utils.imageGaleriesMenu = new ArrayList<>();
            Utils.menuTypes = new ArrayList<>();
            Utils.hourItem = new JsonArray();
            Bundle bundle = new Bundle();
            bundle.putSerializable("businessFromAdd", business);
            Intent intent = new Intent(UpdateActivity.this, ItemBusinessActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        if (view == ivBack) {
            if(commercecategorie.getText().toString().equalsIgnoreCase("")
                    &&commerceName.getText().toString().equalsIgnoreCase("")
                    && commerceregion.getText().toString().equalsIgnoreCase("")
                    && Utils.adress.equalsIgnoreCase("") && Utils.menuTypes.size()==0 && Utils.imageGaleriesMenu.size() ==0
                    && Utils.hourItem.size()==0&& !pickImageCover && !pickImageProfile)
            {

            }
            else {
                showAlert();
            }



        }
        if (view == lblProfileImage || view == ivProfile) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
                pickImageProfile = true;
                pickImageCover = false;
            }

        }
        if (view == lblCoverImage || view == ivCover) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
                pickImageProfile = false;
                pickImageCover = true;
            }

        }
        if (view == layoutHappyHour) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Intent intent = new Intent(UpdateActivity.this, HappyHourActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == lblLocation) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Intent intent = new Intent(UpdateActivity.this, AdressActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == layoutImageGallerie) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Intent intent = new Intent(UpdateActivity.this, PhotosGridActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

          /*  Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            bundle.putSerializable("imageGaleries", imageGaleries);
            Fragment fragment = new PhotosGridFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_content, fragment).commit();*/
        }
        if (view == layoutHoraire) {
            /*Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Fragment fragment = new WorkingHourFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_content, fragment).commit();*/
            Intent intent = new Intent(UpdateActivity.this, HoraireActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);

            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == layoutMenu) {
          /*  Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            bundle.putSerializable("images", (Serializable) imageGaleriesMenu);
            bundle.putSerializable("itemsMenu", (Serializable) menuTypes);
            Fragment fragment = new MenuFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_content, fragment).commit();*/

            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Intent intent = new Intent(UpdateActivity.this, MenuActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }


        if (view == layoutInformations) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("business", business);
            Intent intent = new Intent(UpdateActivity.this, InformationsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == layoutCommerceRegion) {
            final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
            dialogFactoryUtils.showRegionListDialog(this);
            dialogFactoryUtils.setOnConfirmRegionSelectListener(new DialogFactoryUtils.OnConfirmRegionSelect() {
                @Override
                public void OnConfirmRegionSelectListener(Region region) {
                    try {

                        regionToSend = region;
                        commerceregion.setText(region.getLabel());

                    } catch (NullPointerException e) {

                        commerceregion.setText("");
                    }
                }
            });
        }
        if (view == layoutCommerceCategorie) {
            subCategorieDesfault = new ArrayList<>();
            try {
                subCategorieDesfault = Arrays.asList(business.getSubCategory());
            } catch (NullPointerException e) {

            }
            final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
            dialogFactoryUtils.showCategoryListDialog(this, subcategories, new HashMap<>(selectedSubCategoryMap2), subCategorieDesfault);
            dialogFactoryUtils.setOnConfirmSubCategoryConfirmListener(new DialogFactoryUtils.OnConfirmSubCategoryConfirmListener() {
                @Override
                public void OnConfirmSubCategoryConfirmListener(HashMap<Integer, Classification> selectedSubCategoryMap) {
                    selectedSubCategoryMap2 = new HashMap<>(selectedSubCategoryMap);
                    selectedSearchTags = new ArrayList<>(selectedSubCategoryMap2.values());
                    String categoriesName = "";
                    String categoriesNameToSet = "";
                    for (int i = 0; i < selectedSearchTags.size(); i++) {

                        categoriesName += selectedSearchTags.get(i).getLabel() + ".";

                    }
                    try {
                        selectedRegionsList = new Classification[selectedSearchTags.size()];
                        selectedRegionsList = selectedSearchTags.toArray(selectedRegionsList);
                        business.setSubCategory(selectedRegionsList);

                    } catch (NullPointerException | IndexOutOfBoundsException e) {


                    }

                    if (categoriesName.length() > 30) {
                        categoriesNameToSet = categoriesName.substring(0, 25) + "...";
                    } else {
                        categoriesNameToSet = categoriesName;
                    }
                    String bar = "Bar";

                    if (categoriesName.indexOf(bar) != -1) {
                        System.out.println("hneee");
                        commercebeerprice.setEnabled(true);
                        layoutCommerceBeerprice.setVisibility(View.VISIBLE);
                        try {
                            if (business.getBeerPrice() != 0) {
                                commercebeerprice.setText(String.valueOf(business.getBeerPrice()));
                                commercebeerprice.setEnabled(true);
                            }
                        } catch (NullPointerException e) {
                            commercebeerprice.setText("0");
                        }

                    } else {
                        System.out.println("mouch hné");
                        commercebeerprice.setEnabled(false);
                        layoutCommerceBeerprice.setVisibility(View.GONE);
                        commercebeerprice.setText("");

                    }
                    commercecategorie.setText(categoriesNameToSet);


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(commercecategorie.getText().toString().equalsIgnoreCase("")
                &&commerceName.getText().toString().equalsIgnoreCase("")
                && commerceregion.getText().toString().equalsIgnoreCase("")
                && Utils.adress.equalsIgnoreCase("") && Utils.menuTypes.size()==0 && Utils.imageGaleriesMenu.size() ==0
                && Utils.hourItem.size()==0 && !pickImageCover && !pickImageProfile)
        {
            super.onBackPressed();
        }
        else {
            showAlert();
        }
    }

    private void prepareForEditBasic() {

        int regionId = 0;
        String name, description, adress, latitude, longitude, tel, postalCode;
        if (commerceName.getText().toString().equalsIgnoreCase("") || commerceDescription.getText().toString().equalsIgnoreCase("")
                || commercePostal.getText().toString().equalsIgnoreCase("") || commercePhone.getText().toString().equalsIgnoreCase("")
                || commerceregion.getText().toString().equalsIgnoreCase("") || commercecategorie.getText().toString().equalsIgnoreCase("")
                ) {

        } else {
            name = commerceName.getText().toString();
            description = commerceDescription.getText().toString();
            if (Utils.adress.equalsIgnoreCase("")) {
                Utils.adress = "";
            }
            adress = Utils.adress;
            if (Utils.latitude == 0) {
                latitude = business.getLatitude();
            } else {
                latitude = String.valueOf(Utils.latitude);
            }
            if (Utils.longitude == 0) {
                longitude = business.getLongitude();
            } else {
                longitude = String.valueOf(Utils.longitude);
            }
            tel = commercePhone.getText().toString();
            postalCode = commercePostal.getText().toString();
            if (!commerceregion.getText().toString().equalsIgnoreCase("")) {
                try {
                    if (regionToSend != null) {
                        regionId = regionToSend.getId();
                    } else {
                        regionId = business.getRegion().getId();
                    }
                } catch (NullPointerException e) {
                    regionId = business.getRegion().getId();
                }
            } else {
                regionId = 0;
            }

            JsonObject postParam = new JsonObject();
            postParam.addProperty("name", name);
            postParam.addProperty("description", description);
            postParam.addProperty("address", adress);
            postParam.addProperty("regionId", regionId);
            postParam.addProperty("latitude", latitude);
            postParam.addProperty("longitude", longitude);
            postParam.addProperty("tel", tel);
            postParam.addProperty("postalCode", postalCode);
            sendBasiqueInfo(postParam);
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


    private void sendHappyHourToService(JsonArray hourItem, Business business) {

        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editHappyHours(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), hourItem);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();
                System.out.println(response.code());
                System.out.println(response.message());

                if (getBaseContext() != null) {
                    if (code != 200) {

                    } else {

                        Utils.HappyHour = new JsonArray();


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

    private void prepareToeditSubCategorie() {
        JsonObject postparams = new JsonObject();
        JsonArray ids = new JsonArray();
        if (selectedSearchTags.size() > 0) {
            ids = new JsonArray();
            for (int i = 0; i < selectedSearchTags.size(); i++) {
                ids.add(selectedSearchTags.get(i).getId());
            }
        } else {
            ids = new JsonArray();
        }
        postparams.add("id", ids);
        sendSubCategorie(postparams);
    }

    private void prepareTosendBeerPrice() {
        JsonObject postParam = new JsonObject();
        int beerPrice = 0;
        if (commercebeerprice.getText().toString().equalsIgnoreCase("")) {
            beerPrice = 0;
        } else {
            try {
                beerPrice = (int) Double.parseDouble(commercebeerprice.getText().toString());
                postParam.addProperty("newBeerPrice", beerPrice);
            } catch (NumberFormatException e) {

            }

        }
        sendBeerPrice(postParam);

    }

    private void prepareForUploadImageProfile() {
        try {
            if (imgProfile != null) {
                File file = new File(imgProfile.getThumbnailPath());
                RequestBody reqFile = RequestBody.create(
                        MediaType.parse("image/*"), file
                );
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), reqFile);

                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().updateProfileImage(business.getId(), body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {

                            if (response.code() == 200) {
                                try {
                                    layoutLoading.setVisibility(View.GONE);
                                } catch (NullPointerException e) {

                                }


                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            layoutLoading.setVisibility(View.GONE);
                        }

                        layoutLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Log.i("Upload error :", t.getMessage());

                        layoutLoading.setVisibility(View.GONE);
                    }
                });
            }

        } catch (NullPointerException e) {
            System.out.println("null");
        }
        //}
    }

    private void prepareForUploadImageCover() {
       /* if(!ConnectivityService.isOnline(getContext()))
        {
            new CustomToast(getContext(), "Hors connexion", "vous êtes hors connexion", R.drawable.ic_popup_add, CustomToast.ERROR).show();
        }
        else {*/
        try {

            if (imgCover != null) {
                File file = new File(imgCover.getThumbnailPath());
                RequestBody reqFile = RequestBody.create(
                        MediaType.parse("image/*"), file
                );
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), reqFile);

                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().updateProfileCover(business.getId(), body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {

                            if (response.code() == 200) {
                                try {

                                } catch (NullPointerException e) {
                                    layoutLoading.setVisibility(View.GONE);
                                }

                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            layoutLoading.setVisibility(View.GONE);
                        }
                        layoutLoading.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        layoutLoading.setVisibility(View.GONE);
                    }
                });
            }

        } catch (NullPointerException e) {
            layoutLoading.setVisibility(View.GONE);
        }
    }

    private void sendBasiqueInfo(JsonObject postParam) {

        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editBasicBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), postParam);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();


                if (getBaseContext() != null) {
                    if (code != 200) {

                        System.out.println(response.message());
                        System.out.println(response.errorBody());

                    } else {
                        Utils.adress = "";
                        Utils.latitude = 0d;
                        Utils.longitude = 0d;
                        System.out.println("good basic");

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getBaseContext() != null) {
                    if (!isAlertShown)
                        showLowConnectionDialog();
                }

            }
        });
    }

    private void showLowConnectionDialog() {

        isAlertShown = true;

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        View viewAlerte = this.getLayoutInflater().inflate(R.layout.connexion_lente, null);

        final View view = viewAlerte.findViewById(R.id.rl_retry);

        dialogBuilder.setView(viewAlerte);


        dialogBuilder.create();
        final AlertDialog alertDialog = dialogBuilder.show();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAlertShown = false;
                prepareForEditBasic();
                alertDialog.dismiss();
            }
        });

    }

    private void sendSubCategorie(JsonObject postparams) {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editSubCatefory(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), postparams);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {

                        System.out.println("good subCategory");

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void sendBeerPrice(JsonObject postParam) {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editBeerPriceBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), postParam);

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {
                        try {

                            layoutLoading.setVisibility(View.GONE);
                        } catch (NullPointerException e) {
                            if (getBaseContext() != null)
                                layoutLoading.setVisibility(View.GONE);
                        }


                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
        sendAddInfo(jsonArray, business);

    }

    private void sendAddInfo(JsonArray jsonArray, Business business) {
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

    /* private void addAdditionalInfos(Business business) throws JSONException {
         System.out.println("heni hne");
         System.out.println(business.getRecommendedFor().size());
         System.out.println(business.getPrincipalInfo().size());
         System.out.println(business.getImportantInfo().size());
         if (business.getRecommendedFor().size() != 0) {
             for (int i = 0; i < business.getRecommendedFor().size(); i++) {
                 ajoutAdditionalInfos(business.getRecommendedFor().get(i).getId());

             }

         }
         if (business.getPrincipalInfo().size() != 0) {
             for (int i = 0; i < business.getPrincipalInfo().size(); i++) {
                 ajoutAdditionalInfos(business.getPrincipalInfo().get(i).getId());

             }
         }

         if (business.getImportantInfo().size() != 0) {
             for (int i = 0; i < business.getImportantInfo().size(); i++) {
                 ajoutAdditionalInfos(business.getImportantInfo().get(i).getId());

             }
         }
         if (business.getGoodFor().size() != 0) {
             for (int i = 0; i < business.getGoodFor().size(); i++) {
                 ajoutAdditionalInfos(business.getGoodFor().get(i).getId());

             }
         }

     }

     private void ajoutAdditionalInfos(int id) throws JSONException {
         System.out.println("haw ghadi");
         JsonObject jsonObject = new JsonObject();

         jsonObject.addProperty("id", id);
         jsonObject.addProperty("value", true);

         JsonArray jsonArray = new JsonArray();
         jsonArray.add(jsonObject);
         Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addAdditionalInfosToBusiness(business.getId(), jsonArray);
         call.enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 System.out.println(response.message());
                 System.out.println("--------------");
                 if (response.code() == 200) {
                     try {
                         System.out.println("sucess ajout addiInfos"+ response.body().string());
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 } else {

                     System.out.println("erreur ajout addiInfos"+ response.toString() + " ! ");
                 }
             }

             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                 System.out.println("failuree");
             }
         });

     }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            }
            if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                croppedImage = resultUri;
                updateImage();
            } else if (resultCode == UCrop.RESULT_ERROR) {

            }
        }
    }


    private void addHoraires(Business createdBusiness) {

        final ArrayList<HoursHoraires> list = SharedPreferencesFactory.getListHoraires(getBaseContext());
        if (list != null) {
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("dayOfTheWeek", list.get(i).getDayOfTheWeek());
                    jsonObject.addProperty("openTime", list.get(i).getOpenTime());
                    jsonObject.addProperty("closeTime", list.get(i).getCloseTime());
                    JsonArray jsonArray = new JsonArray();
                    jsonArray.add(jsonObject);
                    Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addHoraires(SharedPreferencesFactory.getAdminToken(getBaseContext()), createdBusiness.getId(), jsonArray);
                    final int finalI = i;
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Log.e("success add Horaires ", "day number " + list.get(finalI).getDayOfTheWeek() + " " + response.message());
                            } else {
                                Log.e("error add Horaires ", "day number " + list.get(finalI).getDayOfTheWeek() + " " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("onFailure add Horaires", t.getMessage());

                        }
                    });
                }
            }
        }
    }

    private void updateImage() {
        if (pickImageProfile) {
            try {
                imgProfile = new ImageGalerie(0, croppedImage.getPath(), croppedImage.getPath(), true);
                ivProfile.setImageUriWithoutProgress(imgProfile.getImage(), getBaseContext());

            } catch (NullPointerException e) {
                System.out.println("null");
            }
        } else if (pickImageCover) {
            try {
                imgCover = new ImageGalerie(0, croppedImage.getPath(), croppedImage.getPath(), true);
                ivCover.setImageUriWithoutProgress(imgCover.getImage(), getBaseContext());

            } catch (NullPointerException e) {
                System.out.println("null");
            }
        }

    }


    public class RefreshReceiverFromInformations extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            businessInfos = (Business) intent.getSerializableExtra("businessInformations");
        }

    }

    public class RefreshReceiverFromImage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessImage = (Business) intent.getSerializableExtra("businessImage");

        }
    }

    public class RefreshReceiverFromHoraires extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessHor = (Business) intent.getSerializableExtra("businessHoraires");

        }
    }

    public class RefreshReceiverFromAddress extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessAdd = (Business) intent.getSerializableExtra("businessAddress");

        }
    }


    public class RefreshReceiverFromHappyHour extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessHappyHour = (Business) intent.getSerializableExtra("businessHappyHour");

        }
    }


}
