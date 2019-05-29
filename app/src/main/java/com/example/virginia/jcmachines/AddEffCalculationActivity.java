package com.example.virginia.jcmachines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.databinding.ActivityAddEffCalculationBinding;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;

public class AddEffCalculationActivity extends AppCompatActivity {
    CollapsingToolbarLayout mCollapsingBar;
    ActivityAddEffCalculationBinding binding;
    AddEffCalculationActivityFragment fragment;
    Toolbar toolbar;
    String thisItemID;
    private static machineViewModel viewModel;
    private static machine thismachine;
    private String machineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_eff_calculation);
        binding.setLifecycleOwner(this);
        binding.effCalculationCollapsingBarlayuout.setTitle(getResources().getString(R.string.efficiency_calculator_activity_name));
        binding.effCalculationCollapsingBarlayuout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent, getTheme()));
        setSupportActionBar(binding.calcEffToolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Create and Attach the fragment
        Bundle arguments = new Bundle();
        thisItemID = this.getIntent().getStringExtra(machineDetailFragment.ARG_ITEM_ID);
        arguments.putString(machineDetailFragment.ARG_ITEM_ID, thisItemID);
        if (savedInstanceState == null) {
            machineViewModel viewModel = ViewModelProviders.of(this).get(machineViewModel.class);
            viewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                @Override
                public void onChanged(@Nullable PagedList<machine> machines) {
                    if (machines != null) {
                        thismachine = machines.get(Integer.parseInt(thisItemID));
                        setupUIStart();
                    }
                }
            });
            fragment = new AddEffCalculationActivityFragment();
            fragment.setArguments(arguments);

            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.form_container, fragment, "tag")
                    .commit();

        } else if (savedInstanceState.containsKey(machineDetailFragment.ARG_ITEM_ID)) {
            thisItemID = savedInstanceState.getString(machineDetailFragment.ARG_ITEM_ID);
            arguments.putString(machineDetailFragment.ARG_ITEM_ID, thisItemID);
            fragment = new AddEffCalculationActivityFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.form_container, fragment)
                    .commit();

        }

    }

    private void setupUIStart() {
        Glide.with(this).load(thismachine.getLargeImageOne()).into(binding.appBarMachineImage);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(machineDetailFragment.ARG_ITEM_ID, thisItemID);
        super.onSaveInstanceState(outState);
    }
}
