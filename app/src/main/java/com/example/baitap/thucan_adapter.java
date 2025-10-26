package com.example.baitap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baitap.R;
import com.example.baitap.model_FoodLog;

import java.util.List;

public class thucan_adapter extends BaseAdapter {
    private Context context;
    private List<model_FoodLog> list;

    public thucan_adapter(Context context, List<model_FoodLog> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) { return list.get(position); }

    @Override
    public long getItemId(int position) { return list.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.thucan_item, parent, false);
            h = new ViewHolder();
            h.tvTen = convertView.findViewById(R.id.tvTenMon);
            h.tvCalo = convertView.findViewById(R.id.tvCalo);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        model_FoodLog f = list.get(position);
        h.tvTen.setText(f.getTenMon());
        h.tvCalo.setText(f.getCalo() + " kcal");
        return convertView;
    }

    static class ViewHolder {
        TextView tvTen, tvCalo;
    }

    // helper for activity to update data
    public void updateList(List<model_FoodLog> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
}


