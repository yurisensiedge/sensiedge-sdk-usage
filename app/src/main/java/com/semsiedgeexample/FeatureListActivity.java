package com.semsiedgeexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sensisdk.AbstractNode;
import com.sensisdk.Logger;



/**
 * This simple activity show all the features available in a node.
 * When the user select one feature we request to receive the update notification and
 * we display it
 */
public class FeatureListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * tag used for retrieve the NodeContainerFragment
     */
    private final static String NODE_FRAGMENT = FeatureListActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";

    /**
     * tag used for store the node id that permit us to find the node selected by the user
     */
    private final static String NODE_TAG = FeatureListActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";

    /**
     * node that will stream the data
     */
    private WirelessNode mNode;

    /**
     * fragment that manage the node connection and avoid a re connection each time the activity
     * is recreated
     */
    private NodeContainerFragment mNodeContainer;

    /**
     * list view where we display the available features exported by the node
     */
    private ListView mFeatureList;

    /**
     * adapter that will build the feature item
     */
//    private ArrayAdapter<Feature> mFeatureListAdapter;

    /**
     * listener that will be used for enable the notification when the node is connected
     */
     private AbstractNode.NodeStateListener mNodeStatusListener = new AbstractNode.NodeStateListener() {
        @Override
        public void onStateChange(AbstractNode node, AbstractNode.State newState, AbstractNode.State prevState) {
            Logger.print("onStateChange(): " + node.getMac() + "; State: " + newState);
            if (newState == AbstractNode.State.Connected) {
                FeatureListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateFeatureList();
                        invalidateOptionsMenu(); //enable/disable the settings options
                    }//run
                });
            }//if
        }
    };

    /**
     * listener that will update the displayed feature data
     */
