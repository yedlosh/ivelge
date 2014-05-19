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
    private int position;
    private long testId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_detail);
        Bundle b=getIntent().getExtras();
        this.setTitle(b.getString("name"));
        dataSource.open();
        testId=b.getLong("testId");
        position=b.getInt("position");
        List<Session> sessions=dataSource.getTestSessions(testId);
        session=getdata(sessions,position);
        EditText pretest=(EditText)findViewById(R.id.pretest);
        pretest.setText(session.getPreTest());
        TextView posttest_label=(TextView)findViewById(R.id.posttest_label);
        EditText posttest=(EditText)findViewById(R.id.posttest);
        Button viewLogBtn=(Button)findViewById(R.id.view_log_btn);
        Button startSessionBtn=(Button)findViewById(R.id.start_session_btn);
        if(session.getStartTime()==-1){//not started
            posttest_label.setVisibility(2);//gone
            posttest.setVisibility(2);//gone
            viewLogBtn.setVisibility(2);//gone
            startSessionBtn.setVisibility(0);//visible
        }else{
            posttest_label.setVisibility(0);//visible
            posttest.setVisibility(0);//visible
            posttest.setText(session.getPostTest());
            viewLogBtn.setVisibility(0);//visible
            startSessionBtn.setVisibility(2);//gone
        }
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
    private Session getdata(List<Session> sessions,int position){
        return sessions.get(position);
    }


    public void viewLog(View view) {
        Intent intent=new Intent(this, SessionActivity.class);
        Bundle b=new Bundle();
        b.putLong("sessionId",session.getId());
        b.putString("name",session.getParticipantName());
        b.putLong("endTime",session.getEndTime());
        intent.putExtras(b);
        startActivity(intent);
    }

    public void startSession(View view) {
        Intent intent=new Intent(this, StartSessionActivity.class);
        Bundle b=new Bundle();
        b.putLong("testId",testId);
        b.putLong("sessionId",session.getId());
        b.putString("name",session.getParticipantName());
        b.putInt("position",position);
        intent.putExtras(b);
        startActivity(intent);
    }
}
