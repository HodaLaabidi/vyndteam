package com.vyndsolutions.vyndteam.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.activities.MenuActivity;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Hoda on 07/03/2018.
 */

public class MenuPhotoAdapterr extends BaseAdapter {

    NetworkImageView ivPhoto;
    private ArrayList<ImageGalerie> imageGalerieArrayList;
    ProgressBar progressBar;
    boolean isActivated = false;
    public RelativeLayout layoutShadowImage;
    NetworkImageView ivEdit, ivDeletePhoto;
    MenuActivity menuActivity;
    CardView cardOneImageMenu;
    private ImageGalerie imageGalerie;
    Business business;
    private static final int SEARCH_RESULT_LIMIT = 10;
    private final String TAG_ROOT_ELEMENT = "items";
    private int offset = 0;
    private boolean state = false;
    private int size;
    private boolean isWaitingForLoad;


    public MenuPhotoAdapterr(MenuActivity menuActivity, ArrayList<ImageGalerie> imageGaleries, NetworkImageView ivEdit, boolean isActivated, Business business) {
        this.menuActivity = menuActivity;
        this.imageGalerieArrayList = imageGaleries;
        this.ivEdit = ivEdit;
        this.isActivated = isActivated;
        this.business = business;
        System.out.println(business.getId());
       // size = imageGalerieArrayList.size() - 9;


    }

    public void swap() {
        switchState();
        //if (currentPage < totalPages) {
        //getBusinessMenu(business.getId());
        //}
    }

    public void switchState() {
        this.state = !this.state;
    }

    public Boolean isNotified() {
        return this.state;
    }

    @Override
    public int getCount() {
        return imageGalerieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageGalerieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageGalerieArrayList.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_photo_menu, parent, false);

        ivPhoto = view.findViewById(R.id.iv_photo);
        progressBar = view.findViewById(R.id.progressBar);
        ivDeletePhoto = view.findViewById(R.id.iv_delete_photo);
        layoutShadowImage = view.findViewById(R.id.layout_shadow_image);
        ivDeletePhoto = view.findViewById(R.id.iv_delete_photo);
        cardOneImageMenu = view.findViewById(R.id.card_one_image_menu);
        progressBar.setVisibility(View.VISIBLE);
        /*if (position == size && !isNotified()) {
            swap();
        }
        try {
            if (position == getCount() - 1 && isWaitingForLoad) {
                menuActivity.progressBarPhoto.setVisibility(View.VISIBLE);
            }

        }catch (IndexOutOfBoundsException e)
        {

        }*/
        if (imageGalerieArrayList.size() != 0) {
            ivPhoto.setImageUrlWithoutBorderWithResizing(imageGalerieArrayList.get(position).getImage(), menuActivity, progressBar);

        }
        if (isActivated == false) {

            layoutShadowImage.setVisibility(View.GONE);
            ivDeletePhoto.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardOneImageMenu.setElevation(0f);
            }
            //menuActivity.stopAnimation();
        }
        if (isActivated == true) {

            layoutShadowImage.setVisibility(View.VISIBLE);
            ivDeletePhoto.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardOneImageMenu.setElevation(15f);
            }
            // menuActivity.gridViewItemAnimation();
        }
        ivDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuActivity.showDeleteDialog(imageGalerieArrayList.get(position).getId());
            }
        });

      /*  menuActivity.setOnAnimationListener(new menuActivity.OnAnimation() {
            @Override
            public boolean isAnimated(boolean isAnimated) {
                if (isAnimated) {
                   layoutShadowImage.setVisibility(View.VISIBLE);
                   notifyDataSetChanged();
                    System.out.println("true");
                } else {
                    layoutShadowImage.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    System.out.println("false");
                }

                return true;
            }
        });
*/

        return view;
    }

    public void refresh(ArrayList<ImageGalerie> imageGaleries, boolean isActivated) {
        this.isActivated = isActivated;
        this.imageGalerieArrayList = imageGaleries;
        notifyDataSetChanged();
    }

   /* private void getBusinessMenu(int businessID) {
        menuActivity.progressBarPhoto.setVisibility(View.VISIBLE);
        isWaitingForLoad = true;

        offset += 10;

        WidgetUtils.disableUserInteraction(menuActivity.getActivity());
        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessApiRetrofitServiceClient()
                .getMenu(Utils.token, businessID, offset, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (menuActivity.getActivity() != null) {
                    menuActivity.progressBarPhoto.setVisibility(View.GONE);
                    WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                }
                Gson gson = Utils.getGsonInstance();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (response.code() == 200) {
                        JSONArray jsonArrayImages = jsonObject.getJSONArray("images");
                        if (jsonArrayImages.length() == 0) {


                        } else {
                            if (jsonArrayImages.length() != 0) {

                                for (int i = 0; i < jsonArrayImages.length(); i++) {
                                    imageGalerie = gson.fromJson(jsonArrayImages.get(i).toString(), ImageGalerie.class);
                                    imageGalerieArrayList.add(imageGalerie);
                                }

                            }
                            if (imageGalerieArrayList.size() != 0) {

                                size += jsonArrayImages.length();
                                notifyDataSetChanged();


                            }

                            if (jsonArrayImages.length() > 0) {
                                switchState();
                            }
                            WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                        }

                    } else {
                        WidgetUtils.enableUserInteraction(menuActivity.getActivity());

                    }
                    isWaitingForLoad = false;
                } catch (JSONException | IOException | NullPointerException e) {
                    WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                    isWaitingForLoad = false;

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //WidgetUtils.hideProgressDialog(mProgressDialog,getActivity());
                isWaitingForLoad = false;
                if (menuActivity.getActivity() != null) {
                    WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                    menuActivity.progressBarPhoto.setVisibility(View.GONE);
                }

            }
        });
    }*/

}
