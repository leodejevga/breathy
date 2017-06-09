package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;

public class CreatePlan extends AppCompatActivity {
    PlanManager.Plan plan;
    ExpandableListView planParts;
    FloatingActionButton addPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        plan = (PlanManager.Plan) getIntent().getSerializableExtra("plan");
        plan.getStrengthOut();
        if(plan == null)
            finish();
        else init();
    }

    private void init(){
        planParts = (ExpandableListView) findViewById(R.id.elvPlanParts);
        addPart = (FloatingActionButton) findViewById(R.id.fabAddPlanPart);

        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this,
                plan.getOptions(), null, edit(), delete());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);
    }

    private View.OnClickListener edit(){
        return v -> {
            Intent i = new Intent(this, CreatePlanPart.class);
            i.putExtra("planPart", plan.getOption(v.getId()));
            startActivity(i);
        };
    }

    private View.OnClickListener delete(){
        return v -> plan.removeOption(v.getId());
    }
}
