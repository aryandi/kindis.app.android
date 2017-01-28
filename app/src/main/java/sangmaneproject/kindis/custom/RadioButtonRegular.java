package sangmaneproject.kindis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by DELL on 1/25/2017.
 */

public class RadioButtonRegular extends RadioButton {
    public RadioButtonRegular(Context context) {
        super(context);
        setFont();
    }
    public RadioButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RadioButtonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
