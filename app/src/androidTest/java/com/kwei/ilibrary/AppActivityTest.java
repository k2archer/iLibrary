package com.kwei.ilibrary;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AppActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void login() {
        /*** login ***/
        onView(withId(R.id.ed_user_name)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ed_user_password)).perform(typeText("admin"), closeSoftKeyboard());

        onView(withText("Login")).perform(click());

        // verify
        onView(withId(R.id.menu_home_bt)).check(matches(isDisplayed()));
    }

}
