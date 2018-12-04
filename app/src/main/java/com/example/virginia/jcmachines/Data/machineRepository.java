package com.example.virginia.jcmachines.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.virginia.jcmachines.remote.RemoteEndpointUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class machineRepository {
    private final String TAG= this.getClass().getSimpleName();
    private final machineDAO mMachineDAO;
    private final LiveData<List<machine>> mArticles;
    private List<machine> mArticleList;
    private final Boolean notInitialized = false;

    public machineRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        Timber.d("VK:Getting Items updated");
        this.mMachineDAO = db.machineDAO();

        this.mArticles = this.mMachineDAO.getAll();

        //If mMachines is empty call an Async Task to update online. Or if
        //is the first Time the Repository is initialized
        if (!this.notInitialized) {
            this.refreshItemsOnline(this.mArticles);
            Timber.d("VK: Started Online Update");
        }

    }

    public LiveData<List<machine>> getallArticles() {
        return this.mArticles;
    }

    public void refreshItemsOnline(LiveData<List<machine>> mArticles) {
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

    private static class getJsonArrayOnline extends AsyncTask<LiveData<List<machine>>, Void, Void> {

        private final machineDAO mAsyncTaskDao;
        private JSONArray array;
        private List<machine> mArticles;
        getJsonArrayOnline(machineDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(LiveData<List<machine>>... liveData) {
            List<machine> machineArrayList = new ArrayList<>();
            try {
                this.array = RemoteEndpointUtil.fetchJsonArray();
                //TODO: Add catch for when ARRAY is zero
                int arraysize = this.array.length();
                if (this.array == null) {
                    throw new JSONException("Invalid parsed item array");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i <= this.array.length() - 1; i++) {
                    machine thisMachine = new machine();
                    JSONObject object = new JSONObject();
                    object = this.array.getJSONObject(i);
                    thisMachine.setId(Integer.parseInt(object.getString("id")));
                    thisMachine.setSeries(object.getString("series"));
                    thisMachine.setMachineFullName(object.getString("machineFullName"));
                    thisMachine.setDescription(object.getString("Description"));
                    thisMachine.setThumbnailImage(object.getString("ThumbnailImage"));
                    thisMachine.setLargeImageOne(object.getString("LargeImageOne"));
                    thisMachine.setLargeImageTwo(object.getString("LargeImageTwo"));
                    thisMachine.setInlIneInstallImage(object.getString("InlIneInstallImage"));
                    thisMachine.setDimensionsSpecsImage(object.getString("DimensionsSpecsImage"));
                    thisMachine.setAngleInstallImage(object.getString("AngleInstallImage"));
                    thisMachine.setDatasheetLink(object.getString("DatasheetLink"));
                    thisMachine.setLubricationChartLink(object.getString("LubricationChartLink"));

                    //Convertion for array items: keyFeatures
                    String keyFeatureString=object.getJSONArray("KeyFeatures").toString();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<keyFeatures>>(){}.getType();
                    List<keyFeatures> keyFeaturesList = gson.fromJson(keyFeatureString, type);
                    thisMachine.setKeyFeatures(keyFeaturesList);

                    //Convertion for array items: InstructionalVids
                    String InstructionalVidsString=object.getJSONArray("InstructionalVids").toString();
                    type = new TypeToken<List<instructionalVids>>(){}.getType();
                    List<instructionalVids> instructionalVidsList = gson.fromJson(InstructionalVidsString, type);
                    thisMachine.setInstructionalVids(instructionalVidsList);


                    //Convertion for array items: "TechnicalBulletins"
                    String technicalBulletinsString=object.getJSONArray("TechnicalBulletins").toString();
                    type = new TypeToken<List<technicalBulletins>>(){}.getType();
                    List<technicalBulletins> technicalBulletinsVidsList = gson.fromJson(technicalBulletinsString, type);
                    thisMachine.setTechnicalBulletins(technicalBulletinsVidsList);

                    //Convertion for array items: "TechnicalBulletins"
                    String SparePartsString=object.getJSONArray("SpareParts").toString();
                    type = new TypeToken<List<spareParts>>(){}.getType();
                    List<spareParts> SparePartsList = gson.fromJson(SparePartsString, type);
                    thisMachine.setSpareParts(SparePartsList);

                    this.mAsyncTaskDao.insertOne(thisMachine);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.mArticles = this.mAsyncTaskDao.getAll().getValue();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

}
