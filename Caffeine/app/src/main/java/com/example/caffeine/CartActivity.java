package com.example.caffeine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class CartActivity extends AppCompatActivity {

    private View decorView;
    private static final String TAG = "CartActivity";

    RecyclerView cart_recycler;

    FirestoreRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        mAuth = FirebaseAuth.getInstance();

        cart_recycler = findViewById(R.id.method_recycler);

        createFirebaseRecycler();


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

    public void onBackClick(View view) {
        finish();
    }

    private void createFirebaseRecycler(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        Query query = firebaseFirestore.collection("Cart")
                .whereEqualTo("userUid",user.getUid());

        FirestoreRecyclerOptions<cartClass> options = new FirestoreRecyclerOptions.
                Builder<cartClass>().setQuery(query, cartClass.class).build();

        adapter = new FirestoreRecyclerAdapter<cartClass, CartActivity.cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartActivity.cartViewHolder holder, int position, @NonNull final cartClass model) {
                holder.list_name.setText(model.getProductName());
                holder.list_price.setText(String.valueOf(model.getPrice()));
                holder.list_status.setText(model.getStatus());
                holder.list_piece.setText(String.valueOf(model.getPiece()));
                holder.list_date.setText(model.getDate());
                Glide.with(getApplicationContext())
                        .load(model.getImgUrl())
                        .into(holder.list_image);

            }

            @NonNull
            @Override
            public CartActivity.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
                return new cartViewHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

        cart_recycler.setHasFixedSize(true);
        cart_recycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        cart_recycler.setAdapter(adapter);
    }

    class cartViewHolder extends RecyclerView.ViewHolder {

        private TextView list_name;
        private TextView list_price;
        private TextView list_status;
        private TextView list_piece;
        private TextView list_date;
        private ImageView list_image;


        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.tvProductName);
            list_price = itemView.findViewById(R.id.tvPrice);
            list_status = itemView.findViewById(R.id.tvStatus);
            list_piece = itemView.findViewById(R.id.tvPiece);
            list_date = itemView.findViewById(R.id.tvDate);
            list_image = itemView.findViewById(R.id.ivImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        Query query = firebaseFirestore.collection("Cart")
                .whereEqualTo("userUid",user.getUid());

        FirestoreRecyclerOptions<cartClass> options = new FirestoreRecyclerOptions.
                Builder<cartClass>().setQuery(query, cartClass.class).build();

        adapter = new FirestoreRecyclerAdapter<cartClass, CartActivity.cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartActivity.cartViewHolder holder, int position, @NonNull final cartClass model) {
                holder.list_name.setText(model.getProductName());
                holder.list_price.setText(String.valueOf(model.getPrice()));
                holder.list_status.setText(model.getStatus());
                holder.list_piece.setText(String.valueOf(model.getPiece()));
                holder.list_date.setText(model.getDate());
                Glide.with(getApplicationContext())
                        .load(model.getImgUrl())
                        .into(holder.list_image);

            }

            @NonNull
            @Override
            public CartActivity.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
                return new CartActivity.cartViewHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

    }
}
