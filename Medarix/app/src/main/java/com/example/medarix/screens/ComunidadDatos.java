package com.example.medarix.screens;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.data.DB;
import com.example.medarix.R;
import com.example.medarix.adapters.ComentarioAdapter;
import com.example.medarix.models.Comentario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ComunidadDatos extends AppCompatActivity {

    int id_enfermedad;
    String id;
    RecyclerView recycler;
    ComentarioAdapter adapter;
    List<Comentario> lista = new ArrayList<>();
    TextView titulo;
    TextInputLayout texto_comentario;
    DB bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comunidad_datos);

        id_enfermedad = getIntent().getExtras().getInt("id_enfermedad");
        titulo = findViewById(R.id.titulo_comunidad);
        titulo.setText(getIntent().getExtras().getString("nombre"));
        id = getIntent().getExtras().getString("id");

        recycler = findViewById(R.id.listaComentarios);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        texto_comentario = findViewById(R.id.textoComentario);

        texto_comentario.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                GuardarComentario(v);
                return true; // Indica que el evento se ha manejado
            }
            return false;
        });

        bd = new DB();

        CargarComentarios(String.valueOf(id_enfermedad));
    }

    public void CargarComentarios(String query) {
        bd.obtenerComentarios(query, new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    lista = (List<Comentario>) result; // Cast
                    adapter = new ComentarioAdapter(ComunidadDatos.this, lista);
                    recycler.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void GuardarComentario(View vista)
    {
        bd.insertarComentario(id, String.valueOf(id_enfermedad), texto_comentario.getEditText().getText().toString(), new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    int filas = (int) result; // Cast
                    if (filas > 0){
                        texto_comentario.getEditText().setText("");
                        CargarComentarios(String.valueOf(id_enfermedad));
                    }
                    else{
                        Toast.makeText(ComunidadDatos.this, "No se ha podido mandar el mensaje", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}