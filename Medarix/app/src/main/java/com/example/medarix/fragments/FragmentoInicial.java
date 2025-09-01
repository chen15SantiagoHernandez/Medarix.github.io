package com.example.medarix.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.R;
import com.example.medarix.adapters.NoticiaAdapter;
import com.example.medarix.data.DB;
import com.example.medarix.models.Noticia;
import com.example.medarix.screens.Login;
import com.example.medarix.screens.Logueo;
import com.example.medarix.screens.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentoInicial extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    Intent intencion;
    TextView texto;
    String nombre;
    String id;
    DB bd;
    boolean vistaPrevia;
    RecyclerView recycler;
    NoticiaAdapter adapter;


    public FragmentoInicial() {
        // Required empty public constructor
    }

    public static FragmentoInicial newInstance(String param1, boolean vistaPrevia, String id) {
        FragmentoInicial fragment = new FragmentoInicial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, vistaPrevia);
        args.putString(ARG_PARAM3, id);
        fragment.setArguments(args);
        return fragment;
    }

    // getArguments() devuelve el Bundle que se pasó al fragment
    // Si hay argumentos, se susan para iniciar variables
    //Por el contrario, el fragmento no tiene datos iniciales.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nombre = getArguments().getString(ARG_PARAM1);
            vistaPrevia = getArguments().getBoolean(ARG_PARAM2);
            id = getArguments().getString(ARG_PARAM3);
        }
        else {
            nombre = "desconocido"; // indica que no se conoce el nombre porque no se pasó nada.
            vistaPrevia = true;
            id = "0";
        }
    }

    // Este metodo defines qué se muestra y cómo se ve el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_fragmento_inicial, container, false);

        // El id asociado desde el xml
        ImageView iconoCuenta = view.findViewById(R.id.iconoCuenta);

        // setOnClickListener es para detectar el ratón cuando se le pasa por encima
        // para que mostrase el menú de cambiar la cuenta o cerrar sesión usando: mostrarMenuCuenta
        iconoCuenta.setOnClickListener(v -> {
            mostrarMenuCuenta(v);
        });

        // Instancia de la base de datos
        bd = new DB();

        texto = view.findViewById(R.id.saludo);
        texto.setText("Hi, " + nombre);
        recycler = view.findViewById(R.id.recyclerNoticias);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        CargarNoticias(vistaPrevia);
        return view;
    }

    // Este metodo recibe un parámetro vistaPrevia que indica si se deben mostrar las noticias en modo previo o completo.
    public void CargarNoticias(boolean vistaPrevia) {
        bd.obtenerNoticias(new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<Noticia> lista = (List<Noticia>) result; // Cast
                    adapter = new NoticiaAdapter(lista, requireContext(), vistaPrevia);
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

    // Se crea un metodo privado que muestra un menú anclado a la cuenta.
    // Se agregan dos opciones: "Cambiar cuenta" y "Cerrar sesión".
    // setOnMenuItemClickListener define qué hacer cuando el usuario toca cada opción:
    // usamos if para distinguir entre "Cambiar cuenta" y "Cerrar sesión" y ejecutar la acción correspondiente.
    // Finalmente mostrar el menú.
    private void mostrarMenuCuenta(View anchor) {
        // anchor:es la vista debajo de la cual se mostrará el menú.
        PopupMenu menu = new PopupMenu(requireContext(), anchor);
        // añadiendo dos opciones al menú directamente desde código
        menu.getMenu().add("Cambiar cuenta");
        menu.getMenu().add("Cerrar sesión");

        // setOnMenuItemClickListener: define lo que sucede cuando el usuario pulsa cada opción
        menu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Cambiar cuenta")) {
                if (!vistaPrevia){
                    List<String> cuentas = obtenerCuentasGuardadas(); // metodo que devuelve emails guardados

                    if (cuentas.isEmpty()) {
                        // No hay cuentas guardadas → ir a login
                        Intent intencion = new Intent(requireContext(), Login.class);
                        startActivity(intencion);
                    } else {
                        // Hay cuentas guardadas → mostrar diálogo para seleccionar
                        String[] cuentasArray = cuentas.toArray(new String[0]);
                        // Crea un diálogo emergente (AlertDialog) en la pantalla actual y requireContext para decir en que pantalla debe estar
                        // setItems: crea una lista de opciones clicables dentro del diálogo, una por cada elemento del arreglo.
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Seleccionar cuenta")
                                .setItems(cuentasArray, (dialog, which) -> { // dialog, which:  es el código que se ejecuta cuando el usuario toca una opción
                                    String cuentaSeleccionada = cuentasArray[which];   // obtiene el email que seleccionó el usuario.
                                    iniciarSesionConCuenta(cuentaSeleccionada); // llama al metodo que cambia la sesión a la cuenta seleccionada y actualiza la pantalla
                                })
                                .show();
                    }
                }
            } else if (item.getTitle().equals("Cerrar sesión")) {
                intencion = new Intent(requireContext(), Logueo.class);
                startActivity(intencion);
                return true;
            }
            return false;
        });
        // mostramos el menú en pantalla
        menu.show();
    }

    /*Este metodo permite cambiar a una cuenta de usuario ya guardada y abrir la pantalla principal de la app con los datos de esa cuenta.*/
    // Guardamos la cuenta activa en SharedPreferences
    // Comprobamos que el usuario existe en la base de datos, se llama a  bd.comprobarUsuario(email) para obtener la información del usuario
    // El resultado se devuelve en un listener llamado onSuccess
    // Dentro de onSuccess convertimos el resultado a una lista de Strings y obtenemos el nombre del usuario, creamos un Intent para abrir la pantalla Principal y asamos información adicional usando putExtra:
    // "nombre" es el nombre del usuario que se muestra en la interfaz y "vistaPrevia" es un indicador de que si se va a cambiar la cuenta
    // Luego iniciar nueva actividad con startActivity y cerramos la actividad anterior con requireActivity para dejarlo en el primer plano

    private void iniciarSesionConCuenta(String email) {
        SharedPreferences prefs = requireContext().getSharedPreferences("MisCuentas", Context.MODE_PRIVATE);
        prefs.edit().putString("cuentaActiva", email).apply();

        bd.comprobarUsuario(email, new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<String> lista = (List<String>) result; // Cast
                    nombre = lista.get(0);
                    id = lista.get(1);

                    // Reiniciar la pantalla
                    intencion = new Intent(requireContext(), Principal.class);
                    intencion.putExtra("nombre", nombre);
                    intencion.putExtra("vistaPrevia", false);
                    intencion.putExtra("id", id);
                    startActivity(intencion);
                    requireActivity().finish(); // cerramos la anterior

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

    // Accedemso al almacenamiento de SharedPreferences para guadar las cosas sencillas, en este caso "MisCuentas" es el nombre donde guardamos las cuentas
    // Leemos el conjunto de cuentas guardadas usando HashSet, esto permite elementos repetidos ni tiene un orden fijo, y si no hay cuentas repetidas se devuelve un Set vacio
    // Convertimos el Set en una lista normal (ArrayList) permiten recorrer los elementos con un índice mantener un orden si es necesario
    // Finalmente, devuelve la lista de emails guardados
    private List<String> obtenerCuentasGuardadas() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MisCuentas", Context.MODE_PRIVATE);
        Set<String> cuentasSet = prefs.getStringSet("cuentas", new HashSet<>());
        return new ArrayList<>(cuentasSet);
    }


}