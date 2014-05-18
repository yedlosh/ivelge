package cz.ctu.pda.ivelge;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class SessionActivity extends ActionBarActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        Bundle b = getIntent().getExtras();
        this.setTitle(b.getString("name"));
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getFragmentManager());

        viewPager.setAdapter(mAdapter);
        // actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this.setTabs();

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void setTabs() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        actionBar.addTab(actionBar.newTab().setText(R.string.log)
                .setTabListener(this));
        fragmentList.add(new LogListFragment());

<<<<<<< HEAD
        actionBar.addTab(actionBar.newTab().setText("MAP")
=======
        actionBar.addTab(actionBar.newTab().setText(R.string.map)
>>>>>>> origin/master
                .setTabListener(this));
        fragmentList.add(new LogMapFragment());

        this.mAdapter.setTabFragment(fragmentList);

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}
