package com.example.virginia.jcmachines.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.virginia.jcmachines.Data.FirebaseEffFormulaLive;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class efficiencyFormulaViewModel extends ViewModel {
    Context mContext;
    LiveData<effcalculation> meffCalculation;
    MutableLiveData<effcalculation> effCalculationMutable;
    public efficiencyFormulaViewModel(final Context context, DatabaseReference myRef){
    mContext=context;
    }

    public efficiencyFormulaViewModel() {
        super();
    }

    public MutableLiveData<effcalculation> getMeffCalculation(effcalculation calc) {
        effCalculationMutable.setValue(calc);
        return effCalculationMutable;
    }

    public LiveData<effcalculation> getMeffCalculationFirebase(String mrefString) {
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        FirebaseEffFormulaLive getoneCalculation=new FirebaseEffFormulaLive(mref);
        return meffCalculation;
    }

    public LiveData<DataSnapshot> getMeffCalculationList(String userID, Context context) {
        String mrefString=context.getString(R.string.firebase_ref_calculations_all_for_user);
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        mref.equalTo(userID,context.getString(R.string.firebase_key_user_under_effcalc));
        mref.orderByChild("date");
        FirebaseEffFormulaLive getList=new FirebaseEffFormulaLive(mref);
        return getList;
    }
}
