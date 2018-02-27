package co.digdaya.kindis.live.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.activity.Account.SignInActivity;

/**
 * Created by vincenttp on 1/24/2017.
 */

public class AdapterWalkthrough extends PagerAdapter {
    Context mContext;

    private LayoutInflater layoutInflater;

    public AdapterWalkthrough(Context context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private int[] img = new int[]{
            R.drawable.bg_walkthrough1, R.drawable.bg_walkthrough2, R.drawable.bg_walkthrough3
    };

    private String[] titles = new String[]{
            "Musiq\nEntertaining\nYour Soul", "Taklim\nEnlightening\nYour Soul", "Infaq\nEnriching\nYour Soul"
    };

    private String[] subtitles = new String[]{
            "Explore your favorite Islamic spiritual songs, play it, and let your soul entertained.",
            "Step your finger to reach for spiritual inspiration and let your soul enlightened.",
            "Kindness will never lessen. Do good, and let your soul enriched."
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
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_walkthrough, container, false);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.walkthrough);
        TextView title = (TextView) view.findViewById(R.id.title_walkthrough);
        TextView btn = (TextView) view.findViewById(R.id.btn_skip);
        TextView subTitle = (TextView) view.findViewById(R.id.subtitle_walkthrough);

        layout.setBackgroundResource(img[position]);
        title.setText(titles[position]);
        subTitle.setText(subtitles[position]);
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
