package cz.ctu.pda.ivelge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParticipantDetailActivity extends ActionBarActivity {
    private SessionDataSource dataSource;
    private Session session;
    private long testId;
    private boolean starting;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_participant_detail);

        dataSource = new SessionDataSource(this);
        dataSource.open();

        Bundle b = getIntent().getExtras();

        if (b != null) {
            if (b.containsKey("name")) {
                this.setTitle(b.getString("name"));
            }
            if (b.containsKey("sessionId") && b.containsKey("testId")) {
                testId = b.getLong("testId");
                long sessionId = b.getLong("sessionId");

                session = dataSource.getSession(sessionId);

                EditText pretest = (EditText) findViewById(R.id.pretest);
                if(session.getPreTest() != null) {
                    pretest.setText(session.getPreTest());
                }

                EditText posttest = (EditText) findViewById(R.id.posttest);

                TextView posttest_label = (TextView) findViewById(R.id.posttest_label);
                Button viewLogBtn = (Button) findViewById(R.id.view_log_btn);
                Button startSessionBtn = (Button) findViewById(R.id.start_session_btn);
                if (!session.started()) {
                    posttest_label.setVisibility(View.GONE);
                    posttest.setVisibility(View.GONE);
                    viewLogBtn.setVisibility(View.GONE);
                    startSessionBtn.setVisibility(View.VISIBLE);
                } else {
                    posttest_label.setVisibility(View.VISIBLE);
                    posttest.setVisibility(View.VISIBLE);
                    if(session.getPostTest() != null) {
                        posttest.setText(session.getPostTest());
                    }
                    viewLogBtn.setVisibility(View.VISIBLE);
                    startSessionBtn.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onPause(){
        EditText pretest = (EditText) findViewById(R.id.pretest);
        if(pretest.getText().length() != 0){
            session.setPreTest(pretest.getText().toString());
        }
        if(session.started()){
            EditText posttest = (EditText) findViewById(R.id.posttest);
            if(posttest.getText().length() != 0){
                session.setPostTest(posttest.getText().toString());
            }
        }
        if(starting){
            session.setStartTime(System.currentTimeMillis() / 1000);
        }

        dataSource.commitSession(session);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.participant_detail, menu);
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

    public void viewLog(View view) {
        Intent intent = new Intent(this, SessionActivity.class);
        Bundle b = new Bundle();
        b.putLong("sessionId", session.getId());
        b.putString("name", session.getParticipantName());
        b.putLong("endTime", session.getEndTime());
        intent.putExtras(b);
        startActivity(intent);
    }

    public void startSession(View view) {
        Intent intent = new Intent(this, SessionActivity.class);
        Bundle b = new Bundle();
        b.putLong("testId", testId);
        b.putLong("sessionId", session.getId());
        b.putString("name", session.getParticipantName());
        intent.putExtras(b);
        starting = true;
        startActivity(intent);
    }
}
