package com.apps.philipps.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.philipps.source.PlanManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Var on 13.05.2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    public static int selected = -1;
    private List<LinearLayout> childLayout;
    private List<LinearLayout> parentLayout;

    private View.OnClickListener active;
    private View.OnClickListener edit;
    private View.OnClickListener delete;
    private int planId;


    public ExpandableListViewAdapter(Context context,
                                     View.OnClickListener active, View.OnClickListener edit, View.OnClickListener delete, int... planId) {
        this.context = context;
        this.active = active;
        this.edit = edit;
        this.planId = planId.length == 0 ? -1 : planId[0];
        this.delete = delete;

        childLayout = new ArrayList<>();
        parentLayout = new ArrayList<>();

        buildExpandableListItems();
    }

    private void buildExpandableListItems() {

        if (planId >= 0)
            for (PlanManager.Plan.Option option : PlanManager.getOptions(planId)) {
                createElement(option);
            }
        else {
            for (PlanManager.Plan plan : PlanManager.getPlans()) {
                createElement(plan);
            }
        }
    }

    private void createElement(PlanManager.Part part) {

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout child = new LinearLayout(context);
        LinearLayout parent = new LinearLayout(context);

        if (active != null) {
            Button btnActivatePlan = new Button(context);
            btnActivatePlan.setLayoutParams(buttonLayoutParams);
            btnActivatePlan.setText(context.getString(R.string.activate));
            btnActivatePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnPlus));
            btnActivatePlan.setOnClickListener(active);
            btnActivatePlan.setEnabled(!PlanManager.isActive(part.getId()));
            child.addView(btnActivatePlan);
        }
        if (edit != null) {
            Button btnEditPlan = new Button(context);
            btnEditPlan.setLayoutParams(buttonLayoutParams);
            btnEditPlan.setText(context.getString(R.string.edit));
            btnEditPlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
            btnEditPlan.setOnClickListener(edit);
            child.addView(btnEditPlan);
        }
        if (delete != null) {
            Button btnDeletePlan = new Button(context);
            btnDeletePlan.setLayoutParams(buttonLayoutParams);
            btnDeletePlan.setText(context.getString(R.string.delete));
            btnDeletePlan.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorBtnMinus));
            btnDeletePlan.setOnClickListener(delete);
            child.addView(btnDeletePlan);
        }
        TextView txtPlanName = new TextView(context);
        txtPlanName.setTextSize(18);
        txtPlanName.setPadding(0, 10, 0, 5);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.addView(txtPlanName);

        if (active != null) {
            txtPlanName.setText(part.getName());
            TextView txtPlanDesc = new TextView(context);
            txtPlanDesc.setTextSize(14);
            txtPlanDesc.setPadding(20, 0, 0, 0);
            txtPlanDesc.setText(((PlanManager.Plan) part).getDescription());
            parent.addView(txtPlanDesc);
        } else {
            PlanManager.Plan.Option p = PlanManager.getOption(planId, part.getId());
            txtPlanName.setText(p.getName());

        }
        parentLayout.add(parent);
        childLayout.add(child);
    }


    @Override
    public int getGroupCount() {
        return parentLayout.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        if (isExpanded)
            selected = groupPosition;
        return parentLayout.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return childLayout.get(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
