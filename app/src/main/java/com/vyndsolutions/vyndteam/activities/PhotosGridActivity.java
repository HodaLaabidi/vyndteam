package com.vyndsolutions.vyndteam.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.adapters.PhotoAdapter;
import com.vyndsolutions.vyndteam.adapters.ViewPagerAdapter;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SharedPreferencesFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.utils.Const;
import com.vyndsolutions.vyndteam.utils.Utils;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosGridActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerCallback {

    GridView gridviewPhoto;
    TabLayout tabDots;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPagerPhoto;
    private Uri croppedImage;
    NetworkImageView ivBack, ivAddPhoto, ivDelete;
    View view;
    Business business = new Business();
    private float scaleImage;
    ImageViewTouch ivFromAdapter;
    ImageGalerie[] imageGaleries;
    RelativeLayout rlPager, layoutDeleteBar;
    PhotoAdapter photoAdapter;
    private String[] PERMISSIONS_PHOTO = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private int indexPhoto;
    private int idImage;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private ArrayList<ImageGalerie> imageGalerieArrayList = new ArrayList<>();
    Toolbar toolbar;
    RelativeLayout rlDeletePhoto;
    private static RelativeLayout layoutShadowImage;
    public ProgressBar progressBarPhoto;
    private boolean isAlertShown = false;
    private boolean isFromProfile = false;
    private ArrayList<ImageGalerie> img = new ArrayList<>();
    private View layoutEmtyMenuPhoto;
    private View gridContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_grid);
        try {
            business = (Business) getIntent().getSerializableExtra("business");

            try {
                isFromProfile = getIntent().getBooleanExtra("isFromProfile", false);
            } catch (NullPointerException e) {

            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        initialize();
        //goback(view);
    }

    private void initialize() {

        gridviewPhoto = findViewById(R.id.gridview_photo);
        ivBack = findViewById(R.id.iv_back);
        ivAddPhoto = findViewById(R.id.iv_add_photo);
        viewPagerPhoto = findViewById(R.id.view_pager_photo);
        gridContent = findViewById(R.id.grid_content);
        tabDots = findViewById(R.id.tabDots);
        rlPager = findViewById(R.id.layout_pager);
        toolbar = findViewById(R.id.toolbar);
        progressBarPhoto = findViewById(R.id.progressBarPhoto);
        ivDelete = findViewById(R.id.iv_delete_photo);
        rlDeletePhoto = findViewById(R.id.rl_delete_photo);
        layoutDeleteBar = findViewById(R.id.layout_delete_bar);
        layoutEmtyMenuPhoto = findViewById(R.id.layout_emty_menu_photo);
        viewPagerAdapter = new ViewPagerAdapter(getBaseContext());
        rlDeletePhoto.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivAddPhoto.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        layoutEmtyMenuPhoto.setOnClickListener(this);
        try {

            if (business.getImages().size() == 0) {

                gridContent.setVisibility(View.GONE);
                layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
            } else {

                try {
                    if (business.getImages() != null) {

                        if (business.getImages().size() != 0) {

                            imageGalerieArrayList = business.getImages();
                            layoutEmtyMenuPhoto.setVisibility(View.GONE);
                            gridviewPhoto.setVisibility(View.VISIBLE);
                            try {
                                System.out.println("3");
                                //imageGalerieArrayList = new ArrayList<ImageGalerie>(Arrays.asList(imageGaleries)); do this in the previous activity
                                photoAdapter = new PhotoAdapter(PhotosGridActivity.this, imageGalerieArrayList);
                                gridviewPhoto.setAdapter(photoAdapter);
                                registerForContextMenu(gridviewPhoto);
                                gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                        //layoutShadowImage = view.findViewById(R.id.layout_shadow_image_gallerie);
                                        // layoutShadowImage.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                });
                                gridviewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        showViewPager();
                                        viewPagerAdapter = new ViewPagerAdapter(getBaseContext(), imageGalerieArrayList, layoutDeleteBar, toolbar, tabDots, business);
                                        viewPagerPhoto.setAdapter(viewPagerAdapter);
                                        viewPagerPhoto.setCurrentItem(position);
                                        tabDots.setupWithViewPager(viewPagerPhoto);
                                        toolbar.setVisibility(View.GONE);
                                        layoutDeleteBar.setVisibility(View.GONE);
                                        viewPagerPhoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                idImage = imageGalerieArrayList.get(position).getId(); //this in modifiable
                                                toolbar.setVisibility(View.GONE);
                                                layoutDeleteBar.setVisibility(View.GONE);
                                                tabDots.setVisibility(View.VISIBLE);

                                            }

                                            @Override
                                            public void onPageSelected(int position) {

                                            }

                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });

                                    }
                                });

                                rlDeletePhoto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (idImage != 0) {
                                            showDeleteDialogViewr();


                                        }
                                    }
                                });
                            } catch (NullPointerException e) {
                                System.out.println("4");
                            }
                        } else {
                            gridContent.setVisibility(View.GONE);
                            layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
                            imageGalerieArrayList = new ArrayList<>();
                            setAdapterData(imageGalerieArrayList);
                            System.out.println("6");
                        }
                    }
                } catch (NullPointerException e) {
                    gridContent.setVisibility(View.GONE);
                    layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
                    imageGalerieArrayList = new ArrayList<>();
                    setAdapterData(imageGalerieArrayList);
                    System.out.println("7");


                }
            }

        } catch (NullPointerException e) {
            System.out.println("eeer");
            gridContent.setVisibility(View.GONE);
            layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
        }


        if (Utils.isModifiable) {

        }
        //if (bundle != null) {
        //imageGaleries = (ImageGalerie[]) bundle.getSerializable("imageGaleries");
        // business = (Business) bundle.getSerializable("business");


        //}


    }

    private void setAdapterData(final ArrayList<ImageGalerie> imageGalerieArrayList) {
        if (imageGalerieArrayList.size() > 0) {

            layoutEmtyMenuPhoto.setVisibility(View.GONE);
            gridviewPhoto.setVisibility(View.VISIBLE);
            try {
                //imageGalerieArrayList = new ArrayList<ImageGalerie>(Arrays.asList(imageGaleries)); do this in the previous activity
                photoAdapter = new PhotoAdapter(PhotosGridActivity.this, imageGalerieArrayList);
                gridviewPhoto.setAdapter(photoAdapter);
                registerForContextMenu(gridviewPhoto);
                gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        //layoutShadowImage = view.findViewById(R.id.layout_shadow_image_gallerie);
                        // layoutShadowImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                gridviewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        showViewPager();
                        viewPagerAdapter = new ViewPagerAdapter(getBaseContext(), imageGalerieArrayList, layoutDeleteBar, toolbar, tabDots, business);
                        viewPagerPhoto.setAdapter(viewPagerAdapter);
                        viewPagerPhoto.setCurrentItem(position);
                        tabDots.setupWithViewPager(viewPagerPhoto);
                        toolbar.setVisibility(View.GONE);
                        layoutDeleteBar.setVisibility(View.GONE);
                        viewPagerPhoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                idImage = imageGalerieArrayList.get(position).getId(); //this in modifiable
                                toolbar.setVisibility(View.GONE);
                                layoutDeleteBar.setVisibility(View.GONE);
                                tabDots.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                    }
                });

                rlDeletePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (idImage != 0) {
                            showDeleteDialogViewr();


                        }
                    }
                });
            } catch (NullPointerException e) {

            }

        } else {
            layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
            gridContent.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialogViewr() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PhotosGridActivity.this, R.style.AlertDialogCustom);

        builder.setTitle("Supprimer la photo");
        builder.setMessage("Voulez vous vraiment supprimer cette photo ?");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (idImage != 0) {
                    deleteImageBusinessModifiable(imageGalerieArrayList.get(indexPhoto).getId(), indexPhoto);
                }
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
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    private void showViewPager() {
        //WidgetUtils.changeStatusBarColorsGray(getActivity());
        //toolbar.setBackgroundColor(getResources().getColor(R.color.transparentb));
        rlPager.setVisibility(View.VISIBLE);
        gridviewPhoto.setVisibility(View.GONE);

    }

    private void deleteImageBusinessModifiable(int id, final int indexPhoto) {
        if (business != null) {

            if (business.getId() == 0) {
                imageGalerieArrayList.remove(indexPhoto);
                try {
                    if (imageGalerieArrayList.size() == 0) {
                        layoutEmtyMenuPhoto.setVisibility(View.VISIBLE);
                        gridContent.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {

                }
                refreshViewPager();
            } else {
                final ProgressDialog progress = new ProgressDialog(this);
                progress.setMessage(getString(R.string.loading_progress_dialog));
                progress.show();
                Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                        .deleteBusinessPhoto(SharedPreferencesFactory.getAdminToken(getBaseContext()), business.getId(), id);


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (progress != null && progress.isShowing()) {
                            progress.dismiss();


                        }
                        if (response.code() == 200) {

                            // new CustomToast(getContext(), getContext().getResources().getString(R.string.title_delete_popup_success), getContext().getString(R.string.message_delete_photo_success), R.drawable.ic_popup_delete, CustomToast.SUCCESS).show();
                            imageGalerieArrayList.remove(indexPhoto);
                            refreshViewPager();


                        } else {
                            // new CustomToast(getContext(), "Erreur", "une erreur est survenue lors de la suppression de cette image", R.drawable.ic_popup_delete, CustomToast.ERROR).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


                    }
                });
            }

        }

    }

    private void deleteImageBusinessAdded(int id) {
        //final ProgressDialog progress = new ProgressDialog(getBaseContext());
        // progress.setMessage(getString(R.string.loading_progress_dialog));
        //progress.show();
        imageGalerieArrayList.remove(id);
        refreshViewPager();
        SharedPreferencesFactory.saveListPhotosGallerie(getBaseContext(), imageGalerieArrayList);

    }

    private void refreshViewPager() {
        //final ProgressDialog progress = new ProgressDialog(getBaseContext());
        // progress.setMessage(getString(R.string.loading_progress_dialog));
        // progress.show();
        photoAdapter = new PhotoAdapter(PhotosGridActivity.this, imageGalerieArrayList);
        gridviewPhoto.setAdapter(photoAdapter);
        viewPagerAdapter = new ViewPagerAdapter(getBaseContext(), imageGalerieArrayList, layoutDeleteBar, toolbar, tabDots, business);
        // viewPagerAdapter.notifyDataSetChanged();
        viewPagerPhoto.setAdapter(viewPagerAdapter);
        photoAdapter.refresh(PhotosGridActivity.this, imageGalerieArrayList);
        if (imageGalerieArrayList.size() == 0) {
            imageGalerieArrayList.clear();
        }
        hideViewPager();
    }

    private void hideViewPager() {
        // WidgetUtils.changeStatusBarColors(getActivity(), getResources().getColor(R.color.blue_vynd_manager));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        gridviewPhoto.setVisibility(View.VISIBLE);
        rlPager.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        if (rlPager.isShown() && !toolbar.isShown()) {
            hideViewPager();
            rlPager.setVisibility(View.GONE);
            gridviewPhoto.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        } else {
            //SharedPreferencesFactory.saveListPhotosGallerie(getBaseContext(), imageGalerieArrayList);
            registerDataInformations();
            finish();
        }

    }

    private void addImagesList(Business business, ArrayList<ImageGalerie> img) {
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

    private void registerDataInformations() {


        if (imageGalerieArrayList != null) {
            if (imageGalerieArrayList.size() != 0) {
                try {
                    business.setImages(imageGalerieArrayList);
                } catch (NullPointerException e) {
                    business.setImages(new ArrayList<ImageGalerie>());
                }
                if (isFromProfile) {
                    try {
                        img = new ArrayList<>();
                        String toRemove = "https";
                        for (int i = 0; i < business.getImages().size(); i++) {
                            if (!business.getImages().get(i).getImage().substring(0, 5).equalsIgnoreCase(toRemove)) {
                                img.add(business.getImages().get(i));
                            }

                        }

                        addImagesList(business, img);


                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());

                    }
                }

            }
        }
        Intent intent = new Intent();
        intent.putExtra("businessImage", business);
        intent.setAction("refresh_state_image");
        sendBroadcast(intent);
        finish();

    }

    private void getListPhotoBusiness(final int businessID) {
        // this is true is isModifiable = true
       /* final ProgressDialog progress = new ProgressDialog(getBaseContext());
        //progress.setMessage(getString(R.string.loading_progress_dialog));
        progress.show();
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                .getListPhotoByBusiness(businessID, 0, 10);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();


                }
                Gson gson = Utils.getGsonInstance();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());

                    if (response.code() == 200) {
                        JSONArray jsonArrayItems = jsonObject.getJSONArray("items");

                        if (jsonArrayItems.length() == 0) {


                        } else {
                            imageGaleries = new ImageGalerie[jsonArrayItems.length()];
                            for (int i = 0; i < jsonArrayItems.length(); i++) {
                                imageGaleries[i] = gson.fromJson(jsonArrayItems.get(i).toString(), ImageGalerie.class);
                            }
                            business.setImageGaleries(imageGaleries);
                             imageGalerieArrayList = new ArrayList<ImageGalerie>(Arrays.asList(imageGaleries));
                           try {
                                photoAdapter = new PhotoAdapter(PhotosGridActivity.this, imageGalerieArrayList);
                                gridviewPhoto.setAdapter(photoAdapter);
                                viewPagerAdapter = new ViewPagerAdapter(getBaseContext(), imageGalerieArrayList, layoutDeleteBar, toolbar, PhotosGridFragment.this, tabDots, business);
                                viewPagerPhoto.setAdapter(viewPagerAdapter);
                                photoAdapter.refresh(PhotosGridActivity.this, imageGalerieArrayList);
                                if (imageGalerieArrayList.size() == 0) {
                                    imageGalerieArrayList.clear();
                                }
                                hideViewPager();


                            } catch (NullPointerException e) {
                                if (!isAlertShown)
                                    showErrorLoadingDialog();

                            }

                        }

                    } else {
                        if (!isAlertShown)
                            showErrorLoadingDialog();

                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    if (!isAlertShown)
                        showErrorLoadingDialog();


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //WidgetUtils.hideProgressDialog(mProgressDialog,getActivity());
                if (getBaseContext() != null) {
                    progress.dismiss();
                    if (!isAlertShown)
                        showErrorLoadingDialog();
                }


            }
        });*/
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            if (viewPagerPhoto.isShown()) {
                hideViewPager();

            } else {
                //SharedPreferencesFactory.saveListPhotosGallerie(getBaseContext(), imageGalerieArrayList);
                registerDataInformations();
                finish();
            }
        }
        if (v == ivAddPhoto) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
            }


        }
        if (v == layoutEmtyMenuPhoto) {
            if (hasPermissions(this, Const.MY_PERMISSIONS_REQUEST_STORAGE, PERMISSIONS_PHOTO)) {
                uploadImageDialog();
            }

        }

    }

    private void uploadImageDialog() {
        layoutEmtyMenuPhoto.setVisibility(View.GONE);
        gridContent.setVisibility(View.VISIBLE);
        gridviewPhoto.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PhotosGridActivity.this, R.style.AlertDialogCustom);

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
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
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
                Log.e("ggg", croppedImage.toString());
                //updateImageForEdit(); this is used if isModifiable is true
                // updateImageForAdd();
            } else if (resultCode == UCrop.RESULT_ERROR) {

            }
        }
    }

    private void updateImageForAdd(Uri croppedImage) {
        ImageGalerie imgToAdd = new ImageGalerie(imageGalerieArrayList.size(), croppedImage, getBaseContext());
        if (imgToAdd != null) {
            System.out.println("hne");
            imageGalerieArrayList.add(imgToAdd);
            layoutEmtyMenuPhoto.setVisibility(View.GONE);
            gridContent.setVisibility(View.VISIBLE);
            if (SharedPreferencesFactory.getListPhotosGallerie(getBaseContext()) != null) {
                if (photoAdapter == null) {
                    photoAdapter = new PhotoAdapter(getBaseContext(), imageGalerieArrayList);
                }
                photoAdapter.refresh(PhotosGridActivity.this, imageGalerieArrayList);
            } else {
                photoAdapter = new PhotoAdapter(getBaseContext(), imageGalerieArrayList);
                Log.e("ttt", imgToAdd.toString());
                viewPagerAdapter.notifyDataSetChanged();
                photoAdapter.refresh(PhotosGridActivity.this, imageGalerieArrayList);
            }
            gridviewPhoto.setAdapter(photoAdapter);
            gridviewPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    return false;
                }
            });
            registerForContextMenu(gridviewPhoto);
            viewPagerAdapter.notifyDataSetChanged();


        }
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
        ChosenImage chosenImage = list.get(0);
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setToolbarTitle("Ajouter photo");
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.white));


        UCrop.of(Uri.fromFile(new File((list.get(0).getOriginalPath()))),
                Uri.fromFile(new File((list.get(0).getOriginalPath()))))
                .withOptions(options)
                .withMaxResultSize(1000, 1000)
                .start(this);
        updateImageForAdd(Uri.fromFile(new File(chosenImage.getOriginalPath())));

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        indexPhoto = info.position;

        menu.add(0, v.getId(), 0, "Supprimer");  // Add element "Edit"

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Supprimer") // "Edit" chosen
        {
            showDeleteDialog();

        } else {
            return false;
        }

        return true;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PhotosGridActivity.this, R.style.AlertDialogCustom);

        builder.setTitle("Supprimer la photo");
        builder.setMessage("Voulez vous vraiment supprimer cette photo ?");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                deleteImageBusinessModifiable(imageGalerieArrayList.get(indexPhoto).getId(), indexPhoto);
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
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }


}
