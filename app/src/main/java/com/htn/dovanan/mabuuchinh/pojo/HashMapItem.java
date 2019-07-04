package com.htn.dovanan.mabuuchinh.pojo;

/* class này để xử lý vấn đề trùng địa chỉ
 * nếu 1 địa chỉ trùng nhau thì cộng các title lại và giữ nguyên địa chỉ
 */

import java.util.List;

public class HashMapItem {
    private int id;
    private List<String> mbc;
    private List<String> name;
    private String phone;
    private String data2;
    private String adress;
    private String email;
    private String website;
    private String donVi;

    public HashMapItem(int id, List<String> mbc, List<String> name, String phone, String data2,
                       String adress, String email, String website, String donVi) {
        this.id = id;
        this.mbc = mbc;
        this.name = name;
        this.phone = phone;
        this.data2 = data2;
        this.adress = adress;
        this.email = email;
        this.website = website;
        this.donVi = donVi;
    }

    public HashMapItem(int id, List<String> mbc, List<String> name, String phone, String data2,
                       String email, String website, String donVi) {
        this.id = id;
        this.mbc = mbc;
        this.name = name;
        this.phone = phone;
        this.data2 = data2;
        this.email = email;
        this.website = website;
        this.donVi = donVi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMbc() {
        return mbc;
    }

    public void setMbc(List<String> mbc) {
        this.mbc = mbc;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }
}
