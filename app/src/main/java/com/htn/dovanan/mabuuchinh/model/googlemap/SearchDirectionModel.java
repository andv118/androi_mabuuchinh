package com.htn.dovanan.mabuuchinh.model.googlemap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.htn.dovanan.mabuuchinh.pojo.CtHuyen;
import com.htn.dovanan.mabuuchinh.pojo.CtTinh;
import com.htn.dovanan.mabuuchinh.pojo.MaBuuChinh;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.res.ResClient;
import com.htn.dovanan.mabuuchinh.util.Util;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDirectionModel {

    private static long DELAY_TIMER = 150;
    private static String TAG = "Seach_Direction_Model";
    private static String SHARE_NAME = "Address_direction";

    private Context context;
    private SearchDirectResponeListener callback;
    private Timer timer;
    private SharedPreferences sharedPreferences;
    private GeocoderAsyncTask geocoderAsyncTask;

    public SearchDirectionModel(Context context, SearchDirectResponeListener callback) {
        this.context = context;
        this.callback = callback;
        initComponent();
    }

    private void initComponent() {
        timer = new Timer();
        sharedPreferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        geocoderAsyncTask = new GeocoderAsyncTask(context);
    }

    public void getDataFromWebservice(final String text) {
        if (Util.haveNetwork((Activity) context)) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // call data
                    Call<MaBuuChinh> dataMBCMap = ResClient.getAPIs().getMbcMap(text);
                    dataMBCMap.enqueue(new Callback<MaBuuChinh>() {
                        @Override
                        public void onResponse(Call<MaBuuChinh> call, Response<MaBuuChinh> response) {
                            Log.d("TAG" + "_retrofit", "success" + response.code());
                            MaBuuChinh mbc = response.body();
                            if (mbc != null) {
                                // Filter
                                filterSearchDirect(mbc);
                                Log.d("TAG" + "_retrofit", "response:" + response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<MaBuuChinh> call, Throwable t) {
                            Log.d("TAG" + "_retrofit", "error: " + t.toString());
                        }
                    });
                }
            }, DELAY_TIMER);
        } else {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    callback.onNotHaveNetWork();
                }
            }, 2000);
        }
    }

    /**
     * Lưu marker vào Sharepreference sau đó get trong freference để hiển thị
     *
     * @param item
     * @param origin
     * @param destiny
     * @param isClickTimKiem
     */
    public void saveAndDisplayMarker(MbcItem item, View origin, View destiny, Boolean isClickTimKiem) {
        String title = item.getMabc() + "_" + item.getTen();
        String diaChi = item.getData3();
        if (origin.hasFocus()) {
            callback.onOriginSetText(title);
            // Save data
            sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_TITLE_EDT1, title).commit();
            sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT1, item.getData3()).commit();
            if (isClickTimKiem) {
                LatLng latLng = geocoderAsyncTask.getLocationFromAddress(context, diaChi);
                if (latLng != null) {
                    callback.onAddMarkerSearch(latLng, title, diaChi);
                } else {
                    callback.onAddressChuaCapNhat();
                }
            }
        } else if (destiny.hasFocus()) {
            sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_TITLE_EDT2, title).commit();
            sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT2, item.getData3()).commit();
            callback.onDestinySetText(title);
        }
    }

    /**
     * Lọc dữ liệu sau khi get về
     * return về list filter để hiện thị dữ liệu
     *
     * @param mbc: body response MBC
     */
    private void filterSearchDirect(MaBuuChinh mbc) {
        List<MbcItem> filteredList = new ArrayList<>();
        filteredList.clear();

        List<CtTinh> listCtTinh = mbc.getListCtTinh();
        if (listCtTinh != null) {
            for (int i = 0; i < listCtTinh.size(); i++) {
                filteredList.add(new MbcItem(listCtTinh.get(i).getId(), listCtTinh.get(i).getTen(),
                        listCtTinh.get(i).getMabc(), listCtTinh.get(i).getData1(),
                        listCtTinh.get(i).getData2(), listCtTinh.get(i).getData3(),
                        listCtTinh.get(i).getData4(), listCtTinh.get(i).getData5(),
                        Util.DONVI_CT_TINH));
            }
        }


        List<CtHuyen> listCtHuyen = mbc.getListCtHuyen();
        if (listCtHuyen != null) {
            for (int i = 0; i < listCtHuyen.size(); i++) {
                filteredList.add(new MbcItem(listCtHuyen.get(i).getId(), listCtHuyen.get(i).getTen(),
                        listCtHuyen.get(i).getMabc(), listCtHuyen.get(i).getData1(),
                        listCtHuyen.get(i).getData2(), listCtHuyen.get(i).getData3(),
                        listCtHuyen.get(i).getData4(), listCtHuyen.get(i).getData5(),
                        Util.DONVI_CT_HUYEN));
            }
        }
        callback.onFilterSuccess(filteredList);
    }

    /**
     * Xử lý khi click vào icon chỉ đường
     *
     * @param text1            : nội dung edt 1
     * @param text2            : nội dung edt 2
     * @param listMarkerDirect
     */
    public void direction(String text1, String text2, List<Marker> listMarkerDirect) {
        if (text1.equals("") || text2.equals("")) {
            callback.onToastDienDayDu();
        } else {
            progressDirections(text1, text2, listMarkerDirect);
        }
    }

    /**
     * Xử lý chức năng chỉ đường
     *
     * @param textOrigin       : Text điểm bắt đầu
     * @param textDest         : điểm kết thúc
     * @param listMarkerDirect
     */
    private void progressDirections(String textOrigin, String textDest, List<Marker> listMarkerDirect) {
        LatLng latLngorigin = geocoderAsyncTask.getLocationFromAddress(context, sharedPreferences.getString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT1, ""));
        LatLng latLngDest = geocoderAsyncTask.getLocationFromAddress(context, sharedPreferences.getString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT2, ""));

        Log.d(TAG, "origin:" + latLngorigin + " - Dest:" + latLngDest);

        if (latLngorigin != null && latLngDest != null) {
            if (latLngorigin.latitude == latLngDest.latitude &&
                    latLngorigin.longitude == latLngDest.longitude) {
                callback.onToast2ViTriTrungNhau();

            } else {
                // origin
                MarkerOptions optionsOrigin = new MarkerOptions();
                optionsOrigin.position(latLngorigin);
                optionsOrigin.title(textOrigin);
                optionsOrigin.snippet(sharedPreferences.getString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT1, ""));
                optionsOrigin.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                // dest
                MarkerOptions optionsDest = new MarkerOptions();
                optionsDest.position(latLngDest);
                optionsDest.title(textDest);
                optionsDest.snippet(sharedPreferences.getString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT2, ""));
                optionsDest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                // Trả về 2 tạo độ bắt đầu và kết thúc cho Map activity hiển thị
                callback.onGetMarkerOnOption(optionsOrigin, optionsDest);

