package co.digdaya.kindis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by vincenttp on 1/29/2017.
 */

public class EditTextSemiBold extends EditText{
    public EditTextSemiBold(Context context) {
        super(context);
        setFont();
    }
    public EditTextSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public EditTextSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
