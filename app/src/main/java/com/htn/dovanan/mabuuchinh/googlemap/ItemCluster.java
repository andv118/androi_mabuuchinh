package com.htn.dovanan.mabuuchinh.googlemap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemCluster implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public ItemCluster(double lat, double lng) {
        this.mPosition = new LatLng(lat, lng);
    }

    public ItemCluster(LatLng mPosition, String mTitle, String mSnippet) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
