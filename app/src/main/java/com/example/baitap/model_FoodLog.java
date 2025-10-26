package com.example.baitap;


public class model_FoodLog {
    private int id;
    private int monanId;
    private String tenMon;
    private int calo;
    private String ngay;
    private String note;

    public model_FoodLog(int id, int monanId, String tenMon, int calo, String ngay, String note) {
        this.id = id;
        this.monanId = monanId;
        this.tenMon = tenMon;
        this.calo = calo;
        this.ngay = ngay;
        this.note = note;
    }

    public int getId() { return id; }
    public int getMonanId() { return monanId; }
    public String getTenMon() { return tenMon; }
    public int getCalo() { return calo; }
    public String getNgay() { return ngay; }
    public String getNote() { return note; }
}



