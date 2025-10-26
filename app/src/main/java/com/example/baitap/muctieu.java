package com.example.baitap;

public class muctieu {
    private int id;
    private int userId;
    private String tenMucTieu;
    private String gioiTinh;
    private int tuoi;
    private double chieuCao;
    private double canNang;
    private double bmi;
    private int nangLuong;
    private int luongNuoc;
    private String ngay;

    // ✅ Constructor mặc định
    public muctieu() {}

    // ✅ Constructor đầy đủ khi tạo mới
    public muctieu(int userId, double chieuCao, double canNang, int tuoi, boolean isNam,
                   double bmi, int nangLuong, int luongNuoc, String ngay) {
        this.userId = userId;
        this.gioiTinh = isNam ? "Nam" : "Nữ";
        this.chieuCao = chieuCao;
        this.canNang = canNang;
        this.tuoi = tuoi;
        this.bmi = bmi;
        this.nangLuong = nangLuong;
        this.luongNuoc = luongNuoc;
        this.ngay = ngay;
        this.tenMucTieu = "Giữ cân"; // mặc định ban đầu
    }

    // ✅ Hàm cập nhật giá trị mục tiêu
    public void update(double chieuCao, double canNang, int tuoi, boolean isNam,
                       double bmi, int nangLuong, int luongNuoc, String ngay) {
        this.chieuCao = chieuCao;
        this.canNang = canNang;
        this.tuoi = tuoi;
        this.gioiTinh = isNam ? "Nam" : "Nữ";
        this.bmi = bmi;
        this.nangLuong = nangLuong;
        this.luongNuoc = luongNuoc;
        this.ngay = ngay;
    }

    // ✅ Getter & Setter đầy đủ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTenMucTieu() { return tenMucTieu; }
    public void setTenMucTieu(String tenMucTieu) { this.tenMucTieu = tenMucTieu; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }

    public double getChieuCao() { return chieuCao; }
    public void setChieuCao(double chieuCao) { this.chieuCao = chieuCao; }

    public double getCanNang() { return canNang; }
    public void setCanNang(double canNang) { this.canNang = canNang; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public int getNangLuong() { return nangLuong; }
    public void setNangLuong(int nangLuong) { this.nangLuong = nangLuong; }

    public int getLuongNuoc() { return luongNuoc; }
    public void setLuongNuoc(int luongNuoc) { this.luongNuoc = luongNuoc; }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    // ✅ Hàm phụ để kiểm tra giới tính
    public boolean isGioiTinhNam() {
        return "Nam".equalsIgnoreCase(gioiTinh);
    }
}
