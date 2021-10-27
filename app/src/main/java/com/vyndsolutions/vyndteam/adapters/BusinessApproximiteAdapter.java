package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.activities.HomeActivity;
import com.vyndsolutions.vyndteam.activities.ItemBusinessActivity;
import com.vyndsolutions.vyndteam.factories.BusinessServiceFactory;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.factories.SubCategoriesServiceFactory;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.SubCategory;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.vyndsolutions.vyndteam.utils.RoundedTransformation;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hoda on 22/02/2018.
 */


public class BusinessApproximiteAdapter extends RecyclerView.Adapter<BusinessApproximiteAdapter.MyViewHolder>{
    Context context;
    List<Business> businesses;
    Location currentLocation;
    private boolean state = false;
    private static final int SEARCH_RESULT_LIMIT = 20;
    private int currentPage = 1;
    private int offset = 0;
    private int size;
    public BusinessApproximiteAdapter(Context context, List<Business> businesses, Location currentLocation) {
        this.businesses = businesses;
        this.context = context;
        this.currentLocation = currentLocation ;
        size = getItemCount() - 10;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item,null);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Business business = businesses.get(position);
        holder.address.setText(business.getAdress());
        holder.name.setText(business.getName());
        //holder.subCategory.setText(business.getSubCategory());
        holder.region.setText(business.getRegion().getLabel()+"");
        holder.rang.setText((position+1)+".");
        holder.ligne.setVisibility(View.VISIBLE);
        holder.itemBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ItemBusinessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("business",business);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        Boolean claimedBusiness = business.getClaimed();
        if (claimedBusiness){
            holder.claim.setVisibility(View.VISIBLE);
            holder.blocked.setVisibility(View.GONE);
            holder.repView.setVisibility(View.VISIBLE);
        }else {
            holder.claim.setVisibility(View.GONE);
            holder.blocked.setVisibility(View.VISIBLE);
            holder.repView.setVisibility(View.GONE);

        }
        // upload images
        holder.image.setImageUrlWithCustomBorderColor(business.getImg().getUrlxM(), "#EDF0F5", 2 , R.drawable.ic_image_placeholder);
        swap();

    }
    private void swap() {
        switchState();
        //if (currentPage < totalPages) {
        updateList();
        //}
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


        JsonObject postParams = new JsonObject();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", 0);

        jsonObject.addProperty("label","");
        jsonObject.addProperty("type", 0);

        JsonArray jsonArray= new JsonArray();
        jsonArray.add(jsonObject);


        postParams.add("tags", jsonArray);
        postParams.addProperty("latitude",currentLocation.getLatitude());
        postParams.addProperty("longitude",currentLocation.getLongitude());
        postParams.addProperty("distance",1);
        postParams.addProperty("open",0);
        postParams.addProperty("coupon",0);
        postParams.addProperty("budget",0);
        postParams.addProperty("searchId",0);
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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView subCategory;
        TextView address , region , rang;
        ImageView  iv_delete, claim , blocked ;
        View repView ;
        LinearLayout ligne;
        NetworkImageView image ;
        LinearLayout itemBusiness ;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemBusiness =  itemView.findViewById(R.id.item_business);
            name =  itemView.findViewById(R.id.name);
            subCategory = itemView.findViewById(R.id.subCategory);
            address =  itemView.findViewById(R.id.adress);
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
