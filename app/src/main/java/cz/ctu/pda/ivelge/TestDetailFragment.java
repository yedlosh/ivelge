package cz.ctu.pda.ivelge;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestDetailFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    SessionDataSource dataSource;
    List<Session> sessions;
    public long id;

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(int position,String name,long sessionId);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = savedInstanceState;

        if(getArguments().containsKey("id")) {
            id = getArguments().getLong("id");
        } else if (b != null && b.containsKey("id")){
            id = b.getLong("id");
        }

        dataSource = new SessionDataSource(getActivity());
        dataSource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        sessions = dataSource.getTestSessions(id);
        List<Map<String, String>> list = getdata(sessions);

        String[] from = {"participant", "finished", "duration", "logs", ""};
        int[] to = {R.id.participant_name, R.id.finished_text, R.id.duration_text, R.id.logs_text, R.id.notStarted_text};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.test_detail_list_item, from, to);

        SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
            int current = 0;
            boolean first = true;

            public boolean setViewValue(View view, Object data, String textRepresentation) {
                //android.util.Log.i(TestDetail.class.getName(),"AdapterView: " + view.getTag());
                if (view.getTag() != null && view.getTag().toString().equals("participantName")) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    if (first) {
                        first = false;
                    } else {
                        current++;
                    }
                } else if (view.getTag() != null && view.getTag().toString().equals("notStarted") && !sessions.get(current).started()) {
                    view.setVisibility(View.VISIBLE);
                } else if (!sessions.get(current).started()) {
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.GONE);
                } else {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                }
                return true;
            }
        };

        adapter.setViewBinder(viewBinder);
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_testdetail_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("id", id);
        super.onSaveInstanceState(savedInstanceState);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            Map articleItemMap = (Map) getListAdapter().getItem(position);
            String name = (String) articleItemMap.get("participant");
            long sessionId = sessions.get(position).getId();
            mListener.onFragmentInteraction(position,name,sessionId);
        }
    }

    private List<Map<String, String>> getdata(List<Session> sessions) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        Session session;
        for (int i = 0; i < sessions.size(); i++) {
            session = sessions.get(i);
            map = new HashMap<String, String>();
            map.put("participant", session.getParticipantName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm d.M.yyyy");
            map.put("finished", dateFormat.format(new Date(session.getEndTime() * 1000)));
            map.put("logs", Integer.toString(session.getNumberOfLogs()));
            dateFormat = new SimpleDateFormat("H:mm");
            long duration = session.getEndTime() - session.getStartTime();
            map.put("duration", dateFormat.format(new Date(duration * 1000)));
            list.add(map);
        }
        return list;
    }

    @Override
    public void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }
}
