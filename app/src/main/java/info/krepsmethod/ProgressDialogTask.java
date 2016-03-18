package info.krepsmethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.widget.Toast;

import static java.lang.Thread.sleep;

/**
 * Created by Paweł on 2016-03-18.
 */
public class ProgressDialogTask implements Runnable {


    private ProgressDialog PB;
    final private int DurationFullAudio;
    private MediaPlayer mediaE = new MediaPlayer();
    private MediaPlayer mediaP = new MediaPlayer();
    Context context;


    public ProgressDialogTask(ProgressDialog pb, int durationFullAudio, Context context,
                              MediaPlayer mediaP, MediaPlayer mediaE) {
        this.PB = pb;
        this.DurationFullAudio = durationFullAudio;
        this.context = context;
        this.mediaP = mediaP;
        this.mediaE = mediaE;
    }

    @Override
    public void run() {

            PB.setMessage("Odtwaraznie: ");
            PB.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            PB.setProgress(0);
            PB.setMax(DurationFullAudio);
            PB.setButton(DialogInterface.BUTTON_NEGATIVE, " STOP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();

                mediaE.stop();
                mediaE.release();
                mediaP.stop();
                mediaP.release();

                mediaP = new MediaPlayer();
                mediaE = new MediaPlayer();
                PB = new ProgressDialog(context);


            }
        });

            PB.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (PB.getProgress() < PB.getMax()) {

                        try {
                            sleep(1000);
                            PB.incrementProgressBy(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (PB.getProgress() == PB.getMax()) {
                            PB.dismiss();
                            PB.setProgress(0);

                        }

                    }
                }

            }).start();


        }



    }

