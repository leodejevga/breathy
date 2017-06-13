package com.apps.philipps.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;

public class PlansManager extends AppCompatActivity {

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
//        initExpandableListView();
    }
    private void initExpandableListView() {
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this,
                PlanManager.getParts(), active(), edit(), delete());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initExpandableListView();
    }

    private View.OnClickListener active(){
        return v -> { PlanManager.setActive(v.getId()); initExpandableListView(); };
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
        };
    }
}
