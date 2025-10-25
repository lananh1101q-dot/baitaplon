package com.example.baitap;

public class model_Loai {
    private int id;
    private String ten;
    public model_Loai(int id, String ten) {
        this.id = id;
        this.ten = ten;
    }
    public int getId() { return id; }
    public String getTen() { return ten; }
    @Override
    public String toString() { return ten; } // important for Spinner
}

