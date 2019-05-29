package com.example.virginia.jcmachines.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.virginia.jcmachines.Data.FirebaseEffFormulaLive;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class efficiencyFormulaViewModel extends ViewModel {
    Context mContext;
    LiveData<effcalculation> meffCalculation;
    MutableLiveData<effcalculation> effCalculationMutable;
    LiveData<List<effcalculation>> meffCalculationList;
    public efficiencyFormulaViewModel(final Context context, DatabaseReference myRef){
    mContext=context;
    }

    public efficiencyFormulaViewModel() {
        super();
    }

    public MutableLiveData<effcalculation> getMeffCalculation(effcalculation calc)
    {
        effCalculationMutable.setValue(calc);
        return effCalculationMutable;
    }

    public LiveData<effcalculation> getMeffCalculationFirebase(String mrefString) {
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        FirebaseEffFormulaLive getoneCalculation=new FirebaseEffFormulaLive(mref);
        return meffCalculation;
    }

    public LiveData<List<effcalculation>> getMeffCalculationList(String mrefString) {
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        FirebaseEffFormulaLive getoneCalculation=new FirebaseEffFormulaLive(mref);
        return meffCalculationList;
    }
}
