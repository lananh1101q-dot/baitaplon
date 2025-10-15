package com.example.baitap;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class tapluyen_adapter extends ArrayAdapter<tapluyen_employ> {
    private Context context;
    private int resource;
    private List<tapluyen_employ> list;
    private TapLuyenDAO dao;
    private ArrayList<tapluyen_employ> selectedList = new ArrayList<>();

    // Constructor
    public tapluyen_adapter(Context context, int resource, List<tapluyen_employ> objects, TapLuyenDAO dao) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
        this.dao = dao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.txtTen = convertView.findViewById(R.id.txtTen);
            holder.txtCalo = convertView.findViewById(R.id.txtCalo);
            holder.txtThoigian = convertView.findViewById(R.id.txtThoigian);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        tapluyen_employ tl = list.get(position);

        // Hiển thị thông tin
        holder.txtTen.setText(tl.getTenBaiTap());
        holder.txtCalo.setText(tl.getCaloTieuThu() + " kcal");
        holder.txtThoigian.setText(tl.getThoiGian() + " phút");

        // ✅ Checkbox giữ trạng thái
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedList.contains(tl));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedList.contains(tl)) selectedList.add(tl);
            } else {
                selectedList.remove(tl);
            }
        });

        // ✏️ Nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            tapluyen_sua dialog = new tapluyen_sua(tl, dao, this);
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "EditDialog");
        });

        // ❌ Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa").setMessage("Bạn có chắc muốn xóa bài tập này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        dao.delete(tl.getId());
                        list.remove(position);
                        selectedList.remove(tl); // ✅ Xóa khỏi danh sách chọn luôn
                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã xóa bài tập", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        return convertView;
    }

    // ViewHolder pattern
    static class ViewHolder {
        TextView txtTen, txtCalo, txtThoigian;
        ImageButton btnEdit, btnDelete;
        CheckBox checkBox;
    }

    // 🔄 Refresh dữ liệu mà không xóa selectedList
    public void refreshData(ArrayList<tapluyen_employ> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    // ✅ Lấy danh sách bài tập đã chọn
    public ArrayList<tapluyen_employ> getSelectedList() {
        return new ArrayList<>(selectedList);
    }
}