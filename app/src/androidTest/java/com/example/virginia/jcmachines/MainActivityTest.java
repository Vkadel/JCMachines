package com.example.virginia.jcmachines;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction supportVectorDrawablesButton = onView(
                allOf(withId(R.id.email_button), withText("Sign in with email"),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.container),
                                                0)),
                                0)));
        supportVectorDrawablesButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email_layout),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("vkadel@outlook.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.email), withText("vkadel@outlook.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email_layout),
                                        0),
                                0)));
        textInputEditText2.perform(pressImeActionButton());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.password_layout),
                                        0),
                                0)));
        textInputEditText3.perform(scrollTo(), replaceText("gabriel22"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_done), withText("Sign in"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatButton.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.machine_name), withText("45AT Extruder"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("45AT Extruder")));

        ViewInteraction imageView = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withText("JC Steele Machines"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.app_bar),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("JC Steele Machines")));

        ViewInteraction textView3 = onView(
                allOf(withText("JC Steele Machines"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.app_bar),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("JC Steele Machines")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.machine_name), withText("90AD Extruder"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("90AD Extruder")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.machine_name), withText("90AD Extruder"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("90AD Extruder")));

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.machine_list),
                                childAtPosition(
                                        withId(R.id.pull_refresh_machine_list),
                                        0)),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button = onView(
                allOf(withId(R.id.eff_calculation),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                6),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.make_widget),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                5),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.data_sheet_tv), withText("SEE DATA SHEET"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                2),
                        isDisplayed()));
        textView6.check(matches(withText("SEE DATA SHEET")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.spare_parts_tv), withText("SEE SPARE PARTS LIST"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                4),
                        isDisplayed()));
        textView7.check(matches(withText("SEE SPARE PARTS LIST")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.lubrication_chart_tv), withText("SEE LUBRICATION CHART"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                3),
                        isDisplayed()));
        textView8.check(matches(withText("SEE LUBRICATION CHART")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.description_TV), withText("Mid-size stiff extruder"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                1),
                        isDisplayed()));
        textView9.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.make_widget), withText("Select a machine to Widget"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                5)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.eff_calculation), withText("Calculate efficiency"),
                        childAtPosition(
                                allOf(withId(R.id.machine_detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView2),
                                                0)),
                                6)));
        appCompatButton3.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_company_name_et),
                        childAtPosition(
                                allOf(withId(R.id.company_name_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("company"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.column_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                2)));
        appCompatEditText2.perform(scrollTo(), replaceText("14"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.ax_material_section),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                2)));
        appCompatEditText3.perform(scrollTo(), replaceText("78"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.auge_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.auger_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                14)),
                                1)));
        appCompatEditText4.perform(scrollTo(), replaceText("52"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        pressBack();

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.efficiency_tv), withText("1.0"),
                        childAtPosition(
                                allOf(withId(R.id.efficiency_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                12)),
                                1),
                        isDisplayed()));
        textView10.check(matches(withText("1.0")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.eff_calculation_collapsing_barlayuout), withContentDescription("Efficiency Calculator"),
                                childAtPosition(
                                        withId(R.id.app_bar),
                                        0)),
                        0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction imageView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.eff_calculation_collapsing_barlayuout), withContentDescription("Efficiency Calculator"),
                                childAtPosition(
                                        withId(R.id.app_bar),
                                        0)),
                        0),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction switchMaterial = onView(
                allOf(withId(R.id.know_column_speed), withText("Known"),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                0)));
        switchMaterial.perform(scrollTo(), click());

        ViewInteraction switchMaterial2 = onView(
                allOf(withId(R.id.know_column_speed), withText("Not known"),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                0)));
        switchMaterial2.perform(scrollTo(), click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.cuts_per_sec_layout),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                6),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction switchMaterial3 = onView(
                allOf(withId(R.id.know_column_speed), withText("Known"),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                0)));
        switchMaterial3.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.cut_rate_et),
                        childAtPosition(
                                allOf(withId(R.id.cut_rate_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                6)),
                                1),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.estimation_waste_et),
                        childAtPosition(
                                allOf(withId(R.id.estimation_waste_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                7)),
                                1),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.estimation_waste_et),
                        childAtPosition(
                                allOf(withId(R.id.estimation_waste_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                7)),
                                1),
                        isDisplayed()));
        editText3.check(matches(isDisplayed()));

        ViewInteraction switchMaterial4 = onView(
                allOf(withId(R.id.known_product_section), withText("Known"),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                0)));
        switchMaterial4.perform(scrollTo(), click());

        ViewInteraction switchMaterial5 = onView(
                allOf(withId(R.id.known_product_section), withText("Not known"),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                0)));
        switchMaterial5.perform(scrollTo(), click());

        ViewInteraction switchMaterial6 = onView(
                allOf(withId(R.id.known_product_section), withText("Known"),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                0)));
        switchMaterial6.perform(scrollTo(), click());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.brick_void_percentage_et),
                        childAtPosition(
                                allOf(withId(R.id.brick_void_percentage_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                12)),
                                1),
                        isDisplayed()));
        editText4.check(matches(isDisplayed()));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.brick_void_percentage_et),
                        childAtPosition(
                                allOf(withId(R.id.brick_void_percentage_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                12)),
                                1),
                        isDisplayed()));
        editText5.check(matches(isDisplayed()));

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.brick_h_et),
                        childAtPosition(
                                allOf(withId(R.id.brick_height_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                11)),
                                1),
                        isDisplayed()));
        editText6.check(matches(isDisplayed()));

        ViewInteraction editText7 = onView(
                allOf(withId(R.id.brick_w_et),
                        childAtPosition(
                                allOf(withId(R.id.brick_width_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                10)),
                                1),
                        isDisplayed()));
        editText7.check(matches(isDisplayed()));

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.brick_w_et),
                        childAtPosition(
                                allOf(withId(R.id.brick_width_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                10)),
                                1),
                        isDisplayed()));
        editText8.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.column_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                2)));
        appCompatEditText5.perform(scrollTo(), replaceText("6"), closeSoftKeyboard());

        ViewInteraction switchMaterial7 = onView(
                allOf(withId(R.id.know_column_speed), withText("Not known"),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                0)));
        switchMaterial7.perform(scrollTo(), click());

        pressBack();

        ViewInteraction switchMaterial8 = onView(
                allOf(withId(R.id.known_product_section), withText("Not known"),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                0)));
        switchMaterial8.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.column_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                2)));
        appCompatEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.column_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                2)));
        appCompatEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.ax_material_section),
                        childAtPosition(
                                allOf(withId(R.id.cuts_per_sec_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                9)),
                                2)));
        appCompatEditText8.perform(scrollTo(), replaceText("45"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.column_speed_et),
                        childAtPosition(
                                allOf(withId(R.id.column_speed_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                7)),
                                2)));
        appCompatEditText9.perform(scrollTo(), replaceText("455"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
