package cz.ctu.pda.ivelge;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends ListFragment {
    private CategoryDataSource categoryDataSource;
    private LogDataSource logDataSource;
    private TestDataSource testDataSource;
    private SessionDataSource sessionDataSource;
    private List<Log> logs;
    private long logId;
    long sessionId;
    long testId;

    public interface Callbacks {
        public double getLatitude();
        public double getLongitude();
    }

    //private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle b = savedInstanceState;

        if(getArguments().containsKey("sessionId")) {
            sessionId = getArguments().getLong("sessionId");
            testId = getArguments().getLong("testId");
        } else if (b != null && b.containsKey("sessionId")){
            sessionId = b.getLong("sessionId");
            testId = b.getLong("testId");
        }

        logDataSource = new LogDataSource(getActivity());
        categoryDataSource = new CategoryDataSource(getActivity());
        testDataSource = new TestDataSource(getActivity());
        logDataSource.open();

    }

    @Override
    public void onDestroy() {
        categoryDataSource.close();
        logDataSource.close();
        testDataSource.close();

        super.onDestroy();
    }

/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (Callbacks) activity;
        } catch (ClassCastException ex) {
            android.util.Log.e(LogFragment.class.getName(), "Casting the activity as a Callbacks listener failed" + ex);
            mCallbacks = null;
        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        logs = logDataSource.getSessionLogs(sessionId);
        List<Map<String, String>> list = getdata(logs);

        String[] from = {"timestamp", "priority", "task", "category", "subcategory1"};
        int[] to = {R.id.log_item_time, R.id.log_item_priority, R.id.log_item_task, R.id.log_item_category, R.id.log_item_subcategory1};
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), list, R.layout.log_list_item, from, to);
        setListAdapter(adapter);

        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Map itemMap = (Map) getListAdapter().getItem(position);
        Intent intent = new Intent(this.getActivity(), EventDetailActivity.class);
        Bundle b = new Bundle();
        b.putLong("logId", logs.get(position).getId());
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putLong("sessionId",sessionId);
        outState.putLong("testId",testId);
        super.onSaveInstanceState(outState);
    }


    private List<Map<String, String>> getdata(List<Log> logs) {

        categoryDataSource.open();
        testDataSource.open();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Log log;
        Map<String, String> map;
        for (int i = 0; i < logs.size(); i++) {
            map = new HashMap<String, String>();
            log = logs.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
            map.put("id", Long.toString(log.getId()));
            map.put("timestamp", "Time: " + dateFormat.format(new Date(log.getTimestamp() * 1000)));
            map.put("priority", Integer.toString(log.getPriority()));
            Test test = testDataSource.getTest(testId);
            String task = test.getTasks().get(log.getTaskIndex());
            map.put("task",task); //todo
            Category category = categoryDataSource.getCategory(log.getCategoryId());
            map.put("category", category.getName());
            map.put("subcategory1", category.getSubcategory(log.getSubcategoryIndex()));
            list.add(map);
        }
        categoryDataSource.close();
        return list;
    }


    /*public void showComments(View view) {
        Map itemMap = (Map) getListAdapter().getItem(position);
        Intent intent=new Intent(this.getActivity(), EventDetailActivity.class);
        Bundle b=new Bundle();
        b.putLong("logId",session.getId());
        intent.putExtras(b);
        startActivity(intent);
    }
*/
}
