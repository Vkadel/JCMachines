package com.example.virginia.jcmachines.Data;


import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseEffFormulaLive extends LiveData<DataSnapshot> {
    private final Query mRef;
    private final MyValueEventListener listener = new MyValueEventListener();
    private final String LOG_TAG = "FirebaseEffFormulaLive";

    public FirebaseEffFormulaLive(Query ref) {
        mRef = ref;

    }

    @Override
    protected void onActive() {
        mRef.addValueEventListener(listener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        mRef.removeEventListener(listener);
        super.onInactive();
    }

    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Could not Perform Read from Firebase " + mRef, databaseError.toException());
        }
    }

}
