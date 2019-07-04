package com.htn.dovanan.mabuuchinh.pojo;

public class ItemDownload {
    private String tinh;
    private String url;

    public ItemDownload(String tinh, String url) {
        this.tinh = tinh;
        this.url = url;
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
