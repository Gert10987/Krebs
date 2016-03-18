package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by Paweł on 2016-03-17.
 */
public class ConnectForDur implements Runnable {

    int durPol;
    int durEn;
    int i;

    MediaPlayer mediaE = new MediaPlayer();
    MediaPlayer mediaP = new MediaPlayer();

    public String uP;
    public String uE;


    public ConnectForDur(MediaPlayer mediaE, MediaPlayer mediaP, String uP, String uE) {
        this.mediaE = mediaE;
        this.mediaP = mediaP;
        this.uE = uE;
        this.uP = uP;

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

                            MediaPlayer e = new MediaPlayer();
                            MediaPlayer p = new MediaPlayer();



                            e.setDataSource(uE);
                            p.setDataSource(uP);
                            i = d(e.getDuration(),
                                    p.getDuration());



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


    public int d(int durEn, int durPol) {

        int x = 0;

        x = ((durEn + durPol + durEn) * 2 / 100);

        return x;

    }

    public int get() {

        return i;

    }

    public int beforPlayP(MediaPlayer mediaP, String uP) throws IOException {


        mediaP.setDataSource(uP);
        mediaP.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaP.prepareAsync();
        durPol = mediaP.getDuration();
        return durPol;

    }

    public int beforePlayE(MediaPlayer mediaE, String uE) throws IOException {


        mediaE.setDataSource(uE);
        mediaE.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaE.prepareAsync();
        durEn = mediaE.getDuration();
        return durEn;

    }
}


