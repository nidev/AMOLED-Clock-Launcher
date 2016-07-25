package com.example.amoledclocklauncher;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TextView timeView = null;
    private TextView ampmView = null;
    private final SimpleDateFormat sdf_ampm = new SimpleDateFormat("a", Locale.KOREAN);
    private final SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm", Locale.KOREAN);
    private PowerManager.WakeLock appClockWakeLock;
    private Timer timer;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        updateViews();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (appClockWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            appClockWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "myLock");
        }

        startTimer();
        appClockWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        stopTimer();

        if (appClockWakeLock != null) {
            appClockWakeLock.release();
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "ClockLauncher",
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                null,
                Uri.parse("android-app://com.example.amoledclocklauncher/launcher")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "ClockLauncher",
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                null,
                Uri.parse("android-app://com.example.amoledclocklauncher/launcher")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private synchronized void initViews() {
        timeView = (TextView) findViewById(R.id.timeView);
        timeView.setTextScaleX(0.8f);
        ampmView = (TextView) findViewById(R.id.ampmView);
    }

    private void updateViews() {
        Date now = new Date();

        ampmView.setText(sdf_ampm.format(now));
        timeView.setText(sdf_time.format(now));
    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateViews();
                    }
                });
            }
        };

        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(timerTask, 0, 15*1000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
