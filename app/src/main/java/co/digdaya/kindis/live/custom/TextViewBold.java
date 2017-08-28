package co.digdaya.kindis.live.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewBold extends TextView {
    public TextViewBold(Context context) {
        super(context);
        setFont();
    }
    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
