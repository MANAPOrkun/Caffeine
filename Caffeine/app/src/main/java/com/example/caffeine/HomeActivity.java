package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private View decorView;

    ImageView circle1,circle2,circle3,circle4,circle5;

    RecyclerView home_recycler;

    String [] home_functions;
    String []home_descriptions;
    int [] home_imgs = {R.drawable.brewingmethods, R.drawable.shop2, R.drawable.allaboutcoffee,
            R.drawable.coffeeplaces, R.drawable.timer};

    ImageButton btn_profile;

    boolean logged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });


        btn_profile = findViewById(R.id.ib_back);

        home_recycler = findViewById(R.id.home_recycler);

        home_functions = getResources().getStringArray(R.array.home_functions);
        home_descriptions = getResources().getStringArray(R.array.description_home_functions);


        HomeRecyclerAdapter homeRecyclerAdapter = new HomeRecyclerAdapter(home_imgs, HomeActivity.this);
        home_recycler.setAdapter(homeRecyclerAdapter);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        home_recycler.setLayoutManager(layoutManager);

        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);
        circle4 = findViewById(R.id.circle4);
        circle5 = findViewById(R.id.circle5);


       home_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);
               int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
               switch (firstVisiblePosition){
                   case 0:
                       circle1.setBackgroundResource(R.drawable.selected);
                       circle2.setBackgroundResource(R.drawable.not_selected);
                       circle3.setBackgroundResource(R.drawable.not_selected);
                       circle4.setBackgroundResource(R.drawable.not_selected);
                       circle5.setBackgroundResource(R.drawable.not_selected);

                      break;
                   case 1:
                       circle1.setBackgroundResource(R.drawable.not_selected);
                       circle2.setBackgroundResource(R.drawable.selected);
                       circle3.setBackgroundResource(R.drawable.not_selected);
                       circle4.setBackgroundResource(R.drawable.not_selected);
                       circle5.setBackgroundResource(R.drawable.not_selected);
                       break;
                   case 2:
                       circle1.setBackgroundResource(R.drawable.not_selected);
                       circle2.setBackgroundResource(R.drawable.not_selected);
                       circle3.setBackgroundResource(R.drawable.selected);
                       circle4.setBackgroundResource(R.drawable.not_selected);
                       circle5.setBackgroundResource(R.drawable.not_selected);
                       break;
                   case 3:
                       circle1.setBackgroundResource(R.drawable.not_selected);
                       circle2.setBackgroundResource(R.drawable.not_selected);
                       circle3.setBackgroundResource(R.drawable.not_selected);
                       circle4.setBackgroundResource(R.drawable.selected);
                       circle5.setBackgroundResource(R.drawable.not_selected);
                       break;
                   case 4:
                       circle1.setBackgroundResource(R.drawable.not_selected);
                       circle2.setBackgroundResource(R.drawable.not_selected);
                       circle3.setBackgroundResource(R.drawable.not_selected);
                       circle4.setBackgroundResource(R.drawable.not_selected);
                       circle5.setBackgroundResource(R.drawable.selected);
                       break;
                   default:
                       break;
               }

           }
       });

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
        if(!logged){
            Intent login = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(login);
        }
        else{
            Intent profile =
                    new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profile);
        }
    }
}

