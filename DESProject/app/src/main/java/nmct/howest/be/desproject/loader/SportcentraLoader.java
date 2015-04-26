package nmct.howest.be.desproject.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wouter on 26/04/2015.
 */
public class SportcentraLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;

    public List<String[]> getLijst() {
        return Lijst;
    }

    public SportcentraLoader(Context context) {

        super(context);
    }
public List<String[]> Lijst = new ArrayList<String[]>();
    @Override
    protected void onStartLoading() {
        if(mCursor !=null){
            deliverResult(mCursor);
        }
        if(takeContentChanged()||mCursor==null){
            forceLoad();
        }
        super.onStartLoading();
    }

    private final String[] mColumnNames = new String[]{
            BaseColumns._ID, Contract.COLUMN_SPORTCENTRA_BENAMING, Contract.COLUMN_SPORTCENTRA_ADRES, Contract.COLUMN_SPORTCENTRA_GEMEENTE, Contract.COLUMN_SPORTCENTRA_SOORT, Contract.COLUMN_SPORTCENTRA_SPORT,Contract.COLUMN_SPORTCENTRA_AFMETINGEN,Contract.COLUMN_SPORTCENTRA_X,Contract.COLUMN_SPORTCENTRA_Y
    };
    private static Object lock = new Object();

    @Override
    public Cursor loadInBackground() {
        if (mCursor == null) loadCursor();
        return mCursor;
    }

    String url = "http://data.kortrijk.be/sport/outdoorlocaties.json";

    private void loadCursor() {
        synchronized (lock) {
            if (mCursor != null) return;
            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream input = null;
            JsonReader reader = null;
            try {
                input = new URL(url).openStream();
                reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
                int id = 1;
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    String benaming = "";
                    String Adres = "";
                    String Gemeente = "";
                    String Soort = "";
                    String Sport = "";
                    String Afmeting = "";
                    double X = 0;
                    double Y = 0;
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("benaming")) {
                          benaming =  reader.nextString();
                        }  else if (name.equals("adres")) {
                       Adres =  reader.nextString();
                    }else if (name.equals("gemeente")) {
                           Gemeente = reader.nextString();
                        }else if (name.equals("soort")) {
                       Soort =     reader.nextString();
                        }else if (name.equals("sport")) {
                           Sport =  reader.nextString();
                        }else if (name.equals("afmetingen")) {
                           Afmeting =  reader.nextString();
                        }else if (name.equals("y")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                              Y =  reader.nextDouble();
                            }
                        }else if (name.equals("x")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                               X =  reader.nextDouble();
                            }
                        }else{
                            reader.skipValue();
                        }

                    }
                    MatrixCursor.RowBuilder row = cursor.newRow();
                    //String[] benamingarray = new String[cursor.getCount()];
                    row.add(id);
                     String[] arr = new String[8];
                    row.add(benaming);
                    arr[0] = benaming;
                    row.add(Adres);
                    arr[1] = Adres;
                    row.add(Gemeente);
                    arr[2] = Gemeente;
                    row.add(Soort);
                    arr[3] = Soort;
                    row.add(Sport);
                    arr[4] = Sport;
                    row.add(Afmeting);
                    arr[5] = Afmeting;
                    row.add(Y);
                    arr[6] = "" + Y;
                    row.add(X);
                    arr[7] = "" + X;
                    id++;

                    Lijst.add(arr);
                    reader.endObject();
                }
                reader.endArray();
                mCursor = cursor;
            } catch (IOException ex) {
                ex.printStackTrace();

            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
                try {
                    input.close();
                } catch (IOException ex) {
                }
            }

        }

    }


}
