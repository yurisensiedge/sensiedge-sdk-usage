package com.semsiedgeexample;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sensisdk.DeviceManagerListener;

/**
 * Created by yuri on 06/11/16.
 */

public class NodeArrayAdapter extends ArrayAdapter<WirelessNode> implements DeviceManagerListener {

    /**
     * activity where this adapter is attached
     */
    private Activity mActivity;
    /**
     * image to show for the different boards
     */
    private Drawable mNucleoImage;
    private Drawable mSTEVAL_WESU1_Image;
    private Drawable mGenericImage;


    public NodeArrayAdapter(Activity context) {
        super(context, R.layout.node_view_item);
        mActivity = context;
        Resources res = mActivity.getResources();
        // TODO resolve getDrawable
//        mNucleoImage = res.getDrawable(R.drawable.board_nucleo);
//        mSTEVAL_WESU1_Image = res.getDrawable(R.drawable.board_steval_wesu1);
//        mGenericImage = res.getDrawable(R.drawable.board_generic);

    }

    @Override
    public void onDiscoveryFinish() {

    }

    @Override
    public void onDiscoveryStart() {

    }

//    public void onNodeDiscovered(Manager m, final Node node) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                add(node);
//            }//run
//        });
//    }
//    void disconnectAllNodes() {
//        for (int i = 0; i < getCount(); i++) {
//            Node n = getItem(i);
//            if (n.isConnected())
//                n.disconnect();
//        }//for
//    }//disconnectAllNodes

    /**
     * create a view that describe a particular node
     *
     * @param position position that have to be build
     * @param v        where store the information
     * @param parent   group where the view will be stored
     * @return a view that contains the information about the node in position \code{position}
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.node_view_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.sensorName = (TextView) v.findViewById(R.id.nodeName);
            viewHolder.sensorTag = (TextView) v.findViewById(R.id.nodeTag);
            viewHolder.boardType = (ImageView) v.findViewById(R.id.nodeBoard);
            v.setTag(viewHolder);
        } else {
            //else -> is a recycled view -> we have only to update the values
            viewHolder = (ViewHolderItem) v.getTag();
        }//if-else

        //get the corresponding sensor
//        Node sensor = getItem(position);

//        viewHolder.sensorName.setText(sensor.getName());
//        viewHolder.sensorTag.setText(sensor.getTag());
//        switch (sensor.getType()) {
//            case STEVAL_WESU1:
//                viewHolder.boardType.setImageDrawable(mSTEVAL_WESU1_Image);
//                break;
//            case NUCLEO:
//                viewHolder.boardType.setImageDrawable(mNucleoImage);
//                break;
//            case GENERIC:
//            default:
//                viewHolder.boardType.setImageDrawable(mGenericImage);
//        }//switch

        return v;
    }//getView

    /**
     * class that contains view that we have to change between different items
     */
    private static class ViewHolderItem {
        TextView sensorName;
        TextView sensorTag;
        ImageView boardType;
    }//ViewHolderItem



}
