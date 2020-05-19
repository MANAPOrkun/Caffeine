package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;


public class ShopActivity extends AppCompatActivity {

    private View decorView;
    private static final String TAG = "ShopActivity";

    RecyclerView shop_recycler, rvPopular;

    ImageView circle1,circle2,circle3,circle4,circle5;

    String [] shop_functions;
    String [] shop_descriptions;
    String[] shop_price;
    int [] shop_img = {R.drawable.starbucks_colombia_narino_250g,
            R.drawable.starbucks_espresso_fairtrade_250g, R.drawable.starbucks_kenya_250g,
            R.drawable.starbucks_guatemala_antigua_250g, R.drawable.lavazza_super_crema_1000g};

    FirestoreRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    Dialog positive_dialog;
    Button positive_button;
    ImageButton positive_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        shop_functions = getResources().getStringArray(R.array.coffee_beans);
        shop_descriptions = getResources().getStringArray(R.array.bean_description);
        shop_price = getResources().getStringArray(R.array.bean_price);
        shop_recycler = findViewById(R.id.shop_recycler);
        rvPopular = findViewById(R.id.rvPopular);

        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);
        circle4 = findViewById(R.id.circle4);
        circle5 = findViewById(R.id.circle5);

        positive_dialog = new Dialog(this);

        createPopularRecycler();
        createFirebaseRecycler();

    }

    public void onClick(View view) {
        finish();
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

    public void addBeanToCartClick(View view) {
        TextView name = findViewById(R.id.tvProductName);
        String name_text = name.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        positive_dialog.setContentView(R.layout.popup_positive);
        positive_close = (ImageButton) positive_dialog.findViewById(R.id.ibGo);
        positive_button = (Button) positive_dialog.findViewById(R.id.buttonCart);

        positive_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positive_dialog.dismiss();

            }
        });

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(ShopActivity.this, CartActivity.class);
                startActivity(cart);
            }
        });

        firebaseFirestore.collection("Beans")
                .whereEqualTo("name", name_text)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                CoffeeBeansClass coffeeBeansClass = document
                                        .toObject(CoffeeBeansClass.class);
                                cartClass cart = new cartClass(
                                        coffeeBeansClass.getName(),
                                        user.getUid(),document.getId(),
                                        Calendar.getInstance().getTime().toString(),
                                        "On Cart",
                                        1,
                                        coffeeBeansClass.getPrice(),
                                        coffeeBeansClass.getImgUrl());
                                addToCart(cart);
                                positive_dialog.getWindow()
                                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                positive_dialog.show();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    class beanViewHolder extends RecyclerView.ViewHolder {

        private TextView list_name;
        private TextView list_price;
        private ImageView list_image;

        public beanViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.tvProductName);
            list_price = itemView.findViewById(R.id.tvPrice);
            list_image = itemView.findViewById(R.id.ivImage);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Beans");

        FirestoreRecyclerOptions<CoffeeBeansClass> options = new FirestoreRecyclerOptions.
                Builder<CoffeeBeansClass>().setQuery(query, CoffeeBeansClass.class).build();

        adapter = new FirestoreRecyclerAdapter<CoffeeBeansClass, beanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull beanViewHolder holder, int position, @NonNull final CoffeeBeansClass model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText(String.valueOf(model.getPrice()));
                Glide.with(getApplicationContext())
                        .load(model.getImgUrl())
                        .into(holder.list_image);
                holder.list_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent beanDetail = new Intent(ShopActivity.this, beanDetailActivity.class);
                        beanDetail.putExtra("name", model.getName());
                        beanDetail.putExtra("price",model.getPrice());
                        beanDetail.putExtra("origin",model.getOrigin());
                        beanDetail.putExtra("bean_type",model.getBean_type());
                        beanDetail.putExtra("image", model.getImgUrl());
                        startActivity(beanDetail);
                    }
                });
            }

            @NonNull
            @Override
            public beanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_row2, parent, false);
                return new beanViewHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

    }

    private void createPopularRecycler(){
        ShopRecyclerAdapter shopRecyclerAdapter = new ShopRecyclerAdapter(shop_functions,
                shop_descriptions, shop_price, shop_img, ShopActivity.this);
        rvPopular.setAdapter(shopRecyclerAdapter);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rvPopular.setLayoutManager(layoutManager);

        rvPopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void createFirebaseRecycler(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Beans");

        FirestoreRecyclerOptions<CoffeeBeansClass> options = new FirestoreRecyclerOptions.
                Builder<CoffeeBeansClass>().setQuery(query, CoffeeBeansClass.class).build();

        adapter = new FirestoreRecyclerAdapter<CoffeeBeansClass, beanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull beanViewHolder holder, int position, @NonNull final CoffeeBeansClass model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText(String.valueOf(model.getPrice()));
                Glide.with(getApplicationContext())
                        .load(model.getImgUrl())
                        .into(holder.list_image);
                holder.list_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent beanDetail = new Intent(ShopActivity.this, beanDetailActivity.class);
                        beanDetail.putExtra("name", model.getName());
                        beanDetail.putExtra("price",model.getPrice());
                        beanDetail.putExtra("origin",model.getOrigin());
                        beanDetail.putExtra("bean_type",model.getBean_type());
                        beanDetail.putExtra("image", model.getImgUrl());
                        startActivity(beanDetail);
                    }
                });
            }

            @NonNull
            @Override
            public beanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_row2, parent, false);
                return new beanViewHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

        shop_recycler.setHasFixedSize(true);
        shop_recycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        shop_recycler.setAdapter(adapter);
    }

    private void addToCart(cartClass cart){
        firebaseFirestore.collection("Cart")
                .add(cart)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}
