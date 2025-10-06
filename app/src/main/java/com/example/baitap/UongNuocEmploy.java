package com.example.baitap;

public class UongNuocEmploy {
    private int id;
    private int luongNuoc;  // ml
    private String ngay;

    public UongNuocEmploy(int id, int luongNuoc, String ngay) {
        this.id = id;
        this.luongNuoc = luongNuoc;
        this.ngay = ngay;
    }

    public UongNuocEmploy(int luongNuoc, String ngay) {
        this.luongNuoc = luongNuoc;
        this.ngay = ngay;
    }

    public int getId() {
        return id;
    }

    public int getLuongNuoc() {
        return luongNuoc;
    }

    public void setLuongNuoc(int luongNuoc) {
        this.luongNuoc = luongNuoc;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
