package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apps.philipps.app.R;

public class CreatePlanPart extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private BreathPlan breathPlan;

    private int planId;
    private boolean newPlan;

    private EditText etPlanDescription;

    private TextView txtBreathsPerMinute;
    private SeekBar sbBreathsPerMinute;

    private EditText etDurationOfExercise;

    private boolean activated;

    private Button btnSubmitPlan;
    private Button btnCancelPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_part);

        Intent intent = getIntent();
        this.planId = intent.getIntExtra(PlansManager.EXTRA_PLAN_ID, 0);

        activated = false;

        int requestCode = intent.getIntExtra(PlansManager.EXTRA_REQUEST_CODE, PlansManager.REQUEST_CODE_CREATE_PLAN);
        if (requestCode == PlansManager.REQUEST_CODE_CREATE_PLAN) { // New Plan
            this.newPlan = true;
            this.breathPlan = new BreathPlan(planId, "", true,
                    BreathPlan.INTENSITY_MEDIUM, BreathPlan.MIN_BREATHS_PER_MINUTE, BreathPlan.MIN_DURATION_OF_EXERCISE, false);
            initGUIComponents(newPlan, getString(R.string.creating_plan) + planId);
        } else if (requestCode == PlansManager.REQUEST_CODE_EDIT_PLAN) { // Existing Plan
            this.newPlan = false;
            this.breathPlan = PlansManager.getBreathPlan(planId - 1);
            initGUIComponents(newPlan, getString(R.string.editing_plan) + planId);
        }
    }


    private void initGUIComponents(boolean newPlan, String title) {
        ((TextView) findViewById(R.id.txtCreatePlanTitle)).setText(title);

        etPlanDescription = (EditText) findViewById(R.id.etPlanDescription);

        sbBreathsPerMinute = (SeekBar) findViewById(R.id.sbBreathsPerMinute);
        sbBreathsPerMinute.setMax(BreathPlan.MAX_BREATHS_PER_MINUTE - BreathPlan.MIN_BREATHS_PER_MINUTE);

        txtBreathsPerMinute = (TextView) findViewById(R.id.txtBreathsPerMinute);

        etDurationOfExercise = (EditText) findViewById(R.id.etDurationOfExercise);

        btnSubmitPlan = (Button) findViewById(R.id.btnSubmitPlan);
        btnCancelPlan = (Button) findViewById(R.id.btnCancelPlan);

        if (breathPlan.getName().equals("")) {
            btnSubmitPlan.setEnabled(false);
        } else {
            etPlanDescription.setText(breathPlan.getName());
        }

        if (breathPlan.isBreatheIn()) {
            ((RadioButton) findViewById(R.id.rbInhalation)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.rbExhalation)).setChecked(true);
        }

        int intensity = breathPlan.getIntensity();

        switch (intensity) {
            case BreathPlan.INTENSITY_VERY_LOW:
                ((RadioButton) findViewById(R.id.rbVeryLow)).setChecked(true);
                break;
            case BreathPlan.INTENSITY_LOW:
                ((RadioButton) findViewById(R.id.rbLow)).setChecked(true);
                break;
            case BreathPlan.INTENSITY_MEDIUM:
                ((RadioButton) findViewById(R.id.rbMedium)).setChecked(true);
                break;
            case BreathPlan.INTENSITY_HIGH:
                ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
                break;
            case BreathPlan.INTENSITY_VERY_HIGH:
                ((RadioButton) findViewById(R.id.rbVeryHigh)).setChecked(true);
                break;
            default:
                ((RadioButton) findViewById(R.id.rbMedium)).setChecked(true);
                break;
        }

        int breathsPerMinute = breathPlan.getBreathsPerMinute();
        if (breathsPerMinute >= BreathPlan.MIN_BREATHS_PER_MINUTE && breathsPerMinute <= BreathPlan.MAX_BREATHS_PER_MINUTE) {
            txtBreathsPerMinute.setText(breathsPerMinute + "");
            sbBreathsPerMinute.setProgress(breathsPerMinute - BreathPlan.MIN_BREATHS_PER_MINUTE);
        }

        etDurationOfExercise.setText(breathPlan.getMinutesPerExercise() + "");

        activated = breathPlan.isActivated();

        etPlanDescription.addTextChangedListener(getPlanDescriptionTextWatcher());

        etDurationOfExercise.addTextChangedListener(getDurationOfExercise());

        sbBreathsPerMinute.setOnSeekBarChangeListener(this);

        btnSubmitPlan.setOnClickListener(this);
        btnCancelPlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmitPlan) {
            submitPlan();
            setResult(PlansManager.RESULT_CODE_PLAN_SUBMITTED);
            finish();
        } else if (v.getId() == R.id.btnCancelPlan) {
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            txtBreathsPerMinute.setText((progress + BreathPlan.MIN_BREATHS_PER_MINUTE) + "");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void submitPlan() {
        breathPlan.setName(etPlanDescription.getText() + "");

        breathPlan.setBreatheIn(((RadioButton) findViewById(R.id.rbInhalation)).isChecked());

        int intensity;
        if (((RadioButton) findViewById(R.id.rbVeryHigh)).isChecked()) {
            intensity = BreathPlan.INTENSITY_VERY_HIGH;
        } else if (((RadioButton) findViewById(R.id.rbHigh)).isChecked()) {
            intensity = BreathPlan.INTENSITY_HIGH;
        } else if (((RadioButton) findViewById(R.id.rbMedium)).isChecked()) {
            intensity = BreathPlan.INTENSITY_MEDIUM;
        } else if (((RadioButton) findViewById(R.id.rbLow)).isChecked()) {
            intensity = BreathPlan.INTENSITY_LOW;
        } else {
            intensity = BreathPlan.INTENSITY_VERY_LOW;
        }
        breathPlan.setIntensity(intensity);

        breathPlan.setBreathsPerMinute(sbBreathsPerMinute.getProgress() + BreathPlan.MIN_BREATHS_PER_MINUTE); // getProgress() startet bei 0
        breathPlan.setMinutesPerExercise(Integer.parseInt(etDurationOfExercise.getText() + ""));

        if (newPlan) {
            PlansManager.addBreathPlan(breathPlan);
        } else {
            PlansManager.setBreathPlan(this.planId, breathPlan);
        }
    }

    private TextWatcher getPlanDescriptionTextWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validatePlanDescription(s + "")) {
                    enableSubmitButton(true);
                } else {
                    enableSubmitButton(false);
                }
            }
        };
        return tw;
    }

    private boolean validatePlanDescription(String etPlanDescription) {
        if (etPlanDescription.equals("")) {
            // TODO Benutzer auf benoetigtes Feld hinweisen
            return false;
        } else {
            return true;
        }
    }

    private void enableSubmitButton(boolean enable) {
        btnSubmitPlan.setEnabled(enable);
    }

    private TextWatcher getDurationOfExercise() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateDurationOfExercise(s + "");
            }
        };
        return tw;
    }

    private boolean validateDurationOfExercise(String etDurationOfExerciseString) {
        if (!etDurationOfExerciseString.equals("")) {
            if (Integer.parseInt(etDurationOfExerciseString) > BreathPlan.MAX_DURATION_OF_EXERCISE) {
                etDurationOfExercise.setText(BreathPlan.MAX_DURATION_OF_EXERCISE + "");
            } else if (Integer.parseInt(etDurationOfExerciseString) < BreathPlan.MIN_BREATHS_PER_MINUTE) {
                etDurationOfExercise.setText(BreathPlan.MIN_DURATION_OF_EXERCISE + "");
            }
        } else {
            etDurationOfExercise.setText(BreathPlan.MIN_DURATION_OF_EXERCISE + "");
        }
        return true;
    }
}
