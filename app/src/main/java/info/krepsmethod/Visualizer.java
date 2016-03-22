package info.krepsmethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Paweł on 2016-03-22.
 */
public class Visualizer extends View {

    private Rect rect = new Rect();
    private Paint paint = new Paint();
    private byte[] mBytes;
    private float[] mpoints;


    public Visualizer(Context context) {
        super(context);
    }

    private void init() {

        mBytes = null;
        paint.setStrokeWidth(1f);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(0, 1288, 255));
    }

    public void updateVisualizer(byte[] bytes) {

        mBytes = null;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {

            return;
        }

        if (mpoints == null || mpoints.length < mBytes.length * 4) {

            mpoints = new float[mBytes.length * 4];

        }

        rect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i++) {
            mpoints[i * 4] = rect.width() * i / (mBytes.length - 1);
            mpoints[i * 4 + 1] = rect.height() / 2
                    + ((byte) (mBytes[i] + 128)) * (rect.height() / 2) / 128;
            mpoints[i * 4 + 2] = rect.width() * (i + 1) / (mBytes.length - 1);
            mpoints[i * 4 + 3] = rect.height() / 2
                    + ((byte) (mBytes[i + 1] + 128)) * (rect.height() / 2)
                    / 128;
        }
    }
}
