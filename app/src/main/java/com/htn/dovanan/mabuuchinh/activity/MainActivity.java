package com.htn.dovanan.mabuuchinh.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.htn.dovanan.mabuuchinh.R;
import com.htn.dovanan.mabuuchinh.adapter.DowloadAdapter;
import com.htn.dovanan.mabuuchinh.fragments.FragmentSearch;
import com.htn.dovanan.mabuuchinh.fragments.MainFragGioiThieu;
import com.htn.dovanan.mabuuchinh.fragments.MainFragHuongDan;
import com.htn.dovanan.mabuuchinh.fragments.MainFragVanBan;
import com.htn.dovanan.mabuuchinh.listener.AnimationEndListener;
import com.htn.dovanan.mabuuchinh.listener.ItemDonloadListenner;
import com.htn.dovanan.mabuuchinh.pojo.ItemDownload;
import com.htn.dovanan.mabuuchinh.util.CrossAnimation;
import com.htn.dovanan.mabuuchinh.util.LoadImageBitmap;
import com.htn.dovanan.mabuuchinh.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    //    view
    private FrameLayout container;
    private RelativeLayout header, footer;
    private LinearLayout clickSearch;
    private TextView txtDownload;
    // nav menu
    private TextView txtTrangchu, txtGioiThieu, txtVanBan, txtHuongDan;

    // fragment
    private FragmentSearch fragSearch;
    private MainFragGioiThieu fragGioiThieu;
    private MainFragVanBan fragVanBan;
    private MainFragHuongDan fragHuongDan;

    // dialog
    Dialog dialog;
    long backPressTime;

    // variable
    public Boolean isClickSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // khoi tao cac view, nav
        // load anh
        initUX();
    }

    /*
    Khoi tao bien
     */
    private void initVariable() {
        fragSearch = new FragmentSearch();
        fragGioiThieu = new MainFragGioiThieu();
        fragVanBan = new MainFragVanBan();
        fragHuongDan = new MainFragHuongDan();
    }

    /*
    khoi tao view, bien
     */
    private void initUX() {
        // khoi tao view
        initView();

        // khoi tao bien
        initVariable();

        // load anh bitmap khi run giao dien main
        loadImageBitmap();
    }

    private void initView() {
//        View main
        container = (FrameLayout) findViewById(R.id.container);
        header = (RelativeLayout) findViewById(R.id.header);
        footer = (RelativeLayout) findViewById(R.id.footer);
        clickSearch = (LinearLayout) findViewById(R.id.click_search);
        txtDownload = (TextView) findViewById(R.id.txt_download);
        txtTrangchu = (TextView) findViewById(R.id.txtTrangChu);
        txtGioiThieu = (TextView) findViewById(R.id.txtGioiThieu);
        txtVanBan = (TextView) findViewById(R.id.txtVanBan);
        txtHuongDan = (TextView) findViewById(R.id.txtHuongDan);

//        set click
        txtDownload.setOnClickListener(this);
        txtTrangchu.setOnClickListener(this);
        txtGioiThieu.setOnClickListener(this);
        txtVanBan.setOnClickListener(this);
        txtHuongDan.setOnClickListener(this);
        clickSearch.setOnClickListener(this);


        Fragment fragCurrent = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragCurrent instanceof FragmentSearch || fragCurrent instanceof MainFragGioiThieu
                || fragCurrent instanceof MainFragVanBan || fragCurrent instanceof MainFragHuongDan) {
        } else {
            clickSearch.setOnClickListener(this);
        }

    }

    private void loadImageBitmap() {
        ImageView imgQuocHuy = (ImageView) findViewById(R.id.img_quochuy);
        ImageView imgLogoMain = (ImageView) findViewById(R.id.img_logo);
        ImageView imgLogoFooter = (ImageView) findViewById(R.id.img_logo_footer);
        ImageView imgLanguageVni = (ImageView) findViewById(R.id.img_language_vni);
        ImageView imgLanguageEng = (ImageView) findViewById(R.id.img_language_eng);

        imgQuocHuy.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_logo_quochuy, 157, 157));
        imgLogoMain.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_logo_main, 150, 150));
        imgLogoFooter.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_logo_main, 150, 150));
        imgLanguageEng.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_language_eng, 30, 20));
        imgLanguageVni.setImageBitmap(LoadImageBitmap.decodeSampleBitmapFromResource(getResources(),
                R.drawable.img_language_vn, 30, 20));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//        click search
            case R.id.click_search:

                if (Util.haveNetwork(this)) {
                    if (isClickSearch) {
                        // animation
                        CrossAnimation.slideUp(header);
                        CrossAnimation.slideUp(container, new AnimationEndListener() {
                            @Override
                            public void endAnimation() {
                                // khi load animation xong
                                header.setVisibility(View.GONE); // an header
                                CrossAnimation.slideDown(container);

                                // add fragment
                                addFragment(fragSearch);
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Kiểm tra lại kết nối internet!", Toast.LENGTH_SHORT).show();
                }

                break;

//        click txt download
            case R.id.txt_download:
                if (isClickSearch) {
                    showDialogDownload();
//                    Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
                }
                break;

//        click trang chu
            case R.id.txtTrangChu:
//                Toast.makeText(this, "trang chu", Toast.LENGTH_SHORT).show();

                // remove all fragment
                removeAllFragment();

                txtTrangchu.setTextColor(getResources().getColor(R.color.colorAccent));
                txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
                txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

//        click Gioi Thieu
            case R.id.txtGioiThieu:
//                Toast.makeText(this, "gioi thieu", Toast.LENGTH_SHORT).show();
                addFragment(fragGioiThieu);
                txtTrangchu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtGioiThieu.setTextColor(getResources().getColor(R.color.colorAccent));
                txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
                txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

//        click Van Ban
            case R.id.txtVanBan:
//                Toast.makeText(this, "van ban", Toast.LENGTH_SHORT).show();
                addFragment(fragVanBan);
                txtTrangchu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtVanBan.setTextColor(getResources().getColor(R.color.colorAccent));
                txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

//        click Huong dan
            case R.id.txtHuongDan:
//                Toast.makeText(this, "huong dan", Toast.LENGTH_SHORT).show();
                addFragment(fragHuongDan);
                txtTrangchu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
                txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
                txtHuongDan.setTextColor(getResources().getColor(R.color.colorAccent));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        // get fragment hien tai
        Fragment fragCurrent = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragCurrent instanceof FragmentSearch) {
//            Toast.makeText(this, "fragment search", Toast.LENGTH_SHORT).show();
            header.setVisibility(View.VISIBLE);
            CrossAnimation.slideDown(header);

            // clear text and focus
            removeFragment(fragCurrent);
            fragSearch.clearTextAndFocus();
            isClickSearch = true;
        }

        // frag hien tai: gioi thieu
        else if (fragCurrent instanceof MainFragGioiThieu) {
            removeAllFragment();
            txtTrangchu.setTextColor(getResources().getColor(R.color.colorAccent));
            txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
            txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
            txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));
        }

        // frag hien tai: van ban
        else if (fragCurrent instanceof MainFragVanBan) {
            removeAllFragment();
            txtTrangchu.setTextColor(getResources().getColor(R.color.colorAccent));
            txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
            txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
            txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));

        }

        // frag hien tai: huong dan
        else if (fragCurrent instanceof MainFragHuongDan) {
            removeAllFragment();
            txtTrangchu.setTextColor(getResources().getColor(R.color.colorAccent));
            txtGioiThieu.setTextColor(getResources().getColor(R.color.colorWhite));
            txtVanBan.setTextColor(getResources().getColor(R.color.colorWhite));
            txtHuongDan.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            if (backPressTime + 3000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, getResources().getString(R.string.Main_Activity_Exit_app), Toast.LENGTH_SHORT).show();
            }
            backPressTime = System.currentTimeMillis();


        }

    }

    /*
    them fragment
     */
    public void addFragment(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (frg != null) {
            if (frg.isAdded()) {
                ft.replace(R.id.container, frg);
            } else {
                ft.add(R.id.container, frg);
                ft.addToBackStack(null);
            }
        }
        ft.commit();

        // khi add fragment thi ko cho click vao search nua
        isClickSearch = false;
    }

    /*
    xoa fragment
     */
    public void removeFragment(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (frg != null) {
            ft.remove(frg);
        }
        ft.commit();
    }

    /*
    xoa tat ca cac fragment trong container
     */
    public void removeAllFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        // neu remove tat ca tro ve home thi cho click search
        isClickSearch = true;

    }

    private void showDialogDownload() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_direction);
        final List<ItemDownload> listItem = new ArrayList<>();

        ImageView exit = (ImageView) dialog.findViewById(R.id.img_close);
        final Button btnAll = (Button) dialog.findViewById(R.id.btn_download_all);
        final Button btnTinh = (Button) dialog.findViewById(R.id.btn_download_tinh);
        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView_download_mbc);

        // btn all
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItem.clear();
                listItem.add(new ItemDownload("Mã_bưu_chính_quốc_gia_VN.xlxs", "Ma_buu_chinh_quoc_gia_VN.xlsx"));
                recyclerView.setVisibility(View.VISIBLE);
                btnAll.setBackgroundResource(R.color.colorBlue500);
                btnTinh.setBackgroundResource(R.color.colorGrey);
                recyclerView.setMinimumHeight(30);
                initRecyclerviewDonload(recyclerView, listItem);
            }
        });

        // btn tinh
        btnTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItem.clear();
                addListMbcDownload(listItem);// add item
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setMinimumHeight(150);
                btnTinh.setBackgroundResource(R.color.colorBlue500);
                btnAll.setBackgroundResource(R.color.colorGrey);
                initRecyclerviewDonload(recyclerView, listItem);
            }
        });

        // exit
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initRecyclerviewDonload(RecyclerView recyclerView, List<ItemDownload> itemList) {
        DowloadAdapter adapter = new DowloadAdapter(itemList);
        recyclerView.setAdapter(adapter);
        // neu la all: de 1 cot con lai de 2 cot
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setClicklistener(new ItemDonloadListenner() {
            @Override
            public void onClickItem(final ItemDownload item, int position) {
                // alert dialog
                String alert = "Bạn có muốn tải file về không?";
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("Thông báo")
                        .setMessage(alert);
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog1, int which) {
                                String url = "http://techgarden.vn/api_mbc_update/file_excel_download/" + item.getUrl();
                                downloadFileFromWebservice(url);
                                dialog.dismiss();

                            }
                        })

                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    private void downloadFileFromWebservice(String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }

    private void addListMbcDownload(List<ItemDownload> item) {
        item.add(new ItemDownload("90_An_Giang.xlxs", "1.An_Giang.xlsx"));
        item.add(new ItemDownload("78_Ba_Rịa_Vũng_Tàu.xlxs", "2.Ba_Ria_Vung_Tau.xlsx"));
        item.add(new ItemDownload("26_Bắc_Giang.xlxs", "3.Bac_Giang.xlsx"));
        item.add(new ItemDownload("23_Bắc_Kạn.xlxs", "4.Bac_Kan.xlsx"));
        item.add(new ItemDownload("97_Bạc_Liêu.xlxs", "5.Bac_Lieu.xlsx"));
        item.add(new ItemDownload("16_Bắc_Ninh.xlxs", "6.Bac_Ninh.xlsx"));
        item.add(new ItemDownload("86_Bến_Tre.xlxs", "7.Ben_Tre.xlsx"));
        item.add(new ItemDownload("55_Bình_Định.xlxs", "8.Binh_Dinh.xlsx"));
        item.add(new ItemDownload("75_Bình_Dương.xlxs", "9.Binh_Duong.xlsx"));
        item.add(new ItemDownload("67_Bình_Phước.xlxs", "10.Binh_Phuoc.xlsx"));
        item.add(new ItemDownload("77_Bình_Thuận.xlxs", "11.Binh_Thuan.xlsx"));
        item.add(new ItemDownload("98_Cà_Mau.xlxs", "12.Ca_Mau.xlsx"));
        item.add(new ItemDownload("94_Thành_Phố_Cần_Thơ.xlxs", "13.Can_Tho.xlsx"));
        item.add(new ItemDownload("21_Cao_Bằng.xlxs", "14.Cao_Bang.xlsx"));
        item.add(new ItemDownload("50_Thành_Phố_Đà_Nẵng.xlxs", "15.Da_Nang.xlsx"));
        item.add(new ItemDownload("63-64_Đắk_Lắk.xlxs", "16.Dak_Lac.xlsx"));
        item.add(new ItemDownload("65_Đắk_Nông.xlxs", "17.Dac_Nong.xlsx"));
        item.add(new ItemDownload("32_Điện_Biên.xlxs", "18.Dien_Bien.xlsx"));
        item.add(new ItemDownload("76_Đồng_Nai.xlxs", "19.Dong_Nai.xlsx"));
        item.add(new ItemDownload("81_Đồng_Tháp.xlxs", "20.Dong_Thap.xlsx"));
        item.add(new ItemDownload("61-62_Gia_Lai.xlxs", "21.Gia_Lai.xlsx"));
        item.add(new ItemDownload("20_Hà_Giang.xlxs", "22.Ha_Giang.xlsx"));
        item.add(new ItemDownload("18_Hà_Nam.xlxs", "23.Ha_Nam.xlsx"));
        item.add(new ItemDownload("10-14_Thành_Phố_Hà_Nội.xlxs", "24.Ha_Noi.xlsx"));
        item.add(new ItemDownload("45-46_Hà_Tĩnh.xlxs", "25.Ha_Tinh.xlsx"));
        item.add(new ItemDownload("03_Hải_Dương.xlxs", "26.Hai_Duong.xlsx"));
        item.add(new ItemDownload("04-05_Hải_Phòng.xlxs", "27.Hai_Phong.xlsx"));
        item.add(new ItemDownload("95_Hậu_Giang.xlxs", "28.Hau_Giang.xlsx"));
        item.add(new ItemDownload("70-74_Thành_Phố_Hồ_Chí_Minh.xlxs", "29.Ho_Chi_Minh.xlsx"));
        item.add(new ItemDownload("36_Hòa_Bình.xlxs", "30.Hoa_Binh.xlsx"));
        item.add(new ItemDownload("17_Hưng_Yên.xlxs", "31.Hung_Yen.xlsx"));
        item.add(new ItemDownload("57_Khánh_Hòa.xlxs", "32.Kanh_Hoa.xlsx"));
        item.add(new ItemDownload("91-92_Kiên_Giang.xlxs", "33.Kien_Giang.xlsx"));
        item.add(new ItemDownload("60_Kon_Tum.xlxs", "34.Kon_Tum.xlsx"));
        item.add(new ItemDownload("30_Lai_Châu.xlxs", "35.Lai_Chau.xlsx"));
        item.add(new ItemDownload("66_Lâm_Đồng.xlxs", "36.Lam_Dong.xlsx"));
        item.add(new ItemDownload("25_Lạng_Sơn.xlxs", "37.Lang_Son.xlsx"));
        item.add(new ItemDownload("31_Lào_Cai.xlxs", "38.Lao_Cai.xlsx"));
        item.add(new ItemDownload("82-83_Long_An.xlxs", "39.Long_An.xlsx"));
        item.add(new ItemDownload("07_Nam_Định.xlxs", "40.Nam_Dinh.xlsx"));
        item.add(new ItemDownload("43-44_Nghệ_An.xlxs", "41.Nghe_An.xlsx"));
        item.add(new ItemDownload("08_Ninh_Bình.xlxs", "42.Ninh_Binh.xlsx"));
        item.add(new ItemDownload("59_Ninh_Thuận.xlxs", "43.Ninh_Thuan.xlsx"));
        item.add(new ItemDownload("35_Phú_Thọ.xlxs", "44.Phu_Tho.xlsx"));
        item.add(new ItemDownload("56_Phú_Yên.xlxs", "45.Phu_Xuyen.xlsx"));
        item.add(new ItemDownload("47_Quảng_Bình.xlxs", "46.Quang_Binh.xlsx"));
        item.add(new ItemDownload("51-52_Quảng_Nam.xlxs", "47.Quang_Nam.xlsx"));
        item.add(new ItemDownload("53-54_Quảng_Ngãi.xlxs", "48.Quang_Ngai.xlsx"));
        item.add(new ItemDownload("01-02_Quảng_Ninh.xlxs", "49.Quang_Ninh.xlsx"));
        item.add(new ItemDownload("48_Quảng_Trị.xlxs", "50.Quang_Tri.xlsx"));
        item.add(new ItemDownload("96_Sóc_Trăng.xlxs", "51.Soc_Trang.xlsx"));
        item.add(new ItemDownload("34_Sơn_La.xlxs", "52.Son_La.xlsx"));
        item.add(new ItemDownload("80_Tây_Ninh.xlxs", "53.Tay_Ninh.xlsx"));
        item.add(new ItemDownload("06_Thái_Bình.xlxs", "54.Thai_Binh.xlsx"));
        item.add(new ItemDownload("24_Thái_Nguyên.xlxs", "55.Thai_Nguyen.xlsx"));
        item.add(new ItemDownload("40_42_Thanh_Hóa.xlxs", "56.Thanh_Hoa.xlsx"));
        item.add(new ItemDownload("49_Thừa_Thiên_Huế.xlxs", "57.Thua_Thien_Hue.xlsx"));
        item.add(new ItemDownload("84_Tiền_Giang.xlxs", "58._Tien_Giang.xlsx"));
        item.add(new ItemDownload("87_Trà_Vinh.xlxs", "59.Tra_Vinh.xlsx"));
        item.add(new ItemDownload("22_Tuyên_Quang.xlxs", "60.Tuyen_Quang.xlsx"));
        item.add(new ItemDownload("85_Vĩnh_Long.xlxs", "61.Vinh_Long.xlsx"));
        item.add(new ItemDownload("15_Vĩnh_Phúc.xlxs", "62.Vinh_Phuc.xlsx"));
        item.add(new ItemDownload("33_Yên_Bái.xlxs", "63.Yen_Bai.xlsx"));
    }
}

