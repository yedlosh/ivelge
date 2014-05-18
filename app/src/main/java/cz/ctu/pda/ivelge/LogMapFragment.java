package cz.ctu.pda.ivelge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class LogMapFragment extends Fragment {

    private SessionDataSource dataSource;
    private GoogleMap map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity parentActivity = getActivity();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        dataSource = new SessionDataSource(parentActivity);
        dataSource.open();

        Bundle b = parentActivity.getIntent().getExtras();
        Long id = Long.parseLong(b.getString("id"));
        Session session = dataSource.getSession(id);
        List<Log> logs = session.getLogs();
        createMarks(logs);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    private void createMarks(List<Log> logs) {
        LatLng startCoord = null;
        for (Log log : logs) {
            String title = "Priorita : " + log.getPriority();
            LatLng coordinates = new LatLng(log.getLatitude(), log.getLongitude());
            map.addMarker(new MarkerOptions().position(coordinates)
                    .title(title).snippet(log.getDescription())
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_launcher)));
            if (startCoord == null) {
                startCoord = coordinates;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(startCoord, 15));

            }
        }
    }
}
