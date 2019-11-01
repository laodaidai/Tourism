package com.example.tourism.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourism.R;
import com.example.tourism.entity.MonthDayBean;
import com.example.tourism.entity.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    //攻略用户日期详情数据集
    private List<String> stringList;
    //景区详情日期数据集
    private List<MonthDayBean> monthDayBeanList;
    //订单信息数据集
    private List<Order> orderList;
    private Context context;
    private int type;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }

    //设置攻略详情数据
    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    //设置景区详情日期数据集
    public void setMonthDayBeanList(List<MonthDayBean> monthDayBeanList) {
        this.monthDayBeanList = monthDayBeanList;
    }

    //设置订单信息数据集
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == 0) {
            View view = inflater.inflate(R.layout.strategy_details_item_layout, parent, false);
            view.setOnClickListener(this::onClick);
            return new SViewHolder(view);
        } else if (type == 1) {
            View view = inflater.inflate(R.layout.tourism_details_date_item_layout, parent, false);
            view.setOnClickListener(this::onClick);
            return new DViewHolder(view);
        } else if (type == 2) {
            View view = inflater.inflate(R.layout.item_all_order_layout, parent, false);
            view.setOnClickListener(this::onClick);
            return new AllOrderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SViewHolder) {
            String str = stringList.get(position);
            holder.itemView.setTag(str);
            //显示数据
            ((SViewHolder) holder).tvData.setText(str);
            if (position == 0) {
                ((SViewHolder) holder).ivPic.setImageResource(R.mipmap.icon_date_black);
                ((SViewHolder) holder).tvName.setText("出发日期");
            } else if (position == 1) {
                ((SViewHolder) holder).ivPic.setImageResource(R.mipmap.icon_time_black);
                //显示数据
                ((SViewHolder) holder).tvData.setText(str + "天");
                ((SViewHolder) holder).tvName.setText("出行天数");
            } else if (position == 2) {
                ((SViewHolder) holder).ivPic.setImageResource(R.mipmap.icon_wallet_black);
                //显示数据
                ((SViewHolder) holder).tvData.setText(str + "元");
                ((SViewHolder) holder).tvName.setText("人均");
            } else if (position == 3) {
                ((SViewHolder) holder).ivPic.setImageResource(R.mipmap.icon_character_black);
                ((SViewHolder) holder).tvName.setText("人物");
            } else if (position == 4) {
                ((SViewHolder) holder).ivPic.setImageResource(R.mipmap.icon_hot_ari_black);
                ((SViewHolder) holder).tvName.setText("玩法");
            } else {
                ((SViewHolder) holder).ivPic.setVisibility(View.INVISIBLE);
                ((SViewHolder) holder).tvName.setText("");
            }
        }
        if (holder instanceof DViewHolder) {
            MonthDayBean monthDayBean = monthDayBeanList.get(position);
            //设置Tag以便响应适配器监听点击获取相应数据
            holder.itemView.setTag(monthDayBean);
            ((DViewHolder) holder).tvDateWeek.setText(monthDayBean.getMonth());
            ((DViewHolder) holder).tvPrice.setText("¥" + monthDayBean.getPrice());
            if (position == 0) {
                ((DViewHolder) holder).llDetailsDate.setBackgroundResource(R.drawable.state_orange_selected);
                ((DViewHolder) holder).tvDateWeek.setTextColor(context.getResources().getColor(R.color.color_white));
                ((DViewHolder) holder).tvPrice.setTextColor(context.getResources().getColor(R.color.color_white));
            }
            ((DViewHolder) holder).llDetailsDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DViewHolder) holder).llDetailsDate.setBackgroundResource(R.drawable.state_orange_selected);
                    ((DViewHolder) holder).tvDateWeek.setTextColor(context.getResources().getColor(R.color.color_white));
                    ((DViewHolder) holder).tvPrice.setTextColor(context.getResources().getColor(R.color.color_white));
                }
            });

        }
        if (holder instanceof AllOrderViewHolder) {
            Order order = orderList.get(position);
            holder.itemView.setTag(order);
            ((AllOrderViewHolder) holder).tvTips.setVisibility(View.GONE);
            ((AllOrderViewHolder) holder).tvOrderContent.setText(order.getOrderContent());
            if (order.getOrderState() == 0) {
                ((AllOrderViewHolder) holder).tvOrderState.setText("订单取消");
            } else if (order.getOrderState() == 1) {
                ((AllOrderViewHolder) holder).tvOrderState.setText("待付款");
            } else if (order.getOrderState() == 2) {
                ((AllOrderViewHolder) holder).tvOrderState.setText("待消费");
            } else if (order.getOrderState() == 3) {
                ((AllOrderViewHolder) holder).tvOrderState.setText("待评价");
            } else if (order.getOrderState() == 4) {
                ((AllOrderViewHolder) holder).tvOrderState.setText("退款中");
            }
            ((AllOrderViewHolder) holder).tvDate.setText("出发日期 " + order.getDepartDate());
            ((AllOrderViewHolder) holder).tvTripDay.setText("行程天数 " + order.getDepartDays());
            ((AllOrderViewHolder) holder).tvTripInformtion.setText("出行信息 " + order.getTirpInformation());
            ((AllOrderViewHolder) holder).tvPrice.setText("¥" + order.getOrderPrice());
            Log.d("onBindViewHolder", "onBindViewHolder: " + getItemCount());
            if (position == getItemCount() - 1) {
                ((AllOrderViewHolder) holder).tvTips.setVisibility(View.VISIBLE);
                ((AllOrderViewHolder) holder).tvTips.setText("没有更多了...");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            return stringList == null ? 0 : stringList.size();
        }
        if (type == 1) {
            return monthDayBeanList == null ? 0 : monthDayBeanList.size();
        } if (type == 2) {
            return orderList == null ? 0 : orderList.size();
        }
        return 0;
    }

    class SViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_name)
        TextView tvName;

        public SViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date_week)
        TextView tvDateWeek;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.ll_details_date)
        LinearLayout llDetailsDate;

        public DViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AllOrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_content)
        TextView tvOrderContent;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_trip_day)
        TextView tvTripDay;
        @BindView(R.id.tv_trip_informtion)
        TextView tvTripInformtion;
        @BindView(R.id.iv_forward)
        ImageView ivForward;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_tips)
        TextView tvTips;


        public AllOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 重写onClick
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Item 添加类OnItemClickListener 时间监听方法
     */
    public interface OnItemClickListener {
        /**
         * 当内部的Item发生点击的时候 调用Item点击回调方法
         *
         * @param view   点击的View
         * @param object 回调的数据
         */
        void onItemClick(View view, Object object);
    }

}
