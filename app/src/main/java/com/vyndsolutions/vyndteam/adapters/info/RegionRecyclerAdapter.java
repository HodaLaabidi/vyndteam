package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Region;

import java.util.List;


public class RegionRecyclerAdapter extends RecyclerView.Adapter<RegionRecyclerAdapter.ViewHolder> {

    private static OnRegionSelected onRegionSelected;
    static OnTagItemClickListener onTagClickListener;
    private final Context context;
    private List<Region> regions;
    private TextView tvRegion;


    public RegionRecyclerAdapter(Context context, List<Region> regions) {
        this.context = context;
        try {
            this.regions = regions.subList(0, 3);
        } catch (IndexOutOfBoundsException e) {
            this.regions = regions;
        }

        this.tvRegion = tvRegion;

    }

    public static void setRegionSelected(OnRegionSelected onRegionSelectedl) {
        onRegionSelected = onRegionSelectedl;
    }

    @Override
    public RegionRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.one_item_region, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Region currentRegion = regions.get(position);
        holder.tvRegion.setText(currentRegion.getLabel());
        holder.tvRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("1");
                if (onTagClickListener != null) {
                    System.out.println("2");
                    onTagClickListener.onTagItemClickListener(currentRegion);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("3");
                if (onTagClickListener != null) {
                    System.out.println("4");
                    onTagClickListener.onTagItemClickListener(currentRegion);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return regions != null ? regions.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnRegionSelected {
        void OnRegionSelected(Region region);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegion;


        public ViewHolder(View itemView) {
            super(itemView);

            tvRegion = itemView.findViewById(R.id.tv_region);


        }
    }

    public static void setOnTagClickListener(OnTagItemClickListener onTagClickListener1) {
        onTagClickListener = onTagClickListener1;
    }


    public interface OnTagItemClickListener {
        void onTagItemClickListener(Region region);
    }
}
