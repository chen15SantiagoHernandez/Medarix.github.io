package com.example.medarix.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;
import com.example.medarix.data.DB;

import java.util.List;

public class Registro extends AppCompatActivity {

    EditText nombre, correo, contrasena, confirmar_contrasena;
    Button btnRegistrar;
    DB bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        // Referencias a los elementos de la interfaz
        nombre = findViewById(R.id.nombre);
        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.password);
        confirmar_contrasena = findViewById(R.id.confirmar_password);
        btnRegistrar = findViewById(R.id.botonRegistrarse);

        // Instancia de la base de datos
        bd = new DB();
    }

    public void RegistrarUsuario(View vista)
    {
        if (contrasena.getText().toString().equals(confirmar_contrasena.getText().toString())) {
            bd.comprobarUsuario(correo.getText().toString(), new DB.OnQueryResultListener() {
                @Override
                public void onSuccess(Object result) {
                    try {
                        List<String> lista = (List<String>) result; // Cast
                        if (lista.isEmpty()) {
                            bd.insertarUsuario(nombre.getText().toString(), correo.getText().toString(), contrasena.getText().toString(), "paciente", "", new DB.OnQueryResultListener() {
                                @Override
                                public void onSuccess(Object result) {
                                    try {
                                        int filas = (int) result;
                                        if (filas > 0) {
                                            Toast.makeText(Registro.this, "Te has registrado correctamente.", Toast.LENGTH_LONG);
                                            startActivity(new Intent(Registro.this, Logueo.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(Registro.this, "No te has registrado correctamente.", Toast.LENGTH_SHORT);
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
                        } else {
                            Toast.makeText(Registro.this, "Ya hay una cuenta con ese correo.", Toast.LENGTH_LONG).show();
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
        else {
            Toast.makeText(this, "No coinciden las contraseñas.", Toast.LENGTH_LONG).show();
        }
    }
}