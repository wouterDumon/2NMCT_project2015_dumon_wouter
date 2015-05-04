package nmct.howest.be.desproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static List<String[]> ARG_PARAM2 = new ArrayList<String[]>();
    private static ArrayList<String[]> InAdapter = new ArrayList<String[]>();
    private static List<String[]> filter = new ArrayList<String[]>();
    private static String ARG_PARAM1 = "param1";
    private String mParam1;
    private String mParam2;
private static int aantal = 0;
    private OnFragmentInteractionListener mListener;

    public static DetailFragment newInstance(String param1, List<String[]> param2, List<String[]> param3) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        ARG_PARAM1 = param1;
        ARG_PARAM2 = param2;
        filter = param3;
        aantal = filter.size();
        //args.putStringArrayList(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onStop() {

        //TODO: filter, arg_param1, arg_param2 opslaan in sharedpreferences

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("SportNaam", ARG_PARAM1);
        int i = 0;
        for (String[] sw : filter) {
            if (sw[1].equals("true")) {

                editor.putString("Switch" + i, sw[0]);
                i++;
            }
        }
        editor.putInt("Aantalswitch", i);


        editor.commit();
        super.onStop();
    }

    @Override
    public void onResume() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

       // if (filter.size() == 0) {

            //filter.clear();
        filter = new ArrayList<>();
            //int aantal = prefs.getInt("Aantalswitch", 0);
            for (int ii = 0; ii < aantal; ii++) {
                String sw = prefs.getString("Switch" + ii, "");
                if(!sw.equals("")) {
                    String[] a = new String[2];
                    a[0] = sw;
                    a[1] = "true";
                    filter.add(a);
                }
            }
       // }
        if (ARG_PARAM1.equals("param1") || ARG_PARAM1.equals("")) {
            ARG_PARAM1 = prefs.getString("SportNaam", "");
        }
      //  if (ARG_PARAM2.size() == 0) {
            ARG_PARAM2 = new ArrayList<>();
            ARG_PARAM2 = ((MainActivity) getActivity()).getLijstje(); //wordt al bijgehouden in activity dus moet niet nog eens hier bijgehouden worden
        //}

        super.onResume();
    }

    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  madapter = new DetailAdapter(getActivity(),R.layout.row_detail,null,new String[]{Contract.COLUMN_SPORTCENTRA_BENAMING},new int[]{R.id.txtBenaming},0);
        //  setListAdapter(madapter);
        // Cursor c = new Cursor();


        //  getLoaderManager().initLoader(0,null,this);
    }


    public class DetailsAdapter extends ArrayAdapter<String[]> {
        public DetailsAdapter(Context context, int resource, ArrayList<String[]> arr) {
            super(context, resource, arr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            //  User user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_detail, parent, false);
            }
            // Lookup view for data population
            //   TextView tvName = (TextView) convertView.findViewById(R.id.txtBenaming);
            int teller = 0;
            String text = "";

            String[] array = getItem(position);
            if (array[0] != "") {
                ViewHolder holder = (ViewHolder) convertView.getTag();
                if (holder == null) {
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                }
                for (String[] str : inadap) {
                    if (array[4].equals(str[0])) {

                        text = array[4] + " (Aantal: " + str[1] + ")";
                    }
                }

                holder.afmetingen.setText("" + array[5]);
                holder.Adres.setText("" + array[1]);
                holder.Gemeente.setText("" + array[2]);
                holder.Soort.setText("" + array[3]);
                holder.Sport.setText("" + text);


            }


            return convertView;

        }
    }

    private ArrayList<String[]> inadap = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_detail, container, false);
      /*  Button goback = (Button) v.findViewById(R.id.btnTerug);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });*/

        int i = 0;
        TextView Sportcentra = (TextView) v.findViewById(R.id.txtSoortSport);
        Sportcentra.setText("" + ARG_PARAM1);
        String[] a = new String[1];
        a[0] = "";
        ArrayList<String[]> b = new ArrayList<String[]>();
        b.add(a);

        String[] counting = new String[2];


        //adapter.add("");
        countequals();
        DetailsAdapter adapter = new DetailsAdapter(getActivity(), 0, InAdapter);
        //for(String[]troll)
// Attach the adapter to a ListView
        ListView lv = (ListView) v.findViewById(R.id.mijnlijst);
        lv.setAdapter(adapter);
        return v;
    }

    private void countequals() {
        int i = 0;
        for (String[] arr : ARG_PARAM2) {
            String test1 = arr[0].toString().toLowerCase();
            String test2 = ARG_PARAM1.toString().toLowerCase();
            if (test1.equals(test2)) {
                // holder.benaming.setText("" + arr[0]);
                for (String[] sw : filter) {
                    if (arr[4].equals(sw[0]) && sw[1].equals("true")) {
                        if (i == 0) {
                            //adapter.add(arr);
                            InAdapter = new ArrayList<>();
                            inadap = new ArrayList<>();
                            InAdapter.add(arr);
                            String[] te = new String[2];
                            te[0] = arr[4];
                            te[1] = "1";
                            inadap.add(te);
                            i = 5;

                        } else {
                            int gelijk = 0;
                            String zelfde = "";
                            for (String[] aa : InAdapter) {

                                if (aa[4].equals(arr[4])) {
                                    gelijk = 1;
                                    zelfde = aa[4];
                                    //zit al in de lijst

                                }
                            }
                            if (gelijk == 1) {
                                for (String[] bb : inadap) {
                                    if (bb[0].equals(zelfde)) {
                                        int tell = Integer.parseInt(bb[1]);
                                        tell++;
                                        bb[1] = "" + tell;
                                    }
                                }
                            } else {

                                InAdapter.add(arr);
                                String[] te = new String[2];
                                te[0] = arr[4];
                                te[1] = "1";
                                inadap.add(te);
                            }

                        }


                    }
                }

            }
        }


    }

    class ViewHolder {
        //  public ImageView imgicon = null;
        public TextView afmetingen = null;
        public TextView Sport = null;
        public TextView Soort = null;
        public TextView Adres = null;
        public TextView Gemeente = null;

        public ViewHolder(View row) {
            //this.benaming=(TextView)row.findViewById(R.id.txtSport);
            this.Sport = (TextView) row.findViewById(R.id.txtSport);
            this.Soort = (TextView) row.findViewById(R.id.txtSoort);
            this.Adres = (TextView) row.findViewById(R.id.txtAdres);
            this.afmetingen = (TextView) row.findViewById(R.id.txtAfmetingen);
            this.Gemeente = (TextView) row.findViewById(R.id.txtGemeente);

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }
}
