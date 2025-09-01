package com.example.medarix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.models.Comentario;
import com.example.medarix.R;

import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder>{

    private List<Comentario> lista;
    private Context context;


    public ComentarioAdapter(Context context, List<Comentario> lista ) {
        this.context = context;
        this.lista = lista;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, texto, fecha_comentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreComentario);
            texto = itemView.findViewById(R.id.textComentario);
            fecha_comentario = itemView.findViewById(R.id.fecha_comentario);
        }
    }

    @NonNull
    @Override
    public ComentarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioAdapter.ViewHolder holder, int position) {
        Comentario c = lista.get(position);
        holder.nombre.setText(c.nombre);
        holder.texto.setText(c.texto);
        holder.fecha_comentario.setText(c.fecha_comentario);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
