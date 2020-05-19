package com.example.caffeine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrewingRecyclerClickAdapter extends RecyclerView.Adapter<BrewingRecyclerClickAdapter.ViewHolder> {


    public BrewingRecyclerClickAdapter(String[] data1, Context context) {
        this.data1 = data1;
        this.context = context;
    }

    private String[] data1;
    private Context context;

    @NonNull
    @Override
    public BrewingRecyclerClickAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.brew_click_row, parent, false);

        return new BrewingRecyclerClickAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrewingRecyclerClickAdapter.ViewHolder holder, int position) {
        holder.step.setText(data1[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView step;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.tvStep);

        }
    }
}
