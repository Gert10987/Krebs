package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-03-25.
 */
public class ConnectForAudioLearn implements Runnable {

    private MediaPlayer mediaPLearn = new MediaPlayer();
    private MediaPlayer mediaELearn = new MediaPlayer();

    private String uP;
    private String uE;
    private int durPol;


    public ConnectForAudioLearn(MediaPlayer mediaPLearn, MediaPlayer mediaELearn,
                                String uP, String uE) {
        this.mediaELearn = mediaELearn;
        this.mediaPLearn = mediaPLearn;
        this.uP = uP;
        this.uE = uE;

    }

    @Override
    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient con = null; // Utworzenie zmiennej do polaczenia
                    con = new FTPClient();
                    con.connect("adress FTP"); // ustawienie adresu FTP

                    if (con.login("login", "pass")) {
                        con.enterLocalPassiveMode(); // ustawienie passiveMode
                        con.setFileType(FTP.BINARY_FILE_TYPE); // typ Plikow Binarny

                        try {
                            mediaELearn.reset();
                            mediaPLearn.reset();
                            playFromFTPLearn(mediaPLearn, mediaELearn, uP, uE); //wywolanie metody playFromFTP


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

        }).start();

    }


    public void playFromFTPLearn(MediaPlayer mediaPLearn, final MediaPlayer mediaELearn, String uP, final String uE) throws Exception {

        // mediaP.reset(); pwowduje przyciecia  w runinLearn dlatego resetuje w innym miescu dla runa
        mediaPLearn.setDataSource(uP);
        mediaPLearn.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPLearn.prepare();
        durPol = mediaPLearn.getDuration();
        mediaPLearn.start();// odtworzenie Polskiego slowa


        new Thread(new Runnable() { // utworzenie nowego watku
            @Override
            public void run() {


                try {
                    sleep(durPol * 2); // przerwa na odtworzenie i powtorzenie poolskiego slowa
                    playSoundEnglishLearn(mediaELearn, uE);// odegranie angioelskiego slwoa

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    protected void playSoundEnglishLearn(final MediaPlayer mediaELearn, final String uE) throws Exception {

        // mediaE.reset(); pwowduje przyciecia  w runinLearn dlatego resetuje w innym miescu dla runa
        mediaELearn.setDataSource(uE);
        mediaELearn.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaELearn.prepare();
        mediaELearn.getDuration();
        mediaELearn.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        mediaELearn.start();


        mediaELearn.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren


            @Override

            public void onCompletion(final MediaPlayer E) {


                if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                {

                                    sleep(mediaELearn.getDuration());  // przerwa na samodzielne powtorzenie
                                    mediaELearn.start();
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

