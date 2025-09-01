package com.example.medarix.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medarix.R;
import com.example.medarix.data.DB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Login extends AppCompatActivity {

    Intent intencion;
    EditText correo, contrasena;
    Button btnLogin;
    DB bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        correo = findViewById(R.id.correo_login);
        contrasena = findViewById(R.id.password);
        btnLogin = findViewById(R.id.boton_login);

        // Instancia de la base de datos
        bd = new DB();
    }

    public void Loguearse(View vista)
    {
        bd.obtenerUsuario(correo.getText().toString(), contrasena.getText().toString(), new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<String> lista = (List<String>) result; // Cast
                    if (lista.isEmpty() || lista.get(0).trim().isEmpty()) {
                        Toast.makeText(Login.this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String nombre = lista.get(0);
                        String id = lista.get(1);
                        Toast.makeText(Login.this, "Te has logueado correctamente.", Toast.LENGTH_SHORT).show();

                        // Guardamos la cuenta
                        guardarCuenta(correo.getText().toString());

                        intencion = new Intent(Login.this, Principal.class);
                        intencion.putExtra("nombre", nombre);
                        intencion.putExtra("id", id);
                        intencion.putExtra("vistaPrevia", false);
                        startActivity(intencion);

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

    // Se utiliza SharedPreferences  para guardar dados con la clave "cuentas"
    // Si ya existen cuentas guardadas, se conservan y se agrega la nueva
    // Si SharedPreferences  es "cuentaActiva" se puede usar para mostrar información específica del usuario
    private void guardarCuenta(String email) {
        SharedPreferences prefs = getSharedPreferences("MisCuentas", MODE_PRIVATE);

        // Usa la lista SET porque el usuario inicia sesión varias veces con el mismo email, Set automáticamente evita que se guarde repetido
        Set<String> cuentasGuardadas = prefs.getStringSet("cuentas", null);
        Set<String> cuentas = new HashSet<>();
        if (cuentasGuardadas != null) cuentas.addAll(cuentasGuardadas);

        cuentas.add(email);
        // Crea un editor de SharedPreferences el nombre de la casilla donde guardas tus emails y valor cuentas de emails que quieres almacenar
        prefs.edit().putStringSet("cuentas", cuentas).apply(); // guarda todas las cuentas
        prefs.edit().putString("cuentaActiva", email).apply(); // guarda la cuenta activa
    }
}