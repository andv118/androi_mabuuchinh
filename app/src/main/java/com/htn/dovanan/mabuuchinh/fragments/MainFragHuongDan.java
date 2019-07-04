package com.htn.dovanan.mabuuchinh.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.util.LoadImageBitmap;

public class MainFragHuongDan extends Fragment implements View.OnClickListener {

    View view;
    Context context;

    public static String PACKAGE_NAME = "com.demo.dovanan.mabuuchinh";
    // view
    private TextView txtPage1, txtPage2, txtPage3;
    private ScrollView section1, section2, section3;
    private TextView txtGooglePlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_frag_huongdan, container, false);
            context = getContext();
        }
        // khoi tao view, gia tri
        initView();
        loadImageBitmap();

        return view;
    }

    private void initView() {
        txtGooglePlay = view.findViewById(R.id.txt_link_googleplay);
        txtPage2 = view.findViewById(R.id.txt_page_2);
        txtPage1 = view.findViewById(R.id.txt_page_1);
        txtPage3 = view.findViewById(R.id.txt_page_3);
        section1 = view.findViewById(R.id.section1);
        section2 = view.findViewById(R.id.section2);
        section3 = view.findViewById(R.id.section3);

        // set onclick
        txtGooglePlay.setOnClickListener(this);
        txtPage3.setOnClickListener(this);
        txtPage2.setOnClickListener(this);
        txtPage1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_page_1:
                section1.setVisibility(View.VISIBLE);
                section2.setVisibility(View.GONE);
                section3.setVisibility(View.GONE);
                txtPage1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                txtPage2.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                txtPage3.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                break;
            case R.id.txt_page_2:
                section1.setVisibility(View.GONE);
                section2.setVisibility(View.VISIBLE);
                section3.setVisibility(View.GONE);
                txtPage1.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                txtPage2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                txtPage3.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                break;

            case R.id.txt_page_3:
                section1.setVisibility(View.GONE);
                section2.setVisibility(View.GONE);
                section3.setVisibility(View.VISIBLE);
                txtPage1.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                txtPage2.setBackgroundColor(getResources().getColor(R.color.colorGreyBoder));
                txtPage3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                break;

            case R.id.txt_link_googleplay:
                try {
                    String uri = "market://details?id=" + PACKAGE_NAME;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                } catch (Exception e) {
                    String uri = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                break;
        }
    }

    private void loadImageBitmap() {

        ImageView img_sec_1 = (ImageView) view.findViewById(R.id.img_sec_1);
        ImageView img_sec_1_1 = (ImageView) view.findViewById(R.id.img_sec_1_1);

        ImageView img_sec_2 = (ImageView) view.findViewById(R.id.img_sec_2);
        ImageView img_sec_2_2 = (ImageView) view.findViewById(R.id.img_sec_2_2);
//
        ImageView img_sec_3_1 = (ImageView) view.findViewById(R.id.img_sec_3_1);
        ImageView img_sec_3_2 = (ImageView) view.findViewById(R.id.img_sec_3_2);
        ImageView img_sec_3_3 = (ImageView) view.findViewById(R.id.img_sec_3_3);

        img_sec_1.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_1, 300, 60));
        img_sec_1_1.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_1_1, 300, 100));

        img_sec_2.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_2, 300, 60));
        img_sec_2_2.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_2_2, 300, 200));
//
        img_sec_3_1.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_3_1, 300, 50));
        img_sec_3_2.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_3_2, 300, 180));
        img_sec_3_3.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_sec_3_3, 300, 200));



    }
}
