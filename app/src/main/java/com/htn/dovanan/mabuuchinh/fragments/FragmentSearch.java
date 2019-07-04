package com.htn.dovanan.mabuuchinh.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.htn.dovanan.mabuuchinh.activity.DetailActivity;
import com.htn.dovanan.mabuuchinh.activity.MainActivity;
import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.adapter.ItemSearchAdapter;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.pojo.CtHuyen;
import com.htn.dovanan.mabuuchinh.pojo.CtTinh;
import com.htn.dovanan.mabuuchinh.pojo.Huyen;
import com.htn.dovanan.mabuuchinh.pojo.MaBuuChinh;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.pojo.Tinh;
import com.htn.dovanan.mabuuchinh.res.ResClient;
import com.htn.dovanan.mabuuchinh.util.CrossAnimation;
import com.htn.dovanan.mabuuchinh.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FragmentSearch extends Fragment implements View.OnClickListener, ItemClickListenerSearch {

    private String TAG = "Fragment_Search";
    private Context context;
    private View view;

    private ImageView imgClose;
    public EditText edtSearch;
    private Button btnClearText;
    Dialog dialog;

    // recyclervew
    private RecyclerView recyclerView;
    private ItemSearchAdapter adapter;
    private List<MbcItem> itemList;

    // variable
    private Timer timer = new Timer();
    private final long DELAY = 150; // milliseconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_fragment_search, container, false);
            context = getContext();
        }

        // khoi tao view, bien
        initUX();

        // show dialog khi dang load du lieu
        ((MainActivity) getActivity()).showProgressDialog();

        // hien thi tat ca cac tinh khi chua tim kiem gi
        callData(edtSearch.getText().toString());

        // chuc nang search khi thay doi text
        textChange();

        // click search keyboard
        ClickSearchKeyboard();

        // get location
        putLocationCurrentToPreferences();

        return view;
    }

    private void initView() {
        imgClose = (ImageView) view.findViewById(R.id.img_close_search);
        edtSearch = (EditText) view.findViewById(R.id.edt_search_main);
        btnClearText = (Button) view.findViewById(R.id.btn_search_close_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // clicl listener
        imgClose.setOnClickListener(this);
        btnClearText.setOnClickListener(this);
    }

    private void intVariable() {
        // khoi tao gia tri

        itemList = new ArrayList<>();

//        edtSearch.setText("");
//        edtSearch.requestFocus();
//        Util.checkKeyboardWithFocus((Activity) context, edtSearch);
    }

    /*
    khoi tao view, bien
     */
    private void initUX() {
        initView();
        intVariable();
        initRecyclerview();
    }

    private void initRecyclerview() {
        adapter = new ItemSearchAdapter(context, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setClicklistener((ItemClickListenerSearch) this);
    }

    /*
    click search keyboard
     */
    private void ClickSearchKeyboard() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    List<MbcItem> allItemMbc = adapter.getAllItem();
                    String jsonList = new Gson().toJson(allItemMbc);

                    // chuyen qua activity detail
                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    // gui bundle object
                    Bundle bundle = new Bundle();
                    bundle.putString(Util.STRING_ALL_ITEM, jsonList);
                    intent.putExtra(Util.BUNDLE, bundle);
                    startActivity(intent);


                    return true;
                }
                return false;
            }
        });

