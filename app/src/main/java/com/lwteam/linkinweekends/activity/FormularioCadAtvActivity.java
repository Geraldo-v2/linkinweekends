package com.lwteam.linkinweekends.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.Permissoes;
import com.lwteam.linkinweekends.model.Locais;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FormularioCadAtvActivity extends AppCompatActivity implements View.OnClickListener{

    private Locais localSelecionado;

    private EditText campoNomeAtv, campoDescricaoAtv,campoCustosAtv;
    private EditText campoHoraAtvUtilAbre,campoHoraAtvUtilFecha,campoHoraAtvSabadoAbre,campoHoraAtvSabadoFecha,campoHoraAtvDomingoAbre,campoHoraAtvDomingoFecha;
    private Spinner campoCategoriaAtv, campoDificuldadeAtv;
    private ImageView imagemAtv1,imagemAtv2,imagemAtv3,imagemAtv4,imagemAtv5;
    private Locais anunciolocal;
    private StorageReference storage;
    private AlertDialog dialog;
    private Button btCadastroAtv;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private List<String>listaFotosRecuperadas = new ArrayList<>();
    private List<String>listaURLFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cad_atv);

        //CONFIGURACOES INICIAIS
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        //RECUPERAR DADOS DOS LOCAIS PARA EXIBICAO
        localSelecionado = (Locais) getIntent().getSerializableExtra("localSelecionado");

        //VALIDAR PERMISSOES
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();
        carregarDadosSpinner();

        //RECUPERAR DADOS DOS LOCAIS PARA EXIBICAO
        localSelecionado = (Locais) getIntent().getSerializableExtra("localSelecionado");

        if(localSelecionado !=null){
            getSupportActionBar().setTitle(localSelecionado.getNome());
        }
    }
    public void salvarAtividade() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Atividade")
                .setCancelable(false)
                .build();
        dialog.show();

        //SALVAR IMAGENS NO STORAGE
        for (int i = 0; i < listaFotosRecuperadas.size();i++) {
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
            salvarFotoStorage(urlImagem,tamanhoLista,i);
        }
    }
    private void salvarFotoStorage(String urlString, final int totalFotos, int contador){

        //CRIA AS IMAGENS NO STORAGE
        final StorageReference imagemAnuncio = storage.child("imagens")
                .child("locais")
                .child("imagensatividades")
                .child(anunciolocal.getIdAtividade())
                .child("imagem"+contador);

        //FAZ O UPLOAD DOS ARQUIVOS DE IMAGEM
        imagemAnuncio.putFile(Uri.parse(urlString)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemAnuncio.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String firebaseUrl =uri.toString();
                        String urlConvertida = firebaseUrl;

                        listaURLFotos.add(urlConvertida);
                        if (totalFotos == listaURLFotos.size()) {
                            anunciolocal.setFotosAtividade(listaURLFotos);
                            anunciolocal.salvarAtividade();

                            dialog.dismiss();
                            finish();
                        }
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exibirMensagemErro("Falha ao fazer upload");
                Log.i("INFO","Falha ao fazer upload: "+e.getMessage());
            }
        });
    }
    // RECUPERA OS DADOS INFORMADOS NOS CAMPOS PARA SUBIR NOS "NOS" DO FIREBASE
    private Locais configurarAnuncioAtividade( ){

        String categoriaAtv        = campoCategoriaAtv.getSelectedItem().toString();
        String dificuldadeAtv      = campoDificuldadeAtv.getSelectedItem().toString();
        String nomeAtv             = campoNomeAtv.getText().toString();
        String descricaoAtv        = campoDescricaoAtv.getText().toString();
        String custosAtv           = campoCustosAtv.getText().toString();
        String horaUtilAbreAtv     = campoHoraAtvUtilAbre.getText().toString();
        String horaUtilFechaAtv    = campoHoraAtvUtilFecha.getText().toString();
        String horaSabadoAbreAtv   = campoHoraAtvSabadoAbre.getText().toString();
        String horaSabadoFechaAtv  = campoHoraAtvSabadoFecha.getText().toString();
        String horaDomingoAbreAtv  = campoHoraAtvDomingoAbre.getText().toString();
        String horaDomingoFechaAtv = campoHoraAtvDomingoFecha.getText().toString();

        Locais anunciolocal = new Locais();
        anunciolocal.setEstadosAtv(localSelecionado.getEstado());
        anunciolocal.setRegioesAtv(localSelecionado.getRegiao());
        anunciolocal.setCidadesAtv(localSelecionado.getCidade());
        anunciolocal.setIdLocalAtv(localSelecionado.getIdLocal());

        anunciolocal.setCategoriaAtividade(categoriaAtv);
        anunciolocal.setNivelDificuldadeAtividade(dificuldadeAtv);
        anunciolocal.setNomeAtividade(nomeAtv);
        anunciolocal.setDescricaoAtividade(descricaoAtv);
        anunciolocal.setCustosAtividade(custosAtv);
        anunciolocal.setHoraAbreUtilAtividade(horaUtilAbreAtv);
        anunciolocal.setHoraFechaUtilAtividade(horaUtilFechaAtv);
        anunciolocal.setHoraAbreSabadoAtividade(horaSabadoAbreAtv);
        anunciolocal.setHoraFechaSabadoAtividade(horaSabadoFechaAtv);
        anunciolocal.setHoraAbreDomingoAtividade(horaDomingoAbreAtv);
        anunciolocal.setHoraFechaDomingoAtividade(horaDomingoFechaAtv);

        return anunciolocal;
    }
    //FIM DO BLOCO

    public void validarDadosLocais() {
        anunciolocal = configurarAnuncioAtividade();
        //FILTROS DE VERIFICACAO
        if (listaFotosRecuperadas.size() > 0) {
            if(!anunciolocal.getNomeAtividade().isEmpty()) {
                salvarAtividade();
            } else{
                exibirMensagemErro("Preencha o campo nome da Atividade");
            }
        }else {
            exibirMensagemErro("Escolha uma foto");
        }
    }
    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }
    //ADICIONAR IMAGENS DA ATIVIDADE (SÃO 5)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCadAtv1:
                escolherImagem(1);
                break;
            case R.id.imgCadAtv2:
                escolherImagem(2);
                break;
            case R.id.imgCadAtv3:
                escolherImagem(3);
                break;
            case R.id.imgCadAtv4:
                escolherImagem(4);
                break;
            case R.id.imgCadAtv5:
                escolherImagem(5);
                break;
        }
    }
    //ABRIR A GALERIA PARA SELECIOBNAR IMAGEM, REQUISITADO PERMICAO EXTERNA DE MIDIA
    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){

            //RECUPERA A IMAGEM
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //CONFIGURA A IMAGEM NO IMAGEVIEW (PICASSO)
            if(requestCode==1){
                imagemAtv1.setImageURI(imagemSelecionada);
            }else if(requestCode==2){
                imagemAtv2.setImageURI(imagemSelecionada);
            }else if(requestCode==3){
                imagemAtv3.setImageURI(imagemSelecionada);
            }else if(requestCode==4){
                imagemAtv4.setImageURI(imagemSelecionada);
            }else if(requestCode==5) {
                imagemAtv5.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }
    // CARREGA A CATEGORIA DE OPCOES (TERRA, AGUA, AR)
    // CARREGA AS DIFICULDADES (FACIL) (MEDIO) (DIFICIL) (FACIL E MEDIO E DIFICIL)
    // (FACIL E MEDIO) (FACIL E DIFICIL) (MEDIO E DIFICL)
    public void carregarDadosSpinner(){
        String[] categoria =getResources().getStringArray(R.array.categorias);
        ArrayAdapter<String> adapterCad=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,
                categoria
        );
        adapterCad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoriaAtv.setAdapter(adapterCad);

        String[] dificuldade =getResources().getStringArray(R.array.dificuldades);
        ArrayAdapter<String>adapterDif=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,
                dificuldade
        );
        adapterDif.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoDificuldadeAtv.setAdapter(adapterDif);
    }
    //CONECTA COM OS CAMPOS DA XML
    private void inicializarComponentes(){
        btCadastroAtv = findViewById(R.id.btnCadAtividade);
        btCadastroAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDadosLocais();
            }
        });
        campoCategoriaAtv       = findViewById(R.id.spinnerCategoriaAtividade);
        campoDificuldadeAtv     = findViewById(R.id.spinnerDificuldadeAtividade);

        campoNomeAtv       = findViewById(R.id.txtCadAtividadeNome);
        campoDescricaoAtv  = findViewById(R.id.txtCadAtividadeSobre);
        campoCustosAtv     = findViewById(R.id.txtCadAtividadeCusto);

        campoHoraAtvUtilAbre       = findViewById(R.id.txtCadAtividadeHoraAbreUtil);
        campoHoraAtvUtilFecha      = findViewById(R.id.txtCadAtividadeHoraFechaUtil);
        campoHoraAtvSabadoAbre     = findViewById(R.id.txtCadAtividadeHoraAbreSabado);
        campoHoraAtvSabadoFecha    = findViewById(R.id.txtCadAtividadeHoraFechaSabado);
        campoHoraAtvDomingoAbre    = findViewById(R.id.txtCadAtividadeHoraAbreDomingo);
        campoHoraAtvDomingoFecha   = findViewById(R.id.txtCadAtividadeHoraFechaDomingo);

        imagemAtv1=findViewById(R.id.imgCadAtv1);
        imagemAtv2=findViewById(R.id.imgCadAtv2);
        imagemAtv3=findViewById(R.id.imgCadAtv3);
        imagemAtv4=findViewById(R.id.imgCadAtv4);
        imagemAtv5=findViewById(R.id.imgCadAtv5);

        imagemAtv1.setOnClickListener(this);
        imagemAtv2.setOnClickListener(this);
        imagemAtv3.setOnClickListener(this);
        imagemAtv4.setOnClickListener(this);
        imagemAtv5.setOnClickListener(this);
    }
    //PERMICOES PARA ACESSAR GALERIA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if(permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertavalidacaopermissao();
            }
        }
    }
    private void alertavalidacaopermissao(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
