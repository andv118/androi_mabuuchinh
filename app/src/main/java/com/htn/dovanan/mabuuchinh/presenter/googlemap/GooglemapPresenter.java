package com.htn.dovanan.mabuuchinh.presenter.googlemap;

import android.content.Context;
import android.os.AsyncTask;

import com.htn.dovanan.mabuuchinh.googlemap.ItemCluster;
import com.htn.dovanan.mabuuchinh.model.googlemap.GeocoderAsyncTask;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GooglemapPresenter implements GeocoderAsyncTask.OnAddMapListener {

    String TAG = "GoogleMap_Presenter";

    private Context context;
    private GeocoderAsyncTask geocoderAsyncTask;
    private GoogleMapViewListener callback;

    public GooglemapPresenter(Context context, GoogleMapViewListener callback) {
        this.context = context;
        this.callback = callback;
        geocoderAsyncTask = new GeocoderAsyncTask(context, this);
    }

    /**
     * Lấy dữ liệu: mảng từ sharePreference bằng asyncTask
     */
    public void getDatas() {
//        Log.d(TAG, "getData");
        geocoderAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void cancelDatas() {
//        Log.d(TAG, "cancelData");
        geocoderAsyncTask.cancel(true);
    }

    @Override
    public void onPreData(LatLng currentUserLocation) {
        callback.onGetCurrentLocation(currentUserLocation);
    }

    /**
     * thêm marker vào cluster
     * @param itemCluster
     * @param position    : vị trí trong list đã phân giải đại chỉ
     */
    @Override
    public void onUpdateData(ItemCluster itemCluster, int position) {
        callback.onAddMarkerInListCluster(itemCluster, position);
    }

    @Override
    public void onFinishData(List<ItemCluster> listCluster, int totalTime) {
        callback.onAddMarkerFinish(listCluster, totalTime);
    }

    @Override
    public void onMoveLocationUser() {
        callback.onMoveLocationUser();
    }

    @Override
    public void onMoveFirstAdress(ItemCluster itemCluster, int sizeList) {
        callback.onMoveFirstAdress(itemCluster, sizeList);
    }

    // Listener
    public interface GoogleMapViewListener {
        void onGetCurrentLocation(LatLng currentLocation);

        void onAddMarkerInListCluster(ItemCluster itemCluster, int position);

        void onAddMarkerFinish(List<ItemCluster> listCluster, int totalTime);

        void onMoveLocationUser();

        void onMoveFirstAdress(ItemCluster itemCluster, int sizeList);
    }
}
