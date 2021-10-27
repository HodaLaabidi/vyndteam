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
import com.vyndsolutions.vyndteam.factories.SubCategoriesServiceFactory;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.SubCategory;
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

import static com.vyndsolutions.vyndteam.activities.AddBusinessActivity.ivCategorie;
import static com.vyndsolutions.vyndteam.activities.AddBusinessActivity.listCategories;


/**
 * Created by Hoda on 26/03/2018.
 */


    public class SubCategoryAdapter extends RecyclerView.Adapter<com.vyndsolutions.vyndteam.adapters.SubCategoryAdapter.MyViewHolder> {
    private static final int SEARCH_RESULT_LIMIT = 20;
    Context context;
    List<Classification> subCategories;
    private boolean state = false;
    private int currentPage = 1;
    private int offset = 0;
    private int size;
    private OnItemAddClickListener ListenerAddCategories;

    public SubCategoryAdapter(OnItemAddClickListener ListenerAddCategories,Context context, List<Classification> subCategories) {
        this.subCategories = subCategories;
        this.context = context;
        this.ListenerAddCategories = ListenerAddCategories;
        size = getItemCount() - 10;
    }

    @Override
    public com.vyndsolutions.vyndteam.adapters.SubCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_subcategory, null);
        return new SubCategoryAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(final com.vyndsolutions.vyndteam.adapters.SubCategoryAdapter.MyViewHolder holder, final int position) {
        final Classification subCategory = subCategories.get(position);

        holder.label.setText(subCategory.getLabel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCategories.add(0, subCategory);
                ListenerAddCategories.onItemAddClickListener(view);
                // AddBusinessActivity.et_categories.setText(subCategory.getLabel());
                listCategories.setVisibility(View.GONE);
                ivCategorie.setImageResource(R.drawable.check);
                notifyDataSetChanged();
            }

        });

        // upload images
        //Picasso.with(context).load(subCategory.getIcon()).transform(new RoundedTransformation(50, 4)).resize(100, 100).into(holder.icon);
        holder.icon.setImageUrlWithoutBorder(subCategory.getIcon());
        Log.d("swap", "onBindViewHolder: " + position + " " + size);
        if (position == size && !isNotified()) {
            Log.d("swap", "onBindViewHolder: " + position + " " + size);
            swap();

        }


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
        final List<SubCategory> subCategoriesList = new ArrayList<SubCategory>();

        postParams.addProperty("type", 0);

        postParams.addProperty("keyword", "");
        call = SubCategoriesServiceFactory.getSubCategoryService().getSubCategories(postParams, offset, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            Classification subCategory = gson.fromJson(jsonArray.get(i).toString(), Classification.class);
//                            newBusiness.random = (int) (Math.random() * 3);
                            subCategories.add(subCategory);
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
        return subCategories.size();
    }

    private boolean isNotified() {
        return this.state;
    }


    public interface OnItemAddClickListener {
        void onItemAddClickListener(View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        LinearLayout itemSubCategorie;
        NetworkImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label_subCategory);
            icon = itemView.findViewById(R.id.icon_subcategory);
            itemSubCategorie = itemView.findViewById(R.id.item_subCategorie);
        }
    }

}