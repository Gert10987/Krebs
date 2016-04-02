package info.krepsmethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 2016-02-16.
 */
public class CreateEnglishActivity extends Activity {

    /*
    Inicjalizacja zmiennych Klasa odpowiedzialana za dodawanie ******************
    wprowaznaie Angileskiego slowa i nagrania*************
    oraz za ostateczne dodanie Audio i textu
     */
    Track track = new Track();
    EditText englishWordtext;
    TextView zaladowanoNagranie;
    Button Next;
    String sep = File.separator; // separaottr "/ "/"
    String newFolder = "KrebsFolderSound";      // nazawa folderu do zapisu audio
    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    ProgressDialog PD;
    String url = "url PHP script insert"; // url do skryptu wstawiania nowych rekordow
    String polishWordText, englishWordText;// zmienna ktora ma przchowywac napis
    File myNewFolder = new File(extStorageDirectory + sep + newFolder); // nowy folder


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_english);

        track.OutputFile = Environment.getExternalStorageDirectory().toString() +
                sep + newFolder + sep + "Sound.3gpp";

        PD = new ProgressDialog(this); // inicjalizacja Progress Dialog
        PD.setMessage("Loading....."); // ustawienie napisu
        PD.setCancelable(false); //ustawinie czy PD ma reagowac na przycisk "back"

        englishWordtext = (EditText) findViewById(R.id.textEnglishWord);
        Next = (Button) findViewById(R.id.buttonNext);
        zaladowanoNagranie = (TextView) findViewById(R.id.zaladowanoNagranie);  //  zaladowanie Nagrania komunikat gdy nagranie jest gotowe
        zaladowanoNagranie.setText(" ");
        /*
         Button Next update bazy danych
          */

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                 Pobranie wartosci z pol Edittext angielskiego i polskiego slowa,
                 odebranie wartosci autoIncNextVal z poprzedniej aktywnosci
                 */
                englishWordText = englishWordtext.getText().toString();
                polishWordText = getIntent().getStringExtra("polishWord");
                int autoIncNextVal = getIntent().getIntExtra("idd", 0);


                /*
                 Warunek czy pole angielskie slowo nie jest puste
                  */
                if (englishWordText.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Wpisz ANGIELSKIE słowo", Toast.LENGTH_SHORT).show();
                }else if (zaladowanoNagranie.getText() == " ") {
                    Toast.makeText(getApplicationContext(),
                            "Nagraj Tłumaczenie", Toast.LENGTH_SHORT).show();

                } else {

                    PD.show(); //Wyswietelnie PrgressDialog
                    renameFileAudio(autoIncNextVal);// zmiana nazwy nagranyc nagran + id z bazy danych
                    insertAudio(autoIncNextVal);// insert Audio on FTP
                    insertText();//insertText to Dtatbase


                    /*
                    Powrot do poprzeniej aktywnosci
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            }


        });


    }
    //**************************KONIEC ON CREATE****************************

    /*
    Obsluga przyciskow Audio
     */
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
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
                    zaladowanoNagranie.setText("zaladowano nagranie");
                    //Api 21    track.onDestroy();


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


    /*
    Metoda insertText umiescza texty w bazie danych
     */
    public void insertText() {


        //String Request : wyslanie zmiennej do Skryptu PHP


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override

                    /*

                    Odpowiedz po wykonaniu

                     */
                    public void onResponse(String response) {
                        PD.dismiss();// wylacznie PD
                        Toast.makeText(getApplicationContext(), // Toast
                                "Data Inserted Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                /*

                Wrazie bledu

                 */
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.dismiss();// wylaczneie PD
                        Toast.makeText(getApplicationContext(),
                                "failed to insert", Toast.LENGTH_SHORT).show();
                    }
                })

            /*

            Wykoanie zapytania, dodanie wartosci zminnej item_name do klucza item_name uzywanego w skrypcie php

             */

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PolishWordText", polishWordText);
                params.put("EnglishWordText", englishWordText);

                return params;

            }


        };

        // Dodanie prosby do kolejki
        Singleton.getInstance().addToReqQueue(postRequest);


    }


    /*
    Metoda insert Audio umiescza pliki onFTP i wukonuje metode kasowania z kart sd
     */
    public void insertAudio(final int id) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FTPClient con = null;
                    con = new FTPClient();
                    con.connect("adress FTP");

                    if (con.login("login", "pass")) {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);
                        String pData = "/sdcard/KrebsFolderSound/" + id + "polishSound.3gpp";
                        String eData = "/sdcard/KrebsFolderSound/" + id + "englishSound.3gpp";

                        FileInputStream pIn = new FileInputStream(new File(pData));
                        boolean pResult = con.storeFile("/polishSound/" + id + "polish.3gpp", pIn);

                        FileInputStream eIn = new FileInputStream(new File(eData));
                        boolean eResult = con.storeFile("/englishSound/" + id + "english.3gpp", eIn);
                        pIn.close();
                        eIn.close();
                        if (pResult && eResult) Log.v("upload result", "succeeded");
                        con.logout();
                        con.disconnect();

                        deleteFromSD(id);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    /*
    Kasowanie plikow z karty sd
     */
    public void deleteFromSD(int id) {

        File file = new File(track.SoundInList =
                Environment.getExternalStorageDirectory().toString()
                        + sep + newFolder + sep + id + "polishSound.3gpp");

        file.delete();

        File file2 = new File(track.SoundInList =
                Environment.getExternalStorageDirectory().toString()
                        + sep + newFolder + sep + id + "englishSound.3gpp");

        file2.delete();


    }

    /*
    Zmiana nazwy plikow aby byly kompatybilne z id w bazie
     */

    public void renameFileAudio(int idAudio) {

        File from = new File(myNewFolder, "Sound.3gpp");
        File to = new File(myNewFolder, idAudio + "english" + "Sound.3gpp");
        if (from.exists())
            from.renameTo(to);

    }


}














