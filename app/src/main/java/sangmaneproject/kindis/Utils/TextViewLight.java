package sangmaneproject.kindis.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewLight extends TextView {
    public TextViewLight(Context context) {
        super(context);
        setFont();
    }
    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public TextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
