package com.example.medarix.listaAjuste;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.adapters.NotificacionesAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesActivity extends AppCompatActivity {
    private List<String> listaNotificaciones = new ArrayList<>();
    private NotificacionesAdapter adapter;
    private Handler handler = new Handler();
    private int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        RecyclerView recyclerView = findViewById(R.id.recyclerNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificacionesAdapter(listaNotificaciones);
        recyclerView.setAdapter(adapter);

        // Agregar algunas de inicio
        agregarNotificacion("Recordatorio: cita médica mañana a las 10:00");
        agregarNotificacion("Nuevo mensaje del doctor");

        // Simular notificaciones automáticas cada 5 segundos
        handler.postDelayed(agregarNotificacionRunnable, 5000);
    }

    private void agregarNotificacion(String mensaje) {
        listaNotificaciones.add(mensaje);
        adapter.notifyItemInserted(listaNotificaciones.size() - 1);
    }

    // Runnable que se repite solo
    private Runnable agregarNotificacionRunnable = new Runnable() {
        @Override
        public void run() {
            agregarNotificacion("Notificación automática #" + contador++);
            handler.postDelayed(this, 5000); // se repite cada 5 segundos
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(agregarNotificacionRunnable); // evitar fugas de memoria
    }
}