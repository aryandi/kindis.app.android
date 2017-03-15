package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.digdaya.kindis.R;

public class AdapterBannerEmpty extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;

    public AdapterBannerEmpty(Context context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_banner_empty, container, false);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((View) object);
    }
}
