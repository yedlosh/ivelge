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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Map<String, String>> list=getdata();
        String[] from = {"date", "testName", "participantsNumber","upload"};
        int[] to={R.id.date,R.id.test_name,R.id.participant_number,R.id.upload};
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.test_list_item,from,to);
        setListAdapter(adapter);
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
        Map articleItemMap = (Map) getListAdapter().getItem(position);
        String name=(String)articleItemMap.get("testName");
        //Toast.makeText(this,Integer.toString(position)+" "+Long.toString(id)+" selected",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,TestDetail.class);
        Bundle b=new Bundle();
        b.putLong("id",id);
        b.putString("name",name);
        intent.putExtras(b);
        startActivity(intent);
    }

    private List<Map<String, String>> getdata(){
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map1 =new HashMap<String,String>();
        map1.put("date","10.3.2014");
        map1.put("testName","Test1");
        map1.put("participantsNumber","7 Participants");
        map1.put("upload",Integer.toString(R.drawable.ic_action_upload));
        list.add(map1);
        Map<String,String> map2 =new HashMap<String,String>();
        map2.put("date","11.3.2014");
        map2.put("testName","Test2");
        map2.put("participantsNumber","5 Participants");
        map2.put("upload",Integer.toString(R.drawable.ic_action_upload));
        list.add(map2);
        return list;
    }

}
