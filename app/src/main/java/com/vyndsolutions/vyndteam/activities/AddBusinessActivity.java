package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import com.vyndsolutions.vyndteam.adapters.AddSubCategoriesAdapter;
import com.vyndsolutions.vyndteam.adapters.RegionAdapter;
import com.vyndsolutions.vyndteam.adapters.SubCategoryAdapter;

import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.factories.SubCategoriesServiceFactory;

import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.GeneralInfo;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.models.MenuType;
import com.vyndsolutions.vyndteam.models.Region;
import com.vyndsolutions.vyndteam.utils.CompteManagerService;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.DialogFactoryUtils;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;
import com.vyndsolutions.vyndteam.widgets.images.ui.CustomToast;
import com.yalantis.ucrop.UCrop;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hoda on 22/03/2018.
 */

public class AddBusinessActivity extends AppCompatActivity implements ImagePickerCallback, SubCategoryAdapter.OnItemAddClickListener
        , View.OnClickListener {

    public static EditText ajoutNomCommerce;
    public static boolean isModifiable = false;
    public TextView creer;
    public TextView et_categories;
    public static TextView etRegion;
    private List<Region> regions = new ArrayList<>();
    private View layoutRegion;
    private boolean addCoverPhoto = false, addProfilPhoto;
    private ImagePicker imagePicker;
    private String[] PERMISSIONS_PHOTO = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    TextView lblCouverture, lblProfile;
    private ImageGalerie imgProfile;
    private ImageGalerie imgCover;
    List<Classification> subCategorieDesfault = new ArrayList<>();
    private List<Classification> selectedSearchTags = new ArrayList<>();
    private List<Classification> subcategories = new ArrayList<>();
    private Classification[] selectedRegionsList;
    private HashMap<Integer, Classification> selectedSubCategoryMap = new HashMap<>(), selectedSubCategoryMap2 = new HashMap<>();
    private CameraImagePicker cameraPicker;
    ImageView warningInfos;
    private String pickerPath;
    private Uri croppedImage;
    private SubCategoryAdapter.OnItemAddClickListener listener;
    public static LinearLayout llAddCategories;
    ArrayList<ImageGalerie> imagesGallerie;
    RelativeLayout rlSubcategories;
    public static RecyclerView listCategories, listRegions;
    public static Region selectedRegion = new Region();
    public static boolean isSelectedRegion = false;
    LinearLayout llHoraires, informations, llProfilPhoto, llCoverPhoto, llGallerie;
    RelativeLayout adressLayout;
    ImageView icGallerie, warningMenu;
    GeneralInfo generalInfo = new GeneralInfo();
    ImageView ivNom, ivPhotoCoverIcon;
    public static ImageView ivCategorie, ivRegion;

    ImageView ivReturnIcon, warningAddress, ivPhotoProfilIcon, ivWarningHoraires;
    private Business business = new Business();
    RefreshReceiverFromInformations refreshReceiverFromInformations;
    RefreshReceiverFromHoraires refreshReceiverFromHoraires;
    RefreshReceiverFromAddress refreshReceiverFromAddress;
    RefreshReceiverFromImage refreshReceiverFromImage;
    NetworkImageView ivProfilPhoto, ivPhotoCover;
    Business businessInfos = new Business();
    Business businessAdd = new Business();
    Business businessHor = new Business();
    Business createdBusiness = new Business(), modifBusiness;
    TextView nbrePhoto;
    MultipartBody.Part bodyCoverPhoto, bodyProfilPhoto;
    MultipartBody.Part[] arrayPhotosGallerie;
    private EditText description, codePostal, tel, etBeerPrice;
    private LinearLayout layoutMenu;
    RecyclerView rvAddCategories;
    private TextView categorie;
    private Region regionToSend;
    private ArrayList<Region> displayedRegions = new ArrayList<>();
    private NetworkImageView icPrice;
    private Business businessImage;
    private ArrayList<ImageGalerie> img = new ArrayList<>();
    private ArrayList<ImageGalerie> imgMenu = new ArrayList<>();
    private ArrayList<ImageGalerie> imageGaleriesMenu = new ArrayList<>();
    private List<MenuType> menuTypes = new ArrayList<>();
    private Business businessReady = new Business();
    private LoadToast loadToast;
    private ProgressBar progressBar;
    private View layoutLoading;
    private View layoutHappyHour;
    private NetworkImageView icHappyHour;
    private Business businessHappyHour;
    private RefreshReceiverFromHappyHour refreshReceiverFromHappyHour;
    private View layoutCommerceBeerPrice;


    private void initiTextListener() {
        ajoutNomCommerce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    ivNom.setImageResource(R.drawable.text_warning_icon);
                } else {
                    ivNom.setImageResource(R.drawable.tick_green);
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
                if (s.toString().equalsIgnoreCase("")) {
                    ivRegion.setImageResource(R.drawable.text_warning_icon);
                } else {
                    ivRegion.setImageResource(R.drawable.tick_green);
                }
            }
        });
        et_categories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    ivCategorie.setImageResource(R.drawable.text_warning_icon);
                } else {
                    ivCategorie.setImageResource(R.drawable.tick_green);
                }
            }
        });
        etBeerPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    icPrice.setImageResource(R.drawable.text_warning_icon);
                } else {
                    icPrice.setImageResource(R.drawable.tick_green);
                }
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        creer = findViewById(R.id.creer);
        menuTypes = new ArrayList<>();
        imageGaleriesMenu = new ArrayList<>();
        loadToast = new LoadToast(AddBusinessActivity.this);
        if (getIntent().getSerializableExtra("modifBusiness") != null) {
            Log.e("get intent value ", getIntent() + " !");
            modifBusiness = (Business) getIntent().getSerializableExtra("modifBusiness");

            creer.setText("Modifier");
        } else {
            creer.setText("créer");
        }
        layoutLoading = findViewById(R.id.layout_loading);
        progressBar = findViewById(R.id.progressBar);
        layoutCommerceBeerPrice = findViewById(R.id.layout_commerce_beerprice);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        icGallerie = findViewById(R.id.ic_gallerie);
        warningMenu = findViewById(R.id.warning_menu);
        layoutHappyHour = findViewById(R.id.layout_happy_hour);
        icHappyHour = findViewById(R.id.ic_happy_hour);
        initialiseViews();
        getAllSubCategories();
        getAllRegions();
        initiTextListener();
        //getGalleriePhotos();
        //  AjoutCommerce();
        /// browseCategoriesList();
        browseRegionsList();
        goToClickableActivity();
    }

    /* private void getGalleriePhotos() {
             Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().getImagesFromId(business.getId(), 0, Utils.LIMIT);
             call.enqueue(new Callback<ResponseBody>() {
                 @Override

                 public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                     try {
                         int code = response.code();
                         if (code == 200) {
                         Gson gson = new Gson();
                         JSONObject jObject = new JSONObject(response.body().string());
                         JSONArray jArray = jObject.getJSONArray("items");
                          imagesGallerie = new ArrayList<ImageGalerie>();
                         for (int i = 0; i < jArray.length(); i++) {
                             ImageGalerie imageGalerie = gson.fromJson(jArray.getJSONObject(i).toString() + "", ImageGalerie.class);
                             imagesGallerie.add(imageGalerie);

                         }


                         } else {
                             imagesGallerie = new ArrayList<ImageGalerie>();

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
     }*/
    private void setUpBusinessObject() {
//        if (AdressActivity.hasAddress == true ){
//        Intent intent = getIntent();
//        business = (Business) intent.getSerializableExtra("bunsinessAddress");
//        if (business.getAdress()!= null && business.getLatitude()!= null && business.getLongitude() != null) {
//            warningAddress.setImageResource(R.drawable.check);
//        }
//        }
    }

    private void browseRegionsList() {


        etRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isSelectedRegion) {
                    business.setRegion(selectedRegion);

                }

            }
        });

    }



    private void initialiseViews() {
        listener = this;
        rlSubcategories = findViewById(R.id.rl_subcategories);
        rvAddCategories = findViewById(R.id.rv_add_categories);
        llAddCategories = findViewById(R.id.ll_add_categories);
        layoutMenu = findViewById(R.id.layout_menu);
        nbrePhoto = findViewById(R.id.nbre_photo);
        tel = findViewById(R.id.tel);
        lblCouverture = findViewById(R.id.lbl_couverture);
        lblProfile = findViewById(R.id.lbl_profile);
        layoutRegion = findViewById(R.id.layout_region);
        codePostal = findViewById(R.id.code_postal);
        etBeerPrice = findViewById(R.id.et_beer_price);
        description = findViewById(R.id.description);
        icPrice = findViewById(R.id.ic_price);
        warningInfos = findViewById(R.id.warning_infos);
        llGallerie = findViewById(R.id.layout_gallerie);
        llCoverPhoto = findViewById(R.id.layout_cover_photo);
        ivPhotoCoverIcon = findViewById(R.id.iv_photo_cover_icon);
        ivPhotoCover = findViewById(R.id.iv_photo_cover);
        ivPhotoProfilIcon = findViewById(R.id.iv_photo_profil_icon);
        ivProfilPhoto = findViewById(R.id.iv_profil_photo);
        llProfilPhoto = findViewById(R.id.layout_profil_photo);
        warningAddress = findViewById(R.id.warning_address);
        adressLayout = findViewById(R.id.adress_layout);
        ivRegion = findViewById(R.id.iv_region);
        ajoutNomCommerce = findViewById(R.id.ajout_nom_commerce);
        ivCategorie = findViewById(R.id.iv_categorie);
        informations = findViewById(R.id.informations);
        et_categories = findViewById(R.id.et_categories);
        listCategories = findViewById(R.id.list_categories);
        listRegions = findViewById(R.id.list_region);
        llHoraires = findViewById(R.id.ll_horaires);
        ivReturnIcon = findViewById(R.id.icon_return);
        ivNom = findViewById(R.id.iv_nom);
        categorie = findViewById(R.id.categorie);
        etRegion = findViewById(R.id.et_region);
        ivWarningHoraires = findViewById(R.id.warning_horaires);
        rlSubcategories.setOnClickListener(this);
        layoutRegion.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        refreshBusinessData();


        super.onRestart();
    }



    private void goToClickableActivity() {
        layoutHappyHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("business", business);
                Intent intent = new Intent(AddBusinessActivity.this, HappyHourActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("business", business);
                Intent intent = new Intent(AddBusinessActivity.this, MenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        llGallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("business", business);
                Intent intent = new Intent(AddBusinessActivity.this, PhotosGridActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // refreshBusinessData();
                try {
                    if (ajoutNomCommerce.getText().toString().equalsIgnoreCase("") || etRegion.getText().toString()
                            .equalsIgnoreCase("") || et_categories.getText().toString().equalsIgnoreCase("")
                            || business.getAdress().equalsIgnoreCase("") || business.getLatitude().equalsIgnoreCase("")
                            || business.getLongitude().equalsIgnoreCase("")) {

                        new CustomToast(AddBusinessActivity.this, getResources().getString(R.string.erreur), getResources().getString(R.string.verify_info), R.drawable.ic_erreur, CustomToast.ERROR).show();
                        //loadToast.error();
                    } else {
                        addialog();
                    }
                } catch (NullPointerException e) {
                    new CustomToast(AddBusinessActivity.this, getResources().getString(R.string.erreur), getResources().getString(R.string.verify_info), R.drawable.ic_erreur, CustomToast.ERROR).show();
                }


            }
        });

        llProfilPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(AddBusinessActivity.this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                    addProfilPhoto = true;
                    addCoverPhoto = false;
                    uploadImageDialog();
                }
            }
        });
        llCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(AddBusinessActivity.this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                    addCoverPhoto = true;
                    addProfilPhoto = false;
                    uploadImageDialog();
                }

            }
        });


        adressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (isModifiable) {
                    bundle.putSerializable("business", modifBusiness);

                } else {
                    bundle.putSerializable("business", business);

                }

                Intent intent = new Intent(AddBusinessActivity.this, AdressActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ivReturnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_categories.getText().toString().equalsIgnoreCase("")
                        && ajoutNomCommerce.getText().toString().equalsIgnoreCase("")
                        && etRegion.getText().toString().equalsIgnoreCase("")
                        && Utils.adress.equalsIgnoreCase("") && Utils.menuTypes.size() == 0 && Utils.imageGaleriesMenu.size() == 0
                        && Utils.hourItem.size() == 0 && !addProfilPhoto && !addCoverPhoto) {
                    finish();

                }
                else
                showAlert();
            }
        });

        llHoraires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBusinessActivity.this, HoraireActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("business", business);

                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        informations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddBusinessActivity.this, InformationsActivity.class);
                refreshBusinessData();
                intent.putExtra("business", business);
                startActivity(intent);
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

                    if (code != 200) {

                        System.out.println("erreur supp info");

                    } else {

                        System.out.println("good supp info");

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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


    private void prepareToSendBasic() {
        JsonObject postParams = new JsonObject();

        String nom = "", longitudeToSend = "", adresseToSend = "", latitudeToSend = "", telToSend = "", postalCodeToSend = "", noteForVynd = "", descriptionToSend = "";
        Region region = new Region();
        Double beerPrice = 0d;
        JsonArray subCategories = new JsonArray();
        try {
            latitudeToSend = String.valueOf(Utils.latitude);
            longitudeToSend = String.valueOf(Utils.longitude);
            adresseToSend = Utils.adress;


        } catch (NullPointerException e) {

        }
        if (selectedSearchTags.size() > 0) {
            subCategories = new JsonArray();
            for (int i = 0; i < selectedSearchTags.size(); i++) {
                subCategories.add(selectedSearchTags.get(i).getId());
            }

        } else {
        }
        try {
            if (!ajoutNomCommerce.getText().toString().equalsIgnoreCase("")) {
                nom = ajoutNomCommerce.getText().toString();

            } else {

            }
        } catch (NullPointerException e) {

        }
        try {
            if (!etBeerPrice.getText().toString().equalsIgnoreCase("")) {
                beerPrice = Double.parseDouble(etBeerPrice.getText().toString());

            } else {
                beerPrice = 0d;
            }
        } catch (NullPointerException e) {

        }
        try {
            if (!description.getText().toString().equalsIgnoreCase("")) {
                descriptionToSend = description.getText().toString();

            } else {
                descriptionToSend = "";
            }
        } catch (NullPointerException e) {
            descriptionToSend = "";
        }

        try {
            if (!tel.getText().toString().equalsIgnoreCase("")) {
                telToSend = tel.getText().toString();

            } else {
                telToSend = "";
            }
        } catch (NullPointerException e) {
            telToSend = "";
        }
        try {
            if (!codePostal.getText().toString().equalsIgnoreCase("")) {
                postalCodeToSend = codePostal.getText().toString();

            } else {
                postalCodeToSend = "";
            }
        } catch (NullPointerException e) {
            postalCodeToSend = "";
        }
        try {
            if (!etRegion.getText().toString().equalsIgnoreCase("")) {
                try {
                    if (regionToSend != null) {


                    } else {

                    }
                } catch (NullPointerException e) {

                }
            } else {

            }
        } catch (NullPointerException e) {

        }
        postParams.addProperty("name", nom);
        postParams.addProperty("description", descriptionToSend);
        postParams.addProperty("address", adresseToSend);
        if (regionToSend != null)
            postParams.addProperty("regionId", regionToSend.getId());
        else postParams.addProperty("regionId", 0);
        postParams.add("subCategories", subCategories);
        postParams.addProperty("latitude", latitudeToSend);
        postParams.addProperty("longitude", longitudeToSend);
        postParams.addProperty("beerPrice", beerPrice);
        postParams.addProperty("postalCode", postalCodeToSend);
        postParams.addProperty("tel", telToSend);
        postParams.addProperty("noteForVynd", "");

        sendDataToServer(postParams);


    }

    private void sendDataToServer(JsonObject postParams) {
        try {

            layoutLoading.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {

        }
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addBusiness(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject;
                try {

                    layoutLoading.setVisibility(View.GONE);
                } catch (NullPointerException e) {

                }
                if (response.code() != 200) {
                    new CustomToast(AddBusinessActivity.this, getResources().getString(R.string.erreur), getResources().getString(R.string.problem_add_profile), R.drawable.ic_erreur, CustomToast.ERROR).show();
                } else {
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        jsonObject = jObject.getJSONObject("item2");
                        Gson gson = Utils.getGsonInstance();
                        businessReady = gson.fromJson(jsonObject.toString(), Business.class);
                        Utils.adress = "";
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("businessFromAdd", businessReady);
                        Intent intent = new Intent(AddBusinessActivity.this, ItemBusinessActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                        prepareForUploadImageCover(businessReady);


                        prepareForUploadImageProfile(businessReady);


                        try {
                            if (Utils.HappyHour.size() != 0)
                                sendHourToService(Utils.HappyHour, businessReady);
                        } catch (NullPointerException e) {

                        }

                        // addAdditionalInfos(businessReady);

                        ///addGalleriePhotos(businessReady);
                        // addHoraires(businessReady);
                        if (img != null) {
                            if (img.size() != 0) {
                                addImagesList(businessReady);
                            }
                        }
                        try {
                            if (businessInfos != null) {
                                businessReady.setRecommendedFor(businessInfos.getRecommendedFor());
                                businessReady.setImportantInfo(businessInfos.getImportantInfo());
                                businessReady.setGoodFor(businessInfos.getGoodFor());
                                businessReady.setPrincipalInfo(businessInfos.getPrincipalInfo());
                                preapreForEditAddInfo(businessReady);
                            }


                        } catch (NullPointerException e) {

                        }
                        if (Utils.hourItem != null) {
                            if (Utils.hourItem.size() != 0) {
                                sendHourToService(businessReady, Utils.hourItem);
                            }
                        }
                        if (Utils.menuTypes != null) {
                            if (Utils.menuTypes.size() != 0) {
                                sendBusinessMenu(businessReady);
                            }
                        }
                        if (imgMenu != null) {
                            if (imgMenu.size() != 0) {

                                addImagesMenuList(businessReady);
                            }
                        }
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                        try {

                            layoutLoading.setVisibility(View.GONE);
                        } catch (NullPointerException p) {

                        }
                    } catch (IOException p) {
                        try {

                            layoutLoading.setVisibility(View.GONE);
                        } catch (NullPointerException e) {

                        }
                        p.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {

                    layoutLoading.setVisibility(View.GONE);
                    new CustomToast(AddBusinessActivity.this, getResources().getString(R.string.erreur), getResources().getString(R.string.problem_add_profile), R.drawable.ic_erreur, CustomToast.ERROR).show();
                } catch (NullPointerException e) {

                }
            }
        });
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

                        if (getBaseContext() != null) {
                            if (code != 200) {

                            } else {

                                Utils.imageGaleriesMenu = new ArrayList<>();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
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

    private void prepareForUploadImageCover(Business business) {
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


                            } else {

                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();


                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

        } catch (NullPointerException e) {

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


            }
        });
    }

    private void prepareForUploadImageProfile(Business businessReady) {

        try {
            if (imgProfile != null) {
                File file = new File(imgProfile.getThumbnailPath());
                RequestBody reqFile = RequestBody.create(
                        MediaType.parse("image/*"), file
                );
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), reqFile);

                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().updateProfileImage(businessReady.getId(), body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {

                            if (response.code() == 200) {
                                try {

                                } catch (NullPointerException e) {

                                }


                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Log.i("Upload error :", t.getMessage());


                    }
                });
            }

        } catch (NullPointerException e) {
            System.out.println("null");
        }
        //}
    }

    private void updateImage() {

        if (addProfilPhoto) {
            try {
                imgProfile = new ImageGalerie(0, croppedImage.getPath(), croppedImage.getPath(), true);
                ivProfilPhoto.setImageUriWithoutProgress(imgProfile.getImage(), this);
                ivPhotoProfilIcon.setImageResource(R.drawable.tick_green);
                lblProfile.setTextColor(getResources().getColor(R.color.black));
                try {
                    business.setImage(imgProfile.getImage());
                    businessReady.setImage(imgProfile.getImage());

                }catch (NullPointerException e)
                {

                }

            } catch (NullPointerException e) {
                System.out.println("null");
            }
        } else if (addCoverPhoto) {
            try {
                imgCover = new ImageGalerie(0, croppedImage.getPath(), croppedImage.getPath(), true);
                ivPhotoCover.setImageUriWithoutProgress(imgCover.getImage(), this);
                ivPhotoCoverIcon.setImageResource(R.drawable.tick_green);
                lblCouverture.setTextColor(getResources().getColor(R.color.black));
                try {
                    business.setCoverImg(imgCover);
                    businessReady.setCoverImg(imgCover);
                }catch (NullPointerException e)
                {

                }


            } catch (NullPointerException e) {
                System.out.println("null");
            }
        }


    }

    private void addialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddBusinessActivity.this, R.style.AlertDialogCustom);

        builder.setTitle("Enregistrer un commerce");
        builder.setMessage("Voulez-vous enregistrer ce commerce?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                prepareToSendBasic();

            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // gridviewPhoto.setVisibility(View.VISIBLE);
            }
        });
    }

    private void uploadImageDialog() {
        //gridviewPhoto.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddBusinessActivity.this, R.style.AlertDialogCustom);

        builder.setTitle("Ajouter une photo");
        builder.setMessage("Prenez une photo ou bien sélectionnez depuis vos photos");
        builder.setPositiveButton("GALLERIE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromGallerie();
                dialog.dismiss();
                //gridviewPhoto.setVisibility(View.VISIBLE);

            }
        });

        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromCamera();
                dialog.dismiss();
                //gridviewPhoto.setVisibility(View.VISIBLE);
            }
        });


        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // gridviewPhoto.setVisibility(View.VISIBLE);
            }
        });
    }

    private void imageFromCamera() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }

    private void imageFromGallerie() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(AddBusinessActivity.this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private void refreshBusinessData() {
        if (businessHappyHour != null) {
            try {
                int j = 0;
                business.setHours(businessHappyHour.getHours());
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

        try {
            if (Utils.menuTypes.size() != 0 || Utils.imageGaleriesMenu.size() != 0) {
                warningMenu.setImageResource(R.drawable.tick_green);
            } else warningMenu.setImageResource(R.drawable.text_warning_icon);
        } catch (NullPointerException e) {

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
                    business.setImages(businessImage.getImages());
                    businessReady.setImages(businessImage.getImages());
                } catch (NullPointerException e) {

                }
                if (img.size() == 0) {
                    icGallerie.setImageResource(R.drawable.text_warning_icon);
                } else icGallerie.setImageResource(R.drawable.tick_green);
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());

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
                if (businessReady.getGoodFor().size() != 0 || businessReady.getImportantInfo().size() != 0
                        || businessReady.getPrincipalInfo().size() != 0 || businessReady.getRecommendedFor().size() != 0) {
                    warningInfos.setImageResource(R.drawable.tick_green);
                } else {
                    warningInfos.setImageResource(R.drawable.text_warning_icon);
                }


            } catch (NullPointerException e) {
                warningInfos.setImageResource(R.drawable.text_warning_icon);
            }
        }
        if (businessAdd != null) {
            business.setLatitude(businessAdd.getLatitude());
            business.setLongitude(businessAdd.getLongitude());
            business.setAdress(businessAdd.getAdress());
        }
        if (!Utils.adress.equalsIgnoreCase("")) {
            warningAddress.setImageResource(R.drawable.tick_green);
        } else {
            warningAddress.setImageResource(R.drawable.text_warning_icon);
        }
        if (Utils.hourItem != null) {
            if (Utils.hourItem.size() != 0) {
                ivWarningHoraires.setImageResource(R.drawable.tick_green);
            } else ivWarningHoraires.setImageResource(R.drawable.text_warning_icon);
        }
        imgMenu = new ArrayList<>();
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

    @Override
    protected void onResume() {
        super.onResume();
        refreshReceiverFromInformations = new RefreshReceiverFromInformations();
        refreshReceiverFromHoraires = new RefreshReceiverFromHoraires();
        refreshReceiverFromAddress = new RefreshReceiverFromAddress();
        refreshReceiverFromImage = new RefreshReceiverFromImage();
        refreshReceiverFromHappyHour = new RefreshReceiverFromHappyHour();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatusVariables();
        if (refreshReceiverFromInformations != null && AddBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromInformations);

            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromHoraires != null && AddBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromHoraires);
            } catch (IllegalArgumentException e) {
            }
        } else if (refreshReceiverFromAddress != null && AddBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromAddress);
            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromImage != null && AddBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromImage);
            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromHappyHour != null && AddBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromHappyHour);
            } catch (IllegalArgumentException e) {

            }
        } else {

        }

    }

    private void showAlert() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();


        dialogBuilder.setTitle(getResources().getString(R.string.quitter_withour_save) +
                "\n");
        dialogBuilder.setMessage("Êtes vous sur de vouloir quitter la page sans sauvegarder vos changements ?\n" +
                "\n" +
                "Tous les changements que vous avez faits seront perdus.");
        dialogBuilder.setPositiveButton("QUITTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Utils.imageGaleriesMenu = new ArrayList<>();
                Utils.menuTypes = new ArrayList<>();
                Utils.hourItem = new JsonArray();
                finish();


            }
        });
        dialogBuilder.setNegativeButton("RESTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_3));
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



    @Override
    public void onError(String s) {

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
    public void onItemAddClickListener(View v) {
        if (subcategories.size() != 0) {
            ivCategorie.setImageResource(R.drawable.check);
            et_categories.setVisibility(View.GONE);
            llAddCategories.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvAddCategories.setLayoutManager(linearLayoutManager);
            AddSubCategoriesAdapter addSubCategoriesAdapter = new AddSubCategoriesAdapter(getBaseContext(), subcategories);
            rvAddCategories.setAdapter(addSubCategoriesAdapter);

        } else {
            et_categories.setVisibility(View.VISIBLE);
            et_categories.setHint("Choisir des catégories");
            et_categories.setHintTextColor(getResources().getColor(R.color.gray_2));
            llAddCategories.setVisibility(View.GONE);
            ivCategorie.setImageResource(R.drawable.text_warning_icon);


        }

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
    public void onClick(View v) {
        if (v == layoutRegion) {
            final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
            dialogFactoryUtils.showRegionListDialog(this);
            dialogFactoryUtils.setOnConfirmRegionSelectListener(new DialogFactoryUtils.OnConfirmRegionSelect() {
                @Override
                public void OnConfirmRegionSelectListener(Region region) {
                    try {

                        regionToSend = region;
                        etRegion.setText(region.getLabel());

                    } catch (NullPointerException e) {

                        etRegion.setText("");
                    }
                }
            });
        }
        if (v == rlSubcategories) {
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

                    if (categoriesNameToSet.indexOf(bar) != -1) {
                        System.out.println("mriguel");
                        etBeerPrice.setEnabled(true);
                        layoutCommerceBeerPrice.setVisibility(View.VISIBLE);
                        try {
                            if (business.getBeerPrice() != 0) {
                                etBeerPrice.setText(String.valueOf(business.getBeerPrice()));
                                etBeerPrice.setEnabled(true);
                            }
                        } catch (NullPointerException e) {
                            etBeerPrice.setText("0");
                        }

                    } else {
                        System.out.println("moch mriguel");
                        etBeerPrice.setEnabled(false);
                        layoutCommerceBeerPrice.setVisibility(View.GONE);
                        etBeerPrice.setText("");

                    }
                    et_categories.setText(categoriesNameToSet);


                }
            });
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (et_categories.getText().toString().equalsIgnoreCase("")
                && ajoutNomCommerce.getText().toString().equalsIgnoreCase("")
                && etRegion.getText().toString().equalsIgnoreCase("")
                && Utils.adress.equalsIgnoreCase("") && Utils.menuTypes.size() == 0 && Utils.imageGaleriesMenu.size() == 0
                && Utils.hourItem.size() == 0 && !addProfilPhoto && !addCoverPhoto) {
            super.onBackPressed();

        } else
            showAlert();
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


    private void sendHourToService(JsonArray hourItem, Business business) {

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
                    progressBar.setVisibility(View.GONE);
                    System.out.println(t.getMessage());
                }
            }
        });
    }


    private void setStatusVariables() {
        if (Utils.isAdded) {
            Utils.isAdded = false;
        }
        if (Utils.isModifiable) {
            Utils.isModifiable = false;
        }
    }


}
