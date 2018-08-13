package com.example.administrator.azlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<CityBean> cityBeans = new ArrayList<>();
    private String lastIndex = "";
    private static final int MAIN_ITEM = 0;
    private static final int INDEX_ITEM = 1;

    MainAdapter(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
        }
    }


    @Override
    public int getItemCount() {
        return cityBeans.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_city.setText(cityBeans.get(position).getCity());
    }

/*
    class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_city;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_city;
        private TextView tv_index;

        IndexViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_index = itemView.findViewById(R.id.tv_index);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == MAIN_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            holder = new MainViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_index, parent, false);
            holder = new IndexViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            ((MainViewHolder) holder).tv_city.setText(cityBeans.get(position).getCity());
        } else if (holder instanceof IndexViewHolder) {
            ((IndexViewHolder) holder).tv_index.setText(cityBeans.get(position).getIndex());
            ((IndexViewHolder) holder).tv_city.setText(cityBeans.get(position).getCity());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (cityBeans.get(position).getIndex().isEmpty()) {
            return MAIN_ITEM;
        } else {
            lastIndex = cityBeans.get(position).getIndex();
            return INDEX_ITEM;
        }
    }*/
}
