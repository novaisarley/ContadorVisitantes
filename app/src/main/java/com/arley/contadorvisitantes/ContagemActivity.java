package com.arley.contadorvisitantes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ContagemActivity extends AppCompatActivity {

    Button btVerSalas;
    ImageButton btIncrementar, btDecrementar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contagem);

        btVerSalas = findViewById(R.id.activity_contagem_bt_ver_salas);

        btIncrementar = findViewById(R.id.activity_contagem_bt_incrementar);
        btDecrementar = findViewById(R.id.activity_contagem_bt_decrementar);

        setListeners();
    }

    private void setListeners() {

        btIncrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //incrementarnofirebase
            }
        });

        btVerSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContagemActivity.this, ListaSalasActivity.class));
            }
        });
    }

    String getCurrentSala(){
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String sala = sharedPreferences.getString(getString(R.string.current_sala), "");

        return sala;
    }
}