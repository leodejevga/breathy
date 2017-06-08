package com.apps.philipps.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.apps.philipps.app.ExpandableListViewAdapter;
import com.apps.philipps.app.R;
import com.apps.philipps.source.PlanManager;

import java.util.List;

public class PlansManager extends AppCompatActivity {

//    public static final String EXTRA_PLAN_ID = "#EXTRA_PLAN_ID";
//    public static final String EXTRA_REQUEST_CODE = "#EXTRA_REQUEST_CODE";
//    public static final int REQUEST_CODE_CREATE_PLAN = 1001;
//    public static final int REQUEST_CODE_EDIT_PLAN = 1002;
//    public static final int RESULT_CODE_PLAN_SUBMITTED = 2001;

//    public static final int ID_PLACEHOLDER = 10000;
    List<PlanManager.Plan> data;
    ArrayAdapter<PlanManager.Plan> plans;
    ListView planList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_manager);

        initGUIComponents();
    }

    private void initGUIComponents() {
        List<PlanManager.Plan> data = PlanManager.getPlans();
        if(data == null)
            return;
        FloatingActionButton fabAddPlan = (FloatingActionButton) findViewById(R.id.fabAddPlan);
        fabAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlansManager.this, CreatePlan.class);
                startActivity(i);
            }
        });
        initExpandableListView();
    }
    private void initExpandableListView() {
        plans = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlanManager.getPlans());
        planList.setAdapter(plans);


        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this, data, getActivateButtonOnClickListener(), getEditButtonOnClickListener(), getDeleteButtonOnClickListener());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);

        elvPlans.setOnGroupClickListener((parent, v, clickedGroupPosition, id) -> {
            boolean r = false;
            if (!parent.isGroupExpanded(clickedGroupPosition)) {
                Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition);
                r = parent.expandGroup(clickedGroupPosition);
            } else {
                Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition + " > already expanded!");
            }
            return r;
        });
        elvPlans.setOnGroupExpandListener(clickedGroupPosition -> {
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
        });
    }

    private View.OnClickListener getActivateButtonOnClickListener(){
        return v -> PlanManager.setActive(v.getId());
    }
    private View.OnClickListener getEditButtonOnClickListener(){
        return v -> {
            Intent i = new Intent(this, CreatePlan.class);
            i.putExtra("plan", PlanManager.getPlan(v.getId()));
            startActivity(i);
        };
    }
    private View.OnClickListener getDeleteButtonOnClickListener(){
        return v -> PlanManager.deletePlan(v.getId());
    }
}
