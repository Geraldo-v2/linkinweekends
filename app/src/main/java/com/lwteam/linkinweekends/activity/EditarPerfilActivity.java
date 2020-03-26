package com.lwteam.linkinweekends.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.UsuarioFirebase;
import com.lwteam.linkinweekends.model.Usuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto,editEmailPerfil;
    private TextInputEditText editNomePerfil;
    private Usuario usuarioLogado;
    private Button btSalvarAlteracoes,btExcluirPerfil;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageRef;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_perfil);

        usuarioLogado   = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        //CONFIGURA E CARREGA A TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_actionbar_24dp);

        inicializarComponentes();
        //RECUPERA DADOS DO PERFIL CONECTADO AO APLICATIVO
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        editEmailPerfil.setText(usuarioPerfil.getEmail());
        //CARREGA A FOTO DO USUARIO
        Uri url = usuarioPerfil.getPhotoUrl();
        if(url!=null){
            Glide.with(EditarPerfilActivity.this)
                   .load(url)
                   .into(imageEditarPerfil);
        }else {
            imageEditarPerfil.setImageResource(R.drawable.avatarperfil);
        }
        //SALVA OS DADOS DA ALTERACAO
            btSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomeAtualizado = editNomePerfil.getText().toString();

                    UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                    usuarioLogado.setNome(nomeAtualizado);
                    usuarioLogado.atualizar();

                    Toast.makeText(EditarPerfilActivity.this,
                            "Dados alterados com sucesso!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        //SELECIONA A FOTO NA GALERIA
        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
        //AINDA NÃO IMPLEMENTADO
        btExcluirPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //UPLOAD DA IMAGEM SELECIONADA NO BANCO FIREBASE
    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if (imagem != null) {
                    imageEditarPerfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageRef
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPerfilActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String firebaseUrl =uri.toString();
                                    String urlConvertida = firebaseUrl;

                                    UsuarioFirebase.atualizarFotoUsuario(uri);

                                    usuarioLogado.setCaminhoFoto(urlConvertida);
                                    usuarioLogado.atualizar();
                                    Toast.makeText(EditarPerfilActivity.this,
                                            "Sua foto foi atualizada!",
                                            Toast.LENGTH_SHORT).show();
                                }

                            });
                        }
                    });
                    Toast.makeText(EditarPerfilActivity.this,
                            "Sucesso ao fazer upload da imagem",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //FIM DO UPLOAD DE IMAGEM DE PERFIL NO FIREBASE - FACEBOOK AINDA NAO FUNCIONAL

    //INICIALIZA E CONECTA AO XML (FRONT)
    public void inicializarComponentes(){

        imageEditarPerfil   = findViewById(R.id.imgEditarPerfil);
        textAlterarFoto     = findViewById(R.id.txtAlterarFotoPerfil);

        editNomePerfil      = findViewById(R.id.txtAlterarNomePerfil);
        editEmailPerfil     = findViewById(R.id.txtAlterarEmailPerfil);

        btSalvarAlteracoes  = findViewById(R.id.btnSalvarPerfil);
        btExcluirPerfil     = findViewById(R.id.btnExcluirPerfil);
    }

    //COMANDO QUE FECHA A TELA QUANDO CLICA NO X PRESENTE NA TOOLBAR
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(),PerfilActivity.class));
        finish();
        return false;
    }
}
