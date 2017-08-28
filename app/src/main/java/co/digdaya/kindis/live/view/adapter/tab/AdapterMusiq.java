package co.digdaya.kindis.live.view.adapter.tab;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.fragment.bottomnavigation.musiq.Genres;
import co.digdaya.kindis.live.view.fragment.bottomnavigation.musiq.Discover;
import co.digdaya.kindis.live.view.fragment.bottomnavigation.musiq.RecentlyAdded;

/**
 * Created by vincenttp on 1/26/2017.
 */

public class AdapterMusiq extends FragmentStatePagerAdapter{
    Context context;
    int mNumOfTabs;
    String json;
    String[] title;
    Gson gson;

    public AdapterMusiq(FragmentManager manager, Context context, int mNumOfTabs, String json, String[] title) {
        super(manager);
        this.context = context;
        this.mNumOfTabs = mNumOfTabs;
        this.json = json;
        this.title = title;
        gson = new Gson();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Discover(json);
            case 1:
                return new RecentlyAdded(json);
            case 2:
                return new Genres(json);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.title_tab);
        tv.setText(title[position]);
        return v;
    }

}
