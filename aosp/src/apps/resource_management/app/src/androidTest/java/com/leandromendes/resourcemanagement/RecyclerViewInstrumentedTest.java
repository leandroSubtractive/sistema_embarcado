package com.leandromendes.resourcemanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.Manifest;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewInstrumentedTest {

    private final String[] tabsName = {"Apps", "Processes"};

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // Ensures that the necessary permissions are available
    @Rule
    public GrantPermissionRule denyPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.SYSTEM_ALERT_WINDOW
    );

    /**
     * Check that the RecycleView of the corresponding tab is displayed
     */
    @Test
    public void recyclerViewAppsIsDisplayed() {
        // Select the Tab by name, and check that the right RecycleView is appearing
        onView(withText(tabsName[0])).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewApps))
                    .check(matches(isDisplayed()));

    }

    /**
     * Check that the RecycleView of the corresponding tab is displayed
     */
    @Test
    public void recyclerViewProcessIsDisplayed() {
        // Select the Tab by name, and check that the right RecycleView is appearing
        onView(withText(tabsName[1])).perform(click());
        onView(withText(tabsName[1])).check(matches(isDisplayed()));

        onView(withId(R.id.recyclerViewProcess))
                .check(matches(isDisplayed()));
    }

    /**
     * Check that the screen components are showing up
     */
    @Test
    public void screenComponentsIsDisplayed() {
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));
    }
}

