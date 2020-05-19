package com.example.caffeine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShopRecyclerAdapter2 extends RecyclerView.Adapter<ShopRecyclerAdapter2.ViewHolder> {
    private String[] data1;
    private String[] data2;
    private int[] images;
    private Context context;

    public ShopRecyclerAdapter2(String[] data1, String[] data2, int[] images, Context context) {
        this.data1 = data1;
        this.data2 = data2;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopRecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shop_row2, parent, false);

        return new ShopRecyclerAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopRecyclerAdapter2.ViewHolder holder, final int position) {
        holder.header.setText(data1[position]);
        holder.price.setText(data2[position]);
        holder.img.setImageResource(images[position]);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeRecyclerClickActivity.class);
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                intent.putExtra("img", images[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header,price;
        ImageView img;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tvProductName);
            price = itemView.findViewById(R.id.tvPrice);
            img = itemView.findViewById(R.id.ivImage);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
