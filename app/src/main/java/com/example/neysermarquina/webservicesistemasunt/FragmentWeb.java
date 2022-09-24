package com.example.neysermarquina.webservicesistemasunt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FragmentWeb extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_web, container, false);

        Intent intent = new Intent(getActivity(), PaginaWeb.class);
        getActivity().startActivity(intent);

        return view;
    }


}
