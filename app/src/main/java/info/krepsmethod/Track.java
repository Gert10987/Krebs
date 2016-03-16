package info.krepsmethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-02-10.
 */
public class Track extends Activity {


    //****************** Klasa Track opowiedzialna za nagrywanie ,
    // odtwarzanie nagranTworzenie zmiennych


    private MediaPlayer mediaPlayer;

    private MediaRecorder recorder;

    String OutputFile;

    String SoundInList;


    //  Metoda rozpoczynajca nagranie

    public void beginRecording() throws Exception {

        killMediaRecorder();
        File outFile = new File(OutputFile);
        if (outFile.exists()) {
            outFile.delete();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OutputFile);
        recorder.prepare();
        recorder.start();
    }

    /*

    Stop recording

     */
    protected void stopRecording() throws Exception {
        if (recorder != null) {
            recorder.stop();
        }
    }

    /*

    Kill record

     */
    protected void killMediaRecorder() {
        if (recorder != null) {
            recorder.release();
        }
    }

    /*
    kill Odtwarzanie
     */
    protected void killMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*

    play recording
     */

    protected void playRecording() throws Exception {
        killMediaPlayer(mediaPlayer);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OutputFile);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaRecorder();
        killMediaPlayer(mediaPlayer);


    }


    protected void onDestroySound(MediaPlayer media) {
        super.onDestroy();
        killMediaRecorder();
        killMediaPlayer(media);


    }


    public void stopPlayingSound(MediaPlayer media) throws Exception {

        media.stop();


    }


    public void playFromFTP
            (final MediaPlayer PolishAudio, final String uP,
             final MediaPlayer EnglishAudio, final String uE) throws Exception {


        playSoundPolish(PolishAudio, uP); // odtworzenie Polskiego slowa

        new Thread(new Runnable() { // utworzenie nowego watku
            @Override
            public void run() {

                try {
                    sleep(PolishAudio.getDuration() * 2); // przerwa na odtworzenie i powtorzenie poolskiego slowa
                    playSoundEnglish(EnglishAudio, uE);// odegranie angioelskiego slwoa
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    protected int playSoundPolish(MediaPlayer PolishAudio, String uP) throws Exception {

        // killMediaPlayer(media);


        PolishAudio.setDataSource(uP);
        PolishAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
        PolishAudio.prepare();
        PolishAudio.start();


        return PolishAudio.getDuration();

    }


    protected void playSoundEnglish(MediaPlayer EnglishAudio, String uE) throws Exception {

        // killMediaPlayer(media);

        EnglishAudio.setDataSource(uE);
        EnglishAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
        EnglishAudio.prepare();
        EnglishAudio.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        EnglishAudio.start();

        EnglishAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren


            @Override
            public void onCompletion(MediaPlayer EnglishAudio) {

                if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                    try {
                        synchronized (this) {
                            wait(EnglishAudio.getDuration());  // przerwa na samodzielne powtorzenie
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    EnglishAudio.start();
                    liczbaPowtorzen++;
                }
            }


        });


    }


}



















