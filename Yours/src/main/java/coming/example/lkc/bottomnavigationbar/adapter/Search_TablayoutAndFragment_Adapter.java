package coming.example.lkc.bottomnavigationbar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lkc on 2017/10/16.
 */

public class Search_TablayoutAndFragment_Adapter extends FragmentPagerAdapter {
    private List<String> tab_title;
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public Search_TablayoutAndFragment_Adapter(FragmentManager fm, List<String> tab_title, List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        this.tab_title = tab_title;
    }

    public void removeALLfragment() {
        FragmentTransaction fs = fm.beginTransaction();
        fs.remove(fragmentList.get(0));
        fs.remove(fragmentList.get(1));
        fs.commit();
        notifyDataSetChanged();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
