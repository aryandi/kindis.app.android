package co.digdaya.kindis.live.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by DELL on 1/25/2017.
 */

public class EditTextRegular extends AppCompatEditText {
    public EditTextRegular(Context context) {
        super(context);
        setFont(context);
    }
    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }
    public EditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
