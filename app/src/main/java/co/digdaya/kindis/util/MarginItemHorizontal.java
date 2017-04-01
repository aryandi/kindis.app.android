package co.digdaya.kindis.util;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DELL on 4/1/2017.
 */

public class MarginItemHorizontal extends RecyclerView.ItemDecoration {
    Activity context;

    public MarginItemHorizontal(Activity context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == 0){
            outRect.left = getDP(16);
        }else if (itemPosition == state.getItemCount()-1){
            outRect.left = getDP(8);
            outRect.right = getDP(16);
        }else {
            outRect.left = getDP(8);
        }
    }

    private int getDP(int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
}
