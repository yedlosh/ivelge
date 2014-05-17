package cz.ctu.pda.ivelge;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 17.5.2014.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.tabFragment = new ArrayList<Fragment>();
    }

    public TabsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.tabFragment = fragments;
    }

    public void setTabFragment(List<Fragment> fragments) {
        this.tabFragment = fragments;
    }

    @Override
    public Fragment getItem(int index) {

        return tabFragment.get(index);
/*
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new LogFragment();
            case 1:
                // Games fragment activity
                return new MapFragment();
        }

        return null;*/
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2; //tabFragment.size();
    }

}
