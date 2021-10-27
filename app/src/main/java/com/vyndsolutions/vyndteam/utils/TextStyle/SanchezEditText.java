package com.vyndsolutions.vyndteam.utils.TextStyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.utils.typefaces.SanchezLato;


@SuppressLint("AppCompatCustomView")
public class SanchezEditText extends EditText {
    public SanchezEditText(Context context) {
        super(context);
    }

    public SanchezEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SanchezTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.SanchezTextView_fonttypesanchez, 0);
            SanchezLato fontLato = new SanchezLato(context);
            switch (font) {
                case 0:
                    setTypeface(fontLato.getRegular());
                    break;
                case 1:
                    setTypeface(fontLato.getRegularita());
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SanchezEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
