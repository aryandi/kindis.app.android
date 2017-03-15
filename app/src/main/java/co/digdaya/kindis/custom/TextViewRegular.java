package co.digdaya.kindis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewRegular extends TextView {
    public TextViewRegular(Context context) {
        super(context);
        setFont();
    }
    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
