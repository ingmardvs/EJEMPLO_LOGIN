package com.example.neysermarquina.webservicesistemasunt;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            String mensaje = intent.getStringExtra(Login.NOTIF_MESSAGE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Login.NOTIF_ID);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(this,MapsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager=getSupportFragmentManager();
        if (id == R.id.nav_inicio) {
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentHome()).commit();
            getSupportActionBar().setTitle("Home");
        } else if (id == R.id.nav_ingreso) {
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentIngresar()).commit();
            getSupportActionBar().setTitle("Ingresar");
        } else if (id == R.id.nav_editar) {
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentEditar()).commit();
            getSupportActionBar().setTitle("Editar");
        } else if (id == R.id.nav_eliminar) {
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentEliminar()).commit();
            getSupportActionBar().setTitle("Eliminar");
        } else if (id == R.id.nav_consultar) {
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentConsultar()).commit();
            getSupportActionBar().setTitle("Consultar");}
         else if (id == R.id.nav_vista) {
                fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentVista()).commit();
                getSupportActionBar().setTitle("Consultar Vista");
        } else if (id == R.id.nav_subir){
            fragmentManager.beginTransaction().replace(R.id.contenido,new FragmentWeb()).commit();
            getSupportActionBar().setTitle("Pagina Web");
        }
        else if (id == R.id.nav_send) {
            Toast alertamensaje = Toast.makeText(getApplicationContext(), "Saliendo del aplicacion", Toast.LENGTH_SHORT);
            alertamensaje.show();
            Intent i=new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
