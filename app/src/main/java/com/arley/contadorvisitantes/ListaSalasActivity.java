package com.arley.contadorvisitantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ListaSalasActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView tvLista;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_salas);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        tvLista = findViewById(R.id.activity_lista_salas_tv_salas);
        progressBar = findViewById(R.id.activity_lista_salas_progressbar);

        progressBar.setVisibility(View.VISIBLE);

        tvLista.setText("");

        databaseReference.child(getResources().getString(R.string.salas_horarios)).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tvLista.setText("");
                    for (DataSnapshot dados : snapshot.getChildren()) {
                        if (dados.exists()){
                            String sala = dados.child("nome").getValue().toString() + "  ||  ";
                            sala += Integer.toString((int)dados.getChildrenCount() - 1) + " visitante(s)\n\n";
                            tvLista.append(sala);
                        }
                    }
                    progressBar.setVisibility(View.GONE);

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ListaSalasActivity.this, "Nenhuma sala visitada ainda", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}