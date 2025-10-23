package com.example.baitap;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class tapluyen_sua extends DialogFragment {

    private tapluyen_employ tapLuyen;
    private TapLuyenDAO dao;
    private tapluyen_adapter adapter;

    public tapluyen_sua(tapluyen_employ tapLuyen, TapLuyenDAO dao, tapluyen_adapter adapter) {
        this.tapLuyen = tapLuyen;
        this.dao = dao;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sửa bài tập");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        // Danh mục bài tập
        final Spinner spnBaiTap = new Spinner(getContext());
        String[] dsBaiTap = {"Chạy bộ", "Đạp xe", "Bơi lội", "Yoga", "Nhảy dây",
                "Gập bụng", "Plank", "Đi bộ", "Squat", "Tennis"};
        ArrayAdapter<String> baiTapAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, dsBaiTap);
        spnBaiTap.setAdapter(baiTapAdapter);
        spnBaiTap.setSelection(baiTapAdapter.getPosition(tapLuyen.getTenBaiTap()));
        layout.addView(spnBaiTap);

        // Mức thời gian đa dạng
        final Spinner spnThoiGian = new Spinner(getContext());
        Integer[] dsThoiGian = {5, 10, 15, 20, 30, 45, 60, 75, 90, 120};
        ArrayAdapter<Integer> tgAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, dsThoiGian);
        spnThoiGian.setAdapter(tgAdapter);

        // Chọn đúng giá trị hiện tại
        int index = 0;
        for (int i = 0; i < dsThoiGian.length; i++) {
            if (dsThoiGian[i] == tapLuyen.getThoiGian()) {
                index = i;
                break;
            }
        }
        spnThoiGian.setSelection(index);
        layout.addView(spnThoiGian);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenMoi = spnBaiTap.getSelectedItem().toString();
            int thoiGianMoi = (Integer) spnThoiGian.getSelectedItem();
            int caloMoi = tapluyen_DanhMucBaiTap.tinhCalo(tenMoi, thoiGianMoi);

            tapLuyen.setTenBaiTap(tenMoi);
            tapLuyen.setThoiGian(thoiGianMoi);
            tapLuyen.setCaloTieuThu(caloMoi);

            dao.update(tapLuyen);

            adapter.refreshData(dao.getByDate(tapLuyen.getNgay()));

        });

        builder.setNegativeButton("Hủy", null);
        return builder.create();
    }
}
