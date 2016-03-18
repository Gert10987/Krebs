package info.krepsmethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paweł on 2016-02-16.
 */
public class CreatePolishActivity extends Activity {

    /*

    Klasa odpowiedzialna za dodawanie nowych slow do programu

    Tworznie zmiennych

     */


    Track track = new Track();
    EditText polishWordText;
    TextView zaladowanoNagranie;
    Button Next;
    String sep = File.separator; // separaottr "/ "/"
    String newFolder = "KrebsFolderSound";      // nazawa folderu do zapisu audio
    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    String polishWord;
    File myNewFolder = new File(extStorageDirectory + sep + newFolder);
    String urlID = "http://krebsmethod.cba.pl/getID.php";
    int autoIncNextVal; // nastepna wartosc id

    //JSON ************
    public static final String LAST_ID = "a";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_polish);

        ReadDataForID();

        myNewFolder.mkdir();
        track.OutputFile = extStorageDirectory
                + sep + newFolder + sep + "Sound.3gpp";
        polishWordText = (EditText) findViewById(R.id.textPolishWord);
        Next = (Button) findViewById(R.id.buttonNext);


        //zaladowanie Nagrania komunikat gdy nagranie jest gotowe


        zaladowanoNagranie = (TextView) findViewById(R.id.zaladowanoNagranie);
        zaladowanoNagranie.setText(" ");


        // Button Next update bazy danych


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pobranie wartosci z pol Edittext angielskiego i polskiego slowa

                polishWord = polishWordText.getText().toString();

                // Warunek czy pole polskie slowo nie jest puste

                if (polishWord.isEmpty()) {
                    Toast.makeText(CreatePolishActivity.this,
                            "Wpisz POLSKIE słowo", Toast.LENGTH_SHORT).show();

                } else if (zaladowanoNagranie.getText() == " ") {
                    Toast.makeText(CreatePolishActivity.this,
                            "Nagraj Tłumaczenie", Toast.LENGTH_SHORT).show();

                } else {


                    File from = new File(myNewFolder, "Sound.3gpp");
                    File to = new File(myNewFolder, autoIncNextVal + "polish" + "Sound.3gpp");
                    if (from.exists())
                        from.renameTo(to);


                    Intent intent = new Intent(getApplicationContext(), CreateEnglishActivity.class);
                    intent.putExtra("polishWord", polishWord);
                    intent.putExtra("idd", autoIncNextVal);
                    startActivity(intent);
                }
            }


        });


    }


    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.recordButton:
                try {
                    track.beginRecording();
                    Toast.makeText(this, "Nagrywanie", Toast.LENGTH_SHORT).show();
                    zaladowanoNagranie.setText(" ");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stopButton:
                try {
                    track.stopRecording();
                    Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                    zaladowanoNagranie.setText("zaladowano nagranie");



                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.playButton:
                try {
                    track.playRecording();
                    Toast.makeText(this, "Odtwarzanie", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }


    }


    public int ReadDataForID() {



        JsonObjectRequest jreqq = new JsonObjectRequest(Request.Method.GET, urlID,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String name = response.getString(LAST_ID);
                            autoIncNextVal = Integer.parseInt(name); // Pobranie nastenpej wartosci id


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
        Singleton.getInstance().addToReqQueue(jreqq);

        return autoIncNextVal;

    }

}
















