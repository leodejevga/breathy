package com.apps.philipps.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.apps.philipps.app.R;

public class CreatePlan extends AppCompatActivity implements View.OnClickListener{

    private final int MAX_EXERCISE_DURATION = 60;
    private final int MIN_EXERCISE_DURATION = 1;

    private int exerciseDuration;

    private RadioButton rbInhalation, rbExhalation;

    private EditText etExerciseDuration;

    private Button btnCancelPlan, btnSubmitPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        loadOptions();

        initGUIComponents();
    }

    private void loadOptions() {
        // TODO Daten aus Speicher laden
        exerciseDuration = 5;
    }

    private void initGUIComponents() {
        rbInhalation = (RadioButton) findViewById(R.id.rbInhalation);
        rbExhalation = (RadioButton) findViewById(R.id.rbExhalation);

        etExerciseDuration = (EditText) findViewById(R.id.etDurationOfExercise);
        etExerciseDuration.setText(exerciseDuration);

        ((Button) findViewById(R.id.btnMinutesPlus)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMinutesMinus)).setOnClickListener(this);

        btnCancelPlan = (Button) findViewById(R.id.btnCancelPlan);
        btnSubmitPlan = (Button) findViewById(R.id.btnSubmitPlan);

        btnCancelPlan.setOnClickListener(this);
        btnSubmitPlan.setOnClickListener(this);
    }

    private void saveOptions() {
        // TODO Speichern der Einstellungen implementieren
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMinutesPlus){
            if (getCurrentMinutes() < MAX_EXERCISE_DURATION) {
                etExerciseDuration.setText(getCurrentMinutes() + 1);
            }
        } else if (v.getId() == R.id.btnMinutesMinus) {
            if (getCurrentMinutes() > MIN_EXERCISE_DURATION) {
                etExerciseDuration.setText(getCurrentMinutes() - 1);
            }
        } else if (v.getId() == R.id.btnCancelPlan) {
            // Close the Activity
            finish();
        } else if (v.getId() == R.id.btnSubmitPlan) {
            saveOptions();
            // Close the Activity
            finish();
        }

    }
    private int getCurrentMinutes() {
        return Integer.parseInt(etExerciseDuration.getText() + "");
    }
}
