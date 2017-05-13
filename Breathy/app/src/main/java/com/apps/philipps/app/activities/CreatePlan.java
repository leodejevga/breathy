package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apps.philipps.app.BreathPlan;
import com.apps.philipps.app.R;

public class CreatePlan extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private int planId;
    private boolean newPlan;

    private EditText etPlanDescription;

    private TextView txtBreathsPerMinute;
    private SeekBar sbBreathsPerMinute;

    private EditText etDurationOfExercise;

    private boolean activated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        Intent intent = getIntent();
        this.planId = intent.getIntExtra(PlanManager.EXTRA_PLAN_ID, 0);

        activated = false;

        int requestCode = intent.getIntExtra(PlanManager.EXTRA_REQUEST_CODE, PlanManager.REQUEST_CODE_CREATE_PLAN);
        if (requestCode == PlanManager.REQUEST_CODE_CREATE_PLAN){ // New Plan
            this.newPlan = true;
            initGUIComponents(newPlan, getString(R.string.creating_plan) + planId);
        } else if (requestCode == PlanManager.REQUEST_CODE_EDIT_PLAN) { // Existing Plan
            this.newPlan = false;
            initGUIComponents(newPlan, getString(R.string.editing_plan) + planId);
        }
    }



    private void initGUIComponents(boolean newPlan, String title) {
        ((TextView) findViewById(R.id.txtCreatePlanTitle)).setText(title);

        etPlanDescription = (EditText) findViewById(R.id.etPlanDescription);

        sbBreathsPerMinute = (SeekBar) findViewById(R.id.sbBreathsPerMinute);
        txtBreathsPerMinute = (TextView) findViewById(R.id.txtBreathsPerMinute);

        etDurationOfExercise = (EditText) findViewById(R.id.etDurationOfExercise);

        if (!newPlan){ // Editing Plan
            BreathPlan bp = PlanManager.getBreathPlan(planId - 1);

            etPlanDescription.setText(bp.getName());

            if (bp.isBreatheIn()){
                ((RadioButton) findViewById(R.id.rbInhalation)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.rbExhalation)).setChecked(true);
            }

            int intensity = bp.getIntensity();
            switch (intensity) {
                case BreathPlan.INTENSITY_VERY_LOW :
                    ((RadioButton) findViewById(R.id.rbVeryLow)).setChecked(true);
                case BreathPlan.INTENSITY_LOW :
                    ((RadioButton) findViewById(R.id.rbLow)).setChecked(true);
                case BreathPlan.INTENSITY_MEDIUM :
                    ((RadioButton) findViewById(R.id.rbMedium)).setChecked(true);
                case BreathPlan.INTENSITY_HIGH :
                    ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
                case BreathPlan.INTENSITY_VERY_HIGH :
                    ((RadioButton) findViewById(R.id.rbVeryHigh)).setChecked(true);
            }

            int breathsPerMinute = bp.getBreathsPerMinute();
            if (breathsPerMinute >= 2 && breathsPerMinute <= 30){
                txtBreathsPerMinute.setText(breathsPerMinute);
                sbBreathsPerMinute.setProgress(breathsPerMinute);
            }

            etDurationOfExercise.setText(bp.getMinutesPerExercise());

            activated = bp.isActivated();

        }

        sbBreathsPerMinute.setOnSeekBarChangeListener(this);

        ((Button) findViewById(R.id.btnSubmitPlan)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnCancelPlan)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmitPlan) {
            // TODO Werte übermitteln
            if (checkRequiredValues()) {
                submitPlan();
                setResult(PlanManager.RESULT_CODE_PLAN_SUBMITTED);
                finish();
            } else {
                // TODO Benutzer auf Fehler bei der Eingabe hinweisen
            }
        } else if (v.getId() == R.id.btnCancelPlan) {
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            txtBreathsPerMinute.setText((progress + 2) + "");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private boolean checkRequiredValues(){
        String planDescription = etPlanDescription.getText() + "";
        int durationOfExercise = Integer.parseInt(etDurationOfExercise.getText() + "");

        if (!planDescription.equals("") && durationOfExercise >= 1 && durationOfExercise <= 60) {
            return true;
        }
        return false;
    }
    private void submitPlan() {
        String name = etPlanDescription.getText() + "";
        boolean inhalate = ((RadioButton) findViewById(R.id.rbInhalation)).isChecked();
        int intensity;
        if (((RadioButton) findViewById(R.id.rbVeryHigh)).isChecked()){
            intensity = BreathPlan.INTENSITY_VERY_HIGH;
        } else if (((RadioButton) findViewById(R.id.rbHigh)).isChecked()){
            intensity = BreathPlan.INTENSITY_HIGH;
        } else if (((RadioButton) findViewById(R.id.rbMedium)).isChecked()){
            intensity = BreathPlan.INTENSITY_MEDIUM;
        } else if (((RadioButton) findViewById(R.id.rbLow)).isChecked()){
            intensity = BreathPlan.INTENSITY_LOW;
        } else {
            intensity = BreathPlan.INTENSITY_VERY_LOW;
        }

        int breathsPerMinute = sbBreathsPerMinute.getProgress() + 2;
        int minutesPerExercise = Integer.parseInt(etDurationOfExercise.getText() + "");

        if (newPlan){
            PlanManager.addBreathPlan(new BreathPlan(this.planId, name, inhalate, intensity, breathsPerMinute, minutesPerExercise, this.activated));
        }
    }
}
