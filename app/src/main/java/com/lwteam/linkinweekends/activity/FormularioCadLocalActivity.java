package com.lwteam.linkinweekends.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
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

public class FormularioCadLocalActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNomeLocal, campoDescricao,campoCustos,campoLongitude,campoLatitude;
    private EditText campoHoraUtilAbre,campoHoraUtilFecha,campoHoraSabadoAbre,campoHoraSabadoFecha,campoHoraDomingoAbre,campoHoraDomingoFecha;
    private EditText campoLocalEmail,campoLocaltelefone;
    private EditText campoRua,campoNumero,campoBairro,campoCep;
    private Spinner campoEstado, campoRegiao, campoCidade;
    private ImageView imagem1,imagem2,imagem3,imagem4,imagem5,imagem6,imagem7,imagem8,imagem9,imagem10;
    private Locais anunciolocal;
    private StorageReference storage,refphoto;
    private AlertDialog dialog;
    private Button btCadastroLocal;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private List<String>listaFotosRecuperadas = new ArrayList<>();
    private List<String>listaURLFotos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cad_local);

        //CONFIGURACOES INICIAIS
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        //VALIDAR PERMISSOES
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();
        carregarDadosSpinner();
    }
    public void salvarLocal() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Local")
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
                .child("imagenslocal")
                .child(anunciolocal.getIdLocal())
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
                            anunciolocal.setFotos(listaURLFotos);
                            anunciolocal.salvar();

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
    //RECUPERA OS TEXTOS PRA SUBIR AO FIREBASE "GETTEXT"
    private Locais configurarAnuncioLocal( ){
        String estado = campoEstado.getSelectedItem().toString();
        String regiao = campoRegiao.getSelectedItem().toString();
        String cidade = campoCidade.getSelectedItem().toString();

        String nome             = campoNomeLocal.getText().toString();
        String descricao        = campoDescricao.getText().toString();
        String custos           = campoCustos.getText().toString();
        String horaUtilAbre     = campoHoraUtilAbre.getText().toString();
        String horaUtilFecha    = campoHoraUtilFecha.getText().toString();
        String horaSabadoAbre   = campoHoraSabadoAbre.getText().toString();
        String horaSabadoFecha  = campoHoraSabadoFecha.getText().toString();
        String horaDomingoAbre  = campoHoraDomingoAbre.getText().toString();
        String horaDomingoFecha = campoHoraDomingoFecha.getText().toString();
        String latitude         = campoLatitude.getText().toString();
        String longitude        = campoLongitude.getText().toString();
        String email            = campoLocalEmail.getText().toString();
        String telefone         = campoLocaltelefone.getText().toString();
        String rua              = campoRua.getText().toString();
        String numero           = campoNumero.getText().toString();
        String bairro           = campoBairro.getText().toString();
        String CEP              = campoCep.getText().toString();

        Locais anunciolocal = new Locais();

        anunciolocal.setEstado(estado);
        anunciolocal.setRegiao(regiao);
        anunciolocal.setCidade(cidade);

        anunciolocal.setNome(nome);
        anunciolocal.setNomepesquisa(nome);
        anunciolocal.setDescricao(descricao);
        anunciolocal.setCustos(custos);
        anunciolocal.setHoraAbreUtil(horaUtilAbre);
        anunciolocal.setHoraFechaUtil(horaUtilFecha);
        anunciolocal.setHoraAbreSabado(horaSabadoAbre);
        anunciolocal.setHoraFechaSabado(horaSabadoFecha);
        anunciolocal.setHoraAbreDomingo(horaDomingoAbre);
        anunciolocal.setHoraFechaDomingo(horaDomingoFecha);
        anunciolocal.setLatitude(latitude);
        anunciolocal.setLongitude(longitude);
        anunciolocal.setEmailLocal(email);
        anunciolocal.setTelefoneLocal(telefone);
        anunciolocal.setRua(rua);
        anunciolocal.setNumero(numero);
        anunciolocal.setBairro(bairro);
        anunciolocal.setCEP(CEP);
        return anunciolocal;
    }

    //VALIDA SE TODOS OS CAMPOS ESTAO PREENCHIDOS - ISSO MELHORARIA OS IF'S USANDO UMA INTERFACE WEB
    //PARA SUBIR TODOS OS DADOS - GERALDO
    public void validarDadosLocais(){
        anunciolocal = configurarAnuncioLocal();
        //FILTROS DE VERIFICACAO
        if(listaFotosRecuperadas.size()>2){
            if(!anunciolocal.getEstado().isEmpty()){
                if(!anunciolocal.getRegiao().isEmpty()) {
                    if(!anunciolocal.getCidade().isEmpty()) {
                        if (!anunciolocal.getNome().isEmpty()) {
                            if (!anunciolocal.getDescricao().isEmpty()) {
                                if (!anunciolocal.getCustos().isEmpty()) {
                                    if (!anunciolocal.getHoraAbreUtil().isEmpty()) {
                                        if(!anunciolocal.getHoraFechaUtil().isEmpty()) {
                                            if(!anunciolocal.getHoraAbreSabado().isEmpty()) {
                                                if(!anunciolocal.getHoraFechaSabado().isEmpty()) {
                                                    if(!anunciolocal.getHoraAbreDomingo().isEmpty()) {
                                                        if ((!anunciolocal.getHoraFechaDomingo().isEmpty())) {
                                                            if (!anunciolocal.getLatitude().isEmpty()) {
                                                                if (!anunciolocal.getLongitude().isEmpty()) {
                                                                    if (!anunciolocal.getEmailLocal().isEmpty()){
                                                                        if(!anunciolocal.getTelefoneLocal().isEmpty()){
                                                                            if(!anunciolocal.getRua().isEmpty()){
                                                                                if(!anunciolocal.getNumero().isEmpty()){
                                                                                    if(!anunciolocal.getBairro().isEmpty()){
                                                                                        if(!anunciolocal.getCEP().isEmpty()){
                                                                                            salvarLocal();
                                                                                        }else {
                                                                                            exibirMensagemErro("Preencha o campo CEP");
                                                                                        }
                                                                                    }else{
                                                                                        exibirMensagemErro("Preencha o campo Bairro");
                                                                                    }
                                                                                }else {
                                                                                    exibirMensagemErro("Preencha o campo Numero");
                                                                                }
                                                                            }else {
                                                                                exibirMensagemErro("Preencha o campo Rua");
                                                                            }
                                                                        }else {
                                                                            exibirMensagemErro("Preencha o campo Telefone");
                                                                        }
                                                                    }else {
                                                                        exibirMensagemErro("Preencha o campo Email!");
                                                                    }
                                                                } else {
                                                                    exibirMensagemErro("Preencha o campo Longitude!");
                                                                }
                                                            } else {
                                                                exibirMensagemErro("Preencha o campo Latitude!");
                                                            }
                                                        }else {
                                                            exibirMensagemErro("Preencha o horario que fecha aos domingos");
                                                        }
                                                    }else {
                                                        exibirMensagemErro("Preencha o horario que abre aos domingos!");
                                                    }
                                                }else {
                                                    exibirMensagemErro("Preencha o horario que fecha aos sábados!");
                                                }
                                            }else{
                                                exibirMensagemErro("Preencha o horario que abre aos sábados!");
                                            }
                                        }else {
                                            exibirMensagemErro("Preencha o horario que fecha em dias uteis!");
                                        }
                                    } else {
                                        exibirMensagemErro("Preencha o horario que abre em dias uteis!");
                                    }
                                } else {
                                    exibirMensagemErro("Preencha o campo custos!");
                                }
                            } else {
                                exibirMensagemErro("Preencha o campo descrição!");
                            }
                        } else {
                            exibirMensagemErro("Preencha o campo nome!");
                        }
                    }else {
                        exibirMensagemErro("Escolha uma Cidade");
                    }
                }else{
                    exibirMensagemErro("Escolha uma Região");
                }
            }else {
                exibirMensagemErro("Escolha um Estado");
            }
        }else{
            exibirMensagemErro("Selecione mais de 2 fotos!");
        }

    }
    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }
    //LISTA DE IMAGENS PARA O LOCAL POSSIVEL ADICIONAR 10, MAIS QUE ISSO LIMITADO AO SISTEMA
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCad1:
                escolherImagem(1);
                break;
            case R.id.imgCad2:
                escolherImagem(2);
                break;
            case R.id.imgCad3:
                escolherImagem(3);
                break;
            case R.id.imgCad4:
                escolherImagem(4);
                break;
            case R.id.imgCad5:
                escolherImagem(5);
                break;
            case R.id.imgCad6:
                escolherImagem(6);
                break;
            case R.id.imgCad7:
                escolherImagem(7);
                break;
            case R.id.imgCad8:
                escolherImagem(8);
                break;
            case R.id.imgCad9:
                escolherImagem(9);
                break;
            case R.id.imgCad10:
                escolherImagem(10);
                break;
        }
    }
    //SELECIONA AS IMAGENS E FAZ REQUISICAO DE ACESSO A GALERIA
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
                imagem1.setImageURI(imagemSelecionada);
            }else if(requestCode==2){
                imagem2.setImageURI(imagemSelecionada);
            }else if(requestCode==3){
                imagem3.setImageURI(imagemSelecionada);
            }else if(requestCode==4){
                imagem4.setImageURI(imagemSelecionada);
            }else if(requestCode==5){
                imagem5.setImageURI(imagemSelecionada);
            }else if(requestCode==6){
                imagem6.setImageURI(imagemSelecionada);
            }else if(requestCode==7){
                imagem7.setImageURI(imagemSelecionada);
            }else if(requestCode==8){
                imagem8.setImageURI(imagemSelecionada);
            }else if(requestCode==9){
                imagem9.setImageURI(imagemSelecionada);
            }else if(requestCode==10){
                imagem10.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }
    //CARREGA A LISTA DE ESTADOS PARA O CADASTRO (PRONTO PARA O FUTURO POR ENQUANTO SÓ FUNCIONA SP)
    //CARREGA AS REGIOES PARA CADASTRO (DDD DO ESTADO DE SP DO DDD11 AO DDD19 PRONTO PARA O FUTURO)
    //SOMENTE DDD15 CONFIGURADO COM AS CIDADES
    public void carregarDadosSpinner(){
        String[] estados =getResources().getStringArray(R.array.estados);
        ArrayAdapter<String>adapterE=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,
                estados
        );
        adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoEstado.setAdapter(adapterE);

        String[] regioes =getResources().getStringArray(R.array.regioesSP);
        ArrayAdapter<String>adapterR=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,
                regioes
        );
        adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoRegiao.setAdapter(adapterR);

        String[] cidades =getResources().getStringArray(R.array.ddd15);
        ArrayAdapter<String>adapterC=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,
                cidades
        );
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCidade.setAdapter(adapterC);

    }

    //CONECTA A PORRA TODA COM O FRONT (XML)
    public void inicializarComponentes() {

        btCadastroLocal = findViewById(R.id.btnConfCadLocal);
        btCadastroLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDadosLocais();
            }
        });


        campoEstado     = findViewById(R.id.spinnerEstado);
        campoRegiao     = findViewById(R.id.spinnerRegiao);
        campoCidade     = findViewById(R.id.spinnerCidade);
        campoNomeLocal  = findViewById(R.id.txtCadLocalNome);
        campoDescricao  = findViewById(R.id.txtCadLocalSobre);
        campoCustos     = findViewById(R.id.txtCadLocalCusto);

        campoHoraUtilAbre       = findViewById(R.id.txtCadHoraAbreUtil);
        campoHoraUtilFecha      = findViewById(R.id.txtCadHoraFechaUtil);
        campoHoraSabadoAbre     = findViewById(R.id.txtCadHoraAbreSabado);
        campoHoraSabadoFecha    = findViewById(R.id.txtCadHoraFechaSabado);
        campoHoraDomingoAbre    = findViewById(R.id.txtCadHoraAbreDomingo);
        campoHoraDomingoFecha   = findViewById(R.id.txtCadHoraFechaDomingo);

        campoLatitude           = findViewById(R.id.txtCadLati);
        campoLongitude          = findViewById(R.id.txtCadLong);
        campoLocalEmail         = findViewById(R.id.txtCadLocalEmail);
        campoLocaltelefone      = findViewById(R.id.txtCadLocalTelefone);
        campoRua                = findViewById(R.id.txtCadLocalRua);
        campoNumero             = findViewById(R.id.txtCadLocalNumero);
        campoBairro             = findViewById(R.id.txtCadLocalBairro);
        campoCep                = findViewById(R.id.txtCadLocalCep);

        imagem1=findViewById(R.id.imgCad1);
        imagem2=findViewById(R.id.imgCad2);
        imagem3=findViewById(R.id.imgCad3);
        imagem4=findViewById(R.id.imgCad4);
        imagem5=findViewById(R.id.imgCad5);
        imagem6=findViewById(R.id.imgCad6);
        imagem7=findViewById(R.id.imgCad7);
        imagem8=findViewById(R.id.imgCad8);
        imagem9=findViewById(R.id.imgCad9);
        imagem10=findViewById(R.id.imgCad10);

        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);
        imagem4.setOnClickListener(this);
        imagem5.setOnClickListener(this);
        imagem6.setOnClickListener(this);
        imagem7.setOnClickListener(this);
        imagem8.setOnClickListener(this);
        imagem9.setOnClickListener(this);
        imagem10.setOnClickListener(this);
    }

    //VALIDA AS PERMISSOES DE ACESSO A GALERIA
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
