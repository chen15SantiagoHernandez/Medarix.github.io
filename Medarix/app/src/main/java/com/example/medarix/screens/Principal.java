package com.example.medarix.screens;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;
import com.example.medarix.fragments.FragmentoAjustes;
import com.example.medarix.fragments.FragmentoComunidad;
import com.example.medarix.fragments.FragmentoEnfermedades;
import com.example.medarix.fragments.FragmentoInicial;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Principal extends AppCompatActivity {

    String nombre;
    String id;
    BottomNavigationView menu;
    boolean vistaPrevia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        nombre = getIntent().getExtras().getString("nombre");
        id = getIntent().getExtras().getString("id");
        vistaPrevia = getIntent().getExtras().getBoolean("vistaPrevia");
        menu = findViewById(R.id.menu);

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.menu_inicial) {
                    FragmentoInicial fragment = FragmentoInicial.newInstance(nombre, vistaPrevia, id);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                } else if (itemId == R.id.menu_enfermedades) {
                    FragmentoEnfermedades fragment = FragmentoEnfermedades.newInstance(vistaPrevia);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                } else if (itemId == R.id.menu_comunidades) {
                    FragmentoComunidad fragment = FragmentoComunidad.newInstance(id, vistaPrevia);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }  else if (itemId == R.id.menu_perfil) {
                    FragmentoAjustes fragment = FragmentoAjustes.newInstance(nombre, vistaPrevia);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            // Cargar el fragmento inicial solo una vez
            FragmentoInicial fragment = FragmentoInicial.newInstance(nombre,vistaPrevia, id);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}