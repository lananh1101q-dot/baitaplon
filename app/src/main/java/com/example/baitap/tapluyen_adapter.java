package com.example.baitap;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class tapluyen_adapter extends ArrayAdapter<tapluyen_employ> {
    private Context context;
    private int resource;
    private List<tapluyen_employ> list;
    private TapLuyenDAO dao;

    public tapluyen_adapter(Context context, int resource, List<tapluyen_employ> objects, TapLuyenDAO dao) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
        this.dao = dao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        tapluyen_employ tl = list.get(position);

        // Ánh xạ view
        TextView txtTen = convertView.findViewById(R.id.txtTen);
        TextView txtCalo = convertView.findViewById(R.id.txtCalo);
        TextView txtThoigian = convertView.findViewById(R.id.txtThoigian);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
        ImageView imgBaitap = convertView.findViewById(R.id.imgBaitap);

        // Gán dữ liệu
        txtTen.setText(tl.getTenBaiTap());
        txtCalo.setText(tl.getCaloTieuThu() + " kcal");
        txtThoigian.setText(tl.getThoiGian() + " phút");

        // Ảnh theo danh mục bài tập
        switch (tl.getTenBaiTap().toLowerCase()) {
            case "chạy bộ":
                imgBaitap.setImageResource(R.drawable.chaybo);
                break;
            case "đạp xe":
                imgBaitap.setImageResource(R.drawable.dapxe);
                break;
            case "bơi lội":
                imgBaitap.setImageResource(R.drawable.boi);
                break;
            case "yoga":
                imgBaitap.setImageResource(R.drawable.yoga);
                break;
            case "nhảy dây":
                imgBaitap.setImageResource(R.drawable.nhayday);
                break;
            case "gập bụng":
                imgBaitap.setImageResource(R.drawable.gapbung);
                break;
            case "plank":
                imgBaitap.setImageResource(R.drawable.plank);
                break;
            case "đi bộ":
                imgBaitap.setImageResource(R.drawable.dibo);
                break;
            case "squat":
                imgBaitap.setImageResource(R.drawable.squat);
                break;
            case "tennis":
                imgBaitap.setImageResource(R.drawable.tennis);
                break;
            default:
                imgBaitap.setImageResource(R.drawable.tapluyen);
                break;
        }

        // Nút sửa
        btnEdit.setOnClickListener(v -> {
            tapluyen_sua dialog = new tapluyen_sua(tl, dao, this);
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "EditDialog");
        });

        // Nút xóa
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa bài tập này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        dao.delete(tl.getId());
                        list.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã xóa bài tập", Toast.LENGTH_SHORT).show();

                        // ✅ Cập nhật tổng calo sau khi xóa
                        if (context instanceof tapluyen)
                            ((tapluyen) context).updateTongCaloHomNay();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        return convertView;
    }

    // Làm mới dữ liệu
    public void refreshData(List<tapluyen_employ> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();

        // ✅ Cập nhật lại tổng calo mỗi lần refresh
        if (context instanceof tapluyen)
            ((tapluyen) context).updateTongCaloHomNay();
    }
}
