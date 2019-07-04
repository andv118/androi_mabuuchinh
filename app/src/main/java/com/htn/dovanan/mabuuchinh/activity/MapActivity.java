package com.htn.dovanan.mabuuchinh.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.adapter.AdapterSearchMap;
import com.htn.dovanan.mabuuchinh.adapter.CustomAdapterInforWindown;
import com.htn.dovanan.mabuuchinh.googlemap.ItemCluster;
import com.htn.dovanan.mabuuchinh.model.googlemap.TaskRequestDirections;
import com.htn.dovanan.mabuuchinh.listener.AnimationEndListener;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.presenter.googlemap.BottomNavigationPresenter;
import com.htn.dovanan.mabuuchinh.presenter.googlemap.GooglemapPresenter;
import com.htn.dovanan.mabuuchinh.presenter.googlemap.SearchDirectPresenter;
import com.htn.dovanan.mabuuchinh.util.CrossAnimation;
import com.htn.dovanan.mabuuchinh.util.MaxHeightRecyclerView;
import com.htn.dovanan.mabuuchinh.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        ItemClickListenerSearch,
        GooglemapPresenter.GoogleMapViewListener,
        BottomNavigationPresenter.BottomNavigationListener,
        SearchDirectPresenter.SearchDirectListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private static String TAG = "Map_Activity";
    private static String SHARE_NAME = "Address_direction";
    private static long DELAY_MS = 500;
    private static int ZOOM_CAMERA_15 = 15;
    private static int ZOOM_CAMERA_13 = 13;
    private static int ZOOM_CAMERA_10 = 10;

    // view
    private BottomNavigationView btnNav;
    private RelativeLayout viewSearch;
    private EditText addressDi, addressDen;
    private ImageView imgBack, imgDirect;
    private MaxHeightRecyclerView recyclerViewSearchMap;
    private ImageView btnDirect, btnConnectMap;
    private LinearLayout connectApplicationGoogleMap;

    // variable
    private GooglemapPresenter googlemapPresenter;
    private BottomNavigationPresenter bottomNavigationPresenter;
    private SearchDirectPresenter searchDirectPresenter;

    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    private AdapterSearchMap adapterSearchMap;
    private List<MbcItem> listMbc;
    private Boolean isClickTimKiem = false;
    private Boolean isClickChiDuong = false;
    private Boolean isCkickMarker = false;
    private Boolean isClickOutSide = true;

    // Map
    private GoogleMap map;
    private View mapView;
    private LatLng currentLocation;
    private ClusterManager<ItemCluster> clusterManager;
    private List<ItemCluster> listItemCluster;
    private List<Marker> listMarkersSearch;
    private List<Marker> listMarkerDirect;
    private List<Polyline> listPolyline;
    private Marker markerClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        showProgressDialog();
        initView();
        initComponent();
        initRecyclerview();
        touchEdittextEvent();


        // Delay 1s de load map
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final SupportMapFragment mf = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.dummy_map_view, mf)
                        .commit();
                mapView = findViewById(R.id.dummy_map_view);
                mf.getMapAsync(MapActivity.this);
            }
        }, DELAY_MS);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        closeProgressDialog();
        setupUiMap(googleMap, mapView);
        setupCluster(googleMap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                googlemapPresenter.getDatas();
            }
        });

        // Click marker
        map.setOnMarkerClickListener(this);
        // Click outside
        map.setOnMapClickListener(this);
        // Click infoWindow
        map.setOnInfoWindowClickListener(this);
    }

    /*************************************** Khởi UI UX ************************/
    public void showProgressDialog() {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(false);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeProgressDialog() {
        try {
            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewSearch = (RelativeLayout) findViewById(R.id.relative_search);
        addressDi = (EditText) findViewById(R.id.edt_direct1);
        addressDen = (EditText) findViewById(R.id.edt_direct2);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgDirect = (ImageView) findViewById(R.id.img_direct);
        recyclerViewSearchMap = (MaxHeightRecyclerView) findViewById(R.id.recyclerview_search_map);
        btnDirect = (ImageView) findViewById(R.id.img_direct_infowindown);
        btnConnectMap = (ImageView) findViewById(R.id.img_map_infowindown);
        connectApplicationGoogleMap = (LinearLayout) findViewById(R.id.linear_connect_map);
    }

    private void initComponent() {
        listMbc = new ArrayList<>();
        listMarkersSearch = new ArrayList<>();
        listItemCluster = new ArrayList<>();
        listMarkerDirect = new ArrayList<>();
        listPolyline = new ArrayList<>();

        btnNav.setOnNavigationItemSelectedListener(this);
        imgBack.setOnClickListener(this);
        imgDirect.setOnClickListener(this);
        btnDirect.setOnClickListener(this);
        btnConnectMap.setOnClickListener(this);

        googlemapPresenter = new GooglemapPresenter(this, this);
        bottomNavigationPresenter = new BottomNavigationPresenter(this, this);
        searchDirectPresenter = new SearchDirectPresenter(MapActivity.this, this);
        sharedPreferences = getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear();

        viewSearch.setVisibility(View.GONE);

        // text change edittext
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeTextFindAdress(editable.toString());
            }
        };

        addressDi.addTextChangedListener(textWatcher);
        addressDen.addTextChangedListener(textWatcher);
    }

    private void initRecyclerview() {
        adapterSearchMap = new AdapterSearchMap(this, listMbc);
        recyclerViewSearchMap.setAdapter(adapterSearchMap);
        recyclerViewSearchMap.setLayoutManager(new LinearLayoutManager(this));
        adapterSearchMap.setClicklistener((ItemClickListenerSearch) this);
        adapterSearchMap.notifyDataSetChanged();
    }

    /**
     * setup cluster, infor window
     *
     * @param googleMap
     */
    private void setupCluster(GoogleMap googleMap) {
        clusterManager = new ClusterManager<ItemCluster>(this, googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        final CustomAdapterInforWindown adapterInforWindown = new CustomAdapterInforWindown(this);
        googleMap.setInfoWindowAdapter(adapterInforWindown);

//        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
//        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(adapterInforWindown);
////        clusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(adapterInforWindown);
//
//        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ItemCluster>() {
//            @Override
//            public boolean onClusterItemClick(ItemCluster itemCluster) {
////                clickItemCluster();
//                return false;
//            }
//
//
//        });
    }

    /**
     * set padding các button: zoom, la bàn, ẩn hiện marker, vị trí hiện tại Trên map
     *
     * @param googleMap
     * @param mapView
     */
    private void setupUiMap(GoogleMap googleMap, View mapView) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

//        googleMap.getUiSettings().set

        // Hiển thị button vị trí hiện tại
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        googleMap.setMyLocationEnabled(true);

        // Location Button
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams paramsLocationButton = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        paramsLocationButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsLocationButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsLocationButton.setMargins(0, 350, 20, 0);

        // ZoomButton
        @SuppressLint("ResourceType")
        View zoomButton = (View) mapView.findViewById(0x1);
        RelativeLayout.LayoutParams paramsZoomButton = (RelativeLayout.LayoutParams) zoomButton.getLayoutParams();
        paramsZoomButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramsZoomButton.setMargins(0, 0, 20, 200);

        // Compass Button: La bàn
        ViewGroup parent = (ViewGroup) mapView.findViewWithTag("GoogleMapMyLocationButton").getParent();
        View compassButton = parent.getChildAt(4);
        RelativeLayout.LayoutParams paramsCompassButton = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        paramsCompassButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsCompassButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsCompassButton.setMargins(10, 350, 0, 0);

        // Map toolbar
        View mapToolbar = ((View) mapView.findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("4"));
        RelativeLayout.LayoutParams paramsMapToolbar = (RelativeLayout.LayoutParams) mapToolbar.getLayoutParams();
        paramsMapToolbar.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        paramsMapToolbar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        paramsMapToolbar.setMargins(30, 0, 400, 170);

    }

    /**
     * Set giao diện khi click item bottom navigation view
     *
     * @param id: id của view
     */
    @Override
    public void onSetUiSearchDirection(int id) {
        if (id == R.id.action_trang_chu) {

            addressDi.setText("");
            addressDen.setText("");
            addressDi.clearFocus();
            addressDen.clearFocus();
            Util.hideKeyboard(this, addressDi);
            if (viewSearch.getVisibility() == View.VISIBLE) {
                CrossAnimation.slideUp(viewSearch, new AnimationEndListener() {
                    @Override
                    public void endAnimation() {
                        viewSearch.setVisibility(View.GONE);
                    }
                });
            }
            isClickTimKiem = false;
            isClickChiDuong = false;

        } else if (id == R.id.action_tim_kiem) {
            viewSearch.setVisibility(View.VISIBLE);
            imgDirect.setVisibility(View.GONE);
            addressDen.setVisibility(View.GONE);
            addressDi.setHint(getResources().getString(R.string.Map_Activity_hint_tim_kiem));
            CrossAnimation.slideDown(viewSearch);

            isClickTimKiem = true;
            isClickChiDuong = false;

        } else if (id == R.id.action_chi_duong) {
            viewSearch.setVisibility(View.VISIBLE);
            imgDirect.setVisibility(View.VISIBLE);
            addressDen.setVisibility(View.VISIBLE);
            addressDi.setHint(getResources().getString(R.string.Map_Activity_hint_chi_duong_1));
            addressDen.setHint(getResources().getString(R.string.Map_Activity_hint_chi_duong_2));
            CrossAnimation.slideDown(viewSearch);

            isClickTimKiem = false;
            isClickChiDuong = true;
        }
        // Clear text và con trỏ
        addressDi.setText("");
        addressDen.setText("");
        addressDi.clearFocus();
        addressDen.clearFocus();
    }

    /**
     * Xử lý giao diện khi nhấn item recyclerview
     */
    @Override
    public void onSetUiSearchCallBack() {
        addressDi.clearFocus();
        recyclerViewSearchMap.setVisibility(View.GONE);
        Util.hideKeyboard(this, addressDi);
        CrossAnimation.slideUp(viewSearch);
    }

    /**
     * Xử lý giao diện khi Click vào icon chi đường
     */
    @Override
    public void onSetUiSearchDirections() {
        addressDi.clearFocus();
        Util.hideKeyboard(this, addressDi);
        CrossAnimation.slideUp(viewSearch);
        recyclerViewSearchMap.setVisibility(View.GONE);
    }

    @Override
    public void onOriginSetText(String title) {
        addressDi.setText(title);
        recyclerViewSearchMap.setVisibility(View.GONE);
    }

    @Override
    public void onDestinySetText(String title) {
        addressDen.setText(title);
        recyclerViewSearchMap.setVisibility(View.GONE);
    }

    /**
     * Set text khi click vào infowindow (khi chỉ đường)
     * @param editText
     * @param title
     */
    @Override
    public void onSetTextEdtClickInfoWindow(View editText, String title) {
        if (editText.getId() == R.id.edt_direct1) {
            addressDi.setText(title);
        } else if(editText.getId() == R.id.edt_direct2) {
            addressDen.setText(title);
        }
        recyclerViewSearchMap.setVisibility(View.GONE);
    }

    /*************************************** Event *******************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                addressDi.setText("");
                addressDen.setText("");
                addressDi.clearFocus();
                addressDen.clearFocus();
                Util.hideKeyboard(this, addressDi);
                CrossAnimation.slideUp(viewSearch, new AnimationEndListener() {
                    @Override
                    public void endAnimation() {
                        viewSearch.setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.img_direct:
                String text1 = String.valueOf(addressDi.getText());
                String text2 = String.valueOf(addressDen.getText());
                searchDirectPresenter.clickDirection(text1, text2, listMarkerDirect);
                break;

            case R.id.img_direct_infowindown:
                LatLng latLng1 = markerClick.getPosition();
                Double latitude1 = currentLocation.latitude;
                Double longitude1 =  currentLocation.longitude;
                Double latitude2 = latLng1.latitude;
                Double longitude2 =  latLng1.longitude;

                String uri1 = "http://maps.google.com/maps?f=d&hl=en&saddr="+latitude1+","+longitude1+"&daddr="+latitude2+","+longitude2;
                Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri1));
                startActivity(Intent.createChooser(intent1, "Select an application"));
                break;

            case R.id.img_map_infowindown:
//                Toast.makeText(this, "Connect", Toast.LENGTH_SHORT).show();
                LatLng latLng = markerClick.getPosition();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + latLng.latitude  + ">,<" + latLng.longitude + ">?q=<" + latLng.latitude  + ">,<" + latLng.longitude + ">"));
                startActivity(Intent.createChooser(intent, "Select an application"));
                break;
        }
    }

    /**
     * click chọn item trong recyclerview tìm kiếm, chỉ đường
     *
     * @param item
     * @param position
     */
    @Override
    public void onClickItem(MbcItem item, int position) {
        String title = item.getMabc() + "_" + item.getTen();
//        recyclerViewSearchMap.setVisibility(View.GONE);
        searchDirectPresenter.clickItemRecyclerView(item, addressDi, addressDen, isClickTimKiem);
    }

    /**
     * Click chọn item bottom navigation view
     * trang chủ, tìm kiếm, chỉ đường
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_trang_chu:
                bottomNavigationPresenter.setUiSearchDirection(R.id.action_trang_chu);
                break;
            case R.id.action_tim_kiem:
//                Toast.makeText(this, "tim kiem", Toast.LENGTH_SHORT).show();
                bottomNavigationPresenter.setUiSearchDirection(R.id.action_tim_kiem);
                break;
            case R.id.action_chi_duong:
//                Toast.makeText(this, "chi duong", Toast.LENGTH_SHORT).show();
                bottomNavigationPresenter.setUiSearchDirection(R.id.action_chi_duong);
                break;
        }
        return false;
    }

    /**
     * Touch vào edittext điểm bắt đầu và edittext điểm kết thúc
     */
    private void touchEdittextEvent() {
        addressDi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isClickChiDuong) {
                    changeTextFindAdress(addressDi.getText().toString());
                }
                return false;
            }
        });

        addressDen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isClickChiDuong) {
                    changeTextFindAdress(addressDen.getText().toString());
                }
                return false;
            }
        });
    }

    /**
     * Click vào marker
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        markerClick = marker;
        if (isCkickMarker == false) {
            isCkickMarker = true;
            isClickOutSide = false;
            CrossAnimation.slideRight(connectApplicationGoogleMap);
        }
        return false;
    }

    /**
     * Click outside marker
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (isClickOutSide == false) {
            isClickOutSide = true;
            isCkickMarker = false;
            CrossAnimation.slideLeft(connectApplicationGoogleMap);
        }


    }

    /**
     * Click infoWindow GoogleMap
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        searchDirectPresenter.infoWindowClick(marker, isClickChiDuong, addressDi, addressDen);
    }

    @Override
    public void onBackPressed() {
        if (viewSearch.getVisibility() == View.VISIBLE) {
            viewSearch.setVisibility(View.GONE);
            recyclerViewSearchMap.setVisibility(View.GONE);
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", 200);
            setResult(Activity.RESULT_OK, returnIntent);
            googlemapPresenter.cancelDatas();
            super.onBackPressed();
        }
    }

    /********************************** Google Map**********************************/

    /**
     * Lấy ra tọa độ vị trí hiện tại
     *
     * @param currentLocation
     */
    @Override
    public void onGetCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Thêm marker trong khi load data
     *
     * @param itemCluster
     * @param position
     */
    @Override
    public void onAddMarkerInListCluster(ItemCluster itemCluster, int position) {
        if (position > 0) {
            listItemCluster.add(itemCluster);
//            clusterManager.addItem(itemCluster);
            Log.d(TAG + "_Item_Cluster:", position + " - " + itemCluster.getTitle());
            MarkerOptions options = new MarkerOptions();
            options.position(itemCluster.getPosition());
            options.title(itemCluster.getTitle());
            options.snippet(itemCluster.getSnippet());
            map.addMarker(options);
        }
    }

    /**
     * Thêm marker vào list marker hoàn tất
     *
     * @param listCluster
     * @param totalTime
     */
    @Override
    public void onAddMarkerFinish(final List<ItemCluster> listCluster, int totalTime) {
        Log.d(TAG + "_Total_Time", totalTime + "(s)");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//              clusterManager.cluster();
                Toast.makeText(MapActivity.this, "Tải bản đồ hoàn tất!", Toast.LENGTH_SHORT).show();
//                if (listCluster.size() > 5) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(listCluster.get(0).getPosition(), ZOOM_CAMERA_10));
//                } else if (listCluster.size() > 2) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(listCluster.get(0).getPosition(), ZOOM_CAMERA_13));
//                }
            }
        });
    }

    /**
     * Di chuyển đến vị trí của người dùng khi địa chỉ ko có trên bản đồ
     */
    @Override
    public void onMoveLocationUser() {
        LatLng currentCoordinates = new LatLng(currentLocation.latitude, currentLocation.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, ZOOM_CAMERA_15));
        Toast.makeText(this, "Vị trí chưa cập nhật!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Di chuyển đến cị trí đầu tiên của list marker
     *
     * @param itemCluster
     * @param sizeList
     */
    @Override
    public void onMoveFirstAdress(ItemCluster itemCluster, int sizeList) {
        if (sizeList > 5) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(itemCluster.getPosition(), ZOOM_CAMERA_10));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(itemCluster.getPosition(), ZOOM_CAMERA_15));
        }
    }

    /**************************** Chức năng Tìm kiếm ***********************************/

    /**
     * Khi tìm kiếm thành công -> Add marker
     *
     * @param latLng
     */
    @Override
    public void onAddMarkerSearch(LatLng latLng, String title, String diaChi) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title(title);
        options.snippet(diaChi);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = map.addMarker(options);
        marker.showInfoWindow();

        // Xóa marker trước trong list
        listMarkersSearch.add(0, marker);

        if (listMarkersSearch.size() > 1) {
            listMarkersSearch.get(1).remove();
            listMarkersSearch.remove(1);
        }

        // Xóa marker chỉ đường
        for (int i = 0; i < listMarkerDirect.size(); i++) {
            listMarkerDirect.get(i).remove();
        }

        // Xóa polyline
        if (listPolyline.size() > 0) {
            for (int i = 0; i < listPolyline.size(); i++) {
                listPolyline.get(i).remove();
            }
        }

        // Set info window
        final CustomAdapterInforWindown adapterInforWindown = new CustomAdapterInforWindown(this);
        map.setInfoWindowAdapter(adapterInforWindown);

        // Move camera
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA_15));
    }

    /**************************** Chức năng Chỉ đường ***********************************/

    /**
     * Thêm marker cảu điểm xuất phát và điểm đến trên bản đồ
     *
     * @param optionsOrigin
     * @param optionsDest
     */
    @Override
    public void onAddMarkerDirections(MarkerOptions optionsOrigin, MarkerOptions optionsDest) {

        // Xóa polyline trước
        if (listPolyline.size() > 0) {
            for (int i = 0; i < listPolyline.size(); i++) {
                listPolyline.get(i).remove();
            }
            listPolyline.clear();
        }

        Marker marker1 = map.addMarker(optionsOrigin);
        Marker marker2 = map.addMarker(optionsDest);
        marker1.showInfoWindow();
        marker2.showInfoWindow();
        listMarkerDirect.add(0, marker1);
        listMarkerDirect.add(1, marker2);

        // Xóa marker chỉ đường trước
        if (listMarkerDirect.size() > 2) {
            for (int i = 2; i < listMarkerDirect.size(); i++) {
                listMarkerDirect.get(i).remove();
            }
            listMarkerDirect.clear();
            listMarkerDirect.add(0, marker1);
            listMarkerDirect.add(1, marker2);
        }

        // Xóa marker tìm kiếm trước
        if (listMarkersSearch.size() > 0) {
            listMarkersSearch.get(0).remove();
        }
    }

    /**
     * Di chuyển camera đến điểm xuất phát
     *
     * @param optionsOrigin
     */
    @Override
    public void onMoveCameraDirection(MarkerOptions optionsOrigin) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(optionsOrigin.getPosition(), ZOOM_CAMERA_13));
    }

    /**
     * Hiện thị đường đi sau khi lấy được URL
     *
     * @param url
     */
    @Override
    public void onTaskRequestDirections(String url) {
        Log.d(TAG + "_url", url);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections(this, map, listPolyline);
        taskRequestDirections.execute(url);
    }

    @Override
    public void onIntentConnectAppGoogleMap(String urlConnectGoogleMap) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(urlConnectGoogleMap));
        startActivity(intent);
    }

    /********************************************************/

    /**
     * Xử lý khi thay đổi text trong editext tìm kiếm và chỉ đường
     *
     * @param text: string truyền vào từ editext
     */
    private void changeTextFindAdress(final String text) {
//        Log.d(TAG + "_Text", text);
        if (text.equals("")) {
            recyclerViewSearchMap.setVisibility(View.GONE);
        } else {
            recyclerViewSearchMap.setVisibility(View.VISIBLE);
            searchDirectPresenter.getDatas(text);
        }
    }

    /**
     * Khi thay đổi text nhưng không có internet -> ko get được data hiện thị ra recyclerview
     */
    @Override
    public void onNotifiNotHaveNetwork() {
        Toast.makeText(this, "Kiểm tra lại kết nối internet!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hiện thị data trên recyclerview tìm kiếm, chỉ đường
     *
     * @param filteredList
     */
    @Override
    public void onShowDataFilter(List<MbcItem> filteredList) {
        adapterSearchMap.filterList(filteredList);
        adapterSearchMap.notifyDataSetChanged();
    }

    @Override
    public void onToastChuaCapNhat() {
        Toast.makeText(this, "Địa chỉ chưa cập nhật!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToastChuaDienDayDu() {
        Toast.makeText(this, getResources().getString(R.string.Map_Activity_Toast_Address), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToasDiemXuatPhatSai() {
        Toast.makeText(this, "Điểm xuất phát không chính xác", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToasDiemDenSai() {
        Toast.makeText(this, "Điểm đến không chính xác", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToastChuaTimDuocDuongDi() {
        Toast.makeText(this, "Chưa tìm được đường đi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToast2ViTriTrungNhau() {
        Toast.makeText(this, "2 vị trí giống nhau!", Toast.LENGTH_SHORT).show();
    }

}
