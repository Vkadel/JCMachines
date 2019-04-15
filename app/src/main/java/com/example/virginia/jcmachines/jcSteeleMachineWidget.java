package com.example.virginia.jcmachines;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import timber.log.Timber;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.INVISIBLE;

/**
 * Implementation of App Widget functionality.
 */
public class jcSteeleMachineWidget extends AppWidgetProvider {
    static String thisMachineName;
    static String thisMachineID;
    AppWidgetTarget appWidgetTarget;
    Boolean dontUpdate = false;
    Gson prevMachineIDjson = new Gson();
    Gson prevMachineWidgetIdjson = new Gson();
    ArrayList<String> machineWidgetPrefArrayID = new ArrayList<>();
    ArrayList<String> machineWidgetPrefArrayName = new ArrayList<>();
    ArrayList<String> machineWidgetPrefArrayImageLink = new ArrayList<>();
    ArrayList<String> machineWidgetPrefArrayWidgetId = new ArrayList<>();
    int position;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.plant(new Timber.DebugTree());
        //None of the manual updates to the remoteview Are actually done here but called
        //in the method on update below
        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        views.setTextViewText(R.id.appwidget_text, "changed");
        // Create an Intent to launch ExampleActivity

    }

    public void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        Timber.d("AtWidget pushWidgetUpdate");
        ComponentName myWidget = new ComponentName(context, jcSteeleMachineWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra(machineDetailFragment.ARG_ITEM_ID)!=null & intent.getStringExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET)!=null){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Timber.plant(new Timber.DebugTree());
            Timber.d("AtWidget OnReceive: Entered ");
            Timber.d("AtWidget OnReceive: Request Item "+Integer.parseInt(intent.getExtras().getString(machineDetailFragment.ARG_ITEM_ID)));
            context.startActivity(intent);
        }else{
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.plant(new Timber.DebugTree());
        //Save widget information in preferences for first item
        SharedPreferences newSharedPref = context.getSharedPreferences(context.getResources()
                .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
        //this is the first time the item is setup
        final SharedPreferences.Editor editor = newSharedPref.edit();
        int[] allIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
        final int N = allIds.length;
        int thisWidgetid = 0;

        Boolean isOneItemUpdate=false;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);

        GetSharedPreferences(context);

        if (appWidgetIds.length == 1) {
            thisWidgetid = appWidgetIds[0];
            position = machineWidgetPrefArrayWidgetId.indexOf(thisWidgetid + "");
        }
        //Setup Item widgetID
        if ((position == -1 || appWidgetIds.length < 1)) {
            //This is the first widget that got set up
            position = position + 1;
            machineWidgetPrefArrayWidgetId.add(Integer.toString(thisWidgetid));
            updatePreference(context, editor);
        }

        if (appWidgetIds.length == 1) {
            List<Integer> myList= Arrays.stream(allIds).boxed().collect(Collectors.toList());
            int item = myList.indexOf(thisWidgetid);
            isOneItemUpdate=true;
            updateOneWidget(context, item, appWidgetIds[0]);
            if(allIds.length>1){
                //Trigger Update if there if this is not the onlywidget with all available widgets
            onUpdate(context,appWidgetManager,allIds);}
        }

        if(appWidgetIds.length>1){
            for (int i = 0; i < appWidgetIds.length; i++) {
                updateOneWidget(context, i, appWidgetIds[i]);
            }
        }
    }

    private void updatePreference(Context context, SharedPreferences.Editor editor) {
        editor.putString(context.getString(R.string.my_machine_widget_id_key), prevMachineWidgetIdjson.toJson(machineWidgetPrefArrayWidgetId));
        editor.commit();
    }


    @NonNull
    private void GetSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources()
                .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
        // Perform this loop procedure for each App Widget that belongs to this provider
        Timber.d("AtWidget Get SharedPref");

        //Getting JSon pref DONE
        try {
            ArrayList<String> myTransicionArray = prevMachineIDjson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), ""), ArrayList.class);
            if (myTransicionArray != null) {
                machineWidgetPrefArrayID.addAll(myTransicionArray);
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onClick: Could not create JSON from: machineWidgetPrefArrayID ", new Throwable());
            e.printStackTrace();

        }
        //Getting JSon pref DONE
        try {
            ArrayList<String> myTransicionArray = prevMachineIDjson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_name_for_widget_key), ""), ArrayList.class);
            if (myTransicionArray != null) {
                machineWidgetPrefArrayName.addAll(myTransicionArray);
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onClick: Could not create JSON from: machineWidgetPrefArrayName ", new Throwable());
            e.printStackTrace();

        }

        //Getting JSon pref DONE
        try {
            ArrayList<String> myTransicionArray = prevMachineIDjson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_pic_link_for_widget_key), ""), ArrayList.class);
            if (myTransicionArray != null) {
                machineWidgetPrefArrayImageLink.addAll(myTransicionArray);
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onClick: Could not create JSON from: machineWidgetPrefArrayImageLink", new Throwable());
            e.printStackTrace();

        }
        //Getting JSon pref DONE
        try {
            ArrayList<String> myTransicionArray = prevMachineIDjson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_widget_id_key), ""), ArrayList.class);
            if (myTransicionArray != null) {
                machineWidgetPrefArrayWidgetId.addAll(myTransicionArray);
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "onClick: Could not create JSON from machineWidgetPrefArrayWidgetId ", new Throwable());
            e.printStackTrace();

        }

    }


    private void updateOneWidget(Context context, int position, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        //Only update if there is a widget associated to this position
        if (machineWidgetPrefArrayWidgetId.indexOf(Integer.toString(appWidgetId)) != -1 && machineWidgetPrefArrayID.size() > position) {

            Timber.d("AtWidget pushWidgetUpdate for position:" + position);
            Timber.d("AtWidget Going to add to this view: " + views.getLayoutId());
            Timber.d("AtWidget Going to add this imagelink: " + machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)));
            Timber.d("AtWidget Going to add this name: " + machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            Timber.d("AtWidget Going to add this id: " + machineWidgetPrefArrayName.get(Integer.valueOf(position)));

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, machineDetailActivity.class);
            intent.putExtra(machineDetailFragment.ARG_ITEM_ID, machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            intent.putExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET, "true");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(machineWidgetPrefArrayID.get(Integer.valueOf(position))),
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.appWidget_machine_picture, pendingIntent);

            //only Update picture if Link is different than NA
            if (!machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)).equals("na")||machineWidgetPrefArrayImageLink.get(Integer.valueOf(position))!=null) {
                RequestOptions options = new RequestOptions().
                        override(100, 100);
                appWidgetTarget = new AppWidgetTarget(context, R.id.appWidget_machine_picture, views, appWidgetId) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Timber.d("Resource Ready");
                        super.onResourceReady(resource, transition);
                    }
                };
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)))
                        .apply(options)
                        .into(appWidgetTarget);
                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                views.setTextViewText(R.id.appwidget_text, machineWidgetPrefArrayName.get(Integer.valueOf(position)));
            } else {
                views.setViewVisibility(R.id.appWidget_machine_picture, View.INVISIBLE);
                views.setTextViewText(R.id.appwidget_text,context.getResources().getString(R.string.select_for_widget));
            }

            Timber.d("AtWidget On Update: Going to Add widget text: " + machineWidgetPrefArrayName.get(Integer.valueOf(position)) + "and " + machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            // Tell the AppWidgetManager to perform an update on the current app widget
        } else {
            Timber.d("AtWidget Position and Widget Size :" + position + " " + machineWidgetPrefArrayWidgetId.size());
            Timber.d(machineWidgetPrefArrayWidgetId.toString());
            views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
            views.setTextViewText(R.id.appwidget_text,context.getResources().getString(R.string.select_for_widget));

        }
        views.setViewVisibility(R.id.widgetProgress,INVISIBLE);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        GetSharedPreferences(context);
        for(int i=0;i<appWidgetIds.length;i++){
           Boolean exist=machineWidgetPrefArrayWidgetId.contains(Integer.toString(appWidgetIds[i]));
           if(exist){
               machineWidgetPrefArrayWidgetId.remove(Integer.toString(appWidgetIds[i]));
           }
        }
        //Final clean
        int[] allIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
        if(allIds.length<machineWidgetPrefArrayWidgetId.size()){
            //Do A full clean
            Timber.d("AtWidget: Doing full clean");
            List<String> convertedAllIDs=Arrays.asList(allIds).stream().map(ints -> ints.toString()).collect(Collectors.toList());
            machineWidgetPrefArrayWidgetId.remove(convertedAllIDs);
        }
        Timber.d("AtWidget: FInal widgetID array "+machineWidgetPrefArrayWidgetId.toString());
    }



    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        onUpdate(context, AppWidgetManager.getInstance(context),
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class)));
    }

}


