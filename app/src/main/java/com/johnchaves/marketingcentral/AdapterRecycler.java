package com.johnchaves.marketingcentral;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    private Context context;
    private List<String> productos;
    private List<Integer> fotos;

    public AdapterRecycler(Context context, List<String> productos, List<Integer> fotos){
        this.context = context;
        this.productos = productos;
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_item, parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTextView.setText(productos.get(position));
        holder.mImageView.setImageResource(fotos.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;

        public ViewHolder (@NonNull View itemView){

            super(itemView);

            mImageView = itemView.findViewById(R.id.imgView);
            mTextView = itemView.findViewById(R.id.txtView);

        }
    }
}
