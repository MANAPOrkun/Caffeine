package com.example.caffeine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class BrewingMethodsActivity extends AppCompatActivity {

    private View decorView;

    RecyclerView brewing_recycler;

    String [] brewing_functions;
    String [] brewing_descriptions;
    int [] brewing_imgs = {R.drawable.chemex, R.drawable.v60, R.drawable.kalita,
            R.drawable.french, R.drawable.aero,R.drawable.moka,R.drawable.siphon, R.drawable.custom};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewingmethods);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        brewing_recycler = findViewById(R.id.method_recycler);

        brewing_functions = getResources().getStringArray(R.array.brewing_methods);
        brewing_descriptions = getResources().getStringArray(R.array.brewing_methods);

        BrewingMethodsRecyclerAdapter brewingRecyclerAdapter =
                new BrewingMethodsRecyclerAdapter(brewing_functions, brewing_descriptions, brewing_imgs,
                        BrewingMethodsActivity.this);
        brewing_recycler.setAdapter(brewingRecyclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        /*
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        */
        brewing_recycler.setLayoutManager(gridLayoutManager);



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

    public void onClick(View view) {
        finish();
    }
}
