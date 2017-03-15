package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class AdapterMusiqSlider extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<HashMap<String, String>> listBanner;
    HashMap<String, String> dataSinggle;

    public AdapterMusiqSlider(Context context, ArrayList<HashMap<String, String>> listBanner) {
        this.mContext = context;
        this.listBanner = listBanner;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listBanner.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_musiq_slider, container, false);
        dataSinggle = listBanner.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_musiq_slider);

        Glide.with(mContext)
                .load(ApiHelper.BASE_URL_IMAGE+dataSinggle.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((View) object);
    }
}
