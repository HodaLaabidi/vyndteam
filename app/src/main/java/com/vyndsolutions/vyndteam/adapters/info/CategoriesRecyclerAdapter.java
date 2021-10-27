package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.HashMap;
import java.util.List;


public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Classification> listSousCategorie;
    private final List<Classification> subCategorieDesfault;
    private HashMap<Integer, Classification> selectedSubCategoryMap;
    private int nbSelected = 0;


//    private static List<Classification> selectedSubCategories;

    public CategoriesRecyclerAdapter(Context context, List<Classification> listSousCategorie, HashMap<Integer, Classification> selectedSubCategoryMap, List<Classification> subCategorieDesfault) {
        this.context = context;
        this.listSousCategorie = listSousCategorie;
        this.selectedSubCategoryMap = selectedSubCategoryMap;
        this.subCategorieDesfault = subCategorieDesfault;

        this.nbSelected = 0;
    }

    @Override
    public CategoriesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_sous_categorie, parent, false);
        return new ViewHolder(rootView);
    }

    public HashMap<Integer, Classification> getSelectedSubCategoryMap() {
        return selectedSubCategoryMap;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Classification sousCategorie = listSousCategorie.get(position);

        if (selectedSubCategoryMap.containsKey(position)) {
            holder.cbCategory.setChecked(true);
            nbSelected++;


        } else holder.cbCategory.setChecked(false);
        for (int i = 0; i < subCategorieDesfault.size(); i++) {

            if (subCategorieDesfault.get(i).getId() == sousCategorie.getId()) {
                selectedSubCategoryMap.put(position,sousCategorie);
                holder.cbCategory.setChecked(true);
            }

        }
        //If the position -1 is present in the hash map check restaurant as selected
        if (sousCategorie.getId() == 807) {
            if (selectedSubCategoryMap.containsKey(-1)) {
                nbSelected = 1;
                selectedSubCategoryMap = new HashMap<>();
                selectedSubCategoryMap.put(position, sousCategorie);
                holder.cbCategory.setChecked(true);
            }
        }

        holder.tvCategoryName.setText(sousCategorie.getLabel());

        if (sousCategorie.getIcon() != null && !sousCategorie.getIcon().equals(""))
            holder.ivCategory.setImageUriWithoutProgress(sousCategorie.getIcon(), context);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbCategory.isChecked()) {
                    selectedSubCategoryMap.remove(position);
                    nbSelected--;
                    holder.cbCategory.setChecked(false);
                } else {
                    nbSelected++;
                    selectedSubCategoryMap.put(position, sousCategorie);
                    holder.cbCategory.setChecked(true);
                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return listSousCategorie != null ? listSousCategorie.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void resetSelectedSubCategories() {

        this.selectedSubCategoryMap = new HashMap<>();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        CheckBox cbCategory;
        NetworkImageView ivCategory;


        public ViewHolder(View itemView) {
            super(itemView);

            ivCategory = itemView.findViewById(R.id.iv_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            cbCategory = itemView.findViewById(R.id.cb_category);

        }
    }


}
