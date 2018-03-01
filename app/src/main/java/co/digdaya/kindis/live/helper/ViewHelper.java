package co.digdaya.kindis.live.helper;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ryandzhunter on 3/1/18.
 */

public class ViewHelper {


    public static View setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
        return view;
    }
}
