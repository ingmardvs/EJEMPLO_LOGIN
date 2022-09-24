package com.example.neysermarquina.webservicesistemasunt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;


public class AdaptadorAlumnos extends RecyclerView.Adapter<AdaptadorAlumnos.FragmentVistaViewHolder>{

    Context context;
    ArrayList<Almnos> listaAlumnos;
    RequestQueue request;

    public AdaptadorAlumnos(Context context, ArrayList<Almnos> listaAlumnos){
        this.context = context;
        this.listaAlumnos = listaAlumnos;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public FragmentVistaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_usuario,viewGroup,false);
        return new FragmentVistaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAlumnos.FragmentVistaViewHolder holder, int i) {
        holder.tvId.setText(listaAlumnos.get(i).getIdalumno());
        holder.tvNombre.setText(listaAlumnos.get(i).getNombre());
        holder.tvCarrera.setText(listaAlumnos.get(i).getCarrera());

        if(listaAlumnos.get(i).getRutaImagen()!=null){
            //holder.tvImagen.setImageBitmap(listaAlumnos.get(i).getImagen());
            Cargarimagen(listaAlumnos.get(i).getRutaImagen(),holder);
        }
        else
        {
            holder.tvImagen.setImageResource(R.drawable.noimagen);
        }
    }

    private void Cargarimagen(String rutaImagen, final AdaptadorAlumnos.FragmentVistaViewHolder holder) {
        String urlimagen = "http://200.170.1.13/Webservice/" + rutaImagen;
        urlimagen = urlimagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlimagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.tvImagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"Error al cargar imagen",Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }


    public class FragmentVistaViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvNombre, tvCarrera;
        ImageView tvImagen;


        public FragmentVistaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            tvCarrera = (TextView) itemView.findViewById(R.id.tvCarrera);
            tvImagen = (ImageView) itemView.findViewById(R.id.tvImagen);

        }
    }

    public void filtrar(ArrayList<Almnos> filroAlumnos){
        this.listaAlumnos = filroAlumnos;
        notifyDataSetChanged();
    }



}
