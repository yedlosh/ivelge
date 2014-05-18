package cz.ctu.pda.ivelge;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogListFragment extends ListFragment {
    private SessionDataSource dataSource;
    Log log;
    private Activity parentActivity;

    public LogListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new SessionDataSource(getActivity());
        dataSource.open();

        Activity parentActivity = getActivity();

        Bundle b = parentActivity.getIntent().getExtras();
        Long id = Long.parseLong(b.getString("id"));
        Session session = dataSource.getSession(id);
        List<Log> logs = session.getLogs();
        List<Map<String, String>> list = getdata(logs);

        //TODO
        String[] from = {"", "", "", ""};
        int[] to = {R.id.log_item_priority, R.id.log_item_task, R.id.log_item_time};

        SimpleAdapter adapter = new SimpleAdapter(parentActivity, list, R.layout.log_list_item, from, to);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment-same like for MainActivity
        View view = inflater.inflate(R.layout.activity_log, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

    private List<Map<String, String>> getdata(List<Log> logs) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        Log log;
        for (int i = 0; i < logs.size(); i++) {
            log = logs.get(i);
            map = new HashMap<String, String>();
            //TODO
        }
        return list;
    }


}
