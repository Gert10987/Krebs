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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

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
        if(outFile.exists()) {
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
    /*

    Metoda playSound do odtworzenia w liscie
     */

    protected void playSoundPolish(MediaPlayer PolishAudio, String url) throws Exception {

           // killMediaPlayer(media);

            PolishAudio.setDataSource(url);
            //int waitTime = PolishAudio.getDuration();
            PolishAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            PolishAudio.prepare();
            PolishAudio.start();
           // wait(waitTime);

    }

    protected void playSoundEnglish(MediaPlayer EnglishAudio, String url) throws Exception {

        // killMediaPlayer(media);

        EnglishAudio.setDataSource(url);
        final int waitTime = EnglishAudio.getDuration();
        EnglishAudio.prepare();
        EnglishAudio.setLooping(false); // setlooping ustawione na false, przekazuje wykonanie do setOnCompletionListener
        EnglishAudio.start();

        EnglishAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            int liczbaPowtorzen = 0;// zmienna odpowidzialna za licze powtoren

            @Override
            public void onCompletion(MediaPlayer media) {

                if (liczbaPowtorzen < 1) { //warunek ile ma byc powtorzen


                    try {
                        synchronized (this) {
                            wait(waitTime);  // przerwa na samodzielne powtorzenie
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    media.start();
                    liczbaPowtorzen++;
                }
            }

        });

    }



    protected void onDestroySound(MediaPlayer media) {
        super.onDestroy();
        killMediaRecorder();
        killMediaPlayer(media);


    }


    public void stopPlayingSound(MediaPlayer media) throws Exception{

                media.stop();



        }


    public void playFromFTP(){

    }


    }



















