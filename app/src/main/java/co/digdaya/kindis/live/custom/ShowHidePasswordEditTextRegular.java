package co.digdaya.kindis.live.custom;

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
        setFont(context);
    }
    public ShowHidePasswordEditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }
    public ShowHidePasswordEditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
    private void setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "CitrixSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
