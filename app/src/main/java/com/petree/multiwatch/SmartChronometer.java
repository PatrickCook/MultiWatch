package com.petree.multiwatch;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.util.Log;

public class SmartChronometer extends TextView {
    @SuppressWarnings("unused")
    private static final String TAG = "Chronometer";

    public interface OnChronometerTickListener {

        void onChronometerTick(SmartChronometer chronometer);
    }

    public long mBase, mLastStopTime;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private OnChronometerTickListener mOnChronometerTickListener;

    private static final int TICK_WHAT = 2;

    private long timeElapsed;

    public SmartChronometer(Context context) {
        this (context, null, 0);
    }

    public SmartChronometer(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public SmartChronometer(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);

        init();
    }
    public void reset(){
            init();
    }

    private void init() {
        mLastStopTime = 0;
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }
    public void chronoStart()
    {
        Log.d("Warning","setBase() executed");
        // on first start
        if ( mLastStopTime == 0 )
            setBase( SystemClock.elapsedRealtime() );
            // on resume after pause
        else
        {

            long intervalOnPause = (SystemClock.elapsedRealtime() - mLastStopTime);
            setBase( getBase() +  intervalOnPause );
        }

        start();
    }

    public void start() {
        //mBase = SystemClock.elapsedRealtime();
        mStarted = true;
        updateRunning();
    }
    public void pause()
    {
        stop();
        mLastStopTime = SystemClock.elapsedRealtime();
    }

    public void stop() {

        mStarted = false;
        updateRunning();
    }


    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super .onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super .onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {
        timeElapsed = now - getBase();

        DecimalFormat df = new DecimalFormat("00");

        int hours = (int)(timeElapsed / (3600 * 1000));
        int remaining = (int)(timeElapsed % (3600 * 1000));

        int minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));

        int seconds = (int)(remaining / 1000);
        remaining = (int)(remaining % (1000));

        int milliseconds = (int)(((int)timeElapsed % 1000) / 10);

        String text = "";

        if (hours > 0) {
            text += df.format(hours) + ":";
        }

        text += df.format(minutes) + ":";
        text += df.format(seconds) + ":";
        text += Integer.toString(milliseconds);

        setText(text);
    }
    public String getTime(){
        return (String)getText();
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this , TICK_WHAT),
                        100);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
    public boolean isRunning(){
            return mRunning;

    }

}