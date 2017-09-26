package com.apps.philipps.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.SaveData;

import javax.security.auth.login.LoginException;

public class PlansManager extends AppCompatActivity {

    private static final String TAG = "PLANS_MANAGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_manager);

        initGUIComponents();
    }

    private void initGUIComponents() {

        FloatingActionButton fabAddPlan = (FloatingActionButton) findViewById(R.id.fabAddPlan);
        fabAddPlan.setOnClickListener(v -> {
            Intent i = new Intent(PlansManager.this, CreatePlan.class);
            startActivity(i);
        });
    }
    private void initExpandableListView() {
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this,
                active(), edit(), delete());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);
        elvPlans.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            private int prev = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(prev!=-1 && prev!=groupPosition)
                    elvPlans.collapseGroup(prev);
                prev = groupPosition;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(PlanManager.Plan plan : PlanManager.getPlans()){
            if(plan.getName()==null || plan.getName().length()==0)
                plan.setName("SpaceFight Plan");
        }
        initExpandableListView();
    }

    private View.OnClickListener active(){
        return v -> {
            PlanManager.setActive(ExpandableListViewAdapter.selected);
            initExpandableListView();
        };
    }
    private View.OnClickListener edit(){
        return v -> {
            Intent i = new Intent(this, CreatePlan.class);
            i.putExtra("plan", ExpandableListViewAdapter.selected);
            startActivity(i);
        };
    }
    private View.OnClickListener delete(){
        return v -> {
            PlanManager.deletePlan(ExpandableListViewAdapter.selected); initExpandableListView();
            SaveData.savePlanManager(this);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SaveData.savePlanManager(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveData.savePlanManager(this);
    }
}
