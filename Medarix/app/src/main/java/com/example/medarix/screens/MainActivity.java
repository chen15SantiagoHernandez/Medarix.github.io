package com.example.medarix.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;

public class MainActivity extends AppCompatActivity {

    Intent intencion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establece el layout
        setContentView(R.layout.activity_main);

        intencion = new Intent(this, Logueo.class);
        startActivity(intencion);
    }
}
