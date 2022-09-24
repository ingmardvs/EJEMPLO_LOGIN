package com.example.neysermarquina.webservicesistemasunt;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

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


public class FragmentEditar extends Fragment {
    private Button a_btnEditar, a_btnCancelarE;
    EditText a_txtcodigoE,a_txtNombreE,a_txtcarreraE;
    TextView a_mensajeE;

    // IP de mi Url
    String IP = "http://200.170.1.39/WebService";
    String UPDATE = IP + "/actualizar_alumno.php";

    ObtenerWebServiceEditar hiloconexionE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_editar, container, false);

        a_btnEditar = (Button) view.findViewById(R.id.btnEditar);
        a_btnCancelarE = (Button) view.findViewById(R.id.btnCancelarE);

        a_txtcodigoE = (EditText) view.findViewById(R.id.txtCodigoE);
        a_txtNombreE = (EditText) view.findViewById(R.id.txtNombreE);
        a_txtcarreraE = (EditText) view.findViewById(R.id.txtCarreraE);

        a_mensajeE = (TextView) view.findViewById(R.id.txtmensajeE);

        a_btnEditar.setOnClickListener( new View.OnClickListener() {

            Activity activity = getActivity();
            public void onClick(View view){

                hiloconexionE = new ObtenerWebServiceEditar();
                hiloconexionE.execute(UPDATE,"4",a_txtcodigoE.getText().toString(),a_txtNombreE.getText().toString(),a_txtcarreraE.getText().toString());   // Parámetros que recibe doInBackground
                a_txtcodigoE.setText("");
                a_txtNombreE.setText("");
                a_txtcarreraE.setText("");
                a_txtcodigoE.requestFocus();

            }

        });

        a_btnCancelarE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_txtcodigoE.setText("");
                a_txtNombreE.setText("");
                a_txtcarreraE.setText("");
                a_txtcodigoE.requestFocus();
            }
        });

        return view;
    }
    public class ObtenerWebServiceEditar extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve ="";


            if(params[1]=="4"){    // ACTUALIZAR
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
                    jsonParam.put("idalumno",params[2]);
                    jsonParam.put("nombre", params[3]);
                    jsonParam.put("carrera", params[4]);
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
                            devuelve = "Alumno actualizado correctamente";


                        } else if (resultJSON == "2") {
                            devuelve = "El alumno no pudo actualizarse";
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

            a_mensajeE.setText(s);
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
