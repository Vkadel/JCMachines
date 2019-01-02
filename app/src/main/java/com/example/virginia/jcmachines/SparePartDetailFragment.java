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
    String thisMachineId;
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
        outState.putString(SparePartDetailFragment.ARG_ITEM_ID, thisMachineId);
        outState.putString(SparePartDetailFragment.ARG_SPARE_ITEM_ID,getArguments().getString(SparePartDetailFragment.ARG_SPARE_ITEM_ID));
        outState.putBoolean(SparePartDetailFragment.ARG_IS_TWO_PANE,isTwopane);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)||getArguments().containsKey(ARG_SPARE_ITEM_ID)) {
            //Subscribe to the activity model
            this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
                //Get the variables to identify the Machine and the part
                thisMachineId = this.getArguments().getString(SparePartDetailFragment.ARG_ITEM_ID);
                thisSparePartID=Integer.valueOf(this.getArguments().getString(ARG_SPARE_ITEM_ID));
                isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
                //Check if this is the first the fragment was created
                if (savedInstanceState == null) {
                    //observe the model
                    this.machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                        @Override
                        public void onChanged(@Nullable List<machine> machines) {
                            machineList = machines;
                            sparePartsList=machines.get(Integer.valueOf(thisMachineId)).getSpareParts();
                            thisSparePart =sparePartsList.get(thisSparePartID);
                            if (rootView!=null){
                            updateUI(rootView);}
                        }
                    });
                }
                else{
                    if (savedInstanceState.containsKey(SparePartDetailFragment.ARG_ITEM_ID)){
                    machineList = this.machineViewModel.getMachines().getValue();
                    thisMachineId =savedInstanceState.getString(SparePartDetailFragment.ARG_ITEM_ID);
                    thisMachine = machineList.get(Integer.valueOf(thisMachineId));
                    sparePartsList=thisMachine.getSpareParts();
                    thisSparePart=sparePartsList.get(Integer.valueOf(savedInstanceState.getString(ARG_SPARE_ITEM_ID,"0")));
                    isTwopane =savedInstanceState.getBoolean(SparePartDetailFragment.ARG_IS_TWO_PANE);
                }
                }
                activity = this.getActivity();


        }
    }

    public void updateUI(View rootView) {

        sparePartDescTV=rootView.findViewById(R.id.spare_part_description_tv);
        SparePartSectionTV=rootView.findViewById(R.id.spare_part_section_tv);
        SparePartNameTV=rootView.findViewById(R.id.spare_part_name_tv);
        if (thisSparePart != null) {
        //if We are looking at small screens. Show spare part info
            if (!isTwopane) {
            appBarLayout = this.activity.findViewById(R.id.toolbar_layout);
            appBarLayout.setExpandedTitleColor(this.getResources().getColor(R.color.colorAccent,null));
            appBarLayout.setTitle(thisSparePart.getName());
                SparePartIV=appBarLayout.findViewById(R.id.spare_part_bar_iv);
            CheckifNaImage(SparePartIV,thisSparePart.getImageLink());
                //if this is a SMALL screen hide name, because it's shown on bar
                SparePartNameTV.setVisibility(View.GONE);

        }else{
                //if this is a LARGE screen place the name in the Textview inside the fragment
            CheckifNaText(SparePartNameTV,thisSparePart.getName());
        }
            //For all Add description and Section
            CheckifNaText(sparePartDescTV,thisSparePart.getDescription());
            CheckifNaText(SparePartSectionTV,thisSparePart.getSection());
        }
    }

    //Helper Methods
    public void CheckifNaText(TextView view, String stringToCheck){
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
        if (thisSparePart !=null) {
            this.updateUI(this.rootView);
        }
        return rootView;
    }


}
