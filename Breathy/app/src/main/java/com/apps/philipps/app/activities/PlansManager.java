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
        initExpandableListView();
    }
    private void initExpandableListView() {
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(this,
                PlanManager.getPlans(), active(), edit(), delete());

        ExpandableListView elvPlans = (ExpandableListView) findViewById(R.id.elvPlans);
        elvPlans.setAdapter(expandableListViewAdapter);

//        elvPlans.setOnGroupClickListener((parent, v, clickedGroupPosition, id) -> {
//            boolean r = false;
//            if (!parent.isGroupExpanded(clickedGroupPosition)) {
//                Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition);
//                r = parent.expandGroup(clickedGroupPosition);
//            } else {
//                Log.d("### APP", "onGroupClick() " + " expandGroup() " + clickedGroupPosition + " > already expanded!");
//            }
//            return r;
//        });
//        elvPlans.setOnGroupExpandListener(clickedGroupPosition -> {
//            Log.d("### APP", "onGroupExpand() " + clickedGroupPosition);
//            for(int i = 0; i < expandableListViewAdapter.getGroupCount(); i++) {
//                if(i != clickedGroupPosition) {
//                    // Close all other groups
//                    boolean collapseGroup = elvPlans.collapseGroup(i);
//                    if (collapseGroup){
//                        Log.d("### APP", "onGroupExpand() clickedGroupPosition: " + i + " > collapseGroup() ");
//                    } else {
//                        Log.d("### APP", "onGroupExpand() clickedGroupPosition: " + i + " > collapseGroup() > already collapsed");
//                    }
//                }
//
//            }
//        });
    }

    private View.OnClickListener active(){
        return v -> PlanManager.setActive(v.getId());
    }
    private View.OnClickListener edit(){
        return v -> {
            Intent i = new Intent(this, CreatePlan.class);
            i.putExtra("plan", PlanManager.getPlan(v.getId()));
            startActivity(i);
        };
    }
    private View.OnClickListener delete(){
        return v -> PlanManager.deletePlan(v.getId());
    }
}
