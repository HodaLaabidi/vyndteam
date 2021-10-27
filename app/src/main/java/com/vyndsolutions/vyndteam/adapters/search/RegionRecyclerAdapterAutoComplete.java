package com.vyndsolutions.vyndteam.adapters.search;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Region;

import java.util.ArrayList;
import java.util.List;


public class RegionRecyclerAdapterAutoComplete extends RecyclerView.Adapter<RegionRecyclerAdapterAutoComplete.ViewHolder> {

    private final Context context;
    OnTagItemClickListener onTagClickListener;
    private List<Region> regions;
    private boolean isForSearch = true;
    //private Region proximite;

    public RegionRecyclerAdapterAutoComplete(Context context, List<Region> regions) {
        this.context = context;
        this.regions = new ArrayList<>(regions);
       /* proximite = new Region(-2, context.getString(R.string.aproximite), 0, 0);
        this.regions.add(proximite);*/
    }

    public RegionRecyclerAdapterAutoComplete(Context context, List<Region> regions, boolean isForSearch) {
        this.context = context;
        this.regions = new ArrayList<>(regions);
        this.isForSearch = isForSearch;
    }


    @Override
    public RegionRecyclerAdapterAutoComplete.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.string_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RegionRecyclerAdapterAutoComplete.ViewHolder holder, int position) {
        final Region region = regions.get(position);


        if (position == 0 && isForSearch)
            holder.tvItemTitle.setTextColor(Color.parseColor(context.getString(R.color.colorPrimary)));
        else holder.tvItemTitle.setTextColor(Color.parseColor(context.getString(R.color.black)));
        holder.tvItemTitle.setText(region.getLabel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null)
                    onTagClickListener.onTagItemClickListener(region);
            }
        });

    }

    @Override
    public int getItemCount() {
        return regions != null ? regions.size() : 0;
    }

    public void setOnTagClickListener(OnTagItemClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }


    public interface OnTagItemClickListener {
        void onTagItemClickListener(Region region);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);


        }
    }
}
