package sangmaneproject.kindis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

/**
 * Created by DELL on 1/25/2017.
 */

public class ShowHidePasswordEditTextRegular extends ShowHidePasswordEditText {
    public ShowHidePasswordEditTextRegular(Context context) {
        super(context);
        setFont();
    }
    public ShowHidePasswordEditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public ShowHidePasswordEditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
