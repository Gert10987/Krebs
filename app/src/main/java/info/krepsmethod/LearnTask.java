package info.krepsmethod;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-03-22.
 */
public class LearnTask implements Runnable {

    /*
    Odtwarzanie w LeranActivity
     */
    Thread x;
    private MediaPlayer mediaP = new MediaPlayer();
    private MediaPlayer mediaE = new MediaPlayer();
    ArrayList c = new ArrayList<String>();
    private MediaPlayer mdP = new MediaPlayer();
    private MediaPlayer mdE = new MediaPlayer();
    Button zaladowano;



    LearnTask(Thread x, MediaPlayer mediaE, MediaPlayer mediaP,
              ArrayList c, MediaPlayer mdP, MediaPlayer mdE, Button zaladowano){
        this.x = x;
        this.mediaE = mediaE;
        this.mediaP = mediaP;
        this.c = c;
        this.mdE = mdE;
        this.mdP = mdP;
        this. zaladowano = zaladowano;
    }


    @Override
    public void run() {


        for (int i = 0; i < c.size(); i++) {


            String uP = "url with diretory Polish Words" + c.get(i) + "polish.3gpp";
            String uE = "url with diretory English Words" + c.get(i) + "english.3gpp";

            ConnectForDuration connectForDuration = new
                    ConnectForDuration(uP, uE, mdP, mdE);

            connectForDuration.runInLearn();


            ConnectForAudioLearn connectForAudioLearn = new
                    ConnectForAudioLearn(mediaP, mediaE, uP, uE);

            x = new Thread(connectForAudioLearn);

            x.start();

            if(zaladowano.getText() == "Start"){

                break;
            }


            try {
                sleep(connectForDuration.getFullAudioToLearn());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }









}
