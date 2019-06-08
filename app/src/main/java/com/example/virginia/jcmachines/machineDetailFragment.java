package com.example.virginia.jcmachines;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.color;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;
import com.example.virginia.jcmachines.utils.SendALongToast;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * A fragment representing a single machine detail screen.
 * This fragment is either contained in a {@link machineListActivity}
 * in two-pane mode (on tablets) or a {@link machineDetailActivity}
 * on handsets.
 */
public class machineDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_CAME_FROM_WIDGET = "came_from_widget";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String ARG_ITEM_ROOT_VIEW = "root_view";
    public static final String ARG_IS_TWO_PANE = "is_two_pane";
    public static final String EFF_ARG_ITEM_ID="eff_cal_id";
    List<machine> machineList;
    machine thisMachine;
    com.example.virginia.jcmachines.viewmodels.machineViewModel machineViewModel;
    View rootView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView machineDetailAppBarBackgroundIV;
    String thisMachineId;
    Activity activity;
    Boolean isTwopane;
    private Context context;
    String TAG="machinedetailFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public machineDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        if (this.getArguments().containsKey(machineDetailFragment.ARG_ITEM_ID)) {
            //Subscribe to the activity model
            this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
            //Check if this is the first the fragment was created
            if (savedInstanceState == null) {
                this.thisMachineId = this.getArguments().getString(machineDetailFragment.ARG_ITEM_ID);
                this.isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
                //observe the model

                this.machineViewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<machine> machines) {
                        machineList = machines;
                        thisMachine = machineList.get(Integer.valueOf(thisMachineId));
                        updateUI(rootView);
                    }
                });
            }
            //get the existing Model and get all machines
            else {
                this.machineList = this.machineViewModel.getMachines().getValue();
                if (savedInstanceState.getString(machineDetailFragment.ARG_ITEM_ID) != null) {
                    this.thisMachineId = savedInstanceState.getString(machineDetailFragment.ARG_ITEM_ID);
                } else {
                    thisMachineId = "0";
                }
                this.thisMachine = this.machineList.get(Integer.valueOf(thisMachineId));
                this.isTwopane = savedInstanceState.getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
            }
            this.activity = getActivity();
        }
        if (getActivity().getIntent().hasExtra(SparePartDetailFragment.ARG_ITEM_ID)) {
            thisMachineId = getActivity().getIntent().getStringExtra(SparePartDetailFragment.ARG_ITEM_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(layout.machine_detail, container, false);
        context = getContext();
        // Show the selected machine content as text in a TextView.
        if (this.thisMachine != null) {
            this.updateUI(this.rootView);
        }
        return this.rootView;
    }

    public void updateUI(View rootView) {
        if (!this.isTwopane && rootView!=null) {
            this.collapsingToolbarLayout = this.activity.findViewById(id.detail_CollapsingToolbarLayout);
            if(collapsingToolbarLayout !=null){
                this.collapsingToolbarLayout.setTitle(this.thisMachine.getMachineFullName());
                this.collapsingToolbarLayout.setExpandedTitleColor(this.getResources().getColor(color.colorAccent));
            }
            this.machineDetailAppBarBackgroundIV = this.activity.findViewById(id.app_bar_machine_image);
            if(machineDetailAppBarBackgroundIV!=null){
            Glide.with(this).load(this.thisMachine.getLargeImageOne()).into(this.machineDetailAppBarBackgroundIV);}
        }

        TextView description_tv = rootView.findViewById(R.id.description_TV);
        TextView data_sheet_tv = rootView.findViewById(id.data_sheet_tv);
        TextView lubrication_chart_tv = rootView.findViewById(id.lubrication_chart_tv);
        TextView spare_parts_list_tv = rootView.findViewById(id.spare_parts_tv);
        final Button isThisAWidget = rootView.findViewById(id.make_widget);
        final Button goToformula=rootView.findViewById(id.eff_calculation);
        Boolean isCurrentMachineWidget = IstheCurrentMachineaWidget();

        if (isCurrentMachineWidget) {
            isThisAWidget.setText(context.getResources().getString(R.string.already_widget));
        }

        String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
        String defaultValueName = context.getResources().getString(R.string.my_machine_name_for_widget_default);
        //TODO: Split the different Machines/Identify if current machine is already a widget


        isThisAWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Only add if item is not the dummy data
                //Concat the items
                if (thisMachine != null) {

                    //Select wether you need to add or take out the widget
                    if(isCurrentMachineWidget){
                        JsontoArraytoJsonTakeOut();
                        new SendALongToast(context,getActivity().getResources().getString(R.string.will_remove_thisWidget)
                                + thisMachine.getMachineFullName()).show();
                        isThisAWidget.setVisibility(View.INVISIBLE);


                    }
                    else{
                        JsontoArraytoJson();
                        new SendALongToast(context,getActivity().getResources().getString(R.string.updating_your_widget_with)
                                + thisMachine.getMachineFullName()).show();
                        isThisAWidget.setVisibility(View.INVISIBLE);
                        }
                }
            }



            private void JsontoArraytoJson() {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().
                        getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
                //TODO: Get existing Widgets before adding new

                String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
                String defaultValueName = context.getResources().getString(R.string.my_machine_name_for_widget_default);

                Gson prevMachineIDjson = new Gson();
                Gson prevMachineNamejson = new Gson();
                Gson prevMachineImageLinkjson = new Gson();
                Gson prevMachineWidgetIdjson = new Gson();
                ArrayList<String> machineWidgetPrefArrayID = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayName = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayImageLink = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayWidgetId = new ArrayList<>();
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
                    ArrayList<String> myTransicionArray = prevMachineIDjson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), ""), ArrayList.class);
                    if (myTransicionArray != null) {
                        machineWidgetPrefArrayWidgetId.addAll(myTransicionArray);
                    }
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "onClick: Could not create JSON from machineWidgetPrefArrayWidgetId ", new Throwable());
                    e.printStackTrace();

                }

                SharedPreferences.Editor editor = sharedPref.edit();
                //Add JsonItems only if it doesnt exist
                if(!machineWidgetPrefArrayID.contains(Integer.toString(thisMachine.getId()))){
                    machineWidgetPrefArrayID.add(Integer.toString(thisMachine.getId()));
                }
                if(!machineWidgetPrefArrayName.contains(thisMachine.getMachineFullName())){
                    machineWidgetPrefArrayName.add(thisMachine.getMachineFullName());
                }
               if(!machineWidgetPrefArrayImageLink.contains(thisMachine.getLargeImageOne())){
                   machineWidgetPrefArrayImageLink.add(thisMachine.getLargeImageOne());
               }
                editor.putString(getString(R.string.my_machine_to_widget_key), prevMachineIDjson.toJson(machineWidgetPrefArrayID));
                editor.putString(getString(R.string.my_machine_name_for_widget_key), prevMachineNamejson.toJson(machineWidgetPrefArrayName));
                editor.putString(getString(R.string.my_machine_pic_link_for_widget_key), prevMachineImageLinkjson.toJson(machineWidgetPrefArrayImageLink));
                editor.commit();
            }
            private void JsontoArraytoJsonTakeOut() {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().
                        getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);
                //TODO: Get existing Widgets before adding new

                String defaultValue = context.getResources().getString(R.string.my_machine_to_widget_default);
                String defaultValueName = context.getResources().getString(R.string.my_machine_name_for_widget_default);

                Gson prevMachineIDjson = new Gson();
                Gson prevMachineNamejson = new Gson();
                Gson prevMachineImageLinkjson = new Gson();
                Gson prevMachineWidgetIdjson = new Gson();
                ArrayList<String> machineWidgetPrefArrayID = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayName = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayImageLink = new ArrayList<>();
                ArrayList<String> machineWidgetPrefArrayWidgetId = new ArrayList<>();
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

                SharedPreferences.Editor editor = sharedPref.edit();
                //Add JsonItems
                int position=machineWidgetPrefArrayID.indexOf(Integer.toString(thisMachine.getId()));
                machineWidgetPrefArrayID.remove(position);
                machineWidgetPrefArrayName.remove(position);
                machineWidgetPrefArrayImageLink.remove(position);
                machineWidgetPrefArrayWidgetId.remove(position);
                editor.putString(getString(R.string.my_machine_to_widget_key), prevMachineIDjson.toJson(machineWidgetPrefArrayID));
                editor.putString(getString(R.string.my_machine_name_for_widget_key), prevMachineNamejson.toJson(machineWidgetPrefArrayName));
                editor.putString(getString(R.string.my_machine_pic_link_for_widget_key), prevMachineImageLinkjson.toJson(machineWidgetPrefArrayImageLink));
                editor.putString(getString(R.string.my_machine_widget_id_key), prevMachineImageLinkjson.toJson(machineWidgetPrefArrayImageLink));
                editor.commit();
            }
        });

        goToformula.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AddEffCalculationActivity.class);
                intent.putExtra(ARG_ITEM_ID,thisMachineId);
                startActivity(intent);
            }
        });

        description_tv.setText(this.thisMachine.getDescription());
        ImageView dimensions_tv_inline = rootView.findViewById(id.inline_dimensions_image);

        //Hide Image View for dimensios if there is no picture for it
        if (!(this.thisMachine.getInlIneInstallImage()).equals("na")) {
            dimensions_tv_inline.setVisibility(View.VISIBLE);
            Glide.with(this).load(this.thisMachine.getInlIneInstallImage()).into(dimensions_tv_inline);
        } else {
            dimensions_tv_inline.setVisibility(View.GONE);
        }

        //onClick listeners for pdf files. Hide the View if file is not available

        if (!(this.thisMachine.getLubricationChartLink()).equals("na")) {
            lubrication_chart_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Pdf_viewer_Activity.class);
                    intent.putExtra(Pdf_viewer_Activity.ARG_LINK, String.valueOf(thisMachine.getLubricationChartLink()));
                    intent.putExtra(Pdf_viewer_Activity.ARG_MACHINE_ID, thisMachine.getId());
                    intent.putExtra(Pdf_viewer_Activity.ARG_DOCUMENT_ID, Pdf_viewer_Activity.ARG_DOCUMENT_TYPE_LUBRICATION_CHART);
                    context.startActivity(intent);
                }
            });
        } else {
            lubrication_chart_tv.setVisibility(View.GONE);
        }

        //Creating intent to go to activity displaying list of SpareParts for this machine
        if ((this.thisMachine.getSpareParts() != null)) {
            spare_parts_list_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SparePartListActivity.class);
                    intent.putExtra(SparePartListActivity.ARG_ITEM_ID, thisMachineId);
                    context.startActivity(intent);
                }
            });
        } else {
            spare_parts_list_tv.setVisibility(View.GONE);
        }

        if (!(this.thisMachine.getDatasheetLink()).equals("na")) {
            data_sheet_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Pdf_viewer_Activity.class);
                    intent.putExtra(Pdf_viewer_Activity.ARG_LINK, String.valueOf(thisMachine.getDatasheetLink()));
                    intent.putExtra(Pdf_viewer_Activity.ARG_MACHINE_ID, thisMachine.getId());
                    intent.putExtra(Pdf_viewer_Activity.ARG_DOCUMENT_ID, Pdf_viewer_Activity.ARG_DOCUMENT_TYPE_TECHNICAL_SHEET);
                    context.startActivity(intent);
                }
            });
        } else {
            data_sheet_tv.setVisibility(View.GONE);
        }
    }

    private Boolean IstheCurrentMachineaWidget() {
        //Todo: Check if machine has a widget
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources()
                .getString(R.string.my_machine_to_widget_key), Context.MODE_PRIVATE);

        Gson myMachineIDJson = new Gson();
        ArrayList<String> myMachinesArray = new ArrayList<>();
        try {
            ArrayList<String> myTransicionArray = myMachineIDJson.fromJson(sharedPref.getString(context.getString(R.string.my_machine_to_widget_key), ""), ArrayList.class);
            if (myTransicionArray != null) {
                myMachinesArray.addAll(myTransicionArray);
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Trying to Create Array of Machine ids", new Throwable());
            e.printStackTrace();
        }
        return myMachinesArray.indexOf(thisMachineId) != -1;
    }
    private void triggerWidgetUdate() {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, jcSteeleMachineWidget.class));
        //Calling a widget Update manually
        jcSteeleMachineWidget myWidget;
        myWidget = new jcSteeleMachineWidget();
        myWidget.onUpdate(context, AppWidgetManager.getInstance(context), ids);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(this.thisMachine.getId()));
        super.onSaveInstanceState(outState);
    }

}
