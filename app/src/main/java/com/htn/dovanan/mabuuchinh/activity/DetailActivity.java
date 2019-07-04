package com.htn.dovanan.mabuuchinh.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.adapter.AdapterParent;
import com.htn.dovanan.mabuuchinh.fragments.FragmentChild;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.listener.ItemVisibilityView;
import com.htn.dovanan.mabuuchinh.pojo.CtHuyen;
import com.htn.dovanan.mabuuchinh.pojo.CtTinh;
import com.htn.dovanan.mabuuchinh.pojo.MaBuuChinh;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.res.ResClient;
import com.htn.dovanan.mabuuchinh.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DetailActivity extends BaseActivity implements ItemClickListenerSearch {

    public static String TAG = "Detail_Activity";
    public static int REQUSETCODE = 200;
    // view
    private Toolbar toolbar;

    // recycler view
    private RecyclerView recyclerView;
    private List<MbcItem> itemList;
    private AdapterParent adapter;
    MyAsync async;
    // variable
    private String donVi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // khoi tao view, bien, gia tri
        initUX();

//        addMaps(itemList.get(0));

        clickMaps();
    }

    /*
    khoi tao view, bien, gia tri
     */
    private void initUX() {
        initView();
        initVariable();
        initToolbar(donVi);
        initRecyclerview();
        setVisibilityView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_parent);
    }

    /*
    set visibility and hide view button next page
     */
    private void setVisibilityView() {
        // setvisible button next xem cac view cap nho hon
        if (donVi.equals(Util.DONVI_TINH)) {
            adapter.setVisibilityView(new ItemVisibilityView() {
                @Override
                public void setVisibilityView(View view, int i) {
                    view.setVisibility(View.VISIBLE);
                }
            });
        } else if (donVi.equals(Util.DONVI_CT_HUYEN)) {
            adapter.setVisibilityView(new ItemVisibilityView() {
                @Override
                public void setVisibilityView(View view, int i) {
                    view.setVisibility(View.GONE);
                }
            });
        } else if (donVi.equals(Util.DONVI_HUYEN) ||
                donVi.equals(Util.DONVI_CT_TINH)) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getDonVi().equals(Util.DONVI_HUYEN)) {
                    adapter.setVisibilityView(new ItemVisibilityView() {
                        @Override
                        public void setVisibilityView(View view, int i) {
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    adapter.setVisibilityView(new ItemVisibilityView() {
                        @Override
                        public void setVisibilityView(View view, int i) {
                            view.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }
    }

    /*
    khoi tao gia tri
    get bundle
    */
    private void initVariable() {
        itemList = new ArrayList<>();

        Bundle bundle = getIntent().getBundleExtra(Util.BUNDLE);
        if (bundle != null) {
            MbcItem mbcItem = (MbcItem) bundle.getSerializable(Util.OBJECT_ITEM_RECYCLERVIEW);

            String jsonList = bundle.getString(Util.STRING_ALL_ITEM);

            if (mbcItem != null) {
                itemList.add(mbcItem);
                donVi = mbcItem.getDonVi();
                Log.d("donvi", donVi);
            }

            if (jsonList != null) {
                Log.d("donvi", jsonList);
                List<MbcItem> listMbc = new Gson().fromJson(jsonList, new TypeToken<List<MbcItem>>() {
                }.getType());

                // get don vi
                if (listMbc.get(0).getDonVi().equals(Util.DONVI_TINH)) {
                    donVi = Util.DONVI_TINH;
                } else if (listMbc.get(0).getDonVi().equals(Util.DONVI_HUYEN)) {
                    donVi = Util.DONVI_HUYEN;
                } else if (listMbc.get(0).getDonVi().equals(Util.DONVI_CT_TINH)) {
                    donVi = Util.DONVI_CT_TINH;
                } else if (listMbc.get(0).getDonVi().equals(Util.DONVI_CT_HUYEN)) {
                    donVi = Util.DONVI_CT_HUYEN;
                }

                // add vao itemList
                // neu don vi la huyen va ct_tinh thi add ca huyen va ctTinh vao list
                // neu ko thi tinh add tinh, ctHuyen add ctHuyen
                if (donVi.equals(Util.DONVI_HUYEN) || donVi.equals(Util.DONVI_CT_TINH)) {
                    for (int i = 0; i < listMbc.size(); i++) {
                        if (listMbc.get(i).getDonVi().equals(Util.DONVI_HUYEN) ||
                                listMbc.get(i).getDonVi().equals(Util.DONVI_CT_TINH)) {
                            itemList.add(listMbc.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < listMbc.size(); i++) {
                        if (donVi.equals(listMbc.get(i).getDonVi())) {
                            itemList.add(listMbc.get(i));
                        }
                    }
                }
            }


        }
    }

    public void initToolbar(String donVi) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // set title
        if (donVi.equals(Util.DONVI_TINH)) {
            toolbar.setTitle("Mã bưu chính Tỉnh và Thành Phố");
        } else if (donVi.equals(Util.DONVI_HUYEN) || donVi.equals(Util.DONVI_CT_TINH)) {
            toolbar.setTitle("Mã bưu chính Huyện và đơn vị trực thuộc");
        } else if (donVi.equals(Util.DONVI_CT_HUYEN)) {
            toolbar.setTitle("Mã bưu chính Xã và đơn vị trực thuộc");
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecyclerview() {
        adapter = new AdapterParent(this, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setClickIconNext(this);
        adapter.notifyDataSetChanged();
    }

    /* click icon map -> start intent mapActivity */
    private void clickMaps() {
        if (adapter != null) {
            adapter.setClickMaps(new ItemClickListenerSearch() {
                @Override
                public void onClickItem(MbcItem item, int position) {
                    addMaps(item);
                }
            });
        }
    }

    /* Intent to Map Activity */
    public void addMaps(final MbcItem item) {
        if (Util.haveNetwork(this)) {
            async = new MyAsync();
//            async.putLocationCurrentToPreferences();
            async.execute(item);

            showProgressDialog();

            Intent intent = new Intent(DetailActivity.this, MapActivity.class);
            startActivityForResult(intent, 200);
            closeProgressDialog();

//            Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//                }
//            };
//            handler.postDelayed(runnable, 500);
        } else {
            Toast.makeText(this, "Kiểm tra lại kết nối internet!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    click item of recyclerview
     */
    @Override
    public void onClickItem(MbcItem item, int position) {
        // bundle: donvi, mabuuchinh
        Bundle bundle = new Bundle();
        // gui object la item hien tai
        bundle.putSerializable(Util.BUNDLE_OBJECT, itemList.get(position));
        // add fragment child
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentChild fragChild = new FragmentChild();
        if (fragChild.isAdded()) {
            ft.replace(R.id.container_Detail, fragChild);
        } else {
            ft.add(R.id.container_Detail, fragChild);
            ft.addToBackStack(Util.BACK_STACK_FRAGMENT_CHILD);
        }
        fragChild.setArguments(bundle);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        // get fragment hien tai
        Fragment fragCurrent = getSupportFragmentManager().findFragmentById(R.id.container_Detail);

        if (fragCurrent instanceof FragmentChild &&
                getSupportFragmentManager().getBackStackEntryCount() > 0) { // child

            // neu co fragment con nua
            if (fragCurrent.getChildFragmentManager().getBackStackEntryCount() > 0) {
                fragCurrent.getChildFragmentManager().popBackStack();
            } else {
                getSupportFragmentManager().popBackStack();
            }
//            Fragment fragChil = fragCurrent.getChildFragmentManager().findFragmentById(R.id.container_childFrag);
//            if (fragChil instanceof FragmentChild2) {
//                // remove
//                FragmentTransaction ft = fragCurrent.getChildFragmentManager().beginTransaction();
//                ft.remove(fragChil);
//                ft.commit();
//
//            } else {
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.remove(fragCurrent);
//                ft.commit();
//            }

        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETCODE) {
            async.cancel(true);
            closeProgressDialog();
        }
    }


    /* xu ly du lieu ngam put len shareprefrence */
    class MyAsync extends AsyncTask<MbcItem, Void, Void> {

        @Override
        protected Void doInBackground(MbcItem... voids) {
            MbcItem mbcItem = voids[0];

            saveDataToPreferences1(mbcItem);
            return null;
        }

        /* luu data list mbc tren share preferrence */
        private void saveDataToPreferences1(MbcItem item) {
            final List<MbcItem> listMbc = new ArrayList<>();
            SharedPreferences preferences = getSharedPreferences(TAG + "_Save_Data", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.clear();

            String donVi = item.getDonVi();
            if (donVi.equals(Util.DONVI_CT_TINH) || donVi.equals(Util.DONVI_CT_HUYEN)) {
                if (item.getData3() != null) {
                    listMbc.add(item);
                }
                String jsonListMbc = new Gson().toJson(listMbc);
                Log.d(TAG + "_json", jsonListMbc);
                editor.putString(Util.MAP_ACTIVITY_PREF_KEY_MBC, jsonListMbc).commit();
            } else {
                // get data from webservice
                Call<MaBuuChinh> dataMbcDetail = ResClient.getAPIs().getMbcDetail(item.getMabc(), donVi);
                dataMbcDetail.enqueue(new Callback<MaBuuChinh>() {
                    @Override
                    public void onResponse(Call<MaBuuChinh> call, Response<MaBuuChinh> response) {
                        Log.d(TAG + "_retrofit", "success" + response.code());
                        MaBuuChinh mbcDetail = response.body();
                        if (mbcDetail != null) {
                            List<CtTinh> listCtTinh = mbcDetail.getListCtTinh();
                            if (listCtTinh != null) {
                                for (int i = 0; i < listCtTinh.size(); i++) {
                                    if (listCtTinh.get(i).getData3() != null) {
                                        MbcItem mbcItem = new MbcItem(listCtTinh.get(i).getId(),
                                                listCtTinh.get(i).getTen(), listCtTinh.get(i).getMabc(),
                                                listCtTinh.get(i).getData1(), listCtTinh.get(i).getData2(),
                                                listCtTinh.get(i).getData3(), listCtTinh.get(i).getData4(),
                                                listCtTinh.get(i).getData5(), Util.DONVI_CT_TINH);
                                        listMbc.add(mbcItem);
                                    }
                                }
                            }

                            List<CtHuyen> listCtHuyen = mbcDetail.getListCtHuyen();
                            if (listCtHuyen != null) {
                                for (int i = 0; i < listCtHuyen.size(); i++) {
                                    if (listCtHuyen.get(i).getData3() != null) {
                                        MbcItem mbcItem = new MbcItem(listCtHuyen.get(i).getId(),
                                                listCtHuyen.get(i).getTen(), listCtHuyen.get(i).getMabc(),
                                                listCtHuyen.get(i).getData1(), listCtHuyen.get(i).getData2(),
                                                listCtHuyen.get(i).getData3(), listCtHuyen.get(i).getData4(),
                                                listCtHuyen.get(i).getData5(), Util.DONVI_CT_HUYEN);
                                        listMbc.add(mbcItem);
                                    }
                                }
                            }

                            String jsonListMbc = new Gson().toJson(listMbc);
                            Log.d(TAG + "_json", jsonListMbc);
                            Log.d("TestData_DetailActivity", jsonListMbc);
                            editor.putString(Util.MAP_ACTIVITY_PREF_KEY_MBC, jsonListMbc).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<MaBuuChinh> call, Throwable t) {
                        Log.d(TAG + "_retrofit", "error: " + t.toString());
                    }
                });
            }
        }

        /* put vị trí hiện tại lên sharepreference */
        public void putLocationCurrentToPreferences() {
            SharedPreferences preferences = getSharedPreferences(TAG + "_Location_Current", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.clear();

            int REQUEST_CODE = 201;
            if (ActivityCompat.checkSelfPermission(DetailActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_CODE);
            } else {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DetailActivity.this);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(DetailActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG + "_Current_location", "latitude:" + location.getLatitude() + "\n"
                                    + " - longitude:" + location.getLongitude());
                            editor.putString(Util.MAP_ACTIVITY_PREF_KEY_LATIUDE, String.valueOf(location.getLatitude()));
                            editor.putString(Util.MAP_ACTIVITY_PREF_KEY_LONGITUDE, String.valueOf(location.getLongitude()));
                            editor.commit();
                        }
                    }
                });
            }
        }
    }
}
