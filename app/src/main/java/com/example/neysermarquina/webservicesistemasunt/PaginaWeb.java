package com.example.neysermarquina.webservicesistemasunt;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaginaWeb extends AppCompatActivity {
    private WebView Web;
    private AlertDialog builder;
    String url = "http://www.google.com.mx";

    @Override
    public void onBackPressed() {
        if(Web.canGoBack()) {
        Web.goBack();
        }else{
        super.onBackPressed();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_web);

        WebView Web = (WebView) findViewById(R.id.Webv);
        Web.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                return false;
            }
        });
        WebSettings settings = Web.getSettings();
        settings.setJavaScriptEnabled(true);
        Web.getSettings().setBuiltInZoomControls(true);
        Web.loadUrl(url);

        Web.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaginaWeb.this);
                builder.setTitle("Descarga");
                builder.setMessage("¿Desea guardar el fichero en su tarjeta SD?");
                builder.setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        (new DownloadAsyncTask()).execute(url);
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();


            }


            class DownloadAsyncTask extends AsyncTask<String, Void, String>
            {
                @Override
                protected String doInBackground(String... arg0)
                {
                    String result = null;
                    String url = arg0[0];

                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        URL urlObject = null;
                        InputStream inputStream = null;
                        HttpURLConnection urlConnection = null;
                        try
                        {
                            urlObject = new URL(url);
                            urlConnection = (HttpURLConnection) urlObject.openConnection();
                            inputStream = urlConnection.getInputStream();

                            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/webviewdemo";
                            File directory = new File(fileName);
                            File file = new File(directory, url.substring(url.lastIndexOf("/")));
                            directory.mkdirs();


                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;

                            while (inputStream.available() > 0   && (len = inputStream.read(buffer)) != -1)
                            {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }

                            fileOutputStream.write(byteArrayOutputStream.toByteArray());
                            fileOutputStream.flush();
                            result = "guardado en : " + file.getAbsolutePath();
                        }
                        catch (Exception ex)
                        {
                            result = ex.getClass().getSimpleName() + " " + ex.getMessage();
                        }
                        finally
                        {
                            if (inputStream != null)
                            {
                                try
                                {
                                    inputStream.close();
                                }
                                catch (IOException ex)
                                {
                                    result = ex.getClass().getSimpleName() + " " + ex.getMessage();
                                }
                            }
                            if (urlConnection != null)
                            {
                                urlConnection.disconnect();
                            }
                        }
                    }
                    else
                    {
                        result = "Almacenamiento no disponible";
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(String result)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaginaWeb.this);
                    builder.setMessage(result).setPositiveButton("Aceptar", null).setTitle("Descarga");
                    builder.show();
                }

            }


        });


    }
}




