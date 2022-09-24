package com.example.neysermarquina.webservicesistemasunt;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by Ing_mar on 01/12/17.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SystemClock.sleep(3000);
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
