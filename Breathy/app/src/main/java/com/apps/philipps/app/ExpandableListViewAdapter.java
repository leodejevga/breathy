package com.apps.philipps.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Var on 13.05.2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter{

    private Context context;

    private ArrayList<LinearLayout> expandableListParents;
    private ArrayList<LinearLayout> expandableListChildren;

    public ExpandableListViewAdapter(Context context, ArrayList<BreathPlan> planData){
        this.context = context;

        expandableListParents = new ArrayList<>();
        expandableListChildren = new ArrayList<>();

        buildExpandableListItems(planData);
    }

    private void updatePlanDataAndViews(ArrayList<BreathPlan> planData){

    }

    private void buildExpandableListItems(ArrayList<BreathPlan> planData){
        // remove all Items
        expandableListParents.clear();
        expandableListChildren.clear();

        for (BreathPlan bp: planData) {
            LinearLayout parentLayout = new LinearLayout(context);
            parentLayout.setOrientation(LinearLayout.HORIZONTAL);

            CheckBox cbPlanActivated = new CheckBox(context);
            cbPlanActivated.setChecked(bp.isActivated());
            cbPlanActivated.setClickable(false);
            parentLayout.addView(cbPlanActivated);

            TextView txtPlanName = new TextView(context);
            txtPlanName.setText(bp.getName());
            parentLayout.addView(txtPlanName);

            expandableListParents.add(parentLayout);



            LinearLayout childLayout = new LinearLayout(context);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

            Button btnActivatePlan = new Button(context);
            btnActivatePlan.setLayoutParams(buttonLayoutParams);
            btnActivatePlan.setText(context.getString(R.string.activate));
            btnActivatePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnPlus));
            childLayout.addView(btnActivatePlan);

            Button btnEditPlan = new Button(context);
            btnEditPlan.setLayoutParams(buttonLayoutParams);
            btnEditPlan.setText(context.getString(R.string.edit));
            btnEditPlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
            childLayout.addView(btnEditPlan);

            Button btnDeletePlan = new Button(context);
            btnDeletePlan.setLayoutParams(buttonLayoutParams);
            btnDeletePlan.setText(context.getString(R.string.delete));
            btnDeletePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnMinus));
            childLayout.addView(btnDeletePlan);

            expandableListChildren.add(childLayout);

        }
    }


    @Override
    public int getGroupCount() {
        return expandableListParents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListParents.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListChildren.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return expandableListParents.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return expandableListChildren.get(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
