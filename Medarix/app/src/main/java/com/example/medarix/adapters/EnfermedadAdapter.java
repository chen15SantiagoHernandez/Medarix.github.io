package com.example.medarix.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.models.Enfermedad;
import com.example.medarix.screens.EnfermedadDatos;
import com.example.medarix.R;

import java.util.List;

public class EnfermedadAdapter extends RecyclerView.Adapter<EnfermedadAdapter.ViewHolder> {
    boolean vistaPrevia;
    private List<Enfermedad> lista;
    private Context context;

    public EnfermedadAdapter(List<Enfermedad> lista, Context context, boolean vistaPrevia) {
        this.context = context;
        this.lista = lista;
        this.vistaPrevia = vistaPrevia;
    }

    @NonNull
    @Override
    public EnfermedadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enfermedad, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EnfermedadAdapter.ViewHolder holder, int position) {
        Enfermedad e = lista.get(position);
        holder.nombre.setText(e.nombre);
        holder.descripcion.setText(e.descripcion);

        if (!vistaPrevia) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EnfermedadDatos.class);
                intent.putExtra("nombre", e.getNombre());
                intent.putExtra("descripcion", e.getDescripcion());
                intent.putExtra("sintomas", e.getSintomas());
                intent.putExtra("tratamientos", e.getTratamientos());
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombre);
            descripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }
}

