package edu.uw.tacoma.css.diseasemap;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.uw.tacoma.css.diseasemap.account.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ColorsActivityTest {

    @Rule
    public ActivityTestRule<ColorsActivity> mActivityRule = new
            ActivityTestRule<>(ColorsActivity.class);

    @Test
    public void testSetLeastInfectedColor(){
        onView(withId(R.id.cool_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Green"))).perform(click());
        onView(withId(R.id.cool_spinner)).check(matches(withSpinnerText(containsString("Green"))));
        onView(withId(R.id.btn_colors_selected)).perform(click());
    }

    @Test
    public void testSetMostInfectedColor(){
        onView(withId(R.id.warm_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Red"))).perform(click());
        onView(withId(R.id.warm_spinner)).check(matches(withSpinnerText(containsString("Red"))));
        onView(withId(R.id.btn_colors_selected)).perform(click());
    }

}
