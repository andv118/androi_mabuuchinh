package com.htn.dovanan.mabuuchinh.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MbcItem implements Serializable {

    private int id;
    private String ten;
    private String mabc;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String donVi;

    private String title;

    // contructor use hashMap
    // no used
    public MbcItem(int id, String title, String data1, String data2, String data4, String data5, String donVi) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data4 = data4;
        this.data5 = data5;
        this.donVi = donVi;
        this.title = title;
    }

    // no used
    public MbcItem(int id, String title, String data1, String data2, String data3, String data4, String data5, String donVi) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data4 = data4;
        this.data3 = data3;
        this.data5 = data5;
        this.donVi = donVi;
        this.title = title;
    }

    public MbcItem(int id, String ten, String mabc, String data1, String data2, String data3, String data4, String data5, String donVi) {
        this.id = id;
        this.ten = ten;
        this.mabc = mabc;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.donVi = donVi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMabc() {
        return mabc;
    }

    public void setMabc(String mabc) {
        this.mabc = mabc;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
