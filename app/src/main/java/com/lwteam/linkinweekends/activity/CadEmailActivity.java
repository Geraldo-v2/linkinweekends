package com.lwteam.linkinweekends.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flod.loadingbutton.LoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.UsuarioFirebase;
import com.lwteam.linkinweekends.model.Usuario;

public class CadEmailActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private LoadingButton btCadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BARRA DE NOTIFICACOES TRANSPARENTE
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //FIM DA BARRA DE NOTIFICACOES TRANSPARENTE
        setContentView(R.layout.activity_cad_email);
        getSupportActionBar().hide();

        inicializarComponentes();

        //CADASTRO DO USUARIO VIA EMAIL
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                                usuario = new Usuario();
                                usuario.setNome(textoNome);
                                usuario.setEmail(textoEmail);
                                usuario.setSenha(textoSenha);
                                cadastrarUsuario(usuario);

                        }else{
                            Toast.makeText(CadEmailActivity.this,"Preencha a senha!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadEmailActivity.this,"Preencha o email!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadEmailActivity.this,"Preencha o nome!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //METODO RESPONSAVEL POR CADASTRAR E VALIDAR OS DADOS
    public void cadastrarUsuario(final Usuario usuario){

        btCadastrar.start();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            try {

                                //SALVAR DADOS DO USUARIO NO FIREBASE
                                String idUsuario = task.getResult().getUser().getUid();
                                usuario.setId(idUsuario);
                                usuario.salvar();

                                //SALVAR DADOS NO PROFILE DO FIREBASE
                                UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());


                                btCadastrar.complete();
                                Toast.makeText(CadEmailActivity.this,"Cadastro com sucesso",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                                finish();

                            }catch (Exception e ){
                                e.printStackTrace();
                            }

                        } else {
                            //FILTROS
                            btCadastrar.fail();

                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "Por favor, digite um email válido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(CadEmailActivity.this, "Erro: "+erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    //INICIALIZA COMPONENTES DA TELA
    public void inicializarComponentes(){

        //ANIMACOES DA TELA (SIM AINDA TENHO QUE RETIRAR DAQUI, MAS NESSA MERDA FICA NO BACKEND
        Animation animacaocimapbaixo1000;
        Animation animacaobaixopcima2000;
        Animation animacaobaixopcima4000;

        animacaocimapbaixo1000 = AnimationUtils.loadAnimation(this,R.anim.animacaocimapbaixo1000);
        animacaobaixopcima2000 = AnimationUtils.loadAnimation(this,R.anim.animacaobaixopcima2000);
        animacaobaixopcima4000 = AnimationUtils.loadAnimation(this,R.anim.animacaobaixopcima4000);

        ImageView imgLogoCadEmail    = findViewById(R.id.imgLogoCadEmail);
        ImageView imgFundoSol        = findViewById(R.id.imgFundoSolCadEmail);
        ImageView imgFundoMontanha1  = findViewById(R.id.imgFundoMontanha1CadEmail);
        ImageView imgFundoNuvem1     = findViewById(R.id.imgFundoNuvem1CadEmail);
        ImageView imgFundoNuvem2     = findViewById(R.id.imgFundoNuvem2CadEmail);
        ImageView imgFundoBarraca1   = findViewById(R.id.imgFundoBarraca1CadEmail);
        ImageView imgFundoBarraca2   = findViewById(R.id.imgFundoBarraca2CadEmail);
        //FINAL DA CONEXAO DO XML DA ANIMACAO

        campoNome   = findViewById(R.id.txtCadNome);
        campoEmail  = findViewById(R.id.txtCadEmail);
        campoSenha  = findViewById(R.id.txtCadSenha);
        btCadastrar = findViewById(R.id.btnCadastrar);
        campoNome.requestFocus();

        //ANIMACOES SENDO ATIVADAS NO XML
        imgLogoCadEmail.setAnimation(animacaocimapbaixo1000);
        campoNome.setAnimation(animacaocimapbaixo1000);
        campoEmail.setAnimation(animacaocimapbaixo1000);
        campoSenha.setAnimation(animacaocimapbaixo1000);
        btCadastrar.setAnimation(animacaocimapbaixo1000);

        imgFundoMontanha1.setAnimation(animacaobaixopcima2000);
        imgFundoSol.setAnimation(animacaobaixopcima4000);
        imgFundoNuvem1.setAnimation(animacaobaixopcima4000);
        imgFundoNuvem2.setAnimation(animacaobaixopcima4000);
        imgFundoBarraca1.setAnimation(animacaobaixopcima2000);
        imgFundoBarraca2.setAnimation(animacaobaixopcima2000);
        //FIM DA CONEXAO DAS ANIMACOES
    }

}
