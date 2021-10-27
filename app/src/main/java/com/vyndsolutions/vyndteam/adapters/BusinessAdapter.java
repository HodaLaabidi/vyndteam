package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.activities.ItemBusinessActivity;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hoda on 22/02/2018.
 */


public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {
    private static final int SEARCH_RESULT_LIMIT = 20;
    Context context;
    List<Business> businesses;
    private boolean state = false;
    private View progressBar;
    private JsonObject postParams;
    private int currentPage = 1;
    private int offset = 0;
    private int size;

    public BusinessAdapter(Context context, List<Business> businesses , JsonObject postParams, View progressBar) {
        this.businesses = businesses;
        this.context = context;
        this.progressBar = progressBar;
        this.postParams = postParams;
        size = getItemCount() - 10;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item, null);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (position == size && !isNotified()) {
            Log.d("swap", "onBindViewHolder: " + position + " " + size);
            swap();
        }

        final Business business = businesses.get(position);
        holder.address.setText(business.getAdress());
        holder.name.setText(business.getName());
        if (business.getSubCategory() != null)
            holder.subCategory.setText(business.getFirstSubCategory().getLabel());
        else holder.subCategory.setVisibility(View.GONE);

        if (business.getRegion() != null && business.getRegion().getLabel() != null)
            holder.region.setText(business.getRegion().getLabel() + "");

        holder.rang.setText((position + 1) + ".");
        holder.ligne.setVisibility(View.VISIBLE);
        holder.itemBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemBusinessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("business", business);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        Boolean claimedBusiness = business.getClaimed();
        if (claimedBusiness) {
            holder.claim.setVisibility(View.VISIBLE);
            holder.repView.setVisibility(View.VISIBLE);
        } else {
            holder.claim.setVisibility(View.GONE);
            holder.repView.setVisibility(View.GONE);

        }

        if (business.isValid()) {
            holder.blocked.setVisibility(View.GONE);
        } else {
            holder.blocked.setVisibility(View.VISIBLE);
        }
        // upload images
        //holder.image.setImageUrlWithCustomBorderColor(business.getImg().getUrlxS(), "#B1BAC4", 3 , R.drawable.ic_image_placeholder);
        holder.image.setImageUrlWithCustomBorderColor(business.getImg().getUrl(), "#B1BAC4", 15, R.drawable.empty_business_photo);


    }

    public Boolean isNotified() {
        return this.state;
    }

    private void swap() {
        switchState();
        //if (currentPage < totalPages) {
        updateList();
        //}
    }

    public void clear() {
        if (businesses != null) {
            businesses.clear();
            notifyDataSetChanged();
        }
    }

    private void switchState() {
        this.state = !this.state;

    }

    private void updateList() {

        currentPage++;

        //MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(context, MixPanelUtils.projectToken);

        offset += 20;
        // LoggingFactory.addUserSearchBusinessLog(mixpanelAPI, postParams);

        Log.d("offset", "updateList: " + offset);
        Call<ResponseBody> call = null;
//        if (CompteManagerService.getCurrentCompte(context) != null)
//            call = RetrofitServiceFacotry.getBusinessApiRetrofitServiceClient().searchBusinessV2(Utils.token, postParams, offset, SEARCH_RESULT_LIMIT);
//        else


        call = RetrofitServiceFacotry.getBusinessService().getDefaultBusinesses(postParams, offset, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // use a linear layout manager
                //Gson gson = Utils.getGsonInstance();
                Gson gson = new Gson();
                JSONArray jsonArray;
                try {
                    //Log.d("result", response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = response.code();
                    if (code != 200) {
//                        DialogFactoryUtils.showAlertDialog(context, context.getString(R.string.service_error),
//                                "Vynder !");
                    } else {
                        jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Business business = gson.fromJson(jsonArray.get(i).toString(), Business.class);
//
                            businesses.add(business);
                        }
                        size += jsonArray.length();
                        notifyDataSetChanged();
                        /*
                        if (currentPage < totalPages) {
                            switchState();
                        }
                        */
                        if (jsonArray.length() > 0) {
                            switchState();
                        }
                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    e.printStackTrace();
                }
                //progressBar.setVisibility(View.GONE);
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressDialog.dismiss();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView subCategory;
        TextView address, region, rang;
        ImageView iv_delete, claim, blocked;
        View repView;
        LinearLayout ligne;
        NetworkImageView image;
        LinearLayout itemBusiness;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemBusiness = itemView.findViewById(R.id.item_business);
            name = itemView.findViewById(R.id.name);
            subCategory = itemView.findViewById(R.id.subCategory);
            address = itemView.findViewById(R.id.adress);
            image = itemView.findViewById(R.id.img);
            region = itemView.findViewById(R.id.region);
            rang = itemView.findViewById(R.id.business_rang);
            claim = itemView.findViewById(R.id.claim);
            blocked = itemView.findViewById(R.id.blocked);
            ligne = itemView.findViewById(R.id.ll_ligne);
            repView = itemView.findViewById(R.id.rep_view);
        }
    }
}

