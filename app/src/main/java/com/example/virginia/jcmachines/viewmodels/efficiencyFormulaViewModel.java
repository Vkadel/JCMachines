package com.example.virginia.jcmachines.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.virginia.jcmachines.Data.FirebaseEffFormulaLive;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class efficiencyFormulaViewModel extends ViewModel {
    Context mContext;
    LiveData<effcalculation> meffCalculation;
    MutableLiveData<effcalculation> effCalculationMutable;
    FirebaseEffFormulaLive getList;

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
        getList=new FirebaseEffFormulaLive(mref);
        return getList;
    }

    public LiveData<effcalculation> getMeffCalculation() {
        return meffCalculation;
    }

    public FirebaseEffFormulaLive getGetList() {
        return getList;
    }
}
