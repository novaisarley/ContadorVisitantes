package com.arley.contadorvisitantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
    DatabaseReference databaseRefSala;
    DatabaseReference databaseRefSalaHorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_sala);

        btIniciar = findViewById(R.id.activity_selecao_bt_iniciar);
        spinnerSala = findViewById(R.id.activity_selecao_sala_spinner_sala);
        progressBar = findViewById(R.id.activity_selecao_sala_progressBar);

        /*if (!getCurrentSala().trim().isEmpty()){
            startActivity(new Intent(SelecaoSalaActivity.this, ContagemActivity.class));
        }*/

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseRefSala = mFirebaseDatabase.getReference("Salas");
        databaseRefSalaHorarios = mFirebaseDatabase.getReference(getResources().getString(R.string.salas_horarios));

        progressBar.setVisibility(View.VISIBLE);

        setListeners();

        btIniciar.setClickable(false);
        btIniciar.setFocusable(false);

        databaseRefSala.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    DataSnapshot dataSnapshotSala = snapshot;
                    ValueEventListener eventListener = databaseRefSalaHorarios.
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : dataSnapshotSala.getChildren()) {
                                        databaseRefSalaHorarios.child(dataSnapshot.getValue().toString()).
                                                child("nome").setValue(dataSnapshot.getValue().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    databaseRefSalaHorarios.removeEventListener(eventListener);

                    List<String> salas = (ArrayList<String>) snapshot.getValue();
                    preencherSpinner(salas);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SelecaoSalaActivity.this, "Nenhuma sala cadastrada", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setListeners() {
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
                setCurrentSala(spinnerSala.getSelectedItem().toString());
                startActivity(new Intent(SelecaoSalaActivity.this, ContagemActivity.class));
            }
        });
    }

    public void preencherSpinner(List<String> salas) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, salas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSala.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        btIniciar.setClickable(true);
        btIniciar.setFocusable(true);
    }

    void setCurrentSala(String sala) {
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(getString(R.string.current_sala), sala);
        editor.apply();
    }

    String getCurrentSala() {
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String sala = sharedPreferences.getString(getString(R.string.current_sala), "");

        return sala;
    }
}