package cz.ctu.pda.ivelge;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestDetail extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        Bundle b = getIntent().getExtras();
        this.setTitle(b.getString("name"));

        List<Map<String, String>> list=getdata();
        String[] from = {"participant", "finished","duration","logs"};
        int[] to={R.id.participant_name,R.id.finished_text,R.id.duration_text,R.id.logs_text};
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.test_detail_list_item,from,to);
        setListAdapter(adapter);
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

    private List<Map<String, String>> getdata(){
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map1 =new HashMap<String,String>();
        map1.put("participant","Participant1");
        map1.put("finished","15:43 15.3.2014");
        map1.put("logs","7");
        map1.put("duration","0:25");
        list.add(map1);
        return list;
    }
}
