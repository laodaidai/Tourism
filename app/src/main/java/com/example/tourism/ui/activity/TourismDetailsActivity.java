package com.example.tourism.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.tourism.R;
import com.example.tourism.adapter.SelectionAdapter;
import com.example.tourism.adapter.VPagerFragmentAdapter;
import com.example.tourism.application.InitApp;
import com.example.tourism.application.RetrofitManger;
import com.example.tourism.application.ServerApi;
import com.example.tourism.common.DefineView;
import com.example.tourism.common.RequestURL;
import com.example.tourism.entity.MonthDayBean;
import com.example.tourism.entity.ScenicDetails;
import com.example.tourism.entity.ScenicPic;
import com.example.tourism.entity.ScenicSpot;
import com.example.tourism.ui.fragment.details.EvaluateFragment;
import com.example.tourism.ui.fragment.details.PictureFragment;
import com.example.tourism.ui.fragment.details.TourismRealFragment;
import com.example.tourism.utils.AppUtils;
import com.example.tourism.utils.StatusBarUtil;
import com.example.tourism.widget.ChildAutoViewPager;
import com.example.tourism.widget.ViewBundle;
import com.google.gson.reflect.TypeToken;
import com.hz.android.easyadapter.EasyAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品详情类
 * Name:laodai
 * Time:2019.10.23
 */
