package com.example.baitap;

public class muctieu {
    private int id;
    private String tenMucTieu;
    private double chieuCao;
    private double canNang;
    private double bmi;
    private int nangLuong;
    private int luongNuoc;
    private String ngay;

    public muctieu(int id, String tenMucTieu, double chieuCao, double canNang,
                   double bmi, int nangLuong, int luongNuoc, String ngay) {
        this.id = id;
        this.tenMucTieu = tenMucTieu;
        this.chieuCao = chieuCao;
        this.canNang = canNang;
        this.bmi = bmi;
        this.nangLuong = nangLuong;
        this.luongNuoc = luongNuoc;
        this.ngay = ngay;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getTenMucTieu() { return tenMucTieu; }
    public double getChieuCao() { return chieuCao; }
    public double getCanNang() { return canNang; }
    public double getBmi() { return bmi; }
    public int getNangLuong() { return nangLuong; }
    public int getLuongNuoc() { return luongNuoc; }
    public String getNgay() { return ngay; }
    public void setId(int id) { this.id = id; }
    public void setTenMucTieu(String tenMucTieu) { this.tenMucTieu = tenMucTieu; }
    public void setChieuCao(double chieuCao) { this.chieuCao = chieuCao; }
    public void setCanNang(double canNang) { this.canNang = canNang; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    public void setNangLuong(int nangLuong) { this.nangLuong = nangLuong; }
    public void setLuongNuoc(int luongNuoc) { this.luongNuoc = luongNuoc; }
    public void setNgay(String ngay) { this.ngay = ngay; }
}
