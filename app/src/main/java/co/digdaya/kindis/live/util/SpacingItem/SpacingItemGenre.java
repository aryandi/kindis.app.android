package co.digdaya.kindis.live.util.SpacingItem;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vincenttp on 4/2/2017.
 */

public class SpacingItemGenre extends RecyclerView.ItemDecoration {
    Context context;
    String type;

    public SpacingItemGenre(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (type.equals("more")) {
            if (itemPosition == 0 || itemPosition == 1 || itemPosition == 2){
                outRect.top = getDP(16);
            }
        }
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
        return (int) (dp * scale + 0.5f);
    }
}