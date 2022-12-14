package com.clwater.drawable_mipmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.clwater.drawable_mipmap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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

    }

    private void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        int width = metric.widthPixels; // 宽度（PX）
        int height = metric.heightPixels; // 高度（PX）
        float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;

        Log.d("gzb", width + " , " + height + " , " + densityDpi);

        ViewModel viewModel = new ViewModel();
        viewModel.width = "宽度(width): " + width;
        viewModel.height = "高度(height): " + height;
        viewModel.density = "密度(density): " + density;
        viewModel.densityDpi = "dpi(Dpi): " + densityDpi;

        binding.setViewModel(viewModel);
        binding.executePendingBindings();
    }
}