package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 15/03/2018.
 */

public class InfoSwitchRecyclerViewAdapter extends RecyclerView.Adapter<InfoSwitchRecyclerViewAdapter.ViewHolder> {


    private final Context context;
    private final ValidInfo[] infos;
    private final List<ValidInfo> selectedPrincipalInfos;
    private OnCheckedStateChanged onCheckedStateChanged;

    public InfoSwitchRecyclerViewAdapter(Context context, ValidInfo[] Infos, List<ValidInfo> selectedPrincipalInfos) {
        this.context = context;
        this.infos = Infos;
        this.selectedPrincipalInfos = selectedPrincipalInfos;
        updateListSelection();

    }

    private void updateListSelection() {

        for (ValidInfo v : infos) {
            v.setSelected(false);
            if (selectedPrincipalInfos != null && selectedPrincipalInfos.size() != 0) {
                for (ValidInfo selectedValidInfo : selectedPrincipalInfos) {
                    if (selectedValidInfo.getId() == v.getId())
                        v.setSelected(true);
                }
            }
        }
    }

    @Override
    public InfoSwitchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.item_informations_switcher, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ValidInfo info = infos[position];


        if(selectedPrincipalInfos.contains(info)){
            holder.imgInfosSwitcher.setImageUrlWithoutBorder(info.getIcon());
            holder.switcher.setChecked(true);
            Log.e("zzz", selectedPrincipalInfos.size()+"");
        }


        if (info.isSelected()) {
            if (info.getIcon() != null && !info.getIcon().equals(""))
                holder.imgInfosSwitcher.setImageUrlWithoutBorder(info.getIcon());
           // else holder.imgInfosSwitcher.setImageResource(R.drawable.default_info);
            holder.switcher.setChecked(true);
        } else {
            if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                holder.imgInfosSwitcher.setImageUrlWithoutBorder(info.getIconDisabled());
            //else holder.imgInfosSwitcher.setImageResource(R.drawable.default_info_disabled);
            holder.switcher.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.switcher.isChecked()) {
                    holder.switcher.setChecked(false);
                } else {
                    holder.switcher.setChecked(true);
                }

            }
        });
        

        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (info.getIcon() != null && !info.getIcon().equals(""))
                        holder.imgInfosSwitcher.setImageUrlWithoutBorder(info.getIcon());
                    //else holder.imgInfosSwitcher.setImageResource(R.drawable.default_info);
                    holder.switcher.setChecked(true);
                    info.setSelected(true);


                } else {
                    if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                        holder.imgInfosSwitcher.setImageUrlWithoutBorder(info.getIconDisabled());
                    //else holder.imgInfosSwitcher.setImageResource(R.drawable.default_info_disabled);
                    holder.switcher.setChecked(false);
                    info.setSelected(false);
                }
                if (onCheckedStateChanged != null) {
                    onCheckedStateChanged.onChecked(b);
                }
            }
        });


        holder.principalInfo.setText(info.getLabel());
    }



    public List<ValidInfo> getSelectedInfos() {
        List<ValidInfo> validInfos = new ArrayList<>();
        for (ValidInfo validInfo : infos) {
            if (validInfo.isSelected())
                validInfos.add(validInfo);
        }
        return validInfos;
    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.length : 0;
    }

    public void reset() {
        for (ValidInfo validInfo : infos) {
            validInfo.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void setOnCheckedStateChanged(OnCheckedStateChanged onCheckedStateChanged) {
        this.onCheckedStateChanged = onCheckedStateChanged;
    }

    public interface OnCheckedStateChanged {
        void onChecked(boolean state);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView imgInfosSwitcher;
        TextView principalInfo;
        Switch switcher;
        LinearLayout layoutSwitcher;

        public ViewHolder(View itemView) {
            super(itemView);

            imgInfosSwitcher = itemView.findViewById(R.id.img_infos_switcher);
            principalInfo = itemView.findViewById(R.id.text_info_switcher);
            switcher = itemView.findViewById(R.id.switcher);
            layoutSwitcher = itemView.findViewById(R.id.layout_switcher);


        }
    }

}
