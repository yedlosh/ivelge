package cz.ctu.pda.ivelge;

import android.app.ListActivity;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {
    private TestDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource=new TestDataSource(this);
        dataSource.open();
        List<Test> tests=dataSource.getAllTests();
        List<Map<String, String>> list=getdata(tests);
        String[] from = {"timestamp", "testName", "participantsNumber","uploaded"};
        int[] to={R.id.date,R.id.test_name,R.id.participant_number,R.id.upload};
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.test_list_item,from,to);
        setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.action_new_test){


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map itemMap = (Map) getListAdapter().getItem(position);
        String name=(String)itemMap.get("testName");
        Intent intent=new Intent(this,TestDetail.class);
        Bundle b=new Bundle();
        b.putString("id",(String)itemMap.get("id"));
        b.putString("name",name);
        intent.putExtras(b);
        startActivity(intent);
    }

    private List<Map<String, String>> getdata(List<Test> tests){
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Test test;
        Map<String,String> map;
        for(int i=0;i<tests.size();i++){
            map =new HashMap<String,String>();
            test=tests.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yyyy");
            map.put("id",Long.toString(test.getId()));
            map.put("timestamp",dateFormat.format(new Date(test.getTimestamp() * 1000)));
            map.put("testName",test.getName());
            map.put("participantNumber",Integer.toString(test.getSessions().size()));
            if(test.isUploaded()){
                map.put("uploaded",Integer.toString(R.drawable.ic_action_cloud));
            }else{
                map.put("uploaded",Integer.toString(R.drawable.ic_action_upload));
            }
            list.add(map);
        }
        return list;
    }

}
