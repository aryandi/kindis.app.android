package sangmaneproject.kindis.Custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 1/25/2017.
 */

public class TextViewSemiBold extends TextView {
    public TextViewSemiBold(Context context) {
        super(context);
        setFont();
    }
    public TextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public TextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
