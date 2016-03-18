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

    int durPol;
    int durEn;
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
            recorder.release();
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


    public int playFromFTP(String uP, final String uE) throws Exception {


        mediaPlayer.setDataSource(uP);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        durPol = mediaPlayer.getDuration();

        mediaPlayer.start();// odtworzenie Polskiego slowa
       // mediaPlayer.release();

        new Thread(new Runnable() { // utworzenie nowego watku
            @Override
            public void run() {


                try {
                    sleep(durPol * 2); // przerwa na odtworzenie i powtorzenie poolskiego slowa
                    durEn = playSoundEnglish(uE);// odegranie angioelskiego slwoa

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return ((durPol + durEn));

    }

    protected int playSoundPolish(String uP) throws Exception {

        // killMediaPlayer(media);


        return mediaPlayer.getDuration();


    }


    protected int playSoundEnglish(String uE) throws Exception {

       // mediaPlayer.release();

        mediaPlayer.setDataSource(uE);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();

        mediaPlayer.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        mediaPlayer.start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


                    int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren
                    int dur = 0;

                    @Override
                    public void onCompletion(MediaPlayer EnglishAudio) {


                        if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                            try {
                                synchronized (this) {
                                    Thread.sleep(EnglishAudio.getDuration());  // przerwa na samodzielne powtorzenie
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
        }).start();


        return mediaPlayer.getDuration();
    }


}



















