package com.example.virginia.jcmachines;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class jcSteeleMachineWidget extends AppWidgetProvider {
    static String thisMachineName;
    static String thisMachineID;
    static String thisMachineImageLink;
    static String thisMachineWidgetId;
    AppWidgetTarget appWidgetTarget;
    static String[] thisMachineIDarray;
    static String[] thisMachineImageLinkArray;
    static String[] thisMachineNameArray;
    static List thisMachineWidgetIdArrayList;
    int position;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.plant(new Timber.DebugTree());
        //None of the manual updates to the remoteview Are actually done here but called
        //in the method on update below
        // Construct the RemoteViews object
        //Find out How many
        int[] existingId = appWidgetManager.getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        views.setTextViewText(R.id.appwidget_text, thisMachineName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Timber.d("Going to Add widget text: " + thisMachineName + " and " + thisMachineID);

    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        Timber.d("AtWidget pushWidgetUpdate");
        ComponentName myWidget = new ComponentName(context, jcSteeleMachineWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.plant(new Timber.DebugTree());
        int[] allIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
        Boolean updateOneTime=false;
        SharedPreferences sharedPref = getSharedPreferences(context);

        if (appWidgetIds.length == 1 && !updateOneTime) {
            //There a single widget update
            updateOneTime=true;
            List<Integer> allMyIdsArrayInteger = Arrays.stream(allIds).boxed().collect(Collectors.toList());
            int thisWidgetid = appWidgetIds[0];

            position=findItemPosition(allMyIdsArrayInteger, thisWidgetid);

            Boolean widgetIdSavedinPreferences = false;
            //Set up widget ID
            //Set Up widget ID for first item
            if ((position == -1 || appWidgetIds.length < 1)) {
                //Save widget information in preferences for first item
                SharedPreferences newSharedPref=context.getSharedPreferences(context.getResources()
                        .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);;
                //this is the first time the item is setup
                final SharedPreferences.Editor editor = newSharedPref.edit();
                //This is the first widget that got set up
                if (thisMachineWidgetIdArrayList == null && !widgetIdSavedinPreferences) {
                    position = position + 1;
                    editor.putString(context.getString(R.string.my_machine_widget_id_key), thisWidgetid + "");
                    editor.commit();
                    widgetIdSavedinPreferences = true;
                }
                //Setup widgetID more items
                if (thisMachineWidgetIdArrayList!=null && !widgetIdSavedinPreferences) {
                    position=thisMachineWidgetIdArrayList.size();
                    editor.putString(context.getString(R.string.my_machine_widget_id_key), thisMachineWidgetId+"," + thisWidgetid);
                    editor.commit();
                    widgetIdSavedinPreferences = true;
                }
            }

            updateOneWidget(context, position, appWidgetIds[0]);
        }
        if(appWidgetIds.length>0&& !updateOneTime) {
            //If there is a multiWIdget Update
            updateOneTime=true;
            final int N = allIds.length;
            updateThewidgets(context, appWidgetIds, N);
        }
    }

    private int findItemPosition(List<Integer> allIds, int thisWidgetid) {
        thisMachineWidgetIdArrayList = convertStringtoIntList(thisMachineWidgetId);

        //If there are more widgetids in preferences but this item is not configured
        if (thisMachineWidgetIdArrayList != null && thisMachineWidgetIdArrayList.indexOf(thisWidgetid) == -1) {
            //Check if the current item its a widget and the position
            position = -1;
        }
        //If there are widgets in preferences and this item is configured
        if (thisMachineWidgetIdArrayList != null && thisMachineWidgetIdArrayList.indexOf(thisWidgetid) != -1) {
            //Check if the current item its a widget and the position
            position = thisMachineWidgetIdArrayList.indexOf(thisWidgetid);
        }
        if(thisMachineWidgetIdArrayList==null){
            position=-1;
        }

        return position;
    }

    @NonNull
    private SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources()
                .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
        // Perform this loop procedure for each App Widget that belongs to this provider
        Timber.d("AtWidget onUpdate");
        String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
        String defaultValueName = context.getResources().getString(R.string.my_machine_name_for_widget_default);
        // Split the different Machines
        thisMachineID = sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), defaultValue);
        //TODO: Add to String
        thisMachineName = sharedPref.getString(context.getString(R.string.my_machine_name_for_widget_key), "Go On app and add widget");
        thisMachineImageLink = sharedPref.getString(context.getString(R.string.my_machine_pic_link_for_widget_key), "http");
        thisMachineWidgetId = sharedPref.getString(context.getString(R.string.my_machine_widget_id_key), "");
        return sharedPref;
    }

    private List convertStringtoIntList(String thisMachineWidgetIdArrayString) {
        Gson json = new Gson();
        List<Integer> myIntArray = new ArrayList<>() ;
        //Convert String to String array
        List<String> stringList = Arrays.asList(thisMachineWidgetIdArrayString.split("'"));
        if (stringList == null || stringList.get(0) == "") {
            myIntArray = null;
        } else {

            for(int i=0;i<stringList.size();i++){
                myIntArray.add(Integer.parseInt(thisMachineWidgetIdArrayString.split("'")[i]));
            }
            String myJsonString = json.toJson(myIntArray);
        }

        return myIntArray;
    }

    private void updateThewidgets(Context context, int[] appWidgetIds, int N) {

        for (int i = 0; i < N; i++) {
            Timber.d("AtWidget pushWidgetUpdate for N:" + i);
            int appWidgetId = appWidgetIds[i];

            String[] thisMachineIDarray = thisMachineID.split(",");
            String[] thisMachineImageLinkArray = thisMachineImageLink.split(",");
            String[] thisMachineNameArray = thisMachineName.split(",");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
            Timber.d("AtWidget Going to add to this view: " + views.getLayoutId());
            Timber.d("AtWidget Going to add this link: " + thisMachineImageLinkArray[i]);
            Timber.d("AtWidget Going to add this name: " + thisMachineIDarray[i]);
            Timber.d("AtWidget Going to add this id: " + thisMachineNameArray[i]);
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, machineListActivity.class);
            intent.putExtra(machineDetailFragment.ARG_ITEM_ID, thisMachineIDarray[i]);
            intent.putExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET, "true");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_IMMUTABLE);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.appWidget_machine_picture, pendingIntent);

            views.setTextViewText(R.id.appwidget_text, thisMachineNameArray[i]);
            //only Update picture if Link is different than NA
            if (!thisMachineImageLinkArray[i].equals("na")) {
                RequestOptions options = new RequestOptions().
                        override(300, 300);
                appWidgetTarget = new AppWidgetTarget(context, R.id.appWidget_machine_picture, views, appWidgetId) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Timber.d("Resource Ready");
                        super.onResourceReady(resource, transition);
                    }
                };
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(thisMachineImageLinkArray[i])
                        .apply(options)
                        .into(appWidgetTarget);
            } else {
                views.setViewVisibility(R.id.appWidget_machine_picture, View.INVISIBLE);
            }
            Timber.d("AtWidget On Update: Going to Add widget text: " + thisMachineNameArray[i] + "and " + thisMachineIDarray[0]);
            // Tell the AppWidgetManager to perform an update on the current app widget
            pushWidgetUpdate(context, views);
        }
    }

    private void updateOneWidget(Context context, int position, int appWidgetId) {

        Timber.d("AtWidget pushWidgetUpdate for position:" + position);
        String[] thisMachineIDarray = thisMachineID.split(",");
        String[] thisMachineImageLinkArray = thisMachineImageLink.split(",");
        String[] thisMachineNameArray = thisMachineName.split(",");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        Timber.d("AtWidget Going to add to this view: " + views.getLayoutId());
        Timber.d("AtWidget Going to add this link: " + thisMachineImageLinkArray[position]);
        Timber.d("AtWidget Going to add this name: " + thisMachineIDarray[position]);
        Timber.d("AtWidget Going to add this id: " + thisMachineNameArray[position]);
        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, machineListActivity.class);
        intent.putExtra(machineDetailFragment.ARG_ITEM_ID, thisMachineIDarray[position].toString());
        intent.putExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET, "true");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        views.setOnClickPendingIntent(R.id.appWidget_machine_picture, pendingIntent);

        views.setTextViewText(R.id.appwidget_text, thisMachineNameArray[position]);
        //only Update picture if Link is different than NA
        if (!thisMachineImageLinkArray[position].equals("na")) {
            RequestOptions options = new RequestOptions().
                    override(300, 300);
            appWidgetTarget = new AppWidgetTarget(context, R.id.appWidget_machine_picture, views, appWidgetId) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Timber.d("Resource Ready");
                    super.onResourceReady(resource, transition);
                }
            };
            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(thisMachineImageLinkArray[position])
                    .apply(options)
                    .into(appWidgetTarget);
        } else {
            views.setViewVisibility(R.id.appWidget_machine_picture, View.INVISIBLE);
        }
        Timber.d("AtWidget On Update: Going to Add widget text: " + thisMachineNameArray[position] + "and " + thisMachineIDarray[0]);
        // Tell the AppWidgetManager to perform an update on the current app widget
        pushWidgetUpdate(context, views);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
      /*  AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int [] existingId=appWidgetManager.getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));

        //Need to make sure to clear up the assigned 
        if(appWidgetIds.length==1){
            //One widget is being deleted
        SharedPreferences sharedPref = getSharedPreferences(context);
        findItemPosition(existingId, appWidgetIds[0]);}*/

    }
}

