package com.example.baitap;



public class model_Monan {
    private int id;
    private String ten;
    private int calo;
    private int loaiId;

    public model_Monan(int id, String ten, int calo, int loaiId) {
        this.id = id;
        this.ten = ten;
        this.calo = calo;
        this.loaiId = loaiId;
    }

    public int getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public int getCalo() {
        return calo;
    }

    public int getLoaiId() {
        return loaiId;
    }

    @Override
    public String toString() {
        return ten;
    } // for Spinner
}
