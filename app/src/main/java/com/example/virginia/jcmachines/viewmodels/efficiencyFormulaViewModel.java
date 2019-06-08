package com.example.virginia.jcmachines.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.virginia.jcmachines.Data.FirebaseEffFormulaLive;
import com.example.virginia.jcmachines.Data.FirebaseEffFormulaLiveListChild;
import com.example.virginia.jcmachines.Data.FirebaseOneEffFormulaLive;
import com.example.virginia.jcmachines.Data.effcalculation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class efficiencyFormulaViewModel extends ViewModel {
    Context mContext;
    LiveData<effcalculation> meffCalculation;
    MutableLiveData<effcalculation> effCalculationMutable;
    FirebaseEffFormulaLive getList;
    FirebaseEffFormulaLiveListChild childList;
    boolean remove;

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
        FirebaseOneEffFormulaLive getoneCalculation=new FirebaseOneEffFormulaLive(mref);
        return meffCalculation;
    }

    public LiveData<DataSnapshot> getMeffCalculationList(@NonNull String userID, Context context) {
        String mrefString="effcalculations/"+userID;
        Boolean isTrue=true;
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        Query myquery=mref.orderByChild("active").equalTo(isTrue);
        getList=new FirebaseEffFormulaLive(myquery);
        return getList;

    }
    public LiveData<DataSnapshot> getMeffCalculationListbyChildren(@NonNull String userID, Context context) {
        String mrefString="effcalculations/"+userID;
        Boolean isTrue=true;
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference(mrefString);
        Query myquery=mref.orderByChild("active").equalTo(isTrue);
        childList=new FirebaseEffFormulaLiveListChild(myquery);
        return childList;
    }
    public LiveData<DataSnapshot> getMeffCalculationListbyChildrenRef(){
        return childList;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return childList.isRemove();
    }

    public LiveData<effcalculation> getMeffCalculation() {
        return meffCalculation;
    }

    public FirebaseEffFormulaLive getGetList() {
        return getList;
    }
}
