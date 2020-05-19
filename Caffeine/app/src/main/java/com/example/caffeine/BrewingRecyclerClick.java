package com.example.caffeine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BrewingRecyclerClick extends AppCompatActivity {

    private View decorView;

    RecyclerView method_recycler;
    TextView name,method_name;
    ImageView img;
    int img_numb;
    String [] steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewing_recycler_click);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        method_recycler = findViewById(R.id.method_recycler);
        name = findViewById(R.id.tvName);
        img = findViewById(R.id.ivImage);
        method_name = findViewById(R.id.tvMethodName);

        getData();
        switch (name.getText().toString()){
            case "Hario V60":
                steps = getResources().getStringArray(R.array.v60_method);
                break;
            case "Chemex":
                steps = getResources().getStringArray(R.array.chemex_method);
            case "Kalita Wave":
                steps = getResources().getStringArray(R.array.kalita_method);
            case "French Press":
                steps = getResources().getStringArray(R.array.french_method);
            case "Aero Press":
                steps = getResources().getStringArray(R.array.aero_method);
            case "Moka Pot":
                steps = getResources().getStringArray(R.array.moka_method);
            case "Siphon":
                steps = getResources().getStringArray(R.array.siphon_method);
            default:
                break;
        }


        BrewingRecyclerClickAdapter brewingRecyclerClickAdapter =
                new BrewingRecyclerClickAdapter(steps, BrewingRecyclerClick.this);
        method_recycler.setAdapter(brewingRecyclerClickAdapter);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        method_recycler.setLayoutManager(layoutManager);
    }

    public void onBackClick(View view) {
        finish();
    }

    private void getData(){
        if(getIntent().hasExtra("img")){
            img_numb = getIntent().getIntExtra("img",1);
            img.setImageResource(img_numb);
        }
        if(getIntent().hasExtra("data1")){
            name.setText(getIntent().getStringExtra("data1"));
            method_name.setText(name.getText().toString());
        }
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
}
