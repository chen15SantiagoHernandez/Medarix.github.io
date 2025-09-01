package com.example.medarix.listaAjuste;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.adapters.RecordatorioAdapter;
import com.example.medarix.models.Recordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordatoriosActivity extends AppCompatActivity implements RecordatorioAdapter.Callback {

    private final List<Recordatorio> lista = new ArrayList<>();
    private RecordatorioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        ReminderReceiver.ensureChannel(this);

        // Cargar recordatorios guardados
        cargarRecordatorios();

        RecyclerView rv = findViewById(R.id.recyclerRecordatorios);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordatorioAdapter(lista, this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> mostrarTimePicker());
    }

    private void mostrarTimePicker() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            // Abrir ajustes para que el usuario te dé el permiso
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return; // esperar que el usuario active el permiso
        }

        Calendar now = Calendar.getInstance();
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            Recordatorio r = new Recordatorio("Recordatorio", hourOfDay, minute, true);
            lista.add(r);
            adapter.notifyItemInserted(lista.size() - 1);
            ReminderScheduler.programar(RecordatoriosActivity.this, r);
            guardarRecordatorios();
            Toast.makeText(RecordatoriosActivity.this, "Programado a las " + r.horaFormateada(), Toast.LENGTH_SHORT).show();
        }, h, m, true).show();
    }

    @Override
    public void onActivoChanged(Recordatorio r, boolean activo) {
        r.activo = activo;
        if (activo) {
            ReminderScheduler.programar(RecordatoriosActivity.this, r);
            Toast.makeText(this, "Activado", Toast.LENGTH_SHORT).show();
        } else {
            ReminderScheduler.cancelar(RecordatoriosActivity.this, r);
            Toast.makeText(this, "Desactivado", Toast.LENGTH_SHORT).show();
        }
        guardarRecordatorios();
    }

    @Override
    public void onEliminar(Recordatorio r, int position) {
        ReminderScheduler.cancelar(RecordatoriosActivity.this, r);
        int idx = position >= 0 ? position : lista.indexOf(r);
        if (idx >= 0) {
            lista.remove(idx);
            adapter.notifyItemRemoved(idx);
            guardarRecordatorios();
        }
    }

    @Override
    public void onClickItem(Recordatorio r, int position) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, r.hora);
        cal.set(Calendar.MINUTE, r.minuto);

        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // Cancelamos el recordatorio anterior
            ReminderScheduler.cancelar(RecordatoriosActivity.this, r);

            // Actualizamos el recordatorio con la nueva hora
            r.hora = hourOfDay;
            r.minuto = minute;

            // Volvemos a programarlo
            ReminderScheduler.programar(RecordatoriosActivity.this, r);

            adapter.notifyItemChanged(position);
            guardarRecordatorios();
            Toast.makeText(RecordatoriosActivity.this, "Reprogramado a las " + r.horaFormateada(), Toast.LENGTH_SHORT).show();
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    // Guardar recordatorios en SharedPreferences
    private void guardarRecordatorios() {
        SharedPreferences prefs = getSharedPreferences("MisRecordatorios", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(lista);

        editor.putString("recordatorios", json);
        editor.apply();
    }

    // Cargar recordatorios desde SharedPreferences
    private void cargarRecordatorios() {
        SharedPreferences prefs = getSharedPreferences("MisRecordatorios", MODE_PRIVATE);
        String saved = prefs.getString("recordatorios", "");
        if (!saved.isEmpty()) {
            Type type = new TypeToken<List<Recordatorio>>() {}.getType();
            lista.clear();
            lista.addAll(new Gson().fromJson(saved, type));

            // Volver a programar todas las alarmas guardadas
            for (Recordatorio r : lista) {
                if (r.activo) {
                    ReminderScheduler.programar(this, r);
                }
            }
        }
    }
}
