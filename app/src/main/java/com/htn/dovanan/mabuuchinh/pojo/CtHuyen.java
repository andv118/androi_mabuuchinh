
package com.htn.dovanan.mabuuchinh.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CtHuyen {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("doi_tuong_gan")
    @Expose
    private String doiTuongGan;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("mabc")
    @Expose
    private String mabc;
    @SerializedName("data1")
    @Expose
    private String data1;
    @SerializedName("data2")
    @Expose
    private String data2;
    @SerializedName("data3")
    @Expose
    private String data3;
    @SerializedName("data4")
    @Expose
    private String data4;
    @SerializedName("data5")
    @Expose
    private String data5;
    @SerializedName("id_huyen")
    @Expose
    private Integer idHuyen;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    // contructor
    public CtHuyen(int id, String doiTuongGan, String ten, String mabc,
                   String data1, String data2, String data3, String data4, String data5, Integer idHuyen) {
        this.id = id;
        this.doiTuongGan = doiTuongGan;
        this.ten = ten;
        this.mabc = mabc;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.idHuyen = idHuyen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoiTuongGan() {
        return doiTuongGan;
    }

    public void setDoiTuongGan(String doiTuongGan) {
        this.doiTuongGan = doiTuongGan;
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

    public Integer getIdHuyen() {
        return idHuyen;
    }

    public void setIdHuyen(Integer idHuyen) {
        this.idHuyen = idHuyen;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
