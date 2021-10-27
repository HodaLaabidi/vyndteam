package com.vyndsolutions.vyndteam.utils.typefaces;

import android.content.Context;
import android.graphics.Typeface;

public class DinLato {
    Context context;

    public DinLato(Context context) {
        this.context = context;
    }


    public Typeface getBold() {
        return TypeFacesUtils.obtainTypeface(context, "DINCondensedBold.ttf");
    }


}
