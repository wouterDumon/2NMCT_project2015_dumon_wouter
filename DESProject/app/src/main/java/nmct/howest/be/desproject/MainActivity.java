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


public class MainActivity extends FragmentActivity implements MainFragment.OnFragmentInteractionListener, ShowMapFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {


    public ArrayList<String[]> getEmpty() {
        return empty;
    }

    public List<String[]> getLijstje() {
        return lijstje;
    }

    private boolean resumeHasRun = false;

    private static final String PREFS_NAME = "MyPrefsFile";
    //  private ArrayList Switches;
    private ArrayList<String[]> Switches = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        // fragmentManager = getFragmentManager();


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        lijstje = new ArrayList<>();
        if (savedInstanceState == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Aantallijst", 0);
            editor.commit();
            FragmentManager fragmentManager = getFragmentManager();
            MainFragment fragment1 = new MainFragment();//.newInstance(lijstje);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //parameters:
            //1: ID container
            //2: fragment
            //3: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).
            fragmentTransaction.add(R.id.container, fragment1, "mainfrag").commit(); //VERPLICHT DIT TE ZETTEN ANDERS ZAL HIJ NULL FOUT GEVEN
            // fragmentTransaction.commit();
          /*  new Thread(new Runnable() {
                @Override
                public void run() {

                    // do the thing that takes a long time
                    //   a = new SportcentraLoader(MainActivity.this);
                   // getLoaderManager().initLoader(0, null, MainActivity.this);


                    while (lijstje.size() == 0) {
                        try {
                            if (lijstje.size() == 0) {
                                lijstje = new ArrayList<String[]>();
                                lijstje = a.getLijst();
                                GeefLijstAanDetails = a.getLijst();
                            }
                            int a = 10000;
                            for (int i = 0; i < a; i++) {

                                //waste some time
                            }
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "hallo", Toast.LENGTH_LONG);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progress.dismiss();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            MainFragment fragment1 = new MainFragment().newInstance(lijstje);
                            //parameters:
                            //1: ID container
                            //2: fragment
                            //3: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).
                            fragmentTransaction.add(R.id.container, fragment1, "mainfrag"); //VERPLICHT DIT TE ZETTEN ANDERS ZAL HIJ NULL FOUT GEVEN
                            fragmentTransaction.commit();
                        }
                    });
                }
            }).start();*/
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
        for (String[] listt : lijstje) {
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

        //   Lijstje.clear();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        lijstje = new ArrayList<>();
        if (lijstje.size() == 0) {

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
                if (!lijstje.contains(marray)) {
                    lijstje.add(marray);
                }
                //  Lijstje.clear();
            }
            //  GeefLijstAanDetails = Lijstje;
        }
        super.onResume();
    }

    private List<String[]> lijstje = new ArrayList<String[]>();
    private List<String[]> geefLijstAanDetails = new ArrayList<String[]>();


    private void showFragmentDetails(String title, ArrayList<String[]> Switchlijst) {
       /* requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);*/
        //  setContentView(R.layout.activity_main);
        Fragment newFrag = new DetailFragment().newInstance(title, lijstje, Switchlijst);
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
    public void onFragmentInteraction(ArrayList<String[]> lijstSwitches, List<String[]> lijst) {
        lijstje = lijst;
        empty = lijstSwitches;
        showFragmentMap(lijstSwitches);
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
