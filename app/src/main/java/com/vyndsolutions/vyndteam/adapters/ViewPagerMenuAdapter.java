
package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vyndsolutions.vyndteam.R;

import com.vyndsolutions.vyndteam.activities.MenuActivity;
import com.vyndsolutions.vyndteam.models.ImageGalerie;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;




/**
 * Created by Hoda on 27/04/2018.
 */



public class ViewPagerMenuAdapter extends  PagerAdapter {
    Context context;
    ArrayList<ImageGalerie> imageGalerieArrayList;
    MenuActivity menuActivity;

    public ViewPagerMenuAdapter(Context context, ArrayList<ImageGalerie> imageGalerieArrayList, MenuActivity menuActivity) {
        this.context = context;
        this.imageGalerieArrayList = imageGalerieArrayList;
        this.menuActivity = menuActivity;
    }

    public ViewPagerMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageGalerieArrayList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_item_pager, container, false);


        ProgressBar progressBar = itemView.findViewById(R.id.progressBar);
        final ImageViewTouch ivPhotoPager = itemView.findViewById(R.id.iv_view_pager);
        setImageUri(imageGalerieArrayList.get(position).getImage(), context, progressBar, ivPhotoPager);
        ivPhotoPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public static void setImageUri(final String imageURL, final Context context, final ProgressBar progressBar, final ImageViewTouch circleImageView) {

        if (imageURL != null && context != null) {
//            progressBar.setVisibility(View.VISIBLE);
            Glide
                    .with(context)
                    .load(imageURL)
                    .error(R.drawable.black)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                          //  progressBar.setVisibility(View.GONE);
                            if (isFirstResource)
                                setImageUri(imageURL, context, progressBar, circleImageView);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                           // progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(circleImageView);
        }
    }
}

