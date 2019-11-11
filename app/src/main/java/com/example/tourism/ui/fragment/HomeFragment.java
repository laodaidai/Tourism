package com.example.tourism.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourism.R;
import com.example.tourism.adapter.RecyclerViewAdapter;
import com.example.tourism.adapter.ScenicSpotItemAdapter;
import com.example.tourism.adapter.SecondaryMenuItemAdapter;
import com.example.tourism.application.InitApp;
import com.example.tourism.application.RetrofitManger;
import com.example.tourism.application.ServerApi;
import com.example.tourism.common.DefineView;
import com.example.tourism.common.RequestURL;
import com.example.tourism.entity.HotTopicsBean;
import com.example.tourism.entity.ScenicSpot;
import com.example.tourism.entity.SecondaryMenu;
import com.example.tourism.ui.activity.LocationActivity;
import com.example.tourism.ui.activity.NearbyActivity;
import com.example.tourism.ui.activity.RomanticJourneyActivity;
import com.example.tourism.ui.activity.SecondaryActivity;
import com.example.tourism.ui.fragment.base.BaseFragment;
import com.example.tourism.utils.AppUtils;
import com.example.tourism.utils.StatusBarUtil;
import com.example.tourism.widget.GlideImageLoader;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements DefineView {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.showNearby)
    TextView showNearby;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.iv_hot_topics_pic1)
    ImageView ivHotTopicsPic1;
    @BindView(R.id.tv_hot_topics_content1)
    TextView tvHotTopicsContent1;
    @BindView(R.id.iv_hot_topics_pic3)
    ImageView ivHotTopicsPic3;
    @BindView(R.id.tv_hot_topics_content3)
    TextView tvHotTopicsContent3;
    @BindView(R.id.iv_hot_topics_pic2)
    ImageView ivHotTopicsPic2;
    @BindView(R.id.tv_hot_topics_content2)
    TextView tvHotTopicsContent2;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.status_view)
    View statusView;
    @BindView(R.id.tv_diqu)
    TextView tvDiqu;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.ll_state_toolbar)
    LinearLayout llStateToolbar;
    @BindView(R.id.hfragment)
    RelativeLayout hfragment;
    private int statusHeight;

    private List<String> images = new ArrayList<>();
    private List<SecondaryMenu> secondaryMenuList = new ArrayList<>();
    private List<ScenicSpot> allScenicSpots = new ArrayList<>();
    private SecondaryMenuItemAdapter adapter1;
    private ScenicSpotItemAdapter adapter2;
    private Unbinder unbinder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);
        initView();
        initValidata();
        initListener();
        initLocationText();
        return root;
    }

    public void initLocation() {
        tvDiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);


    }

    @Override
    public void initView() {
        //默认初始工具栏为透明
        initRefreshLayout();
        initBanner();
        initSecondaryMenu();
    }

    @SuppressLint("NewApi")
    @Override
    public void initValidata() {
        //设置状态栏透明
        StatusBarUtil.setTransparentForWindow(getActivity());
        //获取状态栏高度
        statusHeight = AppUtils.getStatusBarHeight(getActivity());
        //设置状态栏高度
        AppUtils.setStatusBarColor(statusView, statusHeight, R.color.color_blue);
        //设置透明度为0
        statusView.getBackground().mutate().setAlpha(0);
        llToolbar.getBackground().mutate().setAlpha(0);
        int bHeight = 400;
        //设置滚动监听
        scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            //设置status，toobar颜色透明渐变
            float detalis = scrollY > bHeight ? bHeight : (scrollY > 30 ? scrollY : 0);
            int alpha = (int) (detalis / bHeight * 255);
            AppUtils.setUpdateActionBar(statusView, llToolbar, alpha);
        });
        bindData();
        queryAllScenicSpot();
    }

    @Override
    public void initListener() {
        initLocation();
        linearLayout.setOnClickListener(view -> {
            Toast.makeText(getContext(), "查看附近景点", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), NearbyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void bindData() {
        ImageLoader.getInstance().displayImage(RequestURL.ip_images + "images/deng.jpg", ivHotTopicsPic1, InitApp.getOptions());
        ImageLoader.getInstance().displayImage(RequestURL.ip_images + "images/romantic.jpg", ivHotTopicsPic2, InitApp.getOptions());
        ImageLoader.getInstance().displayImage(RequestURL.ip_images + "images/depth.jpg", ivHotTopicsPic3, InitApp.getOptions());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind(); //解绑
    }

    private void initRefreshLayout() {
        //设置 Header 为 贝塞尔雷达 样式
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(getContext()).setEnableHorizontalDrag(true)
//                .setPrimaryColorId(R.color.mask_tags_8));
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Translate)
                .setAnimatingColor(0xFF1DA8FE));
        refreshLayout.setOnMultiListener(new OnMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                llStateToolbar.setVisibility(View.INVISIBLE);
                Log.d(InitApp.TAG, "offset: " + offset + "headerHeight: " + headerHeight + "maxDragHeight: " + maxDragHeight);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
                llStateToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
                llStateToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                llStateToolbar.setVisibility(View.INVISIBLE);
                Log.d("@@@", "刷新完成！");
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                //add();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                llStateToolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        images.add(RequestURL.ip_images + "/images/banner/banner1.jpg");
        images.add(RequestURL.ip_images + "/images/banner/banner2.jpg");
        images.add(RequestURL.ip_images + "/images/banner/banner3.jpg");
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initSecondaryMenu() {
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_1, getString(R.string.menu_1)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_2, getString(R.string.menu_2)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_3, getString(R.string.menu_3)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_4, getString(R.string.menu_4)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_5, getString(R.string.menu_5)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_6, getString(R.string.menu_6)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_7, getString(R.string.menu_7)));
        secondaryMenuList.add(new SecondaryMenu(R.drawable.menu_8, getString(R.string.menu_8)));
        adapter1 = new SecondaryMenuItemAdapter(getContext(), secondaryMenuList);
        gridView.setAdapter(adapter1);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i == 0) {
                openActivity(RomanticJourneyActivity.class);
            } else {
                Toast.makeText(getContext(), secondaryMenuList.get(i).menu_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), SecondaryActivity.class);
                intent.putExtra("travel_mode", (i + 1));
                intent.putExtra("menu_name", secondaryMenuList.get(i).menu_name);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter2 = new ScenicSpotItemAdapter(getContext(), allScenicSpots);
        recyclerView.setAdapter(adapter2);
    }

    private void queryAllScenicSpot() {
        ServerApi api = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        Call<ResponseBody> scenicSpotCall = api.getNAsync("queryAllScenicSpot");
        scenicSpotCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    allScenicSpots = RetrofitManger.getInstance().getGson().fromJson(response.body().string(),
                            new TypeToken<List<ScenicSpot>>() {
                            }.getType());
                    if (allScenicSpots == null) return;
                    initRecyclerView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("@@@", t.getMessage());
            }
        });

    }

    private void showNearby() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "查看附近景点", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), NearbyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void add() {
        int l = allScenicSpots.size();
        for (int i = 1; i <= 10; i++) {
            //scenicSpots.add(new ScenicSpot(R.drawable.defaultbg,i+l+""));
        }
        adapter2.notifyDataSetChanged();
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || resultCode == 1) {
            String mLocation = data.getStringExtra("location");
            String temp = sharedPreferences.getString("location", "");
            if (!mLocation.equals(temp)) {
                editor.remove("location");
                editor.putString("location", mLocation);
                editor.commit();
            }
            editor.putString("location", mLocation);
            //步骤4：提交
            editor.commit();
        }
    }


    public void initLocationText() {
        tvDiqu.setText(sharedPreferences.getString("location", ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        initLocationText();
    }
}