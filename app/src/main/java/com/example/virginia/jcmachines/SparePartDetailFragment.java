package com.example.virginia.jcmachines;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import com.example.virginia.jcmachines.Data.spareParts;
import com.example.virginia.jcmachines.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a single SparePart detail screen.
 * This fragment is either contained in a {@link SparePartListActivity}
 * in two-pane mode (on tablets) or a {@link SparePartDetailActivity}
 * on handsets.
 */
public class SparePartDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    List<machine> machineList;
    machine thisMachine;
    machineViewModel machineViewModel;
    Activity activity;
    private spareParts mItem;
    int thisMachineId;
    Boolean isTwopane;
    CollapsingToolbarLayout appBarLayout;
    View rootView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SparePartDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            if (this.getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
                //Subscribe to the activity model
                this.machineViewModel =ViewModelProviders.of(this).get(machineViewModel.class);
                //Check if this is the first the fragment was created
                if (savedInstanceState==null){
                    thisMachineId = Integer.valueOf(this.getArguments().getString(machineDetailFragment.ARG_ITEM_ID));
                    isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
                    //observe the model
                    this.machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                        @Override
                        public void onChanged(@Nullable List<machine> machines) {
                            machineList =machines;
                            thisMachine = SparePartDetailFragment.this.machineList.get(SparePartDetailFragment.this.thisMachineId);
                            updateUI(rootView);
                        }
                    });}

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(thisMachine.getMachineFullName());
            }
            }
        }
    }

    public void updateUI(View rootView) {
        if(!this.isTwopane){
            this.appBarLayout = this.activity.findViewById(R.id.toolbar_layout);
            this.appBarLayout.setExpandedTitleColor(this.getResources().getColor(R.color.colorAccent));
            this.appBarLayout.setTitle(this.thisMachine.getMachineFullName());

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sparepart_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.sparepart_detail)).setText(thisMachine.getMachineFullName());
        }

        return rootView;
    }
}
