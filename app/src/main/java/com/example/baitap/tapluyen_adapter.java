package com.example.baitap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class tapluyen_adapter extends RecyclerView.Adapter<tapluyen_adapter.ViewHolder> {

    private Context context;
    private ArrayList<tapluyen_employ> list;
    private TapLuyenDAO dao;

    public tapluyen_adapter(Context context, ArrayList<tapluyen_employ> list, TapLuyenDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_tapluyen_listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tapluyen_employ tl = list.get(position);
        holder.txtTen.setText(tl.getTenBaiTap());
        holder.txtCalo.setText(tl.getCaloTieuThu() + " kcal");
        holder.txtThoigian.setText(tl.getThoiGian() + " phút");

        // Edit
        holder.btnEdit.setOnClickListener(v -> {
            tapluyen_sua dialog = new tapluyen_sua(tl, dao, this);
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "EditDialog");
        });

        // Delete
        holder.btnDelete.setOnClickListener(v -> {
            dao.delete(tl.getId());
            list.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Dùng để load lại data
    public void refreshData(ArrayList<tapluyen_employ> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtCalo, txtThoigian;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtCalo = itemView.findViewById(R.id.txtCalo);
            txtThoigian = itemView.findViewById(R.id.txtThoigian);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
