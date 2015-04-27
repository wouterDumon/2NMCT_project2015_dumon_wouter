package nmct.howest.be.desproject;

import android.app.Activity;
import android.content.Context;
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
    private static String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, List<String[]> param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        ARG_PARAM1 = param1;
        ARG_PARAM2 = param2;
        //args.putStringArrayList(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
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
              super(context, resource,arr);
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
              ViewHolder holder = (ViewHolder)convertView.getTag();
              if(holder == null){
                  holder = new ViewHolder(convertView);
                  convertView.setTag(holder);
              }


             String[]array = getItem(position);
              if(array[0] !="") {
                  holder.afmetingen.setText("" + array[5]);
                  holder.Adres.setText("" + array[1]);
                  holder.Gemeente.setText("" + array[2]);
                  holder.Soort.setText("" + array[3]);
                  holder.Sport.setText("" + array[4]);
              }
              return convertView;
          }
      }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_detail, container, false);
Button goback = (Button)v.findViewById(R.id.btnTerug);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });

        TextView Sportcentra = (TextView)v.findViewById(R.id.txtSoortSport);
        Sportcentra.setText("" + ARG_PARAM1);
        String[] a = new String[1];
        a[0] = "";
        ArrayList<String[]> b = new ArrayList<String[]>();
        b.add(a);
        DetailsAdapter adapter = new DetailsAdapter(getActivity(),0,b);
        //adapter.add("");
        for(String[] arr : ARG_PARAM2) {
            String test1 = arr[0].toString().toLowerCase();
            String test2 = ARG_PARAM1.toString().toLowerCase();
            if (test1.equals(test2)) {
               // holder.benaming.setText("" + arr[0]);
                adapter.add(arr);

            }
        }
// Attach the adapter to a ListView
        ListView listView = (ListView) v.findViewById(R.id.mijnlijst);
        listView.setAdapter(adapter);
return v;
    }

    class ViewHolder{
      //  public ImageView imgicon = null;
        public TextView afmetingen = null;
     public TextView Sport = null;
        public TextView Soort = null;
        public TextView Adres = null;
        public TextView Gemeente = null;
        public ViewHolder(View row){
            //this.benaming=(TextView)row.findViewById(R.id.txtSport);
this.Sport = (TextView)row.findViewById(R.id.txtSport);
        this.Soort = (TextView)row.findViewById(R.id.txtSoort);
            this.Adres = (TextView)row.findViewById(R.id.txtAdres);
            this.afmetingen = (TextView)row.findViewById(R.id.txtAfmetingen);
            this.Gemeente = (TextView)row.findViewById(R.id.txtGemeente);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}
