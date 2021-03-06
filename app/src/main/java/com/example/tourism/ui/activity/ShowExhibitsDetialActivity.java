package com.example.tourism.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.example.tourism.R;
import com.example.tourism.adapter.ExhibitsCommentItemsAdapter;
import com.example.tourism.application.RetrofitManger;
import com.example.tourism.application.ServerApi;
import com.example.tourism.common.RequestURL;
import com.example.tourism.entity.Exhibits;
import com.example.tourism.entity.ExhibitsComment;
import com.example.tourism.entity.FabulousDetails;
import com.example.tourism.utils.AppUtils;
import com.example.tourism.utils.StatusBarUtil;
import com.example.tourism.widget.GlideImageLoader;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowExhibitsDetialActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.danmakuView)
    DanmakuView danmakuView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.exhibits_name)
    TextView exhibitsName;
    @BindView(R.id.exhibition_area_name)
    TextView exhibitionAreaName;
    @BindView(R.id.guidance_teacher)
    TextView guidanceTeacher;
    @BindView(R.id.team_members)
    TextView teamMembers;
    @BindView(R.id.cell_phone_number)
    TextView cellPhoneNumber;
    @BindView(R.id.exhibits_author)
    TextView exhibitsAuthor;
    @BindView(R.id.exhibits_information)
    TextView exhibitsInformation;
    @BindView(R.id.comment_count)
    TextView commentCount;
    @BindView(R.id.call_up)
    ImageView callUp;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.listView)
    ListView listView;

    private int exhibitsId;
    private Unbinder unbinder;
    private Exhibits exhibit;
    private List<String> images = new ArrayList<>();
    private List<ExhibitsComment> exhibitsComments = new ArrayList<>();
    private boolean showDanmaku;
    private DanmakuContext danmakuContext;
    private ExhibitsCommentItemsAdapter ecitemadapter;
    private int defaultFlag,likeFlag = 0;

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exhibits_detial);
        unbinder = ButterKnife.bind(this,this);
        exhibitsId = (int) getIntent().getExtras().get("exhibitsId");
        queryExhibitsDetails(exhibitsId);
        queryExhibitsComment(exhibitsId);
        queryFabulousDetailsFlag(exhibitsId);
        initToolBar();
        initBanner();
        initVideoView();
        sendComments();
        callUp();
        floatingActionButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initToolBar(){
        StatusBarUtil.setTransparentForWindow(this);
        //扩张时候的title颜色
        collapsingToolbarLayout.setExpandedTitleColor(getColor(R.color.color_transparent));
        //收缩后在Toolbar上显示时的title的颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.color_white));
        toolbar.setTitle(getString(R.string.exhibits_detial));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initBanner(){
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        images.add(RequestURL.ip_images+"/images/banner/banner1.jpg");
        images.add(RequestURL.ip_images+"/images/banner/banner2.jpg");
        images.add(RequestURL.ip_images+"/images/banner/banner3.jpg");
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(position -> {
            Intent intent = new Intent(ShowExhibitsDetialActivity.this, BigImageActivity.class);
            intent.putStringArrayListExtra("imgData", (ArrayList<String>) images);
            intent.putExtra("clickPosition", position);
            startActivity(intent);
        });
    }

    private void initVideoView(){
        //设置有进度条可以拖动快进
        //MediaController localMediaController = new MediaController(this);
        //videoView.setMediaController(localMediaController);
        String url = RequestURL.ip_video+"/test.mp4";
        videoView.setVideoPath(url);
        videoView.start();

        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }

    private void initListView(){
        ecitemadapter = new ExhibitsCommentItemsAdapter(ShowExhibitsDetialActivity.this,exhibitsComments);
        listView.setAdapter(ecitemadapter);
        listView.setDividerHeight(0);
        ecitemadapter.setListViewHeightBasedOnChildren(listView);
        commentCount.setText(getString(R.string.comment_count)+"("+ecitemadapter.getCount()+")");
    }

    private void callUp(){
        callUp.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+cellPhoneNumber.getText()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void floatingActionButton(){
        floatingActionButton.setOnClickListener(view -> {
            if (defaultFlag == 0){
                defaultFlag = 1;
                floatingActionButton.setImageResource(R.drawable.ic_favorite);
                AppUtils.getToast("点赞成功！");
            }else {
                defaultFlag = 0;
                floatingActionButton.setImageResource(R.drawable.ic_unfavorite);
                AppUtils.getToast("点赞取消！");
            }
        });
    }

    private void queryExhibitsDetails(int exhibitsId){
        ServerApi api = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        HashMap hashMap = new HashMap();
        hashMap.put("exhibitsId",exhibitsId);
        Call<ResponseBody> exhibitsCall = api.getASync("queryExhibitsDetails",hashMap);
        exhibitsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String mag = response.body().string();
                    JSONObject json = new JSONObject(mag);
                    Exhibits exhibit = RetrofitManger.getInstance().getGson().fromJson(json.getString("ONE_DETAIL"),
                            new TypeToken<Exhibits>(){}.getType());
                    if (exhibit == null) return;
                    exhibitsName.setText(exhibit.getExhibitsName());
                    guidanceTeacher.setText(exhibit.getGuidanceTeacher());
                    exhibitsAuthor.setText(exhibit.getExhibitsAuthor());
                    teamMembers.setText(exhibit.getTeamMembers());
                    cellPhoneNumber.setText(exhibit.getCellPhoneNumber());
                    exhibitsInformation.setText(exhibit.getExhibitsInformation());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("@@@@",t.getMessage());
            }
        });
    }

    private void queryExhibitsComment(int exhibitsId){
        ServerApi serverApi = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        HashMap hashMap = new HashMap();
        hashMap.put("exhibitsId",exhibitsId);
        Call<ResponseBody> exhibitsCommentsCall = serverApi.getASync("queryExhibitsComment",hashMap);
        exhibitsCommentsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject(data);
                    exhibitsComments = RetrofitManger.getInstance().getGson().fromJson(jsonObject.getString("ONE_DETAIL"),
                            new TypeToken<List<ExhibitsComment>>(){}.getType());
                    if (exhibitsComments == null) return;
                    initListView();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("!!!",t.getMessage());
            }
        });
    }

    private void setFabulousDetailsFlag(int exhibitsId){
        ServerApi serverApi = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        HashMap hashMap = new HashMap();
        hashMap.put("userId", RequestURL.vUserId);
        hashMap.put("exhibitsId",exhibitsId);
        Call<ResponseBody> exhibitsCommentsCall = serverApi.getASync("setFabulousDetailsFlag",hashMap);
        exhibitsCommentsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject(data);
                    FabulousDetails fabulousDetails = RetrofitManger.getInstance().getGson().fromJson(
                            jsonObject.getString("ONE_DETAIL"), new TypeToken<FabulousDetails>(){}.getType());
                    likeFlag = fabulousDetails.getFlag();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("!!!","请求失败！");
                Log.d("!!!",t.getMessage());
            }
        });
    }

    private void queryFabulousDetailsFlag(int exhibitsId){
        ServerApi serverApi = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        HashMap hashMap = new HashMap();
        hashMap.put("userId",RequestURL.vUserId);
        hashMap.put("exhibitsId",exhibitsId);
        Call<ResponseBody> exhibitsCommentsCall = serverApi.getASync("queryFabulousDetailsFlag",hashMap);
        exhibitsCommentsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject(data);
                    defaultFlag = jsonObject.getInt("ONE_DETAIL");
                    likeFlag = jsonObject.getInt("ONE_DETAIL");
                    Log.d("@@@", "likeFlag: "+likeFlag);
                    if (defaultFlag == 0) {
                        floatingActionButton.setImageResource(R.drawable.ic_unfavorite);
                    }else {
                        floatingActionButton.setImageResource(R.drawable.ic_favorite);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("!!!","请求失败！");
                Log.d("!!!",t.getMessage());
            }
        });
    }

    private void sendExhibitsComment(int userId, int exhibitsId, String comment){
        ServerApi serverApi = RetrofitManger.getInstance().getRetrofit(RequestURL.ip_port).create(ServerApi.class);
        HashMap hashMap = new HashMap();
        hashMap.put("userId", RequestURL.vUserId);
        hashMap.put("exhibitsId",exhibitsId);
        hashMap.put("exhibitsCommentContent",comment);
        Call<ResponseBody> responseBodyCall = serverApi.postASync("sendExhibitsComment",hashMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.getString("RESULT").equals("S")){
                        AppUtils.getToast(jsonObject.getString("TIPS"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("!!!",t.getMessage());
            }
        });
    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(() -> {
            while(showDanmaku) {
                int time = new Random().nextInt(1000);
                //String content = "" + time + time;
                for (int i = 0; i < exhibitsComments.size(); i++) {
                    String content = exhibitsComments.get(i).getExhibitsCommentContent();
                    addDanmaku(content,false);
                }
                //addDanmaku(content, false);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 发送弹幕
     */
    private void sendComments(){
        danmakuView.setOnClickListener(view -> {
            if (linearLayout.getVisibility() == View.GONE) {
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    sendExhibitsComment(1,exhibitsId,content);
                    addDanmaku(content, true);
                    editText.setText("");
                }
            }
        });
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                    onWindowFocusChanged(true);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (likeFlag != defaultFlag){
            setFabulousDetailsFlag(exhibitsId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
        if (likeFlag != defaultFlag){
            setFabulousDetailsFlag(exhibitsId);
        }
    }
}
