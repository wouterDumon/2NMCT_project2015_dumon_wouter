package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SportcentraLoader(getActivity());
    }

    private List<String[]> lijstje = new ArrayList<String[]>();


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        madapter.swapCursor(cursor);
        progress.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        madapter.swapCursor(null);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(List<String[]> param1) {
        MainFragment fragment = new MainFragment();
        //  ARG_PARAM1 = param1;
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
            if (sw[1].equals("true")) {

                editor.putString("Switch" + i, sw[0]);
                i++;
            }
        }
        editor.putInt("Aantal", i);


        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // if(Switches.size()==0){
        //Switches.clear();
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int aantal = prefs.getInt("Aantal", 0);
        if (aantal == 0) {
            for (int ii = 0; ii < aantal; ii++) {
                String sw = prefs.getString("Switch" + ii, "");
                String[] a = new String[2];
                a[0] = sw;
                a[1] = "true";
                int gggg = 0;
                for (String[] t : Switches) {
                    if (t[0].equals(a[0])) {
                        gggg = 1;
                        t[1] = "true";
                    }
                }
                if (gggg == 0) {
                    Switches.add(a);
                }

            }
        }
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

        // getLoaderManager().initLoader(0, null, this);

      /*  ARG_PARAM1 = ((MainActivity) getActivity()).getLijstje();
        ArrayList<String> Sporten = new ArrayList<String>();
        //   Sporten.add("test");
        int teller = 0;
        for (String[] arr : ARG_PARAM1) {
            if (teller == 0) {
                Sporten.add(arr[4]);
            }
            teller++;
            if (Sporten.size() != 0) {
                int gelijk = 0;
                for (String a : Sporten) {
                    //Vul Lijst in
                    String[] PerSwitche = new String[2];
                    PerSwitche[0] = a;
                    PerSwitche[1] = "false";
                    int gg = 0;
                    if (Switches.size() != 0) {
                        for (String[] sw : Switches) {
                            if (sw[0].equals(a)) {
                                gg = 1;
                            }
                        }
                        if (gg == 0) {
                            //zit nog niet in array
                            Switches.add(PerSwitche);
                        }
                    } else {
                        Switches.add(PerSwitche);
                    }
                    if ((a.toLowerCase().toString()).contains(arr[4].toLowerCase().toString())) {
                        gelijk = 1;
                    } else {

                    }
                }
                if (gelijk == 0) {
                    Sporten.add(arr[4]);
                }
            }


        }*/
        // Sporten.add("hallo");
      /*  Collections.sort(Sporten, new Comparator() {

            public int compare(Object o1, Object o2) {


                String a = o1.toString().toLowerCase();
                String b = o2.toString().toLowerCase();
                return a.compareToIgnoreCase(b);
            }
        });
*/

        //   MainAdapter adapter = new MainAdapter(getActivity(), 0, Sporten);
        //    ListView listView = (ListView) v.findViewById(R.id.MijnMainLijst);
        //      listView.setAdapter(adapter);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        madapter = new MainAdapter(getActivity(), R.layout.row_main, null, new String[]{Contract.COLUMN_SPORTCENTRA_SPORT}, new int[]{R.id.txtSoortSp}, 0);
        setListAdapter(madapter);
        getLoaderManager().initLoader(0, null, this);
        progress = ProgressDialog.show(getActivity(), "Even geduld",
                "Bezig met het ophalen van data", true);

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

    private ArrayList<String[]> Switches = new ArrayList<String[]>();
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
        // TODO: Update argument type and name
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
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                convertView = LayoutInflater.from(con).inflate(R.layout.row_main,parent,false);
            }
            // Lookup view for data population
            //   TextView tvName = (TextView) convertView.findViewById(R.id.txtBenaming);

// String[] array = (String[]) getItem(position);
            getCursor().moveToPosition(position);
            String[] marray = new String[8];
            String sport = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_SPORT));
            marray[4] = sport;
            String afmetingen = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_AFMETINGEN));
            marray[5] = afmetingen;
            String adres = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_ADRES));
            marray[1] = adres;
            String benaming = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_BENAMING));
            marray[0] = benaming;
            String gemeente = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_GEMEENTE));
            marray[2] = gemeente;
            String soort = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_SOORT));
            marray[3] = soort;
            String x = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_X));
            marray[7] = x;
            String y = getCursor().getString(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_Y));
            marray[6] = y;
            int aa = 0;
            if(lijstje.size() != 0) {
                for (String[] a : lijstje) {
                    if (a[4].equals(sport)) {
                        aa = 1;
                    }
                }
            }
            if(lijstje.contains(marray)){}else {
                lijstje.add(marray);
            }
            if (aa == 1) {
//wordt al getoont

               return convertView;
               // cursor.moveToNext();
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder.SoortSpport.setText("" + marray[4]);
            holder.Switch.setText("" + marray[4]);
            holder.Switch.setOnCheckedChangeListener(MainFragment.this);
            if (Switches.size() != 0) {
                for (String[] Switchh : Switches) {
                    if (Switchh[1].toLowerCase().equals("true")) {
                        if (marray[4].equals(Switchh[0])) {
                            holder.Switch.setChecked(true);
                        }
                    } else {
                        if (marray[4].equals(Switchh[0])) {
                            holder.Switch.setChecked(false);
                        }
                    }

                }


            }
            return convertView;
           // return super.getView(position, convertView, parent);

        }




      /*  @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            String[] marray = new String[8];
            String sport = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SPORT));
            marray[4] = sport;
            String afmetingen = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_AFMETINGEN));
            marray[5] = afmetingen;
            String adres = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_ADRES));
            marray[1] = adres;
            String benaming = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_BENAMING));
            marray[0] = benaming;
            String gemeente = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_GEMEENTE));
            marray[2] = gemeente;
            String soort = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_SOORT));
            marray[3] = soort;
            String x = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_X));
            marray[7] = x;
            String y = cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SPORTCENTRA_Y));
            marray[6] = y;
//if(!lijstje.contains(sport)) {

//}
            int aa = 0;
            if(lijstje.size() != 0) {
                for (String[] a : lijstje) {
                    if (a[4].equals(getCursor().getColumnIndex(Contract.COLUMN_SPORTCENTRA_SPORT))) {
                        aa = 1;
                    }
                }
            }
            if(lijstje.contains(marray)){}else {
                lijstje.add(marray);
            }
            if (aa == 1) {


                cursor.moveToNext();
            } else {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder == null) {
                    holder = new ViewHolder(view);
                    view.setTag(holder);
                }
                holder.SoortSpport.setText(sport);
                holder.Switch.setText(sport);
                holder.Switch.setOnCheckedChangeListener(MainFragment.this);
                if (Switches.size() != 0 || Switches.contains(sport)) {
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
                } else {
                    //eerste keer alle toevoegen aan array
                    String[] mijnswitch = new String[2];
                    mijnswitch[0] = sport;
                    mijnswitch[1] = "false";
                    Switches.add(mijnswitch);
                    holder.Switch.setChecked(false);

                }
            }

        }*/
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
