package com.vyndsolutions.vyndteam.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.AvailableTime;
import com.vyndsolutions.vyndteam.models.Day;
import com.vyndsolutions.vyndteam.models.HoursHoraires;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Hoda on 02/05/2018.
 */

public class HoraireAdapter extends RecyclerView.Adapter<HoraireAdapter.MyViewHolder> {

    Context context ;
    List<Day> days ;
    ArrayList<AvailableTime> savedArray;


    AvailableTime hoursHoraires1 = new AvailableTime("","") , hoursHoraires2 = new AvailableTime("","");

    public HoraireAdapter(Context context, List<Day> days , ArrayList<AvailableTime> savedArray) {
        this.context = context;
        this.days = days;
        this.savedArray = savedArray;
    }

    public HoraireAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_horaire,null);
        return new HoraireAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Day dayFromArray = days.get(position);
        Log.i("position ", position + " ");
        holder.day.setText(dayFromArray.getDayName());
        Log.i("position " + position + " ", "day" + dayFromArray.getDayName());
        holder.switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.switchCompat.isChecked()) {
                    holder.timeDeb.setClickable(true);
                    holder.timeFin.setClickable(true);
                    holder.timeDeb2.setClickable(true);
                    holder.timeFin2.setClickable(true);


                    setTimePickerDialog(holder.timeDeb, holder, position);
                    setTimePickerDialog(holder.timeDeb2, holder, position);
                    setTimePickerDialog(holder.timeFin, holder, position);
                    setTimePickerDialog(holder.timeFin2, holder, position);
                    Log.i("day " + dayFromArray.getDayName(), "position " + position);
                } else {
                    resetHoraireView(holder);
                    //delete elements
                    if(savedArray != null){
                        if (savedArray.size() != 0){
                            if (savedArray.size() == 1){
                                savedArray.clear();
                                savedArray = new ArrayList<AvailableTime>();
                            }else {
                                for(int i = 0 ; i < savedArray.size() ; i++){
                                    if(savedArray.get(i).getDayOfTheWeek() == position){
                                        savedArray.remove(i);
                                    }
                                }

                            }
                        }else{
                            savedArray.clear();
                            savedArray = new ArrayList<AvailableTime>();
                        }
                    }



                    }

                }

        });

        // retieve views and values from sharedPreferenced
        if( savedArray != null){
            if(savedArray.size() != 0) {
                for (int i = 0; i < savedArray.size(); i++) {
                    Log.e("contenue de savedArray", savedArray.get(i).getOpenTime() + " " + savedArray.get(i).getCloseTime() + " " + savedArray.get(i).getDayOfTheWeek());
                }
            }
        }

        if (savedArray != null) {
            if (savedArray.size() != 0) {
                for (int i = 0; i < savedArray.size(); i++) {

                    if (savedArray.get(i).getDayOfTheWeek() == position) {

                        holder.switchCompat.setChecked(true);
                        if ((holder.timeDeb.getText()+"").equalsIgnoreCase("--:--") && (holder.timeFin.getText()+"").equalsIgnoreCase("--:--")) {
                            holder.timeDeb.setText(savedArray.get(i).getOpenTime());
                            holder.timeFin.setText(savedArray.get(i).getCloseTime());

                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if ( !( holder.timeDeb.getText()+"").equalsIgnoreCase("--:--") && ! (holder.timeFin.getText()+"").equalsIgnoreCase("--:--"))
                        {
                            holder.timeDeb2.setText(savedArray.get(i).getOpenTime());
                            holder.timeFin2.setText(savedArray.get(i).getCloseTime());

                        }


                    }
                }


            }


        }
    }
    private void resetHoraireView(MyViewHolder holder) {


            holder.timeDeb.setText("--:--");
            holder.timeFin.setText("--:--");
            holder.timeDeb2.setText("--:--");
            holder.timeFin2.setText("--:--");

           holder.timeDeb.setClickable(false);
           holder.timeFin.setClickable(false);
           holder.timeDeb2.setClickable(false);
           holder.timeFin2.setClickable(false);
           holder.switchCompat.setSelected(false);
           notifyDataSetChanged();


        }

    private void setTimePickerDialog(final TextView time, final MyViewHolder holder, final int position) {

        Calendar currentTime = Calendar.getInstance();
        final int  hour = currentTime.get(Calendar.HOUR_OF_DAY);
        final  int minute = currentTime.get(Calendar.MINUTE);
        if(time.isClickable()){
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("test timepicker", "  ok shown");

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            time.setText(hourOfDay + " : " + minute );

                            if(!((holder.timeFin.getText()+"").equals("--:--")) &&!((holder.timeDeb.getText()+"").equals("--:--")) && (holder.timeFin2.getText()+"").equals("--:--") && (holder.timeDeb2.getText()+"").equals("--:--")) {
                                hoursHoraires1 = new AvailableTime(holder.timeDeb.getText()+"", holder.timeFin.getText()+"",position);
                                Log.e("hoursHoraires1", hoursHoraires1.getOpenTime() + " "+ hoursHoraires1.getCloseTime() );
                                savedArray.add(hoursHoraires1);
                            }

                            if(!((holder.timeFin2.getText()+"").equals("--:--")) && !((holder.timeDeb.getText()+"").equals("--:--")) && !((holder.timeFin.getText()+"").equals("--:--")) && !((holder.timeDeb2.getText()+"").equals("--:--"))) {
                                       hoursHoraires2 = new AvailableTime(holder.timeDeb2.getText()+"", holder.timeFin2.getText()+"", position);
                                Log.e("hoursHoraires2", hoursHoraires2.getOpenTime()+ "  "+ hoursHoraires2.getCloseTime());
                                savedArray.add(hoursHoraires2);
                                }



                            }



                    }, hour , minute , true);
                    timePickerDialog.show();
                      }
                  });

        }

    }

    @Override
    public int getItemCount() {
        return days.size();}

    class MyViewHolder extends RecyclerView.ViewHolder {

        Switch switchCompat ;
        TextView timeDeb , timeFin , timeDeb2 , timeFin2 , day ;
        public MyViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tv_day);
            timeDeb = itemView.findViewById(R.id.time_deb);
            timeDeb2 = itemView.findViewById(R.id.time_deb2);
            timeFin = itemView.findViewById(R.id.time_fin);
            timeFin2 = itemView.findViewById(R.id.time_fin2);
            switchCompat = itemView.findViewById(R.id.switch_horaire);


        }
    }

}