//            if (listMarkerDirect.size() == 2) {
                if (listMarkerDirect.size() >= 2) {
                    String url = getRequestUrl(listMarkerDirect.get(0).getPosition(), listMarkerDirect.get(1).getPosition());
                    callback.onTaskRequest(url);

                    LatLng latLngOrigin1 = listMarkerDirect.get(0).getPosition();
                    LatLng latLngDestiny1 = listMarkerDirect.get(1).getPosition();
                    String urlConnectGoogleMap = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latLngOrigin1.latitude + "," + latLngOrigin1.longitude + "&daddr=" + latLngDestiny1.latitude + "," + latLngDestiny1.longitude;
                    callback.onIntentConnectAppGoogleMap(urlConnectGoogleMap);
                } else {
                    callback.onToastChuaTimDuongDi();
                }
//            }
            }
        } else {
            if (latLngorigin == null) {
                callback.onToastOriginChuaChinhXac();
            } else {
                callback.onToastDestinyChuaChinhXac();
            }
        }
    }


    /**
     * Lấy ra Url cho Json Direct map
     *
     * @param origin
     * @param dest
     * @return
     */
    private String getRequestUrl(LatLng origin, LatLng dest) {
        String keyBanQuyen = "AIzaSyAoDvmEF49FQVfuh1O1ZyNUubq_C4ZSIjI";
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        //Output format
        String output = "json";
        // key ban quyen
        String key = "&key=" + keyBanQuyen;
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + key;
        return url;
    }

    /**
     * Click InfoWindow set text vào edittext origin và destiny
     *
     * @param marker
     * @param isClickChiDuong
     * @param addressDi
     * @param addressDen
     */
    public void infoWindowClick(Marker marker, Boolean isClickChiDuong, EditText addressDi, EditText addressDen) {
        if (isClickChiDuong) {
            String title = marker.getTitle();
            String diaChi = marker.getSnippet();
            if (addressDi.hasFocus()) {

                sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_TITLE_EDT1, title).commit();
                sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT1, diaChi).commit();

                addressDi.clearFocus();
                addressDen.requestFocus();

                callback.onSetTextEdtClickInfoWindow(addressDi, title);
            } else if (addressDen.hasFocus()) {

                sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_TITLE_EDT2, title).commit();
                sharedPreferences.edit().putString(Util.MAP_ACTIVITY_PREF_KEY_ADDRESS_EDT2, diaChi).commit();

                addressDen.clearFocus();
                addressDi.requestFocus();

                callback.onSetTextEdtClickInfoWindow(addressDen, title);
            }
        }
    }

    /**
     * Listener đưa về Search Direction Presenter
     */
    public interface SearchDirectResponeListener {
        void onNotHaveNetWork();

        void onFilterSuccess(List<MbcItem> filteredList);

        void onOriginSetText(String title);

        void onAddMarkerSearch(LatLng latLng, String title, String daiChi);

        void onAddressChuaCapNhat();

        void onDestinySetText(String title);

        // Direction
        void onToastDienDayDu();

        void onToastChuaTimDuongDi();

        void onGetMarkerOnOption(MarkerOptions optionsOrigin, MarkerOptions optionsDest);

        void onToastOriginChuaChinhXac();

        void onToastDestinyChuaChinhXac();

        void onTaskRequest(String url);

        void onIntentConnectAppGoogleMap(String urlConnectGoogleMap);

        void onSetTextEdtClickInfoWindow(View editText, String title);

        void onToast2ViTriTrungNhau();
    }

}
