package com.example.neysermarquina.webservicesistemasunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FragmentEliminar extends Fragment {
    private Button a_btnEliminar, a_btnCancelarD;
    EditText a_txtcodigoD;
    TextView a_mensajeD;


    // IP de mi Url
    String IP = "http://192.168.254.2/WebService";
    String DELETE = IP + "/borrar_alumno1.php";

    ObtenerWebServiceEliminar hiloconexionD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_eliminar, container, false);

        a_btnEliminar = (Button) view.findViewById(R.id.btnEliminar);
        a_btnCancelarD = (Button) view.findViewById(R.id.btnCancelarD);

        a_txtcodigoD = (EditText) view.findViewById(R.id.txtCodigoD);


        a_mensajeD = (TextView) view.findViewById(R.id.txtmensajeD);

        a_btnEliminar.setOnClickListener( new View.OnClickListener() {
            Activity activity = getActivity();
            public void onClick(View view){
                if (a_txtcodigoD.getText().toString().trim().equalsIgnoreCase("")){
                    a_txtcodigoD.setError("El campo no puede estar vacio");}
                else {
                    alertOneButton();
                }

            }

        });

        a_btnCancelarD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_txtcodigoD.setText("");
                a_txtcodigoD.requestFocus();
            }
        });

        return view;
    }

    public void alertOneButton() {

        new AlertDialog.Builder(getActivity())
                .setTitle("RegistroUsuariosWeb")
                .setMessage("¿Desea eliminar al usuario?")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                         hiloconexionD = new ObtenerWebServiceEliminar();
                         hiloconexionD.execute(DELETE,"5",a_txtcodigoD.getText().toString());
                         a_txtcodigoD.setText("");
                         a_txtcodigoD.requestFocus();
                    }

                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();



    }

    public class ObtenerWebServiceEliminar extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve ="";


            if(params[1]=="5"){    // Eliminar
                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("idalumno", params[2]);
                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();


                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            devuelve = "Alumno eliminado correctamente";


                        } else if (resultJSON == "2") {
                            devuelve = "No existe el alumno";
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;


            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            a_mensajeD.setText(s);
            //super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
