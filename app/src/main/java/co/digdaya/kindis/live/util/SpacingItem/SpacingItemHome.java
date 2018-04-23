package co.digdaya.kindis.live.util.SpacingItem;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DELL on 4/1/2017.
 */

public class SpacingItemHome extends RecyclerView.ItemDecoration {
    private Context context;

    public SpacingItemHome(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition%2==0){
            outRect.left = getDP(16);
        }else {
            outRect.left = getDP(8);
        }
    }

    private int getDP(int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
