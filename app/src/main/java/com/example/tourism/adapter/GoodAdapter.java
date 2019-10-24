package com.example.tourism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourism.R;
import com.example.tourism.application.InitApp;
import com.example.tourism.utils.AppUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder> {
    private List<String> picList;
    private Context context;
    private LayoutInflater inflate;

    public GoodAdapter(Context context) {
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.image_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pic = picList.get(position);
        ImageLoader.getInstance().displayImage(pic, holder.imageView, InitApp.getOptions());
        holder.imageView.setOnClickListener(v -> AppUtils.getToast("你点击了第" + position + "张图片"));
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}