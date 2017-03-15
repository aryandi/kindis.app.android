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

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;

/**
 * Created by DELL on 1/27/2017.
 */

public class AdapterListSong extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<String> imgList;

    public AdapterListSong(Context context, ArrayList<String> imgList) {
        this.mContext = context;
        this.imgList = imgList;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_list_song, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_list_song);

        Glide.with(mContext)
                .load(ApiHelper.BASE_URL_IMAGE+imgList.get(position))
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
        container.removeView((View) object);
    }
}
