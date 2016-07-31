package com.varwise.badumtss;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private ArrayList<Integer> soundIds;
    private float volume;
    private InterstitialAd interstitial;
    public static GoogleAnalytics analytics;
    private AdView adView;
    public static boolean adsEnabled = true;
    public static boolean appRateEnabled = true;

        public static final Integer[] rawSoundReferences = {
            R.raw.badumtss
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (adsEnabled) {
            showInterstitial();
            adView = (AdView) findViewById(R.id.adViewMainScreen);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            adView.loadAd(adRequest);
        }

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        setupGoogleAnalytics();
        maybeShowAppRate();


        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(1, volume, volume, 1, 0, 1f);
            }
        });


        soundIds = new ArrayList<>();

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        loadSoundPool();
    }

    private void setupGoogleAnalytics() {
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        Tracker t = analytics.newTracker(getResources().getString(R.string.googleAnalytics));
        t.enableExceptionReporting(true);
        t.enableAdvertisingIdCollection(true);
        t.enableAutoActivityTracking(true);
    }

    private void maybeShowAppRate() {
        if(appRateEnabled) {
            AppRate.with(this)
                    .setInstallDays(1)
                    .setLaunchTimes(2)
                    .setRemindInterval(1)
                    .setShowLaterButton(true)
                    .setDebug(false)
                    .monitor();

            AppRate.showRateDialogIfMeetsConditions(this);
        }
    }

    private void loadSoundPool() {
        LoadSoundPoolTask lspt = new LoadSoundPoolTask(soundPool, soundIds, this);
        lspt.execute();
    }

    private void showInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-5829945009169600/2717701968");
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitial.show();
            }
        });

        interstitial.loadAd(adRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adsEnabled) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adsEnabled) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adsEnabled) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
