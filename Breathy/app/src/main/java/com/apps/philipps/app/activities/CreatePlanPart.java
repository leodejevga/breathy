package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;

public class CreatePlanPart extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private PlanManager.Plan.Option part;
    private PlanManager.Plan.Option newPart;

    private boolean create;

    private SeekBar frequency;
    private TextView frequencyText;
    private TextView duration;
    private SeekBar in;
    private SeekBar out;

    private Button btnSubmitPlan;
    private Button btnCancelPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_part);

        part = (PlanManager.Plan.Option) getIntent().getSerializableExtra("planPart");
        create = part==null;
        initViews();
        if(!create)
            initData();
    }

    private void initData() {
        in.setProgress(part.getIn().id);
        out.setProgress(part.getOut().id);

    }


    private void initViews() {
        ((TextView) findViewById(R.id.txtCreatePlanTitle)).setText(create?"Create new part": "Edit part of plan");

        frequency = (SeekBar) findViewById(R.id.sbBreathsPerMinute);
        frequencyText = (TextView) findViewById(R.id.txtBreathsPerMinute);
        duration = (EditText) findViewById(R.id.etDurationOfExercise);

        btnSubmitPlan = (Button) findViewById(R.id.btnSubmitPlan);
        btnCancelPlan = (Button) findViewById(R.id.btnCancelPlan);
        in = (SeekBar) findViewById(R.id.seekIn);
        out = (SeekBar) findViewById(R.id.seekOut);

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

    public void addMinute(View view) {
    }
    public void subMinute(View view) {
    }
}
