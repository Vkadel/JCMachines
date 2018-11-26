package com.example.virginia.jcmachines;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
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
    private static final String ARG_ITEM_ROOT_VIEW ="root_view" ;
    List<machine> machineList;
    machine thisMachine;
    machineViewModel machineViewModel;
    View rootView;
    CollapsingToolbarLayout appBarLayout;
    ImageView machineDetailAppBarBackgroundIV;
    int thisMachineId;
    Activity activity;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public machineDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //Subscribe to the activity model
            machineViewModel=ViewModelProviders.of(this).get(machineViewModel.class);

            if (savedInstanceState==null){
                thisMachineId= Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            //observe the model
            machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                @Override
                public void onChanged(@Nullable List<machine> machines) {
                    machineList=machines;
                    thisMachine=machineList.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));
                    updateUI(rootView);
                }
            });}
            else{
                machineList=machineViewModel.getMachines().getValue();
                thisMachine=machineList.get(savedInstanceState.getInt(ARG_ITEM_ID));
                thisMachineId=savedInstanceState.getInt(ARG_ITEM_ID);
            }
            activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.machine_detail, container, false);

        // Show the selected machine content as text in a TextView.
        if (thisMachine!=null) {
           updateUI(rootView);
        }
        return rootView;
    }

    public void updateUI(View rootView) {
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        machineDetailAppBarBackgroundIV=(ImageView)activity.findViewById(R.id.app_bar_machine_image);
        ((TextView) rootView.findViewById(R.id.machine_detail)).setText(thisMachine.getDescription());
        appBarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        appBarLayout.setTitle(thisMachine.getMachineFullName());
        Glide.with(this).load(thisMachine.getLargeImageOne()).into(machineDetailAppBarBackgroundIV);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_ITEM_ID,thisMachine.getId());
        outState.putInt(ARG_ITEM_ROOT_VIEW,rootView.getId());
        super.onSaveInstanceState(outState);
    }
}
