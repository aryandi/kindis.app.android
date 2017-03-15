package co.digdaya.kindis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by DELL on 1/25/2017.
 */

public class ButtonSemiBold extends Button {
    public ButtonSemiBold(Context context) {
        super(context);
        setFont();
    }
    public ButtonSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public ButtonSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
