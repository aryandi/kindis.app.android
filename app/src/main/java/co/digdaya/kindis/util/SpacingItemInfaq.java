package co.digdaya.kindis.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DELL on 3/14/2017.
 */

public class SpacingItemInfaq extends RecyclerView.ItemDecoration {
    Context context;

    public SpacingItemInfaq(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (24 * scale + 0.5f);

        outRect.bottom = pixels;
        // Add top margin only for the first item to avoid double space between items
        if ((parent.getChildLayoutPosition(view)%2) == 0) {
            outRect.right = pixels;
        } else {
            outRect.right = 0;
        }
    }
}
