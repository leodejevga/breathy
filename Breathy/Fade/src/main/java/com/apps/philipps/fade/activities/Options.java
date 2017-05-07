package com.apps.philipps.fade.activities;

import android.app.Activity;
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

    private boolean isCustomColor;
    private ToggleButton btnCustomColorr;

    LinearLayout llCustomColor;
    private int red, green, blue;
    private SeekBar sbRed, sbBlue, sbGreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fade_options);

        // Load Stored Values
        loadOptions();

        initGUIComponents();
    }

    private void loadOptions() {
        isCustomColor = false;
        red = 127;
        green = 127;
        blue = 127;
    }

    private void initGUIComponents() {
        btnCustomColorr = (ToggleButton) findViewById(R.id.btnCustomColor);

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


        btnCustomColorr.setChecked(isCustomColor);

        btnCustomColorr.setOnCheckedChangeListener(this);


        llCustomColor = (LinearLayout) findViewById(R.id.llCustomColor);
        if (!isCustomColor) {
            llCustomColor.setVisibility(View.GONE);
        }

        sbRed = (SeekBar) findViewById(R.id.sbRed);
        sbBlue = (SeekBar) findViewById(R.id.sbBlue);
        sbGreen = (SeekBar) findViewById(R.id.sbGreen);

        sbRed.setProgress(red);
        sbGreen.setProgress(green);
        sbBlue.setProgress(blue);

        sbRed.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            // Close the Activity
            finish();
        } else if (v.getId() == R.id.btnSubmit) {
            saveOptions();
            finish();
        } else if (v.getId() == R.id.btnStandardColor1) {
            red = getResources().getColor(R.color.standardColor1) >> 16 & 0xFF;
            green = getResources().getColor(R.color.standardColor1) >> 8 & 0xFF;
            blue = getResources().getColor(R.color.standardColor1) & 0xFF;
        } else if (v.getId() == R.id.btnStandardColor2) {
            red = getResources().getColor(R.color.standardColor2) >> 16 & 0xFF;
            green = getResources().getColor(R.color.standardColor2) >> 8 & 0xFF;
            blue = getResources().getColor(R.color.standardColor2) & 0xFF;
        } else if (v.getId() == R.id.btnStandardColor3) {
            red = getResources().getColor(R.color.standardColor3) >> 16 & 0xFF;
            green = getResources().getColor(R.color.standardColor3) >> 8 & 0xFF;
            blue = getResources().getColor(R.color.standardColor3) & 0xFF;
        } else if (v.getId() == R.id.btnStandardColor4) {
            red = getResources().getColor(R.color.standardColor4) >> 16 & 0xFF;
            green = getResources().getColor(R.color.standardColor4) >> 8 & 0xFF;
            blue = getResources().getColor(R.color.standardColor4) & 0xFF;
        } else if (v.getId() == R.id.btnStandardColor5) {
            red = getResources().getColor(R.color.standardColor5) >> 16 & 0xFF;
            green = getResources().getColor(R.color.standardColor5) >> 8 & 0xFF;
            blue = getResources().getColor(R.color.standardColor5) & 0xFF;
        }
        sbRed.setProgress(red);
        sbGreen.setProgress(green);
        sbBlue.setProgress(blue);
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
        if (fromUser) {
            if (seekBar.getId() == sbRed.getId()) {

            } else if (seekBar.getId() == sbGreen.getId()) {

            } else if (seekBar.getId() == sbBlue.getId()) {

            }
        }
    }

    private void saveOptions() {

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
