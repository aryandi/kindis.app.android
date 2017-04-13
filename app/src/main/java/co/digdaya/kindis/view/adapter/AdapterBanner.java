package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class AdapterBanner extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<HashMap<String, String>> listBanner;
    HashMap<String, String> dataSinggle;
    String type;

    public AdapterBanner(Context context, ArrayList<HashMap<String, String>> listBanner, String type) {
        this.mContext = context;
        this.listBanner = listBanner;
        this.type = type;
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
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
        Button btnDonate = (Button) view.findViewById(R.id.btn_donate);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_musiq_slider);

        Glide.with(mContext)
                .load(dataSinggle.get("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataSinggle.get("link")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);*/
            }
        });

        if (type.equals("infaq")){
            title.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.VISIBLE);
            btnDonate.setVisibility(View.VISIBLE);

            title.setText(dataSinggle.get("title"));
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((View) object);
    }
}
