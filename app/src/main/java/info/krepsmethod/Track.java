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
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
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


}



















