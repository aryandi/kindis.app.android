package co.digdaya.kindis.live.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewRegular extends AppCompatTextView {
    public TextViewRegular(Context context) {
        super(context);
        setFont(context);
    }
    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }
    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
