package com.example.virginia.jcmachines.Data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseEffFormulaLive extends LiveData<DataSnapshot> {
    private final DatabaseReference mRef;
    private final MyValueEventListener listener = new MyValueEventListener();
    private final String LOG_TAG="FirebaseEffFormulaLive";

    public FirebaseEffFormulaLive(DatabaseReference ref) {
        mRef=ref;

    }

    @Override
    protected void onActive() {
       /* Log.d(LOG_TAG, "onActive");*/
        mRef.addValueEventListener(listener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        /*Log.d(LOG_TAG, "onInactive");*/
        mRef.removeEventListener(listener);
        super.onInactive();
    }

    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot!=null){
                Log.d(LOG_TAG, "onDataChange");
                Log.d(LOG_TAG, "onDataChange this is the data: "+dataSnapshot.getValue().toString());
                setValue(dataSnapshot);
            }
            else{
                Log.e(LOG_TAG, "DataSnapshot was null");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Could not Perform Read from Firebase " + mRef, databaseError.toException());
        }

    }


}
