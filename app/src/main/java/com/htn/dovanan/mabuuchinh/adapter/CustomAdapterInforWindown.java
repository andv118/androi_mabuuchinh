package com.htn.dovanan.mabuuchinh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomAdapterInforWindown implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View myContentsView;

    public CustomAdapterInforWindown(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        myContentsView = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindown, null);

        TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.txtHeader));
        TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.txtSnippet));

        tvTitle.setText(marker.getTitle());
        if (!marker.getSnippet().equals("") || marker.getSnippet() != null) {
            tvSnippet.setText(marker.getSnippet());
        } else {
            tvSnippet.setVisibility(View.GONE);
        }
        return myContentsView;
    }
}
