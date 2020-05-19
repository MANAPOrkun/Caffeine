package com.example.caffeine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class beanDetailActivity extends AppCompatActivity {

    private View decorView;

    TextView name, origin,bean_type,price;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_detail);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        name = findViewById(R.id.tvName);
        origin = findViewById(R.id.text_status);
        bean_type = findViewById(R.id.tvBeanType);
        price = findViewById(R.id.tvPrice);
        imageView = findViewById(R.id.mainImageView);

        name.setText(getIntent().getStringExtra("name"));
        origin.setText(getIntent().getStringExtra("origin"));
        bean_type.setText(getIntent().getStringExtra("bean_type"));
        price.setText(getIntent().getStringExtra("price"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("image"))
                .into(imageView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void backClick(View view) {
        finish();
    }
}
