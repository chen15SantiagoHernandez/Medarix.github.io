package com.example.medarix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.data.DB;
import com.example.medarix.R;
import com.example.medarix.adapters.EnfermedadAdapter;
import com.example.medarix.models.Enfermedad;
import com.example.medarix.screens.Login;

import java.util.ArrayList;
import java.util.List;

public class FragmentoEnfermedades extends Fragment {

    SearchView buscador;
    RecyclerView recycler;
    EnfermedadAdapter adapter;
    List<Enfermedad> lista = new ArrayList<>();
    DB bd;
    boolean vistaPrevia;
    private static final String ARG_PARAM1 = "param1";

    public FragmentoEnfermedades() {
        // Constructor público vacío requerido
    }

    public static FragmentoEnfermedades newInstance(boolean param1) {
        FragmentoEnfermedades fragment = new FragmentoEnfermedades();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            vistaPrevia = getArguments().getBoolean(ARG_PARAM1);
        }
        else {
            vistaPrevia = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {
        // Inflar el layout XML de explorar enfermedades raras
        View view = inflater.inflate(R.layout.fragment_fragmento_enfermedades, container, false);

        bd = new DB();

        buscador = view.findViewById(R.id.buscador_enfermedades);
        recycler = view.findViewById(R.id.lista_enfermedades);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        /*adapter = new EnfermedadAdapter(new ArrayList<>(), getContext()); // lista vacía
        recycler.setAdapter(adapter);*/

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CargarEnfermedadesBuscadas(query, vistaPrevia);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CargarEnfermedades(vistaPrevia);
                return false;
            }
        });

        CargarEnfermedades(vistaPrevia);
        return view;
    }

    public void CargarEnfermedades(boolean vistaPrevia) {
        bd.obtenerEnfermedades(new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<Enfermedad> lista = (List<Enfermedad>) result; // Cast
                    adapter = new EnfermedadAdapter(lista, requireContext(), vistaPrevia);
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

    public void CargarEnfermedadesBuscadas(String query, boolean vistaPrevia) {
        bd.obtenerEnfermedadesFiltro(query, new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<Enfermedad> lista = (List<Enfermedad>) result; // Cast
                    adapter = new EnfermedadAdapter(lista, requireContext(), vistaPrevia);
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
}
