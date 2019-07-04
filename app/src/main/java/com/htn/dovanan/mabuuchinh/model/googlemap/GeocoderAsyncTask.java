package com.htn.dovanan.mabuuchinh.model.googlemap;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.htn.dovanan.mabuuchinh.activity.DetailActivity;
import com.htn.dovanan.mabuuchinh.googlemap.ItemCluster;
import com.htn.dovanan.mabuuchinh.pojo.HashMapItem;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.util.Util;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeocoderAsyncTask extends AsyncTask<Void, ItemCluster, List<ItemCluster>> {
    private static String TAG = "Geocoder_Asynctask";
    private Context context;
    private OnAddMapListener callBack;
    private SharedPreferences preferences;
    private List<HashMapItem> listHashMapItem;
    long time1, time2;
    int position = 0;
    int sizeListAdress;

    public GeocoderAsyncTask(Context context) {
    }

    public GeocoderAsyncTask(Context context, OnAddMapListener onAddMapListener) {
        this.context = context;
        this.callBack = onAddMapListener;
    }

    @Override
    protected void onPreExecute() {
//        Log.d(TAG, "PreExecute");
        super.onPreExecute();
        preferences = context.getSharedPreferences(DetailActivity.TAG + "_Save_Data", Context.MODE_PRIVATE);
        listHashMapItem = new ArrayList<>();
        time1 = System.currentTimeMillis();
        listHashMapItem = tranfromListItem();
//        Log.d("Map_Activity_Test", listHashMapItem.toString());
        LatLng currentLocation = getCurrentLocation();
        callBack.onPreData(currentLocation);
    }

    @Override
    protected List<ItemCluster> doInBackground(Void... voids) {
//        Log.d(TAG, "DoInBackground");
        List<ItemCluster> itemClusterList = new ArrayList<>();
        sizeListAdress = listHashMapItem.size();
        if (listHashMapItem.size() > 0) {
            for (int i = 0; i < listHashMapItem.size(); i++) {
                String diaChi = listHashMapItem.get(i).getAdress();
                String title = "";
                if (diaChi != null) {
                    List<String> listMbc = listHashMapItem.get(i).getMbc();
                    List<String> listName = listHashMapItem.get(i).getName();
                    for (int i1 = 0; i1 < listMbc.size(); i1++) {
                        if (i1 == listMbc.size() - 1) {
                            title += listMbc.get(i1) + "_" + listName.get(i1);
                        } else {
                            title += listMbc.get(i1) + "_" + listName.get(i1) + ",";
                        }
                    }

                    final LatLng latLng = getLocationFromAddress(context, diaChi);
                    if (latLng != null) {
                        ItemCluster itemCluster = new ItemCluster(latLng, title, diaChi);
                        position += 1;
                        itemClusterList.add(itemCluster);
                        // chuyển item chuyển đổi được đến update dữ liệu
                        publishProgress(itemCluster);
                    }
                }
            }
        } else {
//            publishProgress(null);
        }
        return itemClusterList;
    }

    @Override
    protected void onProgressUpdate(ItemCluster... value) {
//        Log.d(TAG, "ProgressUpdate");
        super.onProgressUpdate(value);
        if (value[0] != null) {
            callBack.onUpdateData(value[0], position);
            if (position == 1) {
                callBack.onMoveFirstAdress(value[0], sizeListAdress );
            }
        }
    }

    @Override
    protected void onPostExecute(List<ItemCluster> itemClusters) {
//        Log.d(TAG, "PostExcecute");
        super.onPostExecute(itemClusters);
        time2 = System.currentTimeMillis();
        int totalTimeLoadMarker = (int) (time2 - time1) / 1000;
        if (itemClusters.size() > 0) {
            callBack.onFinishData(itemClusters, totalTimeLoadMarker);
        } else {
            callBack.onMoveLocationUser();
        }
    }

    /**
     * Chuyển đổi 1 địa chỉ thành 1 tọa độ
     * @param strAddress
     * @return LatLng coordinates
     */
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        if (strAddress != null) {
            Geocoder coder = new Geocoder(context);
            LatLng p1 = null;
            List<Address> address = null;
            try {
                address = coder.getFromLocationName(strAddress, 1);
                if (address.size() > 0) {
                    Address location = address.get(0);
                    location.getLatitude();
                    location.getLongitude();
                    p1 = new LatLng(location.getLatitude(), location.getLongitude());
                }
            } catch (IOException e) {
                Log.d(TAG + "_Add_Catch", e.toString());
                e.printStackTrace();
            }

            return p1;
        } else {
            return null;
        }
    }

    /**
     * Lấy dữ liệu từ Detai activity
     * xử lý địa chỉ trùng nhau
     * @return List<HashMapItem>
     */
    private List<HashMapItem> tranfromListItem() {
        List<HashMapItem> listHashMapItem = new ArrayList<>();
        List<MbcItem> listItemMbc = new ArrayList<>();

        String jsonListMbc = preferences.getString(Util.MAP_ACTIVITY_PREF_KEY_MBC, "");
        Log.d(TAG + "_Json", jsonListMbc);
        Type type = new TypeToken<List<MbcItem>>() {
        }.getType();
        listItemMbc = new Gson().fromJson(jsonListMbc, type);
        if (listItemMbc != null) {
            // xu ly ten trung nhau
            listHashMapItem = sameAdress(listItemMbc);
        }
        return listHashMapItem;
    }

    /**
     * Xử lý tên trùng nhau
     * Nếu trùng nhau: giữa nguyên địa chỉ, cộng các tên có đại chỉ giống nhau làm 1
     * @param listMbc
     * @return List<HashMapItem>
     */
    private List<HashMapItem> sameAdress(List<MbcItem> listMbc) {
        HashMap<String, HashMapItem> map = new HashMap<>();
        for (int i = 0; i < listMbc.size(); i++) {
            int id = listMbc.get(i).getId();
            List<String> mbc = new ArrayList<>();
            mbc.add(listMbc.get(i).getMabc());
            List<String> name = new ArrayList<>();
            name.add(listMbc.get(i).getTen());
            String phone = listMbc.get(i).getData1();
            String data2 = listMbc.get(i).getData2();
            String diaChi = listMbc.get(i).getData3();
            String email = listMbc.get(i).getData4();
            String website = listMbc.get(i).getData5();
            String donVi = listMbc.get(i).getDonVi();
            if (map.containsKey(diaChi)) {
                // add list mbc va ten vao list moi
                List<String> mbcNew = map.get(diaChi).getMbc();
                String mbcOld = mbc.get(0);
                mbcNew.add(mbcOld);
                List<String> tenNew = map.get(diaChi).getName();
                String nameOld = name.get(0);
                tenNew.add(nameOld);

                map.put(diaChi, new HashMapItem(id, mbcNew, tenNew, phone, data2, email, website, donVi));

            } else {
                map.put(diaChi, new HashMapItem(id, mbc, name, phone, data2, email, website, donVi));
            }
        }
        List<HashMapItem> listHashMap = new ArrayList<>();
        for (String diaChi : map.keySet()) {
            HashMapItem item = map.get(diaChi);
            listHashMap.add(new HashMapItem(item.getId(), item.getMbc(), item.getName(),
                    item.getPhone(), item.getData2(), diaChi, item.getEmail(), item.getWebsite(), item.getDonVi()));
        }
        return listHashMap;
    }

    /**
     * Lấy ra vị trí hiện tại của người dùng
     */
    private LatLng getCurrentLocation() {
        SharedPreferences preferences = context.getSharedPreferences("Location_current", Context.MODE_PRIVATE);
        Double latitude = Double.parseDouble(preferences.getString(Util.MAP_ACTIVITY_PREF_KEY_LATIUDE, "21.018848"));
        Double longitude = Double.parseDouble(preferences.getString(Util.MAP_ACTIVITY_PREF_KEY_LONGITUDE, "105.851005"));
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }

    /**
     * Listener
     */
    public interface OnAddMapListener {
        void onPreData(LatLng currentUserLocation);

        void onUpdateData(ItemCluster itemCluster, int position);

        void onFinishData(List<ItemCluster> listCluster, int totalTime);

        void onMoveLocationUser();

        void onMoveFirstAdress(ItemCluster itemCluster, int sizeList);
    }

}
