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
import com.example.virginia.jcmachines.Data.spareParts;

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
    public static final String ARG_SPARE_ITEM_ID = "spare_item_id";
    public static final String ARG_IS_TWO_PANE ="is_two_pane";
    List<machine> machineList;
    machine thisMachine;
    List<spareParts> sparePartsList;
    spareParts thisSparePart;
    machineViewModel machineViewModel;
    Activity activity;
    int thisMachineId;
    int thisSparePartID;
    Boolean isTwopane;
    CollapsingToolbarLayout appBarLayout;
    View rootView;
    TextView sparePartDescTV;
    TextView SparePartSectionTV;
    TextView SparePartNameTV;
    ImageView SparePartIV;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SparePartDetailFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SparePartDetailFragment.ARG_ITEM_ID, this.thisMachine.getId());
        outState.putString(SparePartDetailFragment.ARG_SPARE_ITEM_ID,getArguments().getString(SparePartDetailFragment.ARG_SPARE_ITEM_ID));
        outState.putBoolean(SparePartDetailFragment.ARG_IS_TWO_PANE,isTwopane);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            if (this.getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
                //Subscribe to the activity model
                this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
                //Check if this is the first the fragment was created
                if (savedInstanceState == null) {
                    thisMachineId = this.getArguments().getInt(ARG_ITEM_ID);
                    thisSparePartID=Integer.valueOf(this.getArguments().getString(ARG_SPARE_ITEM_ID));
                    isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
                    //observe the model
                    this.machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                        @Override
                        public void onChanged(@Nullable List<machine> machines) {
                            machineList = machines;
                            thisMachine = machineList.get(thisMachineId);
                            sparePartsList=thisMachine.getSpareParts();
                            thisSparePart =sparePartsList.get(thisSparePartID);
                            updateUI(rootView);
                        }
                    });
                }
                else{
                    machineList = this.machineViewModel.getMachines().getValue();
                    thisMachineId =savedInstanceState.getInt(SparePartDetailFragment.ARG_ITEM_ID);
                    thisMachine = machineList.get(thisMachineId);
                    sparePartsList=thisMachine.getSpareParts();
                    thisSparePart=sparePartsList.get(Integer.valueOf(savedInstanceState.getString(ARG_SPARE_ITEM_ID,"0")));
                    isTwopane =savedInstanceState.getBoolean(SparePartDetailFragment.ARG_IS_TWO_PANE);
                }
                activity = this.getActivity();
                appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            }
        }
    }

    public void updateUI(View rootView) {

        sparePartDescTV=rootView.findViewById(R.id.spare_part_description_tv);
        SparePartSectionTV=rootView.findViewById(R.id.spare_part_section_tv);
        SparePartNameTV=rootView.findViewById(R.id.spare_part_name_tv);
        if (thisSparePart != null) {
        //if We are looking at small screens. Show spare part info
            if (!isTwopane) {
                //this means we are in small devices
                SparePartIV=appBarLayout.findViewById(R.id.spare_part_bar_iv);
            this.appBarLayout = this.activity.findViewById(R.id.toolbar_layout);
            this.appBarLayout.setExpandedTitleColor(this.getResources().getColor(R.color.colorAccent));
            this.appBarLayout.setTitle(thisSparePart.getName());
            CheckifNaImage(SparePartIV,thisSparePart.getImageLink());
        }else{
                //if this is a LARGE screen place the name in the Textview inside the fragment
            CheckifNaText(SparePartNameTV,thisSparePart.getName());
        }
            //For all Add description and Section
            CheckifNaText(sparePartDescTV,thisSparePart.getDescription());
            CheckifNaText(SparePartSectionTV,thisSparePart.getSection());
        }
    }
    public void CheckifNaText(TextView view,String stringToCheck){
        if(stringToCheck.equals("na")){
            view.setVisibility(View.INVISIBLE);
        }else{
            view.setText(stringToCheck);
        }
    }
    public void CheckifNaImage(ImageView view,String stringToCheck){
        if(stringToCheck.equals("na")){
            view.setVisibility(View.INVISIBLE);
        }else{
            Glide.with(this).load(stringToCheck).into(view);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sparepart_detail, container, false);

        return rootView;
    }



}
