package com.htn.dovanan.mabuuchinh.presenter.googlemap;

import android.content.Context;

public class BottomNavigationPresenter {

    private Context context;
    private BottomNavigationListener callback;

    public BottomNavigationPresenter(Context context, BottomNavigationListener callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setUiSearchDirection(int id){
        callback.onSetUiSearchDirection(id);
    }

    /**
     * Listener
     */
    public interface BottomNavigationListener {
        void onSetUiSearchDirection(int id);
    }
}
