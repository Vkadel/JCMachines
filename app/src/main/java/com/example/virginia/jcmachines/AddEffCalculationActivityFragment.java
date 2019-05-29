package com.example.virginia.jcmachines;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.augers;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.animations.appearAnim;
import com.example.virginia.jcmachines.animations.fadeAnim;
import com.example.virginia.jcmachines.databinding.FragmentAddEffCalculationBinding;
import com.example.virginia.jcmachines.utils.SendALongToast;
import com.example.virginia.jcmachines.viewmodels.efficiencyFormulaViewModel;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.function.Consumer;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddEffCalculationActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    static FragmentAddEffCalculationBinding binding;
    static final String EFF_CALC_ARG = "eff_calc_arg";
    private static Context mContext;
    private static machineViewModel viewModel;
    private static machine thismachine;
    private PagedList<machine> mmachines;
    ArrayList<String> augerList = new ArrayList<>();
    ArrayList<String> augerValueList = new ArrayList<>();
    private static effcalculation meffcalculation = new effcalculation();
    private static final MyLiveEffCalculation meffcalculationLive = new MyLiveEffCalculation();
    private String machineId;
    private ImageView machineImageImage;
    private efficiencyFormulaViewModel viewModeleffcal;
    private String calculationId;


    public AddEffCalculationActivityFragment() {
        super();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.executePendingBindings();
        //Setting up the listener
        mContext = getActivity();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_eff_calculation, container, false);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        binding.setLivedata(meffcalculationLive);
        binding.setCalculation(new Callbacks());
        //Get sent machine information
        if (getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
            machineId = this.getArguments().getString(machineDetailFragment.ARG_ITEM_ID);
        } else {
            machineId = "0";
        }

        if (savedInstanceState == null) {
            //Subscribe to the machine model
            viewModel = ViewModelProviders.of(this).get(machineViewModel.class);
            viewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                @Override
                public void onChanged(@Nullable PagedList<machine> machines) {
                    thismachine = machines.get(Integer.parseInt(machineId));
                    initialUIsetup();
                }
            });

            //Observe the Livedata Item
            meffcalculationLive.observe(this, new Observer<effcalculation>() {
                @Override
                public void onChanged(@Nullable effcalculation effcalculation) {
                    checkAndCalculate();
                }
            });
            meffcalculation.setUserid(FirebaseAuth.getInstance().getUid());
            //Set Initial value
            meffcalculationLive.setValue(meffcalculation);

        } else {
            machineId = savedInstanceState.getString(machineDetailFragment.ARG_ITEM_ID);
            if (savedInstanceState.containsKey(EFF_CALC_ARG)) {
                //getting the exixting value
                Gson json = new Gson();
                meffcalculation = json.fromJson(savedInstanceState.getString(EFF_CALC_ARG), effcalculation.class);
                initialUIsetup();
            }
        }
        return view;
    }

    private void rePopViewsAfterConfigUpdate() {
        //Is imperial
        if (meffcalculation.getImp()) {
            changeToImperial();

        } else {
            //is metric if false
            changeToMetric();
        }
        binding.imperialMetricSwitch.setChecked(meffcalculation.getImp());
        int selection = augerValueList.indexOf(meffcalculation.getVd());
        binding.selectAugerSpinner.setSelection(selection);
    }

    private void initialUIsetup() {
        meffcalculationLive.setValue(meffcalculation);
        rePopViewsAfterConfigUpdate();
        binding.cutLengthLayout.setVisibility(View.GONE);
        binding.cutRateLayout.setVisibility(View.GONE);
        binding.estimationWasteLayout.setVisibility(View.GONE);
        binding.brickWidthLayout.setVisibility(View.GONE);
        binding.brickHeightLayout.setVisibility(View.GONE);
        binding.brickVoidPercentageLayout.setVisibility(View.GONE);
        ImageView machineImage = getActivity().findViewById(R.id.app_bar_machine_image);
        Glide.with(this).load(thismachine.getLargeImageOne()).into(machineImage);
        //setup spinner
        setUpspinner(binding.selectAugerSpinner);
    }

    private void setUpspinner(Spinner spinner) {
        augerList.clear();
        augerValueList.clear();
        thismachine.getAugers().forEach(new Consumer<augers>() {
            @Override
            public void accept(augers augers) {
                augerList.add(augers.getAnam());
                if (binding.imperialMetricSwitch.isChecked()) {
                    augerValueList.add(augers.getTvoli());
                } else {
                    augerValueList.add(augers.getTvolm());
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, augerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    private void has_cutter() {
        new SendALongToast(getActivity(), "Has Cutter").show();
        binding.userHasCutter.setText("System has cutter");

    }

    private void does_not_have_cutter() {
        new SendALongToast(getActivity(), "Does not have a cutter").show();
        binding.userHasCutter.setText(getResources().getString(R.string.system_does_not_have_cutter));
    }

    private void changeToImperial() {
        binding.imperialMetricSwitch.setText(getString(R.string.imperial));
        new SendALongToast(getActivity(), "Imperial").show();
        //TODO: Change all units to imperial/ May want to translate anything that is already there back and forth
        //Change all labels for the units
        binding.columnSpeedLabel.setText(getResources().getString(R.string.column_speed_imp));
        binding.cutLengthLabel.setText(getResources().getString(R.string.cut_length_imp));
        binding.cutRateLayoutLabel.setText(getResources().getString(R.string.cut_rate_imp));
        binding.cutsPerSecLabel.setText(getResources().getString(R.string.enter_material_area_imp));
        binding.brickWLabel.setText(getResources().getString(R.string.enter_brick_w_imp));
        binding.brickHLabel.setText(getResources().getString(R.string.enter_brick_h_imp));
        binding.brickHLabel.setText(getResources().getString(R.string.enter_brick_h_imp));
        setUpspinner(binding.selectAugerSpinner);
        meffcalculation.convertToImp();
    }

    private void changeToMetric() {
        binding.imperialMetricSwitch.setText(getString(R.string.metric));
        new SendALongToast(getActivity(), "Metric").show();
        //TODO: Change all units to imperial/ May want to translate anything that is already there back and forth
        binding.columnSpeedLabel.setText(getResources().getString(R.string.column_speed_metric));
        binding.cutLengthLabel.setText(getResources().getString(R.string.cut_length_metric));
        binding.cutRateLayoutLabel.setText(getResources().getString(R.string.cut_rate_metric));
        binding.cutsPerSecLabel.setText(getResources().getString(R.string.enter_material_area_metric));
        binding.brickWLabel.setText(getResources().getString(R.string.enter_brick_w_metric));
        binding.brickHLabel.setText(getResources().getString(R.string.enter_brick_h_metric));
        binding.brickHLabel.setText(getResources().getString(R.string.enter_brick_h_metric));
        setUpspinner(binding.selectAugerSpinner);
        meffcalculation.convertToMetric();
    }

    private void needToCalculateColumnSpeed() {
        new SendALongToast(getActivity(), getString(R.string.need_to_calculate_column_speed)).show();
        binding.knowColumnSpeed.setText(getString(R.string.not_known));
        new appearAnim(binding.cutLengthLayout, getActivity()).animate();
        new appearAnim(binding.cutRateLayout, getActivity()).animate();
        new appearAnim(binding.estimationWasteLayout, getActivity()).animate();
    }

    private void dontNeedtoCalculateColumnSpeed() {
        new SendALongToast(getActivity(), getString(R.string.dont_need_to_calculate_column_speed)).show();
        binding.knowColumnSpeed.setText(getString(R.string.known));
        binding.cutLengthEt.setText("");
        binding.cutRateEt.setText("");
        binding.estimationWasteEt.setText("");
        binding.columnSpeedEt.setText("");
        new fadeAnim(binding.cutLengthLayout, getActivity()).animate();
        new fadeAnim(binding.cutRateLayout, getActivity()).animate();
        new fadeAnim(binding.estimationWasteLayout, getActivity()).animate();
    }

    private void needProductSection() {
        new SendALongToast(getActivity(), getString(R.string.need_to_calculate_product_area)).show();
        binding.knownProductSection.setText(getString(R.string.not_known));
        new appearAnim(binding.brickWidthLayout, getActivity()).animate();
        new appearAnim(binding.brickHeightLayout, getActivity()).animate();
        new appearAnim(binding.brickVoidPercentageLayout, getActivity()).animate();

    }

    private void dontNeedProductSection() {
        new SendALongToast(getActivity(), getString(R.string.dont_need_to_calculate_product_area)).show();
        binding.knownProductSection.setText(getString(R.string.known));
        binding.brickHEt.setText("");
        binding.brickWEt.setText("");
        binding.brickVoidPercentageEt.setText("");
        binding.axMaterialSection.setText("");
        new fadeAnim(binding.brickWidthLayout, getActivity()).animate();
        new fadeAnim(binding.brickHeightLayout, getActivity()).animate();
        new fadeAnim(binding.brickVoidPercentageLayout, getActivity()).animate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAllInputs();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(machineDetailFragment.ARG_ITEM_ID, machineId);
        Gson json = new Gson();
        outState.putString(EFF_CALC_ARG, json.toJson(meffcalculation));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {
            meffcalculationLive.getValue().setVd(Double.parseDouble(augerValueList.get(position)));
            checkAndCalculate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clearAllInputs(){
        binding.columnSpeedEt.setText("");
        binding.brickVoidPercentageEt.setText("");
        binding.axMaterialSection.setText("");
        binding.columnSpeedEt.setText("");
        binding.brickWEt.setText("");
        binding.brickHEt.setText("");
        binding.cutRateEt.setText("");
        binding.cutLengthEt.setText("");
        binding.augeSpeedEt.setText("");
        binding.efficiencyTv.setText("0.0");
    }

    public class Callbacks {

        public void afterTextChanged(Editable editable) {
            new SendALongToast(mContext, "Checking if calculation can be made").show();
            checkAndCalculate();
        }

        public void userHasCutter(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                has_cutter();
            } else {
                does_not_have_cutter();
            }
            meffcalculation.setHasc(isChecked);
            meffcalculationLive.setValue(meffcalculation);
        }

        public void imperialMetric(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                changeToImperial();

            } else {
                changeToMetric();
            }
            meffcalculation.setImp(isChecked);
            meffcalculationLive.setValue(meffcalculation);
        }

        public void knownProductSection(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                dontNeedProductSection();
            } else {
                needProductSection();
            }

        }

        public void knowColumnSpeed(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                dontNeedtoCalculateColumnSpeed();
            } else {
                needToCalculateColumnSpeed();
            }
        }
    }

    public void checkAndCalculate() {
        if (meffcalculationLive.getValue().getAx() != 0 && meffcalculationLive.getValue().getV() != 0
                && meffcalculationLive.getValue().getVd() != 0 && meffcalculationLive.getValue().getN() != 0) {
            meffcalculationLive.getValue().calculateEf();
            meffcalculationLive.getValue().setEffString(String.valueOf(meffcalculationLive.getValue().getEff()));
            binding.efficiencyTv.setText(String.valueOf(meffcalculationLive.getValue().getEff()));
        }
        //check if column Speed can be calculated
        if (meffcalculationLive.getValue().getC() != 0 && meffcalculationLive.getValue().getL() != 0
                ) {
            meffcalculationLive.getValue().setV(meffcalculationLive.getValue().calculateColumnSpeed());
            //Check if the value is actually diferent before attempting to replace it with the calculation
            if(binding.columnSpeedEt.getText().toString().isEmpty()||
                    Double.parseDouble(binding.columnSpeedEt.getText().toString())!=meffcalculationLive.getValue().getV()) {
            binding.columnSpeedEt.setText(String.valueOf(meffcalculationLive.getValue().getV()));}
        }
        //Check if productSection Can be calculated
        if (meffcalculationLive.getValue().getW() != 0 && meffcalculationLive.getValue().getH() != 0) {
            meffcalculationLive.getValue().setAx(meffcalculationLive.getValue().calculateProductSection());
            //Check if the value is actually diferent before attempting to replace it with the calculation
            if(binding.axMaterialSection.getText().toString().isEmpty()||
                    Double.parseDouble(binding.axMaterialSection.getText().toString())!=meffcalculationLive.getValue().getAx()) {
            binding.axMaterialSection.setText(String.valueOf(meffcalculationLive.getValue().getAx()));}
        }
    }

    public static class MyLiveEffCalculation extends MutableLiveData<effcalculation> {
        public String effString;
        public String compidString;
        public String VString;
        public String CString;
        public String LString;
        public String RString;
        public String AxString;
        public String WString;
        public String HString;
        public String ZString;
        public String NString;


        @Nullable
        @Override
        public effcalculation getValue() {
            return super.getValue();
        }

        @Override
        public void setValue(effcalculation value) {
            super.setValue(value);
        }
    }


}
