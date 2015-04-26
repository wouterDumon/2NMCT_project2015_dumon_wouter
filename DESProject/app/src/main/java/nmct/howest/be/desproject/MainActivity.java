package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import nmct.howest.be.desproject.loader.Contract;
import nmct.howest.be.desproject.loader.SportcentraLoader;


public class MainActivity extends Activity implements OnMapReadyCallback,MainFragment.OnFragmentInteractionListener, ShowMapFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor> {

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
            getLoaderManager().initLoader(0,null,this);
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
private GoogleMap Gmap;
    @Override
    public void onMapReady(GoogleMap map) {
        Gmap = map;
       LatLng Testdata = new LatLng(50.806905141279,3.3014492766399);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Testdata, 5));
        for(String[] array : Lijstje){
            Double X = Double.parseDouble(array[6]);
            Double Y = Double.parseDouble(array[7]);
            LatLng Positie = new LatLng(X,Y);
            map.addMarker(new MarkerOptions()
                    .title(""+array[0])
                    .snippet("klik hier voor meer info")
                    .position(Positie));



        }
      //  madapter = new SportCentraAdapter(this,0,null,null,null,0);
      //  setListAdapter(madapter);
       // Object d = null;
       // getLoaderManager().initLoader(0,null,this);
    /*    map.addMarker(new MarkerOptions()
                .title("testmarker")
                .snippet("this is a test snippet?.")
                .position(Testdata));*/
    }
    SportcentraLoader a = null;
    @Override
    public void onFragmentInteraction(int page) {
        setContentView(R.layout.fragment_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
  //  private SportCentraAdapter madapter;
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      a =  new SportcentraLoader(this);
      return a;
  }
    private List<String[]> Lijstje;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //   madapter.swapCursor(data);
        Lijstje = a.getLijst();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //madapter.swapCursor(null);
    }

    }
