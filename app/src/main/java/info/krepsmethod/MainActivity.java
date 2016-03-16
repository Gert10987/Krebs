package info.krepsmethod;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    /*

    Głowna klasa programu KRrebsMethod

    Incjalizowanie zmiennych;


     */


    Track track = new Track();
    ImageView imagePlayButton;
    MediaPlayer mediaP = new MediaPlayer();
    MediaPlayer mediaE = new MediaPlayer();

    String url = "http://krebsmethod.cba.pl/read.php";
    ArrayList<HashMap<String, String>> Item_List;
    ProgressDialog PD;
    ListAdapter adapter;
    ListView list;
    TextView emptyList;
    ProgressBar PB;
    int DurationFullAudio = 0, postLevelProgress = 1;


    // JSON Node names
    public static final String ITEM_ID = "id";
    public static final String POLISH_WORD = "PolishWordText";
    public static final String ENGLISH_WORD = "EnglishWordText";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Item_List = new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.listView1);

        PB = (ProgressBar) findViewById(R.id.progressBar);
        PB.setVisibility(View.GONE);



        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        emptyList = (TextView) findViewById(R.id.emptyList);// gdy lista jest pusta
        emptyList.setText(" ");

        ReadDataFromDB();

        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idTextView = (TextView) view.findViewById(R.id.IdTextView); // pobranbie id z pola text view
                String idT = idTextView.getText().toString();


                String uP = "http://krebsmethod.cba.pl/PolishSound/" + idT + "polish.3gpp";
                String uE = "http://krebsmethod.cba.pl/EnglishSound/" + idT + "english.3gpp";

                if (PB.isShown() ||  postLevelProgress == DurationFullAudio) {

                    Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT).show();

                    try {

                        mediaE.stop();
                        mediaP.stop();

                        mediaP = new MediaPlayer();
                        mediaE = new MediaPlayer();

                        PB.setVisibility(View.GONE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {

                        progressBar();
                        connectToFTP(uP, uE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }






            }
        });

    }

    // tworzenie menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // obsluga przyciskow menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addMenuClick) {

            Intent intent = new Intent(getApplicationContext(), CreatePolishActivity.class);
            startActivity(intent);

        } else {

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    // KONIEC METODY ON CREATE********************************************


    //**************************** Drugi etap ustawiania context menu


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) menuInfo;


        menu.setHeaderTitle("Select The Action");

        menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title


    }


    //*************************  Trzeci ostatni etap ustawiania context menu


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getTitle() == "Delete") //jezeli nacisne pole Delete wykona sie warunek

        {


            String selectedID = ((TextView) adapter.targetView.findViewById
                    (R.id.IdTextView)).getText().toString();// pobranie id z pola txtview

            deleteText(selectedID);//wywolanie metody usun z bazy danych wzgledem ID
            deleteAudio(selectedID);//wywolanie metody usun z serwera ftp wzgledem id

            Intent intent = new Intent(this, MainActivity.class); //odswiezenie

            startActivity(intent);//odswiezeine*/
            ReadDataFromDB();

        } else {


            return false; // gdy warunek nie spelniony zwraca false
        }


        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ViewGroup v = (ViewGroup) view;


    }


    public void ReadDataFromDB() {
        PD.show();
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("orders");


                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jobj = ja.getJSONObject(i);
                                    HashMap<String, String> item = new HashMap<String, String>();
                                    item.put(ITEM_ID, jobj.getString(ITEM_ID));
                                    item.put(POLISH_WORD, jobj.getString(POLISH_WORD));
                                    item.put(ENGLISH_WORD, jobj.getString(ENGLISH_WORD));

                                    Item_List.add(item);


                                } // for loop ends

                                setList();

                            } else {// if ends

                                emptyList.setText("Brak danych w bazie");
                                PD.dismiss();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });

        // Adding request to request queue
        Singleton.getInstance().addToReqQueue(jreq);


    }


    public void deleteText(String id) {
        PD.show();
        String delete_url = "http://krebsmethod.cba.pl/delete.php?id="
                + id;


        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "failed to delete", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        Singleton.getInstance().addToReqQueue(delete_request);

    }

    public void deleteAudio(String id) {

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect("95.211.80.5");

            if (con.login("xxx@krebsmethod.cba.pl", "dupa.8")) {
                con.enterLocalPassiveMode(); // important!
                String pData = "/polishSound/" + id + "polish.3gpp";
                String eData = "/englishSound/" + id + "english.3gpp";
                con.deleteFile(pData);
                con.deleteFile(eData);
                con.logout();
                con.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setList() {

        String[] from = {ITEM_ID,
                POLISH_WORD,
                ENGLISH_WORD};

        int[] to = {R.id.IdTextView,
                R.id.textViewPolishWord,
                R.id.textViewEnglishWord};


        adapter = new SimpleAdapter(getApplicationContext(),
                Item_List, R.layout.text_view_for_list, from, to);


        Comparator<HashMap<String, String>> comparator = new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> object1, HashMap<String, String> object2) {
                return object1.get("EnglishWordText").compareToIgnoreCase(object2.get("EnglishWordText"));
            }
        };
        Collections.sort(Item_List, comparator);

        list.setAdapter(adapter);

        PD.dismiss();


    }


    public FTPClient connectToFTP(String uP, String uE) {

        FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect("95.211.80.5");

            if (con.login("xxx@krebsmethod.cba.pl", "dupa.8")) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);

                try {
                    track.playFromFTP(mediaP, uP, mediaE, uE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                con.logout();
                con.disconnect();


            }


        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();
        }

        return con;


    }



    public void progressBar() {


        class Async extends AsyncTask<Integer, Integer, Void> {




            @Override
            protected Void doInBackground(Integer... params) {

                for (int count = 0; count < DurationFullAudio; count++) {
                    try {
                        Thread.sleep(100);
                        publishProgress(count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }



                return null;

            }

            @Override
            protected void onPreExecute() {


                DurationFullAudio = ((mediaP.getDuration() * 2) + (mediaE.getDuration() * 2));
                PB.setVisibility(View.VISIBLE);

                super.onPreExecute();


            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                PB.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                postLevelProgress = PB.getProgress();





            }

        }

        new Async().execute();
    }


}




























