package com.example.virginia.jcmachines;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.machineRepository;

import java.util.List;

public class machineViewModel extends AndroidViewModel {
    private LiveData<List<machine>> machines;
    private machineRepository mRepository;

    @Override
    protected void onCleared() {
        super.onCleared();
        mRepository = null;
        machines = null;
    }

    public machineViewModel(@NonNull Application application) {
        super(application);
        mRepository = new machineRepository(application);
        machines = mRepository.getallArticles();
    }


    public LiveData<List<machine>> getMachines() {
        return machines;
    }

    public void insert(machine article) {
        mRepository.insert(article);
    }

    public void loadArticlesOnline() {
        // Do an asynchronous operation to fetch machines.
        //Getting instance of Room DataBase
        mRepository.refreshItemsOnline(machines);
    }
}