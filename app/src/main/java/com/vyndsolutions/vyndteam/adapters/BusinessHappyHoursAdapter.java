package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.HappyHour;
import com.vyndsolutions.vyndteam.utils.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

public class BusinessHappyHoursAdapter extends RecyclerView.Adapter<BusinessHappyHoursAdapter.ViewHolder> {


    private final Context context;
    private final ArrayList<HappyHour> happyHours;

    int day = WidgetUtils.today;
    List<String> happyHoursDay = new ArrayList<>();
    List<HappyHour> happyHoursfinal = new ArrayList<>();
    String lundi = "Lun", mardi = "Mar", mercredi = "Mer", jeudi = "Jeu", vendredi = "Ven", samedi = "Sam", dimanche = "Dim";


    public BusinessHappyHoursAdapter(Context context, ArrayList<HappyHour> happyHours) {
        this.context = context;
        this.happyHours = happyHours;


        happyHoursDay.add(lundi);
        happyHoursDay.add(mardi);
        happyHoursDay.add(mercredi);
        happyHoursDay.add(jeudi);
        happyHoursDay.add(vendredi);
        happyHoursDay.add(samedi);
        happyHoursDay.add(dimanche);



    }

    @Override
    public BusinessHappyHoursAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.one_item_rv_happy_hours, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String dayofweek = happyHoursDay.get(position);
        happyHoursfinal.clear();
        holder.tvDayName.setText(dayofweek);
        for (int j = 0; j < happyHours.size(); j++) {
            if (dayofweek.equalsIgnoreCase(WidgetUtils.getDayName(happyHours.get(j).getDayOfTheWeek()).substring(0, 3))) {
                happyHoursfinal.add(happyHours.get(j));
            }
        }


        for (int i = 0; i < happyHoursfinal.size(); i++) {
            if (!happyHoursfinal.get(i).getHours2().equals("")) {
                holder.tvOpenTime.setVisibility(View.VISIBLE);
                holder.tvCloseTime.setVisibility(View.VISIBLE);
                holder.layoutNoHappyHour.setVisibility(View.INVISIBLE);
                if (happyHoursfinal.get(i).getDayOfTheWeek() == day) {
                    holder.tvDayName.setTextColor(context.getResources().getColor(R.color.blue_light_color));
                    holder.tvOpenTime.setTextColor(context.getResources().getColor(R.color.blue_light_color));
                    holder.tvCloseTime.setTextColor(context.getResources().getColor(R.color.blue_light_color));
//                holder.tvThisEvening.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDayName.setTextColor(context.getResources().getColor(R.color.black));
                    holder.tvOpenTime.setTextColor(context.getResources().getColor(R.color.gray_3));
                    holder.tvCloseTime.setTextColor(context.getResources().getColor(R.color.gray_3));
                }

                // holder.tvDayName.setText(WidgetUtils.getDayName(happyHours[j].getDayOfTheWeek()).substring(0, 3));
                holder.tvOpenTime.setText(happyHoursfinal.get(i).getStartingHour());
                holder.tvCloseTime.setText(happyHoursfinal.get(i).getCloseHour());
            } else {
                holder.tvDayName.setTextColor(context.getResources().getColor(R.color.gray_3));
                holder.tvOpenTime.setVisibility(View.INVISIBLE);
                holder.tvCloseTime.setVisibility(View.INVISIBLE);
                holder.layoutNoHappyHour.setVisibility(View.VISIBLE);
            }
        }
        if (holder.tvOpenTime.getText().toString().equalsIgnoreCase("00:00") && holder.tvCloseTime.getText().toString().equalsIgnoreCase("00:00")) {
            holder.layoutNoHappyHour.setVisibility(View.VISIBLE);
            holder.tvOpenTime.setVisibility(View.INVISIBLE);
            holder.tvCloseTime.setVisibility(View.INVISIBLE);

        }

/*

 */


    }

    @Override
    public int getItemCount() {
        return happyHoursDay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvOpenTime, tvCloseTime, tvDayName;

        View layoutNoHappyHour;


        public ViewHolder(View itemView) {
            super(itemView);

            tvDayName = itemView.findViewById(R.id.tv_day_name);


            tvOpenTime = itemView.findViewById(R.id.tv_open_time);
            tvCloseTime = itemView.findViewById(R.id.tv_close_time);

            layoutNoHappyHour = itemView.findViewById(R.id.layout_no_happy_hour);


        }
    }

}
