package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.util.UniversalTimeScale;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
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
import com.vyndsolutions.vyndteam.adapters.MenuBusinessAdapter;
import com.vyndsolutions.vyndteam.adapters.MenuPhotoAdapter;
import com.vyndsolutions.vyndteam.adapters.MenuPhotoAdapterr;
import com.vyndsolutions.vyndteam.adapters.ViewPagerAdapter;
import com.vyndsolutions.vyndteam.adapters.ViewPagerMenuAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.models.MenuProduct;
import com.vyndsolutions.vyndteam.models.MenuType;
import com.vyndsolutions.vyndteam.utils.ConnectivityService;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
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
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerCallback {

    private ProgressDialog mProgressDialog;
    static RelativeLayout layoutShadowImage;
    LinearLayout lnMenu;
    ViewPagerMenuAdapter viewPagerAdapter;
    ViewPager viewPagerPhoto;
    LinearLayout layoutEmtyMenu;
    RelativeLayout rlLayoutAddPhoto;
    private Uri croppedImage;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    TabLayout tabDots;
    View view, footerView, menuPhotoView;
    String labelProduct = "", descriptionProduct = "", categorieName = "", descriptionCategorie = "";
    Float priceProduct = 0f;
    int idCategorie = 0;
    int testEdit=0;
    EditText etProductName, etProductPrice, etCategorieName;
    private String[] PERMISSIONS_PHOTO = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    LinearLayout layoutPhotoMenu;
    RelativeLayout rlTxtMenu, rlPhotoMenu, layoutExpandMenu, layoutAddPhotoMenu, relativeLayout;
    View viewText, viewPhoto;
    Animation animation;
    NetworkImageView ivBack, ivEdit, ivDeleteImage, ivBackUpdate;
    TextView tvSave;
    MenuPhotoAdapterr menuPhotoAdapter;
    LinearLayout layoutEmtyMenuPhoto;
    TextView lblTxtMenu, lblTxtPhoto;
    Typeface LatoRegular, LatoBold;
    private List<ImageGalerie> imageGaleries = new ArrayList<>();
    MenuType menuType;
    List<MenuProduct> menuProducts = new ArrayList<>();
    RelativeLayout layoutAddCategorie;
    Business business;
    public ExpandableListView lvExpMenu;
    MenuBusinessAdapter menuBusinessAdapter;
    public GridView gridviewPhoto;
    private int indexPhoto;
    public boolean isActivated = false;
    Bundle bundle;
    private ImageGalerie imageGalerie;
    List<MenuType> menuTypes = new ArrayList<>();
    private List<ImageGalerie> imageGaleriesMenu = new ArrayList<>();
    private ImageGalerie imageGalerieMenu;
    Toolbar toolbarUpdate, toolbar;
    View layoutNoInternetConnection;
    private View rlResseayer;
    public ProgressBar progressBarPhoto;
    public ProgressBar progressBarItem;
    public ProgressBar progressBarMenu;
    private View layoutContentMenu, layoutNoInternetMenu;
    private int type = 0;
    private View layoutMenu;
    private boolean isFromProfile = false;
    private ArrayList<ImageGalerie> imgMenu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = vi.inflate(R.layout.footer_view_add_categorie, null, false);
        try {
            business = (Business) getIntent().getSerializableExtra("business");

            try {
                isFromProfile = getIntent().getBooleanExtra("isFromProfile", false);
            } catch (NullPointerException e) {

            }
        } catch (NullPointerException e) {

        }
        initialize();
    }


    public void showAddProductDialog(final MenuType menuType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_product_dialog, null);
        dialogBuilder.setView(dialogView);

        etProductName = dialogView.findViewById(R.id.et_product_name);
        etProductPrice = dialogView.findViewById(R.id.et_product_price);

        dialogBuilder.setTitle(" Ajouter" +
                "\n");
        dialogBuilder.setPositiveButton("CONFIRMER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(etProductPrice.getText()!=null)
                addProduct(menuType, dialog);



            }
        });
        dialogBuilder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_2));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.trackColorChecked));
    }

    private void addProduct(MenuType menuType, DialogInterface dialog) {
        if (!etProductName.getText().toString().equalsIgnoreCase("") && etProductPrice.getText()
                .toString().equalsIgnoreCase("")) {
            Snackbar snackbar = Snackbar
                    .make(layoutMenu, getResources().getString(R.string.invalide_price), Snackbar.LENGTH_SHORT);

            snackbar.show();
        }
        else if(etProductName.getText().toString().equalsIgnoreCase("") && !etProductPrice.getText()
                .toString().equalsIgnoreCase(""))
        {
            Snackbar snackbar = Snackbar
                    .make(layoutMenu, getResources().getString(R.string.invalide_name), Snackbar.LENGTH_SHORT);
            View snackBarLayout = snackbar.getView();
            TextView textView = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_red, 0);
            // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
            snackbar.show();
        }
        else {
            try {
                labelProduct = etProductName.getText().toString();
                descriptionProduct = "";

                priceProduct = Float.valueOf(etProductPrice.getText().toString());
                idCategorie = menuType.getId();
                JsonObject postParam = new JsonObject();
                postParam.addProperty("label", labelProduct);
                postParam.addProperty("description", descriptionProduct);
                postParam.addProperty("price", priceProduct);
                postParam.addProperty("categoryId", idCategorie);
                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setLabel(labelProduct);
                menuProduct.setPrice(priceProduct);

                try {
                    menuType.getProducts().add(menuProduct);
                } catch (NullPointerException e) {
                    List<MenuProduct> menuProducts = new ArrayList<>();
                    menuProducts.add(menuProduct);
                    menuType.setProducts((ArrayList<MenuProduct>) menuProducts);
                }
                menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                if (dialog != null && getBaseContext() != null)
                    dialog.dismiss();
            } catch (NullPointerException | NumberFormatException e) {
                Snackbar snackbar = Snackbar
                        .make(layoutMenu, getResources().getString(R.string.error_price), Snackbar.LENGTH_SHORT);

                snackbar.show();
            }


        }
    }

    public void deleteImageBusiness(int id) {
        try {
            if (!ConnectivityService.isOnline(getBaseContext())) {
                if (!ConnectivityService.isOnline(getBaseContext())) {
                    Snackbar snackbar = Snackbar
                            .make(layoutMenu, getResources().getString(R.string.hors_connexion_mess), Snackbar.LENGTH_SHORT);
                    View snackBarLayout = snackbar.getView();
                    TextView textView = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_red, 0);
                    // textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                    snackbar.show();
                }
            } else {
                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                        .deleteMenuImage(SharedPreferencesFactory.getAdminToken(getBaseContext()), id);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.code() == 200) {
                            /*
                            View snackbarLayout = snackbar.getView();
                            TextView textView = (TextView)snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.white_checkbox, 0, 0, 0);
                            textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
                             */
                           /* Snackbar snackbar = Snackbar
                                    .make(layoutMenu, getResources().getString(R.string.message_delete_photo_success), Snackbar.LENGTH_SHORT);
                            View snackBarLayout = snackbar.getView();
                            TextView textView = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.teck_green, 0);*/
                            //textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));


                           /* snackbar.show();*/
                            imageGaleries.remove(indexPhoto);
                            menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);


                        } else {
                          /*  Snackbar snackbar = Snackbar
                                    .make(layoutMenu, getResources().getString(R.string.problem_image), Snackbar.LENGTH_SHORT);
                            View snackBarLayout = snackbar.getView();
                            TextView textView = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_red, 0);*/
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


                    }
                });
            }
        } catch (NullPointerException e) {
            try {
                imageGaleries.remove(indexPhoto);
                menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
            } catch (NullPointerException p) {

            }

        }

    }

    @Override
    public void onBackPressed() {

        if (ivBackUpdate.isShown()) {
            hideUpdateView();
            if (isActivated) {
                try {
                    isActivated = false;
                    if (menuPhotoAdapter != null)
                        menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                    if (menuBusinessAdapter != null)
                        menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                    else
                        menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);

                } catch (NullPointerException e) {

                }

                //stopAnimation();
            }
        } else if (viewPagerPhoto.isShown()) {
            hideViewPager();
        } else if (viewPhoto.isShown()) {

            if (isActivated) {
                try {

                    isActivated = false;
                    if (menuPhotoAdapter != null) {
                        menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                    }
                    if (menuBusinessAdapter != null)
                        menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                    else
                        menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);


                } catch (NullPointerException e) {

                }
                //stopAnimation();
            } else {
                updateBackgroundTxtMenu();
            }
        } else if (viewText.isShown()) {
            super.onBackPressed();
            sendBusinessMenu();
            finish();
        }
    }

    private void sendBusinessMenu() {
        if (menuTypes != null) {
            if (menuTypes.size() != 0) {
                Utils.menuTypes = new ArrayList<>();
                Utils.menuTypes = menuTypes;
                if (isFromProfile) {
                    sendFromProfileBusinessMenu(business,menuTypes);
                }
            }
        }
        if (imageGaleries != null) {
            if (imageGaleries.size() != 0) {
                Utils.imageGaleriesMenu = new ArrayList<>();
                Utils.imageGaleriesMenu = imageGaleries;
                if(isFromProfile)
                {
                    try {
                        imgMenu = new ArrayList<>();
                        String toRemove = "https";
                        for (int i = 0; i < imageGaleries.size(); i++) {
                            if (!imageGaleries.get(i).getImage().substring(0, 5).equalsIgnoreCase(toRemove)) {
                                imgMenu.add(imageGaleries.get(i));
                            }
                        }
                        addImagesMenuList(business,imgMenu);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());

                    }
                }

            }
        }



    }
    private void addImagesMenuList(Business business,ArrayList<ImageGalerie> imgMenu) {
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
                        System.out.println("failure");
                        System.out.println(call.toString());
                        System.out.println(t.getMessage());
                    }
                });
            }
    }
    private void updateCategorie(MenuType menuType) {
        if (etCategorieName.getText().toString().equalsIgnoreCase("")) {

        } else {
            menuType.setLabel(etCategorieName.getText().toString());
            if (menuBusinessAdapter != null)
                menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
            else {
                menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);
            }
        }
    }

    public void showDeleteDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);

        builder.setTitle("Supprimer la photo");
        builder.setMessage("Voulez vous vraiment supprimer cette photo ?");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImageBusiness(id);
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_light_color));

    }

    public void showUpdateProductDialog(final MenuProduct menuProduct) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_product_dialog, null);
        dialogBuilder.setView(dialogView);

        etProductName = dialogView.findViewById(R.id.et_product_name);
        etProductPrice = dialogView.findViewById(R.id.et_product_price);
        etProductName.setText(menuProduct.getLabel());
        etProductPrice.setText(String.valueOf(menuProduct.getPrice()));
        dialogBuilder.setTitle(" Modifier" +
                "\n");
        dialogBuilder.setPositiveButton("CONFIRMER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateProduct(menuProduct);
                dialog.dismiss();

            }
        });
        dialogBuilder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_2));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.trackColorChecked));
    }

    public void removeProductFromList(MenuProduct currentChild, MenuType menuType) {


        menuType.getProducts().remove(currentChild);
        menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);


    }

    private void updateProduct(MenuProduct menuProduct) {
        if (etProductName.getText().toString().equalsIgnoreCase("") || etProductPrice.getText()
                .toString().equalsIgnoreCase("")) {
            // new CustomToast(getBaseContext(), getResources().getString(R.string.echec), getContext().getString(R.string.add_product_echec), R.drawable.ic_alert_exclamation_red, CustomToast.SUCCESS).show();
        } else {
            String productName = "";
            Float productPrice = 0f;
            productName = etProductName.getText().toString();
            productPrice = Float.valueOf(etProductPrice.getText().toString());
            menuProduct.setLabel(productName);
            menuProduct.setPrice(productPrice);


            if (menuBusinessAdapter != null)
                menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
            else
                menuBusinessAdapter = new MenuBusinessAdapter(MenuActivity.this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);

        }
    }
    private void initializeWithoutMenu()
    {try {
        layoutContentMenu.setVisibility(View.VISIBLE);
        progressBarMenu.setVisibility(View.GONE);
    } catch (NullPointerException e) {

    }
        menuTypes = new ArrayList<>();
        if (Utils.menuTypes != null) {
            if (Utils.menuTypes.size() != 0) {
                menuTypes = Utils.menuTypes;

                layoutEmtyMenu.setVisibility(View.GONE);
                layoutExpandMenu.setVisibility(View.VISIBLE);
                menuBusinessAdapter = new MenuBusinessAdapter(MenuActivity.this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);
                lvExpMenu.setAdapter(menuBusinessAdapter);
                footerView.setVisibility(View.GONE);
                lvExpMenu.addFooterView(footerView);
                lvExpMenu.setAdapter(menuBusinessAdapter);

                for (int i = 0; i < menuTypes.size(); i++) {

                    lvExpMenu.expandGroup(i);
                }
                lvExpMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });

            }
            imageGaleries = new ArrayList<>();
            if (Utils.imageGaleriesMenu != null) {
                if (Utils.imageGaleriesMenu.size() != 0) {
                    imageGaleries = Utils.imageGaleriesMenu;
                    menuPhotoAdapter = new MenuPhotoAdapterr(MenuActivity.this, (ArrayList<ImageGalerie>) imageGaleries, ivEdit, isActivated, business);
                    gridviewPhoto.setAdapter(menuPhotoAdapter);
                    menuPhotoAdapter.notifyDataSetChanged();
                    registerForContextMenu(gridviewPhoto);

                    gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                            layoutShadowImage = view.findViewById(R.id.layout_shadow_image);
                            ivDeleteImage = view.findViewById(R.id.iv_delete_photo);
                            layoutShadowImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    });
                    gridviewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (!isActivated) {
                                showViewPager();
                                viewPagerAdapter = new ViewPagerMenuAdapter(getBaseContext(), (ArrayList<ImageGalerie>) imageGaleries, MenuActivity.this);
                                viewPagerPhoto.setAdapter(viewPagerAdapter);
                                viewPagerPhoto.setCurrentItem(position);
                                tabDots.setupWithViewPager(viewPagerPhoto);

                                toolbar.setVisibility(View.GONE);
                                toolbarUpdate.setVisibility(View.GONE);
                            }
                        }
                    });
                    layoutEmtyMenuPhoto.setVisibility(View.GONE);
                    layoutPhotoMenu.setVisibility(View.VISIBLE);
                }
            }

        }
    }
    private void getMenuBusiness(final Business business) {
        try {
            layoutContentMenu.setVisibility(View.GONE);
            progressBarMenu.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {

        }
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                .getMenuByBusiness(business.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                layoutContentMenu.setVisibility(View.VISIBLE);
                progressBarMenu.setVisibility(View.GONE);
                Gson gson = Utils.getGsonInstance();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (response.code() == 200) {
                        JSONArray jsonArrayMenuText = jsonObject.getJSONArray("items");
                        JSONArray jsonArrayMenuImage = jsonObject.getJSONArray("images");


                        if (jsonArrayMenuText.length() != 0) {
                            menuTypes = new ArrayList<>();
                            if (Utils.menuTypes != null) {
                                if (Utils.menuTypes.size() != 0) {
                                    menuTypes = Utils.menuTypes;
                                } else {
                                    for (int i = 0; i < jsonArrayMenuText.length(); i++) {
                                        MenuType menuType = gson.fromJson(jsonArrayMenuText.get(i).toString(), MenuType.class);
                                        menuTypes.add(menuType);
                                    }
                                }

                            } else {
                                menuTypes = new ArrayList<>();
                                for (int i = 0; i < jsonArrayMenuText.length(); i++) {
                                    MenuType menuType = gson.fromJson(jsonArrayMenuText.get(i).toString(), MenuType.class);
                                    menuTypes.add(menuType);
                                }
                            }

                            rlLayoutAddPhoto.setVisibility(View.GONE);
                            gridviewPhoto.setVisibility(View.GONE);
                            layoutEmtyMenu.setVisibility(View.GONE);
                            layoutExpandMenu.setVisibility(View.VISIBLE);
                            menuBusinessAdapter = new MenuBusinessAdapter(MenuActivity.this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);
                            lvExpMenu.setAdapter(menuBusinessAdapter);
                            footerView.setVisibility(View.GONE);
                            lvExpMenu.addFooterView(footerView);
                            lvExpMenu.setAdapter(menuBusinessAdapter);

                            for (int i = 0; i < menuTypes.size(); i++) {

                                lvExpMenu.expandGroup(i);
                            }
                            lvExpMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                    return true;
                                }
                            });

                        }
                        if (jsonArrayMenuImage.length() != 0) {
                            rlLayoutAddPhoto.setVisibility(View.VISIBLE);
                            gridviewPhoto.setVisibility(View.VISIBLE);
                            imageGaleries = new ArrayList<>();
                            if (Utils.imageGaleriesMenu != null) {
                                if (Utils.imageGaleriesMenu.size() != 0) {
                                    imageGaleries = Utils.imageGaleriesMenu;
                                } else {
                                    for (int i = 0; i < jsonArrayMenuImage.length(); i++) {
                                        ImageGalerie imageGalerie = gson.fromJson(jsonArrayMenuImage.get(i).toString(), ImageGalerie.class);
                                        imageGaleries.add(imageGalerie);
                                    }
                                }
                            } else {
                                for (int i = 0; i < jsonArrayMenuImage.length(); i++) {
                                    ImageGalerie imageGalerie = gson.fromJson(jsonArrayMenuImage.get(i).toString(), ImageGalerie.class);
                                    imageGaleries.add(imageGalerie);
                                }
                            }
                            menuPhotoAdapter = new MenuPhotoAdapterr(MenuActivity.this, (ArrayList<ImageGalerie>) imageGaleries, ivEdit, isActivated, business);
                            gridviewPhoto.setAdapter(menuPhotoAdapter);
                            menuPhotoAdapter.notifyDataSetChanged();
                            registerForContextMenu(gridviewPhoto);

                            gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                    layoutShadowImage = view.findViewById(R.id.layout_shadow_image);
                                    ivDeleteImage = view.findViewById(R.id.iv_delete_photo);
                                    layoutShadowImage.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            });
                            gridviewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (!isActivated) {
                                        showViewPager();
                                        viewPagerAdapter = new ViewPagerMenuAdapter(getBaseContext(), (ArrayList<ImageGalerie>) imageGaleries, MenuActivity.this);
                                        viewPagerPhoto.setAdapter(viewPagerAdapter);
                                        viewPagerPhoto.setCurrentItem(position);
                                        tabDots.setupWithViewPager(viewPagerPhoto);

                                        toolbar.setVisibility(View.GONE);
                                        toolbarUpdate.setVisibility(View.GONE);
                                    }
                                }
                            });
                            layoutEmtyMenuPhoto.setVisibility(View.GONE);
                            layoutPhotoMenu.setVisibility(View.VISIBLE);
                        }
                        if (jsonArrayMenuImage.length() == 0 && viewPhoto.isShown()) {
                            try {
                                layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
                                layoutPhotoMenu.setVisibility(View.GONE);
                            } catch (NullPointerException e) {

                            }

                        }
                        if (jsonArrayMenuText.length() == 0 && viewText.isShown()) {
                            layoutEmtyMenu.setVisibility(View.VISIBLE);
                            layoutEmtyMenuPhoto.setVisibility(View.GONE);
                            layoutExpandMenu.setVisibility(View.GONE);
                            rlLayoutAddPhoto.setVisibility(View.GONE);

                        }
                        if(jsonArrayMenuText.length()!=0 && viewText.isShown())
                        {
                            rlLayoutAddPhoto.setVisibility(View.GONE);
                            layoutEmtyMenuPhoto.setVisibility(View.GONE);
                            //rlPhotoMenu.setVisibility(View.GONE);
                            layoutPhotoMenu.setVisibility(View.GONE);
                            layoutEmtyMenu.setVisibility(View.GONE);
                            layoutExpandMenu.setVisibility(View.VISIBLE);
                            layoutAddPhotoMenu.setVisibility(View.GONE);
                        }
                    } else {

                    }


                } catch (JSONException | IOException | NullPointerException e) {
                    layoutContentMenu.setVisibility(View.VISIBLE);
                    progressBarMenu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    layoutContentMenu.setVisibility(View.VISIBLE);
                    progressBarMenu.setVisibility(View.GONE);
                } catch (NullPointerException e) {

                }
            }
        });
    }

    private void showViewPager() {

        viewPagerPhoto.setVisibility(View.VISIBLE);
        tabDots.setVisibility(View.VISIBLE);
        gridviewPhoto.setVisibility(View.GONE);
        lnMenu.setVisibility(View.GONE);
    }

    private void hideViewPager() {
        viewPagerPhoto.setVisibility(View.GONE);
        tabDots.setVisibility(View.GONE);
        gridviewPhoto.setVisibility(View.VISIBLE);
        lnMenu.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
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

        if (!ConnectivityService.isOnline(getBaseContext())) {
            Snackbar snackbar = Snackbar
                    .make(layoutMenu, getResources().getString(R.string.hors_connexion_mess), Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else {
            try {
                ImageGalerie imgToAdd = new ImageGalerie(imageGaleries.size(), croppedImage.getPath(), croppedImage.getPath(), true);
                if (imgToAdd != null) {
                    imageGaleries.add(imgToAdd);
                    menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                    System.out.println("hne");
                    /*File file = new File(imgToAdd.getThumbnailPath());
                    RequestBody reqFile = RequestBody.create(
                            MediaType.parse("image/*"), file
                    );
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", file.getName(), reqFile);
                    final ProgressDialog progress = new ProgressDialog(getBaseContext());
                    progress.setMessage(getString(R.string.loading_progress_dialog));
                    progress.show();
                    Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService().addPhotoBusinessMenu(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {

                                if (response.code() == 200) {





                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }
                            progress.dismiss();

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //Log.i("Upload error :", t.getMessage());

                            progress.dismiss();
                        }
                    });*/
                }

            } catch (NullPointerException e) {
                try {
                    imageGaleries = new ArrayList<>();
                    ImageGalerie imgToAdd = new ImageGalerie(imageGaleries.size(), croppedImage.getPath(), croppedImage.getPath(), true);
                    if (imgToAdd != null) {
                        imageGaleries.add(imgToAdd);
                        menuPhotoAdapter = new MenuPhotoAdapterr(MenuActivity.this, (ArrayList<ImageGalerie>) imageGaleries, ivEdit, isActivated, business);
                        gridviewPhoto.setAdapter(menuPhotoAdapter);
                        menuPhotoAdapter.notifyDataSetChanged();
                        registerForContextMenu(gridviewPhoto);

                        gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                layoutShadowImage = view.findViewById(R.id.layout_shadow_image);
                                ivDeleteImage = view.findViewById(R.id.iv_delete_photo);
                                layoutShadowImage.setVisibility(View.VISIBLE);
                                return false;
                            }
                        });
                        gridviewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (!isActivated) {
                                    showViewPager();
                                    viewPagerAdapter = new ViewPagerMenuAdapter(getBaseContext(), (ArrayList<ImageGalerie>) imageGaleries, MenuActivity.this);
                                    viewPagerPhoto.setAdapter(viewPagerAdapter);
                                    viewPagerPhoto.setCurrentItem(position);
                                    tabDots.setupWithViewPager(viewPagerPhoto);

                                    toolbar.setVisibility(View.GONE);
                                    toolbarUpdate.setVisibility(View.GONE);
                                }
                            }
                        });
                        layoutEmtyMenuPhoto.setVisibility(View.GONE);
                        layoutPhotoMenu.setVisibility(View.VISIBLE);


                    }
                } catch (NullPointerException p) {

                    System.out.println(p.getMessage());
                }

            }
        }
    }

    private void initialize() {
        toolbarUpdate = findViewById(R.id.toolbar_update);
        toolbar = findViewById(R.id.toolbar);
        rlTxtMenu = findViewById(R.id.rl_txt_menu);
        rlPhotoMenu = findViewById(R.id.rl_photo_menu);
        layoutPhotoMenu = findViewById(R.id.layout_photo_menu);
        layoutAddPhotoMenu = findViewById(R.id.layout_add_photo_menu);
        layoutExpandMenu = findViewById(R.id.layout_expand_menu);
        layoutEmtyMenuPhoto = findViewById(R.id.layout_emty_menu_photo);
        viewPagerPhoto = findViewById(R.id.view_pager_photo);
        progressBarPhoto = findViewById(R.id.progressBarPhoto);
        progressBarItem = findViewById(R.id.progressBarItem);
        layoutContentMenu = findViewById(R.id.layout_content_menu);
        layoutNoInternetMenu = findViewById(R.id.layout_no_internet_menu);
        progressBarMenu = findViewById(R.id.progressBarMenu);
        tabDots = findViewById(R.id.tabDots);
        layoutMenu = findViewById(R.id.layout_menu);

        viewText = findViewById(R.id.view_txt_menu);
        lnMenu = findViewById(R.id.ln_menu);
        viewPhoto = findViewById(R.id.view_photo_menu);
        lblTxtMenu = findViewById(R.id.lbl_txt_menu);
        lblTxtPhoto = findViewById(R.id.lbl_photo_menu);
        gridviewPhoto = findViewById(R.id.gridview_photo);
        layoutEmtyMenu = findViewById(R.id.layout_emty_menu);
        ivBack = findViewById(R.id.iv_back);
        ivBackUpdate = findViewById(R.id.iv_back_update);
        tvSave = findViewById(R.id.tv_save);
        ivEdit = findViewById(R.id.iv_edit);
        rlLayoutAddPhoto = findViewById(R.id.rl_layout_add_photo);
        rlLayoutAddPhoto.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivBackUpdate.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        footerView.setOnClickListener(this);
        layoutAddPhotoMenu.setOnClickListener(this);
        layoutAddCategorie = findViewById(R.id.layout_add_categorie);
        lvExpMenu = findViewById(R.id.lv_exp_menu);
        LatoBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        LatoRegular = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        rlPhotoMenu.setOnClickListener(this);
        rlTxtMenu.setOnClickListener(this);
       /* rlResseayer = layoutNoInternetConnection.findViewById(R.id.layout_ressayer);
        rlResseayer.setOnClickListener(this);*/
        layoutAddCategorie.setOnClickListener(this);
        if (business != null) {
            if (business.getId() != 0) {
                getMenuBusiness(business);
            }
            else if(Utils.menuTypes.size()!=0 || Utils.imageGaleriesMenu.size()!=0) {
                initializeWithoutMenu();
            }
            else {
                updateBackgroundTxtMenu();
            }
        } else {
            updateBackgroundTxtMenu();
        }

    }

    private void updateBackgroundPhotoMenu() {

        viewText.setVisibility(View.GONE);
        viewPhoto.setVisibility(View.VISIBLE);
        rlTxtMenu.setBackground(getResources().getDrawable(R.drawable.shaperel));
        rlPhotoMenu.setBackground(getResources().getDrawable(R.color.blanb));
        lblTxtMenu.setTypeface(LatoRegular);
        lblTxtPhoto.setTypeface(LatoBold);
        layoutPhotoMenu.setVisibility(View.VISIBLE);
        layoutExpandMenu.setVisibility(View.GONE);
        rlLayoutAddPhoto.setVisibility(View.VISIBLE);
        gridviewPhoto.setVisibility(View.VISIBLE);
        layoutAddPhotoMenu.setVisibility(View.VISIBLE);

        if (imageGaleries.size() == 0) {
            layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
            layoutEmtyMenu.setVisibility(View.GONE);
            layoutPhotoMenu.setVisibility(View.GONE);

        }
    }

    private void updateBackgroundTxtMenu() {
        // tvSave.setText(getBaseContext().getResources().getString(R.string.sauvgarder));
        tvSave.setText("");
        viewText.setVisibility(View.VISIBLE);
        viewPhoto.setVisibility(View.GONE);
        rlTxtMenu.setBackground(getResources().getDrawable(R.color.blanb));
        rlPhotoMenu.setBackground(getResources().getDrawable(R.drawable.shaperel));
        lblTxtMenu.setTypeface(LatoBold);
        lblTxtPhoto.setTypeface(LatoRegular);
        layoutPhotoMenu.setVisibility(View.GONE);
        layoutEmtyMenuPhoto.setVisibility(View.GONE);
        layoutExpandMenu.setVisibility(View.VISIBLE);
        try {

            if (menuTypes.size() == 0) {
                layoutEmtyMenu.setVisibility(View.VISIBLE);
                layoutEmtyMenuPhoto.setVisibility(View.GONE);
                layoutExpandMenu.setVisibility(View.GONE);
            } else {
                layoutEmtyMenu.setVisibility(View.GONE);
                layoutEmtyMenuPhoto.setVisibility(View.GONE);
                layoutExpandMenu.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {

        }

    }

    private void uploadImageDialog() {
        gridviewPhoto.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);

        builder.setTitle("Ajouter une photo");
        builder.setMessage("Prenez une photo ou bien sélectionnez depuis vos photos");
        builder.setPositiveButton("GALLERIE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromGallerie();
                dialog.dismiss();
                gridviewPhoto.setVisibility(View.VISIBLE);

            }
        });

        builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageFromCamera();
                dialog.dismiss();
                gridviewPhoto.setVisibility(View.VISIBLE);
            }
        });


        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue_light_color));
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                gridviewPhoto.setVisibility(View.VISIBLE);
            }
        });

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

    private void addCategorie(DialogInterface dialog) {
        try {
            if (etCategorieName.getText().toString().equalsIgnoreCase("")) {

            } else {
                categorieName = etCategorieName.getText().toString();
                JsonObject postParam = new JsonObject();
                postParam.addProperty("label", categorieName);
                postParam.addProperty("description", descriptionCategorie);
                postParam.addProperty("businessId", 0);
                MenuType categorie = new MenuType();
                categorie.setLabel(categorieName);
                if (menuTypes != null)
                    menuTypes.add(categorie);
                else {
                    menuTypes = new ArrayList<>();
                    menuTypes.add(categorie);
                }
                if (menuBusinessAdapter != null) {
                    menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);

                    lvExpMenu.setAdapter(menuBusinessAdapter);
                    layoutExpandMenu.setVisibility(View.VISIBLE);
                    layoutEmtyMenuPhoto.setVisibility(View.GONE);
                    layoutEmtyMenu.setVisibility(View.GONE);
                    layoutPhotoMenu.setVisibility(View.GONE);
                    footerView.setVisibility(View.VISIBLE);
                    if(testEdit==0)
                    {   testEdit = 1;
                        lvExpMenu.addFooterView(footerView);
                    }

                    for (int i = 0; i < menuTypes.size(); i++) {

                        lvExpMenu.expandGroup(i);
                    }
                    lvExpMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    });
                } else {

                    menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);
                    lvExpMenu.setAdapter(menuBusinessAdapter);
                    footerView.setVisibility(View.VISIBLE);
                    lvExpMenu.addFooterView(footerView);
                    lvExpMenu.setAdapter(menuBusinessAdapter);
                    footerView.setOnClickListener(this);
                    for (int i = 0; i < menuTypes.size(); i++) {

                        lvExpMenu.expandGroup(i);
                    }
                    lvExpMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    });
                }
                if (dialog != null && getBaseContext() != null)
                    dialog.dismiss();
                layoutExpandMenu.setVisibility(View.VISIBLE);
                layoutEmtyMenuPhoto.setVisibility(View.GONE);
                layoutEmtyMenu.setVisibility(View.GONE);
                layoutPhotoMenu.setVisibility(View.GONE);



            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

    }

    public void showAddCategorieDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_categorie_dialog, null);
        dialogBuilder.setView(dialogView);

        etCategorieName = dialogView.findViewById(R.id.et_categorie_name);
        dialogBuilder.setTitle(" Nouvelle catégorie" +
                "\n");
        dialogBuilder.setPositiveButton("CONFIRMER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addCategorie(dialog);

            }
        });
        dialogBuilder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_2));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.trackColorChecked));
    }

    public void showUpdateCategorieDialog(final MenuType menuType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_categorie_dialog, null);
        dialogBuilder.setView(dialogView);
        View viewCategorie = dialogView.findViewById(R.id.view_categorie);
        etCategorieName = dialogView.findViewById(R.id.et_categorie_name);
        etCategorieName.setText(menuType.getLabel());
        dialogBuilder.setTitle(" Modifier" +
                "\n");

        dialogBuilder.setPositiveButton("CONFIRMER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateCategorie(menuType);
                dialog.dismiss();

            }
        });
        dialogBuilder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_2));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.trackColorChecked));
    }

    @Override
    public void onClick(View v) {
        if (v == rlLayoutAddPhoto) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
            }
        }

        if (v == layoutAddCategorie) {
            showAddCategorieDialog();
        }
        if (v == rlTxtMenu) {
            if (!ConnectivityService.isOnline(getBaseContext())) {
                layoutNoInternetMenu.setVisibility(View.VISIBLE);
                layoutExpandMenu.setVisibility(View.GONE);
                layoutPhotoMenu.setVisibility(View.GONE);
                rlResseayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ConnectivityService.isOnline(getBaseContext())) {
                            layoutNoInternetMenu.setVisibility(View.VISIBLE);
                            layoutExpandMenu.setVisibility(View.GONE);
                            layoutPhotoMenu.setVisibility(View.GONE);
                        } else {
                            layoutNoInternetMenu.setVisibility(View.GONE);
                            layoutExpandMenu.setVisibility(View.VISIBLE);
                            updateBackgroundTxtMenu();
                        }
                    }
                });
            } else {
                updateBackgroundTxtMenu();
            }

        }
        if (v == rlPhotoMenu) {
            if (!ConnectivityService.isOnline(getBaseContext())) {
                layoutNoInternetMenu.setVisibility(View.VISIBLE);
                layoutPhotoMenu.setVisibility(View.GONE);
                layoutExpandMenu.setVisibility(View.GONE);
                rlResseayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ConnectivityService.isOnline(getBaseContext())) {
                            layoutNoInternetMenu.setVisibility(View.VISIBLE);
                            layoutPhotoMenu.setVisibility(View.GONE);
                            layoutExpandMenu.setVisibility(View.GONE);
                        } else {
                            layoutNoInternetMenu.setVisibility(View.GONE);
                            layoutPhotoMenu.setVisibility(View.VISIBLE);


                            updateBackgroundPhotoMenu();

                        }
                    }
                });
            } else {
                layoutNoInternetMenu.setVisibility(View.GONE);
                layoutPhotoMenu.setVisibility(View.VISIBLE);
                updateBackgroundPhotoMenu();

            }


        }
        if (v == ivBackUpdate) {
            hideUpdateView();
            if (isActivated) {
                try {
                    isActivated = false;
                    if (menuPhotoAdapter != null)
                        menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                    if (menuBusinessAdapter != null)
                        menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                    else
                        menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);

                } catch (NullPointerException e) {

                }

                //stopAnimation();
            }
        }
        if (v == tvSave) {
            //saveChanged();
        }
        if (v == footerView) {
            showAddCategorieDialog();
        }
        if (v == layoutAddPhotoMenu) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
            }

        }

        if (v == ivEdit) {
            if (toolbar.isShown()) {
                showUpdateView();
                footerView.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                toolbarUpdate.setVisibility(View.VISIBLE);

            }
            if (!isActivated) {
                try {
                    isActivated = true;
                    if (menuPhotoAdapter != null)
                        menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                    if (menuBusinessAdapter != null)
                        menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                    else
                        menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);
                } catch (NullPointerException e) {

                }

                //gridViewItemAnimation();

            }
        }
        if (v == ivBack) {
            if (ivBackUpdate.isShown()) {
                hideUpdateView();
                if (isActivated) {
                    try {
                        isActivated = false;
                        if (menuPhotoAdapter != null)
                            menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                        if (menuBusinessAdapter != null)
                            menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                        else
                            menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);

                    } catch (NullPointerException e) {

                    }

                    //stopAnimation();
                }
            } else if (viewPagerPhoto.isShown()) {
                hideViewPager();
            } else if (viewPhoto.isShown()) {

                if (isActivated) {
                    try {

                        isActivated = false;
                        if (menuPhotoAdapter != null) {
                            menuPhotoAdapter.refresh((ArrayList<ImageGalerie>) imageGaleries, isActivated);
                        }
                        if (menuBusinessAdapter != null)
                            menuBusinessAdapter.refresh((ArrayList<MenuType>) menuTypes, isActivated);
                        else
                            menuBusinessAdapter = new MenuBusinessAdapter(this, getBaseContext(), (ArrayList<MenuType>) menuTypes, footerView, isActivated, business);


                    } catch (NullPointerException e) {

                    }
                    //stopAnimation();
                } else {
                    updateBackgroundTxtMenu();
                }
            } else if (viewText.isShown()) {
                super.onBackPressed();
                sendBusinessMenu();
                finish();
            }


        }


    }

    private void sendFromProfileBusinessMenu(Business business,List<MenuType> menuTypes) {
        JsonArray menuToSend = new JsonArray();
        JsonArray productsSend = new JsonArray();
        JsonObject categorieJson;
        JsonObject productJson;

        try {

        } catch (NullPointerException e) {

        }

        if (menuTypes != null) {
            if (menuTypes.size() != 0) {
                for (int i = 0; i < menuTypes.size(); i++) {
                    menuType = menuTypes.get(i);
                    categorieJson = new JsonObject();
                    productsSend = new JsonArray();
                    categorieJson.addProperty("label", menuType.getLabel());
                    categorieJson.addProperty("description", "");
                    categorieJson.addProperty("businessId", business.getId());
                    try {

                    for (int j = 0; j < menuType.getProducts().size(); j++) {
                        productJson = new JsonObject();
                        productJson.addProperty("label", menuType.getProducts().get(j).getLabel());
                        productJson.addProperty("description", "");
                        productJson.addProperty("price", menuType.getProducts().get(j).getPrice());
                        productJson.addProperty("categoryId", menuType.getProducts().get(j).getCategoryId());
                        productsSend.add(productJson);
                    }

                    }catch (NullPointerException e)
                    {

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

    private void showUpdateView() {
        footerView.setVisibility(View.VISIBLE);
        toolbarUpdate.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.GONE);
    }

    private void hideUpdateView() {
        footerView.setVisibility(View.GONE);
        toolbarUpdate.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
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
    public void onImagesChosen(List<ChosenImage> list) {
        if (list != null) {


            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setToolbarTitle("Ajouter photo");
            options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
            options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.blanb));


            UCrop.of(Uri.fromFile(new File((list.get(0).getOriginalPath()))),
                    Uri.fromFile(new File((list.get(0).getOriginalPath()))))
                    .withOptions(options)
                    .withMaxResultSize(1000, 1000)
                    .start(this);


        }
        gridviewPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String s) {

    }
}
