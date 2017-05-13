package com.apps.philipps.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.apps.philipps.app.BreathPlan;
import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;

import java.util.ArrayList;

public class PlanManager extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_PLAN_ID = "#EXTRA_PLAN_ID";
    public static final String EXTRA_REQUEST_CODE = "#EXTRA_REQUEST_CODE";
    public static final int REQUEST_CODE_CREATE_PLAN = 1001;
    public static final int REQUEST_CODE_EDIT_PLAN = 1002;
    public static final int RESULT_CODE_PLAN_SUBMITTED = 2001;


    private static ArrayList<BreathPlan> breathPlans;

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

        breathPlans = new ArrayList<>();
        BreathPlan bp = new BreathPlan(1, "Plan 1", true, 60, 20, 5, true);
        breathPlans.add(bp);

        initExpandableListView();

    }
    private void initExpandableListView() {
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this, breathPlans);

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddPlan){
            startCreatePlanActivity(true, breathPlans.size() + 1);
        }
    }

    private void startCreatePlanActivity(boolean newPlan, int planId){
        Intent newIntent = new Intent(PlanManager.this, CreatePlan.class);
        newIntent.putExtra(PlanManager.EXTRA_PLAN_ID, planId);
        if (newPlan) {
            newIntent.putExtra(PlanManager.EXTRA_REQUEST_CODE, PlanManager.REQUEST_CODE_CREATE_PLAN);
            startActivityForResult(newIntent, PlanManager.REQUEST_CODE_CREATE_PLAN);
        } else {
            newIntent.putExtra(PlanManager.EXTRA_REQUEST_CODE, PlanManager.REQUEST_CODE_EDIT_PLAN);
            startActivityForResult(newIntent, PlanManager.REQUEST_CODE_EDIT_PLAN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == PlanManager.RESULT_CODE_PLAN_SUBMITTED){
            initExpandableListView();
        }
    }

    public static BreathPlan getBreathPlan(int planId) {
        return breathPlans.get(planId);
    }

    public static boolean setBreathPlan(int planId, BreathPlan breathPlan) {
        try {
            PlanManager.breathPlans.set(planId, breathPlan);
        } catch ( IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
    public static void addBreathPlan(BreathPlan breathPlan) {
        breathPlans.add(breathPlan);
    }
}
