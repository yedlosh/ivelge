package cz.ctu.pda.ivelge;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestDetail extends ListActivity {
    SessionDataSource dataSource;
    public long id;
    List<Session> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_detail);

        dataSource = new SessionDataSource(this);

        Bundle b = getIntent().getExtras();

        if(b != null){
            if(b.containsKey("name")){
                this.setTitle(b.getString("name"));
            }
            if(b.containsKey("id")){
                String ids=b.getString("id");
                id=Long.parseLong(ids);

                dataSource.open();
                sessions = dataSource.getTestSessions(id);
                List<Map<String, String>> list=getdata(sessions);

                String[] from = {"participant", "finished","duration","logs"};
                int[] to={R.id.participant_name,R.id.finished_text,R.id.duration_text,R.id.logs_text};
                SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.test_detail_list_item,from,to);
                setListAdapter(adapter);
            }
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map articleItemMap = (Map) getListAdapter().getItem(position);
        String name=(String)articleItemMap.get("participant");
        Intent intent=new Intent(this,ParticipantDetailActivity.class);
        Bundle b=new Bundle();
        b.putString("name",name);
        b.putLong("testId",id);
        b.putLong("sessionId",sessions.get(position).getId());
        intent.putExtras(b);
        startActivity(intent);
    }

    private List<Map<String, String>> getdata(List<Session> sessions){
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map;
        Session session;
        for(int i=0;i<sessions.size();i++){
            session=sessions.get(i);
            map=new HashMap<String,String>();
            map.put("participant",session.getParticipantName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm d.M.yyyy");
            map.put("finished",dateFormat.format(new Date(session.getEndTime() * 1000)));
            map.put("logs",Integer.toString(session.getNumberOfLogs()));
            dateFormat = new SimpleDateFormat("H:mm");
            long duration=session.getEndTime()-session.getStartTime();
            map.put("duration",dateFormat.format(new Date(duration * 1000)));
            list.add(map);
        }
        return list;
    }
}
