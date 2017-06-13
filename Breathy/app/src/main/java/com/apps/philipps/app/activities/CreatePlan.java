package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.SaveData;

public class CreatePlan extends AppCompatActivity {
    private boolean create;
    private PlanManager.Plan plan;
    private ExpandableListView planParts;
    private FloatingActionButton addPart;
    private EditText planName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        plan = PlanManager.getPlan(getIntent().getIntExtra("plan", -1));
        create = plan == null;
        if(create)
            PlanManager.addPlan(plan = new PlanManager.Plan());
        init();
    }

    private void init(){
        planParts = (ExpandableListView) findViewById(R.id.elvPlanParts);
        planName = (EditText) findViewById(R.id.planName);

        plan.setName(planName.getText().toString());
        planName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                plan.setName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        addPart = (FloatingActionButton) findViewById(R.id.fabAddPlanPart);
        addPart.setOnClickListener(v -> {
            Intent i = new Intent(this, CreatePlanPart.class);
            i.putExtra("planId", plan.getId());
            startActivity(i);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this,
                plan.getParts(), null, edit(), delete());
        planParts.setAdapter(expandableListViewAdapter);
    }

    private View.OnClickListener edit(){
        return v -> {
            Intent i = new Intent(this, CreatePlanPart.class);
            i.putExtra("planPart", ExpandableListViewAdapter.selected);
            i.putExtra("planId", plan.getId());
            startActivity(i);
        };
    }

    private View.OnClickListener delete(){
        return v -> {
            plan.removeOption(ExpandableListViewAdapter.selected); onResume();
        };
    }
}
