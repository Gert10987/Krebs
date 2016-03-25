package info.krepsmethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class LearnActivity extends AppCompatActivity {


    Button buttonLearn;
    private MediaPlayer mediaP = new MediaPlayer();
    private MediaPlayer mediaE = new MediaPlayer();
    ArrayList c = new ArrayList<String>();
    MediaPlayer mdP = new MediaPlayer();
    MediaPlayer mdE = new MediaPlayer();
    TextView zaladowano;
    ProgressDialog PB;

    VisualizerView mVisualizerViewE, mVisualizerViewP;

    private MediaPlayer mMediaPlayer, mMediaPlayerC;

    private Visualizer mVisualizerP, mVisualizerE;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonLearn = (Button) findViewById(R.id.buttonLearn);

        mVisualizerViewP = (VisualizerView) findViewById(R.id.vP);
        mVisualizerViewE = (VisualizerView) findViewById(R.id.vE);


        c = getIntent().getStringArrayListExtra("list");

        PB = new ProgressDialog(LearnActivity.this);

        zaladowano = (TextView) findViewById(R.id.zaladowano);
        zaladowano.setText(" ");


        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread x = new Thread();
                LearnTask learnTask = new LearnTask(x, mediaE, mediaP, c, mdP, mdE, buttonLearn);
                final Thread task = new Thread(learnTask);


                if (zaladowano.getText() == " ") {

                    zaladowano.setText("Odtwarzanie");
                    buttonLearn.setText("Stop");

                    task.start();

                    initVisualizerMediaP();

                    initVisualizerMediaE();


                } else if (buttonLearn.getText() == "Stop") {

                    zaladowano.setText(" ");
                    buttonLearn.setText("Start");

                    x.isInterrupted();

                    mediaE.stop();
                    mediaE.release();
                    mediaP.stop();
                    mediaP.release();

                    mVisualizerE.release();
                    mVisualizerP.release();

                    mediaP = new MediaPlayer();
                    mediaE = new MediaPlayer();


                }


            }
        });
    }


    private void initVisualizerMediaP() {

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mMediaPlayer = new MediaPlayer();

        setupVisualizerforMediaP();


        mVisualizerP.setEnabled(true);

        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mVisualizerP.setEnabled(false);
                    }
                });






    }

    private void initVisualizerMediaE() {

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mMediaPlayer = new MediaPlayer();

        setupVisualizerforMediaE();

        mVisualizerE.setEnabled(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVisualizerE.setEnabled(false);
            }
        });

    }


    private void setupVisualizerforMediaP() {


        // Create the Visualizer object and attach it to our media player.


        mVisualizerP = new Visualizer(mediaP.getAudioSessionId());


        mVisualizerP.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizerP.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {


                        mVisualizerViewP.updateVisualizer(bytes);

                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);


    }

    private void setupVisualizerforMediaE() {




        mVisualizerE = new Visualizer(mediaE.getAudioSessionId());

        mVisualizerE.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizerE.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {


                        mVisualizerViewE.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizerP.release();
            mVisualizerE.release();

        }
    }


}















