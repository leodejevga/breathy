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

    public static final int ID_PLACEHOLDER = 10000;


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
        breathPlans.add(createSamplePlan());

        initExpandableListView();

    }
    private void initExpandableListView() {
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this, breathPlans, getActivateButtonOnClickListener(), getEditButtonOnClickListener(), getDeleteButtonOnClickListener());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);

        elvPlans.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int clickedGroupPosition, long id) {
                boolean r = false;
                if (!parent.isGroupExpanded(clickedGroupPosition)) {
                    Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition);
                    r = parent.expandGroup(clickedGroupPosition);
                } else {
                    Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition + " > already expanded!");
                }
                return r;
            }
        });
        elvPlans.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int clickedGroupPosition) {
                Log.d("### APP", "onGroupExpand() " + clickedGroupPosition);
                for(int i = 0; i < expandableListViewAdapter.getGroupCount(); i++) {
                    if(i != clickedGroupPosition) {
                        // Close all other groups
                        boolean collapseGroup = elvPlans.collapseGroup(i);
                        if (collapseGroup){
                            Log.d("### APP", "onGroupExpand() clickedGroupPosition: " + i + " > collapseGroup() ");
                        } else {
                            Log.d("### APP", "onGroupExpand() clickedGroupPosition: " + i + " > collapseGroup() > already collapsed");
                        }
                    }

                }
            }
        });
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
            Log.d("### APP", "RESULT_CODE_PLAN_SUBMITTED > initExpandableListView()");
        }
    }

    private BreathPlan createSamplePlan (){
        BreathPlan bp = new BreathPlan(1, getString(R.string.plan_manager_sample_plan), true, BreathPlan.INTENSITY_MEDIUM, BreathPlan.MIN_BREATHS_PER_MINUTE * 3, 5, true);
        return  bp;
    }

    public static BreathPlan getBreathPlan(int planId) {
        return breathPlans.get(planId);
    }

    public static boolean setBreathPlan(int planId, BreathPlan breathPlan) {
        try {
            PlanManager.breathPlans.set(planId - 1, breathPlan);
        } catch ( IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
    public static void addBreathPlan(BreathPlan breathPlan) {
        breathPlans.add(breathPlan);
    }
    private void activateBreathPlan(int planId) {
        for (BreathPlan bp : breathPlans) {
            if (bp.getPlanId() == planId) {
                bp.setActivated(true);
            } else {
                bp.setActivated(false);
            }
        }
        initExpandableListView();
    }
    private void deleteBreathPlan(int planId) {
        boolean activatedPlanDeleted = false;
        for (BreathPlan bp : breathPlans) {
            if (bp.getPlanId() == planId) {
                if (bp.isActivated()){
                    activatedPlanDeleted = true;
                }
                breathPlans.remove(bp);
                break;
            }
        }

        if (breathPlans.size() == 0) {
            breathPlans.add(createSamplePlan());
        } else {
            reorganizePlanIds(activatedPlanDeleted);
        }

        initExpandableListView();
    }

    private View.OnClickListener getActivateButtonOnClickListener(){
        View.OnClickListener activateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BreathPlan bp : breathPlans) {
                    if (bp.getPlanId() + PlanManager.ID_PLACEHOLDER == v.getId()){
                        activateBreathPlan(bp.getPlanId());
                        break;
                    }
                }
            }
        };
        return activateListener;
    }
    private View.OnClickListener getEditButtonOnClickListener(){
        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BreathPlan bp : breathPlans) {
                    if (bp.getPlanId() + PlanManager.ID_PLACEHOLDER == v.getId()){
                        startCreatePlanActivity(false, bp.getPlanId());
                        break;
                    }
                }
            }
        };
        return editListener;
    }
    private View.OnClickListener getDeleteButtonOnClickListener(){
        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BreathPlan bp : breathPlans) {
                    if (bp.getPlanId() + PlanManager.ID_PLACEHOLDER == v.getId()){
                        deleteBreathPlan(bp.getPlanId());
                        break;
                    }
                }
            }
        };
        return deleteListener;
    }

    private void reorganizePlanIds(boolean activatedPlanDeleted){
        for (int i = 0; i < breathPlans.size(); i++) {
            BreathPlan bp = breathPlans.get(i);
            bp.setPlanId(i + 1);
            if (activatedPlanDeleted && i == 0){ // Set First Plan as active
                bp.setActivated(true);
            }
            PlanManager.breathPlans.set(i, bp);
        }
    }
}
