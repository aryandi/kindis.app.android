package co.digdaya.kindis.live.view.adapter.tab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.fragment.navigationview.saveoffline.AlbumSaveOffline;
import co.digdaya.kindis.live.view.fragment.navigationview.saveoffline.PlaylistSaveOffline;
import co.digdaya.kindis.live.view.fragment.navigationview.saveoffline.SingleSaveOffline;

/**
 * Created by DELL on 4/30/2017.
 */

public class AdapterTabSaveOffline extends FragmentStatePagerAdapter {
    Context context;
    String[] title;

    public AdapterTabSaveOffline(FragmentManager fm, Context context, String[] title) {
        super(fm);
        this.context = context;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SingleSaveOffline();
            case 1:
                return new AlbumSaveOffline();
            case 2:
                return new PlaylistSaveOffline();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        TextView tv = v.findViewById(R.id.title_tab);
        tv.setText(title[position]);
        return v;
    }
}