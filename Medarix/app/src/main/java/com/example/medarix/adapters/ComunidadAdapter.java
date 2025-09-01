package com.example.medarix.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.models.Comunidad;
import com.example.medarix.screens.ComunidadDatos;
import com.example.medarix.R;

import java.util.List;

public class ComunidadAdapter extends RecyclerView.Adapter<ComunidadAdapter.ViewHolder>{

    private List<Comunidad> lista;
    private Context context;
    private String id;
    private boolean vistaPrevia;

    public ComunidadAdapter(Context context, List<Comunidad> lista, String id,boolean vistaPrevia ) {
        this.context = context;
        this.lista = lista;
        this.id = id;
        this.vistaPrevia = vistaPrevia;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, respuestas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_comunidad);
            descripcion = itemView.findViewById(R.id.descripcion_comunidad);
            respuestas = itemView.findViewById(R.id.respuestas_comunidad);
        }
    }

    @NonNull
    @Override
    public ComunidadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comunidad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComunidadAdapter.ViewHolder holder, int position) {
        Comunidad c = lista.get(position);
        holder.nombre.setText(c.nombre);
        holder.descripcion.setText(c.descripcion);
        holder.respuestas.setText(c.respuestas + " respuestas");

        if (!vistaPrevia){
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ComunidadDatos.class);
                intent.putExtra("id_enfermedad", c.getId_enfermedad());
                intent.putExtra("nombre", c.getNombre());
                intent.putExtra("id", id);
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
