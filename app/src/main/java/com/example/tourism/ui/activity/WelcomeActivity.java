package com.example.tourism.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tourism.MainActivity;
import com.example.tourism.R;
import com.example.tourism.ui.activity.base.BaseActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 欢迎广告类
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.btn_skip)
    Button btnSkip;
    @BindView(R.id.welcome_background)
    ConstraintLayout welcome_background;
    private int time = 3;
    private int[] image = {R.drawable.poster_one, R.drawable.poster_two, R.drawable.poster_three};

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                btnSkip.setText("跳过" + getTime() + "s");
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen(false);
        //创建随机数对象
        Random random = new Random();
        int i = random.nextInt(6);
        if (i < 3) {
            setContentView(R.layout.activity_welcome);
            ButterKnife.bind(this);
//            welcome_background.setSystemUiVisibility(View.INVISIBLE);
            //随机替换广告背景
            welcome_background.setBackgroundResource(image[i]);
            btnSkip.setOnClickListener(view -> { //点击跳过
                openActivity(MainActivity.class);
                handler.removeMessages(0);
                finish();
            });
            handler.sendEmptyMessageDelayed(0, 1000);
        } else {
            openActivity(MainActivity.class);
            finish();
        }
    }

    private int getTime() {
        time--;
        if (time == 0) {
            openActivity(MainActivity.class);
            finish();
        }
        return time;
    }

    private void fullscreen(boolean enable) {
        if (enable) { //显示状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else { //隐藏状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

}
