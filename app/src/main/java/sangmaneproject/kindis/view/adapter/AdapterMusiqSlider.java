package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sangmaneproject.kindis.R;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class AdapterMusiqSlider extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;

    public AdapterMusiqSlider(Context context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }

    private int[] img = new int[]{
            R.drawable.bg_sign_in, R.drawable.bg_sign_in, R.drawable.bg_sign_in
    };

    private String[] titles = new String[]{
            "Musiq interesting things", "Taklim interesting things", "Infaq interesting things"
    };

    private String[] btntxt = new String[]{
            "SKIP", "SKIP", "LET'S GO"
    };

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_musiq_slider, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_musiq_slider);
        imageView.setImageResource(img[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((View) object);
    }
}
