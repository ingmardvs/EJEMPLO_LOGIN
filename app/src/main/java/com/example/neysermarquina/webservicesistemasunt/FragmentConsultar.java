package com.example.neysermarquina.webservicesistemasunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FragmentConsultar extends Fragment implements AdapterView.OnItemSelectedListener  {

    private Button a_btnConsultarCC, a_btnCancelarCC,a_btnConsultarCT;
    EditText a_txtcodigoCC;
    TextView a_resultadoCC, a_resultadoCT;
    Spinner spinnerdatos;
    private ArrayList<Alumnos> AlumnosList;
    ProgressDialog pb;
    ProgressDialog progreso;
    ImageView campoImagen;
    private String Mostrarimagen ="";

    StringRequest stringRequest;

    JsonObjectRequest jsonObjectRequest;

    // IP de mi Url
    String IP = "http://200.170.1.9/WebService";

    String GET_BY_ID = IP + "/obtener_alumno_por_id1.php";

    String GET = IP + "/obtener_alumnos1.php";
    private String URL_LISTA_ALUMNOS = "http://200.170.1.9/WebService/listar.php";


    ObtenerWebService hiloconexion;
    ObtenerWebServiceTodos hiloconexionT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_consultar, container, false);
        a_btnConsultarCC = (Button) view.findViewById(R.id.btnConsultarCC);
        a_btnCancelarCC = (Button) view.findViewById(R.id.btnCancelarCC);
        a_btnConsultarCT = (Button) view.findViewById(R.id.btnConsultarCT);
        a_resultadoCC = (TextView) view.findViewById(R.id.txtResultadoCC);
        a_resultadoCT = (TextView) view.findViewById(R.id.txtResultadoCT);
        a_txtcodigoCC = (EditText) view.findViewById(R.id.txtCodigoCC);
        spinnerdatos = (Spinner) view.findViewById(R.id.spinnerdatos);
        campoImagen=(ImageView) view.findViewById(R.id.ImagenId);


        // seleccionar las frutas del spinner
        AlumnosList = new ArrayList<Alumnos>();
        spinnerdatos.setOnItemSelectedListener(this);
        Resources res = getResources();
        new Getalumnos().execute();
        TabHost tabs=(TabHost)view.findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Consulta por Codigo",
                res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Consulta Todos",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


        a_btnConsultarCC.setOnClickListener( new View.OnClickListener() {
            Activity activity = getActivity();
            public void onClick(View view){
                if (a_txtcodigoCC.equals("")){
                    Toast.makeText(getActivity(),"El campo esta vacío", Toast.LENGTH_SHORT);

                }else {

                     hiloconexion = new ObtenerWebService();
                      String cadenallamada = GET_BY_ID + "?idalumno=" + a_txtcodigoCC.getText().toString();
                     hiloconexion.execute(cadenallamada, "2");   // Parámetros que recibe doInBackground
                }
            }

        });

        a_btnConsultarCT.setOnClickListener( new View.OnClickListener() {
            Activity activity = getActivity();
            public void onClick(View view){
               // Toast.makeText(activity,"hola!",Toast.LENGTH_SHORT).show();
                hiloconexionT = new ObtenerWebServiceTodos();
               hiloconexionT.execute(GET,"3");   // Parámetros que recibe doInBackground
            }

        });

        a_btnCancelarCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_txtcodigoCC.setText("");
                a_txtcodigoCC.requestFocus();
            }
        });



        return view;
    }

    private void populateSpinner() {
        List<Integer> lables = new ArrayList<>();



        for (int i = 0; i < AlumnosList.size(); i++) {
            lables.add(AlumnosList.get(i).getId());
        }


        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_item, lables);


        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerdatos.setAdapter(spinnerAdapter);



    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        a_txtcodigoCC.setText(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class Getalumnos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb = new ProgressDialog(getActivity());
            pb.setMessage("Cargando datos de usuarios..");
            pb.setCancelable(false);
            pb.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_LISTA_ALUMNOS, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray frutas = jsonObj
                                .getJSONArray("alumnos");

                        for (int i = 0; i < frutas.length(); i++) {
                            JSONObject catObj = (JSONObject) frutas.get(i);
                            Alumnos cat = new Alumnos(catObj.getInt("idalumno"));
                                  //  catObj.getString("idalu"));
                            AlumnosList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");
            }

            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pb.isShowing())
                pb.dismiss();
            populateSpinner();
        }
    }



    // INICIO DEL WEB SERVICE

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve="";


            if(params[1]=="2"){    // ingresar

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON=="1") {
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumnos");   // estado es el nombre del campo en el JSON

                                devuelve = devuelve + " Codigo: " + alumnosJSON.getJSONObject(0).getString("idalumno") + "\n" + " Nombre: " +
                                        alumnosJSON.getJSONObject(0).getString("nombre") + "\n" + " Carrera: " +
                                        alumnosJSON.getJSONObject(0).getString("carrera") + "\n" +
                                        "---------------------------------------------------------------------------------------\n\n";

                               Mostrarimagen=alumnosJSON.getJSONObject(0).getString("imagen");

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay alumnos";
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

            a_resultadoCC.setText(s);
            Alumnoss miUsuario=new Alumnoss();
            miUsuario.setDato(Mostrarimagen);

             if (miUsuario.getImagen()!=null){
             campoImagen.setImageBitmap(miUsuario.getImagen());
            }else{
              campoImagen.setImageResource(R.drawable.ic_menu_camera);
            }


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



    //REVISAR VOID PARA MOSTRAR IMAGEN

    public class ObtenerWebServiceTodos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve="";


           if(params[1]=="3"){    // Consulta de todos los alumnos

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");
                    connection.connect();

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                        if (resultJSON=="1"){      // hay alumnos a mostrar
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumnos");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve +" Codigo: " +alumnosJSON.getJSONObject(i).getString("idalumno") +"\n"+ " Nombre: " +
                                        alumnosJSON.getJSONObject(i).getString("nombre") +"\n"+ " Carrera: " +
                                        alumnosJSON.getJSONObject(i).getString("carrera") + "\n"+
                                    "---------------------------------------------------------------------------------------\n\n";
                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve =("No hay alumnos");
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

            a_resultadoCT.setText(s);
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
