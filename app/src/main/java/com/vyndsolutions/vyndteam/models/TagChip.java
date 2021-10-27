package com.vyndsolutions.vyndteam.models;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by Hoda on 28/02/2018.
 */

public class TagChip implements Chip {

    private String label ;

    public TagChip(String label) {
        this.label = label;
    }

    public String getName() {

        return label;
    }

    public void setName(String label) {
        this.label = label;
    }


    @Override
    public String getText() {
        return label;
    }
}
