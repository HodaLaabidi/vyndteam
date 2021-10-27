package com.vyndsolutions.vyndteam.utils.TextStyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.utils.typefaces.DinLato;


@SuppressLint("AppCompatCustomView")
public class DinTextView extends TextView {
    public DinTextView(Context context) {
        super(context);
    }

    public DinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DinTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.DinTextView_fonttypedin, 0);
            DinLato dinLato = new DinLato(context);
            switch (font) {
                case 0:
                    setTypeface(dinLato.getBold());
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    public DinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DinTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
