package com.example.virginia.jcmachines;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.color;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;

import java.util.List;

import timber.log.Timber;

/**
 * A fragment representing a single machine detail screen.
 * This fragment is either contained in a {@link machineListActivity}
 * in two-pane mode (on tablets) or a {@link machineDetailActivity}
 * on handsets.
 */
public class machineDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_CAME_FROM_WIDGET="came_from_widget";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String ARG_ITEM_ROOT_VIEW ="root_view";
    public static final String ARG_IS_TWO_PANE ="is_two_pane";
    List<machine> machineList;
    machine thisMachine;
    machineViewModel machineViewModel;
    View rootView;
    CollapsingToolbarLayout appBarLayout;
    ImageView machineDetailAppBarBackgroundIV;
    int thisMachineId;
    Activity activity;
    Boolean isTwopane;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public machineDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        if (this.getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
            //Subscribe to the activity model
            this.machineViewModel =ViewModelProviders.of(this).get(machineViewModel.class);
            //Check if this is the first the fragment was created
            if (savedInstanceState==null){
                this.thisMachineId = Integer.valueOf(this.getArguments().getString(machineDetailFragment.ARG_ITEM_ID));
                this.isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
            //observe the model
                this.machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                @Override
                public void onChanged(@Nullable List<machine> machines) {
                    machineList =machines;
                    thisMachine = machineList.get(thisMachineId);
                    updateUI(rootView);
                }
            });}
            //get the existing Model and get all machines
            else{
                this.machineList = this.machineViewModel.getMachines().getValue();
                this.thisMachineId =savedInstanceState.getInt(machineDetailFragment.ARG_ITEM_ID);
                this.thisMachine = this.machineList.get(this.thisMachineId);
                this.isTwopane =savedInstanceState.getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
            }
            this.activity = getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(layout.machine_detail,container, false);
        context=getContext();
        // Show the selected machine content as text in a TextView.
        if (this.thisMachine !=null) {
            this.updateUI(this.rootView);
        }
        return this.rootView;
    }

    public void updateUI(View rootView) {
        if(!this.isTwopane){
            this.appBarLayout = this.activity.findViewById(id.toolbar_layout);
            this.machineDetailAppBarBackgroundIV = this.activity.findViewById(id.app_bar_machine_image);
            this.appBarLayout.setExpandedTitleColor(this.getResources().getColor(color.colorAccent));
            this.appBarLayout.setTitle(this.thisMachine.getMachineFullName());
            Glide.with(this).load(this.thisMachine.getLargeImageOne()).into(this.machineDetailAppBarBackgroundIV);
        }

        TextView description_tv=rootView.findViewById(R.id.description_TV);
        TextView data_sheet_tv=rootView.findViewById(id.data_sheet_tv);
        TextView lubrication_chart_tv=rootView.findViewById(id.lubrication_chart_tv);
        TextView spare_parts_list_tv=rootView.findViewById(id.spare_parts_tv);
        final Button isThisAWidget=rootView.findViewById(id.make_widget);

        isThisAWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("checked the box");
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().
                        getString(R.string.my_machine_to_widget_key),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.my_machine_to_widget_key),String.valueOf(thisMachine.getId()));
                editor.putString(getString(R.string.my_machine_name_for_widget_key),thisMachine.getMachineFullName());
                editor.putString(getString(R.string.my_machine_pic_link_for_widget_key),thisMachine.getThumbnailImage());
                editor.commit();
                Toast.makeText(context,getActivity().getResources().getString(R.string.updating_your_widget_with)
                        +thisMachine.getMachineFullName(),Toast.LENGTH_SHORT).show();
                //Calling a widget Update manually
                int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
                jcSteeleMachineWidget myWidget = new jcSteeleMachineWidget();
                myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);
            }
        });

        description_tv.setText(this.thisMachine.getDescription());
        ImageView dimensions_tv_inline=rootView.findViewById(id.inline_dimensions_image);

        //Hide Image View for dimensios if there is no picture for it
        if (!(this.thisMachine.getInlIneInstallImage()).equals("na")){
            dimensions_tv_inline.setVisibility(View.VISIBLE);
        Glide.with(this).load(this.thisMachine.getInlIneInstallImage()).into(dimensions_tv_inline);}
        else{
            dimensions_tv_inline.setVisibility(View.GONE);
        }

        //onClick listeners for pdf files. Hide the View if file is not available

        if (!(this.thisMachine.getDatasheetLink()).equals("na")){
            lubrication_chart_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Pdf_viewer_Activity.class);
                intent.putExtra(Pdf_viewer_Activity.ARG_LINK, String.valueOf(thisMachine.getLubricationChartLink()));
                context.startActivity(intent);
            }
        });}else{
            lubrication_chart_tv.setVisibility(View.GONE);
        }

        //Creating intent to go to activity displaying list of SpareParts for this machine
        if ((this.thisMachine.getSpareParts()!=null)){
            spare_parts_list_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SparePartListActivity.class);
                    intent.putExtra(SparePartListActivity.ARG_ITEM_ID,thisMachineId);
                    context.startActivity(intent);
                }
            });}else{
            spare_parts_list_tv.setVisibility(View.GONE);
        }

        if (!(this.thisMachine.getDatasheetLink()).equals("na")){
            data_sheet_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Pdf_viewer_Activity.class);
                    intent.putExtra(Pdf_viewer_Activity.ARG_LINK, String.valueOf(thisMachine.getDatasheetLink()));
                    context.startActivity(intent);
                }
            });}else{
            data_sheet_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(machineDetailFragment.ARG_ITEM_ID, this.thisMachine.getId());
        super.onSaveInstanceState(outState);
    }
}
