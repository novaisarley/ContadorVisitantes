package com.arley.contadorvisitantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ContagemActivity extends AppCompatActivity {

    Button btVerSalas;
    ImageButton btIncrementar, btDecrementar;
    TextView tvSala, tvNumVisitantes;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contagem);

        btVerSalas = findViewById(R.id.activity_contagem_bt_ver_salas);

        tvSala = findViewById(R.id.activity_contagem_tv_sala);
        tvNumVisitantes = findViewById(R.id.activity_contagem_tv_num_visitantes);

        btIncrementar = findViewById(R.id.activity_contagem_bt_incrementar);
        btDecrementar = findViewById(R.id.activity_contagem_bt_decrementar);

        progressBar = findViewById(R.id.activity_contagem_progressbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        String sala = getCurrentSala();
        tvSala.setText(sala);

        setListeners();

        databaseReference.child(getResources().getString(R.string.salas_horarios)).child(getCurrentSala()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long length = snapshot.getChildrenCount();
                String size = "0";

                if ((int) length == 0) {
                    size = "0";
                } else {
                    size = Integer.toString((int) length - 1);
                }

                tvNumVisitantes.setText(size);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListeners() {

        btIncrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btIncrementar.setClickable(false);

                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                Date date = new Date();
                String hora = Integer.toString(date.getHours());
                String min = Integer.toString(date.getMinutes());
                String seg = Integer.toString(date.getSeconds());

                if (Integer.parseInt(hora) / 10 < 1) hora = "0" + hora;
                if (Integer.parseInt(min) / 10 < 1) min = "0" + min;
                if (Integer.parseInt(seg) / 10 < 1) seg = "0" + seg;

                String time = hora + ":" + min + ":" + seg;

                Horario horario = new Horario(time, currentDate);

                databaseReference.child(getResources().getString(R.string.salas_horarios)).child(getCurrentSala()).
                        child(UUID.randomUUID().toString()).setValue(horario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        btIncrementar.setClickable(true);
                    }
                });

                databaseReference.child(getResources().getString(R.string.salas_horarios)).child(getCurrentSala()).
                        child("nome").setValue(getCurrentSala());


            }
        });

        btVerSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContagemActivity.this, ListaSalasActivity.class));
            }
        });
    }

    String getCurrentSala() {
        sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String sala = sharedPreferences.getString(getString(R.string.current_sala), "");

        return sala;
    }
}