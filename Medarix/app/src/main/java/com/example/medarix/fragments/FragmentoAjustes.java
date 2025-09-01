package com.example.medarix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.listaAjuste.AyudaActivity;
import com.example.medarix.listaAjuste.FavoritosActivity;
import com.example.medarix.listaAjuste.InfoCuentaActivity;
import com.example.medarix.listaAjuste.NotificacionesActivity;
import com.example.medarix.listaAjuste.RecordatoriosActivity;
import com.example.medarix.models.Recordatorio;
import com.example.medarix.screens.Logueo;
import com.example.medarix.R;
import com.example.medarix.adapters.MenuAdapter;
import com.example.medarix.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentoAjustes extends Fragment {

    private RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView texto;
    String nombre;
    boolean vistaPrevia;

    public static FragmentoAjustes newInstance(String param1, boolean vistaPrevia) {
        FragmentoAjustes fragment = new FragmentoAjustes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, vistaPrevia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nombre = getArguments().getString(ARG_PARAM1);
            vistaPrevia = getArguments().getBoolean(ARG_PARAM2);
        }
        else {
            nombre = "desconocido";
            vistaPrevia = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_fragmento_ajustes, container, false);

        texto = view.findViewById(R.id.saludo_ajustes);
        texto.setText("Hi, " + nombre);

        // Inicializamos el RecyclerView
        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Lista de ítems
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(android.R.drawable.ic_menu_info_details, "Información de la cuenta"));
        items.add(new MenuItem(android.R.drawable.btn_star_big_on, "Mis favoritos"));
        items.add(new MenuItem(android.R.drawable.ic_dialog_info, "Notificaciones"));
        items.add(new MenuItem(android.R.drawable.ic_lock_power_off, "Cerrar sesión"));
        items.add(new MenuItem(android.R.drawable.ic_menu_agenda, "Recordatorios"));
        items.add(new MenuItem(android.R.drawable.ic_menu_help, "Ayuda y soporte"));

        // Configurar el adaptador
        MenuAdapter adapter = new MenuAdapter(items, item -> {
            Intent intent = null;

            if(!vistaPrevia){
                switch (item.getTitle()) {
                    case "Información de la cuenta":
                        intent = new Intent(requireContext(), InfoCuentaActivity.class);
                        break;
                    case "Mis favoritos":
                        intent = new Intent(requireContext(), FavoritosActivity.class);
                        break;
                    case "Notificaciones":
                        intent = new Intent(requireContext(), NotificacionesActivity.class);
                        break;
                    case "Cerrar sesión":
                        intent = new Intent(requireContext(), Logueo.class);
                        break;
                    case "Recordatorios":
                        intent = new Intent(requireContext(), RecordatoriosActivity.class);
                        break;
                    case "Ayuda y soporte":
                        intent = new Intent(requireContext(), AyudaActivity.class);
                        break;
                }

                startActivity(intent);
            }
            else {
                if (item.getTitle() == "Cerrar sesión") {
                    intent = new Intent(requireContext(), Logueo.class);
                    startActivity(intent);
                }
            }
        });


        recyclerView.setAdapter(adapter);

        // Retornar la vista inflada
        return view;
    }
}
