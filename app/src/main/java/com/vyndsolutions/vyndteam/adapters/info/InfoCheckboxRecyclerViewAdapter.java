package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 15/03/2018.
 */



    public class InfoCheckboxRecyclerViewAdapter extends RecyclerView.Adapter<InfoCheckboxRecyclerViewAdapter.ViewHolder> {


        private final Context context;
        private final ValidInfo[] infos;
        private List<ValidInfo> selectedImportantInfos;
        private OnCheckedStateChanged onCheckedStateChanged;

        public InfoCheckboxRecyclerViewAdapter(Context context, ValidInfo[] infos, List<ValidInfo> selectedImportantInfos) {
            this.context = context;
            this.infos = infos;
            this.selectedImportantInfos = selectedImportantInfos;
            updateListSelection();

        }

        private void updateListSelection() {

            for (ValidInfo v : infos) {
                v.setSelected(false);
                if (selectedImportantInfos != null && selectedImportantInfos.size() != 0)
                    for (ValidInfo selectedValidInfo : selectedImportantInfos) {
                        if (selectedValidInfo.getId() == v.getId())
                            v.setSelected(true);
                    }
            }
        }

        @Override
        public InfoCheckboxRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(this.context).inflate(R.layout.item_informations_checkbox, parent, false);
            return new ViewHolder(rootView);
        }

        public List<ValidInfo> getSelectedInfos() {
            List<ValidInfo> validInfos = new ArrayList<>();
            for (ValidInfo validInfo : infos) {
                if (validInfo.isSelected())
                    validInfos.add(validInfo);
            }
            return validInfos;
        }

        public void reset() {
            for (ValidInfo validInfo : infos) {
                validInfo.setSelected(false);
            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final ValidInfo info = infos[position];


            if (info.isSelected()) {
                if (info.getIcon() != null && !info.getIcon().equals(""))
                    holder.imageImportantInfos.setImageUrlWithoutBorder(info.getIcon());
                //else holder.nivInfo.setImageResource(R.drawable.default_info);
                holder.checkbox.setChecked(true);
            } else {
                if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                    holder.imageImportantInfos.setImageUrlWithoutBorder(info.getIconDisabled());
                //else holder.nivInfo.setImageResource(R.drawable.default_info_disabled);
                holder.checkbox.setChecked(false);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkbox.isChecked()) {
                        holder.checkbox.setChecked(false);
                    } else {
                        holder.checkbox.setChecked(true);
                    }

                }
            });
            changeCheckboxColor(holder);


            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (info.getIcon() != null && !info.getIcon().equals(""))
                            holder.imageImportantInfos.setImageUrlWithoutBorder(info.getIcon());
                        //else holder.imageImportantInfos.setImageResource(R.drawable.default_info);
                        holder.checkbox.setChecked(true);
                        info.setSelected(true);
                    } else {
                        if (info.getIconDisabled() != null && !info.getIconDisabled().equals(""))
                            holder.imageImportantInfos.setImageUrlWithoutBorder(info.getIconDisabled());
                       // else holder.imageImportantInfos.setImageResource(R.drawable.default_info_disabled);
                        holder.checkbox.setChecked(false);
                        info.setSelected(false);
                    }
                    if (onCheckedStateChanged != null)
                        onCheckedStateChanged.onChecked(b);
                }
            });


            holder.textImportantInfos.setText(info.getLabel());
            Log.e(" test adapter", info.getLabel());

        }

    private void changeCheckboxColor(ViewHolder holder) {
              ColorStateList colorStateList =new ColorStateList(
               new int[][]{
                       new int[]{-android.R.attr.state_checked},
                       new int[]{android.R.attr.state_checked}
               },
               new int[] {
                       ContextCompat.getColor(holder.checkbox.getContext(), R.color.uncheckedCheckBox),
                       ContextCompat.getColor(holder.checkbox.getContext(),R.color.colorPrimary)
               });
               holder.checkbox.setButtonTintList(colorStateList);
    }

    @Override
        public int getItemCount() {
            return infos != null ? infos.length : 0;
        }

        public void setOnCheckedStateChanged(OnCheckedStateChanged onCheckedStateChanged) {
            this.onCheckedStateChanged = onCheckedStateChanged;
        }

        public interface OnCheckedStateChanged {
            void onChecked(boolean state);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            NetworkImageView imageImportantInfos;
            TextView textImportantInfos;
            CheckBox checkbox;
            View layoutCheckbox;

            public ViewHolder(View itemView) {
                super(itemView);

                imageImportantInfos = itemView.findViewById(R.id.image_important_infos);
                textImportantInfos = itemView.findViewById(R.id.text_important_infos);
                checkbox = itemView.findViewById(R.id.checkbox);
                layoutCheckbox = itemView.findViewById(R.id.layout_checkbox);


            }
        }


    }


