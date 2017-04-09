package co.digdaya.kindis.util.SpacingItem;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DELL on 4/3/2017.
 */

public class SpacingItemMore extends RecyclerView.ItemDecoration {
    Context context;

    public SpacingItemMore(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition%2 == 0){
            outRect.left = getDP(16);
        }else {
            outRect.right = getDP(16);
        }
        outRect.top = getDP(16);
    }

    private int getDP(int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
}
