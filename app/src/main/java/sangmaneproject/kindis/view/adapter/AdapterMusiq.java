package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.fragment.musiq.Genres;
import sangmaneproject.kindis.view.fragment.musiq.MostPlayed;
import sangmaneproject.kindis.view.fragment.musiq.RecentlyAdded;

/**
 * Created by vincenttp on 1/26/2017.
 */

public class AdapterMusiq extends FragmentPagerAdapter {
    Context context;
    int mNumOfTabs;
    String json;
    private String[] title = {"MOST PLAYED", "RECENTLY ADDED", "GENRES"};

    public AdapterMusiq(FragmentManager manager, Context context, int mNumOfTabs, String json) {
        super(manager);
        this.context = context;
        this.mNumOfTabs = mNumOfTabs;
        this.json = json;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //Fragement for Android Tab
                return new MostPlayed(json);
            case 1:
                //Fragment for Ios Tab
                return new RecentlyAdded(json);
            case 2:
                //Fragment for Windows Tab
                return new Genres(json);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.title_tab);
        tv.setText(title[position]);
        return v;
    }

}
