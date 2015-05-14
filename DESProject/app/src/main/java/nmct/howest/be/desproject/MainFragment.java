package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nmct.howest.be.desproject.loader.Contract;
import nmct.howest.be.desproject.loader.SportcentraLoader;


public class MainFragment extends ListFragment implements CompoundButton.OnCheckedChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static List<String[]> ARG_PARAM1 = new ArrayList<String[]>();
    private static final String ARG_PARAM2 = "param2";
    private static final String PREFS_NAME = "MyPrefsFile";
    private MainAdapter madapter;
    ProgressDialog progress;
    private Cursor mCursor;
    private boolean resumeHasRun;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SportcentraLoader(getActivity());
    }

    private List<String[]> lijstje = new ArrayList<>();
    private List<String[]> inadap = new ArrayList<>();


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        madapter.swapCursor(filterCursor(cursor));
        progress.dismiss();
    }

    private Cursor filterCursor(Cursor cursor) {

        String[] mColumnNames = new String[]{
                BaseColumns._ID, Contract.COLUMN_SPORTCENTRA_BENAMING, Contract.COLUMN_SPORTCENTRA_ADRES, Contract.COLUMN_SPORTCENTRA_GEMEENTE, Contract.COLUMN_SPORTCENTRA_SOORT, Contract.COLUMN_SPORTCENTRA_SPORT, Contract.COLUMN_SPORTCENTRA_AFMETINGEN, Contract.COLUMN_SPORTCENTRA_X, Contract.COLUMN_SPORTCENTRA_Y
        };
        MatrixCursor newCursor = new MatrixCursor(mColumnNames);
        int id = 1;
        if (cursor.moveToFirst()) {
            do {
                String[] marray = new String[8];
                String benaming = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_BENAMING));
                marray[0] = benaming;
                String adres = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_ADRES));
                marray[1] = adres;
                String gemeente = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_GEMEENTE));
                marray[2] = gemeente;
                String soort = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SOORT));
                marray[3] = soort;
                String sport = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SPORT));
                marray[4] = sport;
                String afmetingen = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_AFMETINGEN));
                marray[5] = afmetingen;
                String y = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_Y));
                marray[7] = y;
                String x = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_X));
                marray[6] = x;

                int gelijk = 0;
                if (lijstje.size() != 0) {
                    for (String[] a : lijstje) {
                        if (a[4].toLowerCase().equals(sport.toLowerCase())) {
                            gelijk = 1;
                        }
                    }
                }
                if (!lijstje.contains(marray)) {
                    lijstje.add(marray);
                }
                if (gelijk == 0) { // indien het er nog niet inzit zet het in de nieuwe cursor
                    MatrixCursor.RowBuilder row = newCursor.newRow();
                    row.add(id++);
                    row.add(benaming);
                    row.add(adres);
                    row.add(gemeente);
                    row.add(soort);
                    row.add(sport);
                    row.add(afmetingen);
                    row.add(x);
                    row.add(y);

                }

            } while (cursor.moveToNext());
        }
        return newCursor;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        madapter.swapCursor(null);
    }


    private OnFragmentInteractionListener mListener;


    public static MainFragment newInstance(List<String[]> param1) {
        MainFragment fragment = new MainFragment();
        return fragment;

    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        int i = 0;
        for (String[] sw : Switches) {

            editor.putString("Switch" + i, sw[0]);

            if (sw[1].equals("true")) {
                editor.putString("bool" + i, "true");

            } else {
                editor.putString("bool" + i, "false");
            }
            i++;
        }
        editor.putInt("Aantal", i);

        int si = 0;
        for (String[] listt : lijstje) {
            String str = "";
            for (String s : listt) {
                str += s + ";";
            }
            editor.putString("lijst" + si, str.toString());
            si++;
        }
        editor.putInt("Aantallijst", si);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int aantasl = prefs.getInt("Aantallijst", 0);

        int aantal = prefs.getInt("Aantal", 0);
        if (aantal != 0) {
            for (int ii = 0; ii < aantal; ii++) {
                String sw = prefs.getString("Switch" + ii, "");
                String[] a = new String[2];
                a[0] = sw;
                if (prefs.getString("bool" + ii, "").equals("true")) {
                    a[1] = "true";

                } else {
                    a[1] = "false";
                }

                int gggg = 0;
                //controle of het er al inzit
                for (String[] t : Switches) {
                    if (t[0].equals(a[0])) {
                        gggg = 1;
                        // t[1] = "true";
                    }
                }
                if (gggg == 0) {
                    if (a[0] != "") {
                        Switches.add(a);
                    }
                }

            }
        }


        //   lijstje = new ArrayList<>();
        //      if (lijstje.size() != 0) {
        lijstje = new ArrayList<>();

        for (int ii = 0; ii < aantasl; ii++) {
            String str = prefs.getString("lijst" + ii, "");
            List<String> arraylist = new ArrayList<>(Arrays.asList(str.split(";")));
            int id = 0;
            String[] marray = new String[8];
            for (String st : arraylist) {
                marray[id] = st;
                id++;
            }
            if (!lijstje.contains(marray)) {
                lijstje.add(marray);
            }

        }
        //}

        inadap = new ArrayList<>(); // zeker maken dat hij leeg is

        madapter.swapCursor(reCreateCursor(lijstje));

    }

    private Cursor reCreateCursor(List<String[]> lSwitch) {

        String[] mColumnNames = new String[]{
                BaseColumns._ID, Contract.COLUMN_SPORTCENTRA_BENAMING, Contract.COLUMN_SPORTCENTRA_ADRES, Contract.COLUMN_SPORTCENTRA_GEMEENTE, Contract.COLUMN_SPORTCENTRA_SOORT, Contract.COLUMN_SPORTCENTRA_SPORT, Contract.COLUMN_SPORTCENTRA_AFMETINGEN, Contract.COLUMN_SPORTCENTRA_X, Contract.COLUMN_SPORTCENTRA_Y
        };
        MatrixCursor newCursor = new MatrixCursor(mColumnNames);
        int id = 1;

        for (String[] y : lSwitch) {

            int gelijk = 0;
            if (inadap.size() != 0) {
                for (String[] a : inadap) {
                    if (a[4].toLowerCase().equals(y[4].toLowerCase())) {
                        gelijk = 1;
                    }
                }
            }
            inadap.add(y);
            if (gelijk == 0) {


                MatrixCursor.RowBuilder row = newCursor.newRow();
                row.add(id++);
                row.add(y[0]);
                row.add(y[1]);
                row.add(y[2]);
                row.add(y[3]);
                row.add(y[4]);
                row.add(y[5]);
                row.add(y[6]);
                row.add(y[7]);
            }
        }
        return newCursor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Button ChangeFrag = (Button) v.findViewById(R.id.btnChangeToFragmentMap);

        ChangeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Switches, lijstje);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        madapter = new MainAdapter(getActivity(), R.layout.row_main, null, new String[]{Contract.COLUMN_SPORTCENTRA_SPORT}, new int[]{R.id.txtSoortSp}, 0);
        setListAdapter(madapter);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int aantasl = prefs.getInt("Aantallijst", 0);
        if (aantasl == 0) {
            getLoaderManager().initLoader(0, null, this);
            progress = ProgressDialog.show(getActivity(), "Even geduld",
                    "Bezig met het ophalen van data", true);
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

    private ArrayList<String[]> Switches = new ArrayList<>();
    private String[] PerSwitch = new String[2];

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String naam = buttonView.getText().toString();
        //  private ArrayList<String[]> Switches = new ArrayList<String[]>();
        String[] PerSwitche = new String[2];
        int gelijk = 0;
        for (String[] a : Switches) {

            if ((a[0].toString().toLowerCase()).equals((naam.toString().toLowerCase()))) {
                gelijk = 1;

                a[1] = "" + isChecked;
            }
        }
        if (gelijk == 0) {
            //indien het er niet inzit
            PerSwitche[0] = naam;
            PerSwitche[1] = "" + isChecked;
            Switches.add(PerSwitche);
        }
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(ArrayList<String[]> LijstSwitches, List<String[]> lijst);
    }


    ///
    public class MainAdapter extends SimpleCursorAdapter {
        Context con;

        public MainAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            con = context;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            // String[] marray = new String[8];
            String sport = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SPORT));
            // marray[4] = sport;
            String afmetingen = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_AFMETINGEN));
            // marray[5] = afmetingen;
            String adres = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_ADRES));
            // marray[1] = adres;
            String benaming = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_BENAMING));
            // marray[0] = benaming;
            String gemeente = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_GEMEENTE));
            // marray[2] = gemeente;
            String soort = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SOORT));
            // marray[3] = soort;
            String x = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_X));
            // marray[7] = x;
            String y = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_Y));
            // marray[6] = y;


            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder.SoortSpport.setText(sport);
            holder.Switch.setText(sport);
            holder.Switch.setOnCheckedChangeListener(MainFragment.this);

            //eerste keer alle toevoegen aan array
            String[] mijnswitch = new String[2];
            mijnswitch[0] = sport;
            mijnswitch[1] = "false";
            int mnswt = 0;
            for (String[] test : Switches) {
                if (test[0].toLowerCase().equals(sport.toLowerCase())) {
                    mnswt = 1;
                }
            }
            if (mnswt == 0) {

                Switches.add(mijnswitch);
                holder.Switch.setChecked(false);

            }

            if (Switches.size() != 0) {
                for (String[] sw : Switches) {
                    if (sw[1].toLowerCase().equals("true")) {
                        if (sport.equals(sw[0])) {
                            holder.Switch.setChecked(true);
                        }
                    } else {
                        if (sport.equals(sw[0])) {
                            holder.Switch.setChecked(false);
                        }
                    }

                }
            }
        }
    }

    class ViewHolder {
        //  public ImageView imgicon = null;
        public TextView SoortSpport = null;
        public Switch Switch = null;

        public ViewHolder(View row) {
            //this.benaming=(TextView)row.findViewById(R.id.txtSport);
            this.SoortSpport = (TextView) row.findViewById(R.id.txtSoortSp);
            this.Switch = (Switch) row.findViewById(R.id.switch1);


        }

    }


}