//    private Feature.FeatureListener mGenericUpdate;

    /**
     * create an intent for start this activity
     *
     * @param c    context used for create the intent
     * @param node node to use for the demo
     * @return intent for start a demo activity that use the node as data source
     */
    public static Intent getStartIntent(Context c, @NonNull AbstractNode node) {
        Intent i = new Intent(c, FeatureListActivity.class);
        i.putExtra(NODE_TAG, node.getMac());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        Logger.print("FeatureListActivity:node.getTag(): " + node.getMac());
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

//        //load the gui
//        mFeatureList = (ListView) findViewById(R.id.featureList);
//        mFeatureList.setOnItemClickListener(this);

        //find the node
        String nodeTag = getIntent().getStringExtra(NODE_TAG);
        Logger.print("FeatureListActivity:onCreate() Chosen MAC: " + nodeTag);
// TODO       mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);
        mNode = new WirelessNode("SensiBLE", nodeTag);

//        //create or recover the NodeContainerFragment
//        if (savedInstanceState == null) {
//            Intent i = getIntent();
//            mNodeContainer = new NodeContainerFragment();
//            mNodeContainer.setArguments(i.getExtras());
//
//            getFragmentManager().beginTransaction()
//                    .add(mNodeContainer, NODE_FRAGMENT).commit();
//
//        } else {
//            mNodeContainer = (NodeContainerFragment) getFragmentManager()
//                    .findFragmentByTag(NODE_FRAGMENT);
//
//        }//if-else
    }//onCreate

    /**
     * build the menu and show the item only if the service is available in the node
     *
     * @param menu menu where add the items
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Logger.print("onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_demo, menu);

// TODO       menu.findItem(R.id.menu_showDebug).setVisible(mNode.getDebug() != null);
//        menu.findItem(R.id.menu_showRegister).setVisible(mNode.getConfigRegister() != null);

        return true;
    }//onCreateOptionMenu

    /**
     * start the activity with the debug console or for manage the configuration register
     *
     * @param item item selected by the user
     * @return true if the item is handle by this method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("debugme","onOptionsItemSelected()");

        int id = item.getItemId();

        //we call keepConnectionOpen for skip the node disconnection when the activity is destroyed
        //in this way we avoid fast connection/disconnection call

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_showRegister) {
            mNodeContainer.keepConnectionOpen(true);
// TODO           startActivity(SettingsActivity.getStartIntent(this, mNode));
            return true;
        }//else
        if (id == R.id.menu_showDebug) {
            mNodeContainer.keepConnectionOpen(true);
// TODO           startActivity(DebugConsoleActivity.getStartIntent(this, mNode));
            return true;
        }//else

        return super.onOptionsItemSelected(item);
    }

    /**
     * stop all the enabled notification
     */
    private void disableNeedNotification() {
// TODO       List<Feature> features = mNode.getFeatures();
//        for (Feature f : features) {
//            if (mNode.isEnableNotification(f))
//                mNode.disableNotification(f);
//        }//for sTestFeature
    }//disableNeedNotification

    /**
     * create and populate the adapter with only the enabled features.
     */
    private void populateFeatureList() {
        Log.d("debugme","populateFeatureList()");
        if (mNode != null) {
// TODO           mFeatureListAdapter = new FeatureAdapter(this,
//                    R.layout.feature_list_item);
// TODO           List<Feature> features = mNode.getFeatures();
//            for (Feature f : features) {
//                if (f.isEnabled()) {
//                    mFeatureListAdapter.add(f);
//                }//if
//            }//for

            //set the adapter as data source for the adapter
//            mFeatureList.setAdapter(mFeatureListAdapter);

        }//if
    }//populateFeatureList


    /**
     * if the node is connected enable the gui, otherwise set a listener that will do that
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mNode.isConnected()) {
            populateFeatureList();
            invalidateOptionsMenu(); //enable/disable the settings options
        }
//        } else
//            mNode.addNodeStateListener(mNodeStatusListener);
    }//onResume


    @Override
    protected void onPause() {

        //it is safe remove also if we didn't add it
// TODO       mNode.removeNodeStateListener(mNodeStatusListener);

        //if the node is already disconnected we don't care of disable the notification
// TODO       if (mNode.isConnected()) {
//            disableNeedNotification();
//        }//if

        super.onPause();
    }//stopDemo

    /**
     * When a user select a row we enable/disable the notification for that feature
     *
     * @param adapterView list view
     * @param view        selected view
     * @param position    selected row
     * @param l           selected id
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        Feature selectedFeature = mFeatureListAdapter.getItem(position);

// TODO       if (mNode.isEnableNotification(selectedFeature)) {
//
//            selectedFeature.removeFeatureListener(mGenericUpdate);
//            mNode.disableNotification(selectedFeature);
//
//            ((TextView) view).setText(selectedFeature.getName()); //reset the cell name
//        } else {
//            //create a listener that will update the selected view
//            mGenericUpdate = new GenericFragmentUpdate((TextView) view);
//            selectedFeature.addFeatureListener(mGenericUpdate);
//
//            mNode.enableNotification(selectedFeature);
//        }//if-else
    }//onItemClick

    /**
     * extend an array adapter for change the view content, instead of used the toString result
     * we use the feature name
     */
//    private static class FeatureAdapter extends ArrayAdapter<Feature> {
//
//        /**
//         * @see ArrayAdapter#ArrayAdapter(Context, int)
//         */
//        public FeatureAdapter(Context c, int resourceId) {
//            super(c, resourceId);
//        }
//
//        /**
//         * create a text view and initialize it with the equivalent feature name
//         */
//        @Override
//        public View getView(int position, View v, ViewGroup parent) {
//            Log.d("debugme","getView()");
//
//            if (v == null) {
//                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = inflater.inflate(R.layout.feature_list_item, parent, false);
//            }
//
//            Feature f = getItem(position);
//
//            ((TextView) v).setText(f.getName());
//
//            return v;
//
//        }//getView
//
//    }//FeatureAdapter

//    /**
//     * class used for update the feature display data
//     */
//    private class GenericFragmentUpdate implements Feature.FeatureListener {
//
//        /**
//         * text view that will contain the data/name
//         */
//        final private TextView mTextView;
//
//        /**
//         * @param text text view that will show the name/values
//         */
//        public GenericFragmentUpdate(TextView text) {
//            mTextView = text;
//        }//GenericFragmentUpdate
//
//        /**
//         * set the text view text with the feature toString value
//         *
//         * @param f      feature that has received an update
//         * @param sample new data received from the feature
//         */
//        @Override
//        public void onUpdate(Feature f, Feature.Sample sample) {
//            final String featureDump = f.toString();
//            FeatureListActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mTextView.setText(featureDump);
//                }
//            });
//        }//onUpdate
//
//    }//GenericFragmentUpdate
}
