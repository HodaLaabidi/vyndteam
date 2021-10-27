package com.vyndsolutions.vyndteam.utils.typefaces;

import android.content.Context;
import android.graphics.Typeface;

public class FontLato {
    Context context;

    public FontLato(Context context) {
        this.context = context;
    }

    public Typeface getLight() {
        return TypeFacesUtils.obtainTypeface(context, "Lato-Light.ttf");
    }

    public Typeface getMedium() {
        return TypeFacesUtils.obtainTypeface(context, "Lato-Medium.ttf");
    }

    public Typeface getRegular() {
        return TypeFacesUtils.obtainTypeface(context, "Lato-Regular.ttf");
    }


    public Typeface getBold() {
        return TypeFacesUtils.obtainTypeface(context, "Lato-Bold.ttf");
    }


}
