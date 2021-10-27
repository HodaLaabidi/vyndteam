package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.models.ValidInfoChip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 15/03/2018.
 */


public class InfoChipViewAdapter extends ChipViewAdapter {
    ValidInfo[] validInfos;
    private OnCheckedStateChanged onCheckedStateChanged;


    public InfoChipViewAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutRes(int position) {


        /*ValidInfoChip tag = (ValidInfoChip) getChip(position);

        if (tag.isSelected()) {

                return R.layout.chip_double_close;


        }else {
                return R.layout.chip_close;
        }*/
        return R.layout.item_info_chip;
    }

    @Override

    public int getBackgroundColor(int position) {
        /*Tag tag = (Tag) getChip(position);

        switch (tag.getType()) {
            default:
                return 0;

            case 1:
            case 4:
                return getColor(R.color.blue);

            case 2:
            case 5:
                return getColor(R.color.purple);

            case 3:
                return getColor(R.color.teal);
        }*/
        return 0;
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        ValidInfoChip tag = (ValidInfoChip) getChip(position);

        if (tag.isSelected()) {
            return R.drawable.button_primary_radius18dp;
        } else {
            return R.drawable.button_gray_transparent_radius18dp;
        }
    }

    public void updateListSelection(List<ValidInfoChip> selectedInfos) {
        List<Chip> chipList = getChipList();
        for (Chip c : chipList) {
            ((ValidInfoChip) c).setSelected(false);
            if (selectedInfos != null && selectedInfos.size() != 0)
                for (ValidInfoChip selectedValidInfo : selectedInfos) {
                    if (selectedValidInfo.getLabel().equals(((ValidInfoChip) c).getLabel()))
                        ((ValidInfoChip) c).setSelected(true);
                }
        }
        setChipList(chipList);
        setChanged();
        notifyObservers();
    }

    public List<ValidInfoChip> getSelectedValidInfoChips() {

        List<Chip> chipList = getChipList();
        List<ValidInfoChip> resultChipList = new ArrayList<>();
        for (Chip c : chipList) {
            if (((ValidInfoChip) c).isSelected())
                resultChipList.add((ValidInfoChip) c);
        }
        return resultChipList;

    }

    @Override
    public void onLayout(View view, int position) {

        TextView tvChip = view.findViewById(R.id.tv_chip);
        final ValidInfoChip tag = (ValidInfoChip) getChip(position);
        tvChip.setText(tag.getLabel());
        if (tag.isSelected())
            tvChip.setTextColor(getColor(R.color.white));
        else tvChip.setTextColor(getColor(R.color.gray_3));
        tvChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag.setSelected(!tag.isSelected());
                if (onCheckedStateChanged != null)
                    onCheckedStateChanged.onChecked(tag.isSelected());
                setChanged();
                notifyObservers();
            }
        });
    }

    public void reset() {

        List<Chip> chipList = getChipList();
        for (Chip chip : chipList) {
            ((ValidInfoChip) chip).setSelected(false);
        }
        setChanged();
        notifyObservers();
    }

    public void setOnCheckedStateChanged(OnCheckedStateChanged onCheckedStateChanged) {
        this.onCheckedStateChanged = onCheckedStateChanged;
    }

    public interface OnCheckedStateChanged {
        void onChecked(boolean state);
    }
}

