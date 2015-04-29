package nmct.howest.be.desproject;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nmct.howest.be.desproject.loader.Contract;
import nmct.howest.be.desproject.loader.SportcentraLoader;


public class MainFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static List<String[]> ARG_PARAM1 = new ArrayList<String[]>();
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(List<String[]> param1) {
        MainFragment fragment = new MainFragment();
   ARG_PARAM1 = param1;
        return fragment;

    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v =  inflater.inflate(R.layout.fragment_main, container, false);
Button ChangeFrag = (Button)v.findViewById(R.id.btnChangeToFragmentMap);

        ChangeFrag.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mListener.onFragmentInteraction(Switches);
    }
});
ArrayList<String> Sporten = new ArrayList<String>();
     //   Sporten.add("test");
        int teller = 0;
        for(String[] arr : ARG_PARAM1){
            if(teller==0){
                Sporten.add(arr[4]);
            }
            teller++;
            if(Sporten.size()!=0){
            int gelijk = 0;
                for(String a : Sporten) {

                if (a.toLowerCase().toString().contains(arr[4].toLowerCase())) {
                    gelijk = 1;
                } else {

                }
            }
                if(gelijk == 0){
                    Sporten.add(arr[4]);
                }
            }


}
        Collections.sort(Sporten);
        MainAdapter adapter = new MainAdapter(getActivity(),0,Sporten);
        ListView listView = (ListView) v.findViewById(R.id.MijnMainLijst);
        listView.setAdapter(adapter);

    return v;
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
        for(String[] a : Switches){

            if((a[0].toString().toLowerCase()).equals((naam.toString().toLowerCase()))){gelijk = 1;}
        }
        if(gelijk==0){
            //indien het er niet inzit
            PerSwitche[0] = naam;
            PerSwitche[1] = ""+ isChecked;
            Switches.add(PerSwitche);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(ArrayList<String[]> LijstSwitches);
    }



    ///
    public class MainAdapter extends ArrayAdapter<String> {
        public MainAdapter(Context context, int resource, ArrayList<String> arr) {
            super(context, resource,arr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            //  User user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_main, parent, false);
            }
            // Lookup view for data population
            //   TextView tvName = (TextView) convertView.findViewById(R.id.txtBenaming);
            ViewHolder holder = (ViewHolder)convertView.getTag();
            if(holder == null){
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }


            String array = getItem(position);
            if(array !="") {
              /*  holder.afmetingen.setText("" + array[5]);
                holder.Adres.setText("" + array[1]);
                holder.Gemeente.setText("" + array[2]);
                holder.Soort.setText("" + array[3]);
                holder.Sport.setText("" + array[4]);*/
                holder.SoortSpport.setText(""+array);
holder.Switch.setText(""+array);
                holder.Switch.setOnCheckedChangeListener(MainFragment.this);
            }
            return convertView;
        }
    }

    class ViewHolder{
        //  public ImageView imgicon = null;
        public TextView SoortSpport = null;
        public Switch Switch = null;
        public ViewHolder(View row){
            //this.benaming=(TextView)row.findViewById(R.id.txtSport);
            this.SoortSpport = (TextView)row.findViewById(R.id.txtSoortSp);
            this.Switch = (Switch)row.findViewById(R.id.switch1);


        }

    }



}
