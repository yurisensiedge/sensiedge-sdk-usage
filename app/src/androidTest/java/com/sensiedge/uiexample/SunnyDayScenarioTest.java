package com.sensiedge.uiexample;

import android.app.Instrumentation;
import android.os.Environment;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SunnyDayScenarioTest {

    private final static int TEST_ALL_FEATURES_TIMEOUT_MS = 800;

    private Instrumentation.ActivityMonitor monitor;
    private Instrumentation mInstrumentation;
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initMembers() {
        mInstrumentation = getInstrumentation();
        monitor = mInstrumentation.addMonitor(SunnyDayScenarioTest.class.getName(), null, false);
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    @After
    public void waitForFinish() {
        Logger.toDebug("Wait for Activity close");
        try { Thread.sleep(5000); } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        mInstrumentation.waitForMonitorWithTimeout(monitor, 5000);
    }

    @Test
    public void test1PassOverAllFeatures() {
        int iInx;
        connectSensible();

        for (iInx = 6; iInx < 14; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, TEST_ALL_FEATURES_TIMEOUT_MS);
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, 500);
        }
        for (iInx = 2; iInx < 4; iInx++) {
            onData(anything()).inAdapterView(withId(R.id.featureList)).atPosition(iInx).perform(click());
            mInstrumentation.waitForMonitorWithTimeout(monitor, TEST_ALL_FEATURES_TIMEOUT_MS);
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
        takeScreenShot();
    }

    public void connectSensible() {
        onView(withId(R.id.menu_start_scan)).perform(click());
        mInstrumentation.waitForMonitorWithTimeout(monitor, 3000);
        takeScreenShot();
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

    public static String getTimeStampNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static long getTimeFromBoot() {
        return (System.currentTimeMillis() - SystemClock.elapsedRealtime());
    }

    public void takeScreenShot() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "SensiedgeUITest-" + getClass().getSimpleName() + "-" + getTimeFromBoot() + ".png";
        mDevice.takeScreenshot(new File(file, fileName));
    }

}
