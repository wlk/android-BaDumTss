package com.varwise.badumtss;

import android.media.SoundPool;
import android.os.AsyncTask;

import java.util.ArrayList;

public class LoadSoundPoolTask extends AsyncTask<Void, Void, Void> {

    SoundPool soundPool;
    ArrayList<Integer> soundIds;
    MainActivity mainActivity;

    public LoadSoundPoolTask(SoundPool soundPool, ArrayList<Integer> soundIds, MainActivity mainActivity) {
        this.soundPool = soundPool;
        this.soundIds = soundIds;
        this.mainActivity = mainActivity;

    }

    @Override
    protected Void doInBackground(Void... params) {
        for (Integer rawSoundReference : MainActivity.rawSoundReferences) {
            soundIds.add(soundPool.load(mainActivity, rawSoundReference, 1));
        }
        return null;
    }
}
