package com.htn.dovanan.mabuuchinh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;

public class MainFragGioiThieu extends Fragment implements View.OnClickListener {

    View view;
    Context context;
    // view
    private TextView txtPage1, txtPage2;
    private LinearLayout section1, section2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frag_gioithieu, container, false);
            context = getContext();
        }
        // khoi tao view, gia tri
        initUX();

        return view;
    }

    private void initUX() {
        initView();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_page_1:
                section1.setVisibility(View.VISIBLE);
                section2.setVisibility(View.GONE);
                txtPage1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                txtPage2.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                break;
            case R.id.txt_page_2:
                section1.setVisibility(View.GONE);
                section2.setVisibility(View.VISIBLE);
                txtPage2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                txtPage1.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                break;
        }
    }
    private void initView() {
        txtPage2 = view.findViewById(R.id.txt_page_2);
        txtPage1 = view.findViewById(R.id.txt_page_1);
        section1 = view.findViewById(R.id.section1);
        section2 = view.findViewById(R.id.section2);

        // set onclick
        txtPage2.setOnClickListener(this);
        txtPage1.setOnClickListener(this);
    }


}
