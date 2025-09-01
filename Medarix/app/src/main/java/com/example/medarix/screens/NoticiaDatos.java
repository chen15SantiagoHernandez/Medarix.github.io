package com.example.medarix.screens;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;

public class NoticiaDatos extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView txtTitulo, txtFuente, txtDescripcion, txtFecha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.item_noticia);

        // Vistas del item incluido
        imgLogo        = findViewById(R.id.imgLogo);
        txtTitulo      = findViewById(R.id.txtTitulo);
        txtFuente      = findViewById(R.id.txtFuente);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtFecha       = findViewById(R.id.txtFecha);
        // Leer extras de forma segura
        String titulo  = getIntent().getStringExtra("titulo");
        String fuente  = getIntent().getStringExtra("fuente");
        String desc    = getIntent().getStringExtra("descripcion");
        String fecha   = getIntent().getStringExtra("fecha");
        String idEnf   = getIntent().getStringExtra("id_enfermedad");
        String acces   = getIntent().getStringExtra("accesible_para");
        int logoRes    = getIntent().getIntExtra("logoRes", 0);

        // Pintar
        // "?": Si el valor existe, se muestra.
        //Si es null, se muestra un valor por defecto (en estos casos una cadena vacía).
        txtTitulo.setText(titulo != null ? titulo : "Sin título");
        txtFuente.setText(fuente != null ? fuente : "");
        txtDescripcion.setText(desc != null ? desc : "");
        txtFecha.setText(fecha != null ? fecha : "");
        imgLogo.setImageResource(logoRes != 0 ? logoRes : android.R.drawable.ic_menu_report_image);

    }
}
