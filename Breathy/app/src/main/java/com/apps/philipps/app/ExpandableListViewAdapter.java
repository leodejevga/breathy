package com.apps.philipps.app;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.apps.philipps.app.activities.PlanManager;

import java.util.ArrayList;

/**
 * Created by Var on 13.05.2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter{

    private Context context;

    private ArrayList<LinearLayout> expandableListParents;
    private ArrayList<LinearLayout> expandableListChildren;

    private View.OnClickListener activateButtonOnClickListener;
    private View.OnClickListener editButtonOnClickListener;
    private View.OnClickListener deleteButtonOnClickListener;


    public ExpandableListViewAdapter(Context context, ArrayList<BreathPlan> planData,
                                     View.OnClickListener activateButtonOnClickListener, View.OnClickListener editButtonOnClickListener, View.OnClickListener deleteButtonOnClickListener){
        this.context = context;

        this.activateButtonOnClickListener = activateButtonOnClickListener;
        this.editButtonOnClickListener = editButtonOnClickListener;
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;

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
            cbPlanActivated.setFocusable(false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40, 0, 0, 0);

            cbPlanActivated.setLayoutParams(layoutParams);

            parentLayout.addView(cbPlanActivated);

            TextView txtPlanName = new TextView(context);
            txtPlanName.setText(bp.getName());
            parentLayout.addView(txtPlanName);

            expandableListParents.add(parentLayout);


            LinearLayout childLayout = new LinearLayout(context);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);
            childLayout.setBackgroundColor(context.getResources().getColor(R.color.background));

            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

            Button btnActivatePlan = new Button(context);
            btnActivatePlan.setId(PlanManager.ID_PLACEHOLDER + bp.getPlanId());
            btnActivatePlan.setLayoutParams(buttonLayoutParams);
            btnActivatePlan.setText(context.getString(R.string.activate));
            btnActivatePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnPlus));
            if (bp.isActivated()){
                btnActivatePlan.setEnabled(false);
            }
            btnActivatePlan.setOnClickListener(activateButtonOnClickListener);
            childLayout.addView(btnActivatePlan);

            Button btnEditPlan = new Button(context);
            btnEditPlan.setId(PlanManager.ID_PLACEHOLDER + bp.getPlanId());
            btnEditPlan.setLayoutParams(buttonLayoutParams);
            btnEditPlan.setText(context.getString(R.string.edit));
            btnEditPlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
            btnEditPlan.setOnClickListener(editButtonOnClickListener);
            childLayout.addView(btnEditPlan);

            Button btnDeletePlan = new Button(context);
            btnDeletePlan.setId(PlanManager.ID_PLACEHOLDER + bp.getPlanId());
            btnDeletePlan.setLayoutParams(buttonLayoutParams);
            btnDeletePlan.setText(context.getString(R.string.delete));
            btnDeletePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnMinus));
            btnDeletePlan.setOnClickListener(deleteButtonOnClickListener);
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
        // TODO
        return expandableListChildren.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return expandableListParents.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("### APP", "getChild()  groupPosition: " + groupPosition + " childPosition: " + childPosition);
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
        Log.d("### APP", "getGroupView()  groupPosition: " + groupPosition);
        return expandableListParents.get(groupPosition);
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.d("### APP", "getChildView()  groupPosition: " + groupPosition + " childPosition: " + childPosition);
        if (groupPosition != childPosition) {
            return new Space(context);
        } else {
            return expandableListChildren.get(0);
        }
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
