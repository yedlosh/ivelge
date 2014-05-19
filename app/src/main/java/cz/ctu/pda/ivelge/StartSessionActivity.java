package cz.ctu.pda.ivelge;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class StartSessionActivity extends ActionBarActivity implements
        ActionBar.TabListener,AdapterView.OnItemSelectedListener {
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private TestDataSource dataSource;
    private ArrayAdapter<String> spAdapter;
    private String name;
    private Long testId;
    private Long sessionId;
    private int selectedTaskIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_session);
        dataSource=new TestDataSource(this);
        dataSource.open();
        Bundle b = getIntent().getExtras();
        name=b.getString("name");
        this.setTitle(name);
        testId=b.getLong("testId");
        sessionId=b.getLong("sessionId");
        Test test=dataSource.getTest(testId);
        Spinner spinner=(Spinner)findViewById(R.id.tasks_spinner);
        spinner.setOnItemSelectedListener(this);
        spAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,test.getTasks());
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        viewPager = (ViewPager) findViewById(R.id.pagerStart);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this.setTabs();
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
        //fragmentList.add(new StartLogFragment());
        fragmentList.add(new LogFragment());
        actionBar.addTab(actionBar.newTab().setText(R.string.map)
                .setTabListener(this));
        fragmentList.add(new LogMapFragment());

        this.mAdapter.setTabFragment(fragmentList);

    }
    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.end_session) {
            //??todo??
            dataSource.close();
            Intent intent=new Intent(this,ParticipantDetailActivity.class);
            Bundle b=new Bundle();
            b.putString("name",name);
            b.putLong("testId",testId);
            b.putLong("sessionId", sessionId);
            intent.putExtras(b);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public void addNewEvent(View view) {
        Intent intent=new Intent(this,NewEventActivity.class);
        Bundle b=new Bundle();
        b.putInt("selectedTaskIndex",selectedTaskIndex);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        selectedTaskIndex=pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //selectedTaskIndex=0;
    }
}
