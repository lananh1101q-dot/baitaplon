package com.example.baitap;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

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

        final EditText edtTen = new EditText(getContext());
        edtTen.setHint("Tên bài tập");
        edtTen.setText(tapLuyen.getTenBaiTap());
        layout.addView(edtTen);

        final EditText edtTime = new EditText(getContext());
        edtTime.setHint("Thời gian (phút)");
        edtTime.setText(String.valueOf(tapLuyen.getThoiGian()));
        layout.addView(edtTime);

        final EditText edtCalo = new EditText(getContext());
        edtCalo.setHint("Calo tiêu thụ");
        edtCalo.setText(String.valueOf(tapLuyen.getCaloTieuThu()));
        layout.addView(edtCalo);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            tapLuyen.setTenBaiTap(edtTen.getText().toString());
            tapLuyen.setThoiGian(Integer.parseInt(edtTime.getText().toString()));
            tapLuyen.setCaloTieuThu(Integer.parseInt(edtCalo.getText().toString()));
            dao.update(tapLuyen);
            adapter.refreshData(dao.getAll());
        });

        builder.setNegativeButton("Hủy", null);

        return builder.create();
    }
}
