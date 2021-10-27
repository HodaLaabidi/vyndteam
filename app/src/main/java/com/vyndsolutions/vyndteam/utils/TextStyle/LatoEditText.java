package com.vyndsolutions.vyndteam.utils.TextStyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.vyndsolutions.vyndteam.R;
import com.vyndsolutions.vyndteam.utils.typefaces.FontLato;


@SuppressLint("AppCompatCustomView")
public class LatoEditText extends EditText {
    public LatoEditText(Context context) {
        super(context);
    }

    public LatoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LatoTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.LatoTextView_fonttype, 0);
            FontLato fontLato = new FontLato(context);
            switch (font) {
                case 0:
                    setTypeface(fontLato.getRegular());
                    break;
                case 2:
                    setTypeface(fontLato.getLight());
                    break;
                case 3:
                    setTypeface(fontLato.getRegular());
                    break;
                case 4:
                    setTypeface(fontLato.getBold());
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LatoEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
