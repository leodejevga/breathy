package com.apps.philipps.fade.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.apps.philipps.fade.R;

public class Options extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button btnStandardColor1, btnStandardColor2, btnStandardColor3, btnStandardColor4, btnStandardColor5;

    private ToggleButton btnCustomColor;

    LinearLayout llCustomColor;
    private SeekBar sbRed, sbBlue, sbGreen;

    private View vwColorPreview;

    private Button btnCancelOptions, btnSubmitOptions;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fade_options);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // Load Stored Values
        boolean isCustomColor = loadCustomColor();
        int activeColor = loadColor();

        initGUIComponents(isCustomColor, activeColor);
    }

    private void initGUIComponents(boolean isCustomColor, int activeColor) {
        btnCustomColor = (ToggleButton) findViewById(R.id.btnCustomColor);

        btnStandardColor1 = (Button) findViewById(R.id.btnStandardColor1);
        btnStandardColor2 = (Button) findViewById(R.id.btnStandardColor2);
        btnStandardColor3 = (Button) findViewById(R.id.btnStandardColor3);
        btnStandardColor4 = (Button) findViewById(R.id.btnStandardColor4);
        btnStandardColor5 = (Button) findViewById(R.id.btnStandardColor5);

        btnStandardColor1.setOnClickListener(this);
        btnStandardColor2.setOnClickListener(this);
        btnStandardColor3.setOnClickListener(this);
        btnStandardColor4.setOnClickListener(this);
        btnStandardColor5.setOnClickListener(this);


        btnCustomColor.setChecked(isCustomColor);

        btnCustomColor.setOnCheckedChangeListener(this);


        llCustomColor = (LinearLayout) findViewById(R.id.llCustomColor);
        if (!isCustomColor) {
            llCustomColor.setVisibility(View.GONE);
        }

        sbRed = (SeekBar) findViewById(R.id.sbRed);
        sbBlue = (SeekBar) findViewById(R.id.sbBlue);
        sbGreen = (SeekBar) findViewById(R.id.sbGreen);

        sbRed.setProgress(Color.red(activeColor));
        sbGreen.setProgress(Color.green(activeColor));
        sbBlue.setProgress(Color.blue(activeColor));

        sbRed.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);

        vwColorPreview = (View) findViewById(R.id.vwColorPreview);
        updateColorPreview();

        btnCancelOptions = (Button) findViewById(R.id.btnCancelOptions);
        btnSubmitOptions = (Button) findViewById(R.id.btnSubmitOptions);

        btnCancelOptions.setOnClickListener(this);
        btnSubmitOptions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int red, green, blue;
        if (v.getId() == btnCancelOptions.getId()) {
            // Close the Activity
            finish();
        } else if (v.getId() == btnSubmitOptions.getId()) {
            saveSharedPrefs();
            finish();
        } else if (v.getId() == btnStandardColor1.getId()) {
            updateSeekBars(getResources().getColor(R.color.standardColor1));
        } else if (v.getId() == btnStandardColor2.getId()) {
            updateSeekBars(getResources().getColor(R.color.standardColor2));
        } else if (v.getId() == btnStandardColor3.getId()) {
            updateSeekBars(getResources().getColor(R.color.standardColor3));
        } else if (v.getId() == btnStandardColor4.getId()) {
            updateSeekBars(getResources().getColor(R.color.standardColor4));
        } else if (v.getId() == btnStandardColor5.getId()) {
            updateSeekBars(getResources().getColor(R.color.standardColor5));
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
        if (toggleButton.getId() == R.id.btnCustomColor) {
            if (isChecked) {
                llCustomColor.setVisibility(View.VISIBLE);
            } else {
                llCustomColor.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateColorPreview();
    }

    private void updateSeekBars(int newColor){
        sbRed.setProgress(Color.red(newColor));
        sbGreen.setProgress(Color.green(newColor));
        sbBlue.setProgress(Color.blue(newColor));
    }

    private void updateColorPreview(){
        vwColorPreview.setBackgroundColor(Color.rgb(sbRed.getProgress(), sbGreen.getProgress(), sbBlue.getProgress()));
    }

    private int loadColor() {
        int red = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_red), 127);
        int green = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_green), 127);
        int blue = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_blue), 127);
        return Color.rgb(red, green, blue);
    }
    private boolean loadCustomColor(){
        return sharedPref.getBoolean(getString(R.string.com_apps_philipps_fade_preference_key_custom_color), false);
    }

    private void saveSharedPrefs() {
        sharedPref.edit().putBoolean(getString(R.string.com_apps_philipps_fade_preference_key_custom_color), btnCustomColor.isChecked()).commit();
        sharedPref.edit().putInt(getString(R.string.com_apps_philipps_fade_preference_key_red), sbRed.getProgress()).commit();
        sharedPref.edit().putInt(getString(R.string.com_apps_philipps_fade_preference_key_green), sbGreen.getProgress()).commit();
        sharedPref.edit().putInt(getString(R.string.com_apps_philipps_fade_preference_key_blue), sbBlue.getProgress()).commit();
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
