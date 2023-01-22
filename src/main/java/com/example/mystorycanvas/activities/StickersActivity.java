package com.example.mystorycanvas.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorycanvas.R;

public class StickersActivity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers);
        RecyclerView stickersRecycler = findViewById(R.id.recycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        stickersRecycler.setLayoutManager(gridLayoutManager);
        StickerAdapter adapter=new StickerAdapter();
        stickersRecycler.setAdapter(adapter);
    }
}
