package com.example.caffeine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private int[] images;
    private Context context;

    HomeRecyclerAdapter(int[] img, Context context) {
        this.images = img;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.img.setImageResource(images[position]);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    Intent intent = new Intent(context, BrewingMethodsActivity.class);
                    context.startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(context, ShopActivity.class);
                    context.startActivity(intent);
                }
                else if(position == 3){
                    Intent intent = new Intent(context, CoffeePlacesActivity.class);
                    context.startActivity(intent);
                }
                else if(position == 4){
                    Intent intent = new Intent(context, TimerActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, HomeRecyclerClickActivity.class);
                    intent.putExtra("img", images[position]);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivImage);
            ivImage = itemView.findViewById(R.id.ivImage);
            
        }
    }
}
