package com.example.virginia.jcmachines.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;
import android.util.Log;

import com.example.virginia.jcmachines.R;
import com.example.virginia.jcmachines.remote.RemoteEndpointUtil;
import com.example.virginia.jcmachines.utils.SendALongToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class machineRepository {
    private final machineDAO mMachineDAO;
    private final LiveData<PagedList<machine>> mMachines;
    private final Boolean notInitialized = false;
    private final Application mApplication;

    public machineRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        Timber.d("VK:Getting Items updated");
        this.mMachineDAO = db.machineDAO();
        mApplication = application;
        //Old Implementation without PagedList
        //mMachines = this.mMachineDAO.getAll();

        mMachines = new LivePagedListBuilder<>(mMachineDAO.getAll(), application.getResources().getInteger(R.integer.page_size)).build();
        //If mMachines is empty call an Async Task to update online. Or if
        //is the first Time the Repository is initialized
        //TODO: may want to delete this because user can just drag down and update
        if (!this.notInitialized) {
            this.refreshItemsOnline(mMachines);
            Timber.d("VK: Started Online Update");
        }

    }

    public LiveData<PagedList<machine>> getAllMachines() {
        return mMachines;
    }

    public void refreshItemsOnline(LiveData<PagedList<machine>> mArticles) {
        new machineRepository.getJsonArrayOnline(this.mMachineDAO).execute(mArticles);
    }

    public void insert(machine machine) {
        new machineRepository.insertAsyncTask(this.mMachineDAO).execute(machine);
    }

    private static class insertAsyncTask extends AsyncTask<machine, Void, Void> {

        private final machineDAO mAsyncTaskDao;

        insertAsyncTask(machineDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(machine... params) {
            this.mAsyncTaskDao.insertOne(params[0]);
            return null;
        }
    }

    private static class getJsonArrayOnline extends AsyncTask<LiveData<PagedList<machine>>, Void, Void> {
        private String JsonBack;
        private final machineDAO mAsyncTaskDao;
        private JSONArray array;
        private LiveData<PagedList<machine>> mMachines;


        getJsonArrayOnline(machineDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(LiveData<PagedList<machine>>... liveData) {
            List<machine> machineArrayList = new ArrayList<>();
            Collection<machine> thisMachineList;


            JsonBack = RemoteEndpointUtil.fetchJsonString();

            Gson gson = new Gson();
            machine thisMachine = new machine();
            Type collectionType = new TypeToken<Collection<machine>>() {}.getType();

            try {
              thisMachineList = gson.fromJson(JsonBack, collectionType);
              machineArrayList.addAll(thisMachineList);
            }catch (Exception e){
                thisMachineList=null;
            }

            if(thisMachineList!=null) {
                for (int i = 0; i <= thisMachineList.size() - 1; i++) {
                    thisMachine=machineArrayList.get(i);
                    this.mAsyncTaskDao.insertOne(thisMachine);
                }
            }
            Timber.d("VK:Updating the pagesize data Online");
            mMachines = new LivePagedListBuilder<>(mAsyncTaskDao.getAll(), 3).build();
            //this.mArticles = this.mAsyncTaskDao.getAll().getValue();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
