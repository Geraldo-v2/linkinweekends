package com.lwteam.linkinweekends.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.UsuarioFirebase;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    private TextView tvNomeUsuario, tvEmailUsuario;
    private Button btEditarPerfil,btSair;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        //CONFIGURACOES INICIAIS
        autenticacao    = ConfiguracaoFirebase.getFirebaseAutenticacao();
        inicializarComponentes();

        //CONFIGURA TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECUPERAR DADOS DO USUARIO
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        tvNomeUsuario.setText(usuarioPerfil.getDisplayName());
        tvEmailUsuario.setText(usuarioPerfil.getEmail());
        Uri url = usuarioPerfil.getPhotoUrl();
        if(url!=null){
            Glide.with(PerfilActivity.this)
                    .load(url)
                    .into(imagePerfil);
        }else {
            imagePerfil.setImageResource(R.drawable.avatarperfil);
        }

    }
    //CONECTA OS COMPONENTES DO LAYOUT COM A CLASSE JAVA
    public void inicializarComponentes(){
        progressBar     = findViewById(R.id.progressBarPerfil);
        imagePerfil     = findViewById(R.id.imgPerfil);
        tvNomeUsuario   = findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario  = findViewById(R.id.tvEmailUsuario);
        btEditarPerfil  = findViewById(R.id.btnEditarPerfil);
        btSair          = findViewById(R.id.btnSairPerfil);

        btEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditarPerfilActivity.class));
                finish();
            }
        });
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    autenticacao.signOut();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(PerfilActivity.this,InicioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
