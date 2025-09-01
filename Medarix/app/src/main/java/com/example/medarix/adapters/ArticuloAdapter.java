package com.example.medarix.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder> {
    private List<String> listaArticulos;
    private Context context;

    public ArticuloAdapter(List<String> listaArticulos, Context context) {
        this.listaArticulos = listaArticulos;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        String articulo = listaArticulos.get(position);
        holder.txtNombre.setText(articulo);

        holder.btnFavorito.setOnClickListener(v -> {
            guardarFavorito(articulo);
            Toast.makeText(context, articulo + " agregado a favoritos", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    static class ArticuloViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        ImageButton btnFavorito;

        ArticuloViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreArticulo);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }

    private void guardarFavorito(String item) {
        SharedPreferences prefs = context.getSharedPreferences("Favoritos", Context.MODE_PRIVATE);
        Set<String> favoritos = prefs.getStringSet("lista", new HashSet<>());

        Set<String> nuevosFavoritos = new HashSet<>(favoritos);
        nuevosFavoritos.add(item);

        prefs.edit().putStringSet("lista", nuevosFavoritos).apply();
    }
}
