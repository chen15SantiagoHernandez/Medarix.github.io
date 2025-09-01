package com.example.medarix.screens;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;

public class EnfermedadDatos extends AppCompatActivity {

    TextView nombre, descripcion, sintomas, tratamientos;

    // Siempre es necesario al sobrescribir onCreate --> savedInstanceState
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enfermedad_datos);

        nombre = findViewById(R.id.nombre_enfermedad);
        descripcion = findViewById(R.id.descripcion_enfermedad);
        sintomas = findViewById(R.id.sintomas_enfermedad);
        tratamientos = findViewById(R.id.tratamientos_enfermedad);

        nombre.setText(getIntent().getExtras().getString("nombre"));
        descripcion.setText(getIntent().getExtras().getString("descripcion"));
        sintomas.setText(getIntent().getExtras().getString("sintomas"));
        tratamientos.setText(getIntent().getExtras().getString("tratamientos"));

    }
}