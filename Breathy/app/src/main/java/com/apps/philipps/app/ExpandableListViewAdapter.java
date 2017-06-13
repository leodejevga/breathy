package com.apps.philipps.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.philipps.source.PlanManager;

import java.util.List;

/**
 * Created by Var on 13.05.2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter{

    private Context context;
    public static int selected = -1;
    private List<PlanManager.Part> data;
    private LinearLayout childLayout;
    private LinearLayout parentLayout;

    private View.OnClickListener activateButtonOnClickListener;
    private View.OnClickListener editButtonOnClickListener;
    private View.OnClickListener deleteButtonOnClickListener;


    public ExpandableListViewAdapter(Context context, List<PlanManager.Part> data,
                                     View.OnClickListener activateButtonOnClickListener, View.OnClickListener editButtonOnClickListener, View.OnClickListener deleteButtonOnClickListener){
        this.context = context;
        this.data = data;
        this.activateButtonOnClickListener = activateButtonOnClickListener;
        this.editButtonOnClickListener = editButtonOnClickListener;
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;

        childLayout = new LinearLayout(context);
        childLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLayout.setBackgroundColor(context.getResources().getColor(R.color.background));

        buildExpandableListItems();
    }

    private void buildExpandableListItems(){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 0, 0, 0);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        Button btnEditPlan = new Button(context);

        parentLayout = new LinearLayout(context);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView txtPlanName = new TextView(context);
        parentLayout.addView(txtPlanName);

        if (activateButtonOnClickListener!=null){
            Button btnActivatePlan = new Button(context);
            btnActivatePlan.setLayoutParams(buttonLayoutParams);
            btnActivatePlan.setText(context.getString(R.string.activate));
            btnActivatePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnPlus));
            btnActivatePlan.setOnClickListener(activateButtonOnClickListener);

            childLayout.addView(btnActivatePlan);
        }
        btnEditPlan.setLayoutParams(buttonLayoutParams);
        btnEditPlan.setText(context.getString(R.string.edit));
        btnEditPlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
        btnEditPlan.setOnClickListener(editButtonOnClickListener);
        childLayout.addView(btnEditPlan);

        Button btnDeletePlan = new Button(context);
        btnDeletePlan.setLayoutParams(buttonLayoutParams);
        btnDeletePlan.setText(context.getString(R.string.delete));
        btnDeletePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnMinus));
        btnDeletePlan.setOnClickListener(deleteButtonOnClickListener);
        childLayout.addView(btnDeletePlan);
    }



    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition);
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
        return true;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        selected = groupPosition;
        TextView view = (TextView) parentLayout.getChildAt(0);
        view.setText(data.get(groupPosition).getName());
        return parentLayout;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        selected = groupPosition;
        if(groupPosition == PlanManager.getCurrentPlan())
            childLayout.getChildAt(0).setEnabled(false);
        else childLayout.getChildAt(0).setEnabled(true);
        return childLayout;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
