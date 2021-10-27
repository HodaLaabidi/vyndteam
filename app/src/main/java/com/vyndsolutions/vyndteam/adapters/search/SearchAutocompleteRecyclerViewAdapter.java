package com.vyndsolutions.vyndteam.adapters.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.activities.ItemBusinessActivity;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.search.SearchAutoComplete;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.List;


public class SearchAutocompleteRecyclerViewAdapter extends RecyclerView.Adapter<SearchAutocompleteRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<SearchAutoComplete> autocomplete;
    private OnTagItemClickListener onTagClickListener;

    public SearchAutocompleteRecyclerViewAdapter(Context context, List<SearchAutoComplete> autocomplete) {
        this.context = context;
        this.autocomplete = autocomplete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_search_autocomplete, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchAutoComplete auto = autocomplete.get(position);
        if (auto.getType() == 1) {
            holder.itemImage.setImageResource(R.drawable.icon_business_default);
            if (auto.getIcon() != null && !auto.getIcon().equals(""))
                holder.itemImage.setImageUrl(auto.getIcon());
            holder.itemDescription.setText(auto.getRegionLabel());
            holder.itemCategory.setText(auto.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Business business = new Business(auto.getLabel(), auto.getId());
                    business.setImage(auto.getIcon());
                    business.setName(auto.getLabel());

                    Intent activityChangeIntent = new Intent(context, ItemBusinessActivity.class);
                    activityChangeIntent.putExtra("business", business);
                    ((AppCompatActivity) context).startActivityForResult(activityChangeIntent, 0);


                }
            });
        } else if (auto.getType() == 2) {
            holder.itemDescription.setText(context.getString(R.string.categorie));
            holder.itemCategory.setVisibility(View.GONE);
            if (auto.getIcon() != null && !auto.getIcon().equals("")) {
                holder.itemImage.setImageUrlWithoutBorderWithoutResizing(auto.getIcon());
            } else holder.itemImage.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null)
                        onTagClickListener.onTagItemClickListener(auto);
                }
            });
        } else if (auto.getType() == 3) {
            holder.itemDescription.setVisibility(View.GONE);
            holder.itemCategory.setVisibility(View.GONE);
            holder.itemImage.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null)
                        onTagClickListener.onTagItemClickListener(auto);
                }
            });
        }

        holder.itemName.setText(auto.getLabel());


    }

    public void setOnTagClickListener(OnTagItemClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    public int getItemCount() {
        return autocomplete != null ? autocomplete.size() : 0;
    }

    public interface OnTagItemClickListener {
        void onTagItemClickListener(SearchAutoComplete searchAutoComplete);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView itemImage;
        TextView itemName;
        TextView itemCategory;
        TextView itemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (NetworkImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemCategory = (TextView) itemView.findViewById(R.id.item_category);
            itemDescription = (TextView) itemView.findViewById(R.id.item_description);
        }
    }
}
