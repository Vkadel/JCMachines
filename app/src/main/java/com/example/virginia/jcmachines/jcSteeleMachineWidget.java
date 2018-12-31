package com.example.virginia.jcmachines;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.RemoteViews;

import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.machineRepository;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class jcSteeleMachineWidget extends AppWidgetProvider {
static String thisMachineName;
static String thisMachineID;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        views.setTextViewText(R.id.appwidget_text, thisMachineName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        int thisItemID;
        List<machine> machines;
        machine thisMachine;

        machineRepository mRepository=null;

        machines=mRepository.getallArticles().getValue();

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            SharedPreferences sharedPref = context.getSharedPreferences(context.getResources()
                    .getString(R.string.my_machine_to_widget_key),Context.MODE_PRIVATE);
            String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
            String defaultValueName=context.getResources().getString(R.string.my_machine_name_for_widget_default);
            thisMachineID = sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), defaultValue);
            thisMachineName=sharedPref.getString(context.getString(R.string.my_machine_name_for_widget_key), defaultValueName);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, machineListActivity.class);
            intent.putExtra(machineDetailFragment.ARG_ITEM_ID,i);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);}

    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

