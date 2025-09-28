package com.example.baitap;

public class    tapluyen_employ {
    private int id;
    private String tenBaiTap;
    private int thoiGian; // phút
    private int caloTieuThu;
    private String ngay;

    public tapluyen_employ(int id, String tenBaiTap, int thoiGian, int caloTieuThu, String ngay) {
        this.id = id;
        this.tenBaiTap = tenBaiTap;
        this.thoiGian = thoiGian;
        this.caloTieuThu = caloTieuThu;
        this.ngay = ngay;
    }

    public tapluyen_employ(String tenBaiTap, int thoiGian, int caloTieuThu, String ngay) {
        this.tenBaiTap = tenBaiTap;
        this.thoiGian = thoiGian;
        this.caloTieuThu = caloTieuThu;
        this.ngay = ngay;
    }

    public int getId() { return id; }
    public String getTenBaiTap() { return tenBaiTap; }
    public int getThoiGian() { return thoiGian; }
    public int getCaloTieuThu() { return caloTieuThu; }
    public String getNgay() { return ngay; }

    public void setId(int id) { this.id = id; }
    public void setTenBaiTap(String tenBaiTap) { this.tenBaiTap = tenBaiTap; }
    public void setThoiGian(int thoiGian) { this.thoiGian = thoiGian; }
    public void setCaloTieuThu(int caloTieuThu) { this.caloTieuThu = caloTieuThu; }
    public void setNgay(String ngay) { this.ngay = ngay; }

}


