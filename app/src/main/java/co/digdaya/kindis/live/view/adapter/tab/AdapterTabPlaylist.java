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
    private String[] title;

    public AdapterTabPlaylist(FragmentManager fm, Context context, String[] title) {
        super(fm);
        this.context = context;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Playlist(title[0]);
            case 1:
                return new PlaylistPremium(title[1]);
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
        TextView tv = (TextView) v.findViewById(R.id.title_tab);
        tv.setText(title[position]);
        return v;
    }
}
