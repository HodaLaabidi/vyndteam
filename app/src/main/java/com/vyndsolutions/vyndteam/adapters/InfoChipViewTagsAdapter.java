package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.Classification;
import com.vyndsolutions.vyndteam.models.ValidInfoChip;


import java.util.ArrayList;
import java.util.List;

public class InfoChipViewTagsAdapter extends ChipViewAdapter {
    Classification[] classifications;
    Context context;
    Boolean isFirstTouch=false;
    List<Classification> validInfos = new ArrayList<>();
    List<Classification> validInfosToReturn = new ArrayList<>();
    private ArrayList<Classification> calssificationsToReturn=new ArrayList<>();

    public InfoChipViewTagsAdapter(Context context, Classification[] classifications, List<Classification> validInfos) {
        super(context);
        this.context = context;
        this.classifications = classifications;
        this.validInfos = validInfos;
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
        if(!isFirstTouch)
        {
        for (int i = 0; i < classifications.length; i++) {

            if (tag.getLabel().equalsIgnoreCase(classifications[i].getLabel())) {
                tag.setSelected(true);

            }
        }
        if (tag.isSelected()) {
            return R.drawable.button_primary_radius18dp;
        } else {
            return R.drawable.button_gray_transparent_radius18dp;
        }}
        else {
            if (tag.isSelected()) {
                return R.drawable.button_primary_radius18dp;
            } else {
                return R.drawable.button_gray_transparent_radius18dp;
            }
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

    public List<Classification> getSelectedValidInfoChips() {
        validInfosToReturn = new ArrayList<>();
        List<Chip> chipList = getChipList();
        List<ValidInfoChip> resultChipList = new ArrayList<>();
        for (Chip c : chipList) {
            if (((ValidInfoChip) c).isSelected()) {
                resultChipList.add((ValidInfoChip) c);
                for (int i = 0; i < validInfos.size(); i++)
                    if (((ValidInfoChip) c).getLabel().equalsIgnoreCase(validInfos.get(i).getLabel())) {
                        validInfosToReturn.add(validInfos.get(i));
                    }
            }
        }

        return validInfosToReturn;

    }
    public ArrayList<Classification> getTagsRet()
    {
        for(int i=0 ;i<getSelectedValidInfoChips().size();i++)
        {   Classification v = getSelectedValidInfoChips().get(i);
            for(int j=0;j<classifications.length;j++)
            {
                if(v.getLabel().equalsIgnoreCase(classifications[j].getLabel()))
                {
                    calssificationsToReturn.add(classifications[j]);
                }
            }
        }
        return calssificationsToReturn;
    }

    @Override
    public void onLayout(View view, int position) {

        TextView tvChip = view.findViewById(R.id.tv_chip);
        final ValidInfoChip tag = (ValidInfoChip) getChip(position);
        tvChip.setText(tag.getLabel());


        if (tag.isSelected()) {
            tvChip.setTextColor(getColor(R.color.white));

        } else tvChip.setTextColor(getColor(R.color.gray_3));
        tvChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTouch=true;
                tag.setSelected(!tag.isSelected());
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


}

