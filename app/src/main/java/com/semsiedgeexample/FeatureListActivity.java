package com.semsiedgeexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.sensisdk.DeviceManager;
import com.sensisdk.nodes.AbstractNode;
import com.st.BlueSTSDK.Feature;


import java.util.List;


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
    private AbstractNode mNode;

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
    private ArrayAdapter<Feature> mFeatureListAdapter;

    /**
     * listener that will be used for enable the notification when the node is connected
     */
    private AbstractNode.NodeStateListener mNodeStatusListener = new AbstractNode.NodeStateListener() {
        @Override
        public void onStateChange(final AbstractNode node, AbstractNode.State newState, AbstractNode.State prevState) {
            Logger.print("onStateChange(): " + node.getTag() + "; State: " + newState);
            if (newState == AbstractNode.State.Connected) {
                FeatureListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateFeatureList();
                        invalidateOptionsMenu(); //enable/disable the settings options
                    }
                });
            }
        }
    };

    /**
     * listener that will update the displayed feature data
     */
    private Feature.FeatureListener mGenericUpdate;

    /**
     * create an intent for start this activity
     *
     * @param c    context used for create the intent
     * @param node node to use for the demo
     * @return intent for start a demo activity that use the node as data source
     */
    public static Intent getStartIntent(Context c, @NonNull AbstractNode node) {
        Intent i = new Intent(c, FeatureListActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        Logger.print("FeatureListActivity:node.getTag(): " + node.getTag());
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //load the gui
        mFeatureList = (ListView) findViewById(R.id.featureList);
        mFeatureList.setOnItemClickListener(this);

        //find the node
        String nodeTag = getIntent().getStringExtra(NODE_TAG);
        Logger.print("FeatureListActivity:onCreate() Chosen MAC: " + nodeTag);
        mNode = DeviceManager.getBleManager().getNodeWithTag(nodeTag);

        //create or recover the NodeContainerFragment
        if (savedInstanceState == null) {
            Intent i = getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());

            getFragmentManager().beginTransaction()
                    .add(mNodeContainer, NODE_FRAGMENT).commit();
        } else {
            mNodeContainer = (NodeContainerFragment) getFragmentManager()
                    .findFragmentByTag(NODE_FRAGMENT);

        }
    }

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
//        menu.findItem(R.id.menu_showDebug).setVisible(mNode.getDebug() != null);
//        menu.findItem(R.id.menu_showRegister).setVisible(mNode.getConfigRegister() != null);
        return true;
    }

    /**
     * start the activity with the debug console or for manage the configuration register
     *
     * @param item item selected by the user
     * @return true if the item is handle by this method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.print("onOptionsItemSelected()");

        int id = item.getItemId();

        //we call keepConnectionOpen for skip the node disconnection when the activity is destroyed
        //in this way we avoid fast connection/disconnection call

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_showRegister) {
            mNodeContainer.keepConnectionOpen(true);
//            startActivity(SettingsActivity.getStartIntent(this, mNode));
            return true;
        }
        if (id == R.id.menu_showDebug) {
            mNodeContainer.keepConnectionOpen(true);
//            startActivity(DebugConsoleActivity.getStartIntent(this, mNode));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * stop all the enabled notification
     */
    private void disableNeedNotification() {
        List<Feature> features = mNode.getFeatures();
        for (Feature f : features) {
            if (mNode.isEnableNotification(f))
                mNode.disableNotification(f);
        }
    }

    /**
     * create and populate the adapter with only the enabled features.
     */
    private void populateFeatureList() {
        Logger.toDebug("populateFeatureList()");
        if (mNode != null) {
            mFeatureListAdapter = new FeatureAdapter(this, R.layout.feature_list_item);
            List<Feature> features = mNode.getFeatures();
            for (Feature f : features) {
                if (f.isEnabled()) {
                    mFeatureListAdapter.add(f);
                }
            }
            //set the adapter as data source for the adapter
            mFeatureList.setAdapter(mFeatureListAdapter);
        }
    }

    /**
     * if the node is connected enable the gui, otherwise set a listener that will do that
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mNode.isConnected()) {
            populateFeatureList();
            invalidateOptionsMenu(); //enable/disable the settings options
        } else
            mNode.addNodeStateListener(mNodeStatusListener);
    }

    @Override
    protected void onPause() {
        //it is safe remove also if we didn't add it
        mNode.removeNodeStateListener(mNodeStatusListener);
        //if the node is already disconnected we don't care of disable the notification
        if (mNode.isConnected()) {
            disableNeedNotification();
        }
        super.onPause();
    }

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
        Feature selectedFeature = mFeatureListAdapter.getItem(position);

        if (mNode.isEnableNotification(selectedFeature)) {

            selectedFeature.removeFeatureListener(mGenericUpdate);
            mNode.disableNotification(selectedFeature);

            ((TextView) view).setText(selectedFeature.getName()); //reset the cell name
        } else {
            //create a listener that will update the selected view
            mGenericUpdate = new GenericFragmentUpdate((TextView) view);
            selectedFeature.addFeatureListener(mGenericUpdate);

            mNode.enableNotification(selectedFeature);
        }
    }

    /**
     * extend an array adapter for change the view content, instead of used the toString result
     * we use the feature name
     */
    class FeatureAdapter extends ArrayAdapter<Feature> {

        /**
         * @see ArrayAdapter#ArrayAdapter(Context, int)
         */
        public FeatureAdapter(Context c, int resourceId) {
            super(c, resourceId);
        }

        /**
         * create a text view and initialize it with the equivalent feature name
         */
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Logger.toDebug("getView()");
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.feature_list_item, parent, false);
            }
            Feature f = getItem(position);
            ((TextView) v).setText(f.getName());
            return v;
        }
    }

    /**
     * class used for update the feature display data
     */
    class GenericFragmentUpdate implements Feature.FeatureListener {
        /**
         * text view that will contain the data/name
         */
        final private TextView mTextView;

        /**
         * @param text text view that will show the name/values
         */
        public GenericFragmentUpdate(TextView text) {
            mTextView = text;
        }

        /**
         * set the text view text with the feature toString value
         *
         * @param f      feature that has received an update
         * @param sample new data received from the feature
         */
        @Override
        public void onUpdate(Feature f, Feature.Sample sample) {
            final String featureDump = f.toString();
            Logger.toDebug("Feature name: " + f.getName() + ": " + sample.data[0]);
            FeatureListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(featureDump);
                }
            });
        }
    }
}
