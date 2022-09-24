package com.example.neysermarquina.webservicesistemasunt;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentVista extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    EditText etBuscador;
    RecyclerView rvLista;
    AdaptadorAlumnos adaptador;
    List<Almnos> listaAlumnos;
    ProgressDialog pb;
    LinearLayoutManager linearLayoutManager;
    RequestQueue queue;
    public String Mostrarimagen ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista, container, false);

        etBuscador = (EditText) view.findViewById(R.id.etBuscar);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });

        rvLista = (RecyclerView) view.findViewById(R.id.rvLista);

        rvLista.setLayoutManager(new GridLayoutManager(this.getActivity(),1));

        listaAlumnos = new ArrayList<>();

        adaptador = new AdaptadorAlumnos(this.getActivity(), (ArrayList<Almnos>) listaAlumnos);

        obtenerAlumnos();

        rvLista.setAdapter(adaptador);

        return view;

    }

        public void obtenerAlumnos(){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.URL_VISTA),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("Almnos");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Almnos alumnos = new Almnos();
                                    alumnos.setIdalumno(jsonObject1.getString("idalumno"));
                                    alumnos.setNombre(jsonObject1.getString("nombre"));
                                    alumnos.setCarrera(jsonObject1.getString("carrera"));
                                    alumnos.setRutaImagen(jsonObject1.getString("ruta_imagen"));
                                    listaAlumnos.add(alumnos);


                                }

                                adaptador = new AdaptadorAlumnos(getContext(), (ArrayList<Almnos>) listaAlumnos);
                                rvLista.setAdapter(adaptador);

                                //adaptador.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }

            );
           requestQueue.add(stringRequest);

        }

        public void filtrar(String texto){
        ArrayList<Almnos> filtrarLista = new ArrayList<>();

        for(Almnos almnos : listaAlumnos){
            if(almnos.getNombre().toLowerCase().contains(texto.toLowerCase())){
                    filtrarLista.add(almnos);
                }
            }

            adaptador.filtrar(filtrarLista);
        }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(JSONObject jsonObject) {

    }
}
