package sangmaneproject.kindis.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincenttp on 1/26/2017.
 */

public class AdapterMusiq extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public AdapterMusiq(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    /*public View getTabView(int position) {
        View tab = LayoutInflater.from(Main.this).inflate(R.layout.custom_tabview, null);
        TextView tv = (TextView) tab.findViewById(R.id.custom_text);
        tv.setText(mFragmentTitleList.get(position));
        return tab;
    }*/
}
