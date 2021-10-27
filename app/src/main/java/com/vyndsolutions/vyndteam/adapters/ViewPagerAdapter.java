
package com.vyndsolutions.vyndteam.adapters;


/**
 * Created by Hoda on 22/04/2018.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.ImageGalerie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;


public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<ImageGalerie> imageGalerieArrayList;
    RelativeLayout layoutDeleteBar;
    Toolbar toolbar;
    TabLayout tabDots;
    Business business;
    private static final int SEARCH_RESULT_LIMIT = 10;
    private final String TAG_ROOT_ELEMENT = "items";
    private int offset = 0;
    private boolean state = false;
    private int size;
    Gson gson;
    private boolean isWaitingForLoad;
    private ImageGalerie imageGaleries;


    public ViewPagerAdapter(Context context, ArrayList<ImageGalerie> imageGalerieArrayList, RelativeLayout layoutDeleteBar, Toolbar toolbar,TabLayout tabDots
            ,Business business) {
        this.context=context;
        this.imageGalerieArrayList=imageGalerieArrayList;
        this.layoutDeleteBar = layoutDeleteBar;
        this.toolbar = toolbar;
        this.tabDots = tabDots;
        this.business = business;
        size = imageGalerieArrayList.size()-9;
    }

    public ViewPagerAdapter(Context context)
    {
        this.context = context;
    }

    public void swap() {
        switchState();
        //if (currentPage < totalPages) {
       // getListPhotoBusiness(business.getId());
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
    public boolean isViewFromObject(View view, Object object) {
        return view == ( object);
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_item_pager, container, false);


        ProgressBar progressBar = itemView.findViewById(R.id.progressssBar);
        final ImageViewTouch ivPhotoPager =  itemView.findViewById(R.id.iv_view_pager);
        /*if (position == size && !isNotified()) {
            swap();
        }*/
        /*if (position == getCount() - 1 && isWaitingForLoad) {
           // photosGridFragment.progressBarPhoto.setVisibility(View.VISIBLE);
        }*/
        if(imageGalerieArrayList.get(position).getImage()!=null)
            setImageUri(imageGalerieArrayList.get(position).getImage(),context,progressBar,ivPhotoPager);
        ivPhotoPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
            }
        });
       /* ivPhotoPager.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                if(!toolbar.isShown())
                {   Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                   Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    layoutDeleteBar.startAnimation(slideUp);
                    //toolbar.startAnimation(slideDown);
                    tabDots.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    layoutDeleteBar.setVisibility(View.VISIBLE);
                }
            }
        });
        ivPhotoPager.setDoubleTapListener(new ImageViewTouch.OnImageViewTouchDoubleTapListener() {
            @Override
            public void onDoubleTap() {
                tabDots.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                layoutDeleteBar.setVisibility(View.GONE);

            }
        });*/

        container.addView(itemView);

        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public static void setImageUri(final String imageURL, final Context context, final ProgressBar progressBar, final ImageViewTouch circleImageView) {

        if (imageURL != null && context != null) {

            Glide
                    .with(context)
                    .load(imageURL)
                   //.error(R.drawable.feedback_popup_disable_1)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            if (isFirstResource)
                                setImageUri(imageURL, context, progressBar, circleImageView);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(circleImageView);
        }
    }

    private void getListPhotoBusiness(int businessID) {
       // if(context!=null)
           // photosGridFragment.progressBarPhoto.setVisibility(View.VISIBLE);

        isWaitingForLoad = true;

        offset += 10;



        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessService()
                .getImagesFromId(businessID,offset,SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(context!=null)
                   // photosGridFragment.progressBarPhoto.setVisibility(View.GONE);

                 gson = new Gson();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());

                    if (response.code() == 200) {
                        JSONArray jsonArrayItems = jsonObject.getJSONArray("items");

                        if (jsonArrayItems.length() == 0) {


                        } else {

                            for (int i = 0; i < jsonArrayItems.length(); i++) {
                                imageGaleries = gson.fromJson(jsonArrayItems.get(i).toString(), ImageGalerie.class);
                                imageGalerieArrayList.add(imageGaleries);
                            }

                            if (imageGalerieArrayList.size() != 0) {

                                size += jsonArrayItems.length();
                                notifyDataSetChanged();


                            }

                            if (jsonArrayItems.length() > 0) {
                                switchState();
                            }
                        }

                    } else {


                    }
                    isWaitingForLoad = false;
                } catch (JSONException | IOException | NullPointerException e) {

                    isWaitingForLoad = false;

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //WidgetUtils.hideProgressDialog(mProgressDialog,getActivity());
                isWaitingForLoad = false;
              //  if(context!=null)
                   // photosGridFragment.progressBarPhoto.setVisibility(View.GONE);


            }
        });
    }

}

