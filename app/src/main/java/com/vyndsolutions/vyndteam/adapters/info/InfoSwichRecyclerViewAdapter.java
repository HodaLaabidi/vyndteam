package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.models.ValidInfoParent;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class InfoSwichRecyclerViewAdapter extends RecyclerView.Adapter<InfoSwichRecyclerViewAdapter.ViewHolder> {


    private final Context context;
    private final ValidInfo[] infos;
    private final List<ValidInfoParent> principalInfos;
    private List<ValidInfoParent> principalInfosToReturn = new ArrayList<>();

    public InfoSwichRecyclerViewAdapter(Context context, ValidInfo[] Infos, List<ValidInfoParent> principalInfos) {
        this.context = context;
        this.infos = Infos;
        this.principalInfos = principalInfos;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.item_switch_info, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ValidInfo info = infos[position];

        for (int i = 0; i < principalInfos.size(); i++) {
            if (info.getId() == principalInfos.get(i).getValidInfo().getId()) {
                info.setSelected(true);
            }
        }
        if (info.isSelected()) {
            if (info.getIcon() != null && !info.getIcon().equals(""))
                holder.nivInfo.setImageUriWithoutProgress(info.getIcon(), context);
            else holder.nivInfo.setImageResource(R.drawable.default_info);
            holder.switchInfo.setChecked(true);
        } else {
            if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                holder.nivInfo.setImageUriWithoutProgress(info.getIconDisabled(), context);
            else holder.nivInfo.setImageResource(R.drawable.default_info_disabled);
            holder.switchInfo.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.switchInfo.isChecked()) {
                    holder.switchInfo.setChecked(false);
                } else {
                    holder.switchInfo.setChecked(true);
                }

            }
        });

        holder.switchInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (info.getIcon() != null && !info.getIcon().equals(""))
                        holder.nivInfo.setImageUriWithoutProgress(info.getIcon(), context);
                    else holder.nivInfo.setImageResource(R.drawable.default_info);
                    holder.switchInfo.setChecked(true);
                    info.setSelected(true);
                } else {
                    if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                        holder.nivInfo.setImageUriWithoutProgress(info.getIconDisabled(), context);
                    else holder.nivInfo.setImageResource(R.drawable.default_info_disabled);
                    holder.switchInfo.setChecked(false);
                    info.setSelected(false);
                }

            }
        });

        holder.tvInfo.setText(info.getLabel());
    }

    public List<ValidInfo> getSelectedInfos() {
        List<ValidInfo> validInfos = new ArrayList<>();
        for (ValidInfo validInfo : infos) {
            if (validInfo.isSelected())
                validInfos.add(validInfo);
        }
        return validInfos;
    }

    public List<ValidInfoParent> getPrincipalInfos() {
        for (int i = 0; i < getSelectedInfos().size(); i++) {
            ValidInfoParent p = new ValidInfoParent();
            p.setValidInfo(getSelectedInfos().get(i));
            principalInfosToReturn.add(p);
        }
        return principalInfosToReturn;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView nivInfo;
        TextView tvInfo;
        Switch switchInfo;
        View layoutInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            nivInfo = itemView.findViewById(R.id.niv_info);
            tvInfo = itemView.findViewById(R.id.tv_info);
            switchInfo = itemView.findViewById(R.id.switch_info);
            layoutInfo = itemView.findViewById(R.id.layout_info);


        }
    }

}
