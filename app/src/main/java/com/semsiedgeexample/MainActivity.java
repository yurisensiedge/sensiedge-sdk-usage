package com.semsiedgeexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.sensisdk.BleManager;
import com.sensisdk.DeviceManager;
import com.sensisdk.DeviceManagerListener;

public class MainActivity extends AppCompatActivity implements AbsListView.OnItemClickListener {

    private BleManager mBleManager;

    private NodeArrayAdapter mAdapter;

    /**
     * number of millisecond that we spend looking for a new node
     */
    private final static int SCAN_TIME_MS = 10 * 1000; //10sec


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBleManager = DeviceManager.getBleManager();
        mBleManager.addListener(devListener);

        AbsListView listView = (AbsListView) findViewById(R.id.nodeListView);
        //create the adapter and set it to the list view
        mAdapter = new NodeArrayAdapter(this);
        listView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

//        //add the already discovered nodes
//        mAdapter.addAll(mManager.getNodes());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);

        boolean isScanning = (mBleManager != null) && mBleManager.isDiscovering();
        menu.findItem(R.id.menu_stop_scan).setVisible(isScanning);
        menu.findItem(R.id.menu_start_scan).setVisible(!isScanning);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_start_scan) {
            if (null != mBleManager)
                mBleManager.startDiscovery(SCAN_TIME_MS, this);
            return true;
        }//else
        if (id == R.id.menu_stop_scan) {
            if (null != mBleManager)
                mBleManager.stopNodeDiscovery();
            return true;
        }//else

        return super.onOptionsItemSelected(item);
    }

    DeviceManagerListener devListener = new DeviceManagerListener() {
        @Override
        public void onDiscoveryFinish() {
            Log.d("sdktest", "DeviceManagerListener-onDiscoveryFinish()");
            invalidateOptionsMenu();
        }

        @Override
        public void onDiscoveryStart() {
            Log.d("sdktest", "DeviceManagerListener-onDiscoveryStart()");
            invalidateOptionsMenu();
        }
    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
