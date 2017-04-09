package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.view.activity.Account.SignInActivity;

/**
 * Created by vincenttp on 1/24/2017.
 */

public class AdapterWalkthrough extends PagerAdapter {
    Context mContext;

    private LayoutInflater layoutInflater;

    public AdapterWalkthrough(Context context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }

    private int[] img = new int[]{
            R.drawable.bg_walkthrough1, R.drawable.bg_walkthrough2, R.drawable.bg_walkthrough3
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
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_walkthrough, container, false);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.walkthrough);
        TextView title = (TextView) view.findViewById(R.id.title_walkthrough);
        TextView btn = (TextView) view.findViewById(R.id.btn_skip);
        TextView subTitle = (TextView) view.findViewById(R.id.subtitle_walkthrough);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        switch (metrics.densityDpi){
            case DisplayMetrics.DENSITY_MEDIUM :
                title.setTextSize(22);
                subTitle.setTextSize(10);
                break;
            case DisplayMetrics.DENSITY_HIGH :
                title.setTextSize(24);
                subTitle.setTextSize(12);
                break;
        }

        layout.setBackgroundResource(img[position]);
        title.setText(titles[position]);
        btn.setText(btntxt[position]);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SignInActivity.class);
                mContext.startActivity(intent);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((View) object);
    }
}