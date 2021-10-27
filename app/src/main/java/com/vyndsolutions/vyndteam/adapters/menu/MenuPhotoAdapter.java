package com.vyndsolutions.vyndteam.adapters.menu;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ImageGalerie;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Hoda on 06/03/2018.
 */

public class MenuPhotoAdapter extends BaseAdapter {

    public RelativeLayout layoutShadowImage;
    NetworkImageView ivPhoto;
    ProgressBar progressBar;
    boolean isActivated = false;
    NetworkImageView ivEdit, ivDeletePhoto;
    CardView cardOneImageMenu;
    Context context;
    private ArrayList<ImageGalerie> imageGalerieArrayList;
    private OnItemClickListenerDeleteDialog listenerDeleteDialog;
    private OnItemClickListenerStopAnimation listenerStopAnimation;
    private OnItemClickListenerGridAnimation listenerGridAnimation;



    public MenuPhotoAdapter( OnItemClickListenerDeleteDialog listenerDeleteDialog,OnItemClickListenerStopAnimation listenerStopAnimation,OnItemClickListenerGridAnimation listenerGridAnimation,
                             Context context, ArrayList<ImageGalerie> imageGaleries, NetworkImageView ivEdit, boolean isActivated) {
        this.context = context;
        this.imageGalerieArrayList = imageGaleries;
        this.ivEdit = ivEdit;
        this.isActivated=isActivated;
        this.listenerDeleteDialog = listenerDeleteDialog;
        this.listenerGridAnimation = listenerGridAnimation;
        this.listenerStopAnimation = listenerStopAnimation;



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

        if (imageGalerieArrayList.size() != 0) {
            ivPhoto.setImageUrlWithoutBorderWithoutResizing(imageGalerieArrayList.get(position).getUrlxS(),R.drawable.empty_business_photo,progressBar);

        }
        if(isActivated==false)
        {

            layoutShadowImage.setVisibility(View.GONE);
            ivDeletePhoto.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardOneImageMenu.setElevation(0f);
            }
            //view it can be false variable
            listenerStopAnimation.onItemClickListenerStopAnimationt(view);
            //context.stopAnimation();
        }
        if(isActivated==true) {

            layoutShadowImage.setVisibility(View.VISIBLE);
            ivDeletePhoto.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardOneImageMenu.setElevation(15f);
            }
            // context.gridViewItemAnimation();
            //view it can be false variable
            listenerGridAnimation.onItemClickListenerGridAnimation(view);
        }
        ivDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Menu2Activity.idPositionArrayGallerie =imageGalerieArrayList.get(position).getId();
                listenerDeleteDialog.onItemClickListenerDeleteDialog(v);
               // context.showDeleteDialog(imageGalerieArrayList.get(position).getId());
            }
        });
    /*   Menu2Activity.setOnAnimationListener(new Menu2Activity.OnAnimation() {
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
        });*/


        return view;
    }

    public  void refresh(ArrayList<ImageGalerie> imageGaleries, boolean isActivated)
    {   this.isActivated = isActivated;
        this.imageGalerieArrayList = imageGaleries;
        notifyDataSetChanged();
    }

    public interface OnItemClickListenerStopAnimation{
        void onItemClickListenerStopAnimationt(View v);
    }

    public interface OnItemClickListenerDeleteDialog{
        void onItemClickListenerDeleteDialog(View v);
    }

    public interface OnItemClickListenerGridAnimation{
        void onItemClickListenerGridAnimation(View v);
    }




}
