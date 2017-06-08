package com.apps.philipps.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;

public class CreatePlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        PlanManager.Plan plan = (PlanManager.Plan) getIntent().getSerializableExtra("plan");
        if(plan == null)
            finish();
        else init();
    }

    private void init(){

    }
}
