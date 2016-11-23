package com.sensiedge.uiexample;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SunnyDayScenarioTest {

    Instrumentation.ActivityMonitor monitor;
    Instrumentation mInstrumentation;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initMembers() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        monitor = mInstrumentation.addMonitor(SunnyDayScenarioTest.class.getName(), null, false);
    }

    @After
    public void waitForFinish() {
        Logger.toDebug("Wait for Activity close");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1PassOverAllFeatures() {
        int iInx;
        connectSensible();
        for (iInx = 0; iInx < 6; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }
        for (iInx = 0; iInx < 6; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }

        for (iInx = 6; iInx < 10; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }
        for (iInx = 6; iInx < 10; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }

        for (iInx = 10; iInx < 14; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }
        for (iInx = 10; iInx < 14; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }
    }

    @Test
    public void test2SpecificFeaturesOpen() {
        connectSensible();
//        clickOnBatTempHumid();
        clickOnMagnetometrGyro();
        mInstrumentation.waitForMonitorWithTimeout(monitor, 3000);

    }

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.sensiedge.uiexample", appContext.getPackageName());
    }

    public void connectSensible() {
        onView(withId(R.id.menu_start_scan)).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 3000);

        onView(allOf(withId(R.id.nodeName), withText("SensiBLE"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 6000);
    }

    public void clickOnBatTempHumid() {
        onView(allOf(withText("Battery"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 1000);
        onView(allOf(withText("Temperature"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 1000);
        onView(allOf(withText("Humidity"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 1000);
    }

    public void clickOnMagnetometrGyro() {
        onView(allOf(withText("Magnetometer"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        onView(allOf(withText("Gyroscope"))).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
    }

}
