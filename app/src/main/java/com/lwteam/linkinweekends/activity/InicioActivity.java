package com.lwteam.linkinweekends.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.UsuarioFirebase;
import com.lwteam.linkinweekends.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class InicioActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth autenticacao;
    private FirebaseUser user;
    private Usuario usuario;
    //IMPLEMENTACAO GOOGLE PRONTA ADICIONE AS LINHAS DE FUNCOES PARA O BOTAO
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BARRA DE NOTIFICAOES TRANSPARENTE
        requestWindowFeature(1);
        autenticacao = FirebaseAuth.getInstance();
        user = autenticacao.getCurrentUser();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FIM DO CODIGO DE BARRA TRANSPARENTE

        setContentView(R.layout.activity_inicio);
        //OCULTA A BARRA DE NOTIFCACOES PADRAO DO ANDROID
        getSupportActionBar().hide();

        inicializarComponentes();
        verificarUsuarioLogado();
    }

    private void inicializarComponentes() {

        //BLOCO DE ANIMACAO - VOU TIRAR DAQUI - GERALDO
        Animation animacaocimapbaixo2000;
        Animation animacaocimapbaixo4000;
        Animation animacaobaixopcima2000;
        Animation animacaobaixopcima4000;

        ImageView imgFundoMontanha1 = findViewById(R.id.imgFundoMontanha1Inicio);
        ImageView imgFundoMontanha2 = findViewById(R.id.imgFundoMontanha2Inicio);
        ImageView imgFundoBalao1 = findViewById(R.id.imgFundoBalao1Inicio);
        ImageView imgFundoBalao2 = findViewById(R.id.imgFundoBalao2Inicio);
        //FIM DO BLOCO DE ANIMACOES

        ImageView imgLogoinicio = findViewById(R.id.imgLogoInicial);
        Button btRegistrar = findViewById(R.id.btnRegistrar);
        Button btEntrar = findViewById(R.id.btnInicioEntrar);
        //BOTAO GOOGLE JA INICIALIZADO COM XML (FRONT)
        SignInButton btGoogle = findViewById(R.id.btnContGoogle);
        LoginButton btFacebook = findViewById(R.id.btnContFace);
        TextView infoTxtRegistrarGoogleFace = findViewById(R.id.infoRegistrarFaceGoogle);

        //LEVA A TELA DE CADASTRO VIA EMAIL
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(InicioActivity.this, CadEmailActivity.class);
                startActivity(it);
            }
        });
        //LEVA A TELA DE LOGIN VIA EMAIL
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(InicioActivity.this, EntrarActivity.class);
                startActivity(it);
            }
        });

        //LOGIN VIA FACEBOOK - VOU CORRIGIR OS ID'S GERALDO
        callbackManager = CallbackManager.Factory.create();
        btFacebook.setPermissions(Arrays.asList("email", "public_profile"));
        btFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loaduserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cancelado pelo usuário",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //FIM DA PRIMEIRA PARTE DE LOGIN VIA FACEBOOK

        //CONEXAO DAS ANIMACOES
        animacaocimapbaixo2000 = AnimationUtils.loadAnimation(this, R.anim.animacaocimapbaixo2000);
        animacaocimapbaixo4000 = AnimationUtils.loadAnimation(this, R.anim.animacaocimapbaixo4000);
        animacaobaixopcima2000 = AnimationUtils.loadAnimation(this, R.anim.animacaobaixopcima2000);
        animacaobaixopcima4000 = AnimationUtils.loadAnimation(this, R.anim.animacaobaixopcima4000);

        imgLogoinicio.setAnimation(animacaocimapbaixo4000);

        btRegistrar.setAnimation(animacaocimapbaixo2000);
        btEntrar.setAnimation(animacaocimapbaixo2000);
        btGoogle.setAnimation(animacaocimapbaixo2000);
        btFacebook.setAnimation(animacaocimapbaixo2000);
        infoTxtRegistrarGoogleFace.setAnimation(animacaocimapbaixo2000);

        imgFundoMontanha1.setAnimation(animacaobaixopcima2000);
        imgFundoMontanha2.setAnimation(animacaobaixopcima4000);
        imgFundoBalao1.setAnimation(animacaobaixopcima4000);
        imgFundoBalao2.setAnimation(animacaobaixopcima2000);
        //FIM DO BLOCO DAS ANIMAÇOES
    }

    //VERIFICA SE O USUARIO JA FEZ O LOGIN VIA EMAIL E MANDA PRO MENU
    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }
    }

    //SEGUNDA PARTE DO FACEBOOK - ABRE A PAGINA PARA LOGIN NO FACEBOOK
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode,resultCode,data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {

            } else
                loaduserProfile(currentAccessToken);

        }
    };

    //PEGA OS DADOS DO FACEBOOK E SOBE NO FIREBASE
    private void loaduserProfile(AccessToken accessToken) {
        AuthCredential credential =FacebookAuthProvider.getCredential(accessToken.getToken());
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser myuserobj = autenticacao.getCurrentUser();
                            updateUI(myuserobj);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Usuario não registrado no banco de dados",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //IDENTIFICA QUE O USUARIO ESTA CONECTADO AO FACEBOOK E MANDA ELE PRO MENU
    private void updateUI(FirebaseUser myuserobj) {
        if(autenticacao.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
            finish();
        }
    }
    //FIM DA SEGUNDA PARTE DO FACEBOOK
}

