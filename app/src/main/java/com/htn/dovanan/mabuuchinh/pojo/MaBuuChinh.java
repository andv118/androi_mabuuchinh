
package com.htn.dovanan.mabuuchinh.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaBuuChinh {

    @SerializedName("tinh")
    @Expose
    private List<Tinh> listTinh = null;

    @SerializedName("huyen")
    @Expose
    private List<Huyen> listHuyen = null;

    @SerializedName("ct_tinh")
    @Expose
    private List<CtTinh> listCtTinh = null;

    @SerializedName("ct_huyen")
    @Expose
    private List<CtHuyen> listCtHuyen = null;

    // tinh
    public List<Tinh> getListTinh() {
        return listTinh;
    }

    public void setListTinh(List<Tinh> tinh) {
        this.listTinh = tinh;
    }

    // huyen
    public List<Huyen> getListHuyen() {
        return listHuyen;
    }

    public void setListHuyen(List<Huyen> huyen) {
        this.listHuyen = huyen;
    }

    // ct tinh
    public List<CtTinh> getListCtTinh() {
        return listCtTinh;
    }

    public void setListCtTinh(List<CtTinh> ctTinh) {
        this.listCtTinh = ctTinh;
    }

    // ct huyen
    public List<CtHuyen> getListCtHuyen() {
        return listCtHuyen;
    }

    public void setListCtHuyen(List<CtHuyen> ctHuyen) {
        this.listCtHuyen = ctHuyen;
    }



}
