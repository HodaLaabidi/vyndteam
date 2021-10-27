package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.activities.AddBusinessActivity;
import com.vyndsolutions.vyndteam.factories.RetrofitServiceFacotry;
import com.vyndsolutions.vyndteam.models.Region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vyndsolutions.vyndteam.activities.AddBusinessActivity.isSelectedRegion;
import static com.vyndsolutions.vyndteam.activities.AddBusinessActivity.listRegions;

/**
 * Created by Hoda on 26/03/2018.
 */


public class RegionAdapter extends RecyclerView.Adapter<com.vyndsolutions.vyndteam.adapters.RegionAdapter.MyViewHolder>{
    private static final int SEARCH_RESULT_LIMIT = 20;
    Context context;
    List<Region> regions ;
    private boolean state = false;
    private int currentPage = 1;
    private int offset = 0;
    private int size;

    public RegionAdapter(Context context, List<Region> regions) {
        this.regions = regions;
        this.context = context;
        size = getItemCount() - 10;
    }

    @Override
    public com.vyndsolutions.vyndteam.adapters.RegionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_region,null);
        return new RegionAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(final com.vyndsolutions.vyndteam.adapters.RegionAdapter.MyViewHolder holder, final int position) {
        final Region region = regions.get(position);

        holder.label.setText(region.getLabel());
        holder.itemV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelectedRegion = true ;
                AddBusinessActivity.selectedRegion = region;
                AddBusinessActivity.etRegion.setText(region.getLabel());
                listRegions.setVisibility(View.GONE);
                AddBusinessActivity.ivRegion.setImageResource(R.drawable.check);



            }

        });
            swap();



    }

    private void swap() {
        switchState();
        //if (currentPage < totalPages) {
        updateList();
        //}
    }

    private void updateList() {

        currentPage++;

        //MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(context, MixPanelUtils.projectToken);

        offset += 20;
        // LoggingFactory.addUserSearchBusinessLog(mixpanelAPI, postParams);

        Log.d("offset", "updateList: " + offset);
        Call<ResponseBody> call = null;


        JsonObject postParams = new JsonObject();
        postParams.addProperty("type", 1);
        postParams.addProperty("keyword", "");
        postParams.addProperty("parentId", 0);
         call = RetrofitServiceFacotry.getGeneralInfoService().getAllRegions(postParams, offset, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() != 200) {

                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject jObject = new JSONObject(response.body().string());
                        JSONArray jArray = jObject.getJSONArray("items");
                        for (int i = 0; i < jArray.length(); i++) {
                            Region region = gson.fromJson(jArray.getJSONObject(i).toString() + "", Region.class);
                            regions.add(region);

                        }
                        size += jArray.length();
                        notifyDataSetChanged();
                        if (jArray.length() > 0) {
                            switchState();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void switchState() {
        this.state = !this.state;

    }

    @Override
    public int getItemCount() {
        return regions.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView label ;
        LinearLayout itemV ;


        public MyViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_region);
            itemV = itemView.findViewById(R.id.item_region);
        }
    }


}
