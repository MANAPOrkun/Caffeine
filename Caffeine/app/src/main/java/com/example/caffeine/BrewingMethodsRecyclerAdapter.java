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

public class BrewingMethodsRecyclerAdapter extends RecyclerView.Adapter<BrewingMethodsRecyclerAdapter.ViewHolder> {

    private String[] data1;
    private String[] data2;
    private int[] images;
    private Context context;


    public BrewingMethodsRecyclerAdapter(String[] data1, String[] data2, int[] images, Context context) {
        this.data1 = data1;
        this.data2 = data2;
        this.images = images;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.brewing_row, parent, false);

        return new BrewingMethodsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.header.setText(data1[position]);
        //holder.description.setText(data2[position]);
        holder.img.setImageResource(images[position]);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrewingRecyclerClick.class);
                intent.putExtra("data1", data1[position]);
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
        TextView header, description;
        ImageView img;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tvProductName);
            description = itemView.findViewById(R.id.text_status);
            img = itemView.findViewById(R.id.ivImage);
            ivImage = itemView.findViewById(R.id.ivImage);

        }
    }
}
