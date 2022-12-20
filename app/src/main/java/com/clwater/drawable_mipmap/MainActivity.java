package com.clwater.drawable_mipmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.clwater.drawable_mipmap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Animation animation;
    Long createTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    public class ViewModel{
        public String width;
        public String height;
        public String density;
        public String densityDpi;

        public void onClick(){
            binding.ivMain.setImageResource(R.mipmap.image_base);
//            binding.ivMain.startAnimation(animation);
        }

    }


    private void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;
        int densityDpi = metric.densityDpi;

        Log.d("clwater.drawable_mipmap", width + " , " + height + " , " + densityDpi);

        ViewModel viewModel = new ViewModel();
        viewModel.width = "宽度(width): " + width;
        viewModel.height = "高度(height): " + height;
        viewModel.density = "密度(density): " + density;
        viewModel.densityDpi = "dpi(Dpi): " + densityDpi;

        binding.setViewModel(viewModel);
        binding.executePendingBindings();

        binding.ivMain.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                try {
                    Log.d("drawable_mipmap_time", "" + (System.currentTimeMillis() - createTime));
                    return true;
                } finally {
                    // Remove listener as further notifications are not needed
                    binding.ivMain.getViewTreeObserver().removeOnPreDrawListener(this);
                }
            }
        });


//        animation = new ScaleAnimation(0, 5.0f, 0f, 5.0f);
//        animation.setDuration(5000);
//        animation.setRepeatCount(0);
//        animation.setFillAfter(true);


    }
}