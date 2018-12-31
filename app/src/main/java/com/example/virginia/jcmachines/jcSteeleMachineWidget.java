package com.example.virginia.jcmachines;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.machineRepository;

import java.util.List;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class jcSteeleMachineWidget extends AppWidgetProvider {
    static String thisMachineName;
    static String thisMachineID;
    static String thisMachineImageLink;
    AppWidgetTarget appWidgetTarget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.plant(new Timber.DebugTree());
        //None of the manual updates to the remoteview Are actually done here but called
        //in the method on update below
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        views.setTextViewText(R.id.appwidget_text, thisMachineName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Timber.d("Going to Add widget text: " + thisMachineName + " and " + thisMachineID);

    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, jcSteeleMachineWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            SharedPreferences sharedPref = context.getSharedPreferences(context.getResources()
                    .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
            String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
            String defaultValueName = context.getResources().getString(R.string.my_machine_name_for_widget_default);
            thisMachineID = sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), defaultValue);
            thisMachineName = sharedPref.getString(context.getString(R.string.my_machine_name_for_widget_key), defaultValueName);
            thisMachineImageLink = sharedPref.getString(context.getString(R.string.my_machine_pic_link_for_widget_key), "http");

            Timber.d("Going to add this link: " + thisMachineImageLink);
            Timber.d("Going to add this name: " + thisMachineName);
            Timber.d("Going to add this id: " + thisMachineName);
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, machineListActivity.class);
            intent.putExtra(machineDetailFragment.ARG_ITEM_ID, thisMachineID);
            intent.putExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET,"true");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            views.setTextViewText(R.id.appwidget_text, thisMachineName);
            //only Update picture if Link is different than NA
            if (!thisMachineImageLink.equals("na")) {
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
                        .load(thisMachineImageLink)
                        .apply(options)
                        .into(appWidgetTarget);
            } else {
                views.setViewVisibility(R.id.appWidget_machine_picture, View.INVISIBLE);
            }
            Timber.d("On Update: Going to Add widget text: " + thisMachineName + "and " + thisMachineID);

            // Tell the AppWidgetManager to perform an update on the current app widget
            pushWidgetUpdate(context, views);
        }

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

