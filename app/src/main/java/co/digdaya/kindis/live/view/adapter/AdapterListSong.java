package co.digdaya.kindis.live.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;

/**
 * Created by DELL on 1/27/2017.
 */

public class AdapterListSong extends PagerAdapter {
    Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<String> imgList;
    PlayerSessionHelper playerSessionHelper;

    public AdapterListSong(Context context, ArrayList<String> imgList) {
        this.mContext = context;
        this.imgList = imgList;
        this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        playerSessionHelper = new PlayerSessionHelper();
    }



    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.adapter_list_song, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_list_song);
        String image;
        if (Boolean.parseBoolean(playerSessionHelper.getPreferences(mContext, "is_offline_mode"))){
            image = imgList.get(position);
        }else {
            image = ApiHelper.BASE_URL_IMAGE+imgList.get(position);
        }
        Glide.with(mContext)
                .load(image)
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
