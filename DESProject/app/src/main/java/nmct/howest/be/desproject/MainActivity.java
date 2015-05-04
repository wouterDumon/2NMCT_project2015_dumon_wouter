package nmct.howest.be.desproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nmct.howest.be.desproject.loader.SportcentraLoader;


public class MainActivity extends FragmentActivity implements MainFragment.OnFragmentInteractionListener, ShowMapFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor>, DetailFragment.OnFragmentInteractionListener {
    ProgressDialog progress;


    public ArrayList<String[]> getEmpty() {
        return empty;
    }

    public List<String[]> getLijstje() {
        return Lijstje;
    }
    private boolean resumeHasRun = false;

    private static final String PREFS_NAME = "MyPrefsFile";
    //  private ArrayList Switches;
    private ArrayList<String[]> Switches = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        fragmentManager = getFragmentManager();


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Lijstje = new ArrayList<>();
        if (savedInstanceState == null) {
            progress = ProgressDialog.show(this, "Even geduld",
                    "Bezig met het ophalen van data", true);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    // do the thing that takes a long time
                    //   a = new SportcentraLoader(MainActivity.this);
                    getLoaderManager().initLoader(0, null, MainActivity.this);


                    while (Lijstje.size() == 0) {
                        try {
                          if(Lijstje.size() == 0) {
Lijstje = new ArrayList<String[]>();
    Lijstje = a.getLijst();
    GeefLijstAanDetails = a.getLijst();
}
                            int a = 10000;
                            for(int i = 0 ; i<a; i++){

                                //waste some time
                            }
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this,"hallo",Toast.LENGTH_LONG);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
        super.onCreate(savedInstanceState);

    }

/*
*
* */

    public static FragmentManager fragmentManager;
    private ArrayList<String[]> empty = new ArrayList<String[]>();

    private void showFragmentMap(ArrayList<String[]> Lijst) {
        Fragment newFrag = new ShowMapFragment();//.newInstance(empty,Lijstje); Niet anders ropet hij oncreateview niet meer op!
        FragmentManager fMgr = getFragmentManager();
        FragmentTransaction fTr = fMgr.beginTransaction();
        fTr.replace(R.id.container, newFrag);
        fTr.addToBackStack("mainfrag");
        fTr.commit();
    }

    SportcentraLoader a = null;

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        int i = 0;
        for (String[] listt : Lijstje) {
            String str = "";
            for (String s : listt) {
                str += s + ";";
            }
            editor.putString("lijst" + i, str.toString());
            i++;
        }
        editor.putInt("Aantallijst", i);
        editor.commit();
    }

    @Override
    protected void onResume() {
        empty.clear();
        if (!resumeHasRun) {
            super.onResume();
            resumeHasRun = true;
            return;
        }
        //   Lijstje.clear();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Lijstje = new ArrayList<>();
        if (Lijstje.size() == 0) {

            int aantal = prefs.getInt("Aantallijst", 0);
            for (int ii = 0; ii < aantal; ii++) {
                String str = prefs.getString("lijst" + ii, "");
                List<String> arraylist = new ArrayList<String>(Arrays.asList(str.split(";")));
                int id = 0;
                String[] marray = new String[8];
                for (String st : arraylist) {
                    marray[id] = st;
                    id++;
                }
                if (!Lijstje.contains(marray)) {
                    Lijstje.add(marray);
                }
                //  Lijstje.clear();
            }
          //  GeefLijstAanDetails = Lijstje;
        }
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        a = new SportcentraLoader(this);
        return a;
    }

    private List<String[]> Lijstje = new ArrayList<String[]>();
    private List<String[]> GeefLijstAanDetails = new ArrayList<String[]>();

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //   madapter.swapCursor(data);
        progress.dismiss();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //madapter.swapCursor(null);
    }


    private void showFragmentDetails(String title, ArrayList<String[]> Switchlijst) {
       /* requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);*/
        //  setContentView(R.layout.activity_main);
        Fragment newFrag = new DetailFragment().newInstance(title, Lijstje, Switchlijst);
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
    public void onFrtInteraction(String marker, ArrayList<String[]> SwitchLisjt) {
        showFragmentDetails(marker, SwitchLisjt);

    }

    @Override
    public void onFragmentInteraction(ArrayList<String[]> LijstSwitches) {
        empty = LijstSwitches;
        showFragmentMap(LijstSwitches);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) { // zit er iets in backstack?
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed(); //niets in backstack doe wat hij normaal zou doen
        }
    }
}
