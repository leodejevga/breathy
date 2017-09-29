package com.apps.philipps.opengltest.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.philipps.opengltest.Backend;
import com.apps.philipps.opengltest.R;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.OptionManager;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * OptionManager Activity
 */
public class Options extends Activity {
    private TextView coinsText;
    private AudioManager audioManager;
    int[] musicIndex;
    Spinner music_spinner;
    String[] song_names = new String[0];
    SeekBar volumeSeekbar;
    int yellow = 0xffffff00;
    int green = 0xff00ff00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_options);
        initOptions(getApplicationContext());
        initLabels();
        initControls();
    }

    private void initLabels() {
        coinsText = (TextView) findViewById(R.id.asOptionsCoins);
        coinsText.setText(Coins.getAmount() + " Coins");
    }

    private void initOptions(final Context context) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.optionsButtons);
        for (int i = 0; i < Backend.options.size(); i++) {
            OptionManager.Option option = Backend.options.getOption(i);
            Button btn = new Button(this);
            int color = btn.getSolidColor();
            btn.setBackgroundColor((Boolean) option.getValue() ? yellow : color);
            btn.setText(option.Parameter.toString());
            btn.setId(i + 251);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    int id = b.getId() - 251;
                    OptionManager.Option option = Backend.options.getOption(id);

                    boolean bought = false;
                    if (!Backend.options.getValue(id)) {
                        bought = Coins.buy(option.getPrice(), context);
                        Backend.options.set(id, bought);
                    }
                    unsetAllOptions(id);
                    coinsText.setText(Coins.getAmount() + " Coins");
                    Backend.saveGameOptions(context, Backend.gName);
                }
            });
            layout.addView(btn);
        }
    }

    private void unsetAllOptions(int exceptID) {
        for (int i = 0; i < Backend.options.size(); i++) {
            if (i == exceptID)
                Backend.options.getOption(i).setIsSet(true);
            else
                Backend.options.getOption(i).setIsSet(false);
            Button b = (Button) findViewById(i + 251);
            OptionManager.Option<String, Boolean> opt = Backend.options.getOption(i);
            int color = b.getSolidColor();
            if (opt.isSet())
                b.setBackgroundColor(green);
            else
                b.setBackgroundColor(Backend.options.getValue(i) ? yellow : color);
        }
    }

    private void initControls() {

        try {
            song_names = getListDraw();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        music_spinner = (Spinner) findViewById(R.id.musiclist);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Options.this, android.R.layout.simple_spinner_item, song_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        music_spinner.setAdapter(adapter);

        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.musicSeekBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int selected_music_index = getResourceIndex();
                MediaPlayer myMediaPlayer = MediaPlayer.create(Options.this, musicIndex[selected_music_index]);

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                    myMediaPlayer.stop();
                    myMediaPlayer.release();
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                    selected_music_index = getResourceIndex();
                    myMediaPlayer = MediaPlayer.create(Options.this, musicIndex[selected_music_index]);
                    myMediaPlayer.start();
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button applyAndBackButton = (Button) findViewById(R.id.audiosurf_apply_and_back_button);
        applyAndBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGameMusicAndVolumme();
            }
        });
    }

    private String[] getListDraw() throws IllegalAccessException {
        Field[] fields = R.raw.class.getFields();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().contains("as")) {
                indices.add(fields[i].getInt(fields[i]));
                names.add(fields[i].getName().replace("as", "").toUpperCase());
            }
        }
        musicIndex = new int[indices.size()];
        String[] names_array = new String[names.size()];
        for (int i = 0; i < indices.size(); i++) {
            musicIndex[i] = indices.get(i);
            names_array[i] = names.get(i);
        }
        return names_array;
    }

    int getResourceIndex() {
        String selected_song = music_spinner.getSelectedItem().toString();
        int index = -1;
        for (int i = 0; i < song_names.length; i++) {
            if (song_names[i].equals(selected_song))
                return i;
        }
        return index;
    }

    public void changeGameMusicAndVolumme() {
        Backend.setDefault_music_resource_id(musicIndex[getResourceIndex()]);
        finish();
    }
}