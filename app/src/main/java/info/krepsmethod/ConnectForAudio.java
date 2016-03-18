package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-03-17.
 */
public class ConnectForAudio implements Runnable {


    private MediaPlayer mediaP = new MediaPlayer();
    private MediaPlayer mediaE = new MediaPlayer();
    private String uP;
    private String uE;
    private int durPol;
    private int durEn;

    public ConnectForAudio(MediaPlayer mediaP, MediaPlayer mediaE, String uP, String uE) {
        this.mediaE = mediaE;
        this.mediaP = mediaP;
        this.uP = uP;
        this.uE = uE;


    }

    @Override
    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient con = null;
                    con = new FTPClient();
                    con.connect("95.211.80.5");

                    if (con.login("xxx@krebsmethod.cba.pl", "dupa.8")) {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        try {

                            playFromFTP(mediaP, mediaE, uP, uE);


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


            }
        }).start();


    }


    public int playFromFTP(MediaPlayer mediaP, final MediaPlayer mediaE, String uP, final String uE) throws Exception {

//        mediaP.setDataSource(uP);
       // mediaP.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaP.prepareAsync();
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


        return durPol;

    }


    protected int playSoundEnglish(MediaPlayer mediaE, final String uE) throws Exception {

        //  mediaE.setDataSource(uE);
      //  mediaE.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaE.prepare();
        mediaE.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        mediaE.start();
        mediaE.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren


            @Override
            public void onCompletion(MediaPlayer E) {


                if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                    try {
                        synchronized (this) {
                            wait(E.getDuration());  // przerwa na samodzielne powtorzenie
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    E.start();
                    liczbaPowtorzen++;
                }
            }


        });


        return durEn;
    }


}
