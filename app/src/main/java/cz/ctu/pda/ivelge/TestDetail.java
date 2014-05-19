package cz.ctu.pda.ivelge;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestDetail extends Activity implements TestDetailFragment.OnFragmentInteractionListener {
    SessionDataSource dataSource;
    public long id;
    List<Session> sessions;
    private String name;
    TestDetailFragment testDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_detail);

        dataSource = new SessionDataSource(this);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            if (b.containsKey("name")) {
                name = b.getString("name");
                this.setTitle(name);
            }
            if (b.containsKey("id")) {
                String ids = b.getString("id");
                id = Long.parseLong(ids);
            }
        } else {
            b = savedInstanceState;
            if (b != null) {
                if (b.containsKey("name")) {
                    name = b.getString("name");
                    this.setTitle(name);
                }
                if (b.containsKey("id_test")) {
                    String ids = b.getString("id_test");
                    id = Long.parseLong(ids);
                }
            }
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong("id", id);
            testDetailFragment = new TestDetailFragment();

            testDetailFragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.frameContainer, testDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id_test", id);
        outState.putString("name", name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(int position,String name, long sessionId) {
        Intent intent = new Intent(this, ParticipantDetailActivity.class);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putLong("testId", this.id);
        b.putLong("sessionId", sessionId);
        intent.putExtras(b);

        startActivity(intent);
    }
}
