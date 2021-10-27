package com.vyndsolutions.vyndteam.utils.typefaces;

import android.content.Context;
import android.graphics.Typeface;

public class SanchezLato {
    Context context;

    public SanchezLato(Context context) {
        this.context = context;
    }


    public Typeface getRegular() {
        return TypeFacesUtils.obtainTypeface(context, "Sanchezregular.otf");
    }


    public Typeface getRegularita() {
        return TypeFacesUtils.obtainTypeface(context, "Sanchezregular-ita.otf");
    }


}
