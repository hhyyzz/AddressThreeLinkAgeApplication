package com.example.jingzhao.addressthreelinkageapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jingzhao on 2017/9/15.
 */
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceAdapterHolder> {
    OnItemClickListener listener;
    List<Cityinfo> province_list = new ArrayList<Cityinfo>();

    public ProvinceAdapter(List<Cityinfo> province_list) {
        this.province_list = province_list;
        notifyDataSetChanged();
    }

    @Override
    public ProvinceAdapter.ProvinceAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_province_list, parent, false);
        return new ProvinceAdapter.ProvinceAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProvinceAdapter.ProvinceAdapterHolder holder, final int position) {

        holder.tv_province_name.setText(province_list.get(position).getCity_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return province_list == null ? 0 : province_list.size();
    }


    public class ProvinceAdapterHolder extends RecyclerView.ViewHolder {

        TextView tv_province_name;

        public ProvinceAdapterHolder(View itemView) {
            super(itemView);
            tv_province_name = (TextView) itemView.findViewById(R.id.tv_province_name);
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
