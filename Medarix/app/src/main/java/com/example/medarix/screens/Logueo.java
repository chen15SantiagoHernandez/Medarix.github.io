// Logueo.java
package com.example.medarix.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;

public class Logueo extends AppCompatActivity {

    Intent intencion;
    Button loguear, registrarse, vistaPrevia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo);

        // Referencias a los elementos
        loguear = findViewById(R.id.boton_logueo);
        registrarse = findViewById(R.id.boton_registrarse);
        vistaPrevia = findViewById(R.id.boton_vista_previa);

    }

    public void Login(View vista)
    {
        intencion = new Intent(this, Login.class);
        startActivity(intencion);
    }

    public void Registrarse(View vista)
    {
        intencion = new Intent(this, Registro.class);
        startActivity(intencion);
    }

    public void VistaPrevia(View vista)
    {
        intencion = new Intent(this, Principal.class);
        intencion.putExtra("nombre", "visitante");
        intencion.putExtra("vistaPrevia", true);
        startActivity(intencion);
    }
}