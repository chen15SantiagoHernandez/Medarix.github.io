package com.example.medarix.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.models.Noticia;
import com.example.medarix.screens.EnfermedadDatos;

import java.util.ArrayList;
import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.ViewHolder> {

    private List<Noticia> data = new ArrayList<>();
    private final Context context;
    private final boolean vistaPrevia;

    public NoticiaAdapter(List<Noticia> lista, Context context, boolean vistaPrevia) {
        this.context = context;
        this.data = lista != null ? lista : new ArrayList<>();
        this.vistaPrevia = vistaPrevia;
        Log.d("ADAPTER", "Adapter creado con " + data.size() + " elementos");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noticia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Noticia e = data.get(position);
        holder.txtFuente.setText(e.fuente);
        holder.txtTitulo.setText(e.titulo);
        holder.txtDescripcion.setText(e.descripcion);

        holder.txtFecha.setText((e.fecha));

        holder.imgLogo.setImageResource(e.logoRes != 0
                ? e.logoRes
                : android.R.drawable.ic_menu_report_image);


        if (!vistaPrevia) {
            holder.itemView.setOnClickListener(v -> {
                // Recupera la URL
                String url = e.getUrl();
                if (url != null && !url.isEmpty()) {
                    // Crea el Intent para abrir la URL en el navegador
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    // Importante: añade la FLAG si estás fuera de un Activity (por ejemplo en Adapter)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Lanza el Intent
                    v.getContext().startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), "URL no disponible", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Noticia> nuevas) {
        data.clear();
        if (nuevas != null) data.addAll(nuevas);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtFuente, txtTitulo, txtDescripcion, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtFuente = itemView.findViewById(R.id.txtFuente);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
