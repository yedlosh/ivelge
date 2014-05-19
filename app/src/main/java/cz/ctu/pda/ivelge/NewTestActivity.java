package cz.ctu.pda.ivelge;

import android.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class NewTestActivity extends ActionBarActivity {
    LinearLayout linearPart;
    LinearLayout linearTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_test);

        linearPart = (LinearLayout) findViewById(R.id.new_test_participants_layout);
        linearTask = (LinearLayout) findViewById(R.id.new_test_tasks_layout);

        if (savedInstanceState != null) {
            EditText e;
            int pcount = linearPart.getChildCount();
            int bpcount = savedInstanceState.getInt("participantsCount");
            int tcount = linearTask.getChildCount();
            int btcount = savedInstanceState.getInt("tasksCount");
            for (int i = 0; i < bpcount; i++) {
                if (bpcount == pcount) {
                    e = (EditText) linearPart.getChildAt(i);
                } else {
                    e = new EditText(this);
                }
                e.setText(savedInstanceState.getString("p" + i));
                e.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearPart.addView(e);
            }
            for (int i = 0; i < btcount; i++) {
                if (btcount == tcount) {
                    e = (EditText) linearPart.getChildAt(i);
                } else {
                    e = new EditText(this);
                }
                e.setText(savedInstanceState.getString("t" + i));
                e.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearTask.addView(e);
            }
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_test, menu);
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
    }*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayout participantLayout = (LinearLayout) findViewById(R.id.new_test_participants_layout);
        int participantsCount = participantLayout.getChildCount();
        LinearLayout taskLayout = (LinearLayout) findViewById(R.id.new_test_tasks_layout);
        int tasksCount = taskLayout.getChildCount();
        outState.putInt("participantsCount", participantsCount);
        outState.putInt("tasksCount", tasksCount);
        for (int i = 0; i < participantsCount; i++) {
            outState.putString("p" + i, participantLayout.getChildAt(i).toString());
        }
        for (int i = 0; i < tasksCount; i++) {
            outState.putString("t" + i, taskLayout.getChildAt(i).toString());
        }
    }


    public void addParticipant(View view) {
        EditText text = new EditText(this);
        //???text .setId(i);
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearPart = (LinearLayout) findViewById(R.id.new_test_participants_layout);
        text.setText("Participant " + (linearPart.getChildCount() + 1));
        linearPart.addView(text);
    }

    public void addTask(View view) {
        EditText text = new EditText(this);
        //???text .setId(i);
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearTask = (LinearLayout) findViewById(R.id.new_test_tasks_layout);
        text.setText("Task " + (linearTask.getChildCount() + 1));
        linearTask.addView(text);
    }

    public void saveNewTest(View view) {
        long timestamp = System.currentTimeMillis() / 1000;
        Test newTest = new Test(timestamp);

        List<Session> sessions = new ArrayList<Session>();

        EditText text = (EditText) findViewById(R.id.new_test_name);
        newTest.setName(text.getText().toString());

        LinearLayout lin;
        lin = (LinearLayout) findViewById(R.id.new_test_participants_layout);

        for (int i = 0; i < lin.getChildCount(); i++) {
            text = (EditText) lin.getChildAt(i);//participant name
            Session session = new Session(text.getText().toString());//new session with participant name
            sessions.add(session);
        }

        newTest.setSessions(sessions);
        lin = (LinearLayout) findViewById(R.id.new_test_tasks_layout);

        for (int i = 0; i < lin.getChildCount(); i++) {
            text = (EditText) lin.getChildAt(i);
            newTest.addTask(text.getText().toString());
        }

        TestDataSource dataSource = new TestDataSource(this);
        dataSource.open();
        dataSource.insertTest(newTest);
        dataSource.close();

        NavUtils.navigateUpFromSameTask(this);
    }
}
