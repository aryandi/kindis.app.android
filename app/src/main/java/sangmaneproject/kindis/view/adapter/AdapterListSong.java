package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sangmaneproject.kindis.R;

/**
 * Created by DELL on 1/27/2017.
 */

public class AdapterListSong extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;
    int index;

    public AdapterListSong(Context context, int index) {
        this.mContext = context;
        this.index = index;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return index;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_list_song, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_list_song);
        imageView.setImageResource(R.drawable.bg_sign_in);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