public class TourismDetailsActivity extends AppCompatActivity implements DefineView {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ty_smbol)
    TextView tySmbol;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ty_everyone)
    TextView tyEveryone;
    @BindView(R.id.fl_red_envelopes)
    FrameLayout flRedEnvelopes;
    @BindView(R.id.fl_shop_red_envelopes)
    FrameLayout flShopRedEnvelopes;
    @BindView(R.id.fl_explain)
    FrameLayout flExplain;
    @BindView(R.id.rv_date)
    RecyclerView rvDate;
    @BindView(R.id.tv_more_dates)
    TextView tvMoreDates;
    @BindView(R.id.tv_traffic)
    TextView tvTraffic;
    @BindView(R.id.tv_trip)
    TextView tvTrip;
    @BindView(R.id.tv_arrive)
    TextView tvArrive;
    @BindView(R.id.tv_stay)
    TextView tvStay;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.buttom_child_viewPager)
    ChildAutoViewPager buttomChildViewPager;
    @BindView(R.id.tv_info_imagetext)
    TextView tvInfoImagetext;
    @BindView(R.id.tv_info_product)
    TextView tvInfoProduct;
    @BindView(R.id.tv_info_evaluate)
    TextView tvInfoEvaluate;
    @BindView(R.id.iv_corsor)
    ImageView ivCorsor;
    @BindView(R.id.layout_classify)
    LinearLayout layoutClassify;
    @BindView(R.id.nsv_scrollview)
    NestedScrollView nsvScrollview;
    @BindView(R.id.status_view)
    View statusView;
    @BindView(R.id.imageView_one)
    ImageView imageViewOne;
    @BindView(R.id.imageView_two)
    ImageView imageViewTwo;
    @BindView(R.id.imageView_three)
    ImageView imageViewThree;
    @BindView(R.id.back_left_image)
    ImageView backLeftImage;
    @BindView(R.id.details_title_text)
    TextView detailsTitleText;
    @BindView(R.id.shopping_chart_image)
    ImageView shoppingChartImage;
    @BindView(R.id.more_image)
    ImageView moreImage;
    @BindView(R.id.details_toolbar)
    ConstraintLayout detailsToolbar;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.customer_service_line)
    LinearLayout customerServiceLine;
    @BindView(R.id.collection_image)
    ImageView collectionImage;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.btn_shapping_chart)
    Button btnShappingChart;
    @BindView(R.id.btn_reserve)
    Button btnReserve;
    @BindView(R.id.ll_buttom)
    LinearLayout llButtom;
    @BindView(R.id.iv_back_top)
    ImageView ivBackTop;
    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.tv_loading_content)
    TextView tvLoadingContent;
    @BindView(R.id.loading_line)
    ConstraintLayout loadingLine;
    @BindView(R.id.tv_empty_content)
    TextView tvEmptyContent;
    @BindView(R.id.empty_line)
    LinearLayout emptyLine;
    @BindView(R.id.tv_erro_content)
    TextView tvErroContent;
    @BindView(R.id.error_line)
    LinearLayout errorLine;
    @BindView(R.id.ll_head_information)
    LinearLayout llHeadInformation;
    //保存顶部状态栏的高度
    private int statusHeight;
    //保存顶部标题栏的高度
    private int toolbarHeight;
    //保存轮播图的高度
    private int bHeight;
    //保存筛选栏的高度
    private int classifyHeight;
    // bmpw: 表示游标的宽度，mCurrentIndex: 表示当前所在的页面
    private int bmpw = 0;
    private int mCurrentIndex = 0;
    private int fixLeftMargin;
    private LinearLayout.LayoutParams params;
    //底部ViewPager设置
    private List<Fragment> mDatas;
    private VPagerFragmentAdapter bAdapter;
    private PictureFragment pictureFragment;
    private TourismRealFragment tourismRealFragment;
    private EvaluateFragment evaluateFragment;
    // 记录底部ViewPager距离顶部的高度
    private int vpagerTopDistance;
    //请求API
    private ServerApi api;
    private ScenicSpot scenicSpot;
    private ScenicDetails scenicDetails;
    private List<ScenicPic> scenicPicList;
    //日期适配器
    private SelectionAdapter rvAdapter;
    private List<MonthDayBean> monthDayBeanList = new ArrayList<>();
    //收藏、取消判断
    private boolean flag = false;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourism_details_layout);
        ButterKnife.bind(this);
        initView();
        initValidata();
        initListener();
        initImg();
    }

    @Override
    public void initView() {

    }

    @SuppressLint("NewApi")
    @Override
    public void initValidata() {
        //设置中间内容隐藏
        nsvScrollview.setVisibility(View.GONE);
        llToolbar.setVisibility(View.GONE);
        llButtom.setVisibility(View.GONE);
        ivBackTop.setVisibility(View.GONE);
        loadingLine.setVisibility(View.VISIBLE);
        emptyLine.setVisibility(View.GONE);
        errorLine.setVisibility(View.GONE);

        //设置状态栏透明
        StatusBarUtil.setTransparentForWindow(this);
        //获取状态栏高度
        statusHeight = AppUtils.getStatusBarHeight(this);
        //设置状态栏高度
        AppUtils.setStatusBarColor(statusView, statusHeight, R.color.color_blue);
        //设置透明度为0
        statusView.getBackground().mutate().setAlpha(0);
        detailsToolbar.getBackground().mutate().setAlpha(0);

        //设置标题栏隐藏
        detailsTitleText.setVisibility(TextView.GONE);
        imageViewOne.setBackgroundResource(R.drawable.select_bar_translucent);
        imageViewTwo.setBackgroundResource(R.drawable.select_bar_translucent);
        imageViewThree.setBackgroundResource(R.drawable.select_bar_translucent);

        //设置RecyclerView
        //设置线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置Data横向显示
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //将布局管理设置到控件中
        rvDate.setLayoutManager(layoutManager);
        //创建适配器对象

        //获取轮播图的高度
        //获取顶部标题栏的高度
        detailsToolbar.post(() -> toolbarHeight = detailsToolbar.getHeight() + statusHeight);
        toolbarHeight = 89;
        bHeight = 500;
        //浮动栏初始化时隐藏
        layoutClassify.setVisibility(View.INVISIBLE);
        //获取浮动栏控件的高度
        layoutClassify.post(() -> classifyHeight = layoutClassify.getHeight());
        //底部ViewPager
        if (mDatas == null) mDatas = new ArrayList<>();
        pictureFragment = PictureFragment.newInstance(new ViewBundle(buttomChildViewPager));
        tourismRealFragment = TourismRealFragment.newInstance(new ViewBundle(buttomChildViewPager));
        evaluateFragment = EvaluateFragment.newInstance(new ViewBundle(buttomChildViewPager));
        mDatas.add(pictureFragment);
        mDatas.add(tourismRealFragment);
        mDatas.add(evaluateFragment);
        //设置ViewPager适配器
        bAdapter = new VPagerFragmentAdapter(getSupportFragmentManager(), mDatas);
        buttomChildViewPager.setAdapter(bAdapter);
        //缓存
        buttomChildViewPager.setOffscreenPageLimit(mDatas.size());
        //侦听器
        buttomChildViewPager.addOnPageChangeListener(new ButtomPageChangeListener());
        //设置ViewPager
        //设置滚动监听
        nsvScrollview.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            //设置status，toobar颜色透明渐变
            float detalis = scrollY > bHeight ? bHeight : (scrollY > 30 ? scrollY : 0);
            int alpha = (int) (detalis / bHeight * 255);
            AppUtils.setUpdateActionBar(statusView, detailsToolbar, alpha);
            if (alpha < 200) {
                imageViewOne.setBackgroundResource(R.drawable.select_bar_translucent);
                imageViewTwo.setBackgroundResource(R.drawable.select_bar_translucent);
                imageViewThree.setBackgroundResource(R.drawable.select_bar_translucent);
            } else {
                imageViewOne.setBackgroundResource(R.drawable.select_bar_transparent);
                imageViewTwo.setBackgroundResource(R.drawable.select_bar_transparent);
                imageViewThree.setBackgroundResource(R.drawable.select_bar_transparent);
            }
            //获取高度
            vpagerTopDistance = buttomChildViewPager.getTop() - classifyHeight - statusView.getHeight()
                    - detailsToolbar.getHeight();

            //设置浮动栏距离顶部的高度
            //设置浮动栏
            int translation = Math.max(scrollY, vpagerTopDistance);
            layoutClassify.setTranslationY(translation);
            layoutClassify.setVisibility(View.VISIBLE);
            Log.d(InitApp.TAG, "vpagerTopDistance: " + vpagerTopDistance);
            Log.d(InitApp.TAG, "translation: " + translation);
            //设置返回顶部
            if (scrollY >= vpagerTopDistance) {
                ivBackTop.setVisibility(View.VISIBLE);
            } else {
                ivBackTop.setVisibility(View.GONE);
            }
        });

        //获取传入的景区编号
        int scenicSpotId = this.getIntent().getIntExtra("scenicSpotId", 0);
        //获取景区详情数据
        api = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        Map<String, Object> map = new HashMap<>();
        map.put("scenicSpotId", scenicSpotId);
        Call<ResponseBody> dataCall = api.getASync("queryByScenicDetailsAndPic", map);
        dataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String message = response.body().string();
                    JSONObject json = new JSONObject(message);
                    if (json.getString(RequestURL.RESULT).equals("S")) {
                        scenicSpot = RetrofitManger.getInstance().getGson().fromJson(json.getString(RequestURL.ONE_DATA), ScenicSpot.class);
                        scenicDetails = RetrofitManger.getInstance().getGson().fromJson(json.getString(RequestURL.TWO_DATA), ScenicDetails.class);
                        scenicPicList = RetrofitManger.getInstance().getGson().fromJson(json.getString(RequestURL.THREE_DATA),
                                new TypeToken<List<ScenicPic>>() {
                                }.getType());
                        if (scenicSpot != null && scenicDetails != null && scenicPicList != null) {
                            bindData();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //设置线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //横向显示
        manager.setOrientation(RecyclerView.HORIZONTAL);
        //设置管理器
        rvDate.setLayoutManager(manager);
        //创新适配器对象，并指定子布局
        rvAdapter = new SelectionAdapter(this, 0);
        //设置点击模式
        rvAdapter.setSelectMode(EasyAdapter.SelectMode.CLICK);
        //设置单选模式
        rvAdapter.setSelectMode(EasyAdapter.SelectMode.SINGLE_SELECT);
    }

    @Override
    public void initListener() {
        ivLeftBack.setOnClickListener(v -> finish());
        //监听单选
        rvAdapter.setOnItemSingleSelectListener(new EasyAdapter.OnItemSingleSelectListener() {
            @Override
            public void onSelected(int itemPosition, boolean isSelected) {
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindData() {
        //设置中间内容隐藏
        nsvScrollview.setVisibility(View.VISIBLE);
        llToolbar.setVisibility(View.VISIBLE);
        llButtom.setVisibility(View.VISIBLE);
        ivBackTop.setVisibility(View.GONE);
        loadingLine.setVisibility(View.GONE);
        emptyLine.setVisibility(View.GONE);
        errorLine.setVisibility(View.GONE);

        //banner图片轮播
        List<String> list_path = new ArrayList<>();
        List<String> list_title = new ArrayList<>();
        //添加数据
        for (ScenicPic scenicPic : scenicPicList) {
            list_path.add(RequestURL.ip_images + scenicPic.getScenicPicUrl());
        }
        list_title.add("产品编号 3524985628");
        list_title.add("产品编号 3524985629");
        list_title.add("产品编号 3524985630");
        list_title.add("产品编号 3524985631");
        list_title.add("产品编号 3524985632");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //必须最后调用的方法，启动轮播图。
                .start();
        banner.setOnBannerListener(position -> {
            Intent intent = new Intent(TourismDetailsActivity.this, BigImageActivity.class);
            intent.putStringArrayListExtra("imgData", (ArrayList<String>) list_path);
            intent.putExtra("clickPosition", position);
            startActivity(intent);
        });
        //显示详情页数据
        tvContent.setText(scenicSpot.getScenicSpotTheme());
        tvPrice.setText(scenicSpot.getScenicSpotPrice() + "");
        tvTraffic.setText(scenicDetails.getConsultTraffic());
        tvTrip.setText(scenicDetails.getTravelDays());
        tvStay.setText(scenicDetails.getStayStandard());
        tvArrive.setText(scenicDetails.getDepartArrive());
        getMonthDays();
    }

    /**
     * 获取一个月内的日期和时间
     */
    private void getMonthDays() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] array = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        //获取当月天数
        int currentDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //当月剩余天数
        int remainingMonth = currentDays - day;
        //可预订起始时间 当前日期往后延长1天
        int startDays = day + 1;
        //天数
        String mDate = "";
        //集合存储天数
        List<String> dList = new ArrayList<>();
        for (int i = startDays; i <= remainingMonth + day; i++) {
            if (currentDays - day == 1) break;
            calendar.set(Calendar.DAY_OF_MONTH, i);
            int weeks = calendar.get(Calendar.DAY_OF_WEEK);
            if (month < 10 && i < 10) {
                mDate = "0" + month + "/0" + i + " " + array[weeks - 1];
            }
            if (month < 10 && i >= 10) {
                mDate = "0" + month + "/" + i + " " + array[weeks - 1];
            }
            if (month >= 10 && i < 10) {
                mDate = month + "/0" + i + " " + array[weeks - 1];
            }
            if (month >= 10 && i >= 10) {
                mDate = month + "/" + i + " " + array[weeks - 1];
            }
            dList.add(mDate);
        }

        if (day >= 1) {
            //设置月份为下个月，从0开始算起
            calendar.set(Calendar.MONTH, month);
            //获取下个月的月份
            int months = calendar.get(Calendar.MONTH) + 1;
            for (int i = 1; i <= day; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, i);
                int weeks = calendar.get(Calendar.DAY_OF_WEEK);
                if (months < 10 && i < 10) {
                    mDate = "0" + months + "/0" + i + " " + array[weeks - 1];
                }
                if (months < 10 && i >= 10) {
                    mDate = "0" + months + "/" + i + " " + array[weeks - 1];
                }
                if (months >= 10 && i < 10) {
                    mDate = months + "/0" + i + " " + array[weeks - 1];
                }
                if (months >= 10 && i >= 10) {
                    mDate = months + "/" + i + " " + array[weeks - 1];
                }
                dList.add(mDate);
            }
        }

        for (int i = 0; i < dList.size(); i++) {
            MonthDayBean monthDayBean = new MonthDayBean(dList.get(i), String.valueOf(scenicSpot.getScenicSpotPrice()));
            monthDayBeanList.add(monthDayBean);
        }

        if (monthDayBeanList != null) {
            //设置数据
            rvAdapter.setMonthDayBeanList(monthDayBeanList);
            //设置适配器
            rvDate.setAdapter(rvAdapter);
        }
    }

    @OnClick({R.id.back_left_image, R.id.shopping_chart_image, R.id.more_image,
            R.id.tv_info_imagetext, R.id.tv_info_product, R.id.tv_info_evaluate,
            R.id.btn_shapping_chart, R.id.btn_reserve, R.id.iv_back_top,
            R.id.tv_more_dates, R.id.ll_collection, R.id.ll_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_left_image:
                finish();
                break;
            case R.id.shopping_chart_image:
                AppUtils.getToast("加入行程");
                break;
            case R.id.more_image:
                AppUtils.getToast("点击了更多");
                break;
            case R.id.tv_info_imagetext:
                nsvScrollview.fling(0);
                nsvScrollview.smoothScrollTo(0, vpagerTopDistance);
                buttomChildViewPager.setCurrentItem(0);
                break;
            case R.id.tv_info_product:
                nsvScrollview.fling(0);
                nsvScrollview.smoothScrollTo(0, vpagerTopDistance);
                buttomChildViewPager.setCurrentItem(1);
                break;
            case R.id.tv_info_evaluate:
                nsvScrollview.fling(0);
                nsvScrollview.smoothScrollTo(0, vpagerTopDistance);
                buttomChildViewPager.setCurrentItem(2);
                break;
            case R.id.btn_shapping_chart:
                intent = new Intent(TourismDetailsActivity.this, CalendarActivity.class);
                //指定类型，0表示加入行程
                intent.putExtra("type", 0);
                intent.putExtra("scenicSpotId", scenicSpot.getScenicSpotId());
                intent.putExtra("scenicDetailsId", scenicDetails.getScenicDetailsId());
                intent.putExtra("price", scenicSpot.getScenicSpotPrice());
                startActivity(intent);
                break;
            case R.id.btn_reserve:
                intent = new Intent(TourismDetailsActivity.this, CalendarActivity.class);
                //指定类型，1表示立即预定
                intent.putExtra("type", 1);
                intent.putExtra("scenicSpotId", scenicSpot.getScenicSpotId());
                startActivity(intent);
                break;
            case R.id.iv_back_top:
                //返回顶部
                nsvScrollview.fling(0);
                nsvScrollview.smoothScrollTo(0, 0);
                break;
            case R.id.tv_more_dates:
                intent = new Intent(TourismDetailsActivity.this, CalendarActivity.class);
                //指定类型，2表示更多日期
                intent.putExtra("type", 2);
                //跳转传值
                startActivityForResult(intent, 1);
                break;
            case R.id.ll_collection:
                if (flag) {
                    flag = false;
                    collectionImage.setBackgroundResource(R.drawable.ic_collection_gray_24dp);
                } else {
                    api = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", RequestURL.vUserId);
                    map.put("scenicDetailsId", scenicDetails.getScenicSpotId());
                    Call<ResponseBody> call = api.postASync("userAddByCollection", map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String message = response.body().string();
                                JSONObject json = new JSONObject(message);
                                if (json.getString(RequestURL.RESULT).equals("S")) {
                                    flag = true;
                                    AppUtils.getToast("收藏成功");
                                    collectionImage.setBackgroundResource(R.drawable.ic_collection_red_24dp);
                                } else {
                                    AppUtils.getToast(json.getString(RequestURL.TIPS));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.ll_service:
                //客服
                AppUtils.getToast("客服");
                break;
        }
    }

    /**
     * 内部类实现底部ViewPager监听
     */
    private class ButtomPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 滑动的过程
         *
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 滑动过程
            if (mCurrentIndex == 0 && position == 0) {
                params.leftMargin = (int) (mCurrentIndex * bmpw + positionOffset * bmpw)
                        + fixLeftMargin;
            } else if (mCurrentIndex == 1 && position == 0) {
                params.leftMargin = (int) (mCurrentIndex * bmpw + (positionOffset - 1) * bmpw)
                        + fixLeftMargin;
            } else if (mCurrentIndex == 1 && position == 1) {
                params.leftMargin = (int) (mCurrentIndex * bmpw + positionOffset * bmpw)
                        + fixLeftMargin;
            } else if (mCurrentIndex == 2 && position == 1) {
                params.leftMargin = (int) (mCurrentIndex * bmpw + (positionOffset - 1) * bmpw)
                        + fixLeftMargin;
            }
            ivCorsor.setLayoutParams(params);
            //重置当前高度
            buttomChildViewPager.resetHeight(position);
        }

        @Override
        public void onPageSelected(int position) {
            setChangeTv(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 初始化底部指示器imageView
     */
    private void initImg() {
        Display disPlay = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        disPlay.getMetrics(outMetrics);
        int mScreen1_4 = outMetrics.widthPixels / 4;
        bmpw = outMetrics.widthPixels / 3;
        fixLeftMargin = (bmpw - mScreen1_4) / 2;
        ViewGroup.LayoutParams layoutParams = ivCorsor.getLayoutParams();
        layoutParams.width = mScreen1_4;
        ivCorsor.setLayoutParams(layoutParams);
        /**
         * 设置左侧固定距离
         */
        params = (LinearLayout.LayoutParams) ivCorsor.getLayoutParams();
        params.leftMargin = fixLeftMargin;
        ivCorsor.setLayoutParams(layoutParams);
    }

    /**
     * 改变游动条颜色
     *
     * @param position
     */
    private void setChangeTv(int position) {
        tvInfoImagetext.setTextColor(Color.parseColor("#666666"));
        tvInfoProduct.setTextColor(Color.parseColor("#666666"));
        tvInfoEvaluate.setTextColor(Color.parseColor("#666666"));
        switch (position) {
            case 0:
                tvInfoImagetext.setTextColor(Color.parseColor("#FF7198"));
                break;
            case 1:
                tvInfoProduct.setTextColor(Color.parseColor("#FF7198"));
                break;
            case 2:
                tvInfoEvaluate.setTextColor(Color.parseColor("#FF7198"));
                break;
        }
        mCurrentIndex = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 2) {
            String dateStr = data.getStringExtra("dateStr");
            AppUtils.getToast(dateStr);
        }
    }

}
