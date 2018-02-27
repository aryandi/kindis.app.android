package co.digdaya.kindis.live.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewSemiBold extends AppCompatTextView {
    public TextViewSemiBold(Context context) {
        super(context);
        setFont(context);
    }
    public TextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }
    public TextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "CitrixSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
