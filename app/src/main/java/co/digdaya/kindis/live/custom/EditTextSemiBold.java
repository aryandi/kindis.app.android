package co.digdaya.kindis.live.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by vincenttp on 1/29/2017.
 */

public class EditTextSemiBold extends android.support.v7.widget.AppCompatEditText{
    public EditTextSemiBold(Context context) {
        super(context);
        setFont(context);
    }
    public EditTextSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }
    public EditTextSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "CitrixSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
