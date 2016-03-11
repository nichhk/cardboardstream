package com.nichhk.remotestream.mjpeg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nichhk.remotestream.CardboardOverlayView;
import com.nichhk.remotestream.mjpeg.MjpegInputStream;

import java.io.IOException;

public class MjpegPlayer implements SurfaceHolder.Callback{


    public final static int SIZE_STANDARD   = 1;
    public final static int SIZE_BEST_FIT   = 4;

    private MjpegViewThread thread;
    private MjpegInputStream mIn = null;
    private boolean mRun = false;
    private Paint overlayPaint;

    private boolean surface1Done, surface2Done;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("TAG", "a surface was created!");
        if (surface1Done){
            surface2Done = true;
        }
        else{
            surface1Done = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public class MjpegViewThread extends Thread {
        private SurfaceView[] surfaces;

        public MjpegViewThread(SurfaceView ... surfaces) { this.surfaces = surfaces; }

        public void run() {

            final Paint p = new Paint();
            while (mRun) {
                if (surface1Done && surface2Done) {
                    try {
                        final Bitmap bm = mIn.readMjpegFrame();
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, surfaces[0].getWidth(), surfaces[0].getHeight(), false);
                        for (final SurfaceView surfaceView : surfaces) {
                            SurfaceHolder surface = surfaceView.getHolder();
                            synchronized (surface) {
                                Canvas c = surface.lockCanvas();
                                c.drawColor(Color.BLACK);
                                c.drawBitmap(scaled, 0, 0, p);
                                surface.unlockCanvasAndPost(c);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

    private void init(SurfaceView ... holders) {
        thread = new MjpegViewThread(holders);
        overlayPaint = new Paint();
        overlayPaint.setTextAlign(Paint.Align.LEFT);
        overlayPaint.setTextSize(12);
        overlayPaint.setTypeface(Typeface.DEFAULT);
    }

    public void startPlayback() {
        if(mIn != null) {
            mRun = true;
            thread.start();
        }
    }

    public void stopPlayback() {
        mRun = false;
        boolean retry = true;
        while(retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }


    public MjpegPlayer(CardboardOverlayView cov) {
        init(cov.getSurfaceViews());
        cov.setCallback(this);
    }


    public void setSource(MjpegInputStream source) {
        mIn = source;
        startPlayback();
    }
}
