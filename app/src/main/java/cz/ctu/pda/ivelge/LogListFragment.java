package cz.ctu.pda.ivelge;



import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LogListFragment extends ListFragment {
    private LogDataSource dataSource;
    Log log;

    public LogListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dataSource=new LogDataSource(getActivity());
        dataSource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment-same like for MainActivity
        View view=inflater.inflate(R.layout.activity_main, container, false);
        return view;
    }


}
