package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFileFilter;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-03-17.
 */
public class ConnectForAudio implements Runnable {

    /*
     Inicjazlizacji zmiennych do Klasy********************
     Klasa Odpowiedzialna za odegranie audio z FTP
     */
    private MediaPlayer mediaP = new MediaPlayer();
    private MediaPlayer mediaE = new MediaPlayer();

    private String uP;
    private String uE;
    private int durPol;
    ProgressDialogTask progressDialogTask = new ProgressDialogTask();




    /*
     Utworzenie konstruktora Klasy*********************
    */


    public ConnectForAudio(MediaPlayer mediaP, MediaPlayer mediaE,
                           String uP, String uE, ProgressDialogTask progressDialogTask) {
        this.mediaE = mediaE;
        this.mediaP = mediaP;
        this.uP = uP;
        this.uE = uE;
        this.progressDialogTask = progressDialogTask;

    }

    /*
    Utworzenie metody Run osobnego watku interfejsu Runnable******************
    */
    @Override
    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient con = null; // Utworzenie zmiennej do polaczenia
                    con = new FTPClient();
                    con.connect("adresss FTP"); // ustawienie adresu FTP

                    if (con.login("login", "pass")) {
                        con.enterLocalPassiveMode(); // ustawienie passiveMode
                        con.setFileType(FTP.BINARY_FILE_TYPE); // typ Plikow Binarny

                        try {

                            mediaP.reset(); // resetuje tutaj bo nie dziala nauka gdy resetuje w metodach
                            mediaE.reset();
                            playFromFTP(mediaP, mediaE, uP, uE); //wywolanie metody playFromFTP


                        } catch (Exception e) {
                            e.printStackTrace(); //wyjatki
                        }
                        con.logout(); // wylogowanie z FTP
                        con.disconnect(); // Rozlacznie z FTP

                    }


                } catch (Exception e) { // Wyjatki
                    Log.v("download result", "failed"); // LOG
                    e.printStackTrace();
                }


            }
        }).start(); // Start Watku


    }





    /*
    Metoda playFromFTP do Odtwaznia Audio bezposrednio z FTP , przyjmuje 4 zmienne,
  / Wykorzystuje metode playSoundEnglish   ***************************************
    */
    public void playFromFTP(MediaPlayer mediaP, final MediaPlayer mediaE, String uP, final String uE) throws Exception {

       // mediaP.reset(); pwowduje przyciecia  w runinLearn dlatego resetuje w innym miescu dla runa
        mediaP.setDataSource(uP);
        mediaP.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaP.prepare();
        mediaP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                progressDialogTask.run();

            }
        });
        durPol = mediaP.getDuration();
        mediaP.start();// odtworzenie Polskiego slowa


        new Thread(new Runnable() { // utworzenie nowego watku
            @Override
            public void run() {


                try {
                    sleep(durPol * 2); // przerwa na odtworzenie i powtorzenie poolskiego slowa
                    playSoundEnglish(mediaE, uE);// odegranie angioelskiego slwoa

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    /*
    Metoda play Sound English przyjmuje dwie zmienne odgrywa dwa razy angielskie slowa**************
    */
    protected void playSoundEnglish(final MediaPlayer mediaE, final String uE) throws Exception {

     // mediaE.reset(); pwowduje przyciecia  w runinLearn dlatego resetuje w innym miescu dla runa
        mediaE.setDataSource(uE);
        mediaE.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaE.prepare();
        mediaE.getDuration();
        mediaE.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        mediaE.start();


        mediaE.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren


            @Override

            public void onCompletion(final MediaPlayer E) {


                if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                {

                                    sleep(mediaE.getDuration());  // przerwa na samodzielne powtorzenie
                                    mediaE.start();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            liczbaPowtorzen++;

                        }
                    }).start();

                }
            }


        });


    }












}
