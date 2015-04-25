package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity implements OnMapReadyCallback,MainFragment.OnFragmentInteractionListener, ShowMapFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainFragment fragment1 = new MainFragment();
            //parameters:
            //1: ID container
            //2: fragment
            //3: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).
            fragmentTransaction.add(R.id.container, fragment1, "mainfrag"); //VERPLICHT DIT TE ZETTEN ANDERS ZAL HIJ NULL FOUT GEVEN
            fragmentTransaction.commit();
        }
    }

/*
*
* */
private void showFragmentMap(){

    Fragment newFrag = new ShowMapFragment().newInstance("a","v");
    FragmentManager fMgr = getFragmentManager();
    FragmentTransaction fTr = fMgr.beginTransaction();
    fTr.replace(R.id.container, newFrag);
    fTr.addToBackStack("mainfrag");
    fTr.commit();
}

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Testdata = new LatLng(50.806905141279,3.3014492766399);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Testdata, 13));

        map.addMarker(new MarkerOptions()
                .title("testmarker")
                .snippet("this is a test snippet?.")
                .position(Testdata));
    }

    @Override
    public void onFragmentInteraction(int page) {
        setContentView(R.layout.fragment_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}