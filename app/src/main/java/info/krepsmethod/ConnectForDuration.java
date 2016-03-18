package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by Paweł on 2016-03-18.
 */
public class ConnectForDuration implements Runnable {

    /*
    Inicjalizacja zminenych w Klasie ConnectForDuration
    Klasa Odpowiedzialna za pobranie dlugosci nagran, przed odtworzniem****************
     */
    final private String uE;
    final private String uP;
    private int durEn;
    private int durPol;
    MediaPlayer mediaP = new MediaPlayer();
    MediaPlayer mediaE = new MediaPlayer();


    //Utworznie konstruktora Klasy ConnectForDuration************************************
    public ConnectForDuration(String uE, String uP, MediaPlayer mediaP, MediaPlayer mediaE) {
        this.uE = uE;
        this.uP = uP;
        this.mediaE = mediaE;
        this.mediaP = mediaP;
    }

    /*
    Metoda run() interfejsu Runnable (synchronized) jako pierwszy z tzrech watkow korzysta ze zminnych w metodzie
    MainActivity aby pobrac dlugosc nagran**************************
    Wywoluje metody beforePlayP, beforePlayE
     */
    synchronized public void run() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient con = null;
                    MediaPlayer e = new MediaPlayer();
                    MediaPlayer p = new MediaPlayer();

                    con = new FTPClient();
                    con.connect("95.211.80.5");

                    if (con.login("xxx@krebsmethod.cba.pl", "dupa.8")) {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        try {

                            beforePlayE(e, uE);
                            beforePlayP(p, uP);


                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }


                        con.logout();
                        con.disconnect();

                    }


                } catch (Exception e) {
                    Log.v("download result", "failed");
                    e.printStackTrace();
                }


            }
        }).start();


    }

   public void runInLearn() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient con = null;
                    MediaPlayer e = new MediaPlayer();
                    MediaPlayer p = new MediaPlayer();

                    con = new FTPClient();
                    con.connect("95.211.80.5");

                    if (con.login("xxx@krebsmethod.cba.pl", "dupa.8")) {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        try {

                            beforePlayE(e, uE);
                            beforePlayP(p, uP);


                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }


                        con.logout();
                        con.disconnect();

                    }


                } catch (Exception e) {
                    Log.v("download result", "failed");
                    e.printStackTrace();
                }


            }
        }).start();


    }


    //Metoda wzor na dlugosc nagran dla ProgressDialog'a zwraca Maxa progressDialoga
    public int AudioLenght(int durEn, int durPol) {

        int x = 0;

        x = ((((durPol) * 2) + (durEn) * 3) / 100);

        return x;

    }

    public int AudioLenghtForLearn(int durEn, int durPol){

        int x = 0;

        x = ((((durPol) * 2) + (durEn) * 4));

        return x;

    }


    /*
    Metoda beforePlay zwraca dlugosc Polskiego slowa
     */
    public int beforePlayP(MediaPlayer mediaP, String uP) throws IOException {


        mediaP.setDataSource(uP);
        mediaP.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaP.prepare();
        durPol = mediaP.getDuration();
        return durPol;

    }


    /*
    Metoda beforePlay zwraca dlugosc Angielskiego slowa
     */
    public int beforePlayE(MediaPlayer mediaE, String uE) throws IOException {


        mediaE.setDataSource(uE);
        mediaE.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaE.prepare();
        durEn = mediaE.getDuration();
        return durEn;

    }



    /*
    Metoda getFullAudioLenght zwraca calkowita dlugosc nagran
     */
    public int getFullAudioLenght() throws IOException {

        return AudioLenght(beforePlayE(mediaE, uE), beforePlayP(mediaP, uP));


    }

    public int getFullAudioToLearn() throws IOException {


        return AudioLenghtForLearn(beforePlayE(mediaE, uE), beforePlayP(mediaP, uP));

    }


}