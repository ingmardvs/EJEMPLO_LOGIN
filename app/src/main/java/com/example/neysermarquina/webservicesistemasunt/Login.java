package com.example.neysermarquina.webservicesistemasunt;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnIngresar;
    EditText l_txtusuario, l_txtPassword;
    TextView l_error;
    NotificationCompat.Builder mBuilder;
    ProgressBar pb;


    String IP = "http://200.170.1.4/WebService";
    // Rutas de los Web Services
    String GET_SESION = IP + "/login1.php";
    ObtenerWebServiceLogin hiloconexion;

    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";
    private static final String TAG = "MyService";
    private int Accesso = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        l_error = (TextView) findViewById(R.id.txtError);
        l_txtusuario = (EditText) findViewById(R.id.txtUsuario);
        l_txtPassword = (EditText) findViewById(R.id.txtClave);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        btnIngresar.setOnClickListener(this);
        //notificacion();

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("¿Deseas salir de la aplicación?");
        builder.setCancelable(true);
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnIngresar:

                l_error.setText("");

                if (l_txtusuario.getText().toString().trim().equalsIgnoreCase("")) {
                    l_txtusuario.setError("El campo no puede estar vacio");
                } else {
                    if (l_txtPassword.getText().toString().trim().equalsIgnoreCase("")) {
                        l_txtPassword.setError("El campo no puede estar vacio");
                    } else {

                        ConnectivityManager connectivity = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                        if(String.valueOf(info_wifi.getState()).equals("CONNECTED")){
                            hiloconexion = new ObtenerWebServiceLogin();
                            String cadenallamada = GET_SESION + "?idalumno=" + l_txtusuario.getText().toString() + "&clave=" + l_txtPassword.getText().toString();
                            hiloconexion.execute(cadenallamada, "1");   // Parámetros que recibe doInBackground
                        }
                        else{
                            if(String.valueOf(info_datos.getState()).equals("CONNECTED")){
                                hiloconexion = new ObtenerWebServiceLogin();
                                String cadenallamada = GET_SESION + "?idalumno=" + l_txtusuario.getText().toString() + "&clave=" + l_txtPassword.getText().toString();
                                hiloconexion.execute(cadenallamada, "1");   // Parámetros que recibe doInBackground
                            }
                            else{
                                MostrarToast();
                            }
                        }

                    }
                }


                break;
            default:
                break;
        }

    }


    public class ObtenerWebServiceLogin extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {


            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve = "";


            if (params[1] == "1") {

                    for(int i=0;  i<100; i++){
                        publishProgress(i);


                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }


                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");
                    connection.setRequestMethod("GET");
                    int respuesta = connection.getResponseCode();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.
                        StringBuilder result = new StringBuilder();
                        String line = "";


                        while ((line = reader.readLine()) != null) {
                            result.append(line).toString();        // Paso toda la entrada al StringBuilder

                        }
                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.

                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                        Accesso=1;


                        if (resultJSON == "1") {      // hay un alumno que mostrar
                            // prg.setVisibility(View.VISIBLE)
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                        } else if (resultJSON == "2") {
                            devuelve = "Datos incorrectos";

                        }

                    }
                } catch (MalformedURLException e) {
                    devuelve = "Error URL incorrecta";
                    e.printStackTrace();
                } catch (IOException e) {
                    devuelve = "Error de respuesta del servidor";
                    e.printStackTrace();
                } catch (JSONException e) {
                    devuelve = "Error desconocido";
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

            l_error.setText(s);
            pb.setVisibility(View.GONE);
            pb.setProgress(0);
            Limpiar();

            //super.onPostExecute(s);

        }

        @Override
        protected void onPreExecute() {

            notificacion();
            super.onPreExecute();
            pb.setMax(100);
            btnIngresar.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);






        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);

        }
    }

    public void Limpiar(){
        l_txtusuario.setText("");
        l_txtPassword.setText("");
        btnIngresar.setVisibility(View.VISIBLE);
    }


    public void MostrarToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_root));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0 ,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @SuppressLint("ResourceAsColor")
    private void notificacion() {
       // if (Accesso == 1) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ingreso)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notify_body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setLights(R.color.colorPrimaryDark, 1, 0)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setOngoing(true);
            Intent resultintent = new Intent(this, MainActivity.class);
            resultintent.putExtra(NOTIF_MESSAGE, getString(R.string.notify_body_intent));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(this);
            stackBuilder.addNextIntent(resultintent);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIF_ID, mBuilder.build());

       // }

    }

}




