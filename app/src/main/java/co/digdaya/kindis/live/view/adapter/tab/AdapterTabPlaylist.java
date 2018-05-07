package co.digdaya.kindis.live.view.adapter.tab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.fragment.bottomnavigation.playlist.Playlist;
import co.digdaya.kindis.live.view.fragment.bottomnavigation.playlist.PlaylistPremium;

/**
 * Created by DELL on 3/28/2017.
 */

public class AdapterTabPlaylist extends FragmentPagerAdapter {
    private Context context;
    private String[] titles;

    public AdapterTabPlaylist(FragmentManager fm, Context context, String[] titles) {
        super(fm);
        this.context = context;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Playlist(titles[0]);
            case 1:
                return new PlaylistPremium(titles[1]);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        TextView tv = v.findViewById(R.id.title_tab);
        tv.setText(titles[position]);
        return v;
    }
}
