package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.BusinessHappyHoursAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.Day;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.TextStyle.LatoTextView;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;
import com.yalantis.ucrop.UCrop;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemBusinessActivity extends AppCompatActivity implements ImagePickerCallback {

    Business business;
    RelativeLayout adressLayout;
    ImagePicker imagePicker;
    ImageView ivwarningMenu;
    MultipartBody.Part bodyCoverPhoto;
    ImageView warningAdressItem;
    CameraImagePicker cameraPicker;
    ImageView returnIcon;
    LinearLayout informations;
    ImageView warningInfosItem;
    NetworkImageView coverImage, imageProfil, imageProfilVisible;
    LinearLayout addCoverPhoto, valide, nonValide;
    EditText tel, codePostal, description;
    TextView textToast;
    TextView name, subcategory, region, address, nombrePhoto;
    ImageView moreItemBusiness;
    RecyclerView rvHappyHours;
    ArrayList<Day> days = new ArrayList<Day>();
    private String pickerPath;
    private Uri croppedImage;
    private View layoutValiderNon;
    private String[] PERMISSIONS_PHOTO = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private View layoutBusinessProfileContent, layoutBusinessProfileLoading, layoutHappyHours;
    private LoadToast loadToast;
    private View layoutPhotoGallerie;
    private Business businessAdd;
    private RefreshReceiverFromAddress refreshReceiverFromAddress;
    private View ivClaimed;
    private View llHoraire;
    private View layoutMenu;
    private Business businessInfos;
    private RefreshReceiverFromInformations refreshReceiverFromInformations;
    private Business businessImage;
    private RefreshReceiverFromImage refreshReceiverFromImage;
    private LatoTextView tvNbrView;
    private ImageView ivWarningHoraire;
    private ImageGalerie imgCover;

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
    protected void onDestroy() {
        super.onDestroy();
        if (refreshReceiverFromAddress != null && ItemBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromAddress);
            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromAddress != null && ItemBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromAddress);
            } catch (IllegalArgumentException e) {

            }
        } else if (refreshReceiverFromImage != null && ItemBusinessActivity.this != null) {
            try {
                getApplicationContext().unregisterReceiver(refreshReceiverFromImage);
            } catch (IllegalArgumentException e) {

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshBusinessData();

    }

    private void refreshBusinessData() {
        if (!business.isHasMenu()) {
            if (Utils.menuTypes.size() != 0 || Utils.imageGaleriesMenu.size() != 0)
                ivwarningMenu.setImageResource(R.drawable.tick_green);

            else ivwarningMenu.setImageResource(R.drawable.text_warning_icon);
        }
        if (business.getWorkingHours().size() != 0) {
            int j = 0;
            for (int i = 0; i < business.getWorkingHours().size(); i++) {
                if (!business.getWorkingHours().get(i).isClosed())
                    j++;
            }
            if (j == 0) {
                if (Utils.hourItem.size() != 0) {
                    ivWarningHoraire.setImageResource(R.drawable.tick_green);
                } else {
                    ivWarningHoraire.setImageResource(R.drawable.text_warning_icon);
                }

            } else ivWarningHoraire.setImageResource(R.drawable.tick_green);
        }


        if (businessImage != null) {
            try {
                business.setImages(businessImage.getImages());
                nombrePhoto.setText(String.valueOf(business.getImages().size()));

            } catch (NullPointerException e) {
                System.out.println(e.getMessage());

            }
        }
        try {
            if (businessAdd != null) {
                business.setLatitude(businessAdd.getLatitude());
                business.setLongitude(businessAdd.getLongitude());
                business.setAdress(businessAdd.getAdress());
                Utils.longitude = Double.parseDouble(business.getLongitude());
                Utils.latitude = Double.parseDouble(business.getLatitude());
                Utils.adress = business.getAdress();
                warningAdressItem.setImageResource(R.drawable.tick_green);

            }

        } catch (NullPointerException e) {

        }
        try {
            if (businessInfos != null) {
                try {

                    business.setRecommendedFor(businessInfos.getRecommendedFor());
                    business.setGoodFor(businessInfos.getGoodFor());

                    business.setPrincipalInfo(businessInfos.getPrincipalInfo());

                    business.setImportantInfo(businessInfos.getImportantInfo());
                    if (business.getRecommendedFor().size() > 0 || business.getPrincipalInfo().size() > 0 || business.getImportantInfo().size() > 0
                            || business.getGoodFor().size() > 0) {
                        warningInfosItem.setImageResource(R.drawable.tick_green);
                    } else warningInfosItem.setImageResource(R.drawable.text_warning_icon);


                } catch (NullPointerException e) {

                }
            }


        } catch (NullPointerException e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshReceiverFromAddress = new RefreshReceiverFromAddress();
        IntentFilter intentFilterAdd = new IntentFilter("refresh_state_address");
        getApplicationContext().registerReceiver(refreshReceiverFromAddress, intentFilterAdd);
        refreshReceiverFromInformations = new RefreshReceiverFromInformations();
        IntentFilter intentFilter = new IntentFilter("refresh_state_informations");
        getApplicationContext().registerReceiver(refreshReceiverFromInformations, intentFilter);
        refreshReceiverFromImage = new RefreshReceiverFromImage();
        IntentFilter intentFilterImage = new IntentFilter("refresh_state_image");
        getApplicationContext().registerReceiver(refreshReceiverFromImage, intentFilterImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_business);
        Intent intent = getIntent();
        try {
            if (intent.getSerializableExtra("businessFromAdd") != null)
                business = (Business) intent.getSerializableExtra("businessFromAdd");
            else business = (Business) intent.getSerializableExtra("business");
        } catch (NullPointerException e) {
            business = (Business) intent.getSerializableExtra("business");
        }


        initialiseViews();
        getBusiness();
        setUpOnClickListeners();

    }


    private void getBusinessStat(final int businessID) {


        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                .getByIdStat(SharedPreferencesFactory.getAdminToken(getBaseContext()), businessID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());

                    if (response.code() == 200) {

                        if (jsonObject.length() == 0) {


                        } else {


                            business.setNbrViews(jsonObject.getInt("nbrViews"));
                            tvNbrView.setText(String.valueOf(business.getNbrViews()));


                            // setBusinessStatData();
                        }

                    } else {

                    }
                } catch (JSONException | IOException | NullPointerException e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    private void setImgGallerie() {
        if (SharedPreferencesFactory.getListPhotosGallerie(getBaseContext()) != null) {
            SharedPreferencesFactory.getListPhotosGallerie(getBaseContext()).clear();

        }
        if (business.getImages() != null) {
            SharedPreferencesFactory.saveListPhotosGallerie(getBaseContext(), business.getImages());
            nombrePhoto.setText(String.valueOf(business.getNbrImages()));
            if (business.getNbrImages() != 0) {
                nombrePhoto.setTextColor(getResources().getColor(R.color.green_color));
            }

            /*Intent intent = new Intent(this, PhotosGridActivity.class);
            intent.putExtra("isFromProfile", true);
            startActivity(intent);*/

        }
    }

    private void setAddress() {

        if ((business.getLongitude() != "" && business.getLatitude() != "") && (
                (!(Utils.longitude + "").equalsIgnoreCase(business.getLongitude())) && (!((Utils.latitude + "").equalsIgnoreCase(business.getLatitude()))))) {
            warningAdressItem.setImageResource(R.drawable.tick_green);


        }
    }

    private void setHappyHoursLayout() {
        if (business.getHasHappyHour() == 1) {
            layoutHappyHours.setVisibility(View.VISIBLE);
            rvHappyHours.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            BusinessHappyHoursAdapter businessHappyHoursHorizontalRecyclerViewAdapter = new BusinessHappyHoursAdapter(getBaseContext(), business.getHours());
            rvHappyHours.setAdapter(businessHappyHoursHorizontalRecyclerViewAdapter);

        } else {
            layoutHappyHours.setVisibility(View.GONE);

        }

    }

    private void initialiseViews() {

        ivClaimed = findViewById(R.id.iv_claimed);
        layoutBusinessProfileContent = findViewById(R.id.layout_business_profile_content);
        layoutBusinessProfileLoading = findViewById(R.id.layout_business_profile_loading);
        layoutHappyHours = findViewById(R.id.happy_hours);
        ivWarningHoraire = findViewById(R.id.iv_warning_horaire);
        layoutPhotoGallerie = findViewById(R.id.layout_photo_gallerie);
        layoutMenu = findViewById(R.id.layout_menu);
        tvNbrView = findViewById(R.id.tv_nbr_view);
        layoutValiderNon = findViewById(R.id.layout_valider_non);
        ivwarningMenu = findViewById(R.id.iv_warning_menu);
        layoutValiderNon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectivityService.isOnline(getBaseContext())) {
                    Snackbar snackbar = Snackbar
                            .make(layoutMenu, getResources().getString(R.string.hors_connexion_mess), Snackbar.LENGTH_SHORT);
                    View snackBarLayout = snackbar.getView();
                    TextView textView = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_red, 0);
                    // textView.setCompoundDrawablePadding(getResourc00es().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                    snackbar.show();
                } else {
                    validateBusiness(business);
                }
            }
        });
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemBusinessActivity.this, MenuActivity.class);
                intent.putExtra("isFromProfile", true);
                intent.putExtra("business", business);
                startActivity(intent);

            }
        });
        layoutPhotoGallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemBusinessActivity.this, PhotosGridActivity.class);
                intent.putExtra("isFromProfile", true);
                intent.putExtra("business", business);
                startActivity(intent);
            }
        });

        llHoraire = findViewById(R.id.ll_horaires);
        llHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemBusinessActivity.this, HoraireActivity.class);
                intent.putExtra("isFromProfile", true);
                intent.putExtra("business", business);
                startActivity(intent);
            }
        });
        informations = findViewById(R.id.informations);
        warningInfosItem = findViewById(R.id.warning_infos_item);
        nombrePhoto = findViewById(R.id.nombre_photo);
        adressLayout = findViewById(R.id.adress_layout);
        warningAdressItem = findViewById(R.id.warning_adress_item);
        moreItemBusiness = findViewById(R.id.more_item_business);
        tel = findViewById(R.id.tel);
        textToast = (TextView) findViewById(R.id.text_toast);
        subcategory = findViewById(R.id.item_subcategory);
        imageProfilVisible = findViewById(R.id.img_profil_visible);
        addCoverPhoto = findViewById(R.id.add_cover_photo);
        returnIcon = findViewById(R.id.item_profil_return_default);
        coverImage = findViewById(R.id.item_business_cover_photo);
        imageProfil = findViewById(R.id.img_profil);
        codePostal = findViewById(R.id.code_postal);
        description = findViewById(R.id.description);
        name = findViewById(R.id.nom_item);
        region = findViewById(R.id.item_region);
        address = findViewById(R.id.item_address);
        valide = findViewById(R.id.valide);
        nonValide = findViewById(R.id.non_valide);
        nombrePhoto = findViewById(R.id.nombre_photo);
        rvHappyHours = findViewById(R.id.rv_happy_hours);
        adressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemBusinessActivity.this, AdressActivity.class);
                intent.putExtra("isFromProfile", true);
                intent.putExtra("business", business);
                startActivity(intent);

            }
        });
        addCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(ItemBusinessActivity.this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                    uploadImageDialog();
                }
            }
        });
    }

    private void addialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ItemBusinessActivity.this, R.style.AlertDialogCustom);

        builder.setTitle("Supprimer un commerce");
        builder.setMessage("Voulez-vous vraiment supprimer ce commerce?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteItem();
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

    private void setUpOnClickListeners() {

        moreItemBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(ItemBusinessActivity.this, R.style.popup);
                PopupMenu popupMenu = new PopupMenu(wrapper, moreItemBusiness);

                popupMenu.getMenuInflater().inflate(R.menu.mod_del_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.del) {
                            addialog();
                        }
                        if (item.getItemId() == R.id.mod) {
                            Utils.isModifiable = true;
                            System.out.println(business.getId());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("modifBusiness", business);
                            //bundle.putBoolean("isFromProfile", true);
                            Intent intent = new Intent(ItemBusinessActivity.this, UpdateActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // resetGalleriePhotosList();
                try {
                    sendBasicData();
                    prepareForUploadImageCover();
                } catch (NullPointerException e) {

                }

                finish();
            }
        });

        informations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemBusinessActivity.this, InformationsActivity.class);
                intent.putExtra("business", business);
                intent.putExtra("isFromProfile", true);
                startActivity(intent);
            }
        });
    }

    private void deleteItem() {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().deleteBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId());
        Log.e("token", business.getId() + "   " + SharedPreferencesFactory.getAdminToken(getBaseContext()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.e("success delete", response.toString());
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast));

                    TextView text = (TextView) layout.findViewById(R.id.text_toast);
                    text.setText("Action effectuée avec succès !");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    finish();

                } else {
                    Log.e("error server", response.toString());
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast));

                    TextView text = (TextView) layout.findViewById(R.id.text_toast);
                    text.setText(" Réessayer une autre fois !");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void imageFromGallerie() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(ItemBusinessActivity.this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private void uploadImageDialog() {
        //gridviewPhoto.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ItemBusinessActivity.this, R.style.AlertDialogCustom);

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

    private void setItemAttributes(Business business) {
        try {
            if (!business.getCoverImg().getImage().equalsIgnoreCase("")) {
                addCoverPhoto.setVisibility(View.GONE);
                System.out.println(business.getCoverImg().getImage());
                coverImage.setImageUrlWithoutBorderWithoutResizing(business.getCoverImg().getImage());
            } else {
                addCoverPhoto.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            addCoverPhoto.setVisibility(View.VISIBLE);

        }


        if (business.getImage() != null) {
            imageProfil.setVisibility(View.GONE);
            imageProfilVisible.setVisibility(View.VISIBLE);
            imageProfilVisible.setImageUrlWithCustomBorderColor(business.getImg().getUrlxS(), "#D2D7DE", 15,
                    R.drawable.empty_business_photo);
        }
        if (business.getTel() != null) {
            tel.setText(business.getTel() + "");
        }


        if (business.getPostalCode() != 0) {
            codePostal.setText(business.getPostalCode() + "");
        }

        if (business.getDescription() != null) {
            description.setText(business.getDescription() + "");
        }
        if (business.getName() != null) {
            name.setText(business.getName() + "");
        }

        System.out.println("tel:" + business.getTel());
        System.out.println("code:" + business.getPostalCode());
        System.out.println("des:" + business.getDescription());
        try {
            if (business.getSubCategory() != null) {
                if (business.getSubCategory().length != 0) {
                    for (int i = 0; i < business.getSubCategory().length; i++) {
                        subcategory.setText(business.getSubCategory()[i].label + ".");
                    }
                }

            }
        } catch (NullPointerException e) {

        }

        try {

            if (business.getWorkingHours().size() != 0) {
                int j = 0;
                for (int i = 0; i < business.getWorkingHours().size(); i++) {
                    if (!business.getWorkingHours().get(i).isClosed())
                        j++;
                }
                if (j == 0)
                    ivWarningHoraire.setImageResource(R.drawable.text_warning_icon);
                else ivWarningHoraire.setImageResource(R.drawable.tick_green);
            }

        } catch (NullPointerException e) {

        }

        try {
            if (business.getClaimed()) {
                ivClaimed.setVisibility(View.VISIBLE);
            } else ivClaimed.setVisibility(View.GONE);
        } catch (NullPointerException e) {

        }

        try {
            if (business.getRegion() != null) {
                if (business.getRegion().getLabel() == "") {
                    region.setText("Tunis" + "");
                } else {
                    region.setText(business.getRegion().getLabel() + "");
                }

            } else {
                region.setText("Tunis" + "");
            }
        } catch (NullPointerException e) {

        }
        try {
            if (business.getAdress() != null) {
                if (!business.getAdress().equalsIgnoreCase("")) {
                    address.setText(business.getAdress() + "");
                } else {
                    address.setText("Grand Tunis, Tunisie" + "");
                }

            } else {
                address.setText("Grand Tunis, Tunisie" + "");
            }
        } catch (NullPointerException e) {

        }

        try {
            if (business.isValid()) {
                valide.setVisibility(View.VISIBLE);
                nonValide.setVisibility(View.GONE);
                layoutValiderNon.setVisibility(View.GONE);
            } else {
                valide.setVisibility(View.GONE);
                nonValide.setVisibility(View.VISIBLE);
                layoutValiderNon.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {

        }

        try {
            if (business.getNbrImages() != 0) {
                nombrePhoto.setText(business.getNbrImages() + "");
            }
        } catch (NullPointerException e) {

        }


        try {
            if (business.isHasMenu()) {
                ivwarningMenu.setImageResource(R.drawable.tick_green);
            } else ivwarningMenu.setImageResource(R.drawable.text_warning_icon);
        } catch (NullPointerException e) {

        }

        try {
            if (business.getRecommendedFor().size() != 0 || business.getGoodFor().size() != 0 ||
                    business.getImportantInfo().size() != 0 || business.getPrincipalInfo().size() != 0) {
                warningInfosItem.setImageResource(R.drawable.tick_green);
            } else warningInfosItem.setImageResource(R.drawable.text_warning_icon);


        } catch (NullPointerException e) {

        }


    }

    private void validateBusiness(Business business) {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().validateBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId());

        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();

                if (getBaseContext() != null) {
                    if (code != 200) {


                    } else {

                        layoutValiderNon.setVisibility(View.GONE);
                        nonValide.setVisibility(View.GONE);
                        valide.setVisibility(View.VISIBLE);
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

    private void updateImage() {
        imgCover = new ImageGalerie(0, croppedImage.getPath(), croppedImage.getPath(), true);
        coverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        coverImage.setImageUriWithoutProgress(imgCover.getImage(), getBaseContext());
        addCoverPhoto.setVisibility(View.GONE);


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
                                    // layoutLoading.setVisibility(View.GONE);
                                }

                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            // layoutLoading.setVisibility(View.GONE);
                        }
                        // layoutLoading.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // layoutLoading.setVisibility(View.GONE);
                    }
                });
            }

        } catch (NullPointerException e) {
            // layoutLoading.setVisibility(View.GONE);
        }
    }


    private void resetGalleriePhotosList() {
        if (SharedPreferencesFactory.getListPhotosGallerie(getBaseContext()) != null) {
            SharedPreferencesFactory.saveListPhotosGallerie(getBaseContext(), null);
        }

    }

    @Override
    public void onBackPressed() {
        //  resetGalleriePhotosList();
        try {
            sendBasicData();
            prepareForUploadImageCover();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        finish();
    }

    private void sendBasicData() {
        JsonObject postParam = new JsonObject();
        try {
            postParam.addProperty("name", business.getName());
        } catch (NullPointerException e) {
            postParam.addProperty("name", "");
        }
        try {
            postParam.addProperty("description", description.getText().toString());
        } catch (NullPointerException e) {
            postParam.addProperty("description", "");
        }
        try {
            postParam.addProperty("address", business.getAdress());
        } catch (NullPointerException e) {
            postParam.addProperty("address", "");
        }
        try {
            postParam.addProperty("regionId", business.getRegion().getId());
        } catch (NullPointerException e) {
            postParam.addProperty("regionId", 0);
        }
        try {
            postParam.addProperty("latitude", business.getLatitude());
        } catch (NullPointerException e) {
            postParam.addProperty("latitude", "");
        }

        try {
            postParam.addProperty("longitude", business.getLongitude());
        } catch (NullPointerException e) {
            postParam.addProperty("longitude", "");
        }

        try {
            postParam.addProperty("tel", tel.getText().toString());
        } catch (NullPointerException e) {
            postParam.addProperty("tel", "");
        }

        try {
            postParam.addProperty("postalCode", codePostal.getText().toString());
        } catch (NullPointerException e) {
            postParam.addProperty("postalCode", "");
        }

        sendDataToServer(postParam);
    }

    private void sendDataToServer(JsonObject postParams) {
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().editBasicBusiness(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject;
                System.out.println(response.code());
                System.out.println(response.message());

                if (response.code() != 200) {
                    ///Error adding Business
                } else {
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        jsonObject = jObject.getJSONObject("item2");
                        Gson gson = Utils.getGsonInstance();
                        business = gson.fromJson(jsonObject.toString(), Business.class);
                        System.out.println(business.getTel());
                        System.out.println(business.getDescription());


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                        System.out.println(e.getMessage());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                System.out.println(t.getMessage());
            }
        });
    }

    public void getBusiness() {
        if (ConnectivityService.isOnline(this)) {
            Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().getBusinessById(business.getId());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(response.code());
                    System.out.println(response.message());
                    int code = response.code();
                    if (code == 200) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());

                            Gson gson = Utils.getGsonInstance();
                            business = gson.fromJson(jsonObject.getJSONObject("item2").toString(), Business.class);
                            getBusinessStat(business.getId());
                            setItemAttributes(business);
                            setHappyHoursLayout();
                            setAddress();

                            setImgGallerie();
                            layoutBusinessProfileContent.setVisibility(View.VISIBLE);
                            layoutBusinessProfileLoading.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println(t.getMessage());

                }
            });
        }
    }

    public class RefreshReceiverFromAddress extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            businessAdd = (Business) intent.getSerializableExtra("businessAddress");

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
}