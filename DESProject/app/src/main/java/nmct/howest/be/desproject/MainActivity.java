package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import nmct.howest.be.desproject.loader.Contract;
import nmct.howest.be.desproject.loader.SportcentraLoader;


public class MainActivity extends Activity implements OnMapReadyCallback,MainFragment.OnFragmentInteractionListener, ShowMapFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor>, GoogleMap.OnInfoWindowClickListener, DetailFragment.OnFragmentInteractionListener {
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            progress = ProgressDialog.show(this, "Even geduld",
                    "Bezig met het ophalen van data", true);
            new Thread(new Runnable() {
                @Override
                public void run()
                {

                    // do the thing that takes a long time
                //   a = new SportcentraLoader(MainActivity.this);
getLoaderManager().initLoader(0,null,MainActivity.this);


                    while(Lijstje.size() == 0){
                       try {
                           Lijstje = a.getLijst();
                           GeefLijstAanDetails = Lijstje;
                       }catch(Exception ex){}
                    }
               //     a =  new SportcentraLoader(MainActivity.this);
                 /*   try {
                    //    Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationCot(), "error",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                          /*  Lijstje = a.getLijst();
if(Lijstje.size() == 0){
    Toast.makeText(getApplicationContext(), "NOT GOOD",
            Toast.LENGTH_LONG).show();
}
*/

                            progress.dismiss();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            MainFragment fragment1 = new MainFragment().newInstance(Lijstje);
                            //parameters:
                            //1: ID container
                            //2: fragment
                            //3: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).
                            fragmentTransaction.add(R.id.container, fragment1, "mainfrag"); //VERPLICHT DIT TE ZETTEN ANDERS ZAL HIJ NULL FOUT GEVEN
                            fragmentTransaction.commit();
                        }
                    });
                }
            }).start();
        }
    }

/*
*
* */
private ArrayList<String[]> empty = new ArrayList<String[]>();
 private void showFragmentMap(ArrayList<String[]> Lijst){
empty = Lijst;
    Fragment newFrag = new ShowMapFragment().newInstance("a","b");
    FragmentManager fMgr = getFragmentManager();
    FragmentTransaction fTr = fMgr.beginTransaction();
    fTr.replace(R.id.container, newFrag);
    fTr.addToBackStack("mainfrag");
    fTr.commit();
    MapFragment mapFragment = (MapFragment) newFrag;
    mapFragment.getMapAsync(this);
}
private GoogleMap Gmap;
    @Override
    public void onMapReady(GoogleMap map) {

        Gmap = map;
 //      setContentView(R.layout.activity_main);
      map.clear(); //delete de vorige markers
       LatLng Testdata = new LatLng(50.806905141279,3.3014492766399);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnInfoWindowClickListener(this);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Testdata, 11));
        for(String[] array : Lijstje){
            int gelijk = 0;
            for(String[] a : empty) {
                if (a[1].toLowerCase().equals("true")) {
                    if ((array[4].toString().toLowerCase()).equals((a[0].toString().toLowerCase()))) {
                        gelijk = 1;
                    }
                }
            }
            if(gelijk==1){
                //indien het er  inzit
                Double X = Double.parseDouble(array[6]);
                Double Y = Double.parseDouble(array[7]);
                LatLng Positie = new LatLng(Y,X);
                map.addMarker(new MarkerOptions()
                        .title("" + array[0])
                        .snippet("klik hier voor meer info")
                        .position(Positie));
            }





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

  //  private SportCentraAdapter madapter;
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      a =  new SportcentraLoader(this);
      return a;
  }
    private List<String[]> Lijstje = new ArrayList<String[]>();
    private List<String[]> GeefLijstAanDetails = new ArrayList<String[]>();
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //   madapter.swapCursor(data);
        progress.dismiss();
       // Lijstje = a.getLijst();
        Toast.makeText(getApplicationContext(), "YaY",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //madapter.swapCursor(null);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        showFragmentDetails(marker.getTitle());
       // Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();

    }

    private void showFragmentDetails(String title) {
       /* requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);*/
      //  setContentView(R.layout.activity_main);
        Fragment newFrag = new DetailFragment().newInstance(title,GeefLijstAanDetails,empty);
        FragmentManager fMgr = getFragmentManager();
        FragmentTransaction fTr = fMgr.beginTransaction();
        fTr.replace(R.id.container, newFrag);
        fTr.addToBackStack("Mapfrag");
        fTr.commit();
    }

    @Override
    public void onFragmentInteraction() {
        FragmentManager fMgr = getFragmentManager();
        fMgr.popBackStack();
       // MainFragment fragment1 = (MainFragment) getFragmentManager().findFragmentByTag("Mapfrag");
    }

    @Override
    public void onFragmentInteraction(ArrayList<String[]> LijstSwitches) {
        showFragmentMap(LijstSwitches);
    }
}
