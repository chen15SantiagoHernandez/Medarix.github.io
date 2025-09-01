package com.example.medarix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.data.DB;
import com.example.medarix.R;
import com.example.medarix.adapters.ComunidadAdapter;
import com.example.medarix.models.Comunidad;

import java.util.List;

public class FragmentoComunidad extends Fragment {

    SearchView buscador;
    private RecyclerView recycler;
    private ComunidadAdapter adapter;
    private List<Comunidad> listaComunidades;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DB bd;
    boolean vistaPrevia;
    String id;

    public static FragmentoComunidad newInstance(String param1, boolean vistaPrevia) {
        FragmentoComunidad fragment = new FragmentoComunidad();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, vistaPrevia);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            vistaPrevia = getArguments().getBoolean(ARG_PARAM2);
            id = getArguments().getString(ARG_PARAM1);
        }
        else {
            vistaPrevia = true;
            id = "0";
        }
    }

    // Para hacer la consulta cada vez se entre una pantalla
    @Override
    public void onResume() {
        super.onResume();
        CargarComunidades();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_comunidad, container, false);

        recycler = view.findViewById(R.id.recyclerComunidad);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        bd = new DB();

        buscador = view.findViewById(R.id.buscadorComunidades);

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CargarComunidadesBuscadas(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CargarComunidades();
                return false;
            }
        });

        CargarComunidades();

        return view;
    }

    public void CargarComunidades() {
        bd.obtenerComunidades(new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<Comunidad> lista = (List<Comunidad>) result; // Cast
                    adapter = new ComunidadAdapter(requireContext(), lista, id, vistaPrevia);
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

    public void CargarComunidadesBuscadas(String query) {
        bd.obtenerComunidadesFiltro(query, new DB.OnQueryResultListener() {
            @Override
            public void onSuccess(Object result) {
                try {
                    List<Comunidad> lista = (List<Comunidad>) result; // Cast
                    adapter = new ComunidadAdapter(requireContext(), lista, id, vistaPrevia);
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
