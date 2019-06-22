package com.example.virginia.jcmachines;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewFinder;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.example.virginia.jcmachines.utils.SendALongToast;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class CheckRecyclerAndDetailActivity {

    @Rule
    public ActivityScenarioRule<machineListActivity> machineListRule=new ActivityScenarioRule<>(machineListActivity.class);

    @Test
    public void MainActivityTestStartPortrait(){
        if(getActivityStart().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            testPortrait();
        }
        if(getActivityStart().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            testLandscape();
        }
    }

    private void testLandscape() {
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description_TV)).check(matches(withText("Mid-size stiff extruder")));
        onView(withId(R.id.eff_calculation)).check(matches(isClickable()));
        pressBack();
        //check if second view does not show the eff calculation
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.eff_calculation)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.make_widget)).perform(click());
        pressBack();
        onView(withId(R.id.machine_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.eff_calculation)).perform(click());
        onView(withId(R.id.enter_company_name_et)).perform(typeText("veryLongCompanyName Test"+ Calendar.getInstance().getTime()));
        onView(withId(R.id.column_speed_et)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.ax_material_section)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.auge_speed_et)).perform(click()).perform(typeText("2"));
        onView(withId(R.id.efficiency_tv)).check(matches(withText("70.0")));
        onView(withId(R.id.fab)).perform(click());
        Activity myactivity=getActivity();
        myactivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.efficiency_tv)).check(matches(withText("70.0")));
        onView(withId(R.id.ax_material_section)).check(matches(isEnabled()));
        myactivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void testPortrait(){
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description_TV)).check(matches(withText("Mid-size stiff extruder")));
        onView(withId(R.id.eff_calculation)).check(matches(isClickable()));
        pressBack();
        //check if second view does not show the eff calculation
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.eff_calculation)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.make_widget)).perform(click());
        pressBack();
        onView(withId(R.id.machine_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.eff_calculation)).perform(click());
        onView(withId(R.id.enter_company_name_et)).perform(typeText("veryLongCompanyName Test"+ Calendar.getInstance().getTime()));
        onView(withId(R.id.column_speed_et)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.ax_material_section)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.auge_speed_et)).perform(click()).perform(typeText("2"));
        onView(withId(R.id.efficiency_tv)).check(matches(withText("70.0")));
        onView(withId(R.id.fab)).perform(click());
        Activity myactivity=getActivity();
        myactivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.efficiency_tv)).check(matches(withText("70.0")));
        onView(withId(R.id.ax_material_section)).check(matches(isEnabled()));
        myactivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static Activity getActivity() {
        final Activity[] currentActivity = new Activity[1];
        onView(withId(R.id.fab)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view.getContext() instanceof Activity) {
                    Activity activity1 = ((Activity)view.getContext());
                    currentActivity[0] = activity1;
                }
            }
        });
        return currentActivity[0];
    }

    public Activity getActivityStart(){
        final Activity[] myActivity = new Activity[1];
        machineListRule.getScenario().onActivity(new ActivityScenario.ActivityAction<machineListActivity>() {
            @Override
            public void perform(machineListActivity activity) {
                myActivity[0] =activity;
            }
        });
        return myActivity[0];
    }
}
