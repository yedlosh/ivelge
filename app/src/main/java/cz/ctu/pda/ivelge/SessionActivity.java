package cz.ctu.pda.ivelge;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.openenviron.andeasylib.EasyLocation;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class SessionActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private SharedPreferences mPrefs;
    double currentLat;
    double currentLong;
    long sessionId;
    long testId;

    private LogFragment logFragment;
    private LogMapFragment logMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);

        Bundle b = getIntent().getExtras();

        if (b != null && b.containsKey("sessionId")) {
            sessionId = b.getLong("sessionId");
            testId = b.getLong("testId");
        }

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        //For setting correct tab when returning to main activity from article detail activity
        mPrefs = this.getPreferences(MODE_PRIVATE);
        //mViewPager.setCurrentItem(mPrefs.getInt("tabIndex", 0));
        mViewPager.setCurrentItem(0);

        EasyLocation.startGPS(this);

        BroadcastReceiver br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // The Latitude data is available as string Extra from the intent.
                currentLat = Double.parseDouble(intent.getStringExtra(EasyLocation.LATITUDE));
                currentLong = Double.parseDouble(intent.getStringExtra(EasyLocation.LONGITUDE));
            }
        };

        // Register the Broadcast receiver and use an intent Filter passing in the AND_EASY_LIB. this filter is universal for the library.
        registerReceiver(br, new IntentFilter(EasyLocation.AND_EASY_LIB));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.session, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_newEvent) {
            Intent intent = new Intent(this, NewEventActivity.class);
            Bundle b = new Bundle();
            b.putLong("testId", testId);
            b.putLong("sessionId", sessionId);
            intent.putExtras(b);
            startActivity(intent);
        } else if (id == R.id.action_endSession) {
            //TODO save EVERYTHING
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (logFragment == null) {
                    Bundle arguments = new Bundle();
                    arguments.putLong("sessionId", sessionId);
                    logFragment = new LogFragment();
                    logFragment.setArguments(arguments);
                }
                return logFragment;
            }
            if (position == 1) {
                if (logMapFragment == null) {
                    Bundle arguments = new Bundle();
                    arguments.putLong("sessionId", sessionId);
                    logMapFragment = new LogMapFragment();
                    logMapFragment.setArguments(arguments);
                }
                return logMapFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_log).toUpperCase(l);
                case 1:
                    return getString(R.string.title_map).toUpperCase(l);
            }
            return null;
        }
    }
}