//        edtSearch.addTextChangedListener(this);
    }

    // click view
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // xoa text trong edittext
            case R.id.btn_search_close_text:
                edtSearch.setText("");
                callData(edtSearch.getText().toString());
                break;

            // remove fragment hien tai
            case R.id.img_close_search:
                // header main activity
                RelativeLayout header = ((MainActivity) getActivity()).findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
                CrossAnimation.slideDown(header);
                edtSearch.setText("");
                edtSearch.clearFocus();
                Util.hideKeyboard((Activity) context, edtSearch);
                ((MainActivity) getActivity()).removeAllFragment();
                ((MainActivity) getActivity()).isClickSearch = true;

                break;
        }
    }

    // click item recyclerview
    @Override
    public void onClickItem(MbcItem item, int position) {
        // check internet
        if (Util.haveNetwork(getActivity())) {
//            showDialogDirection(item);
            if (edtSearch.hasFocus()) {
                    edtSearch.clearFocus();
//                    Util.checkKeyboardWithFocus((Activity) context, edtSearch);
                }

                // chuyen qua activity detail
                Intent intent = new Intent(getActivity(), DetailActivity.class);

                // gui bundle object
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.OBJECT_ITEM_RECYCLERVIEW, item);
                intent.putExtra(Util.BUNDLE, bundle);
                startActivityForResult(intent, 201);

        } else {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Toast.makeText(context, "Kiểm tra lại kết nối internet!", Toast.LENGTH_SHORT).show();
                }
            }, 2000);
        }
    }

    /*
    Load data from JSON
   */
    private void callData(String text) {
        Call<MaBuuChinh> dataMBC = ResClient.getAPIs().getMaBuuChinh(text);
        dataMBC.enqueue(new Callback<MaBuuChinh>() {
            @Override
            public void onResponse(Call<MaBuuChinh> call, Response<MaBuuChinh> response) {
                Log.d(TAG + "_retrofit", "success:" + response.code());
                MaBuuChinh mbc = response.body();
                if (mbc != null) {
                    // filter
                    myFilter(mbc);
                }
                ((MainActivity) getActivity()).closeProgressDialog();
            }

            @Override
            public void onFailure(Call<MaBuuChinh> call, Throwable t) {
                ((MainActivity) getActivity()).closeProgressDialog();
                Log.d(TAG + "_retrofit", "error: " + t.toString());
            }
        });
    }

    /*
    tien hanh loc khi co duoc 4 mang
    tinh, huyen, chi tiet tinh, chi tiet huyen
     */
    private void myFilter(MaBuuChinh mbc) {

        // khoi tao list
        List<Tinh> listTinh = mbc.getListTinh();
        List<Huyen> listHuyen = mbc.getListHuyen();
        List<CtTinh> listCtTinh = mbc.getListCtTinh();
        List<CtHuyen> listCtHuyen = mbc.getListCtHuyen();

        // khoi tao list object do du lieu ra recyclerview
        List<MbcItem> filteredList = new ArrayList<>();
        filteredList.clear();

        // thuoc tinh cua MbcItem
        int id;
        String ten, mbcItem, data1, data2, data3, data4, data5;

        // Tim cho TINH
        if (listTinh != null) {
            for (int i = 0; i < listTinh.size(); i++) {
                id = listTinh.get(i).getId();
                ten = listTinh.get(i).getTen();
                mbcItem = listTinh.get(i).getMabc();
                data1 = (String) listTinh.get(i).getData1();
                data2 = (String) listTinh.get(i).getData2();
                data3 = (String) listTinh.get(i).getData3();
                data4 = (String) listTinh.get(i).getData4();
                data5 = (String) listTinh.get(i).getData5();

                filteredList.add(new MbcItem(id, ten, mbcItem,
                        data1, data2, data3, data4, data5,
                        Util.DONVI_TINH));

            }
        }

        // Tim cho HUYEN
        if (listHuyen != null) {
            for (int i = 0; i < listHuyen.size(); i++) {
                id = listHuyen.get(i).getId();
                ten = listHuyen.get(i).getTen();
                mbcItem = listHuyen.get(i).getMabc();
                data1 = (String) listHuyen.get(i).getData1();
                data2 = (String) listHuyen.get(i).getData2();
                data3 = (String) listHuyen.get(i).getData3();
                data4 = (String) listHuyen.get(i).getData4();
                data5 = (String) listHuyen.get(i).getData5();

                filteredList.add(new MbcItem(id, ten, mbcItem,
                        data1, data2, data3, data4, data5,
                        Util.DONVI_HUYEN));
            }
        }

        // Tim cho CHI_TIET_TINH
        if (listCtTinh != null) {
            for (int i = 0; i < listCtTinh.size(); i++) {
                id = listCtTinh.get(i).getId();
                ten = listCtTinh.get(i).getTen();
                mbcItem = listCtTinh.get(i).getMabc();
                data1 = (String) listCtTinh.get(i).getData1();
                data2 = (String) listCtTinh.get(i).getData2();
                data3 = (String) listCtTinh.get(i).getData3();
                data4 = (String) listCtTinh.get(i).getData4();
                data5 = (String) listCtTinh.get(i).getData5();

                filteredList.add(new MbcItem(id, ten, mbcItem,
                        data1, data2, data3, data4, data5,
                        Util.DONVI_CT_TINH));

            }
        }


        if (listCtHuyen != null) {
            for (int i = 0; i < listCtHuyen.size(); i++) {
                id = listCtHuyen.get(i).getId();
                ten = listCtHuyen.get(i).getTen();
                mbcItem = listCtHuyen.get(i).getMabc();
                data1 = (String) listCtHuyen.get(i).getData1();
                data2 = (String) listCtHuyen.get(i).getData2();
                data3 = (String) listCtHuyen.get(i).getData3();
                data4 = (String) listCtHuyen.get(i).getData4();
                data5 = (String) listCtHuyen.get(i).getData5();

                filteredList.add(new MbcItem(id, ten, mbcItem,
                        data1, data2, data3, data4, data5,
                        Util.DONVI_CT_HUYEN));
            }
        }

        adapter.filterList(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void textChange() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            // trc khi thay doi text
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("TextChange", "trc");
            }

            // trong luc thay doi text
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("TextChange", "trong");
            }

            // sau khi thay doi text
            @Override
            public void afterTextChanged(final Editable editable) {
//                Log.d("TextChange", "sau");
                if (Util.haveNetwork(getActivity())) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            callData(editable.toString());
                        }
                    }, DELAY);
                } else {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Kiểm tra lại kết nối internet!", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            }
        });
    }

    /*
    clear text and focus khi activiti back press
    activity use
     */
    public void clearTextAndFocus() {
        EditText edt = view.findViewById(R.id.edt_search_main);
        edt.setText("");
        edt.clearFocus();
    }

    private void showDialogDirection(final MbcItem item) {
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_direction);
//
//        ImageView exit = (ImageView) dialog.findViewById(R.id.img_close);
//        TextView txtSearch = (TextView) dialog.findViewById(R.id.txt_search) ;
//        TextView txtMap = (TextView) dialog.findViewById(R.id.txt_map) ;
//
//        // search xem chi tiet
//        txtSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (edtSearch.hasFocus()) {
//                    edtSearch.clearFocus();
////                    Util.checkKeyboardWithFocus((Activity) context, edtSearch);
//                }
//
//                // chuyen qua activity detail
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//
//                // gui bundle object
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Util.OBJECT_ITEM_RECYCLERVIEW, item);
//                intent.putExtra(Util.BUNDLE, bundle);
//                startActivityForResult(intent, 201);
//            }
//        });
//
//        // xem ban do
//        txtMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MainActivity) getActivity()).showProgressDialog();
//
//                String mbc = item.getMabc();
//                String donVi = item.getDonVi();
//                retrieveDataFromWebservice(mbc, donVi);
//
//                long timerStarted = System.currentTimeMillis();
//                Log.d("loadGoogle", "DetaiActivity - time:" + timerStarted);
//                Intent intent = new Intent(getActivity(), MapActivity.class);
//                // gui bundle object
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Util.OBJECT_ITEM_RECYCLERVIEW, item);
//                intent.putExtra(Util.BUNDLE, bundle);
//                startActivityForResult(intent, 200);
//            }
//        });
//
//        // exit
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
    }

    /*
    retrieve data from webservice
    save data into share preferences
     */
    private void retrieveDataFromWebservice(String mbc, final String donvi) {
        SharedPreferences preferences = getActivity().getSharedPreferences("List_mbc", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        Call<MaBuuChinh> dataMbcDetail = ResClient.getAPIs().getMbcDetail(mbc, donvi);
        dataMbcDetail.enqueue(new Callback<MaBuuChinh>() {
            @Override
            public void onResponse(Call<MaBuuChinh> call, Response<MaBuuChinh> response) {
                Log.d("retrofit_detai_mbc", "success");
                Log.d("retrofit_detai_mbc", response.code() + "");
                MaBuuChinh mbcDetail = response.body();
                if (mbcDetail != null) {
                    saveDataToPreferences(mbcDetail, donvi, editor);
                }
            }

            @Override
            public void onFailure(Call<MaBuuChinh> call, Throwable t) {
                Log.d("retrofit_detai_mbc", "error");
                Log.d("retrofit_detai_mbc", t.toString());
            }
        });
    }

    /*
    put data to share preferences
     */
    private void saveDataToPreferences(MaBuuChinh mbcDetail, String donvi, SharedPreferences.Editor editor) {
        // dovi: 2 value
        // 1: DONVI_TINH OR 2: DONVI_HUYEN

        if (donvi.equals(Util.DONVI_TINH)) {
            List<Huyen> listHuyen = mbcDetail.getListHuyen();
            List<CtTinh> listCtTinh = mbcDetail.getListCtTinh();
            Gson gson = new Gson();

            if (listHuyen != null) {
                String jsonListHuyen = gson.toJson(listHuyen);
                editor.putString(Util.PREF_KEY_HUYEN, jsonListHuyen);
                editor.commit();
            }
            if (listCtTinh != null) {
                String jsonListCtTinh = gson.toJson(listCtTinh);
                editor.putString(Util.PREF_KEY_CT_TINH, jsonListCtTinh);
                editor.commit();
            }

        } else if (donvi.equals(Util.DONVI_HUYEN)) {
            List<CtHuyen> listCtHuyen = mbcDetail.getListCtHuyen();
            Gson gson = new Gson();

            if (listCtHuyen != null) {
                String jsonListCtHuyen = gson.toJson(listCtHuyen);
                editor.putString(Util.PREF_KEY_CT_HUYEN, jsonListCtHuyen);
                editor.commit();
            }
        }
    }

    private void putLocationCurrentToPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Location_current", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        int REQUEST_CODE = 201;
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("Current_location", "latitude:" + location.getLatitude()
                                + " - longitude:" + location.getLongitude());
                        editor.putString(Util.MAP_ACTIVITY_PREF_KEY_LATIUDE, String.valueOf(location.getLatitude()));
                        editor.putString(Util.MAP_ACTIVITY_PREF_KEY_LONGITUDE, String.valueOf(location.getLongitude()));
                        editor.commit();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            ((MainActivity) getActivity()).closeProgressDialog();
        }
//        dialog.dismiss();
    }
}
