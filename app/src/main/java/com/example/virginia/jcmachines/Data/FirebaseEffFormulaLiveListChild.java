package com.example.virginia.jcmachines.Data;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

public class FirebaseEffFormulaLiveListChild extends LiveData<DataSnapshot> {
    private final Query mRef;
    private final mychildEventListener listener = new mychildEventListener();
    private final String LOG_TAG = "FirebaseEffFormulaLive";
    private boolean remove=false;

    public FirebaseEffFormulaLiveListChild(Query ref) {
        mRef = ref;

    }

    @Override
    protected void onActive() {
        mRef.addChildEventListener(listener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        mRef.removeEventListener(listener);
        super.onInactive();
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    private class mychildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.getValue() != null) {
                Log.d(LOG_TAG, "onDataChange");
                Log.d(LOG_TAG, "onDataChange this is the data: " + dataSnapshot.getValue().toString());
                setValue(dataSnapshot);
                setRemove(false);
            } else {
                Log.e(LOG_TAG, "DataSnapshot was null");
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.getValue() != null) {
                Log.d(LOG_TAG, "onDataChange");
                Log.d(LOG_TAG, "onDataChange this is the data: " + dataSnapshot.getValue().toString());
                setValue(dataSnapshot);
                setRemove(true);
            } else {
                Log.e(LOG_TAG, "DataSnapshot was null");
            }

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                Log.d(LOG_TAG, "onDataChange");
                Log.d(LOG_TAG, "onDataChange this is the data: " + dataSnapshot.getValue().toString());
                setRemove(true);
                setValue(dataSnapshot);
            } else {
                Log.e(LOG_TAG, "DataSnapshot was null");
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.getValue() != null) {
                Log.d(LOG_TAG, "onDataChange");
                Log.d(LOG_TAG, "onDataChange this is the data: " + dataSnapshot.getValue().toString());
                setValue(dataSnapshot);
            } else {
                Log.e(LOG_TAG, "DataSnapshot was null");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Could not Perform Read from Firebase " + mRef, databaseError.toException());
        }

    }

}
