package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wouter on 25/04/2015.
 */
public class ShowMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static ArrayList<String[]> empt = new ArrayList<>();
    private static List<String[]> Lijst = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(ArrayList<String[]> param1, List<String[]> param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();

        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);

        return fragment;
    }

    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        int i = 0;
        for (String[] sw : empt) {
            if (sw[1].equals("true") && !sw[0].equals("")) {
                editor.putString("Switchmap" + i, sw[0]);
                i++;
            }
        }
        editor.putInt("Aantalmapsw", i);
        editor.commit();
    }

    private MapFragment fragment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
            fm.executePendingTransactions();
        }
    }

    @Override
    public void onResume() {
        filluplists();
        if (empt.size() == 0) {
            empt = new ArrayList<>();
            SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int aantal = prefs.getInt("Aantalmapsw", 0);
            for (int ii = 0; ii < aantal; ii++) {
                String sw = prefs.getString("Switchmap" + ii, "");
                if (!sw.equals("")) {
                    String[] a = new String[2];
                    a[0] = sw;
                    a[1] = "true";
                    empt.add(a);
                }

            }
        }


        // filluplists();
        setUpMapIfNeeded(); //MOET VOOR SUPER

        super.onResume();
        // zetmapgoed(mMap);

    }

    public ShowMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void filluplists() {
        //  if (empt.size() == 0) {
        //   empt.clear();
        try {
            empt = ((MainActivity) getActivity()).getEmpty();
        } catch (Exception ex) {

            //}
        }
        if (Lijst.size() == 0) {
            Lijst.clear();
            Lijst = ((MainActivity) getActivity()).getLijstje();
        }

    }

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view;
        // View view = inflater.inflate(R.layout.fragment_map, container, false);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMap != null)
            zetmapgoed(mMap);

        if (mMap == null) {
            if (fragment == null) return;
            mMap = fragment.getMap();
            if (mMap != null)
                zetmapgoed(mMap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMap != null) {
            mMap = null;
        }
    }

    private static GoogleMap mMap;

    public void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = fragment.getMap();

            if (mMap != null)
                zetmapgoed(mMap);
        }
    }


    private void zetmapgoed(GoogleMap map) {

        map.clear(); //delete de vorige markers
        //map.setMyLocationEnabled(true);
        //3.257957375219,"x":50.829103511767
        LatLng Testdata = new LatLng(50.829103511767, 3.257957375219);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnInfoWindowClickListener(this);
        //map.setMyLocationEnabled(true);
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(Testdata, 11));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Testdata, 11), 650, null);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        for (String[] array : Lijst) {
            int gelijk = 0;
            for (String[] a : empt) {
                if (a[1].toLowerCase().equals("true") && !a[1].equals("")) {
                    if ((array[4].toString().toLowerCase()).equals((a[0].toString().toLowerCase()))) {
                        gelijk = 1;
                    }
                }
            }
            if (gelijk == 1) {
                //indien het er  inzit
                Double X = Double.parseDouble(array[6]);
                Double Y = Double.parseDouble(array[7]);
                LatLng Positie = new LatLng(Y, X);
                map.addMarker(new MarkerOptions()
                        .title("" + array[0])
                        .snippet("klik hier voor meer info")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(Positie));
            }
        }
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
    public void onInfoWindowClick(Marker marker) {
        mListener.onFrtInteraction(marker.getTitle(), empt);
    }


    public interface OnFragmentInteractionListener {

        public void onFrtInteraction(String marker, ArrayList<String[]> SwitchLisjt);
    }

}