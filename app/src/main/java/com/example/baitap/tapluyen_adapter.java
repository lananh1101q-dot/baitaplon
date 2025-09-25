package com.example.baitap;

import android.widget.Filter;
import android.widget.Filterable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class tapluyen_adapter extends RecyclerView.Adapter<tapluyen_adapter.BaiTapViewHolder> implements Filterable {

    private List<tapluyen_employ> danhSach;
    private List<tapluyen_employ> danhSachFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public tapluyen_adapter(List<tapluyen_employ> danhSach) {
        this.danhSach = danhSach;
        this.danhSachFull = new ArrayList<>(danhSach);
    }

    @NonNull
    @Override
    public BaiTapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_tapluyen_listitem, parent, false);
        return new BaiTapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiTapViewHolder holder, int position) {
        tapluyen_employ bt = danhSach.get(position);
        holder.txtTen.setText(bt.getTen());
        holder.txtCalo.setText(bt.getCalo() + " kcal");
        holder.txtThoigian.setText(bt.getThoigian() + " phút");
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<tapluyen_employ> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(danhSachFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (tapluyen_employ item : danhSachFull) {
                    if (item.getTen().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            danhSach.clear();
            danhSach.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class BaiTapViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtCalo, txtThoigian;
        ImageButton btnEdit, btnDelete;

        public BaiTapViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtCalo = itemView.findViewById(R.id.txtCalo);
            txtThoigian = itemView.findViewById(R.id.txtThoigian);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    int pos = getBindingAdapterPosition();
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
