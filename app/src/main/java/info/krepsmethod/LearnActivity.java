package info.krepsmethod;

import android.app.ProgressDialog;
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
    TextView zaladowano;


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

                if(zaladowano.getText() == " ") {
                    try {
                        zaladowano.setText("Odtwarzanie");
                        ds();
                        buttonLearn.setText("Stop");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(buttonLearn.getText() == "Stop") {


                    mediaE.stop();
                    mediaE.release();
                    mediaP.stop();
                    mediaP.release();

                    mediaP = new MediaPlayer();
                    mediaE = new MediaPlayer();
                    zaladowano.setText(" ");


                }








            }
        });
    }

    public void ds() throws InterruptedException, IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {



                for (int i = 0; i < c.size(); i++) {
                    MediaPlayer mdP =  new MediaPlayer();
                    MediaPlayer mdE = new MediaPlayer();


                    String uP = "http://krebsmethod.cba.pl/PolishSound/" + c.get(i) + "polish.3gpp";
                    String uE = "http://krebsmethod.cba.pl/EnglishSound/" + c.get(i) + "english.3gpp";

                    ConnectForDuration connectForDuration = new
                            ConnectForDuration(uP, uE, mdP, mdE);

                    connectForDuration.runInLearn();


                    ConnectForAudio connectForAudio = new
                            ConnectForAudio(mediaP, mediaE, uP, uE);
                    connectForAudio.runInLearn();

                    Thread x = new Thread(connectForAudio);


                    x.start();





                    try {
                        sleep(connectForDuration.getFullAudioLenght()* 100 + 2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    mediaP = new MediaPlayer();
                    mediaE = new MediaPlayer();





                }

            }


        }).start();


    }
}






               /* for(int i = 1; i < listLength; i++){





                /*    new Thread(new Runnable() { // utworzenie nowego watku
                        @Override
                        public void run() {


                            try {
                                sleep(connectForDuration.getFullAudioLenght()); // przerwa na odtworzenie i powtorzenie poolskiego slowa
                                // odegranie angioelskiego slwoa

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/



        /*        }

            }
        });*/









