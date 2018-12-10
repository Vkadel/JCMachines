package com.example.virginia.jcmachines;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.color;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;
import com.example.virginia.jcmachines.dummy.DummyContent;

import java.util.List;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public machineDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    machineDetailFragment.this.machineList =machines;
                    machineDetailFragment.this.thisMachine = machineDetailFragment.this.machineList.get(machineDetailFragment.this.thisMachineId);
                    machineDetailFragment.this.updateUI(machineDetailFragment.this.rootView);
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

        TextView description_tv=rootView.findViewById(id.description_TV);
        TextView data_sheet_tv=rootView.findViewById(id.data_sheet_tv);
        description_tv.setText(this.thisMachine.getDescription());
        ImageView dimensions_tv_inline=rootView.findViewById(id.inline_dimensions_image);

        //Hide Image View for dimensios if there is no picture for it
        if (this.thisMachine.getInlIneInstallImage()!="na"){
            dimensions_tv_inline.setVisibility(View.VISIBLE);
        Glide.with(this).load(this.thisMachine.getInlIneInstallImage()).into(dimensions_tv_inline);}
        else{
            dimensions_tv_inline.setVisibility(View.GONE);
        }

        //onClick listeners for pdf files
        data_sheet_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Pdf_viewer.class);
                intent.putExtra(Pdf_viewer.ARG_LINK, String.valueOf(thisMachine.getDatasheetLink()));
                context.startActivity(intent);
            }
        });
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
