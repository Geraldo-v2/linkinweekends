package com.lwteam.linkinweekends.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.adapter.AdapterAtividades;
import com.lwteam.linkinweekends.adapter.AdapterLocais;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.RecyclerItemClickListener;
import com.lwteam.linkinweekends.model.Locais;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ListaAtividadesActivity extends AppCompatActivity {

    private RecyclerView recyclerAtividades;
    private List<Locais> Listalocaisatividades = new ArrayList<>();
    private AdapterAtividades adapterLocaisAtividades;
    private DatabaseReference localUsuarioRef;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BARRA DE NOTIFACOES TRANSPARENTE
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FECHA O BLOCO DE NOTIFICACOES TRANSPARENTE
        setContentView(R.layout.activity_lista_atividade);
        //OCULTA A BARRA PADRAO
        getSupportActionBar().hide();

        //RECUPERA O CAMINHO DO FIREBASE
        Intent dados = getIntent();
            String estadorecebido = dados.getStringExtra("estado");
            String regiaorecebido = dados.getStringExtra("regiao");
            String cidaderecebido = dados.getStringExtra("cidade");
            String idrecebido = dados.getStringExtra("id");

        //CONFIGURACOES INICIAS
        localUsuarioRef = ConfiguracaoFirebase.getFirebase()
                .child("locais")
                .child(estadorecebido)
                .child(regiaorecebido)
                .child(cidaderecebido)
                .child(idrecebido)
                .child("atividades");
        inicializarComponentes();
        //CONFIGURA O RECYCLER VIEW (LISTA COM OS LOCAIS)
        recyclerAtividades.setLayoutManager(new LinearLayoutManager(this));
        recyclerAtividades.setHasFixedSize(true);
        adapterLocaisAtividades = new AdapterAtividades(Listalocaisatividades, this);
        recyclerAtividades.setAdapter(adapterLocaisAtividades);

        //RECUPERA DADOS DOS LOCAIS PARA O USUARIO
        recuperarLocais();

        //ADICIONA O EVENTO DE CLICK NO RECYCLER VIEW
        recyclerAtividades.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerAtividades,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Locais localSelecionado = Listalocaisatividades.get(position);
                                Intent i = new Intent(ListaAtividadesActivity.this,AtividadeActivity.class);
                                i.putExtra("localSelecionado",localSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );
    }
    //FAZ O CAMINHO NO FIREBASE NO POR NÓ PARA RECUPERAR OS DADOS - DA PRA ALTERAR O CODIGO RODANDO
    //O NÓ POR FORA IGUAL NOS COMENTARIOS - AINDA POR IMPLEMENTAR - GERALDO
    private void recuperarLocais(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando atividades")
                .setCancelable(false)
                .build();
        dialog.show();

        localUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Listalocaisatividades.clear();
                for(DataSnapshot categorias : dataSnapshot.getChildren()){
                    for(DataSnapshot dificuldade : categorias.getChildren()) {
                        for (DataSnapshot ds : dificuldade.getChildren()) {
                            Locais atividades = (ds.getValue(Locais.class));
                            Listalocaisatividades.add(atividades);
                        }
                    }
                }

                Collections.reverse(Listalocaisatividades);
                adapterLocaisAtividades.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void inicializarComponentes(){
        recyclerAtividades = findViewById(R.id.recycleAtividades);
    }
}
