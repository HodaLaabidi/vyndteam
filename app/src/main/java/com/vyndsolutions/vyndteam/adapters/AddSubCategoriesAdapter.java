package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.List;

/**
 * Created by HP on 07/05/2018.
 */


public class AddSubCategoriesAdapter extends RecyclerView.Adapter<com.vyndsolutions.vyndteam.adapters.AddSubCategoriesAdapter.MyViewHolder>{
    Context context;
    List<Classification> subCategories ;
   public AddSubCategoriesAdapter(Context context, List<Classification> subCategories) {
        this.subCategories = subCategories;
        this.context = context;

    }

    @Override
    public com.vyndsolutions.vyndteam.adapters.AddSubCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_add_subcategory,null);
        return new AddSubCategoriesAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(final com.vyndsolutions.vyndteam.adapters.AddSubCategoriesAdapter.MyViewHolder holder, final int position) {
        final Classification subCategorie = subCategories.get(position);
        holder.textItemSubcategory.setText(subCategorie.getLabel());
        holder.imageCategory.setImageUrlWithCustomBorderColor(subCategorie.getIcon(),"#D2D7DE", 2,R.drawable.empty_business_photo);
      /*  holder.itemDeleteSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBusinessActivity.subcategories.remove(position);
                if(AddBusinessActivity.subcategories.size() == 0){
                    ivCategorie.setImageResource(R.drawable.text_warning_icon);
                    et_categories.setVisibility(View.VISIBLE);
                    et_categories.setHint("Choisir des catégories");
                    et_categories.setHintTextColor(context.getResources().getColor(R.color.gray_2));
                    llAddCategories.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });*/


    }





    @Override
    public int getItemCount() {
        return subCategories.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textItemSubcategory ;
        LinearLayout itemAddSubcategory ;
        ImageView itemDeleteSubcategory;
        NetworkImageView imageCategory;


        public MyViewHolder(View itemView) {
            super(itemView);

            textItemSubcategory = itemView.findViewById(R.id.text_item_subcategories);
            itemAddSubcategory = itemView.findViewById(R.id.item_add_subcategory);
            itemDeleteSubcategory = itemView.findViewById(R.id.item_delete_subcategory);
            imageCategory = itemView.findViewById(R.id.image_category);
        }
    }


}

