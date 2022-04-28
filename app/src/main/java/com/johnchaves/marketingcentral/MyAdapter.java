package com.johnchaves.marketingcentral;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.BitSet;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<String> productos;
    private List<Integer> fotos;

    public MyAdapter(Context context, List<String> productos, List<Integer> fotos){
        this.context = context;
        this.productos = productos;
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_item, parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mTextView.setText(productos.get(position));
        holder.mImageView.setImageResource(fotos.get(position));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;

        public MyViewHolder(@NonNull View itemView){

            super(itemView);

            mImageView = itemView.findViewById(R.id.imgView);
            mTextView = itemView.findViewById(R.id.txtView);

        }
    }
}
