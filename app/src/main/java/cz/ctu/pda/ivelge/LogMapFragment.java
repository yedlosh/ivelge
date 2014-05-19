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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class LogMapFragment extends Fragment {

    private SessionDataSource dataSource;
    private GoogleMap map;
    private Long sessionId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new SessionDataSource(getActivity());
        dataSource.open();
        Bundle b=getActivity().getIntent().getExtras();
        sessionId=b.getLong("sessionId");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        MapFragment mapFragment=((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
        Session session = dataSource.getSession(sessionId);
        List<Log> logs = session.getLogs();
        createMarks(logs);

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
