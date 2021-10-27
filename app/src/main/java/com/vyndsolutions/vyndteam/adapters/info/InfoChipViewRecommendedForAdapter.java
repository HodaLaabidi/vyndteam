package com.vyndsolutions.vyndteam.adapters.info;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.models.ValidInfo;
import com.vyndsolutions.vyndteam.models.ValidInfoChip;
import com.vyndsolutions.vyndteam.models.ValidInfoParent;

import java.util.ArrayList;
import java.util.List;

public class InfoChipViewRecommendedForAdapter extends ChipViewAdapter {
    List<ValidInfoParent> recommendedFors;
    List<ValidInfo> validInfos = new ArrayList<>();
    List<ValidInfo> validInfosToReturn = new ArrayList<>();
    List<ValidInfoParent> recommedInfosToReturn = new ArrayList<>();
    Context context;
    private boolean isFirstTouch = false;

    public InfoChipViewRecommendedForAdapter(Context context, List<ValidInfoParent> recommendedFors, List<ValidInfo> validInfos) {
        super(context);
        this.context = context;
        this.recommendedFors = recommendedFors;
        this.validInfos = validInfos;

    }


    @Override
    public int getLayoutRes(int position) {


        return R.layout.item_info_chip;
    }

    @Override
    public int getBackgroundColor(int position) {

        return 0;
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        ValidInfoChip tag = (ValidInfoChip) getChip(position);
        if (!isFirstTouch) {
            for (int i = 0; i < recommendedFors.size(); i++) {
                System.out.println(tag.getLabel());
                System.out.println(recommendedFors.get(i).toString());
                if (tag.getLabel().equalsIgnoreCase(recommendedFors.get(i).getValidInfo().getLabel())) {

                    tag.setSelected(true);


                }
            }
            if (tag.isSelected()) {
                return R.drawable.button_primary_radius18dp;
            } else {
                return R.drawable.button_gray_transparent_radius18dp;
            }
        } else {
            if (tag.isSelected()) {
                return R.drawable.button_primary_radius18dp;
            } else {
                return R.drawable.button_gray_transparent_radius18dp;
            }
        }
    }


    @Override
    public void onLayout(View view, final int position) {

        TextView tvChip = view.findViewById(R.id.tv_chip);
        final ValidInfoChip tag = (ValidInfoChip) getChip(position);
        tvChip.setText(tag.getLabel());


        if (tag.isSelected()) {
            tvChip.setTextColor(getColor(R.color.white));

        } else tvChip.setTextColor(getColor(R.color.gray_3));
        tvChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTouch = true;
                tag.setSelected(!tag.isSelected());
                setChanged();
                notifyObservers();
            }
        });
    }

    public List<ValidInfo> getSelectedValidInfoChips() {
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

    public List<ValidInfoParent> getRecommendedFors() {
        for (int i = 0; i < getSelectedValidInfoChips().size(); i++) {
            ValidInfoParent p = new ValidInfoParent();
            p.setValidInfo(getSelectedValidInfoChips().get(i));
            recommedInfosToReturn.add(p);
        }
        return recommedInfosToReturn;
    }


}

