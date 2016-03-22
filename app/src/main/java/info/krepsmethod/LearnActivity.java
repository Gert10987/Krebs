package info.krepsmethod;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    private Visualizer mVisualizer;


    ProgressDialog PB;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonLearn = (Button) findViewById(R.id.buttonLearn);


        c = getIntent().getStringArrayListExtra("list");

        PB = new ProgressDialog(LearnActivity.this);

        zaladowano = (TextView) findViewById(R.id.zaladowano);
        zaladowano.setText(" ");


        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread x = new Thread();
                LearnTask learnTask = new LearnTask(x, mediaE, mediaP, c, mdP, mdE, buttonLearn);
                Thread task = new Thread(learnTask);


                if (zaladowano.getText() == " ") {

                    zaladowano.setText("Odtwarzanie");
                    buttonLearn.setText("Stop");
                    task.start();


                } else if (buttonLearn.getText() == "Stop") {

                    zaladowano.setText(" ");
                    buttonLearn.setText("Start");

                    x.isInterrupted();

                    mediaE.stop();
                    mediaE.release();
                    mediaP.stop();
                    mediaP.release();

                    mediaP = new MediaPlayer();
                    mediaE = new MediaPlayer();


                }


            }
        });
    }





}













