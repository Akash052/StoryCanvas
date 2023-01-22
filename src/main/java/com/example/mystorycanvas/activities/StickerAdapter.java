package com.example.mystorycanvas.activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorycanvas.R;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sticker,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sticker.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_stickers_v1));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView sticker;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sticker= itemView.findViewById(R.id.sticker);
        }
    }
}
