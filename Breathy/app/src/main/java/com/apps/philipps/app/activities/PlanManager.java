package com.apps.philipps.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.apps.philipps.app.BreathPlan;
import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;

import java.util.ArrayList;

public class PlanManager extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initGUIComponents();
    }

    private void initGUIComponents() {
        FloatingActionButton fabAddPlan = (FloatingActionButton) findViewById(R.id.fabAddPlan);
        fabAddPlan.setOnClickListener(this);

        ArrayList<BreathPlan> breathPlan = new ArrayList<>();
        BreathPlan bp = new BreathPlan(1, "Plan 1", true, 60, 20, 5, true);
        breathPlan.add(bp);

        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this, breathPlan);

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddPlan){

        }
    }
}
