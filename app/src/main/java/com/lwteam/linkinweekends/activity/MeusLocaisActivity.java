package com.lwteam.linkinweekends.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.adapter.AdapterLocais;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.RecyclerItemClickListener;
import com.lwteam.linkinweekends.model.Locais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MeusLocaisActivity extends AppCompatActivity {

    private RecyclerView recyclerLocais;
    private List<Locais> Listalocais = new ArrayList<>();
    private AdapterLocais adapterLocais;
    private DatabaseReference localUsuarioRef;
    private AlertDialog dialog;
    private Locais localSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BARRA DE NOTIFICACOES TRANSPARENTE
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FIM DA BARRA TRANSPAREMTE
        setContentView(R.layout.activity_meus_locais);

        //CONFIGURACOES INICIAS RECUPERA TODOS OS LOCAIS CADASTRADOS NO NÓ LOCAIS DO FIREBASE
        localUsuarioRef = ConfiguracaoFirebase.getFirebase()
                .child("locais");

        inicializarComponentes();

        //CONFIGURA E CARREGA A TOOLBAR (BARRA SUPERIOR)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabLocal = findViewById(R.id.fabLocal);
        fabLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FormularioCadLocalActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //CONFIGURA O RECYCLER VIEW (LISTA COM OS LOCAIS)
        recyclerLocais.setLayoutManager(new LinearLayoutManager(this));
        recyclerLocais.setHasFixedSize(true);
        adapterLocais = new AdapterLocais(Listalocais, this);
        recyclerLocais.setAdapter(adapterLocais);

        //RECUPERA DADOS DOS LOCAIS PARA O USUARIO
        recuperarLocais();

        //ADICIONA O EVENTO DE CLICK NO RECYCLER VIEW
        recyclerLocais.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerLocais,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Locais localSelecionado = Listalocais.get(position);
                                Intent i = new Intent(MeusLocaisActivity.this, FormularioCadAtvActivity.class);
                                i.putExtra("localSelecionado",localSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Locais localSelecionado = Listalocais.get(position);
                                localSelecionado.remover();
                                adapterLocais.notifyDataSetChanged();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );
    }

    private void recuperarLocais(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando locais")
                .setCancelable(false)
                .build();
        dialog.show();

        localUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Listalocais.clear();
                for(DataSnapshot estados : dataSnapshot.getChildren()){
                    for(DataSnapshot regioes : estados.getChildren()){
                        for(DataSnapshot cidades : regioes.getChildren()){
                            for(DataSnapshot ds : cidades.getChildren()){
                                Locais locais = (ds.getValue(Locais.class));
                                Listalocais.add(locais);
                            }
                        }
                    }
                }
                Collections.reverse(Listalocais);
                adapterLocais.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void inicializarComponentes(){
        recyclerLocais = findViewById(R.id.recyclerLocais);
    }
}
