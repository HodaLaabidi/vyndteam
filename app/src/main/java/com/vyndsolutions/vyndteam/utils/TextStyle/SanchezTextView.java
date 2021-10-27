package com.vyndsolutions.vyndteam.utils.TextStyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.utils.typefaces.SanchezLato;


@SuppressLint("AppCompatCustomView")
public class SanchezTextView extends TextView {
    public SanchezTextView(Context context) {
        super(context);
    }

    public SanchezTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SanchezTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.SanchezTextView_fonttypesanchez, 0);
            SanchezLato sanchezLato = new SanchezLato(context);
            switch (font) {
                case 0:
                    setTypeface(sanchezLato.getRegular());
                    break;
                case 2:
                    setTypeface(sanchezLato.getRegularita());
                    break;

            }
        } finally {
            a.recycle();
        }
    }

    public SanchezTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SanchezTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
