package com.johnchaves.marketingcentral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;

public class Productos extends Activity {

    private RecyclerView mRecyclerView;
    private List<String> productos;
    private List<Integer> fotos;
    private AdapterRecycler adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);

        mRecyclerView = findViewById(R.id.recyclerView);


        productos = new ArrayList<>();
        fotos = new ArrayList<>();

        adapter = new AdapterRecycler(this, productos, fotos);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(adapter);

    }
}
