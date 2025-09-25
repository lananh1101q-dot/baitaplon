package com.example.baitap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class luyentap_adapter extends RecyclerView.Adapter<luyentap_adapter.BaiTapViewHolder> {

    private List<luyentap_employ> danhSach;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public luyentap_adapter(List<luyentap_employ> danhSach) {
        this.danhSach = danhSach;
    }

    @NonNull
    @Override
    public BaiTapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_tapluyen_listitem, parent, false); // file XML bạn gửi
        return new BaiTapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiTapViewHolder holder, int position) {
        luyentap_employ bt = danhSach.get(position);
        holder.txtTen.setText(bt.getTen());
        holder.txtCalo.setText(bt.getCalo() + " kcal");
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    public class BaiTapViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtCalo;
        ImageButton btnEdit, btnDelete;

        public BaiTapViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtCalo = itemView.findViewById(R.id.txtCalo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    int pos = getBindingAdapterPosition(); // dùng cái này thay vì getAdapterPosition()
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onEdit(pos);
                    }
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onDelete(pos);
                    }
                }
            });

        }
    }
}
