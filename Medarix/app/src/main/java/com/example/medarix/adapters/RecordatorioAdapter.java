package com.example.medarix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.models.Recordatorio;

import java.util.List;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.ViewHolder> {

    public interface Callback {
        void onActivoChanged(Recordatorio r, boolean activo);
        void onEliminar(Recordatorio r, int position);
        void onClickItem(Recordatorio r, int position);
    }

    private final List<Recordatorio> lista;
    private final Callback callback;

    public RecordatorioAdapter(List<Recordatorio> lista, Callback callback) {
        this.lista = lista;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recordatorio, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Recordatorio r = lista.get(pos);
        h.tvTitulo.setText(r.titulo);
        h.tvHora.setText(r.horaFormateada());
        h.switchActivo.setChecked(r.activo);

        h.switchActivo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            callback.onActivoChanged(r, isChecked);
        });

        h.btnEliminar.setOnClickListener(v -> {
            callback.onEliminar(r, pos);
        });

        h.itemView.setOnClickListener(v -> {
            callback.onClickItem(r, pos);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvHora;
        Switch switchActivo;
        ImageButton btnEliminar;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvHora = itemView.findViewById(R.id.tvHora);
            switchActivo = itemView.findViewById(R.id.switchActivo);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
