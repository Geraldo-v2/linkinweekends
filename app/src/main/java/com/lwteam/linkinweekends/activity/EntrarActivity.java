package com.lwteam.linkinweekends.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flod.loadingbutton.LoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.model.Usuario;

public class EntrarActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private LoadingButton btEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //CONFIGURA BARRA DE NOTIFICACOES TRANPARENTTE
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FECHA O CODIGO DA BARRA TRANSPARENTE

        setContentView(R.layout.activity_entrar);
        //ESCONDE A TOOLBAR / ACTION BAR PADRAO DO ANDROID
        getSupportActionBar().hide();

        verificarUsuarioLogado();
        inicializarComponentes();

        //LOGIN VIA EMAIL E VALIDA SE OS CAMPOS ESTAO PREENCHIDOS
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btEntrar.start();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();


                if(!textoEmail.isEmpty()){
                    if(!textoSenha.isEmpty()){
                        usuario=new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin(usuario);
                    }else{
                        btEntrar.fail();
                        Toast.makeText(EntrarActivity.this,"Preencha a senha!",Toast.LENGTH_SHORT).show();

                    }
                }else{ btEntrar.fail();
                    Toast.makeText(EntrarActivity.this,"Preencha o email!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    //VERIFICA SE USUARIO JA ESTA LOGADO, SE POSITIVO, VAI DIRETO NO MENU
    public void verificarUsuarioLogado(){
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            if(autenticacao.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                finish();
            }
    }
    //VALIDACAO DE LOGIN COMPARA OS DADOS COM O BANCO FIREBASE
    public void validarLogin(Usuario usuario){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                            btEntrar.complete();
                            finish();
                        }else {
                            Toast.makeText(EntrarActivity.this, "Erro ao fazer Login", Toast.LENGTH_SHORT).show();
                            btEntrar.fail();
                        }
                    }
                }
        );
    }
    public void inicializarComponentes(){

        //BLOCO DE ANIMACOES - QUERO RETIRAR DAQUI QUANTO ANTES - GERALDO
        Animation animacaocimapbaixo1000;
        Animation animacaocimapbaixo4000;
        Animation animacaobaixopcima2000;
        Animation animacaobaixopcima4000;

        animacaocimapbaixo1000 = AnimationUtils.loadAnimation(this,R.anim.animacaocimapbaixo1000);
        animacaocimapbaixo4000 = AnimationUtils.loadAnimation(this,R.anim.animacaocimapbaixo4000);
        animacaobaixopcima2000 = AnimationUtils.loadAnimation(this,R.anim.animacaobaixopcima2000);
        animacaobaixopcima4000 = AnimationUtils.loadAnimation(this,R.anim.animacaobaixopcima4000);

        ImageView imgFundoBarraca1Login = findViewById(R.id.imgFundoBarraca1Login);
        ImageView imgFundoFogueira      = findViewById(R.id.imgFundoFogueiraLogin);
        ImageView imgFundoArvore1       = findViewById(R.id.imgFundoArvore1Login);
        ImageView imgFundoArvore2       = findViewById(R.id.imgFundoArvore2Login);
        ImageView imgFundoMontanha1     = findViewById(R.id.imgFundoMontanha1Login);
        //FIM DAS ANIMACOES


        //CONECTA AO FRONT-END
        ImageView imgLogoEntrar = findViewById(R.id.imgLogoEntrar);
        campoEmail = findViewById(R.id.txtLoginEmail);
        campoSenha = findViewById(R.id.txtLoginSenha);
        btEntrar   = findViewById(R.id.btnEntrar);
        campoEmail.requestFocus();

        //APLICA AS ANIMACOES
        imgLogoEntrar.setAnimation(animacaocimapbaixo1000);
        campoEmail.setAnimation(animacaocimapbaixo1000);
        campoSenha.setAnimation(animacaocimapbaixo1000);
        btEntrar.setAnimation(animacaocimapbaixo1000);

        imgFundoArvore2.setAnimation(animacaobaixopcima4000);
        imgFundoMontanha1.setAnimation(animacaobaixopcima4000);
        imgFundoArvore1.setAnimation(animacaobaixopcima2000);
        imgFundoBarraca1Login.setAnimation(animacaobaixopcima2000);
        imgFundoFogueira.setAnimation(animacaobaixopcima2000);
        //FINALIZA AS ANIMACOES

    }
}
