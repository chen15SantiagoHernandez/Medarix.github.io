package com.example.medarix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {
    private List<String> listaFavoritos;

    public FavoritosAdapter(List<String> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        holder.textView.setText(listaFavoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaFavoritos.size();
    }

    static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        FavoritoViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
