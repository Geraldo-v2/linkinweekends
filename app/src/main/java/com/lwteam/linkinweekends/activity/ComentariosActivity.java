package com.lwteam.linkinweekends.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.adapter.AdapterComentario;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.UsuarioFirebase;
import com.lwteam.linkinweekends.model.Comentario;
import com.lwteam.linkinweekends.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private EditText editComentario;
    private RecyclerView recyclerComentarios;
    private String idLocal;
    private Usuario usuario;
    private AdapterComentario adapterComentario;
    private List<Comentario> listaComentarios = new ArrayList<>();

    private DatabaseReference firebaseRef;
    private DatabaseReference comentariosRef;
    private ValueEventListener valueEventListenerComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CONECTA COM XML
        setContentView(R.layout.activity_comentarios);
        //CARREGA OS ELEMENTOS DO XML
        editComentario =findViewById(R.id.txtComentario);
        recyclerComentarios = findViewById(R.id.recyclerComentarios);

        // VALIDA A CONEXAO COM FIREBASE
        usuario = UsuarioFirebase.getDadosUsuarioLogado();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //CONFIGURA A TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comentários");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_actionbar_24dp);
        //FIM DA CONFIG DA TOOLBAR

        //CARREGA A LISTA DE COMENTARIOS E CONFIGURA O LAYOUT
        adapterComentario = new AdapterComentario(listaComentarios, getApplicationContext());
        recyclerComentarios.setHasFixedSize(true);
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerComentarios.setAdapter(adapterComentario);
        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            idLocal =bundle.getString("id");

        }

    }
    //RECUPERA TODOS OS COMENTARIOS NO FIREBASE
    private void recuperarComentarios(){
        comentariosRef = firebaseRef.child("comentarios")
                .child(idLocal);
        valueEventListenerComentarios = comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaComentarios.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    listaComentarios.add(ds.getValue(Comentario.class));
                }
                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentariosRef.removeEventListener(valueEventListenerComentarios);
    }

    //SALVA OS COMENTARIOS RECUPERANDO O CAMINHO DO FIREBASE
    public void salvarComentario(View view){
        String textoComentario = editComentario.getText().toString();
        if (textoComentario != null && !textoComentario.equals("")){

            Comentario comentario = new Comentario();
            comentario.setIdLocal(idLocal);
            comentario.setIdUsuario(usuario.getId());
            comentario.setNomeUsuario(usuario.getNome());
            comentario.setCaminhoFoto(usuario.getCaminhoFoto());
            comentario.setComentario(textoComentario);
            if(comentario.salvar()){
                Toast.makeText(this, "Comentário salvo com sucesso",
                        Toast.LENGTH_SHORT).show();
            }
            //EM CASO DE CAMPO VAZIO
        }else{
            Toast.makeText(this,
                    "Insira o comentário antes de salvar",
                    Toast.LENGTH_SHORT).show();
        }
        editComentario.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
