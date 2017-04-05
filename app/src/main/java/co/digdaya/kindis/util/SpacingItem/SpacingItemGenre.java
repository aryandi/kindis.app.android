package co.digdaya.kindis.util.SpacingItem;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vincenttp on 4/2/2017.
 */

public class SpacingItemGenre extends RecyclerView.ItemDecoration {
    Context context;

    public SpacingItemGenre(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (((itemPosition+1)%3)==0){
            outRect.right = getDP(16);
        }else if (itemPosition%3==0){
            outRect.left = getDP(16);
        }else {
            outRect.left = getDP(8);
        }
        outRect.bottom = getDP(16);
    }

    private int getDP(int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
}