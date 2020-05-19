package com.example.caffeine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeRecyclerClickActivity extends AppCompatActivity {

    private View decorView;

    TextView tvTitle, tvDescription,tvDescription2;
    UserDatabaseClass db;

    ImageView mainImageView;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homerecycler_click);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        mainImageView = findViewById(R.id.mainImageView);
        db = new UserDatabaseClass(this);

        tvTitle = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.text_status);
        tvDescription2 = findViewById(R.id.tvBeanType);

        getData();
        setData();
    }


    private void getData(){
        if(getIntent().hasExtra("img")){
            img = getIntent().getIntExtra("img",1);
        }
        else if(getIntent().hasExtra("email")){
            String mail = getIntent().getStringExtra("email");
            UserClass user = db.getUser(mail, this);
            tvTitle.setText(user.getName());
            tvDescription.setText(mail);
           // tvDescription2.setText(user.getPhone());
            mainImageView.setImageResource(R.drawable.round_person_white_48dp);
        }
        else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        mainImageView.setImageResource(img);
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
        //Intent back = new Intent (RecyclerClickActivity.this, HomeActivity.class);
        finish();
    }
}
