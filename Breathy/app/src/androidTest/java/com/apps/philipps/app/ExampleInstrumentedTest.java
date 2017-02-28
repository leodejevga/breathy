package com.apps.philipps.app;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.apps.philipps.app.activities.Menu;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    ArrayList<String> gameNameList = new ArrayList<>();
    @Rule
    public ActivityTestRule<Menu> mActivityRule = new ActivityTestRule<>(
            Menu.class);

    @Before
    public void initValidString() {
        gameNameList.add("Audio Surf");
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.apps.philipps.app", appContext.getPackageName());
    }

    @Test
    public void menuActivityUITest() throws Exception {
        /* When the app starts, the 2 buttons (Main game and main option) should be showed*/
        onView(withId(R.id.mainGames)).check(matches(isDisplayed()));
        onView(withId(R.id.mainOptions)).check(matches(isDisplayed()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void gameActivityUITest() throws Exception {
        String testGame = gameNameList.get(0);
        /* After clicking on play button, the play activity should be showed*/
        onView(withId(R.id.mainGames)).perform(click());
        onView(withId(R.id.videoView)).check(matches(isDisplayed()));
        onView(withId(R.id.games)).check(matches(isDisplayed()));
        for (int i = 0; i < gameNameList.size(); i++) {
            onView(withText(gameNameList.get(i))).check(matches(isDisplayed()));
        }
        /*Click on the first game, and then dispose the dialog box*/
        onView(withText(testGame)).perform(click());
        onView(withText("Can't play this video.")).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        /*Check if the byu button available*/
        onView(withId(R.id.buttonBuy)).check(matches(isDisplayed()));
        /*Option button and play button should do nothing, because no game is available*/
        onView(withId(R.id.buttonOptions)).perform(click());
        onView(withText("The game Audio Surf was not bought")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        /*Now we buy a game to test*/
        onView(withId(R.id.buttonBuy)).perform(click());
        onView(withText("Congratulations! You bought " + testGame)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        //TODO Option button is now available, gameoption activity should be checked
        onView(withId(R.id.buttonOptions)).perform(click());


    }

    //TODO @Test If play button is available after device is connected via bluetooth and a game is bought


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void mainOptionUITest() throws Exception {
        /* After clicking on option button, the option activity should be showed*/
        onView(withId(R.id.mainOptions)).perform(click());
        scrollAndCheckView(R.id.name);
        scrollAndCheckView(R.id.email);
        scrollAndCheckView(R.id.age);
        scrollAndCheckView(R.id.beginner);
        scrollAndCheckView(R.id.expert);
        scrollAndCheckView(R.id.doctoremail);
        scrollAndCheckView(R.id.saveandback);
        scrollAndCheckView(R.id.sendemailtodoctor);
        scrollAndCheckView(R.id.activate_bt_button);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void userdataStoredCorrectly() throws Exception {
        /*In this test we try to store the data in cache*/
        onView(withId(R.id.mainOptions)).perform(click());
        onView(withId(R.id.name)).perform(clearText(), typeText("mustermann"));
        onView(withId(R.id.email)).perform(clearText(), typeText("mustermann@musterserver.com"));
        onView(withId(R.id.age)).perform(clearText(), typeText(String.valueOf("18")));
        onView(withId(R.id.saveandback)).perform(scrollTo());
        onView(withId(R.id.saveandback)).perform(click());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void userdataReadCorrectly() throws Exception {
        /*In this test we try to store the data in cache and then check if the data is stored correctly*/
        userdataStoredCorrectly();
        onView(withId(R.id.mainOptions)).perform(click());
        onView(withId(R.id.name)).check(matches(withText("mustermann")));
        onView(withId(R.id.email)).check(matches(withText("mustermann@musterserver.com")));
        onView(withId(R.id.age)).check(matches(withText(String.valueOf("18"))));
    }

    private void scrollAndCheckView(int viewId) {
        onView(withId(viewId)).perform(scrollTo());
        onView(withId(viewId)).check(matches(isDisplayed()));
    }
}
