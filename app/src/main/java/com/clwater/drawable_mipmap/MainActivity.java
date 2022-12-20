package com.clwater.drawable_mipmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.clwater.drawable_mipmap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            binding.ivMain.setImageResource(R.drawable.image_base);
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


//        animation = new ScaleAnimation(0, 5.0f, 0f, 5.0f);
//        animation.setDuration(5000);
//        animation.setRepeatCount(0);
//        animation.setFillAfter(true);


    }
}