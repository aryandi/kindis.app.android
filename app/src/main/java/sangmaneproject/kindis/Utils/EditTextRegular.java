package sangmaneproject.kindis.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by DELL on 1/25/2017.
 */

public class EditTextRegular extends EditText {
    public EditTextRegular(Context context) {
        super(context);
        setFont();
    }
    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public EditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
