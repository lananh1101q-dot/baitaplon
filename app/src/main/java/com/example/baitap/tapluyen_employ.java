package com.example.baitap;

public class tapluyen_employ {
    private String ten;
    private int thoigian; // phút
    private int calo;     // calo tiêu thụ

    public tapluyen_employ(String ten, int thoigian, int calo) {
        this.ten = ten;
        this.thoigian = thoigian;
        this.calo = calo;
    }

    public String getTen() { return ten; }
    public int getThoigian() { return thoigian; }
    public int getCalo() { return calo; }

    public void setTen(String ten) { this.ten = ten; }
    public void setThoigian(int thoigian) { this.thoigian = thoigian; }
    public void setCalo(int calo) { this.calo = calo; }
}
