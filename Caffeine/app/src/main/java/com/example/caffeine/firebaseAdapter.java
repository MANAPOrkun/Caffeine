package com.example.caffeine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class firebaseAdapter extends RecyclerView.Adapter<firebaseAdapter.MyViewHolder>{

    Context context;
    List<CoffeeBeansClass> coffeeBeansClassArrayList;

    public firebaseAdapter(Context c , List<CoffeeBeansClass> p)
    {
        context = c;
        coffeeBeansClassArrayList = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_row2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CoffeeBeansClass coffeeBeansClass = coffeeBeansClassArrayList.get(position);
        holder.name.setText(coffeeBeansClassArrayList.get(position).getName());
        holder.price.setText(coffeeBeansClassArrayList.get(position).getPrice());
        Picasso.get().load(coffeeBeansClassArrayList.get(position).getImgUrl()).into(holder.productImg);

    }

    @Override
    public int getItemCount() {
        return coffeeBeansClassArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price;
        ImageView productImg;
        Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvProductName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            productImg = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }
}
