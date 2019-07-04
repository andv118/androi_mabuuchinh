package com.htn.dovanan.mabuuchinh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.activity.DetailActivity;
import com.htn.dovanan.mabuuchinh.adapter.AdapterParent;
import com.htn.dovanan.mabuuchinh.listener.ItemClickListenerSearch;
import com.htn.dovanan.mabuuchinh.listener.ItemVisibilityView;
import com.htn.dovanan.mabuuchinh.pojo.CtHuyen;
import com.htn.dovanan.mabuuchinh.pojo.CtTinh;
import com.htn.dovanan.mabuuchinh.pojo.Huyen;
import com.htn.dovanan.mabuuchinh.pojo.MaBuuChinh;
import com.htn.dovanan.mabuuchinh.pojo.MbcItem;
import com.htn.dovanan.mabuuchinh.res.ResClient;
import com.htn.dovanan.mabuuchinh.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChild extends Fragment implements View.OnClickListener, ItemClickListenerSearch {
    private Context context;
    private View view;

    // rercyclerview
    private RecyclerView recyclerView;
    private List<MbcItem> listMbc;
    private AdapterParent adapter;

    // view
    private ImageView imgPreview;
    private TextView txtTitle;

    // variable
    String donVi = "";
    String mbc;
    // item cua cap cha gui den
    MbcItem itemParent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_child, container, false);
            context = getContext();
        }

        ((DetailActivity) getActivity()).showProgressDialog();

        // khoi tao view, gia tri
        initUX();

        clickMaps();

        return view;
    }

    private void initUX() {
        initView();
        initVariable();
        initRecyclerview();
        visibilityIconNext();
    }

    private void initView() {
        imgPreview = (ImageView) view.findViewById(R.id.img_icon_preview);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_child);
        imgPreview.setOnClickListener(this);
    }

    private void initVariable() {
        listMbc = new ArrayList<>();
        recyclerView.setVisibility(View.GONE);

        // get bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            itemParent = (MbcItem) bundle.getSerializable(Util.BUNDLE_OBJECT);
            mbc = itemParent.getMabc();
            txtTitle.setText(itemParent.getMabc() + " - " + itemParent.getTen());

            // get donvi, set toolbar
            if (itemParent.getDonVi().equals(Util.DONVI_TINH)) {
                donVi = Util.DONVI_HUYEN;
            } else if (itemParent.getDonVi().equals(Util.DONVI_HUYEN)) {
                donVi = Util.DONVI_CT_HUYEN;
            }
            ((DetailActivity) getActivity()).initToolbar(donVi);

            // don vi cua view cha
            // lay du lieu
            callData(mbc, itemParent.getDonVi());
        }
    }

    private void initRecyclerview() {
        adapter = new AdapterParent(context, listMbc);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setClickIconNext(this);
        adapter.notifyDataSetChanged();
    }

    private void visibilityIconNext() {
        adapter.setVisibilityView(new ItemVisibilityView() {
            @Override
            public void setVisibilityView(View view, int i) {
                String donVi = listMbc.get(i).getDonVi();
                if (donVi.equals(Util.DONVI_HUYEN)) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_icon_preview:
                // lay ra fragment hien tai cua parent
                Fragment fragCurent = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_Detail);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.remove(fragCurent);
                ft.commit();

                // set text toolbar khi quay lai
                if (itemParent.getDonVi().equals(Util.DONVI_TINH)) {
                    ((DetailActivity) getActivity()).initToolbar(Util.DONVI_TINH);
                } else if (itemParent.getDonVi().equals(Util.DONVI_HUYEN)) {
                    ((DetailActivity) getActivity()).initToolbar(Util.DONVI_HUYEN);
                }
                break;
        }
    }

    private void clickMaps() {
        if (adapter != null) {
            adapter.setClickMaps(new ItemClickListenerSearch() {
                @Override
                public void onClickItem(MbcItem item, int position) {
                    // add map
                    ((DetailActivity) getActivity()).addMaps(item);
                }
            });
        }
    }

    //    click item next
    @Override
    public void onClickItem(MbcItem item, int position) {
//        Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();

        // add fragment
        // bundle: gui object
        Bundle bundle = new Bundle();
        // gui object la item hien tai
        bundle.putSerializable(Util.BUNDLE_OBJECT, listMbc.get(position));
        // add fragment child
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentChild2 fragChild2 = new FragmentChild2();
        if (fragChild2.isAdded()) {
            ft.replace(R.id.container_childFrag, fragChild2);
        } else {
            ft.add(R.id.container_childFrag, fragChild2);
            ft.addToBackStack(Util.BACK_STACK_FRAGMENT_CHILD_2);
        }
        fragChild2.setArguments(bundle);
        ft.commit();
    }

    private void callData(String mbc, final String donvi) {
        Call<MaBuuChinh> dataMbcDetail = ResClient.getAPIs().getMbcDetail(mbc, donvi);
        dataMbcDetail.enqueue(new Callback<MaBuuChinh>() {
            @Override
            public void onResponse(Call<MaBuuChinh> call, Response<MaBuuChinh> response) {
                Log.d("retrofit Detai MBC", "success");
                Log.d("retrofit Detai MBC", response.code() + "");
                MaBuuChinh mbcDetail = response.body();
                if (mbcDetail != null) {
                    progressData(mbcDetail, donvi);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                ((DetailActivity) getActivity()).closeProgressDialog();
            }

            @Override
            public void onFailure(Call<MaBuuChinh> call, Throwable t) {
                ((DetailActivity) getActivity()).closeProgressDialog();
                Log.d("retrofit Detai MBC", "error");
                Log.d("retrofit Detai MBC", t.toString());
            }
        });
    }

    private void progressData(MaBuuChinh mbcDetail, String donvi) {

        int id;
        String ten, mbc, data1, data2, data3, data4, data5;

        if (donvi.equals(Util.DONVI_TINH)) {
            List<Huyen> listHuyen = mbcDetail.getListHuyen();
            List<CtTinh> listCtTinh = mbcDetail.getListCtTinh();

            if (listHuyen != null) {
                for (int i = 0; i < listHuyen.size(); i++) {
                    id = listHuyen.get(i).getId();
                    ten = listHuyen.get(i).getTen();
                    mbc = listHuyen.get(i).getMabc();
                    data1 = listHuyen.get(i).getData1();
                    data2 = listHuyen.get(i).getData2();
                    data3 = listHuyen.get(i).getData3();
                    data4 = listHuyen.get(i).getData4();
                    data5 = listHuyen.get(i).getData5();
                    listMbc.add(new MbcItem(id, ten, mbc, data1, data2, data3, data4, data5, Util.DONVI_HUYEN));
                }
            }

            if (listCtTinh != null) {
                for (int i = 0; i < listCtTinh.size(); i++) {
                    id = listCtTinh.get(i).getId();
                    ten = listCtTinh.get(i).getTen();
                    mbc = listCtTinh.get(i).getMabc();
                    data1 = listCtTinh.get(i).getData1();
                    data2 = listCtTinh.get(i).getData2();
                    data3 = listCtTinh.get(i).getData3();
                    data4 = listCtTinh.get(i).getData4();
                    data5 = listCtTinh.get(i).getData5();
                    listMbc.add(new MbcItem(id, ten, mbc, data1, data2, data3, data4, data5, Util.DONVI_CT_TINH));
                }
            }

        } else {
            List<CtHuyen> listCtHuyen = mbcDetail.getListCtHuyen();
            if (listCtHuyen != null) {
                for (int i = 0; i < listCtHuyen.size(); i++) {
                    id = listCtHuyen.get(i).getId();
                    ten = listCtHuyen.get(i).getTen();
                    mbc = listCtHuyen.get(i).getMabc();
                    data1 = listCtHuyen.get(i).getData1();
                    data2 = listCtHuyen.get(i).getData2();
                    data3 = listCtHuyen.get(i).getData3();
                    data4 = listCtHuyen.get(i).getData4();
                    data5 = listCtHuyen.get(i).getData5();
                    listMbc.add(new MbcItem(id, ten, mbc, data1, data2, data3, data4, data5, Util.DONVI_CT_HUYEN));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
