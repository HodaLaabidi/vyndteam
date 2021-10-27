package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.HappyHour;
import com.vyndsolutions.vyndteam.utils.WidgetUtils;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 13/02/2018.
 */

public class HappyHourArrayAdapter extends RecyclerView.Adapter<HappyHourArrayAdapter.ViewHolder> {
    List<HappyHour> happyHours = new ArrayList<>();
    Context context;
    public HappyHourArrayAdapter(Context context , List<HappyHour> happyHours) {
        this.context = context;
        this.happyHours = happyHours;

    }

    @Override
    public HappyHourArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_happy_hour, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HappyHourArrayAdapter.ViewHolder holder, int position) {
        final HappyHour happyHour = happyHours.get(position);
        System.out.println(happyHour.toString());
        try {
            if (!happyHour.getHours2().equals("")) {
                holder.layoutOneHappy.setVisibility(View.VISIBLE);

                holder.tvDay.setText(WidgetUtils.getDayName(happyHour.getDayOfTheWeek()));

                if (happyHour.getDescription() != null && !happyHour.getDescription().equals("")) {
                    holder.tvDescription.setText(happyHour.getDescription());
                    holder.tvDescription.setVisibility(View.VISIBLE);
                } else holder.tvDescription.setVisibility(View.GONE);
                holder.tvTime.setText(happyHour.getHours2());
            } else holder.layoutOneHappy.setVisibility(View.GONE);
        }catch (NullPointerException|IndexOutOfBoundsException e)
        {

        }



    }

    @Override
    public int getItemCount() {
        return happyHours.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay,tvDescription,tvTime;
        RelativeLayout layoutOneHappy;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day_name);
            tvDescription = itemView.findViewById(R.id.tv_day_description);
            tvTime = itemView.findViewById(R.id.tv_day_time);
            layoutOneHappy = itemView.findViewById(R.id.layout_one_happy);
        }
    }
}
