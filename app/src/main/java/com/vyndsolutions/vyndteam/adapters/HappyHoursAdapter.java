package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Day;
import com.vyndsolutions.vyndteam.models.Hours;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Hoda on 06/05/2018.
 */
public class HappyHoursAdapter extends RecyclerView.Adapter<HappyHoursAdapter.MyViewHolder> {

    Context context ;
    List<Day> days = new ArrayList<Day>();
    ArrayList<Hours> hours;


    public HappyHoursAdapter(Context context ,ArrayList<Hours> hours) {
        this.context = context;
        this.hours = hours;
        days.add(new Day("Lun",0));
        days.add(new Day("Mar",1));
        days.add(new Day("Mer",2));
        days.add(new Day("Jeu",3));
        days.add(new Day("Ven",4));
        days.add(new Day("Sam",5));
        days.add(new Day("Dim",6));
    }

    public HappyHoursAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_happy_hours,null);
        return new HappyHoursAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Day dayOfHappyHours = days.get(position);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.e("day of the week ", day+" !");

        if(hours.size() != 0) {
            Log.e("size happy hours", hours.size()+" !");
            final Hours hoursitem = hours.get(position);


            holder.dayHappyHours.setText(dayOfHappyHours.getDayName());

            if (hoursitem.isAvailable()) {
                holder.dayHappyHours.setTextColor(context.getResources().getColor(R.color.blackTextColor));
                holder.llValidTime.setVisibility(View.VISIBLE);
                holder.invalidTime.setVisibility(View.GONE);
                holder.openTime.setText(hoursitem.getTime().getOpenTime());
                holder.closeTime.setText(hoursitem.getTime().getCloseTime());
                if (day == position+1){
                    holder.dayHappyHours.setTextColor(context.getResources().getColor(R.color.teal_color));
                    holder.openTime.setTextColor(context.getResources().getColor(R.color.teal_color));
                    holder.closeTime.setTextColor(context.getResources().getColor(R.color.teal_color));
                }

            } else {

                holder.llValidTime.setVisibility(View.GONE);
                holder.invalidTime.setVisibility(View.VISIBLE);
                if (day == position+1){
                    holder.dayHappyHours.setTextColor(context.getResources().getColor(R.color.teal_color));
                    holder.invalidTime.setTextColor(context.getResources().getColor(R.color.teal_color));
                }
            }
        } else {


                holder.dayHappyHours.setText(dayOfHappyHours.getDayName());
                holder.llValidTime.setVisibility(View.GONE);
                holder.invalidTime.setVisibility(View.VISIBLE);
            if (day == position+1){
                holder.dayHappyHours.setTextColor(context.getResources().getColor(R.color.teal_color));
                holder.invalidTime.setTextColor(context.getResources().getColor(R.color.teal_color));

            }

        }


    }

    @Override
    public int getItemCount() {
        return days.size();}

    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView dayHappyHours, invalidTime, openTime, closeTime;
         LinearLayout llValidTime;
        public MyViewHolder(View itemView) {
            super(itemView);

        dayHappyHours = itemView.findViewById(R.id.day_Happy_hours);
        invalidTime = itemView.findViewById(R.id.invalid_time);
        openTime = itemView.findViewById(R.id.open_time);
        closeTime = itemView.findViewById(R.id.close_time);
        llValidTime = itemView.findViewById(R.id.ll_valid_time);

        }
    }

}
