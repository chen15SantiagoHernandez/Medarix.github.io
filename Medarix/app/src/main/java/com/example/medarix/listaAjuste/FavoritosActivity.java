package com.example.medarix.listaAjuste;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.adapters.FavoritosAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        RecyclerView recyclerView = findViewById(R.id.recyclerFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("Favoritos", MODE_PRIVATE);
        Set<String> favoritos = prefs.getStringSet("lista", new HashSet<>());

        List<String> listaFavoritos = new ArrayList<>(favoritos);

        FavoritosAdapter adapter = new FavoritosAdapter(listaFavoritos);
        recyclerView.setAdapter(adapter);
    }
}