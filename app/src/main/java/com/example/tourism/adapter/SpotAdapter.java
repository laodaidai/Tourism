package com.example.tourism.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourism.R;
import com.example.tourism.application.InitApp;
import com.example.tourism.common.RequestURL;
import com.example.tourism.entity.ScenicSpot;
import com.example.tourism.ui.activity.TourismDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    private List<ScenicSpot> scenicSpots;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SpotAdapter(Context context,List<ScenicSpot> scenicSpots) {
        this.context = context;
        this.scenicSpots = scenicSpots;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_adapter,parent,false);
        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
        ScenicSpot scenicSpot = scenicSpots.get(position);
        holder.spotTitle.setText(scenicSpot.getScenicSpotTheme());
        ImageLoader.getInstance().displayImage(RequestURL.ip_images+scenicSpot.getScenicSpotPicUrl(),holder.spotPic, InitApp.getOptions());
        holder.spotStart.setText("出发:"+scenicSpot.getStartLand());
        holder.spotEnd.setText("终点:"+scenicSpot.getEndLand());
        holder.spotPrice.setText(scenicSpot.getScenicSpotPrice()+"起/人");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClick(position);
                }
                Intent intent = new Intent(context, TourismDetailsActivity.class);
                intent.putExtra("id", scenicSpot.getScenicSpotId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scenicSpots.size();
    }

    static class SpotViewHolder extends RecyclerView.ViewHolder {
         ImageView spotPic;
         TextView spotTitle;
         TextView spotStart;
         TextView spotEnd;
         TextView spotPrice;

        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            spotPic = (ImageView) itemView.findViewById(R.id.spot_pic);
            spotTitle = (TextView) itemView.findViewById(R.id.spot_title);
            spotStart = (TextView) itemView.findViewById(R.id.spot_start);
            spotEnd = (TextView) itemView.findViewById(R.id.spot_end);
            spotPrice = (TextView) itemView.findViewById(R.id.spot_price);
        }
    }

}
