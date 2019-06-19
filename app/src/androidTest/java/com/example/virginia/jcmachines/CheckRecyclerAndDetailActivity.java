package com.example.virginia.jcmachines;

import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
    public void MainActivityTest(){
        Intent intent=new Intent();
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description_TV)).check(matches(withText("Mid-size stiff extruder")));
        onView(withId(R.id.eff_calculation)).check(matches(isClickable()));
        pressBack();
        //check if second view does not show the eff calculation
        onView(withId(R.id.machine_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.eff_calculation)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        /*onView(withId(R.id.make_widget)).perform(click());*/
        pressBack();
        onView(withId(R.id.machine_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.eff_calculation)).perform(click());
        onView(withId(R.id.column_speed_et)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.ax_material_section)).perform(click()).perform(typeText("50"));
        onView(withId(R.id.auge_speed_et)).perform(click()).perform(typeText("2"));
        onView(withId(R.id.efficiency_tv)).check(matches(withText("70.0")));
        onView(withId(R.id.fab)).perform(click());
    }

}
