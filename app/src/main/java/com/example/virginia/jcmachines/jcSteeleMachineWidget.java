package com.example.virginia.jcmachines;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
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
    Boolean islarge=false;
    Boolean isSmall=false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.plant(new Timber.DebugTree());
        //None of the manual updates to the remoteview Are actually done here but called
        //in the method on update below
        // Construct the RemoteViews object

    }

    public void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        Timber.d("AtWidget pushWidgetUpdate");
        ComponentName myWidget = new ComponentName(context, jcSteeleMachineWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
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


        Boolean isOneItemUpdate = false;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);

        GetSharedPreferences(context);

        if (appWidgetIds.length == 1) {
            thisWidgetid = appWidgetIds[0];
        }

        if (appWidgetIds.length == 1) {
            List<Integer> myList = Arrays.stream(allIds).boxed().collect(Collectors.toList());
            int item = myList.indexOf(thisWidgetid);
            isOneItemUpdate = true;
            updateOneWidget(context, appWidgetIds[0], allIds,item);
            if (allIds.length > 1 && !dontUpdate) {
                //Trigger Update if there if this is not the onlywidget with all available widgets
                onUpdate(context, appWidgetManager, allIds);
                dontUpdate=true;
            }
        }

        if (appWidgetIds.length > 1) {
            for (int i = 0; i < appWidgetIds.length; i++) {
                updateOneWidget(context, appWidgetIds[i], allIds,i);
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


    private void updateOneWidget(Context context, int appWidgetId, int[] allIds,int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.jc_steele_machine_widget);
        //Only update if there is a widget associated to this position
        List<Integer> listAllIds = Arrays.stream(allIds).boxed().collect(Collectors.toList());
        int index=listAllIds.indexOf(appWidgetId);
        int i=1;
        int pos=position;



        //Adjust position
        while (pos>machineWidgetPrefArrayID.size()){
            pos=pos-(allIds.length/machineWidgetPrefArrayID.size());
            i=i+1;
        }

        if ( index!= -1 && machineWidgetPrefArrayID.size() > position) {

            views.setViewVisibility(R.id.widgetProgress, View.VISIBLE);
            views.setTextViewText(R.id.appwidget_text, context.getResources().getString(R.string.loading));
            views.setTextViewText(R.id.appwidget_text, machineWidgetPrefArrayName.get(Integer.valueOf(position)));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            views.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_PX, 50f);}

            Timber.d("AtWidget pushWidgetUpdate for position:" + position);
            Timber.d("AtWidget Going to add to this view: " + views.getLayoutId());
            Timber.d("AtWidget Going to add this imagelink: " + machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)));
            Timber.d("AtWidget Going to add this name: " + machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            Timber.d("AtWidget Going to add this id: " + machineWidgetPrefArrayName.get(Integer.valueOf(position)));

            //Find out screen Size
            getScreenSize(context);

            Intent intent=new Intent();
            // Create an Intent to launch Detail activity if the screen is Small
            if(isSmall){
                intent.setClass(context,machineDetailActivity.class);}
            // Create an Intent to launch List activity if the screen is Large
            else if(islarge){
                intent.setClass(context,machineListActivity.class);
            }

            intent.putExtra(machineDetailFragment.ARG_ITEM_ID, machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            intent.putExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET, "true");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(machineWidgetPrefArrayID.get(Integer.valueOf(position))),
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.appWidget_machine_picture, pendingIntent);

            //only Update picture if Link is different than NA
            if (!machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)).equals("na") || machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)) != null) {
                RequestOptions options = new RequestOptions().
                        override(100, 100);
                appWidgetTarget = new AppWidgetTarget(context, R.id.appWidget_machine_picture, views, appWidgetId) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Timber.d("Resource Ready");
                        views.setViewVisibility(R.id.widgetProgress, View.GONE);
                        super.onResourceReady(resource, transition);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        views.setViewVisibility(R.id.widgetProgress, View.GONE);
                        super.onLoadFailed(errorDrawable);
                    }
                };
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(machineWidgetPrefArrayImageLink.get(Integer.valueOf(position)))
                        .apply(options)
                        .into(appWidgetTarget);
                // Get the layout for the App Widget and attach an on-click listener
                // to the button
            }

            Timber.d("AtWidget On Update: Going to Add widget text: " + machineWidgetPrefArrayName.get(Integer.valueOf(position)) + "and " + machineWidgetPrefArrayID.get(Integer.valueOf(position)));
            // Tell the AppWidgetManager to perform an update on the current app widget

        }
        else{
            views.setViewVisibility(R.id.widgetProgress, View.GONE);
            views.setTextViewText(R.id.withText,context.getResources().getString(R.string.add_a_widget));
        }


    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        GetSharedPreferences(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            Boolean exist = machineWidgetPrefArrayWidgetId.contains(Integer.toString(appWidgetIds[i]));
            if (exist) {
                machineWidgetPrefArrayWidgetId.remove(Integer.toString(appWidgetIds[i]));
            }
        }
        //Final clean
        int[] allIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
        if (allIds.length < machineWidgetPrefArrayWidgetId.size()) {
            //Do A full clean
            Timber.d("AtWidget: Doing full clean");
            List<String> convertedAllIDs = Arrays.asList(allIds).stream().map(ints -> ints.toString()).collect(Collectors.toList());
            machineWidgetPrefArrayWidgetId.remove(convertedAllIDs);
        }
        Timber.d("AtWidget: Final widgetID array " + machineWidgetPrefArrayWidgetId.toString());
    }

    void getScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                toastMsg = "Extra Large screen";
                islarge=true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                islarge=true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                isSmall=true;
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                isSmall=true;
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        Timber.d("Screen Size is: "+toastMsg);
    }

    @Override
    public void onDisabled(Context context) {
        //To prevent widget configurations and state to be lost overnight
        onUpdate(context, AppWidgetManager.getInstance(context),
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class)));
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //To prevent widget configurations and state to be lost overnight
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, jcSteeleMachineWidget.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        onUpdate(context, AppWidgetManager.getInstance(context),
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class)));

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}


