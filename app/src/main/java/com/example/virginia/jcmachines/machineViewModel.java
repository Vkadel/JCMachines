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
        this.mRepository = null;
        this.machines = null;
    }

    public machineViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new machineRepository(application);
        this.machines = this.mRepository.getallArticles();
    }


    public LiveData<List<machine>> getMachines() {
        return this.machines;
    }

    public void insert(machine article) {
        this.mRepository.insert(article);
    }

    public void loadArticlesOnline() {
        // Do an asynchronous operation to fetch machines.
        //Getting instance of Room DataBase
        this.mRepository.refreshItemsOnline(this.machines);
    }
}