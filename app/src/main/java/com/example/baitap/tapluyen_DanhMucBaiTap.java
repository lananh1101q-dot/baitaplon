package com.example.baitap;



import java.util.HashMap;
import java.util.Map;

    public class tapluyen_DanhMucBaiTap {

        // Mức calo trung bình / phút cho từng loại bài tập
        public static final Map<String, Integer> CALO_MAP = new HashMap<>();

        static {
            CALO_MAP.put("Chạy bộ", 10);       // 10 calo/phút
            CALO_MAP.put("Đạp xe", 8);
            CALO_MAP.put("Bơi lội", 9);
            CALO_MAP.put("Yoga", 5);
            CALO_MAP.put("Nhảy dây", 12);
            CALO_MAP.put("Gập bụng", 7);
            CALO_MAP.put("Plank", 6);
            CALO_MAP.put("Đi bộ", 4);
            CALO_MAP.put("Squat", 8);
            CALO_MAP.put("Tennis", 9);
        }

        // Hàm lấy calo tiêu thụ tương ứng tên bài tập + thời gian
        public static int tinhCalo(String tenBaiTap, int thoiGianPhut) {

            Integer caloPhut = CALO_MAP.get(tenBaiTap);
            if (caloPhut == null) caloPhut = 5; // mặc định nếu không có
            return caloPhut * thoiGianPhut;
        }
    }


