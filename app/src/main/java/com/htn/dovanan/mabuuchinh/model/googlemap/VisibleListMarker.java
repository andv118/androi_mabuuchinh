package com.htn.dovanan.mabuuchinh.model.googlemap;

import android.os.AsyncTask;

import com.htn.dovanan.mabuuchinh.googlemap.ItemCluster;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VisibleListMarker extends AsyncTask<Void, Void, List<Marker>> {

    private ClusterManager<ItemCluster> clusterManager;
    private callBackListMarker callBackListMarker;

    public VisibleListMarker(ClusterManager<ItemCluster> clusterManager,
                             callBackListMarker callBackListMarker) {
        this.clusterManager = clusterManager;
        this.callBackListMarker = callBackListMarker;
    }

    @Override
    protected List<Marker> doInBackground(Void... voids) {
        Collection<Marker> markerCollection = clusterManager.getClusterMarkerCollection().getMarkers();
        List<Marker> listMarkerCluster = new ArrayList<Marker>(markerCollection);
        return listMarkerCluster;
    }

    @Override
    protected void onPostExecute(List<Marker> value) {
        super.onPostExecute(value);
        callBackListMarker.onCallBackListMarker(value);
    }

    // Listener

    public interface callBackListMarker {
        void onCallBackListMarker(List<Marker> listMarker);
    }

}
