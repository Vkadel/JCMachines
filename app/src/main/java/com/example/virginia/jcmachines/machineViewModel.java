package com.example.virginia.jcmachines;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.machineRepository;

import timber.log.Timber;

public class machineViewModel extends AndroidViewModel {
    public LiveData<PagedList<machine>> machines;
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
        machines = this.mRepository.getAllMachines();
    }



    public LiveData<PagedList<machine>> getMachines() {
        return this.machines;
    }

    public void insert(machine article) {
        this.mRepository.insert(article);
    }

    public void loadArticlesOnline() {
        // Do an asynchronous operation to fetch machines.
        //Getting instance of Room DataBase
        Timber.d("Going to start loadArticles");
        this.mRepository.refreshItemsOnline(this.machines);
    }
}