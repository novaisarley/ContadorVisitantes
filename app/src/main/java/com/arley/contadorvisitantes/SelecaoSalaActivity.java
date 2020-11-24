package com.arley.contadorvisitantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelecaoSalaActivity extends AppCompatActivity {

    Spinner spinnerSala;
    Button btIniciar;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_sala);

        if (!getCurrentSala().trim().isEmpty()){
            startActivity(new Intent(SelecaoSalaActivity.this, ContagemActivity.class));
            finish();
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference("Salas");

        btIniciar = findViewById(R.id.activity_inicio_bt_iniciar);
        spinnerSala = findViewById(R.id.activity_selecao_sala_spinner_sala);
        progressBar = findViewById(R.id.activity_selecao_sala_progressBar);

        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> salas = (ArrayList<String>) snapshot.getValue();
                preencherSpinner(salas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setListeners(){
        spinnerSala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCurrentSala(spinnerSala.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelecaoSalaActivity.this, ContagemActivity.class));
            }
        });
    }

    public void preencherSpinner(List<String> salas){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, salas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSala.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    void setCurrentSala(String sala){
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(getString(R.string.current_sala), sala);
        editor.apply();
    }

    String getCurrentSala(){
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String sala = sharedPreferences.getString(getString(R.string.current_sala), "");

        return sala;
    }
}