package com.example.virginia.jcmachines;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseMethod;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.example.virginia.jcmachines.Data.augers;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.animations.appearAnimLayout;
import com.example.virginia.jcmachines.animations.fadeAnimLayout;
import com.example.virginia.jcmachines.databinding.FragmentAddEffCalculationBinding;
import com.example.virginia.jcmachines.utils.DoubleTruncate;
import com.example.virginia.jcmachines.utils.DoubleTruncateZero;
import com.example.virginia.jcmachines.utils.MDateFormating;
import com.example.virginia.jcmachines.utils.SendALongToast;
import com.example.virginia.jcmachines.viewmodels.efficiencyFormulaViewModel;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddEffCalculationActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static FragmentAddEffCalculationBinding binding;
    static final public String EFF_CALC_ARG_EXIST = "eff_calc_arg";
    static final String IS_TWO_PANE = "is_two_pane";
    private static Context mContext;
    private static machine thismachine;
    private PagedList<machine> mmachines;
    private ArrayList<String> augerList = new ArrayList<>();
    private ArrayList<String> augerValueList = new ArrayList<>();
    private static effcalculation meffcalculation = new effcalculation();
    private final MyLiveEffCalculation meffcalculationLive = new MyLiveEffCalculation();
    private String machineId;
    private String thisCalculationID;



    public AddEffCalculationActivityFragment() {
        super();
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        //Setting up the listener
        mContext = getActivity();
        if (savedInstanceState!=null&&savedInstanceState.containsKey(EFF_CALC_ARG_EXIST)) {
            //getting the existing value
            Gson json = new Gson();
            effcalculation tran=json.fromJson(savedInstanceState.getString(EFF_CALC_ARG_EXIST), effcalculation.class);
            meffcalculationLive.setValue(tran);
            binding.setLivedata(meffcalculationLive);
            initialUISetup();
        }
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_eff_calculation, container, false);
        binding.setLifecycleOwner(this);

        //checking if the binding is used in the addCalculation Activity
        FloatingActionButton fab=getActivity().findViewById(R.id.fab);

        View view = binding.getRoot();
        binding.setLivedata(meffcalculationLive);
        binding.setCalculation(new Callbacks());
        binding.setConverter(new Converter());
        fab.setOnClickListener(new myUpdateAndSendEmailListener());

        //Get sent machine information
        if (getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
            machineId = this.getArguments().getString(machineDetailFragment.ARG_ITEM_ID);
        } else {
            //TODO: may need to store de machine id in the calculation
            machineId = "0";
        }

        //Only load and connect to the Eff calculation model if an id is passed and is not null
        if (getArguments().getString(machineDetailFragment.EFF_ARG_ITEM_ID)!=null && savedInstanceState == null) {
                disableAllDataEntry();
                thisCalculationID=getArguments().getString(machineDetailFragment.EFF_ARG_ITEM_ID);
            efficiencyFormulaViewModel viewModeleffList = ViewModelProviders.of(this).get(efficiencyFormulaViewModel.class);
                viewModeleffList.getMeffCalculationList(FirebaseAuth.getInstance().getUid(), getActivity()).observe(this, new myObserver());
        }

        if (savedInstanceState == null) {
            //SetUpFor the first time
            meffcalculationLive.setValue(meffcalculation.clear());
            //Subscribe to the machine model
            machineViewModel viewModel = ViewModelProviders.of(this).get(machineViewModel.class);
            viewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                @Override
                public void onChanged(@Nullable PagedList<machine> machines) {
                    //Get Machine number from sent if you do not have it
                    if (machineId == null) {
                        machineId = String.valueOf(meffcalculationLive.getValue().getMid());
                    }
                    thismachine = machines.get(Integer.parseInt(machineId));
                    initialUISetup();

                }
            });

            //Set Initial value if none is provided
            if (meffcalculationLive.getValue() == null && !getArguments().containsKey(machineDetailFragment.EFF_ARG_ITEM_ID)) {
                meffcalculationLive.setValue(meffcalculation);
            }

        } else {
            machineId = savedInstanceState.getString(machineDetailFragment.ARG_ITEM_ID);
            if (savedInstanceState.containsKey(EFF_CALC_ARG_EXIST)) {
                //getting the existing value
                Gson json = new Gson();
                effcalculation tran=json.fromJson(savedInstanceState.getString(EFF_CALC_ARG_EXIST), effcalculation.class);
                meffcalculationLive.setValue(tran);
                binding.setLivedata(meffcalculationLive);
                initialUISetup();
                //if Item already exist then need to disable all entry again
                if(savedInstanceState.containsKey(machineDetailFragment.EFF_ARG_ITEM_ID)){
                    thisCalculationID=savedInstanceState.getString(machineDetailFragment.EFF_ARG_ITEM_ID);
                    disableAllDataEntry();
                }
            }
        }
        return view;
    }

    private void disableAllDataEntry() {
        binding.enterCompanyNameEt.setEnabled(false);
        binding.userHasCutter.setEnabled(false);
        binding.imperialMetricSwitch.setEnabled(false);
        binding.cutLengthEt.setEnabled(false);
        binding.cutRateEt.setEnabled(false);
        binding.columnSpeedEt.setEnabled(false);
        binding.augeSpeedEt.setEnabled(false);
        binding.brickHEt.setEnabled(false);
        binding.brickWEt.setEnabled(false);
        binding.brickVoidPercentageEt.setEnabled(false);
        binding.axMaterialSection.setEnabled(false);
        binding.estimationWasteEt.setEnabled(false);
        binding.imperialMetricSwitch.setEnabled(false);
        binding.knowColumnSpeed.setEnabled(false);
        binding.knownProductSection.setEnabled(false);
        binding.selectAugerSpinner.setEnabled(false);
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

    private void initialUISetup() {
        rePopViewsAfterConfigUpdate();
        if (!(meffcalculationLive.getValue().getL() != 0
                || meffcalculationLive.getValue().getR() != 0
                || meffcalculationLive.getValue().getC() != 0)) {
            binding.cutLengthLayout.setVisibility(View.GONE);
            binding.cutRateLayout.setVisibility(View.GONE);
            binding.estimationWasteLayout.setVisibility(View.GONE);
        }

        if (!(meffcalculationLive.getValue().getH() != 0
                || meffcalculationLive.getValue().getW() != 0
                || meffcalculationLive.getValue().getZ() != 0)) {
            binding.brickWidthLayout.setVisibility(View.GONE);
            binding.brickHeightLayout.setVisibility(View.GONE);
            binding.brickVoidPercentageLayout.setVisibility(View.GONE);
        }

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
        new appearAnimLayout(binding.cutLengthLayout, getActivity()).animate();
        new appearAnimLayout(binding.cutRateLayout, getActivity()).animate();
        new appearAnimLayout(binding.estimationWasteLayout, getActivity()).animate();
    }

    private void dontNeedtoCalculateColumnSpeed() {
        new SendALongToast(getActivity(), getString(R.string.dont_need_to_calculate_column_speed)).show();
        binding.knowColumnSpeed.setText(getString(R.string.known));
        binding.cutLengthEt.setText("");
        binding.cutRateEt.setText("");
        binding.estimationWasteEt.setText("");
        binding.columnSpeedEt.setText("");
        new fadeAnimLayout(binding.cutLengthLayout, getActivity()).animate();
        new fadeAnimLayout(binding.cutRateLayout, getActivity()).animate();
        new fadeAnimLayout(binding.estimationWasteLayout, getActivity()).animate();
    }

    private void needProductSection() {
        new SendALongToast(getActivity(), getString(R.string.need_to_calculate_product_area)).show();
        binding.knownProductSection.setText(getString(R.string.not_known));
        new appearAnimLayout(binding.brickWidthLayout, getActivity()).animate();
        new appearAnimLayout(binding.brickHeightLayout, getActivity()).animate();
        new appearAnimLayout(binding.brickVoidPercentageLayout, getActivity()).animate();

    }

    private void dontNeedProductSection() {
        new SendALongToast(getActivity(), getString(R.string.dont_need_to_calculate_product_area)).show();
        binding.knownProductSection.setText(getString(R.string.known));
        binding.brickHEt.setText("");
        binding.brickWEt.setText("");
        binding.brickVoidPercentageEt.setText("");
        binding.axMaterialSection.setText("");
        new fadeAnimLayout(binding.brickWidthLayout, getActivity()).animate();
        new fadeAnimLayout(binding.brickHeightLayout, getActivity()).animate();
        new fadeAnimLayout(binding.brickVoidPercentageLayout, getActivity()).animate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(machineDetailFragment.ARG_ITEM_ID, machineId);
        Gson json = new Gson();
        outState.putString(EFF_CALC_ARG_EXIST, json.toJson(meffcalculationLive.getValue()));
        if(thisCalculationID!=null){
        outState.putString(machineDetailFragment.EFF_ARG_ITEM_ID,thisCalculationID);}
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {
            meffcalculationLive.getValue().setVd(Double.parseDouble(augerValueList.get(position)));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clearAllInputs() {
        binding.enterCompanyNameEt.setText("");
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

    public class MyLiveEffCalculation extends MutableLiveData<effcalculation> {

        @Nullable
        @Override
        public effcalculation getValue() {
            return super.getValue();
        }
    }

    public class myUpdateAndSendEmailListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Snackbar.make(v, getResources().getString(R.string.will_save_your_calculation), Snackbar.LENGTH_LONG).show();
            //Get the information from the fragment
            long currentTimeinMillis = Calendar.getInstance().getTimeInMillis();
            String mydate = new MDateFormating(getActivity()).getMdate();
            String itemRef = getResources().getString(R.string.firebase_ref_calculations_add_one, FirebaseAuth.getInstance().getUid(), currentTimeinMillis);
            meffcalculationLive.getValue().setUserid(FirebaseAuth.getInstance().getUid());
            meffcalculationLive.getValue().setDate(currentTimeinMillis);
            meffcalculationLive.getValue().setDateS(mydate);
            meffcalculationLive.getValue().setActive(true);
            meffcalculationLive.getValue().setMid(thismachine.getId());
            meffcalculationLive.getValue().setMids(thismachine.getMachineFullName());
            meffcalculationLive.getValue().setCalcid(String.valueOf(currentTimeinMillis));
            itemRef.trim();
            itemRef.replace("", "").replace(":", "");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(itemRef);
            ref.setValue(meffcalculationLive.getValue());
        }
    }

    public class myObserver implements Observer<DataSnapshot> {
        @Override
        public void onChanged(@Nullable DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                binding.executePendingBindings();
                List<effcalculation> myList = new ArrayList<>();
                dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        myList.add(dataSnapshot.getValue(effcalculation.class));
                    }
                });
                //if the calculation ID is provided to show an item in particular
                effcalculation mycal = myList.get(Integer.parseInt(thisCalculationID));
                meffcalculationLive.setValue(mycal);

            } else {
                //We don't have a list of calculations
                new SendALongToast(mContext, getResources().getString(R.string.please_add_more_eff_calc_brick)).show();
            }
        }
    }

    public class Converter {
        @InverseMethod("stringToDouble")
        public String doubleToString(double value) {
            // Converts Double to String
            if (value == 0) {
                return "";
            } else {
                String myvalue="";
                if(value>=99){
                     myvalue = String.valueOf(new DoubleTruncateZero(value, mContext).getTruncatedNumber());
                }else{
                     myvalue = String.valueOf(new DoubleTruncate(value, mContext).getTruncatedNumber());
                }

                return myvalue;
            }
        }

        public double stringToDouble(String value) {
            // Converts String to Double
            if (!value.isEmpty()) {
                double number = Double.valueOf(value);
                return number;
            } else {
                return 0;
            }

        }
    }

}